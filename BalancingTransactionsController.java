package com.fusionx.central.cash.management.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fusionx.central.cash.management.base.MessagePropertyBase;
import com.fusionx.central.cash.management.core.LogginAuthentcation;
import com.fusionx.central.cash.management.domain.Transaction;
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.resource.BalancingTransactionCashInOutResponse;
import com.fusionx.central.cash.management.resource.DenominationDetailsAddResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.BalancingTransactionsService;

@RestController
@RequestMapping(value = "/balancing-transactions")
@CrossOrigin(origins = "*")
public class BalancingTransactionsController extends MessagePropertyBase {

	private String userNotFound = "common.user-not-found";

	private String commonSaved = "common.saved";

	@Autowired
	private Environment environment;

	@Autowired
	private BalancingTransactionsService balancingTransactionsService;

	private String recordNotFound = "common.record-not-found";

	@GetMapping(value = "/{tenantId}/vault-id/{id}")
	public ResponseEntity<Object> getTransactionsByVaultId(
			@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "id", required = true) Long id) {
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		BalancingTransactionCashInOutResponse finalResponse = balancingTransactionsService.findByVaultId(id);
		if (finalResponse != null) {
			return new ResponseEntity<>(finalResponse, HttpStatus.OK);
		} else {
			responseMessage.setMessages(environment.getProperty(recordNotFound));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}

	@PostMapping(value = "/{tenantId}/vault-id/{vaultId}")
	public ResponseEntity<Object> addBranchTellerCurrencies(
			@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "vaultId", required = true) Long vaultId,
			@Valid @RequestBody DenominationDetailsAddResource denominationDetailsAddResource) {
		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty()) {
			throw new UserNotFound(environment.getProperty(userNotFound));
		}

		Transaction transaction = balancingTransactionsService.saveDenominationDetails(tenantId, vaultId,denominationDetailsAddResource);
		SuccessAndErrorDetailsResource successAndErrorDetails = new SuccessAndErrorDetailsResource(environment.getProperty(commonSaved),
				Long.toString(transaction.getId()));
		return new ResponseEntity<>(successAndErrorDetails, HttpStatus.CREATED);

	}

}
