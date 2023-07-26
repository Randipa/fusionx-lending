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
import com.fusionx.central.cash.management.domain.FeatureBenefitItem;
import com.fusionx.central.cash.management.enums.CommonStatus;
import com.fusionx.central.cash.management.resource.AddFeatureBenefitItemResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.resource.UpdateFeatureBenefitItemResource;
import com.fusionx.central.cash.management.service.FeatureBenefitItemService;

/**
 * Feature Benefit Item Controller
 * 
 ********************************************************************************************************
 *  ###   Date         Story Point   Task No    Author       Description
 *-------------------------------------------------------------------------------------------------------
 *   1   31-08-2020     FX-4631      FX-4572    ThavinyaR     Created
 *    
 ********************************************************************************************************
*/

@RestController
@RequestMapping("/feature-benefit-item")
@CrossOrigin("*")
public class FeatureBenefitItemController extends MessagePropertyBase{
	
	@Autowired
	private FeatureBenefitItemService featureBenefitItemService;
	
	/**
	 * get all FeatureBenefitItems
	 * @param @PathVariable{tenantId}
	 * @param @PathVariable{all}
	 * @return List
	 **/
	
	@GetMapping("/{tenantId}/all")
	public ResponseEntity<Object> findAllFeatureBenefitItems(@PathVariable(value = "tenantId", required = true) String tenantId){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		List<FeatureBenefitItem>isPresentFeatureBenefitItem = featureBenefitItemService.findall();
		if(!isPresentFeatureBenefitItem.isEmpty()) {
			return new ResponseEntity<>((Collection<FeatureBenefitItem>)isPresentFeatureBenefitItem,HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	

	/**
	 * get FeatureBenefitItem by ID
	 * @param @pathVariable{tenantId}
	 * @param @pathVariable {id}
	 * @return Optional
	 **/
	@GetMapping(value = "/{tenantId}/{id}")
	public ResponseEntity<Object> findAllFeatureBenefitItemById(@PathVariable(value = "tenantId", required = true) String tenantId,
													   @PathVariable(value = "id", required = true) Long id){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		Optional<FeatureBenefitItem> isPresentFeatureBenefitItem = featureBenefitItemService.findById(id);
		if(isPresentFeatureBenefitItem.isPresent()) {
			return new ResponseEntity<>(isPresentFeatureBenefitItem.get(), HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * get FeatureBenefitItem by name
	 * @param @pathVariable{tenantId}
	 * @param @pathVariable {testName}
	 * @return Optional
	 **/
	@GetMapping(value = "/{tenantId}/name/{name}")
	public ResponseEntity<Object> findFeatureBenefitItemByName(@PathVariable(value = "tenantId", required = true) String tenantId,
													   @PathVariable(value = "name", required = true) String name){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		List<FeatureBenefitItem> isPresentFeatureBenefitItem = featureBenefitItemService.findbyName(name);
		if(!isPresentFeatureBenefitItem.isEmpty()) {
			return new ResponseEntity<>(isPresentFeatureBenefitItem, HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * get FeatureBenefitItem by status
	 * @param @pathVariable{tenantId}
	 * @param @pathVariable {testName}
	 * @return Optional
	 **/
	@GetMapping(value = "/{tenantId}/status/{status}")
	public ResponseEntity<Object> findFeatureBenefitItemByStatus(@PathVariable(value = "tenantId", required = true) String tenantId,
													   @PathVariable(value = "status", required = true) String status){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		if(status.equals(CommonStatus.ACTIVE.toString()) || status.equals(CommonStatus.INACTIVE.toString())) {
			List<FeatureBenefitItem> isPresentFeatureBenefitItem = featureBenefitItemService.findByStatus(status);
			if(!isPresentFeatureBenefitItem.isEmpty()) {
				return new ResponseEntity<>(isPresentFeatureBenefitItem, HttpStatus.OK);
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
     * save FeatureBenefitItem
     * @param @PathVariable{tenantId}
     * @param @RequestBody{AddTestResource}
     * @return SuccessAndErrorDetailsDto
     */
	@PostMapping("/{tenantId}")
	public ResponseEntity<Object> findFeatureBenefitItem(@PathVariable(value = "tenantId", required = true) String tenantId,
			   									           @Valid @RequestBody AddFeatureBenefitItemResource addFeatureBenefitItemResource){
		FeatureBenefitItem featureBenefitItem = featureBenefitItemService.addFeatureBenefitItem(tenantId, addFeatureBenefitItemResource);
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource(getEnvironment().getProperty(RECORD_CREATED));
		return new ResponseEntity<>(responseMessage,HttpStatus.CREATED);
	}
	
	/**
     * update FeatureBenefitItem
     * @param @PathVariable{tenantId}
     * @param @RequestBody{CommonUpdateResource}
     * @return SuccessAndErrorDetailsDto
     */
	@PutMapping(value = "{tenantId}/{id}")
	public ResponseEntity<Object> updateFeatureBenefitItem(@PathVariable(value = "tenantId", required = true) String tenantId, 
										              @PathVariable(value = "id", required = true) Long id, 
										              @Valid @RequestBody UpdateFeatureBenefitItemResource updateFeatureBenefitItemResource){
		SuccessAndErrorDetailsResource successAndErrorDetailsResource=new SuccessAndErrorDetailsResource();
		Optional<FeatureBenefitItem>isPresentClearingType = featureBenefitItemService.findById(id);
		
		if(isPresentClearingType.isPresent()) {
			updateFeatureBenefitItemResource.setId(id.toString());
			FeatureBenefitItem featureBenefitItem = featureBenefitItemService.updateFeatureBenefitItem(tenantId, updateFeatureBenefitItemResource);
			successAndErrorDetailsResource = new SuccessAndErrorDetailsResource(getEnvironment().getProperty(RECORD_UPDATED), featureBenefitItem.getId().toString());
			return new ResponseEntity<>(successAndErrorDetailsResource,HttpStatus.OK);
		}
		else {
			successAndErrorDetailsResource.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

}
