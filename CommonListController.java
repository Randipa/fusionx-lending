package com.fusionx.central.cash.management.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.env.Environment;
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
import com.fusionx.central.cash.management.domain.CommonList;
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.resource.CommonListAddResource;
import com.fusionx.central.cash.management.resource.CommonListUpdateResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.CommonListService;


import io.micrometer.core.annotation.Timed;

@RestController
@RequestMapping(value = "/common-list")
@CrossOrigin(origins = "*")
@Timed
public class CommonListController extends MessagePropertyBase {
	@Autowired
	private Environment environment;
	@Autowired
	private CommonListService  service;
	
	/**
	 * get all common list details
	 * @param tenantId
	// * @param pageable
	// * @param predicate
	 * @return option
	 */
	@GetMapping(value = "/{tenantId}/all")
	public ResponseEntity<Object> getAllCommonLits(@PathVariable(value = "tenantId", required = true) String tenantId) {
		
		List<CommonList> list = service.findAll();
	      if(!list.isEmpty()) 
            return new ResponseEntity<>(list, HttpStatus.OK);
        else {
            return new ResponseEntity<>(list, HttpStatus.NO_CONTENT);
        }
	}
    
    
	
	
	/**
	 * get common list Info by id
	 * @param tenantId
	 * @param id
	 * @return option
	 */
	@GetMapping(value = "/{tenantId}/{id}")
	public ResponseEntity<Object> getCommonListById(@PathVariable(value = "tenantId", required = true) String tenantId, 
			                                                     @PathVariable(value = "id", required = true) Long id) {
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();

			Optional<CommonList> commonList = service.findById(id);
	 		if (commonList.isPresent()) {
	 			return new ResponseEntity<>(commonList.get(), HttpStatus.OK);
	 		} 
	 		else {
	 			responseMessage.setMessages(environment.getProperty("record-not-found"));
				return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
	 		}
	}
 	
 	
 	
 	/**
 	 * get common list Info by common list code
 	 * @param tenantId
 	 * @param code
 	 * @return option
 	 */
 	@GetMapping(value = "/{tenantId}/code/{code}")
	public ResponseEntity<Object> getCommonListByCode(@PathVariable(value = "tenantId", required = true) String tenantId, 
			                                                     @PathVariable(value = "code", required = true) String code) {
 		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();

		List<CommonList> commonList = service.findByCode(code);
	 		if (!commonList.isEmpty()) {
	 			return new ResponseEntity<>(commonList, HttpStatus.OK);
	 		} 
	 		else {
	 			responseMessage.setMessages(environment.getProperty("record-not-found"));
				return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
	 		}
	}
 	
 	
 
 	/**
 	 * get common list Info by common list reference code
 	 * @param tenantId
 	 * @param referenceCode
 	 * @return option
 	 */
 	@GetMapping(value = "/{tenantId}/refcode/{referenceCode}")
 	public ResponseEntity<Object> getCommonListByRefCode(@PathVariable(value = "tenantId", required = true) String tenantId, 
 			                                    @PathVariable(value = "referenceCode", required = true) String referenceCode) {
 		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();

 		List<CommonList> commonList = service.findByReferenceCode(referenceCode);
	 		
 			if (!commonList.isEmpty()) {
	 			return new ResponseEntity<>(commonList, HttpStatus.OK);
	 		} 
	 		else {
	 			responseMessage.setMessages(environment.getProperty("record-not-found"));
				return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
	 		}
 	
 	}
 	
 	
 	/**
 	 * get common list Info by common list status
 	 * @param tenantId
 	 * @param status
 	// * @param pageable
 	 //* @param predicate
 	 * @return option
 	 */
 	@GetMapping(value = "/{tenantId}/status/{status}")
	public ResponseEntity<Object> getCommonListByStatus(@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "status", required = true) String status) {

		List<CommonList> list = service.findByStatus(status);
		if (!list.isEmpty())
			return new ResponseEntity<>(list, HttpStatus.OK);
		else {
			return new ResponseEntity<>(list, HttpStatus.NO_CONTENT);
		}
	}
 	
	
 	
 	
 	/**
 	 * save common list Info
 	 * @param tenantId
 	 * @param commonListAddResource
 	 * @return successDetailsDto
 	 */
 	@PostMapping(value = "/{tenantId}")
	public ResponseEntity<Object> save(@PathVariable(value = "tenantId", required = true) String tenantId,
			@Valid @RequestBody CommonListAddResource commonListAddResource) {
		if(LogginAuthentcation.getUserName()==null || LogginAuthentcation.getUserName().isEmpty()) { 
 			throw new UserNotFound(environment.getProperty("common.user-not-found"));
 		}
		
		CommonList commonList=service.save(tenantId, commonListAddResource);
		SuccessAndErrorDetailsResource successDetailsDto=new SuccessAndErrorDetailsResource(environment.getProperty("rec.saved"),commonList.getId().toString());
    	return new ResponseEntity<>(successDetailsDto,HttpStatus.CREATED);
	}
 	
	
	
	
	/**
	 * update common list Info
	 * @param tenantId
	 * @param id
	 * @param commonListUpdateResource
	 * @return successAndErrorDetailsResource
	 */
	@PutMapping(value = "/{tenantId}/{id}")
 	public ResponseEntity<Object> updateCommonList(@PathVariable(value = "tenantId", required = true) String tenantId,
             	@PathVariable(value = "id", required = true) Long id,
				@Valid @RequestBody CommonListUpdateResource commonListUpdateResource ) {


 		if(LogginAuthentcation.getUserName()==null || LogginAuthentcation.getUserName().isEmpty()) {
 			throw new UserNotFound(environment.getProperty("common.user-not-found"));
 		}
 	//	updateBaseRequest.setModifiedUser(LogginAuthentcation.getUserName());

 		SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();

 		Optional<CommonList>  commonList = service.findById(id);
 		if(commonList.isPresent()) {
 			commonListUpdateResource.setId(id.toString());
 			//commonListUpdateResource.setTenantId(tenantId);

 			CommonList updatedObj = service.update(tenantId, commonListUpdateResource);
 			successAndErrorDetailsResource = new SuccessAndErrorDetailsResource(environment.getProperty("common-list-updated"), updatedObj.getId().toString());
 			return new ResponseEntity<>(successAndErrorDetailsResource,HttpStatus.OK);
 		}
        	else {
        		successAndErrorDetailsResource.setMessages(environment.getProperty("record-not-found"));
        		return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.UNPROCESSABLE_ENTITY);

 		}
	}



}
