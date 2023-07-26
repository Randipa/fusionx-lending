package com.fusionx.central.cash.management.controller;

import com.querydsl.core.types.Predicate;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fusionx.central.cash.management.Constants;
import com.fusionx.central.cash.management.base.MessagePropertyBase;
import com.fusionx.central.cash.management.core.LogginAuthentcation;
import com.fusionx.central.cash.management.domain.BranchParameter;
import com.fusionx.central.cash.management.exception.PageableLengthException;
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.resource.BranchParameterAddResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.BranchParameterService;


@RestController
@RequestMapping("/brn-param")
@CrossOrigin("*")
public class BranchParameterController extends MessagePropertyBase{
	
	@Autowired
	private BranchParameterService branchParameterService;
	
	private String pageableLength = "common.pageable-length";
	
	/**
	 * get all records
	 * @param @PathVariable{tenantId}
	 * @param @PathVariable{all}
	 * @return List
	 **/
	@GetMapping("/{tenantId}/all")
	public ResponseEntity<Object> findAllBranchParameter(@PathVariable(value = "tenantId", required = true) String tenantId,
													  @PageableDefault(size = 10) Pageable pageable,
													  @QuerydslPredicate(root = BranchParameter.class) Predicate predicate){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		Page<BranchParameter> branchParameter = branchParameterService.findall(pageable,predicate);
		if(branchParameter.hasContent()) {
			return new ResponseEntity<>(branchParameter,HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * Add BranchParameter.
	 *
	 * @param tenantId - the tenant id
	 * @param branchParameterAddResource - the branch Parameter  resource
	 * @return the response entity
	 */
	@PostMapping(value = "/{tenantId}")
	public ResponseEntity<Object> addBranchParameter(@PathVariable(value = "tenantId", required = true) String tenantId,
			@Valid @RequestBody BranchParameterAddResource branchParameterAddResource) {
		
		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new UserNotFound(environment.getProperty(USER_NOT_FOUND));
		
		Long id = branchParameterService.addBranchParameter(tenantId, LogginAuthentcation.getUserName(), branchParameterAddResource);
		
		SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(RECORD_CREATED), id.toString());
		return new ResponseEntity<>(successDetailsDto,HttpStatus.CREATED);
	}
	
	/**
	 * Search BranchParameter.
	 *
	 * @param tenantId - the tenant id
	 * @param searchq - the searchq
	 * @param name - the name
	 * @param code - the code
	 * @param status - the status
	 * @param pageable - the pageable
	 * @return the page
	 */
	@GetMapping(value = "/{tenantId}/search")
	public Page<BranchParameter> searchBranchParameter(@PathVariable(value = "tenantId", required = true) String tenantId,
			@RequestParam(value = "searchq", required = false) String searchq,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "status", required = false) String status,
			@PageableDefault(size = 10) Pageable pageable) {
		if(pageable.getPageSize()>Constants.MAX_PAGEABLE_LENGTH) 
			throw new PageableLengthException(environment.getProperty(pageableLength));
		return branchParameterService.searchBranchParameter(searchq, name, code, status, pageable);
	}

}
