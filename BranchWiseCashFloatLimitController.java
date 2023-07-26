package com.fusionx.central.cash.management.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fusionx.central.cash.management.base.MessagePropertyBase;
import com.fusionx.central.cash.management.core.LogginAuthentcation;
import com.fusionx.central.cash.management.domain.BranchWiseCashLimitHeader;
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.resource.BranchWiseCashLimitAddResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.BranchWiseCashLimitService;

@RestController
@RequestMapping(value = "/branch-wise-cash-limit")
@CrossOrigin(origins = "*")
public class BranchWiseCashFloatLimitController extends MessagePropertyBase{
	
	private String userNotFound = "common.user-not-found";

	private String commonSaved = "common.saved";

	private String recordNotFound = "common.record-not-found";
	
	@Autowired
	private BranchWiseCashLimitService branchWiseCashLimitService;
	
	@PostMapping(value = "/{tenantId}/vault-id/{vaultId}")
	public ResponseEntity<Object> addBranchTellerCurrencies(
			@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "vaultId", required = true) Long vaultId,
			@Valid @RequestBody BranchWiseCashLimitAddResource branchWiseCashLimitAddResource) {
		
		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty()) {
			throw new UserNotFound(environment.getProperty(userNotFound));
		}

		BranchWiseCashLimitHeader branchWiseCashLimitHeader = branchWiseCashLimitService.save(tenantId, branchWiseCashLimitAddResource);
		SuccessAndErrorDetailsResource successAndErrorDetails = new SuccessAndErrorDetailsResource(environment.getProperty(commonSaved),
				Long.toString(branchWiseCashLimitHeader.getId()));
		return new ResponseEntity<>(successAndErrorDetails, HttpStatus.CREATED);

	}

}
