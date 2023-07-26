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
import com.fusionx.central.cash.management.domain.FeatureBenefitEligibilityCriteria;
import com.fusionx.central.cash.management.enums.CommonStatus;
import com.fusionx.central.cash.management.resource.AddFeatureBenefitEligibilityCriteriaResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.resource.UpdateFeatureBenefitEligibilityCriteriaResource;
import com.fusionx.central.cash.management.service.FeatureBenefitEligibilityCriteriaService;

/**
 * Feature Benefit Eligibility Criteria Controller
 * 
 ********************************************************************************************************
 *  ###   Date         Story Point   Task No    Author       Description
 *-------------------------------------------------------------------------------------------------------
 *   1   03-09-2020      FX-         FX-4577    ThavinyaR     Created
 *    
 ********************************************************************************************************
*/

@RestController
@RequestMapping("/feature-benefit-eligibility-criteria")
@CrossOrigin("*")
public class FeatureBenefitEligibilityCriteriaController extends MessagePropertyBase{
	
	@Autowired
	private FeatureBenefitEligibilityCriteriaService featureBenefitEligibilityCriteriaService;
	
	/**
	 * get all FeatureBenefitEligibilityCriteria
	 * @param @PathVariable{tenantId}
	 * @param @PathVariable{all}
	 * @return List
	 **/
	
	@GetMapping("/{tenantId}/all")
	public ResponseEntity<Object> findAllFeatureBenefitEligibilityCriteria(@PathVariable(value = "tenantId", required = true) String tenantId){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		List<FeatureBenefitEligibilityCriteria>isPresentFeatureBenefitEligibilityCriteria = featureBenefitEligibilityCriteriaService.findall();
		if(!isPresentFeatureBenefitEligibilityCriteria.isEmpty()) {
			return new ResponseEntity<>((Collection<FeatureBenefitEligibilityCriteria>)isPresentFeatureBenefitEligibilityCriteria,HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	

	/**
	 * get Feature Benefit Eligibility Criteria by ID
	 * @param @pathVariable{tenantId}
	 * @param @pathVariable {id}
	 * @return Optional
	 **/
	@GetMapping(value = "/{tenantId}/{id}")
	public ResponseEntity<Object> findAllFeatureBenefitEligibilityCriteriaById(@PathVariable(value = "tenantId", required = true) String tenantId,
													   @PathVariable(value = "id", required = true) Long id){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		Optional<FeatureBenefitEligibilityCriteria> isPresentFeatureBenefitEligibilityCriteria = featureBenefitEligibilityCriteriaService.findById(id);
		if(isPresentFeatureBenefitEligibilityCriteria.isPresent()) {
			return new ResponseEntity<>(isPresentFeatureBenefitEligibilityCriteria.get(), HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * get FeatureBenefitEligibilityCriteria by name
	 * @param @pathVariable{tenantId}
	 * @param @pathVariable {testName}
	 * @return Optional
	 **/
	@GetMapping(value = "/{tenantId}/name/{name}")
	public ResponseEntity<Object> findFeatureBenefitEligibilityCriteriaByName(@PathVariable(value = "tenantId", required = true) String tenantId,
													   @PathVariable(value = "name", required = true) String name){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		List<FeatureBenefitEligibilityCriteria> isPresentFeatureBenefitEligibilityCriteria = featureBenefitEligibilityCriteriaService.findbyName(name);
		if(!isPresentFeatureBenefitEligibilityCriteria.isEmpty()) {
			return new ResponseEntity<>(isPresentFeatureBenefitEligibilityCriteria, HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * get Feature Benefit Eligibility Criteria by status
	 * @param @pathVariable{tenantId}
	 * @param @pathVariable {testName}
	 * @return Optional
	 **/
	@GetMapping(value = "/{tenantId}/status/{status}")
	public ResponseEntity<Object> findFeatureBenefitEligibilityCriteriaByStatus(@PathVariable(value = "tenantId", required = true) String tenantId,
													   @PathVariable(value = "status", required = true) String status){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		if(status.equals(CommonStatus.ACTIVE.toString()) || status.equals(CommonStatus.INACTIVE.toString())) {
			List<FeatureBenefitEligibilityCriteria> isPresentFeatureBenefitEligibilityCriteria = featureBenefitEligibilityCriteriaService.findByStatus(status);
			if(!isPresentFeatureBenefitEligibilityCriteria.isEmpty()) {
				return new ResponseEntity<>(isPresentFeatureBenefitEligibilityCriteria, HttpStatus.OK);
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
     * save FeatureBenefitEligibilityCriteria
     * @param @PathVariable{tenantId}
     * @param @RequestBody{AddTestResource}
     * @return SuccessAndErrorDetailsDto
     */
	@PostMapping("/{tenantId}")
	public ResponseEntity<Object> findFeatureBenefitEligibilityCriteria(@PathVariable(value = "tenantId", required = true) String tenantId,
			   									           @Valid @RequestBody AddFeatureBenefitEligibilityCriteriaResource addFeatureBenefitEligibilityCriteriaResource){
		FeatureBenefitEligibilityCriteria featureBenefitEligibilityCriteria = featureBenefitEligibilityCriteriaService.addFeatureBenefitEligibilityCriteria(tenantId, addFeatureBenefitEligibilityCriteriaResource);
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource(getEnvironment().getProperty(RECORD_CREATED));
		return new ResponseEntity<>(responseMessage,HttpStatus.CREATED);
	}
	
	/**
     * update FeatureBenefitEligibilityCriteria
     * @param @PathVariable{tenantId}
     * @param @RequestBody{CommonUpdateResource}
     * @return SuccessAndErrorDetailsDto
     */
	@PutMapping(value = "{tenantId}/{id}")
	public ResponseEntity<Object> updateFeatureBenefitEligibilityCriteria(@PathVariable(value = "tenantId", required = true) String tenantId, 
										              @PathVariable(value = "id", required = true) Long id, 
										              @Valid @RequestBody UpdateFeatureBenefitEligibilityCriteriaResource updateFeatureBenefitEligibilityCriteriaResource){
		SuccessAndErrorDetailsResource successAndErrorDetailsResource=new SuccessAndErrorDetailsResource();
		Optional<FeatureBenefitEligibilityCriteria>isPresentClearingType = featureBenefitEligibilityCriteriaService.findById(id);
		
		if(isPresentClearingType.isPresent()) {
			updateFeatureBenefitEligibilityCriteriaResource.setId(id.toString());
			FeatureBenefitEligibilityCriteria featureBenefitEligibilityCriteria = featureBenefitEligibilityCriteriaService.updateFeatureBenefitEligibilityCriteria(tenantId, updateFeatureBenefitEligibilityCriteriaResource);
			successAndErrorDetailsResource = new SuccessAndErrorDetailsResource(getEnvironment().getProperty(RECORD_UPDATED), featureBenefitEligibilityCriteria.getId().toString());
			return new ResponseEntity<>(successAndErrorDetailsResource,HttpStatus.OK);
		}
		else {
			successAndErrorDetailsResource.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}


}
