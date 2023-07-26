package com.fusionx.central.cash.management.controller;

import java.util.Collection;
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
import com.fusionx.central.cash.management.domain.FeatureBenefitItemType;
import com.fusionx.central.cash.management.enums.CommonStatus;
import com.fusionx.central.cash.management.resource.AddFeatureBenefitItemTypeResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.resource.UpdateFeatureBenefitItemTypeResource;
import com.fusionx.central.cash.management.service.FeatureBenefitItemTypeService;

/**
 * Feature Benefit Item Type Controller 
 * 
 ********************************************************************************************************
 *  ###   Date         Story Point   Task No    Author       Description
 *-------------------------------------------------------------------------------------------------------
 *    1  19-08-2020     FX-   		  FX-4571 	 ThavinyaR 	 Created
 *    
 ********************************************************************************************************
 */

@RestController
@RequestMapping("/feature-benefit-item-type")
@CrossOrigin("*")
public class FeatureBenefitItemTypeController extends MessagePropertyBase{
	
	@Autowired
	private FeatureBenefitItemTypeService featureBenefitItemTypeService;
	
	/**
	 * get all Feature Benefit Item Types
	 * @param @PathVariable{tenantId}
	 * @param @PathVariable{all}
	 * @return List
	 **/
	
	@GetMapping("/{tenantId}/all")
	public ResponseEntity<Object> getAllFeatureBenefitItemTypes(@PathVariable(value = "tenantId", required = true) String tenantId){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		List<FeatureBenefitItemType>isPresentFeatureBenefitItemTypes= featureBenefitItemTypeService.findall();
		if(!isPresentFeatureBenefitItemTypes.isEmpty()) {
			return new ResponseEntity<>((Collection<FeatureBenefitItemType>)isPresentFeatureBenefitItemTypes,HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * get feature Benefit Item Type by ID
	 * @param @pathVariable{tenantId}
	 * @param @pathVariable {id}
	 * @return Optional
	 **/
	@GetMapping(value = "/{tenantId}/{id}")
	public ResponseEntity<Object> getAllFeatureBenefitItemTypeById(@PathVariable(value = "tenantId", required = true) String tenantId,
													   @PathVariable(value = "id", required = true) Long id){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		Optional<FeatureBenefitItemType> isPresentFeatureBenefitItemTypes = featureBenefitItemTypeService.findById(id);
		if(isPresentFeatureBenefitItemTypes.isPresent()) {
			return new ResponseEntity<>(isPresentFeatureBenefitItemTypes.get(), HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * get feature Benefit Item Type by code
	 * @param @pathVariable{tenantId}
	 * @param @pathVariable {code}
	 * @return Optional
	 **/
	@GetMapping(value = "/{tenantId}/code/{code}")
	public ResponseEntity<Object> getFeatureBenefitItemTypeByCode(@PathVariable(value = "tenantId", required = true) String tenantId,
													   @PathVariable(value = "code", required = true) String code){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		Optional<FeatureBenefitItemType> isPresentFeatureBenefitItemTypes = featureBenefitItemTypeService.findByCode(code);
		if(isPresentFeatureBenefitItemTypes.isPresent()) {
			return new ResponseEntity<>(isPresentFeatureBenefitItemTypes.get(), HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
 	/**
     * get feature Benefit Item Type by name
     * @param @PathVariable{tenantId}
     * @param @PathVariable{name}
     * @return Option
     */
	@GetMapping(value = "/{tenantId}/name/{name}")
	public ResponseEntity<Object> getFeatureBenefitItemTypeByName(@PathVariable(value = "tenantId", required = true) String tenantId,
													   @PathVariable(value = "name", required = true) String name){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		List<FeatureBenefitItemType> isPresentFeatureBenefitItemType = featureBenefitItemTypeService.findbyName(name);
		if(!isPresentFeatureBenefitItemType.isEmpty()) {
			return new ResponseEntity<>(isPresentFeatureBenefitItemType, HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * get feature Benefit Item Type by status
	 * @param @pathVariable{tenantId}
	 * @param @pathVariable {status}
	 * @return Optional
	 **/
	@GetMapping(value = "/{tenantId}/status/{status}")
	public ResponseEntity<Object> getFeatureBenefitItemTypeByStatus(@PathVariable(value = "tenantId", required = true) String tenantId,
													   @PathVariable(value = "status", required = true) String status){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		if(status.equals(CommonStatus.ACTIVE.toString()) || status.equals(CommonStatus.INACTIVE.toString())) {
			List<FeatureBenefitItemType> isPresentFeatureBenefitItemTypes = featureBenefitItemTypeService.findByStatus(status);
			if(!isPresentFeatureBenefitItemTypes.isEmpty()) {
				return new ResponseEntity<>(isPresentFeatureBenefitItemTypes, HttpStatus.OK);
			}
			else {
				responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
				return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
			}
		}
		else {
			responseMessage.setMessages(RECORD_NOT_FOUND);
	        return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
     * save feature Benefit Item Type
     * @param @PathVariable{tenantId}
     * @param @RequestBody{AddTestResource}
     * @return SuccessAndErrorDetailsDto
     */
	@PostMapping("/{tenantId}")
	public ResponseEntity<Object> addFeatureBenefitItemType(@PathVariable(value = "tenantId", required = true) String tenantId,
			   									           @Valid @RequestBody AddFeatureBenefitItemTypeResource addFeatureBenefitItemTypeResource){
		FeatureBenefitItemType featureBenefitItemType = featureBenefitItemTypeService.addFeatureBenefitItemType(tenantId, addFeatureBenefitItemTypeResource);
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource(getEnvironment().getProperty(RECORD_CREATED), addFeatureBenefitItemTypeResource.getCode());
		return new ResponseEntity<>(responseMessage,HttpStatus.CREATED);
	}
	
	/**
     * update feature Benefit Item Type
     * @param @PathVariable{tenantId}
     * @param @RequestBody{CommonUpdateResource}
     * @return SuccessAndErrorDetailsDto
     */
	@PutMapping(value = "{tenantId}/{id}")
	public ResponseEntity<Object> updateFeatureBenefitItemType(@PathVariable(value = "tenantId", required = true) String tenantId, 
										              @PathVariable(value = "id", required = true) Long id, 
										              @Valid @RequestBody UpdateFeatureBenefitItemTypeResource updateFeatureBenefitItemTypeResource){
		SuccessAndErrorDetailsResource successAndErrorDetailsResource=new SuccessAndErrorDetailsResource();
		Optional<FeatureBenefitItemType>isPresentClearingType = featureBenefitItemTypeService.findById(id);
		
		if(isPresentClearingType.isPresent()) {
			updateFeatureBenefitItemTypeResource.setId(id.toString());
			FeatureBenefitItemType featureBenefitItemType = featureBenefitItemTypeService.updateFeatureBenefitItemType(tenantId, updateFeatureBenefitItemTypeResource);
			successAndErrorDetailsResource = new SuccessAndErrorDetailsResource(getEnvironment().getProperty(RECORD_UPDATED), featureBenefitItemType.getId().toString());
			return new ResponseEntity<>(successAndErrorDetailsResource,HttpStatus.OK);
		}
		else {
			successAndErrorDetailsResource.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

}
