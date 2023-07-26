package com.fusionx.central.cash.management.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.fusionx.central.cash.management.resource.ChequeLeavesUpdateRequestResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fusionx.central.cash.management.base.MessagePropertyBase;
import com.fusionx.central.cash.management.core.LogginAuthentcation;
import com.fusionx.central.cash.management.domain.ChequeDepositBatchChequeLeaves;
import com.fusionx.central.cash.management.exception.ValidateRecordException;
import com.fusionx.central.cash.management.resource.ChequeDepositBatchChequeLeavesRequestResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.ChequeDepositChequeLeavesService;

@RestController
@RequestMapping(value = "/outward-cheque-leaves")
@CrossOrigin(origins = "*")
public class OutwardChequeCreationController extends MessagePropertyBase {

	@Autowired
	private ChequeDepositChequeLeavesService chequeDepositChequeLeavesService;

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
			@Valid @RequestBody List<ChequeDepositBatchChequeLeavesRequestResource> chequeDepositBatchChequeLeavesRequestResourceList) {

		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new ValidateRecordException(getEnvironment().getProperty(COMMON_NOT_NULL), "username");

		chequeDepositChequeLeavesService.saveAndValidateChequeDepositBatchChequeLeaves(tenantId, LogginAuthentcation.getUserName(), chequeDepositBatchChequeLeavesRequestResourceList);
		SuccessAndErrorDetailsResource successDetailsDto=new SuccessAndErrorDetailsResource(environment.getProperty("common.saved"));
    	return new ResponseEntity<>(successDetailsDto,HttpStatus.CREATED);
	}
	
	@GetMapping(value = "/{tenantId}/created-branch/{createdBranchId}")
	public ResponseEntity<Object> getByCreatedBranchId(@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "createdBranchId", required = true) Long createdBranchId) {

		List<ChequeDepositBatchChequeLeaves> chequeDepositBatchChequeLeaves = chequeDepositChequeLeavesService.findByCreatedBranchId(createdBranchId);
		if (!chequeDepositBatchChequeLeaves.isEmpty()) {
			return new ResponseEntity<>(chequeDepositBatchChequeLeaves, HttpStatus.OK);
		} else {
			SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
			responseMessage.setMessages(environment.getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * Update outward cheque leaves response entity.
	 *
	 * @param tenantId                          the tenant id
	 * @param chequeLeavesUpdateRequestResource the cheque leaves update request resource
	 * @return the response entity
	 */
	@PutMapping(value = "/{tenantId}")
	public ResponseEntity<Object> updateOutwardChequeLeaves(
			@PathVariable(value = "tenantId", required = true) String tenantId,
			@Valid @RequestBody List<ChequeLeavesUpdateRequestResource> chequeLeavesUpdateRequestResource) {

		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new ValidateRecordException(getEnvironment().getProperty(COMMON_NOT_NULL), "username");

		chequeDepositChequeLeavesService.updateOutwardChequeLeaves(tenantId, LogginAuthentcation.getUserName(), chequeLeavesUpdateRequestResource);
		SuccessAndErrorDetailsResource successDetailsDto=new SuccessAndErrorDetailsResource(environment.getProperty("common.saved"));
		return new ResponseEntity<>(successDetailsDto,HttpStatus.OK);
	}
	
	@GetMapping(value = "/{tenantId}/cheque-leave-id/{chequeLeaveId}")
	public ResponseEntity<Object> getByChequeLeaveId(@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "chequeLeaveId", required = true) Long chequeLeaveId) {

		Optional<ChequeDepositBatchChequeLeaves> chequeDepositBatchChequeLeaves = chequeDepositChequeLeavesService.getByChequeLeaveId(chequeLeaveId);
		if (chequeDepositBatchChequeLeaves.isPresent()) {
			return new ResponseEntity<>(chequeDepositBatchChequeLeaves, HttpStatus.OK);
		} else {
			SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
			responseMessage.setMessages(environment.getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}

}
