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
import com.fusionx.central.cash.management.domain.PaymentType;
import com.fusionx.central.cash.management.resource.PaymentTypeAddResource;
import com.fusionx.central.cash.management.resource.PaymentTypeUpdateResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.PaymentTypeService;

import io.micrometer.core.annotation.Timed;

@RestController
@RequestMapping(value = "/payment-type")
@CrossOrigin(origins = "*")
@Timed
public class PaymentTypeController extends MessagePropertyBase{

	@Autowired
	private PaymentTypeService paymentTypeService;
	
	
	
	@GetMapping("/{tenantId}/all")
	public ResponseEntity<Object> getAllPaymentType(@PathVariable(value = "tenantId", required = true) String tenantId){
		List<PaymentType> list = paymentTypeService.findAll();
	      if(!list.isEmpty()) 
          return new ResponseEntity<>(list, HttpStatus.OK);
      else {
          return new ResponseEntity<>(list, HttpStatus.NO_CONTENT);
      }
	}
	
	
	
	@GetMapping(value = "/{tenantId}/{id}")
	public ResponseEntity<Object> getPaymentTypeById(@PathVariable(value = "tenantId", required = true) String tenantId,
													         	  @PathVariable(value = "id", required = true) Long id){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		
		Optional<PaymentType> isPresentPaymentType = paymentTypeService.findById(id);
		if(isPresentPaymentType.isPresent()) {
			return new ResponseEntity<>(isPresentPaymentType.get(), HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	
	@GetMapping(value = "/{tenantId}/code/{code}")
	public ResponseEntity<Object> getPaymentTypeByCode(@PathVariable(value = "tenantId", required = true) String tenantId,
													   	            @PathVariable(value = "code", required = true) String code){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		
		Optional<PaymentType> isPresentPaymentType = paymentTypeService.findByCode(code);
		if(isPresentPaymentType.isPresent()) {
			return new ResponseEntity<>(isPresentPaymentType.get(), HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	
	@GetMapping(value = "/{tenantId}/name/{name}")
	public ResponseEntity<Object> getPaymentTypeByName(@PathVariable(value = "tenantId", required = true) String tenantId,
															        @PathVariable(value = "name", required = true) String name){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		
		List<PaymentType> isPresentPaymentType = paymentTypeService.findByName(name);
		if(!isPresentPaymentType.isEmpty()) {
			return new ResponseEntity<>(isPresentPaymentType, HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	
	@GetMapping(value = "/{tenantId}/status/{status}")
	public ResponseEntity<Object> getPaymentTypeByStatus(@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "status", required = true) String status) {

		List<PaymentType> list = paymentTypeService.findByStatus(status);
		if (!list.isEmpty())
			return new ResponseEntity<>(list, HttpStatus.OK);
		else {
			return new ResponseEntity<>(list, HttpStatus.NO_CONTENT);
		}
	}
	
	
	@PostMapping("/{tenantId}")
	public ResponseEntity<Object> addPaymentTypes(@PathVariable(value = "tenantId", required = true) String tenantId,
			   									       		  @Valid @RequestBody PaymentTypeAddResource paymentTypeAddResource){
		PaymentType paymentType = paymentTypeService.save(tenantId, paymentTypeAddResource);
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource(getEnvironment().getProperty(RECORD_CREATED), Long.toString(paymentType.getId()));
		return new ResponseEntity<>(responseMessage,HttpStatus.CREATED);
	}
	
	
	@PutMapping(value = "{tenantId}/{id}")
	public ResponseEntity<Object> updatePaymentTypes(@PathVariable(value = "tenantId", required = true) String tenantId, 
												                 @PathVariable(value = "id", required = true) Long id, 
												                 @Valid @RequestBody PaymentTypeUpdateResource paymentTypeUpdateResource){
		SuccessAndErrorDetailsResource successAndErrorDetailsResource=new SuccessAndErrorDetailsResource();
		Optional<PaymentType>isPresentPaymentType = paymentTypeService.findById(id);		
		if(isPresentPaymentType.isPresent()) {
			paymentTypeUpdateResource.setId(id.toString());
			PaymentType paymentType = paymentTypeService.update(tenantId, paymentTypeUpdateResource);
			successAndErrorDetailsResource = new SuccessAndErrorDetailsResource(getEnvironment().getProperty(RECORD_UPDATED), paymentType.getId().toString());
			return new ResponseEntity<>(successAndErrorDetailsResource,HttpStatus.OK);
		}
		else {
			successAndErrorDetailsResource.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
}
