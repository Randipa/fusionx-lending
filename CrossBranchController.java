package com.fusionx.central.cash.management.controller;

import com.querydsl.core.types.Predicate;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.fusionx.central.cash.management.domain.CrossBranch;
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.resource.CrossBranchAddBodyRequestResource;
import com.fusionx.central.cash.management.resource.CrossBranchUpdateBodyRequestResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.CrossBranchService;

@RestController
@RequestMapping("/cross-brn")
@CrossOrigin("*")
public class CrossBranchController extends MessagePropertyBase{
	
	@Autowired
	private CrossBranchService crossBranchService;
	
	/**
	 * get all records
	 * @param @PathVariable{tenantId}
	 * @param @PathVariable{all}
	 * @return List
	 **/
	@GetMapping("/{tenantId}/all")
	public ResponseEntity<Object> findAllCrossCurrency(@PathVariable(value = "tenantId", required = true) String tenantId,
													  @PageableDefault(size = 10) Pageable pageable,
													  @QuerydslPredicate(root = CrossBranch.class) Predicate predicate){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		List<CrossBranch> crossBranchList = crossBranchService.findAll(tenantId);
		if(!crossBranchList.isEmpty()) {
			return new ResponseEntity<>(crossBranchList,HttpStatus.OK);
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
													  @QuerydslPredicate(root = CrossBranch.class) Predicate predicate){
		
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		List<CrossBranch> crossBranchList = crossBranchService.findByStatus(status,tenantId);
		if(!crossBranchList.isEmpty()) {
			return new ResponseEntity<>(crossBranchList,HttpStatus.OK);
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
	 * @return crossBranch
	 **/
	@GetMapping("/{tenantId}/{id}")
	public ResponseEntity<Object> findById(@PathVariable(value = "tenantId", required = true) String tenantId,@PathVariable(value = "id", required = true) String id,
													  @PageableDefault(size = 10) Pageable pageable,
													  @QuerydslPredicate(root = CrossBranch.class) Predicate predicate){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		CrossBranch crossBranch = crossBranchService.findById(Long.parseLong(id),tenantId);
		if(crossBranch!=null) {
			return new ResponseEntity<>(crossBranch,HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * Add CrossBranch.
	 *
	 * @param tenantId - the tenant id
	 * @param crossBranchAddResource
	 * @return the response entity
	 */
	@PostMapping(value = "/{tenantId}")
	public ResponseEntity<Object> addCrossBranch(@PathVariable(value = "tenantId", required = true) String tenantId,
			@Valid @RequestBody CrossBranchAddBodyRequestResource crossBranchAddBodyRequestResource) {
		
		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new UserNotFound(environment.getProperty(USER_NOT_FOUND));
		
		crossBranchService.addCrossBranch(tenantId, LogginAuthentcation.getUserName(), crossBranchAddBodyRequestResource);
		
		SuccessAndErrorDetailsResource successDetailsDto=new SuccessAndErrorDetailsResource(environment.getProperty("common.saved"));
		return new ResponseEntity<>(successDetailsDto,HttpStatus.CREATED);
	}
	
	/**
	 * Update CrossBranch.
	 *
	 * @param tenantId - the tenant id
	 * @param crossBranchUpdateBodyRequestResource 
	 * @return the response entity
	 */
	@PutMapping(value = "{tenantId}/update")
	public ResponseEntity<Object> updateCrossBranch(@PathVariable(value = "tenantId", required = true) String tenantId,
			@Valid @RequestBody CrossBranchUpdateBodyRequestResource crossBranchUpdateBodyRequestResource) {
		
		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new UserNotFound(environment.getProperty(USER_NOT_FOUND));
		
		crossBranchService.updateCrossBranch(tenantId,LogginAuthentcation.getUserName(), crossBranchUpdateBodyRequestResource);
		
		SuccessAndErrorDetailsResource successDetailsDto=new SuccessAndErrorDetailsResource(environment.getProperty("common.updated"));
		return new ResponseEntity<>(successDetailsDto,HttpStatus.OK);
		
	}

}
