package com.fusionx.central.cash.management.controller;

import com.fusionx.central.cash.management.base.MessagePropertyBase;
import com.fusionx.central.cash.management.core.LogginAuthentcation;
import com.fusionx.central.cash.management.domain.BranchWiseInsuranceCompany;
import com.fusionx.central.cash.management.exception.ValidateRecordException;
import com.fusionx.central.cash.management.resource.BranchWiseInsuranceCompanyDetailsResource;
import com.fusionx.central.cash.management.resource.BranchWiseInsuranceCompanyResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.resource.UpdateBranchWiseInsuraceCompanyResource;
import com.fusionx.central.cash.management.service.BranchWiseInsuranceCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

// TODO: Auto-generated Javadoc
/**
 *  The type Bank deposit cheque controller.
 *
 ********************************************************************************************************
 *  ###   Date         Story Point   Task No    Author       Description
 *-------------------------------------------------------------------------------------------------------
 *    1   15-09-2022      		            	LasenH      Created
 *
 ********************************************************************************************************
 */

@RestController
@RequestMapping(value = "/branch_wise_insurance_company")
@CrossOrigin(origins = "*")
public class BranchWiseInsuranceCompanyController extends MessagePropertyBase {

    /** The environment. */
    @Autowired
    private Environment environment;

    /** The branch wise insurance company service. */
    @Autowired
    private BranchWiseInsuranceCompanyService branchWiseInsuranceCompanyService;

    /** The common updated. */
    private String commonUpdated = "common.updated";
    
    /** The record not found. */
    private String recordNotFound = "common.record-not-found";

