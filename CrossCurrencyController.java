package com.fusionx.central.cash.management.controller;

import com.querydsl.core.types.Predicate;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fusionx.central.cash.management.base.MessagePropertyBase;
import com.fusionx.central.cash.management.core.LogginAuthentcation;
import com.fusionx.central.cash.management.domain.CrossCurrency;
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.resource.CrossCurrencyAddResource;
import com.fusionx.central.cash.management.resource.CrossCurrencyUpdateResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.CrossCurrencyService;

@RestController
@RequestMapping("/cross-cur")
@CrossOrigin("*")
public class CrossCurrencyController extends MessagePropertyBase{
	
	@Autowired
	private CrossCurrencyService crossCurrencyService;
	
	/**
	 * get all records
	 * @param @PathVariable{tenantId}
	 * @param @PathVariable{all}
	 * @return List
	 **/
	@GetMapping("/{tenantId}/all")
	public ResponseEntity<Object> findAllCrossCurrency(@PathVariable(value = "tenantId", required = true) String tenantId,
													  @PageableDefault(size = 10) Pageable pageable,
													  @QuerydslPredicate(root = CrossCurrency.class) Predicate predicate){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		Page<CrossCurrency> crossCurrency = crossCurrencyService.findall(pageable,predicate);
		if(crossCurrency.hasContent()) {
			return new ResponseEntity<>(crossCurrency,HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * by status
	 * @param @PathVariable{tenantId}
	 * @param @PathVariable{status}
	 * @return List
	 **/
	@GetMapping("/{tenantId}/status/{status}")
	public ResponseEntity<Object> findByStatus(@PathVariable(value = "tenantId", required = true) String tenantId,@PathVariable(value = "status", required = true) String status,
													  @PageableDefault(size = 10) Pageable pageable,
													  @QuerydslPredicate(root = CrossCurrency.class) Predicate predicate){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		List<CrossCurrency> crossCurrencyList = crossCurrencyService.findByStatus(status);
		if(!crossCurrencyList.isEmpty()) {
			return new ResponseEntity<>(crossCurrencyList,HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * by id
	 * @param @PathVariable{tenantId}
	 * @param @PathVariable{id}
	 * @return crossCurrency
	 **/
	@GetMapping("/{tenantId}/{id}")
	public ResponseEntity<Object> findById(@PathVariable(value = "tenantId", required = true) String tenantId,@PathVariable(value = "id", required = true) String id,
													  @PageableDefault(size = 10) Pageable pageable,
													  @QuerydslPredicate(root = CrossCurrency.class) Predicate predicate){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		CrossCurrency crossCurrency = crossCurrencyService.findById(Long.parseLong(id));
		if(crossCurrency!=null) {
			return new ResponseEntity<>(crossCurrency,HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * Add CrossCurrency.
	 *
	 * @param tenantId - the tenant id
	 * @param crossCurrencyAddResource
	 * @return the response entity
	 */
	@PostMapping(value = "/{tenantId}")
	public ResponseEntity<Object> addCrossCurrency(@PathVariable(value = "tenantId", required = true) String tenantId,
			@Valid @RequestBody CrossCurrencyAddResource crossCurrencyAddResource) {
		
		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new UserNotFound(environment.getProperty(USER_NOT_FOUND));
		
		Long id = crossCurrencyService.addCrossCurrency(tenantId, LogginAuthentcation.getUserName(), crossCurrencyAddResource);
		
		SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(RECORD_CREATED), id.toString());
		return new ResponseEntity<>(successDetailsDto,HttpStatus.CREATED);
	}
	
	/**
	 * Update CrossCurrency.
	 *
	 * @param tenantId - the tenant id
	 * @param roundOffLimitUpdateResource - the round Off Limit Resource
	 * @return the response entity
	 */
	@PutMapping(value = "{tenantId}/{id}")
	public ResponseEntity<Object> updateCrossCurrency(@PathVariable(value = "tenantId", required = true) String tenantId,@PathVariable(value = "id", required = true) String id,
			@Valid @RequestBody CrossCurrencyUpdateResource crossCurrencyUpdateResource) {
		
		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new UserNotFound(environment.getProperty(USER_NOT_FOUND));
		
		SuccessAndErrorDetailsResource successAndErrorDetailsResource=new SuccessAndErrorDetailsResource();
		CrossCurrency crossCurrency = crossCurrencyService.findById(Long.parseLong(id));
		
		if(crossCurrency!=null) {
			crossCurrencyUpdateResource.setId(id.toString());
			CrossCurrency crossCurrencyResponse = crossCurrencyService.updateCrossCurrency(tenantId, crossCurrencyUpdateResource);
			successAndErrorDetailsResource = new SuccessAndErrorDetailsResource(getEnvironment().getProperty(RECORD_UPDATED), crossCurrencyResponse.getId().toString());
			return new ResponseEntity<>(successAndErrorDetailsResource,HttpStatus.OK);
		}else {
			successAndErrorDetailsResource.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

}
