package com.fusionx.central.cash.management.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.fusionx.central.cash.management.domain.BranchWiseBankAccountCreation;
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.resource.BranchWiseBankAccountCreationAddResource;
import com.fusionx.central.cash.management.resource.BranchWiseBankAccountCreationUpdateResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.BranchWiseBankAccountCreationService;

import io.micrometer.core.annotation.Timed;

@RestController
@RequestMapping(value = "/branch-wise-bank-account-creation")
@CrossOrigin(origins = "*")
@Timed
public class BranchWiseBankAccountCreationController extends MessagePropertyBase{

	@Autowired
	private BranchWiseBankAccountCreationService  service;
	
	@GetMapping(value = "/{tenantId}/all")
	public ResponseEntity<Object> getAllAccounts(@PathVariable(value = "tenantId", required = true) String tenantId) {
		
		List<BranchWiseBankAccountCreation> list = service.findAll();
	      if(!list.isEmpty()) 
            return new ResponseEntity<>(list, HttpStatus.OK);
        else {
            return new ResponseEntity<>(list, HttpStatus.NO_CONTENT);
        }
	}
	
	@GetMapping(value = "/{tenantId}/{id}")
	public ResponseEntity<Object> getAccountsById(@PathVariable(value = "tenantId", required = true) String tenantId, 
			                                                     @PathVariable(value = "id", required = true) Long id) {
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();

			Optional<BranchWiseBankAccountCreation> branchWiseBankAccountCreation = service.findById(id);
	 		if (branchWiseBankAccountCreation.isPresent()) {
	 			return new ResponseEntity<>(branchWiseBankAccountCreation.get(), HttpStatus.OK);
	 		} 
	 		else {
	 			responseMessage.setMessages(environment.getProperty("record-not-found"));
				return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
	 		}
	}
	
	@GetMapping(value = "/{tenantId}/code/{code}")
	public ResponseEntity<Object> getAccountsByCode(@PathVariable(value = "tenantId", required = true) String tenantId,
													   	            @PathVariable(value = "code", required = true) String code){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		
		Optional<BranchWiseBankAccountCreation> isPresentBranchWiseBankAccountCreation = service.findByCode(code);
		if(isPresentBranchWiseBankAccountCreation.isPresent()) {
			return new ResponseEntity<>(isPresentBranchWiseBankAccountCreation.get(), HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping(value = "/{tenantId}/name/{name}")
	public ResponseEntity<Object> getAccountsByName(@PathVariable(value = "tenantId", required = true) String tenantId,
															        @PathVariable(value = "name", required = true) String name){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		
		List<BranchWiseBankAccountCreation> isPresentBranchWiseBankAccountCreation = service.findByName(name);
		if(!isPresentBranchWiseBankAccountCreation.isEmpty()) {
			return new ResponseEntity<>(isPresentBranchWiseBankAccountCreation, HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping(value = "/{tenantId}/accountNo/{accountNo}")
	public ResponseEntity<Object> getAccountsByAccountNo(@PathVariable(value = "tenantId", required = true) String tenantId,
													   	            @PathVariable(value = "accountNo", required = true) String accountNo){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		
		List<BranchWiseBankAccountCreation> isPresentBranchWiseBankAccountCreation = service.findByAccountNo(accountNo);
		if(!isPresentBranchWiseBankAccountCreation.isEmpty()) {
			return new ResponseEntity<>(isPresentBranchWiseBankAccountCreation, HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping(value = "/{tenantId}/status/{status}")
	public ResponseEntity<Object> getAccountsByStatus(@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "status", required = true) String status) {

		List<BranchWiseBankAccountCreation> list = service.findByStatus(status);
		if (!list.isEmpty())
			return new ResponseEntity<>(list, HttpStatus.OK);
		else {
			return new ResponseEntity<>(list, HttpStatus.NO_CONTENT);
		}
	}
	
	@PostMapping(value = "/{tenantId}")
	public ResponseEntity<Object> save(@PathVariable(value = "tenantId", required = true) String tenantId,
			@Valid @RequestBody BranchWiseBankAccountCreationAddResource branchWiseBankAccountCreationAddResource) {
		if(LogginAuthentcation.getUserName()==null || LogginAuthentcation.getUserName().isEmpty()) { 
 			throw new UserNotFound(environment.getProperty("common.user-not-found"));
 		}
		
		BranchWiseBankAccountCreation branchWiseBankAccountCreation=service.save(tenantId, branchWiseBankAccountCreationAddResource);
		SuccessAndErrorDetailsResource successDetailsDto=new SuccessAndErrorDetailsResource(environment.getProperty("rec.saved"),branchWiseBankAccountCreation.getId().toString());
    	return new ResponseEntity<>(successDetailsDto,HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/{tenantId}/{id}")
 	public ResponseEntity<Object> update(@PathVariable(value = "tenantId", required = true) String tenantId,
             	@PathVariable(value = "id", required = true) Long id,
				@Valid @RequestBody BranchWiseBankAccountCreationUpdateResource branchWiseBankAccountCreationUpdateResource ) {
		
 		
 		if(LogginAuthentcation.getUserName()==null || LogginAuthentcation.getUserName().isEmpty()) { 
 			throw new UserNotFound(environment.getProperty("common.user-not-found"));
 		}
 		
 		SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
 		
 		Optional<BranchWiseBankAccountCreation>  branchWiseBankAccountCreation = service.findById(id);
 		if(branchWiseBankAccountCreation.isPresent()) {
 			branchWiseBankAccountCreationUpdateResource.setId(id.toString());
 			branchWiseBankAccountCreationUpdateResource.setTenantId(tenantId);
 			
 			BranchWiseBankAccountCreation updatedObj = service.update(tenantId, branchWiseBankAccountCreationUpdateResource);
 			successAndErrorDetailsResource = new SuccessAndErrorDetailsResource(environment.getProperty("rec.updated"), updatedObj.getId().toString());
 			return new ResponseEntity<>(successAndErrorDetailsResource,HttpStatus.OK);
 		}
        	else {
        		successAndErrorDetailsResource.setMessages(environment.getProperty(RECORD_NOT_FOUND));
        		return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.UNPROCESSABLE_ENTITY);
 			
 		}
	}
	
	
}
