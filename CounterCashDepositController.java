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
import com.fusionx.central.cash.management.resource.CounterCashDepositAddResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.CounterCashDepositService;

@RestController
@RequestMapping("/counter-cash-deposit")
@CrossOrigin("*")
public class CounterCashDepositController extends MessagePropertyBase{
	
	@Autowired
	private CounterCashDepositService counterCashDepositService;
	
	/**
	 * Adds the counter cash.
	 *
	 * @param tenantId the tenant id
	 * @param counterCashDepositAddResource the counter cash deposit add resource
	 * @return the response entity
	 */
	@PostMapping(value = "/{tenantId}")
	public ResponseEntity<Object> addCounterCash(@PathVariable(value = "tenantId", required = true) String tenantId,
			@Valid @RequestBody CounterCashDepositAddResource counterCashDepositAddResource) {
		
		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new UserNotFound(environment.getProperty(USER_NOT_FOUND));
		
		Long id = counterCashDepositService.save(tenantId, counterCashDepositAddResource);
		
		SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(RECORD_CREATED), id.toString());
		return new ResponseEntity<>(successDetailsDto,HttpStatus.CREATED);
	}
	

}



