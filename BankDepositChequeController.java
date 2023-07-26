package com.fusionx.central.cash.management.controller;

import com.fusionx.central.cash.management.base.MessagePropertyBase;
import com.fusionx.central.cash.management.core.LogginAuthentcation;
import com.fusionx.central.cash.management.domain.BankDepositBatch;
import com.fusionx.central.cash.management.exception.ValidateRecordException;
import com.fusionx.central.cash.management.resource.BankDepositChequeBatchClearingChequeLeavesBodyRequestResource;
import com.fusionx.central.cash.management.resource.BankDepositChequeRequestResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.BankDepositChequeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

/**
 *  The type Bank deposit cheque controller.
 *
 ********************************************************************************************************
 *  ###   Date         Story Point   Task No    Author       Description
 *-------------------------------------------------------------------------------------------------------
 *    1   26-04-2022      		     FX-10215 	MenukaJ      Created
 *
 ********************************************************************************************************
 */
@RestController
@RequestMapping(value = "/bank-deposit-cheque")
@CrossOrigin(origins = "*")
public class BankDepositChequeController extends MessagePropertyBase {

    @Autowired
    private Environment environment;

    @Autowired
    private BankDepositChequeService bankDepositChequeService;

    private String userNotFound="common.user-not-found";
    private String commonSaved="common.saved";
    private String commonNotFound = "common.not-found";

    /**
     * Add cheque deposit batch response entity.
     *
     * @param tenantId                         the tenant id
     * @param bankDepositChequeRequestResource the bank deposit cheque request resource
     * @return the response entity
     */
    @PostMapping(value = "/{tenantId}")
    public ResponseEntity<Object> addChequeDepositBatch(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                        @Valid @RequestBody BankDepositChequeRequestResource bankDepositChequeRequestResource) {

        if (LogginAuthentcation.getUserName()==null || LogginAuthentcation.getUserName().isEmpty() )
            throw new ValidateRecordException(environment.getProperty(COMMON_NOT_NULL), "username");

        Long id =bankDepositChequeService.saveAndValidateBankDepositCheque(tenantId, bankDepositChequeRequestResource, LogginAuthentcation.getUserName());
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
        if (id != null) {
            successAndErrorDetailsResource.setMessages(environment.getProperty("common.saved"));
            successAndErrorDetailsResource.setValue(id.toString());
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.CREATED);
        } else
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * Clearing bank deposit batch cheque leaves response entity.
     *
     * @param tenantId the tenant id
     * @param request  the request
     * @return the response entity
     */
    @PostMapping(value = "/{tenantId}/clearing")
    public ResponseEntity<Object> clearingBankDepositBatchChequeLeaves(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                                   @Valid @RequestBody BankDepositChequeBatchClearingChequeLeavesBodyRequestResource request) {

        if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty()) {
            throw new ValidateRecordException(getEnvironment().getProperty(COMMON_NOT_NULL), "username");
        }
        bankDepositChequeService.saveAndValidateBankDepChequeBatchClearingChequeLeaves(tenantId, LogginAuthentcation.getUserName(), request.getChequeLeaves());
        SuccessAndErrorDetailsResource successDetailsDto=new SuccessAndErrorDetailsResource(environment.getProperty("common.saved"));
        return new ResponseEntity<>(successDetailsDto,HttpStatus.CREATED);
    }
    
    @GetMapping(value = "/{tenantId}/batch/{id}")
	public ResponseEntity<Object> findByBatchId(@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "id", required = true) Long id) {

		Optional<BankDepositBatch> isPresentBankDepositBatch = bankDepositChequeService.findByBatchId(id);
		if (isPresentBankDepositBatch.isPresent()) {
			return new ResponseEntity<>(isPresentBankDepositBatch.get(), HttpStatus.OK);
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

		List<BankDepositBatch> bankDepositBatch = bankDepositChequeService.findByCreatedBranchIdAndStatus(createdBranchId, status);
		if (!bankDepositBatch.isEmpty()) {
			return new ResponseEntity<>(bankDepositBatch, HttpStatus.OK);
		} else {
			SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
			responseMessage.setMessages(environment.getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping(value = "/{tenantId}/batch/all")
	public ResponseEntity<Object> findAll(@PathVariable(value = "tenantId", required = true) String tenantId) {

		List<BankDepositBatch> bankDepositBatch = bankDepositChequeService.findAll();
		if (!bankDepositBatch.isEmpty()) {
			return new ResponseEntity<>(bankDepositBatch, HttpStatus.OK);
		} else {
			SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
			responseMessage.setMessages(environment.getProperty(RECORD_NOT_FOUND));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}

}
