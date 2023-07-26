package com.fusionx.central.cash.management.controller;

import com.fusionx.central.cash.management.base.MessagePropertyBase;
import com.fusionx.central.cash.management.core.LogginAuthentcation;
import com.fusionx.central.cash.management.domain.FundRequestCenterCash;
import com.fusionx.central.cash.management.domain.FundRequestCenterCashWorkFlow;
import com.fusionx.central.cash.management.enums.*;
import com.fusionx.central.cash.management.exception.ValidateRecordException;
import com.fusionx.central.cash.management.resource.FundRequestApproveRejectResource;
import com.fusionx.central.cash.management.resource.FundRequestFromCenterCashRequestResource;
import com.fusionx.central.cash.management.resource.FundRequestFromCenterCashUpdateResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.FundRequestService;
import com.fusionx.central.cash.management.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * The type Fund request controller.
 */
@RestController
@RequestMapping(value = "/fund-request")
@CrossOrigin(origins = "*")
public class FundRequestController extends MessagePropertyBase {

    private Environment environment;

    private FundRequestService fundRequestService;

    private WorkflowService workflowService;

    /**
     * Instantiates a new Fund request controller.
     *
     * @param environment        the environment
     * @param fundRequestService the fund request service
     */
    @Autowired
    public FundRequestController(Environment environment, FundRequestService fundRequestService, WorkflowService workflowService) {
        this.environment = environment;
        this.fundRequestService = fundRequestService;
        this.workflowService = workflowService;
    }