    /**
     * Adds the company.
     *
     * @param tenantId - the tenant id
     * @param branchWiseInsuranceCompanyResource - the company add resource
     * @return the response entity
     */
    @PostMapping(value = "/{tenantId}")
    public ResponseEntity<Object> saveBranchWiseInsuranceCompany(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                                 @Valid @RequestBody BranchWiseInsuranceCompanyResource branchWiseInsuranceCompanyResource) {

        if (LogginAuthentcation.getUserName()==null || LogginAuthentcation.getUserName().isEmpty() )
            throw new ValidateRecordException(environment.getProperty(COMMON_NOT_NULL), "username");

        Long id = branchWiseInsuranceCompanyService.saveAndValidateBranchWiseInsuranceCompany(tenantId, branchWiseInsuranceCompanyResource, LogginAuthentcation.getUserName());
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
        successAndErrorDetailsResource.setMessages(environment.getProperty("common.saved"));
        successAndErrorDetailsResource.setValue(id.toString());
        return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.CREATED);
    }

    /**
     * Update company.
     *
     * @param tenantId - the tenant id
     * @param id - the id
     * @param updateBranchWiseInsuraceCompanyResource - the company update resource
     * @return the response entity
     */
    @PutMapping(value = "/{tenantId}/{id}")
    public ResponseEntity<Object> updateBranchWiseInsuranceCompany(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                                   @PathVariable(value = "id", required = true)Long id,
                                                                   @Valid @RequestBody UpdateBranchWiseInsuraceCompanyResource updateBranchWiseInsuraceCompanyResource){
        if (LogginAuthentcation.getUserName()==null || LogginAuthentcation.getUserName().isEmpty() )
            throw new ValidateRecordException(environment.getProperty(COMMON_NOT_NULL), "username");

        Optional<BranchWiseInsuranceCompany> isPresentBranch = branchWiseInsuranceCompanyService.getById(id);
        if(isPresentBranch.isPresent()) {
            Long branchWiseInsuranceCompany = branchWiseInsuranceCompanyService.updatBranchWiseInsuranceCompany(tenantId, updateBranchWiseInsuraceCompanyResource, id, LogginAuthentcation.getUserName());
            SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(commonUpdated));
            return new ResponseEntity<>(successDetailsDto,HttpStatus.OK);
        }
        else {
            SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(recordNotFound));
            return new ResponseEntity<>(successDetailsDto, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * Update contact details.
     *
     * @param tenantId - the tenant id
     * @param id - the id
     * @param contactDetails - the contact details resource
     * @return the response entity
     */
    @PutMapping(value = "/{tenantId}/contact-details/{id}")
    public ResponseEntity<Object> updateContactDetails(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                       @PathVariable(value = "id", required = true)Long id,
                                                       @Valid @RequestBody List<BranchWiseInsuranceCompanyDetailsResource> contactDetails){
        if (LogginAuthentcation.getUserName()==null || LogginAuthentcation.getUserName().isEmpty() )
            throw new ValidateRecordException(environment.getProperty(COMMON_NOT_NULL), "username");

        Optional<BranchWiseInsuranceCompany> isPresentBranch = branchWiseInsuranceCompanyService.getById(id);
        if(isPresentBranch.isPresent()) {
            Long branchWiseInsuranceCompany = branchWiseInsuranceCompanyService.updateContactDetails(tenantId, contactDetails, id, LogginAuthentcation.getUserName());
            SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(commonUpdated));
            return new ResponseEntity<>(successDetailsDto,HttpStatus.OK);
        }
        else {
            SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(recordNotFound));
            return new ResponseEntity<>(successDetailsDto, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * Gets the all companies.
     *
     * @param tenantId - the tenant id
     * @return the all companies
     */
    @GetMapping("/{tenantId}/all")
    public ResponseEntity<Object> getAll(@PathVariable(value = "tenantId", required = true) String tenantId){
        List<BranchWiseInsuranceCompany> list = branchWiseInsuranceCompanyService.getAll();
        if(!list.isEmpty())
            return new ResponseEntity<>(list, HttpStatus.OK);
        else {
            return new ResponseEntity<>(list, HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Gets the company by id.
     *
     * @param tenantId - the tenant id
     * @param id - the id
     * @return the company by id
     */
    @GetMapping(value = "/{tenantId}/{id}")
    public ResponseEntity<Object> getById(
            @PathVariable(value = "tenantId", required = true) String tenantId,
            @PathVariable(value = "id", required = true) Long id) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        Optional<BranchWiseInsuranceCompany> isPresentBranch = branchWiseInsuranceCompanyService.getById(id);
        if (isPresentBranch.isPresent()) {
            return new ResponseEntity<>(isPresentBranch, HttpStatus.OK);
        } else {
            responseMessage.setMessages(environment.getProperty(recordNotFound));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Gets the company by status.
     *
     * @param tenantId - the tenant id
     * @param status - the status
     * @return the company by status
     */
    @GetMapping(value = "/{tenantId}/status/{status}")
    public ResponseEntity<Object> getByStatus(
            @PathVariable(value = "tenantId", required = true) String tenantId,
            @PathVariable(value = "status", required = true) String status) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        List<BranchWiseInsuranceCompany> branchWiseInsuranceCompanies = branchWiseInsuranceCompanyService.getByStatus(status);
        if (!branchWiseInsuranceCompanies.isEmpty()) {
            return new ResponseEntity<>((Collection<BranchWiseInsuranceCompany>) branchWiseInsuranceCompanies, HttpStatus.OK);
        } else {
            responseMessage.setMessages(environment.getProperty(recordNotFound));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Gets the company by name.
     *
     * @param tenantId - the tenant id
     * @param name - the name
     * @return the company by name
     */
    @GetMapping(value = "/{tenantId}/name/{name}")
    public ResponseEntity<Object> getByName(
            @PathVariable(value = "tenantId", required = true) String tenantId,
            @PathVariable(value = "name", required = true) String name) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        Optional<BranchWiseInsuranceCompany> branchWiseInsuranceCompanies = branchWiseInsuranceCompanyService.getByName(name);
        if (branchWiseInsuranceCompanies.isPresent()) {
            return new ResponseEntity<>(branchWiseInsuranceCompanies, HttpStatus.OK);
        } else {
            responseMessage.setMessages(environment.getProperty(recordNotFound));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Gets the company by name.
     *
     * @param tenantId - the tenant id
     * @param id - the id
     * @return the company by name
     */
    @GetMapping(value = "/{tenantId}/contact-details/{id}")
    public ResponseEntity<Object> getContactDetailsById(
            @PathVariable(value = "tenantId", required = true) String tenantId,
            @PathVariable(value = "id", required = true) Long id) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        Optional<BranchWiseInsuranceCompany> branchWiseInsuranceCompanies = branchWiseInsuranceCompanyService.getById(id);
        if (branchWiseInsuranceCompanies.isPresent()) {
            return new ResponseEntity<>(branchWiseInsuranceCompanies, HttpStatus.OK);
        } else {
            responseMessage.setMessages(environment.getProperty(recordNotFound));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }
    
    /**
     * Gets the by branch id.
     *
     * @param tenantId the tenant id
     * @param branchId the branch id
     * @return the by branch id
     */
    @GetMapping(value = "/{tenantId}/branch-id/{id}")
    public ResponseEntity<Object> getByBranchId(
            @PathVariable(value = "tenantId", required = true) String tenantId,
            @PathVariable(value = "branchId", required = true) Long branchId) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        List<BranchWiseInsuranceCompany> branchWiseInsuranceCompanies = branchWiseInsuranceCompanyService.findByBranchId(branchId);
        if (!branchWiseInsuranceCompanies.isEmpty()) {
            return new ResponseEntity<>((Collection<BranchWiseInsuranceCompany>) branchWiseInsuranceCompanies, HttpStatus.OK);
        } else {
            responseMessage.setMessages(environment.getProperty(recordNotFound));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }

}