package com.fusionx.central.cash.management.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.fusionx.central.cash.management.resource.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fusionx.central.cash.management.base.MessagePropertyBase;
import com.fusionx.central.cash.management.core.LogginAuthentcation;
import com.fusionx.central.cash.management.domain.OutwardChequeClearenceBatch;
import com.fusionx.central.cash.management.enums.OutwardChequeClearenceStatus;
import com.fusionx.central.cash.management.exception.ValidateRecordException;
import com.fusionx.central.cash.management.service.OutwardChequeClearenceService;

/**
 * The type Outward cheque clearence controller.
 */
@RestController
@RequestMapping(value = "/outward-batch")
@CrossOrigin(origins = "*")
public class OutwardChequeClearenceController extends MessagePropertyBase{

	@Autowired
	private OutwardChequeClearenceService outwardChequeClearenceService;

	/**
	 * Save OutwardChequeClearenceBatch
	 * 
	 * @param @PathVariable{tenantId}
	 * @param @RequestBody{OutwardChequeClearenceRequestResource}
	 * @return SuccessAndErrorDetailsDto
	 */
	@PostMapping(value = "/{tenantId}")
	public ResponseEntity<Object> addOutwardChequeClearenceBatch(
			@PathVariable(value = "tenantId", required = true) String tenantId,
			@Valid @RequestBody OutwardChequeClearenceRequestResource outwardChequeClearenceRequestResource) {

		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new ValidateRecordException(getEnvironment().getProperty(COMMON_NOT_NULL), "username");

		Long id = outwardChequeClearenceService.saveAndValidateOutwardChequeClearenceBatch(tenantId,LogginAuthentcation.getUserName(), outwardChequeClearenceRequestResource);
		SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty("common.saved"), id.toString());
		return new ResponseEntity<>(successDetailsDto, HttpStatus.CREATED);
	}
	
	
	/**
	 * Save OutwardChequeBatch Details
	 * @param @PathVariable{tenantId}
	 * @param @RequestBody{OutwardChequeLeavesBatchBodyRequestResource}
	 * @return SuccessAndErrorDetailsDto
	 */
	@PostMapping(value = "/{tenantId}/{outwardChequeClearencebatchId}")
	public ResponseEntity<Object> addOutwardChequeBatchDetail(
			@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "outwardChequeClearencebatchId", required = true) Long outwardChequeClearencebatchId,
			@Valid @RequestBody OutwardChequeLeavesBatchBodyRequestResource outwardChequeLeavesBatchBodyRequestResource) {

		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new ValidateRecordException(getEnvironment().getProperty(COMMON_NOT_NULL), "username");

		if(outwardChequeClearenceService.existsByIdAndStatus(outwardChequeClearencebatchId, OutwardChequeClearenceStatus.CREATED)) {
			outwardChequeClearenceService.saveAndValidateOutwardChequeBatchDetails(tenantId, LogginAuthentcation.getUserName(), outwardChequeLeavesBatchBodyRequestResource.getChequeLeaves(), outwardChequeClearencebatchId);
			SuccessAndErrorDetailsResource successDetailsDto=new SuccessAndErrorDetailsResource(environment.getProperty("common.saved"));
	    	return new ResponseEntity<>(successDetailsDto,HttpStatus.CREATED);
		}else {
			SuccessAndErrorDetailsResource successDetailsDto=new SuccessAndErrorDetailsResource(environment.getProperty("invalid.outwardChequeClearenceBatchId"));
	    	return new ResponseEntity<>(successDetailsDto,HttpStatus.UNPROCESSABLE_ENTITY);
		}

	}
	
	
	/**
	    * finish-or-cancel OutwardCheque Clearence Batch
	    * @param @PathVariable{tenantId}
	    * @param @PathVariable{outwardChequeClearenceBatchId}
	    * @param @RequestBody{OutwardChequeClearenceBatchFinishRequestResource}
	    * @return SuccessAndErrorDetailsDto
	    */
		@PostMapping(value = "/{tenantId}/finish-or-cancel/{outwardChequeClearenceBatchId}")
		public ResponseEntity<Object> finishOrCancelChequeDepositBatch(@PathVariable(value = "tenantId", required = true) String tenantId,
				@PathVariable(value = "outwardChequeClearenceBatchId", required = true) Long outwardChequeClearenceBatchId,
				@Valid @RequestBody OutwardChequeClearenceBatchFinishRequestResource outwardChequeClearenceBatchFinishRequestResource) {
			
			if(LogginAuthentcation.getUserName()==null || LogginAuthentcation.getUserName().isEmpty()) 
				throw new ValidateRecordException(getEnvironment().getProperty(COMMON_NOT_NULL), "username");
			
			if(outwardChequeClearenceService.existsByIdAndStatus(outwardChequeClearenceBatchId, OutwardChequeClearenceStatus.CREATED)) {
				outwardChequeClearenceService.updateOutwardChequeClearenceBatchStatus(tenantId, LogginAuthentcation.getUserName(), outwardChequeClearenceBatchFinishRequestResource, outwardChequeClearenceBatchId);
				SuccessAndErrorDetailsResource successDetailsDto=new SuccessAndErrorDetailsResource(environment.getProperty("common.saved"));
		    	return new ResponseEntity<>(successDetailsDto,HttpStatus.CREATED);
			}else {
				SuccessAndErrorDetailsResource successDetailsDto=new SuccessAndErrorDetailsResource(environment.getProperty("invalid.outwardChequeClearenceBatchId"));
		    	return new ResponseEntity<>(successDetailsDto,HttpStatus.UNPROCESSABLE_ENTITY);
			}
		}
		
		
		@PostMapping(value = "/{tenantId}/clearing")
		public ResponseEntity<Object> clearingOutwardBatchChequeLeaves(@PathVariable(value = "tenantId", required = true) String tenantId,
				@Valid @RequestBody OutwardChequeBatchClearingChequeLeavesBodyRequestResource outwardChequeBatchClearingChequeLeavesBodyRequestResource) {
			
			if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty()) {
				throw new ValidateRecordException(getEnvironment().getProperty(COMMON_NOT_NULL), "username");
			}
			
			outwardChequeClearenceService.saveAndValidateOutwardChequeBatchClearingChequeLeaves(tenantId, LogginAuthentcation.getUserName(), outwardChequeBatchClearingChequeLeavesBodyRequestResource.getChequeLeaves());
			SuccessAndErrorDetailsResource successDetailsDto=new SuccessAndErrorDetailsResource(environment.getProperty("common.saved"));
	    	return new ResponseEntity<>(successDetailsDto,HttpStatus.CREATED);
		}

	@GetMapping(value = "/{tenantId}/{id}")
	public ResponseEntity<Object> getByBatchId(@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "id", required = true) Long id) {

		Optional<OutwardChequeClearenceBatch> isPresentOutwardChequeClearenceBatch = outwardChequeClearenceService.findById(id);
		if (isPresentOutwardChequeClearenceBatch.isPresent()) {
			return new ResponseEntity<>(isPresentOutwardChequeClearenceBatch.get(), HttpStatus.OK);
		} else {
			SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
			responseMessage.setMessages(RECORD_NOT_FOUND);
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}

	@GetMapping(value = "/{tenantId}/created-branch/{createdBranchId}/status/{status}")
	public ResponseEntity<Object> getByCreatedBranchIdAndStatus(@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "createdBranchId", required = true) Long createdBranchId,
			@PathVariable(value = "status", required = true) String status) {

		List<OutwardChequeClearenceBatch> outwardChequeClearenceBatch = outwardChequeClearenceService.findByCreatedBranchIdAndStatus(createdBranchId, status);
		if (!outwardChequeClearenceBatch.isEmpty()) {
			return new ResponseEntity<>(outwardChequeClearenceBatch, HttpStatus.OK);
		} else {
			SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
			responseMessage.setMessages(environment.getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * Gets pending cheque leaves by batch id.
	 *
	 * @param tenantId the tenant id
	 * @param id       the id
	 * @return the pending cheque leaves by batch id
	 */
	@GetMapping(value = "/{tenantId}/pending/{id}")
	public ResponseEntity<Object> getPendingChequeLeavesByBatchId(@PathVariable(value = "tenantId", required = true) String tenantId,  @PathVariable(value = "id", required = true) Long id) {
		Optional<OutwardChequeClearenceBatch> isPresentOutwardChequeClearenceBatch = outwardChequeClearenceService.getPendingChequeLeavesByBatchId(id);
		if (isPresentOutwardChequeClearenceBatch.isPresent()) {
			return new ResponseEntity<>(isPresentOutwardChequeClearenceBatch.get(), HttpStatus.OK);
		} else {
			SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
			responseMessage.setMessages(RECORD_NOT_FOUND);
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * Update outward cheque batch detail response entity.
	 *
	 * @param tenantId                                          the tenant id
	 * @param outwardChequeClearencebatchId                     the outward cheque clearencebatch id
	 * @param outwardChequeLeavesBatchUpdateBodyRequestResource the outward cheque leaves batch update body request resource
	 * @return the response entity
	 */
	@PutMapping(value = "/{tenantId}/{outwardChequeClearencebatchId}")
	public ResponseEntity<Object> updateOutwardChequeBatchDetail(
			@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "outwardChequeClearencebatchId", required = true) Long outwardChequeClearencebatchId,
			@Valid @RequestBody OutwardChequeLeavesBatchUpdateBodyRequestResource outwardChequeLeavesBatchUpdateBodyRequestResource) {

		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new ValidateRecordException(getEnvironment().getProperty(COMMON_NOT_NULL), "username");

		if(outwardChequeClearenceService.existsByIdAndStatus(outwardChequeClearencebatchId, OutwardChequeClearenceStatus.RETURN_BUT_PRESENT)) {
			outwardChequeClearenceService.updateAndValidateOutwardChequeBatchDetails(tenantId, LogginAuthentcation.getUserName(), outwardChequeLeavesBatchUpdateBodyRequestResource, outwardChequeClearencebatchId);
			SuccessAndErrorDetailsResource successDetailsDto=new SuccessAndErrorDetailsResource(environment.getProperty("common.saved"));
			return new ResponseEntity<>(successDetailsDto,HttpStatus.OK);
		}else {
			SuccessAndErrorDetailsResource successDetailsDto=new SuccessAndErrorDetailsResource(environment.getProperty("invalid.outwardChequeClearenceBatchId"));
			return new ResponseEntity<>(successDetailsDto,HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

}
