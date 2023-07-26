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
import com.fusionx.central.cash.management.domain.FeaBeneEligiCriMapping;
import com.fusionx.central.cash.management.enums.CommonStatus;
import com.fusionx.central.cash.management.resource.AddFeaBeneEligiCriMappingResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.resource.UpdateFeaBeneEligiCriMappingResource;
import com.fusionx.central.cash.management.service.FeaBeneEligiCriMappingService;


/**
 * 	Feature Benefit Eligibility Criteria Mapping Controller
 * 
 ********************************************************************************************************
 *  ###   Date         Story Point   Task No    Author       Description
 *-------------------------------------------------------------------------------------------------------
 *    1   07-09-2020     FX-      FX-4578    ThavinyaR     Created
 *    
 ********************************************************************************************************
*/

@RestController
@RequestMapping("/fea-benefit-eligi-criteria")
@CrossOrigin("*")
public class FeaBeneEligiCriMappingController extends MessagePropertyBase{
	
	@Autowired
	private FeaBeneEligiCriMappingService feaBeneEligiCriMappingService;
	
	/**
	 * get all Mappings
	 * @param @PathVariable{tenantId}
	 * @param @PathVariable{all}
	 * @return List
	 **/
	
	@GetMapping("/{tenantId}/all")
	public ResponseEntity<Object> findAllFeaBeneEligiCriMappings(@PathVariable(value = "tenantId", required = true) String tenantId){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		List<FeaBeneEligiCriMapping>isPresentFeaBeneEligiCriMappings = feaBeneEligiCriMappingService.findall();
		if(!isPresentFeaBeneEligiCriMappings.isEmpty()) {
			return new ResponseEntity<>((Collection<FeaBeneEligiCriMapping>)isPresentFeaBeneEligiCriMappings,HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * get Mapping by ID
	 * @param @pathVariable{tenantId}
	 * @param @pathVariable {id}
	 * @return Optional
	 **/
	@GetMapping(value = "/{tenantId}/{id}")
	public ResponseEntity<Object> findAllFeaBeneEligiCriMappingById(@PathVariable(value = "tenantId", required = true) String tenantId,
													   @PathVariable(value = "id", required = true) Long id){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		Optional<FeaBeneEligiCriMapping> isPresentFeaBeneEligiCriMappings = feaBeneEligiCriMappingService.findById(id);
		if(isPresentFeaBeneEligiCriMappings.isPresent()) {
			return new ResponseEntity<>(isPresentFeaBeneEligiCriMappings.get(), HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * get Mapping by status
	 * @param @pathVariable{tenantId}
	 * @param @pathVariable {status}
	 * @return Optional
	 **/
	@GetMapping(value = "/{tenantId}/status/{status}")
	public ResponseEntity<Object> findFeaBeneEligiCriMappingByStatus(@PathVariable(value = "tenantId", required = true) String tenantId,
													   @PathVariable(value = "status", required = true) String status){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		if(status.equals(CommonStatus.ACTIVE.toString()) || status.equals(CommonStatus.INACTIVE.toString())) {
			List<FeaBeneEligiCriMapping> isPresentFeaBeneEligiCriMappings = feaBeneEligiCriMappingService.findByStatus(status);
			if(!isPresentFeaBeneEligiCriMappings.isEmpty()) {
				return new ResponseEntity<>(isPresentFeaBeneEligiCriMappings, HttpStatus.OK);
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
     * save Mapping
     * @param @PathVariable{tenantId}
     * @param @RequestBody{AddResource}
     * @return SuccessAndErrorDetailsDto
     */
	@PostMapping("/{tenantId}")
	public ResponseEntity<Object> addFeaBeneEligiCriMapping(@PathVariable(value = "tenantId", required = true) String tenantId,
			   									           @Valid @RequestBody AddFeaBeneEligiCriMappingResource addFeaBeneEligiCriMappingResource){
		FeaBeneEligiCriMapping feaBeneEligiCriMapping = feaBeneEligiCriMappingService.addFeaBeneEligiCriMapping(tenantId, addFeaBeneEligiCriMappingResource);
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource(getEnvironment().getProperty(RECORD_CREATED));
		return new ResponseEntity<>(responseMessage,HttpStatus.CREATED);
	}
	
	/**
     * update Mapping
     * @param @PathVariable{tenantId}
     * @param @RequestBody{CommonUpdateResource}
     * @return SuccessAndErrorDetailsDto
     */
	@PutMapping(value = "{tenantId}/{id}")
	public ResponseEntity<Object> updateFeaBeneEligiCriMapping(@PathVariable(value = "tenantId", required = true) String tenantId, 
										              @PathVariable(value = "id", required = true) Long id, 
										              @Valid @RequestBody UpdateFeaBeneEligiCriMappingResource updateFeaBeneEligiCriMappingResource){
		SuccessAndErrorDetailsResource successAndErrorDetailsResource=new SuccessAndErrorDetailsResource();
		Optional<FeaBeneEligiCriMapping>isPresentClearingType = feaBeneEligiCriMappingService.findById(id);
		
		if(isPresentClearingType.isPresent()) {
			updateFeaBeneEligiCriMappingResource.setId(id.toString());
			FeaBeneEligiCriMapping feaBeneEligiCriMapping = feaBeneEligiCriMappingService.updateFeaBeneEligiCriMapping(tenantId, updateFeaBeneEligiCriMappingResource);
			successAndErrorDetailsResource = new SuccessAndErrorDetailsResource(getEnvironment().getProperty(RECORD_UPDATED), feaBeneEligiCriMapping.getId().toString());
			return new ResponseEntity<>(successAndErrorDetailsResource,HttpStatus.OK);
		}
		else {
			successAndErrorDetailsResource.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

}
