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
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.resource.CashCourierTransactionAddResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.CashCourierTransactionService;

@RestController
@RequestMapping(value = "/cash-courier-transactions")
@CrossOrigin(origins = "*")
public class CashCourierTransactionController extends MessagePropertyBase{

	@Autowired
	private CashCourierTransactionService cashCourierTransactionService;
	
	@PostMapping(value = "/{tenantId}")
	public ResponseEntity<Object> saveCashCourierTransaction(@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "vaultId", required = true) Long vaultId,
			@Valid @RequestBody CashCourierTransactionAddResource cashCourierTransactionAddResource) {
		
		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new UserNotFound(environment.getProperty(USER_NOT_FOUND));
		
		Long id = cashCourierTransactionService.saveCashCourierDetails(tenantId, vaultId, cashCourierTransactionAddResource);
		
		SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(RECORD_CREATED), id.toString());
		return new ResponseEntity<>(successDetailsDto,HttpStatus.CREATED);
	}
	
	
}
