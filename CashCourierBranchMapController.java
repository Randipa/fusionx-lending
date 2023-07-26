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
import com.fusionx.central.cash.management.domain.CashCourierBranchMap;
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.resource.CashCourierBranchMapAddResource;
import com.fusionx.central.cash.management.resource.CashCourierBranchMapUpdateResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.CashCourierBranchMapService;


@RestController
@RequestMapping("/cash-courier-brn")
@CrossOrigin("*")
public class CashCourierBranchMapController extends MessagePropertyBase{
	
	@Autowired
	private CashCourierBranchMapService cashCourierBranchMapService;
	
	/**
	 * get all records
	 * @param @PathVariable{tenantId}
	 * @param @PathVariable{all}
	 * @return List
	 **/
	@GetMapping("/{tenantId}/all")
	public ResponseEntity<Object> findAllCashCourierBranch(@PathVariable(value = "tenantId", required = true) String tenantId,
													  @PageableDefault(size = 10) Pageable pageable,
													  @QuerydslPredicate(root = CashCourierBranchMap.class) Predicate predicate){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		List<CashCourierBranchMap> cashCourierBranchList = cashCourierBranchMapService.findAll();
		if(!cashCourierBranchList.isEmpty()) {
			return new ResponseEntity<>(cashCourierBranchList,HttpStatus.OK);
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
													  @QuerydslPredicate(root = CashCourierBranchMap.class) Predicate predicate){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		List<CashCourierBranchMap> cashCourierBranchList = cashCourierBranchMapService.findByStatus(status);
		if(!cashCourierBranchList.isEmpty()) {
			return new ResponseEntity<>(cashCourierBranchList,HttpStatus.OK);
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
	 * @return cashCourierBranchMap
	 **/
	@GetMapping("/{tenantId}/{id}")
	public ResponseEntity<Object> findById(@PathVariable(value = "tenantId", required = true) String tenantId,@PathVariable(value = "id", required = true) String id,
													  @PageableDefault(size = 10) Pageable pageable,
													  @QuerydslPredicate(root = CashCourierBranchMap.class) Predicate predicate){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		CashCourierBranchMap cashCourierBranchMap = cashCourierBranchMapService.findById(Long.parseLong(id));
		if(cashCourierBranchMap!=null) {
			return new ResponseEntity<>(cashCourierBranchMap,HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * Add CashCourierBranchMap.
	 *
	 * @param tenantId - the tenant id
	 * @param cashCourierBranchMapAddResource
	 * @return the response entity
	 */
	@PostMapping(value = "/{tenantId}")
	public ResponseEntity<Object> addCourierBranchMap(@PathVariable(value = "tenantId", required = true) String tenantId,
			@Valid @RequestBody CashCourierBranchMapAddResource cashCourierBranchMapAddResource) {
		
		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new UserNotFound(environment.getProperty(USER_NOT_FOUND));
		
		Long id = cashCourierBranchMapService.addCashCourierBranchMap(tenantId, LogginAuthentcation.getUserName(), cashCourierBranchMapAddResource);
		
		SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(RECORD_CREATED), id.toString());
		return new ResponseEntity<>(successDetailsDto,HttpStatus.CREATED);
	}
	
	/**
	 * Update CashCourierBranchMap.
	 *
	 * @param tenantId - the tenant id
	 * @param cashCourierBranchMapUpdateResource
	 * @return the response entity
	 */
	@PutMapping(value = "{tenantId}/{id}")
	public ResponseEntity<Object> updateCourierBranchMap(@PathVariable(value = "tenantId", required = true) String tenantId,@PathVariable(value = "id", required = true) String id,
			@Valid @RequestBody CashCourierBranchMapUpdateResource cashCourierBranchMapUpdateResource) {
		
		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new UserNotFound(environment.getProperty(USER_NOT_FOUND));
		
		SuccessAndErrorDetailsResource successAndErrorDetailsResource=new SuccessAndErrorDetailsResource();
		CashCourierBranchMap courierBranchMap = cashCourierBranchMapService.findById(Long.parseLong(id));
		
		if(courierBranchMap!=null) {
			cashCourierBranchMapUpdateResource.setId(id.toString());
			CashCourierBranchMap cashCourierBranchMap = cashCourierBranchMapService.updateCashCourierBranchMap(tenantId, cashCourierBranchMapUpdateResource);
			successAndErrorDetailsResource = new SuccessAndErrorDetailsResource(getEnvironment().getProperty(RECORD_UPDATED), cashCourierBranchMap.getId().toString());
			return new ResponseEntity<>(successAndErrorDetailsResource,HttpStatus.OK);
		}else {
			successAndErrorDetailsResource.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

}
