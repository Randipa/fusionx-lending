package com.fusionx.central.cash.management.controller;

import com.fusionx.central.cash.management.base.MessagePropertyBase;
import com.fusionx.central.cash.management.core.LogginAuthentcation;
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.domain.PendingTransactionHeader;
import com.fusionx.central.cash.management.domain.PendingTransaction;
import com.fusionx.central.cash.management.resource.InterTellerCashTransfersResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.InterTellerCashTransfersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/inter-teller-cash-transfer")
@CrossOrigin(origins = "*")
public class InterTellerCashTransfersController extends MessagePropertyBase {

    @Autowired
    private Environment environment;

    @Autowired
    private InterTellerCashTransfersService interTellerCashTransfersService;

    private String userNotFound = "common.user-not-found";

    private String commonSaved = "common.saved";

    private String recordNotFound = "common.record-not-found";

    /**
     * Add Inter Teller Cash Transfers
     *
     * @param tenantId the tenant id
     * @param interTellerCashTransfersResource the inter teller cash transfer add resource
     * @return the response entity
     */
    @PostMapping(value = "/{tenantId}")
    public ResponseEntity<Object> addInterTellerCashTransfers(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                              @Valid @RequestBody InterTellerCashTransfersResource interTellerCashTransfersResource) {

        if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty()) {
            throw new UserNotFound(environment.getProperty(userNotFound));
        }

