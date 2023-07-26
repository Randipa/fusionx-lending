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
import com.fusionx.central.cash.management.domain.PaymentCategory;
import com.fusionx.central.cash.management.resource.PaymentCategoryAddResource;
import com.fusionx.central.cash.management.resource.PaymentCategoryUpdateResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.PaymentCategoryService;

import io.micrometer.core.annotation.Timed;

@RestController
@RequestMapping(value = "/payment-category")
@CrossOrigin(origins = "*")
@Timed
public class PaymentCategoryController extends MessagePropertyBase{
	
	@Autowired
	private PaymentCategoryService paymentCategoryService;
	
	
	
	@GetMapping("/{tenantId}/all")
	public ResponseEntity<Object> getAllPaymentCategory(@PathVariable(value = "tenantId", required = true) String tenantId){
		List<PaymentCategory> list = paymentCategoryService.findAll();
	      if(!list.isEmpty()) 
          return new ResponseEntity<>(list, HttpStatus.OK);
      else {
          return new ResponseEntity<>(list, HttpStatus.NO_CONTENT);
      }
	}
	
	
	
	@GetMapping(value = "/{tenantId}/{id}")
	public ResponseEntity<Object> getPaymentCategoryById(@PathVariable(value = "tenantId", required = true) String tenantId,
													         	  @PathVariable(value = "id", required = true) Long id){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		
		Optional<PaymentCategory> isPresentPaymentCategory = paymentCategoryService.findById(id);
		if(isPresentPaymentCategory.isPresent()) {
			return new ResponseEntity<>(isPresentPaymentCategory.get(), HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	
	@GetMapping(value = "/{tenantId}/code/{code}")
	public ResponseEntity<Object> getPaymentCategoryByCode(@PathVariable(value = "tenantId", required = true) String tenantId,
													   	            @PathVariable(value = "code", required = true) String code){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		
		Optional<PaymentCategory> isPresentPaymentCategory = paymentCategoryService.findByCode(code);
		if(isPresentPaymentCategory.isPresent()) {
			return new ResponseEntity<>(isPresentPaymentCategory.get(), HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	
	@GetMapping(value = "/{tenantId}/name/{name}")
	public ResponseEntity<Object> getPaymentCategoryByName(@PathVariable(value = "tenantId", required = true) String tenantId,
															        @PathVariable(value = "name", required = true) String name){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		
		List<PaymentCategory> isPresentPaymentCategory = paymentCategoryService.findByName(name);
		if(!isPresentPaymentCategory.isEmpty()) {
			return new ResponseEntity<>(isPresentPaymentCategory, HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	
	@GetMapping(value = "/{tenantId}/status/{status}")
	public ResponseEntity<Object> getPaymentCategoryByStatus(@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "status", required = true) String status) {

		List<PaymentCategory> list = paymentCategoryService.findByStatus(status);
		if (!list.isEmpty())
			return new ResponseEntity<>(list, HttpStatus.OK);
		else {
			return new ResponseEntity<>(list, HttpStatus.NO_CONTENT);
		}
	}
	
	
	@PostMapping("/{tenantId}")
	public ResponseEntity<Object> addPaymentCategory(@PathVariable(value = "tenantId", required = true) String tenantId,
			   									       		  @Valid @RequestBody PaymentCategoryAddResource paymentCategoryAddResource){
		PaymentCategory paymentCategory = paymentCategoryService.save(tenantId, paymentCategoryAddResource);
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource(getEnvironment().getProperty(RECORD_CREATED), Long.toString(paymentCategory.getId()));
		return new ResponseEntity<>(responseMessage,HttpStatus.CREATED);
	}
	
	
	@PutMapping(value = "{tenantId}/{id}")
	public ResponseEntity<Object> updatePaymentCategory(@PathVariable(value = "tenantId", required = true) String tenantId, 
												                 @PathVariable(value = "id", required = true) Long id, 
												                 @Valid @RequestBody PaymentCategoryUpdateResource paymentCategoryUpdateResource){
		SuccessAndErrorDetailsResource successAndErrorDetailsResource=new SuccessAndErrorDetailsResource();
		Optional<PaymentCategory>isPresentPaymentCategory = paymentCategoryService.findById(id);		
		if(isPresentPaymentCategory.isPresent()) {
			paymentCategoryUpdateResource.setId(id.toString());
			PaymentCategory paymentCategory = paymentCategoryService.update(tenantId, paymentCategoryUpdateResource);
			successAndErrorDetailsResource = new SuccessAndErrorDetailsResource(getEnvironment().getProperty(RECORD_UPDATED), paymentCategory.getId().toString());
			return new ResponseEntity<>(successAndErrorDetailsResource,HttpStatus.OK);
		}
		else {
			successAndErrorDetailsResource.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

}
