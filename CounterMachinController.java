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
import com.fusionx.central.cash.management.domain.CounterMachine;
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.resource.CounterMachineAddResource;
import com.fusionx.central.cash.management.resource.CounterMachineUpdateResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.CounterMachineService;


@RestController
@RequestMapping("/counter-machine")
@CrossOrigin("*")
public class CounterMachinController extends MessagePropertyBase{
	
	@Autowired
	private CounterMachineService counterMachineService;
	
	/**
	 * get all records
	 * @param @PathVariable{tenantId}
	 * @param @PathVariable{all}
	 * @return List
	 **/
	@GetMapping("/{tenantId}/all")
	public ResponseEntity<Object> findAllCounterMachine(@PathVariable(value = "tenantId", required = true) String tenantId,
													  @PageableDefault(size = 10) Pageable pageable,
													  @QuerydslPredicate(root = CounterMachine.class) Predicate predicate){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		Page<CounterMachine> counterMachine = counterMachineService.findall(pageable,predicate);
		if(counterMachine.hasContent()) {
			return new ResponseEntity<>(counterMachine,HttpStatus.OK);
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
													  @QuerydslPredicate(root = CounterMachine.class) Predicate predicate){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		List<CounterMachine> counterMachine = counterMachineService.findByStatus(status);
		if(!counterMachine.isEmpty()) {
			return new ResponseEntity<>(counterMachine,HttpStatus.OK);
		}else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * by id
	 * @param @PathVariable{tenantId}
	 * @param @PathVariable{id}
	 * @return counterMachine
	 **/
	@GetMapping("/{tenantId}/{id}")
	public ResponseEntity<Object> findById(@PathVariable(value = "tenantId", required = true) String tenantId,@PathVariable(value = "id", required = true) String id,
													  @PageableDefault(size = 10) Pageable pageable,
													  @QuerydslPredicate(root = CounterMachine.class) Predicate predicate){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		CounterMachine counterMachine = counterMachineService.findById(Long.parseLong(id));
		if(counterMachine!=null) {
			return new ResponseEntity<>(counterMachine,HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * Add CounterMachine.
	 *
	 * @param tenantId - the tenant id
	 * @param counterMachineAddResource
	 * @return the response entity
	 */
	@PostMapping(value = "/{tenantId}")
	public ResponseEntity<Object> addCounterMachine(@PathVariable(value = "tenantId", required = true) String tenantId,
			@Valid @RequestBody CounterMachineAddResource counterMachineAddResource) {
		
		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new UserNotFound(environment.getProperty(USER_NOT_FOUND));
		
		Long id = counterMachineService.addCounterMachine(tenantId, LogginAuthentcation.getUserName(), counterMachineAddResource);
		
		SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(RECORD_CREATED), id.toString());
		return new ResponseEntity<>(successDetailsDto,HttpStatus.CREATED);
	}
	
	/**
	 * Update CounterMachine.
	 *
	 * @param tenantId - the tenant id
	 * @param counterMachineUpdateResource
	 * @return the response entity
	 */
	@PutMapping(value = "{tenantId}/{id}")
	public ResponseEntity<Object> updateCounterMachine(@PathVariable(value = "tenantId", required = true) String tenantId,@PathVariable(value = "id", required = true) String id,
			@Valid @RequestBody CounterMachineUpdateResource counterMachineUpdateResource) {
		
		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new UserNotFound(environment.getProperty(USER_NOT_FOUND));
		
		SuccessAndErrorDetailsResource successAndErrorDetailsResource=new SuccessAndErrorDetailsResource();
		CounterMachine counterMachine = counterMachineService.findById(Long.parseLong(id));
		
		if(counterMachine!=null) {
			counterMachineUpdateResource.setId(id.toString());
			CounterMachine counterMachineResponse = counterMachineService.updateCounterMachine(tenantId, counterMachineUpdateResource);
			successAndErrorDetailsResource = new SuccessAndErrorDetailsResource(getEnvironment().getProperty(RECORD_UPDATED), counterMachineResponse.getId().toString());
			return new ResponseEntity<>(successAndErrorDetailsResource,HttpStatus.OK);
		}else {
			successAndErrorDetailsResource.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

}