    /**
     * Save fund request response entity.
     *
     * @param tenantId                         the tenant id
     * @param fundRequestFromCenterCashRequest the fund request from center cash request
     * @return the response entity
     */
    @PostMapping(value = "/{tenantId}")
    public ResponseEntity<Object> saveFundRequest(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                  @Valid @RequestBody List<FundRequestFromCenterCashRequestResource> fundRequestFromCenterCashRequest) {

        if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
            throw new ValidateRecordException(environment.getProperty(COMMON_NOT_NULL), USERNAME);

        fundRequestService.saveFundRequest(tenantId, fundRequestFromCenterCashRequest, LogginAuthentcation.getUserName());
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
        successAndErrorDetailsResource.setMessages(environment.getProperty(RECORD_CREATED));
        return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.CREATED);
    }


    /**
     * Save fund request response entity.
     *
     * @param tenantId                                the tenant id
     * @param id                                      the id
     * @param fundRequestFromCenterCashUpdateResource the fund request from center cash update resource
     * @return the response entity
     */
    @PutMapping(value = "/{tenantId}/{id}")
    public ResponseEntity<Object> saveFundRequest(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                  @PathVariable(value = "id", required = true) Long id,
                                                  @Valid @RequestBody FundRequestFromCenterCashUpdateResource fundRequestFromCenterCashUpdateResource) {

        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
        if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
            throw new ValidateRecordException(environment.getProperty(COMMON_NOT_NULL), USERNAME);

        Optional<FundRequestCenterCash> fundRequestCenterCash = fundRequestService.getById(id);
        if (!fundRequestCenterCash.isPresent()) {
            successAndErrorDetailsResource.setMessages(environment.getProperty(RECORD_NOT_FOUND));
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        fundRequestService.updateRequest(tenantId, fundRequestCenterCash.get(), fundRequestFromCenterCashUpdateResource, LogginAuthentcation.getUserName());
        successAndErrorDetailsResource.setMessages(environment.getProperty(RECORD_CREATED));
        return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.OK);
    }

    /**
     * Gets all fund request.
     *
     * @param tenantId the tenant id
     * @return the all fund request
     */
    @GetMapping(value = "/{tenantId}/all")
    public ResponseEntity<Object> getAllFundRequest(@PathVariable(value = "tenantId", required = true) String tenantId) {
        List<FundRequestCenterCash> fundRequestCenterCash = fundRequestService.getAll();
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        if (fundRequestCenterCash != null && !fundRequestCenterCash.isEmpty()) {
            return new ResponseEntity<>((Collection<FundRequestCenterCash>) fundRequestCenterCash, HttpStatus.OK);
        } else {
            responseMessage.setMessages(environment.getProperty(RECORD_NOT_FOUND));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Gets all fund request by id.
     *
     * @param tenantId the tenant id
     * @param id       the id
     * @return the all fund request by id
     */
    @GetMapping(value = "/{tenantId}/{id}")
    public ResponseEntity<Object> getAllFundRequestById(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                        @PathVariable(value = "id", required = true) Long id) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        Optional<FundRequestCenterCash> fundRequestCenterCash = fundRequestService.getById(id);
        if (fundRequestCenterCash.isPresent()) {
            return new ResponseEntity<>(fundRequestCenterCash, HttpStatus.OK);
        } else {
            responseMessage.setMessages(environment.getProperty(RECORD_NOT_FOUND));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Gets by status.
     *
     * @param tenantId the tenant id
     * @param status   the status
     * @return the by status
     */
    @GetMapping(value = "/{tenantId}/status/{status}")
    public ResponseEntity<Object> getByStatus(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                    @PathVariable(value = "status", required = true) String status) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        if(!status.equals(FundRequestStatus.COMPLETED.toString()) || !status.equals(FundRequestStatus.IN_PROGRESS.toString())) {
            responseMessage.setMessages(environment.getProperty(RECORD_NOT_FOUND));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
        List<FundRequestCenterCash> fundRequestCenterCash = fundRequestService.getByStatus(FundRequestStatus.valueOf(status));
        if (fundRequestCenterCash != null && !fundRequestCenterCash.isEmpty()) {
            return new ResponseEntity<>((Collection<FundRequestCenterCash>) fundRequestCenterCash, HttpStatus.OK);
        } else {
            responseMessage.setMessages(environment.getProperty(RECORD_NOT_FOUND));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Gets by process status.
     *
     * @param tenantId the tenant id
     * @param status   the status
     * @return the by process status
     */
    @GetMapping(value = "/{tenantId}/process-status/{status}")
    public ResponseEntity<Object> getByProcessStatus(@PathVariable(value = "tenantId", required = true) String tenantId,
                                              @PathVariable(value = "status", required = true) String status) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        if(!status.equals(ProcessStatus.APPROVED.toString()) || !status.equals(ProcessStatus.PENDING.toString()) || !status.equals(ProcessStatus.REJECTED.toString())) {
            responseMessage.setMessages(environment.getProperty(RECORD_NOT_FOUND));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
        List<FundRequestCenterCash> fundRequestCenterCash = fundRequestService.getByProcessStatus(ProcessStatus.valueOf(status));
        if (fundRequestCenterCash != null && !fundRequestCenterCash.isEmpty()) {
            return new ResponseEntity<>((Collection<FundRequestCenterCash>) fundRequestCenterCash, HttpStatus.OK);
        } else {
            responseMessage.setMessages(environment.getProperty(RECORD_NOT_FOUND));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Approve fund request response entity.
     *
     * @param tenantId                         the tenant id
     * @param id                               the id
     * @param fundRequestApproveRejectResource the fund request approve reject resource
     * @return the response entity
     */
    @PutMapping(value = "/{tenantId}/approve/{id}")
    public ResponseEntity<Object> approveFundRequest(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                  @PathVariable(value = "id", required = true) Long id,
                                                  @Valid @RequestBody FundRequestApproveRejectResource fundRequestApproveRejectResource) {

        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
        if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
            throw new ValidateRecordException(environment.getProperty(COMMON_NOT_NULL), USERNAME);

        Optional<FundRequestCenterCash> fundRequestCenterCash = fundRequestService.getById(id);
        if (!fundRequestCenterCash.isPresent()) {
            successAndErrorDetailsResource.setMessages(environment.getProperty(RECORD_NOT_FOUND));
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Optional<FundRequestCenterCashWorkFlow> cashWorkFlow = workflowService.getByCheckWFExist(WorkflowType.CREATE_FUND_REQUEST, WorkflowStatus.ACTIVE, id);
        if (!cashWorkFlow.isPresent()) {
            successAndErrorDetailsResource.setMessages(environment.getProperty(RECORD_NOT_FOUND));
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        fundRequestService.approveFundRequest(tenantId, fundRequestCenterCash.get(), fundRequestApproveRejectResource, LogginAuthentcation.getUserName(), cashWorkFlow.get());
        successAndErrorDetailsResource.setMessages(environment.getProperty(RECORD_CREATED));
        return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.OK);
    }

    /**
     * Reject fund request response entity.
     *
     * @param tenantId                         the tenant id
     * @param id                               the id
     * @param fundRequestApproveRejectResource the fund request approve reject resource
     * @return the response entity
     */
    @PutMapping(value = "/{tenantId}/reject/{id}")
    public ResponseEntity<Object> rejectFundRequest(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                     @PathVariable(value = "id", required = true) Long id,
                                                     @Valid @RequestBody FundRequestApproveRejectResource fundRequestApproveRejectResource) {

        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
        if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
            throw new ValidateRecordException(environment.getProperty(COMMON_NOT_NULL), USERNAME);

        Optional<FundRequestCenterCash> fundRequestCenterCash = fundRequestService.getById(id);
        if (!fundRequestCenterCash.isPresent()) {
            successAndErrorDetailsResource.setMessages(environment.getProperty(RECORD_NOT_FOUND));
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        Optional<FundRequestCenterCashWorkFlow> cashWorkFlow = workflowService.getByCheckWFExist(WorkflowType.CREATE_FUND_REQUEST, WorkflowStatus.ACTIVE, id);
        if (!cashWorkFlow.isPresent()) {
            successAndErrorDetailsResource.setMessages(environment.getProperty(RECORD_NOT_FOUND));
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        fundRequestService.rejectFundRequest(tenantId, fundRequestCenterCash.get(), fundRequestApproveRejectResource, LogginAuthentcation.getUserName(), cashWorkFlow.get());
        successAndErrorDetailsResource.setMessages(environment.getProperty(RECORD_CREATED));
        return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.OK);
    }

}
