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
import com.fusionx.central.cash.management.domain.PaymentMethod;
import com.fusionx.central.cash.management.enums.CommonStatus;
import com.fusionx.central.cash.management.resource.AddPaymentMethodResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.resource.UpdatePaymentMethodResource;
import com.fusionx.central.cash.management.service.PaymentMethodService;

/**
 * Payment Method Controller
 * 
 ********************************************************************************************************
 *  ###   Date         Story Point   Task No    Author       Description
 *-------------------------------------------------------------------------------------------------------
 *    1  19-08-2020   FX-4508 		  FX-4449 	 ThavinyaR 	 Created
 *    
 ********************************************************************************************************
 */

@RestController
@RequestMapping("/payment-method")
@CrossOrigin("*")
public class PaymentMethodController extends MessagePropertyBase{

	@Autowired
	private PaymentMethodService paymentMethodService;
	
	/**
	 * get all payment methods
	 * @param @PathVariable{tenantId}
	 * @param @PathVariable{all}
	 * @return List
	 **/
	
	@GetMapping("/{tenantId}/all")
	public ResponseEntity<Object> getAllPaymentMethods(@PathVariable(value = "tenantId", required = true) String tenantId){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		List<PaymentMethod>isPresentPaymentMethods= paymentMethodService.findall();
		if(!isPresentPaymentMethods.isEmpty()) {
			return new ResponseEntity<>((Collection<PaymentMethod>)isPresentPaymentMethods,HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * get payment method by ID
	 * @param @pathVariable{tenantId}
	 * @param @pathVariable {id}
	 * @return Optional
	 **/
	@GetMapping(value = "/{tenantId}/{id}")
	public ResponseEntity<Object> getAllPaymentMethodById(@PathVariable(value = "tenantId", required = true) String tenantId,
													   @PathVariable(value = "id", required = true) Long id){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		Optional<PaymentMethod> isPresentPaymentMethods = paymentMethodService.findById(id);
		if(isPresentPaymentMethods.isPresent()) {
			return new ResponseEntity<>(isPresentPaymentMethods.get(), HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * get payment method by code
	 * @param @pathVariable{tenantId}
	 * @param @pathVariable {code}
	 * @return Optional
	 **/
	@GetMapping(value = "/{tenantId}/code/{code}")
	public ResponseEntity<Object> getPaymentMethodByCode(@PathVariable(value = "tenantId", required = true) String tenantId,
													   @PathVariable(value = "code", required = true) String code){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		Optional<PaymentMethod> isPresentPaymentMethods = paymentMethodService.findByCode(code);
		if(isPresentPaymentMethods.isPresent()) {
			return new ResponseEntity<>(isPresentPaymentMethods.get(), HttpStatus.OK);
		}
		else {
			responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * get payment method by status
	 * @param @pathVariable{tenantId}
	 * @param @pathVariable {status}
	 * @return Optional
	 **/
	@GetMapping(value = "/{tenantId}/status/{status}")
	public ResponseEntity<Object> getPaymentMethodByStatus(@PathVariable(value = "tenantId", required = true) String tenantId,
													   @PathVariable(value = "status", required = true) String status){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		if(status.equals(CommonStatus.ACTIVE.toString()) || status.equals(CommonStatus.INACTIVE.toString())) {
			List<PaymentMethod> isPresentPaymentMethods = paymentMethodService.findByStatus(status);
			if(!isPresentPaymentMethods.isEmpty()) {
				return new ResponseEntity<>(isPresentPaymentMethods, HttpStatus.OK);
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
     * save PaymentMethod
     * @param @PathVariable{tenantId}
     * @param @RequestBody{AddTestResource}
     * @return SuccessAndErrorDetailsDto
     */
	@PostMapping("/{tenantId}")
	public ResponseEntity<Object> addPaymentMethod(@PathVariable(value = "tenantId", required = true) String tenantId,
			   									           @Valid @RequestBody AddPaymentMethodResource addPaymentMethodResource){
		PaymentMethod paymentMethod = paymentMethodService.addPaymentMethod(tenantId, addPaymentMethodResource);
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource(getEnvironment().getProperty(RECORD_CREATED), addPaymentMethodResource.getCode());
		return new ResponseEntity<>(responseMessage,HttpStatus.CREATED);
	}
	
	/**
     * update PaymentMethod
     * @param @PathVariable{tenantId}
     * @param @RequestBody{CommonUpdateResource}
     * @return SuccessAndErrorDetailsDto
     */
	@PutMapping(value = "{tenantId}/{id}")
	public ResponseEntity<Object> updatePaymentMethod(@PathVariable(value = "tenantId", required = true) String tenantId, 
										              @PathVariable(value = "id", required = true) Long id, 
										              @Valid @RequestBody UpdatePaymentMethodResource updatePaymentMethodResource){
		SuccessAndErrorDetailsResource successAndErrorDetailsResource=new SuccessAndErrorDetailsResource();
		Optional<PaymentMethod>isPresentClearingType = paymentMethodService.findById(id);
		
		if(isPresentClearingType.isPresent()) {
			updatePaymentMethodResource.setId(id.toString());
			PaymentMethod paymentMethod = paymentMethodService.updatePaymentMethod(tenantId, updatePaymentMethodResource);
			successAndErrorDetailsResource = new SuccessAndErrorDetailsResource(getEnvironment().getProperty(RECORD_UPDATED), paymentMethod.getId().toString());
			return new ResponseEntity<>(successAndErrorDetailsResource,HttpStatus.OK);
		}
		else {
			successAndErrorDetailsResource.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	
}
