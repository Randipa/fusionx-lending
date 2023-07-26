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
import com.fusionx.central.cash.management.domain.FeatureBenefitEligibilityType;
import com.fusionx.central.cash.management.enums.CommonStatus;
import com.fusionx.central.cash.management.resource.AddFeatureBenefitEligibilityTypeResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.resource.UpdateFeatureBenefitEligibilityTypeResource;
import com.fusionx.central.cash.management.service.FeatureBenefitEligibilityTypeService;

/**
 * Feature Benefit Eligibility Type Controller 
 * 
 ********************************************************************************************************
 *  ###   Date         Story Point   Task No    Author       Description
 *-------------------------------------------------------------------------------------------------------
 *    1  19-08-2020     FX-   		  FX-4574 	 ThavinyaR 	 Created
 *    
 ********************************************************************************************************
 */

@RestController
@RequestMapping("/feature-benefit-eligibility-type")
@CrossOrigin("*")
public class FeatureBenefitEligibilityTypeController extends MessagePropertyBase{
	
	@Autowired
	private FeatureBenefitEligibilityTypeService featureBenefitEligibilityTypeService;
	
	/**
	 * get all Feature Benefit Eligibility Types
	 * @param @PathVariable{tenantId}
	 * @param @PathVariable{all}
	 * @return List
	 **/
	
	@GetMapping("/{tenantId}/all")
	public ResponseEntity<Object> getAllFeatureBenefitEligibilityTypes(@PathVariable(value = "tenantId", required = true) String tenantId){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		List<FeatureBenefitEligibilityType>isPresentFeatureBenefitEligibilityTypes= featureBenefitEligibilityTypeService.findall();
		if(!isPresentFeatureBenefitEligibilityTypes.isEmpty()) {
			return new ResponseEntity<>((Collection<FeatureBenefitEligibilityType>)isPresentFeatureBenefitEligibilityTypes,HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * get feature Benefit Eligibility Type by ID
	 * @param @pathVariable{tenantId}
	 * @param @pathVariable {id}
	 * @return Optional
	 **/
	@GetMapping(value = "/{tenantId}/{id}")
	public ResponseEntity<Object> getAllFeatureBenefitEligibilityTypeById(@PathVariable(value = "tenantId", required = true) String tenantId,
													   @PathVariable(value = "id", required = true) Long id){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		Optional<FeatureBenefitEligibilityType> isPresentFeatureBenefitEligibilityTypes = featureBenefitEligibilityTypeService.findById(id);
		if(isPresentFeatureBenefitEligibilityTypes.isPresent()) {
			return new ResponseEntity<>(isPresentFeatureBenefitEligibilityTypes.get(), HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * get feature Benefit Eligibility Type by code
	 * @param @pathVariable{tenantId}
	 * @param @pathVariable {code}
	 * @return Optional
	 **/
	@GetMapping(value = "/{tenantId}/code/{code}")
	public ResponseEntity<Object> getFeatureBenefitEligibilityTypeByCode(@PathVariable(value = "tenantId", required = true) String tenantId,
													   @PathVariable(value = "code", required = true) String code){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		Optional<FeatureBenefitEligibilityType> isPresentFeatureBenefitEligibilityTypes = featureBenefitEligibilityTypeService.findByCode(code);
		if(isPresentFeatureBenefitEligibilityTypes.isPresent()) {
			return new ResponseEntity<>(isPresentFeatureBenefitEligibilityTypes.get(), HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
 	/**
     * get feature Benefit Eligibility Type by name
     * @param @PathVariable{tenantId}
     * @param @PathVariable{name}
     * @return Option
     */
	@GetMapping(value = "/{tenantId}/name/{name}")
	public ResponseEntity<Object> getFeatureBenefitEligibilityTypeByName(@PathVariable(value = "tenantId", required = true) String tenantId,
													   @PathVariable(value = "name", required = true) String name){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		List<FeatureBenefitEligibilityType> isPresentFeatureBenefitEligibilityType = featureBenefitEligibilityTypeService.findbyName(name);
		if(!isPresentFeatureBenefitEligibilityType.isEmpty()) {
			return new ResponseEntity<>(isPresentFeatureBenefitEligibilityType, HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * get feature Benefit Eligibility Type by status
	 * @param @pathVariable{tenantId}
	 * @param @pathVariable {status}
	 * @return Optional
	 **/
	@GetMapping(value = "/{tenantId}/status/{status}")
	public ResponseEntity<Object> getFeatureBenefitEligibilityTypeByStatus(@PathVariable(value = "tenantId", required = true) String tenantId,
													   @PathVariable(value = "status", required = true) String status){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		if(status.equals(CommonStatus.ACTIVE.toString()) || status.equals(CommonStatus.INACTIVE.toString())) {
			List<FeatureBenefitEligibilityType> isPresentFeatureBenefitEligibilityTypes = featureBenefitEligibilityTypeService.findByStatus(status);
			if(!isPresentFeatureBenefitEligibilityTypes.isEmpty()) {
				return new ResponseEntity<>(isPresentFeatureBenefitEligibilityTypes, HttpStatus.OK);
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
     * save feature Benefit Eligibility Type
     * @param @PathVariable{tenantId}
     * @param @RequestBody{AddTestResource}
     * @return SuccessAndErrorDetailsDto
     */
	@PostMapping("/{tenantId}")
	public ResponseEntity<Object> addFeatureBenefitEligibilityType(@PathVariable(value = "tenantId", required = true) String tenantId,
			   									           @Valid @RequestBody AddFeatureBenefitEligibilityTypeResource addFeatureBenefitEligibilityTypeResource){
		FeatureBenefitEligibilityType featureBenefitEligibilityType = featureBenefitEligibilityTypeService.addFeatureBenefitEligibilityType(tenantId, addFeatureBenefitEligibilityTypeResource);
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource(getEnvironment().getProperty(RECORD_CREATED), addFeatureBenefitEligibilityTypeResource.getCode());
		return new ResponseEntity<>(responseMessage,HttpStatus.CREATED);
	}
	
	/**
     * update feature Benefit Eligibility Type
     * @param @PathVariable{tenantId}
     * @param @RequestBody{CommonUpdateResource}
     * @return SuccessAndErrorDetailsDto
     */
	@PutMapping(value = "{tenantId}/{id}")
	public ResponseEntity<Object> updateFeatureBenefitEligibilityType(@PathVariable(value = "tenantId", required = true) String tenantId, 
										              @PathVariable(value = "id", required = true) Long id, 
										              @Valid @RequestBody UpdateFeatureBenefitEligibilityTypeResource updateFeatureBenefitEligibilityTypeResource){
		SuccessAndErrorDetailsResource successAndErrorDetailsResource=new SuccessAndErrorDetailsResource();
		Optional<FeatureBenefitEligibilityType>isPresentClearingType = featureBenefitEligibilityTypeService.findById(id);
		
		if(isPresentClearingType.isPresent()) {
			updateFeatureBenefitEligibilityTypeResource.setId(id.toString());
			FeatureBenefitEligibilityType featureBenefitEligibilityType = featureBenefitEligibilityTypeService.updateFeatureBenefitEligibilityType(tenantId, updateFeatureBenefitEligibilityTypeResource);
			successAndErrorDetailsResource = new SuccessAndErrorDetailsResource(getEnvironment().getProperty(RECORD_UPDATED), featureBenefitEligibilityType.getId().toString());
			return new ResponseEntity<>(successAndErrorDetailsResource,HttpStatus.OK);
		}
		else {
			successAndErrorDetailsResource.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

}
