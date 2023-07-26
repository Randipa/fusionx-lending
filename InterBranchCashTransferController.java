package com.fusionx.central.cash.management.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fusionx.central.cash.management.base.MessagePropertyBase;
import com.fusionx.central.cash.management.domain.InterBranchCashTransfer;
import com.fusionx.central.cash.management.resource.CashTransferRequestRejectNoteResource;
import com.fusionx.central.cash.management.resource.InterBranchCashTransferAddResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.InterBranchCashTransferService;

@RestController
@RequestMapping("/inter-branch-cash-transfer")
@CrossOrigin("*")
public class InterBranchCashTransferController extends MessagePropertyBase {

	@Autowired
	InterBranchCashTransferService interBranchCashTransferService;
	
	
	/**
	 * Adds the branch parameter.
	 *
	 * @param tenantId the tenant id
	 * @param interBranchCashTransferAddResource the inter branch cash transfer add resource
	 * @return the response entity
	 */
	@PostMapping(value = "/{tenantId}")
	public ResponseEntity<Object> addBranchParameter(@PathVariable(value = "tenantId", required = true) String tenantId,
			@Valid @RequestBody InterBranchCashTransferAddResource interBranchCashTransferAddResource) {
		
		InterBranchCashTransfer interBranchCashTransfer = interBranchCashTransferService.createInterBranchCashTransferRequest(tenantId, interBranchCashTransferAddResource);
		SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(RECORD_CREATED), interBranchCashTransfer.getId().toString());
		return new ResponseEntity<>(successDetailsDto,HttpStatus.CREATED);
	}

	/**
	 * Approve cash transfer request.
	 *
	 * @param tenantId the tenant id
	 * @param cashTransferRequestId the cash transfer request id
	 * @return the response entity
	 */
	@PutMapping(value = "{tenantId}/approve/cash-transfer-request-id/{cashTransferRequestId}")
	public ResponseEntity<Object> approveCashTransferRequest(@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "cashTransferRequestId", required = true) Long cashTransferRequestId) {

		SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
		Optional<InterBranchCashTransfer> isPresentInterBranchCashTransferPending = interBranchCashTransferService
				.findById(cashTransferRequestId);
		if (isPresentInterBranchCashTransferPending.isPresent()) {

			interBranchCashTransferService.approve(cashTransferRequestId);
			successAndErrorDetailsResource = new SuccessAndErrorDetailsResource(environment.getProperty("common.approved"), cashTransferRequestId.toString());
			return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.OK);

		} else {
			successAndErrorDetailsResource.setMessages(environment.getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
	
	/**
	 * Reject cash transfer request.
	 *
	 * @param tenantId the tenant id
	 * @param cashTransferRequestId the cash transfer request id
	 * @param cashTransferRequestRejectNoteResource the cash transfer request reject note resource
	 * @return the response entity
	 */
	@PutMapping(value = "{tenantId}/reject/cash-transfer-request-id/{cashTransferRequestId}")
	public ResponseEntity<Object> rejectCashTransferRequest(@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "cashTransferRequestId", required = true) Long cashTransferRequestId, @Valid @RequestBody CashTransferRequestRejectNoteResource cashTransferRequestRejectNoteResource) {
		
		SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
		Optional<InterBranchCashTransfer> isPresentInterBranchCashTransferPending = interBranchCashTransferService
				.findById(cashTransferRequestId);
		if (isPresentInterBranchCashTransferPending.isPresent()) {
			
			interBranchCashTransferService.reject(cashTransferRequestId, cashTransferRequestRejectNoteResource);
			successAndErrorDetailsResource = new SuccessAndErrorDetailsResource(environment.getProperty("common.approved"), cashTransferRequestId.toString());
			return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.OK);
			
		} else {
			successAndErrorDetailsResource.setMessages(environment.getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

}
