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
import com.fusionx.central.cash.management.domain.RoundOffLimit;
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.resource.RoundOffLimitAddResource;
import com.fusionx.central.cash.management.resource.RoundOffLimitUpdateResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.RoundOffLimitsService;


@RestController
@RequestMapping("/round-off-lmt")
@CrossOrigin("*")
public class RoundOffLimitController extends MessagePropertyBase{
	
	@Autowired
	private RoundOffLimitsService roundOffLimitsService;
	
	/**
	 * get all records
	 * @param @PathVariable{tenantId}
	 * @param @PathVariable{all}
	 * @return List
	 **/
	@GetMapping("/{tenantId}/all")
	public ResponseEntity<Object> findAllRoundOffLimit(@PathVariable(value = "tenantId", required = true) String tenantId,
													  @PageableDefault(size = 10) Pageable pageable,
													  @QuerydslPredicate(root = RoundOffLimit.class) Predicate predicate){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		Page<RoundOffLimit> roundOffLimitList = roundOffLimitsService.findall(pageable,predicate);
		if(roundOffLimitList.hasContent()) {
			return new ResponseEntity<>(roundOffLimitList,HttpStatus.OK);
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
													  @QuerydslPredicate(root = RoundOffLimit.class) Predicate predicate){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		List<RoundOffLimit> roundOffLimitList = roundOffLimitsService.findByStatus(status);
		if(!roundOffLimitList.isEmpty()) {
			return new ResponseEntity<>(roundOffLimitList,HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * by id
	 * @param @PathVariable{tenantId}
	 * @param @PathVariable{roundOffLmtId}
	 * @return roundOffLimit
	 **/
	@GetMapping("/{tenantId}/{roundOffLmtId}")
	public ResponseEntity<Object> findById(@PathVariable(value = "tenantId", required = true) String tenantId,@PathVariable(value = "roundOffLmtId", required = true) String roundOffLmtId,
													  @PageableDefault(size = 10) Pageable pageable,
													  @QuerydslPredicate(root = RoundOffLimit.class) Predicate predicate){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		RoundOffLimit roundOffLimit = roundOffLimitsService.findById(Long.parseLong(roundOffLmtId));
		if(roundOffLimit!=null) {
			return new ResponseEntity<>(roundOffLimit,HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * Add RoundOffLimit.
	 *
	 * @param tenantId - the tenant id
	 * @param roundOffLimitAddResource - the round Off Limit Resource
	 * @return the response entity
	 */
	@PostMapping(value = "/{tenantId}")
	public ResponseEntity<Object> addRoundOffLimit(@PathVariable(value = "tenantId", required = true) String tenantId,
			@Valid @RequestBody RoundOffLimitAddResource roundOffLimitAddResource) {
		
		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new UserNotFound(environment.getProperty(USER_NOT_FOUND));
		
		Long id = roundOffLimitsService.addRoundOffLimit(tenantId, LogginAuthentcation.getUserName(), roundOffLimitAddResource);
		
		SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(RECORD_CREATED), id.toString());
		return new ResponseEntity<>(successDetailsDto,HttpStatus.CREATED);
	}
	
	/**
	 * Update RoundOffLimit.
	 *
	 * @param tenantId - the tenant id
	 * @param roundOffLimitUpdateResource - the round Off Limit Resource
	 * @return the response entity
	 */
	@PutMapping(value = "{tenantId}/{id}")
	public ResponseEntity<Object> updateRoundOffLimit(@PathVariable(value = "tenantId", required = true) String tenantId,@PathVariable(value = "id", required = true) String id,
			@Valid @RequestBody RoundOffLimitUpdateResource roundOffLimitUpdateResource) {
		
		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new UserNotFound(environment.getProperty(USER_NOT_FOUND));
		
		SuccessAndErrorDetailsResource successAndErrorDetailsResource=new SuccessAndErrorDetailsResource();
		RoundOffLimit roundOffLimit = roundOffLimitsService.findById(Long.parseLong(id));
		
		if(roundOffLimit!=null) {
			roundOffLimitUpdateResource.setId(id.toString());
			RoundOffLimit roundOffLimitResponse = roundOffLimitsService.updateRoundOffLimit(tenantId, roundOffLimitUpdateResource);
			successAndErrorDetailsResource = new SuccessAndErrorDetailsResource(getEnvironment().getProperty(RECORD_UPDATED), roundOffLimitResponse.getId().toString());
			return new ResponseEntity<>(successAndErrorDetailsResource,HttpStatus.OK);
		}else {
			successAndErrorDetailsResource.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

}
