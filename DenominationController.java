package com.fusionx.central.cash.management.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.querydsl.core.types.Predicate;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fusionx.central.cash.management.base.MessagePropertyBase;
import com.fusionx.central.cash.management.domain.Denomination;
import com.fusionx.central.cash.management.enums.CommonStatus;
import com.fusionx.central.cash.management.enums.DenominationType;
import com.fusionx.central.cash.management.resource.AddDenominationResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.resource.UpdateDenominationResource;
import com.fusionx.central.cash.management.service.DenominationService;

/**
 * Denomination Controller
 * 
 ********************************************************************************************************
 *  ###   Date         Story Point   Task No    Author       Description
 *-------------------------------------------------------------------------------------------------------
 *   1   09-09-2020      FX-         FX-4623    ThavinyaR     Created
 *    
 ********************************************************************************************************
*/

@RestController
@RequestMapping("/denomination")
@CrossOrigin("*")
public class DenominationController extends MessagePropertyBase{
	
	@Autowired
	private DenominationService denominationService;
	
	/**
	 * get all records
	 * @param @PathVariable{tenantId}
	 * @param @PathVariable{all}
	 * @return List
	 **/
	@GetMapping("/{tenantId}/all")
	public ResponseEntity<Object> findAllDenomination(@PathVariable(value = "tenantId", required = true) String tenantId,
													  @PageableDefault(size = 10) Pageable pageable,
													  @QuerydslPredicate(root = Denomination.class) Predicate predicate){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		Page<Denomination> isPresentDenomination = denominationService.findall(pageable,predicate);
		if(isPresentDenomination.hasContent()) {
			return new ResponseEntity<>(isPresentDenomination,HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	

	/**
	 * get record by ID
	 * @param @pathVariable{tenantId}
	 * @param @pathVariable {id}
	 * @return Optional
	 **/
	@GetMapping(value = "/{tenantId}/{id}")
	public ResponseEntity<Object> findAllDenominationById(@PathVariable(value = "tenantId", required = true) String tenantId,
													   @PathVariable(value = "id", required = true) Long id){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		Optional<Denomination> isPresentDenomination = denominationService.findById(id);
		if(isPresentDenomination.isPresent()) {
			return new ResponseEntity<>(isPresentDenomination.get(), HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * get record by code
	 * @param @pathVariable{tenantId}
	 * @param @pathVariable {code}
	 * @return Optional
	 **/
	@GetMapping(value = "/{tenantId}/code/{code}")
	public ResponseEntity<Object> getDenominationByCode(@PathVariable(value = "tenantId", required = true) String tenantId,
													   @PathVariable(value = "code", required = true) String code){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		Optional<Denomination> isPresentDenomination = denominationService.findByCode(code);
		if(isPresentDenomination.isPresent()) {
			return new ResponseEntity<>(isPresentDenomination.get(), HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * get record by status
	 * @param @pathVariable{tenantId}
	 * @param @pathVariable {testName}
	 * @return Optional
	 **/
	@GetMapping(value = "/{tenantId}/status/{status}")
	public ResponseEntity<Object> findDenominationByStatus(@PathVariable(value = "tenantId", required = true) String tenantId,
													   @PathVariable(value = "status", required = true) String status){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		if(status.equals(CommonStatus.ACTIVE.toString()) || status.equals(CommonStatus.INACTIVE.toString())) {
			List<Denomination> isPresentDenomination = denominationService.findByStatus(status);
			if(!isPresentDenomination.isEmpty()) {
				return new ResponseEntity<>(isPresentDenomination, HttpStatus.OK);
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
	 * get record by type
	 * @param @pathVariable{tenantId}
	 * @param @pathVariable {testName}
	 * @return Optional
	 **/
	@GetMapping(value = "/{tenantId}/denominationType/{denominationType}")
	public ResponseEntity<Object> findDenominationByType(@PathVariable(value = "tenantId", required = true) String tenantId,
													   @PathVariable(value = "denominationType", required = true) String type){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		if(type.equals(DenominationType.NOTES.toString()) || type.equals(DenominationType.COINS.toString())) {
			List<Denomination> isPresentDenomination = denominationService.findByType(type);
			if(!isPresentDenomination.isEmpty()) {
				return new ResponseEntity<>(isPresentDenomination, HttpStatus.OK);
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
     * save records
     * @param @PathVariable{tenantId}
     * @param @RequestBody{AddTestResource}
     * @return SuccessAndErrorDetailsDto
     */
	@PostMapping("/{tenantId}")
	public ResponseEntity<Object> findDenomination(@PathVariable(value = "tenantId", required = true) String tenantId,
			   									           @Valid @RequestBody AddDenominationResource addDenominationResource){
		Denomination denomination = denominationService.addDenomination(tenantId, addDenominationResource);
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource(getEnvironment().getProperty(RECORD_CREATED));
		return new ResponseEntity<>(responseMessage,HttpStatus.CREATED);
	}
	
	/**
     * update records
     * @param @PathVariable{tenantId}
     * @param @RequestBody{CommonUpdateResource}
     * @return SuccessAndErrorDetailsDto
     */
	@PutMapping(value = "{tenantId}/{id}")
	public ResponseEntity<Object> updateDenomination(@PathVariable(value = "tenantId", required = true) String tenantId, 
										              @PathVariable(value = "id", required = true) Long id, 
										              @Valid @RequestBody UpdateDenominationResource updateDenominationResource){
		SuccessAndErrorDetailsResource successAndErrorDetailsResource=new SuccessAndErrorDetailsResource();
		Optional<Denomination>isPresentClearingType = denominationService.findById(id);
		
		if(isPresentClearingType.isPresent()) {
			updateDenominationResource.setId(id.toString());
			Denomination denomination = denominationService.updateDenomination(tenantId, updateDenominationResource);
			successAndErrorDetailsResource = new SuccessAndErrorDetailsResource(getEnvironment().getProperty(RECORD_UPDATED), denomination.getId().toString());
			return new ResponseEntity<>(successAndErrorDetailsResource,HttpStatus.OK);
		}
		else {
			successAndErrorDetailsResource.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}



}
