package com.fusionx.central.cash.management.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.resource.CashDepositFromTellerAddResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.CashDepositFromTellerService;

@RestController
@RequestMapping(value = "/cash-deposit-from-teller")
@CrossOrigin(origins = "*")
public class CashDepositFromTellerController extends MessagePropertyBase{

	private String userNotFound = "common.user-not-found";

	private String commonSaved = "common.saved";

	@Autowired
	private Environment environment;

	@Autowired
	private CashDepositFromTellerService cashDepositFromTellerService;


	@PostMapping(value = "/{tenantId}")
	public ResponseEntity<Object> addCashTransferToTeller(
			@PathVariable(value = "tenantId", required = true) String tenantId,
			@Valid @RequestBody CashDepositFromTellerAddResource cashDepositFromTellerAddResource) {
		
		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new UserNotFound(environment.getProperty(userNotFound));
		
		Long id = cashDepositFromTellerService.saveCashDepositFromTeller(tenantId, cashDepositFromTellerAddResource);
		SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(
				environment.getProperty(commonSaved), id.toString());
		return new ResponseEntity<>(successDetailsDto, HttpStatus.CREATED);
	}

}
