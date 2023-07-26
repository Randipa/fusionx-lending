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
import com.fusionx.central.cash.management.domain.CashDeliveryMethod;
import com.fusionx.central.cash.management.resource.CashDeliveryMethodAddResource;
import com.fusionx.central.cash.management.resource.CashDeliveryMethodUpdateResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.CashDeliveryMethodService;

import io.micrometer.core.annotation.Timed;

@RestController
@RequestMapping(value = "/cash-delivery-method")
@CrossOrigin(origins = "*")
@Timed
public class CashDeliveryMethodController extends MessagePropertyBase{

	@Autowired
	private CashDeliveryMethodService cashDeliveryMethodService;
	
	
	
	@GetMapping("/{tenantId}/all")
	public ResponseEntity<Object> getAllCashDeliveryMethod(@PathVariable(value = "tenantId", required = true) String tenantId){
		List<CashDeliveryMethod> list = cashDeliveryMethodService.findAll();
	      if(!list.isEmpty()) 
          return new ResponseEntity<>(list, HttpStatus.OK);
      else {
          return new ResponseEntity<>(list, HttpStatus.NO_CONTENT);
      }
	}
	
	
	
	@GetMapping(value = "/{tenantId}/{id}")
	public ResponseEntity<Object> getCashDeliveryMethodById(@PathVariable(value = "tenantId", required = true) String tenantId,
													         	  @PathVariable(value = "id", required = true) Long id){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		
		Optional<CashDeliveryMethod> isPresentCashDeliveryMethod = cashDeliveryMethodService.findById(id);
		if(isPresentCashDeliveryMethod.isPresent()) {
			return new ResponseEntity<>(isPresentCashDeliveryMethod.get(), HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	
	@GetMapping(value = "/{tenantId}/code/{code}")
	public ResponseEntity<Object> getCashDeliveryMethodByCode(@PathVariable(value = "tenantId", required = true) String tenantId,
													   	            @PathVariable(value = "code", required = true) String code){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		
		Optional<CashDeliveryMethod> isPresentCashDeliveryMethod = cashDeliveryMethodService.findByCode(code);
		if(isPresentCashDeliveryMethod.isPresent()) {
			return new ResponseEntity<>(isPresentCashDeliveryMethod.get(), HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	
	@GetMapping(value = "/{tenantId}/name/{name}")
	public ResponseEntity<Object> getCashDeliveryMethodByName(@PathVariable(value = "tenantId", required = true) String tenantId,
															        @PathVariable(value = "name", required = true) String name){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		
		List<CashDeliveryMethod> isPresentCashDeliveryMethod = cashDeliveryMethodService.findByName(name);
		if(!isPresentCashDeliveryMethod.isEmpty()) {
			return new ResponseEntity<>(isPresentCashDeliveryMethod, HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	
	@GetMapping(value = "/{tenantId}/status/{status}")
	public ResponseEntity<Object> getCashDeliveryMethodByStatus(@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "status", required = true) String status) {

		List<CashDeliveryMethod> list = cashDeliveryMethodService.findByStatus(status);
		if (!list.isEmpty())
			return new ResponseEntity<>(list, HttpStatus.OK);
		else {
			return new ResponseEntity<>(list, HttpStatus.NO_CONTENT);
		}
	}
	
	
	@PostMapping("/{tenantId}")
	public ResponseEntity<Object> addCashDeliveryMethod(@PathVariable(value = "tenantId", required = true) String tenantId,
			   									       		  @Valid @RequestBody CashDeliveryMethodAddResource cashDeliveryMethodAddResource){
		CashDeliveryMethod cashDeliveryMethod = cashDeliveryMethodService.save(tenantId, cashDeliveryMethodAddResource);
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource(getEnvironment().getProperty(RECORD_CREATED), Long.toString(cashDeliveryMethod.getId()));
		return new ResponseEntity<>(responseMessage,HttpStatus.CREATED);
	}
	
	
	@PutMapping(value = "{tenantId}/{id}")
	public ResponseEntity<Object> updateCashDeliveryMethod(@PathVariable(value = "tenantId", required = true) String tenantId, 
												                 @PathVariable(value = "id", required = true) Long id, 
												                 @Valid @RequestBody CashDeliveryMethodUpdateResource cashDeliveryMethodUpdateResource){
		SuccessAndErrorDetailsResource successAndErrorDetailsResource=new SuccessAndErrorDetailsResource();
		Optional<CashDeliveryMethod>isPresentCashDeliveryMethod = cashDeliveryMethodService.findById(id);		
		if(isPresentCashDeliveryMethod.isPresent()) {
			cashDeliveryMethodUpdateResource.setId(id.toString());
			CashDeliveryMethod cashDeliveryMethod = cashDeliveryMethodService.update(tenantId, cashDeliveryMethodUpdateResource);
			successAndErrorDetailsResource = new SuccessAndErrorDetailsResource(getEnvironment().getProperty(RECORD_UPDATED), cashDeliveryMethod.getId().toString());
			return new ResponseEntity<>(successAndErrorDetailsResource,HttpStatus.OK);
		}
		else {
			successAndErrorDetailsResource.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
}
