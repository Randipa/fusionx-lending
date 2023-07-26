package com.fusionx.central.cash.management.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.fusionx.central.cash.management.domain.CoinBagAndBundle;
import com.fusionx.central.cash.management.enums.DenominationType;
import com.fusionx.central.cash.management.exception.ValidateRecordException;
import com.fusionx.central.cash.management.resource.CoinBagAndBundleAddResource;
import com.fusionx.central.cash.management.resource.CoinBagAndBundleUpdateResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.CoinBagAndBundleService;


import io.micrometer.core.annotation.Timed;

@RestController
@RequestMapping(value = "/coin-bag-and-bundle")
@CrossOrigin(origins = "*")
@Timed
public class CoinBagAndBundleController extends MessagePropertyBase{

	@Autowired
	private CoinBagAndBundleService coinBagAndBundleService;
	
	
	
	@GetMapping("/{tenantId}/all")
	public ResponseEntity<Object> getAllCoinBagAndBundle(@PathVariable(value = "tenantId", required = true) String tenantId,
	@PageableDefault(size = 10) Pageable pageable){
		Page<CoinBagAndBundle> list = coinBagAndBundleService.findAll(pageable);
	      if(!list.isEmpty()) 
          return new ResponseEntity<>(list, HttpStatus.OK);
      else {
          return new ResponseEntity<>(list, HttpStatus.NO_CONTENT);
      }
	}
	
	
	
	@GetMapping(value = "/{tenantId}/{id}")
	public ResponseEntity<Object> getCoinBagAndBundleById(@PathVariable(value = "tenantId", required = true) String tenantId,
													         	  @PathVariable(value = "id", required = true) Long id){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		
		Optional<CoinBagAndBundle> isPresentCoinBagAndBundle = coinBagAndBundleService.findById(id);
		if(isPresentCoinBagAndBundle.isPresent()) {
			return new ResponseEntity<>(isPresentCoinBagAndBundle.get(), HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping(value = "/{tenantId}/status/{status}")
	public ResponseEntity<Object> getCoinBagAndBundleByStatus(@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "status", required = true) String status, @PageableDefault(size = 10) Pageable pageable) {

		Page<CoinBagAndBundle> list = coinBagAndBundleService.findByStatus(status, pageable);
		if (!list.isEmpty())
			return new ResponseEntity<>(list, HttpStatus.OK);
		else {
			return new ResponseEntity<>(list, HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping(value = "/{tenantId}/denomination/{denominationId}")
	public ResponseEntity<Object> getByDenominationId(@PathVariable(value = "tenantId", required = true) String tenantId,
													   	            @PathVariable(value = "denominationId", required = true) Long denominationId, @PageableDefault(size = 10) Pageable pageable){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		
		Page<CoinBagAndBundle> isPresentCoinBagAndBundle = coinBagAndBundleService.findByDenominationId(denominationId, pageable);
		if(!isPresentCoinBagAndBundle.isEmpty()) {
			return new ResponseEntity<>(isPresentCoinBagAndBundle, HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping(value = "/{tenantId}/denomination-type/{denominationType}")
 	public ResponseEntity<Object> getByDenominationType(@PathVariable(value = "tenantId", required = true) String tenantId, 
 			                                    @PathVariable(value = "denominationType", required = true) DenominationType denominationType, @PageableDefault(size = 10) Pageable pageable) {
 		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		if(denominationType.equals(DenominationType.NOTES) || denominationType.equals(DenominationType.COINS)) {

 			Page<CoinBagAndBundle>  coinBagAndBundle = coinBagAndBundleService.findByDenominationType(denominationType, pageable);
	 		
 			if(!coinBagAndBundle.isEmpty()) {
				return new ResponseEntity<>(coinBagAndBundle, HttpStatus.OK);
			}
			else {
				responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
				return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
			}
		}
		else {
			responseMessage.setMessages(RECORD_NOT_FOUND);
			throw new ValidateRecordException(environment.getProperty("DenominationType.pattern"), "denominationType");
		}
	}
	
	@PostMapping("/{tenantId}")
	public ResponseEntity<Object> addCoinBagAndBundle(@PathVariable(value = "tenantId", required = true) String tenantId,
			   									       		  @Valid @RequestBody CoinBagAndBundleAddResource coinBagAndBundleAddResource){
		CoinBagAndBundle coinBagAndBundle = coinBagAndBundleService.save(tenantId, coinBagAndBundleAddResource);
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource(getEnvironment().getProperty(RECORD_CREATED), Long.toString(coinBagAndBundle.getId()));
		return new ResponseEntity<>(responseMessage,HttpStatus.CREATED);
	}
	
	
	@PutMapping(value = "{tenantId}/{id}")
	public ResponseEntity<Object> updateCoinBagAndBundle(@PathVariable(value = "tenantId", required = true) String tenantId, 
												                 @PathVariable(value = "id", required = true) Long id, 
												                 @Valid @RequestBody CoinBagAndBundleUpdateResource coinBagAndBundleUpdateResource){
		SuccessAndErrorDetailsResource successAndErrorDetailsResource=new SuccessAndErrorDetailsResource();
		Optional<CoinBagAndBundle>isPresentCoinBagAndBundle = coinBagAndBundleService.findById(id);		
		if(isPresentCoinBagAndBundle.isPresent()) {
			coinBagAndBundleUpdateResource.setId(id.toString());
			CoinBagAndBundle coinBagAndBundle = coinBagAndBundleService.update(tenantId, coinBagAndBundleUpdateResource);
			successAndErrorDetailsResource = new SuccessAndErrorDetailsResource(getEnvironment().getProperty(RECORD_UPDATED), coinBagAndBundle.getId().toString());
			return new ResponseEntity<>(successAndErrorDetailsResource,HttpStatus.OK);
		}
		else {
			successAndErrorDetailsResource.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
}
