package com.fusionx.central.cash.management.controller;

import java.util.List;

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
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.resource.TellerDayEndResource;
import com.fusionx.central.cash.management.resource.TellerDayEndSendResource;
import com.fusionx.central.cash.management.resource.TellerDenominationResource;
import com.fusionx.central.cash.management.service.TellerDayEndService;

@RestController
@RequestMapping("/teller-day-end")
@CrossOrigin("*")
public class TellerDayEndController extends MessagePropertyBase{
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private TellerDayEndService tellerDayEndService;
	
	private String recordNotFound = "common.record-not-found";

	@GetMapping(value = "/{tenantId}/{tellerId}")
	public ResponseEntity<Object> getTellerDayEndList(@PathVariable(value = "tenantId", required = true) String tenantId,@PathVariable(value = "tellerId", required = true) String tellerId) {
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		TellerDayEndResource dayEnd = tellerDayEndService.findTellerDayEnd(Long.parseLong(tellerId));
		if (dayEnd!=null) {
			return new ResponseEntity<>(dayEnd, HttpStatus.OK);
		} else {
			responseMessage.setMessages(environment.getProperty(recordNotFound));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping(value = "/{tenantId}/denomination/{tellerId}/currency/{currencyId}")
	public ResponseEntity<Object> getTellerDenomination(@PathVariable(value = "tenantId", required = true) String tenantId,@PathVariable(value = "tellerId", required = true) String tellerId,@PathVariable(value = "currencyId", required = true) String currencyId) {
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		List<TellerDenominationResource> denominationList = tellerDayEndService.findTellerDenomination(Long.parseLong(tellerId),Long.parseLong(currencyId));
		if (!denominationList.isEmpty()) {
			return new ResponseEntity<>(denominationList, HttpStatus.OK);
		} else {
			responseMessage.setMessages(environment.getProperty(recordNotFound));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * Add Teller Day End
	 *
	 * @param tenantId - the tenant id
	 * @param tellerDayEndSendResource
	 * @return the response entity
	 */
	@PostMapping(value = "/{tenantId}")
	public ResponseEntity<Object> addTellerDayEnd(@PathVariable(value = "tenantId", required = true) String tenantId,@Valid @RequestBody TellerDayEndSendResource tellerDayEndSendResource) {
		
		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new UserNotFound(environment.getProperty(USER_NOT_FOUND));
		
		tellerDayEndService.addTellerDayEnd(tenantId, LogginAuthentcation.getUserName(), tellerDayEndSendResource);
		
		SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(RECORD_CREATED));
		return new ResponseEntity<>(successDetailsDto,HttpStatus.CREATED);
	}

}