        Long id = interTellerCashTransfersService.addInterTellerCashTransfers(tenantId, LogginAuthentcation.getUserName(), interTellerCashTransfersResource);
        SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(RECORD_CREATED), id.toString());
        return new ResponseEntity<>(successDetailsDto, HttpStatus.CREATED);
    }


    /**
     * Approve Teller Cash Transfer Request.
     *
     * @param tenantId the tenant id
     * @param id teller cash transfer request id
     * @return the response entity
     */
    @PutMapping(value = "{tenantId}/approve/pending-transaction-id/{id}")
    public ResponseEntity<Object> approveTellerCashTransferRequest(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                                   @PathVariable(value = "id", required = true) Long id,
                                                                   @Valid @RequestBody InterTellerCashTransfersResource interTellerCashTransfersResource) {
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
        Optional<PendingTransactionHeader> optionalPendingTransactionHeader = interTellerCashTransfersService.findById(id);
        if (optionalPendingTransactionHeader.isPresent()) {
            interTellerCashTransfersService.approve(id);
            successAndErrorDetailsResource = new SuccessAndErrorDetailsResource(environment.getProperty("common.approved"), id.toString());
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.OK);
        } else {
            successAndErrorDetailsResource.setMessages(environment.getProperty(RECORD_NOT_FOUND));
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


    /**
     * Get All Teller Cash Transfers.
     *
     * @param tenantId the tenant id
     * @return the teller cash transfers
     */
    @GetMapping(value = "/{tenantId}/all")
    public ResponseEntity<Object> getAllTellerTransfers(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                        @PageableDefault(size = 10) Pageable pageable) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        List<PendingTransactionHeader> transactions = interTellerCashTransfersService.findAll();
        int size = transactions.size();
        if(size > 0) {
            return new ResponseEntity<>((Collection<PendingTransactionHeader>) transactions, HttpStatus.OK);
        } else {
            responseMessage.setMessages(recordNotFound);
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }


    /**
     * Get Teller Cash Transfers By From Teller Code.
     *
     * @param tenantId        the tenant id
     * @param fromTellerCode  the fromTellerCode
     * @return the cash transfers by fromTellerCode
     */
    @GetMapping(value = "/{tenantId}/from-teller-code/{fromTellerCode}")
    public ResponseEntity<Object> getTellerTransfersByFromTellerCode(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                                     @PathVariable(value = "fromTellerCode", required = true) String fromTellerCode,
                                                                     @PageableDefault(size = 10) Pageable pageable) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        List<PendingTransactionHeader> transactions = interTellerCashTransfersService.findByFromTellerCode(fromTellerCode);
        int size = transactions.size();
        if(size>0){
            return new ResponseEntity<>(transactions,HttpStatus.OK);
        }
        else {
            responseMessage.setMessages(recordNotFound);
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }


    /**
     * Get Teller Cash Transfers By To Teller Code.
     *
     * @param tenantId        the tenant id
     * @param toTellerCode    the toTellerCode
     * @return the cash transfers by toTellerCode
     */
    @GetMapping(value = "/{tenantId}/to-teller-code/{toTellerCode}")
    public ResponseEntity<Object> getTellerTransfersByToTellerCode(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                                   @PathVariable(value = "toTellerCode", required = true) String toTellerCode,
                                                                   @PageableDefault(size = 10) Pageable pageable) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        List<PendingTransactionHeader> transactions = interTellerCashTransfersService.findByToTellerCode(toTellerCode);
        int size = transactions.size();
        if(size>0){
            return new ResponseEntity<>(transactions,HttpStatus.OK);
        }
        else {
            responseMessage.setMessages(recordNotFound);
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Get Teller Cash Transfers By To Transaction Date.
     *
     * @param tenantId         the tenant id
     * @param transactionDateStr  the transactionDate
     * @return the cash transfers by transactionDate
     */
    @GetMapping(value = "/{tenantId}/transaction-date/{transactionDate}")
    public ResponseEntity<Object> getTellerTransfersByTransactionDate(
            @PathVariable(value = "tenantId", required = true) String tenantId,
            @PathVariable(value = "transactionDate", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) String transactionDateStr,
            @PageableDefault(size = 10) Pageable pageable) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        SimpleDateFormat inputFormatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date transactionDate = inputFormatter.parse(transactionDateStr);
            List<PendingTransaction> transactions = interTellerCashTransfersService.findByTransactionDate(transactionDateStr);
            int size = transactions.size();
            if (size > 0) {
                return new ResponseEntity<>(transactions, HttpStatus.OK);
            } else {
                responseMessage.setMessages(recordNotFound);
                return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
            }
        } catch (ParseException e) {
            responseMessage.setMessages("Invalid date format - transactionDate should be YYYY-MM-DD");
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * Get Teller Cash Transfers By From Teller Id.
     *
     * @param tenantId      the tenant id
     * @param fromTellerId  the fromTellerId
     * @return the cash transfers by fromTellerId
     */
    @GetMapping(value = "/{tenantId}/from-teller-id/{fromTellerId}")
    public ResponseEntity<Object> getTellerTransfersByFromTellerId(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                                   @PathVariable(value = "fromTellerId", required = true) String fromTellerId,
                                                                   @PageableDefault(size = 10) Pageable pageable) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        List<PendingTransactionHeader> transactions = interTellerCashTransfersService.findByFromTellerId(fromTellerId);
        int size = transactions.size();
        if(size>0){
            return new ResponseEntity<>(transactions,HttpStatus.OK);
        }
        else {
            responseMessage.setMessages(recordNotFound);
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }


    /**
     * Get Teller Cash Transfers By To Teller Id.
     *
     * @param tenantId    the tenant id
     * @param toTellerId  the toTellerId
     * @return the cash transfers by toTellerId
     */
    @GetMapping(value = "/{tenantId}/to-teller-id/{toTellerId}")
    public ResponseEntity<Object> getTellerTransfersByToTellerId(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                                   @PathVariable(value = "toTellerId", required = true) String toTellerId,
                                                                   @PageableDefault(size = 10) Pageable pageable) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        List<PendingTransactionHeader> transactions = interTellerCashTransfersService.findByToTellerId(toTellerId);
        int size = transactions.size();
        if(size>0){
            return new ResponseEntity<>(transactions,HttpStatus.OK);
        }
        else {
            responseMessage.setMessages(recordNotFound);
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }
}
