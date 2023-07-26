package com.fusionx.central.cash.management.controller;

import com.fusionx.central.cash.management.Constants;
import com.fusionx.central.cash.management.base.MessagePropertyBase;
import com.fusionx.central.cash.management.core.LogginAuthentcation;
import com.fusionx.central.cash.management.exception.PageableLengthException;
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.domain.BranchTellerCurrencies;
import com.fusionx.central.cash.management.resource.BranchTellerCurrenciesResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.BranchTellerCurrenciesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/branch-teller-currencies")
@CrossOrigin(origins = "*")
public class BranchTellerCurrenciesController extends MessagePropertyBase {

    @Autowired
    private Environment environment;

    @Autowired
    private BranchTellerCurrenciesService branchTellerCurrenciesService;

    private String userNotFound = "common.user-not-found";

    private String commonSaved = "common.saved";

    private String commonUpdated = "common.updated";

    private String recordNotFound = "common.record-not-found";

    private String pageableLength = "common.pageable-length";


    @GetMapping(value = "/{tenantId}/all")
    public Page<BranchTellerCurrencies> getAllBranchTellerCurrencies(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                                     @PageableDefault(size = 10) Pageable pageable) {
        if (pageable.getPageSize() > Constants.MAX_PAGEABLE_LENGTH)
            throw new PageableLengthException(environment.getProperty(pageableLength));
        return branchTellerCurrenciesService.findAll(pageable);
    }


    @GetMapping(value = "/{tenantId}/{id}")
    public ResponseEntity<Object> getBranchTellerCurrenciesById(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                                @PathVariable(value = "id", required = true) Long id) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        Optional<BranchTellerCurrencies> isPresentBranchTellerCurrencies = branchTellerCurrenciesService.findById(id);
        if (isPresentBranchTellerCurrencies.isPresent()) {
            return new ResponseEntity<>(isPresentBranchTellerCurrencies, HttpStatus.OK);
        } else {
            responseMessage.setMessages(environment.getProperty(recordNotFound));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }


    @GetMapping(value = "/{tenantId}/status/{status}")
    public ResponseEntity<Object> getBranchTellerCurrenciesByStatus(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                                    @PathVariable(value = "status", required = true) String status,
                                                                    @PageableDefault(size = 10) Pageable pageable) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        List <BranchTellerCurrencies> branchTellerCurrencies = branchTellerCurrenciesService.findByStatus(status);
        if(!branchTellerCurrencies.isEmpty()){
            return new ResponseEntity<>((Collection<BranchTellerCurrencies>) branchTellerCurrencies, HttpStatus.OK);
        } else {
        responseMessage.setMessages(environment.getProperty(recordNotFound));
        return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }


    @GetMapping(value = "/{tenantId}/branch/{branchId}")
    public ResponseEntity<Object> getBranchTellerCurrenciesByBranchId(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                                      @PathVariable(value = "branchId", required = true) Long branchId,
                                                                      @PageableDefault(size = 10) Pageable pageable) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        List<BranchTellerCurrencies> isPresentBranchTellerCurrencies = branchTellerCurrenciesService.findByBranchId(branchId);
        int size = isPresentBranchTellerCurrencies.size();
        if(size>0){
            return new ResponseEntity<>(isPresentBranchTellerCurrencies,HttpStatus.OK);
        }
        else {
            responseMessage.setMessages(recordNotFound);
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }


    @GetMapping(value = "/{tenantId}/teller/{tellerId}")
    public ResponseEntity<Object> getBranchTellerCurrenciesByTellerId(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                                      @PathVariable(value = "tellerId", required = true) Long tellerId,
                                                                      @PageableDefault(size = 10) Pageable pageable) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        List <BranchTellerCurrencies> isPresentBranchTellerCurrencies = branchTellerCurrenciesService.findByTellerId(tellerId);
        int size = isPresentBranchTellerCurrencies.size();
        if(size>0){
            return new ResponseEntity<>(isPresentBranchTellerCurrencies,HttpStatus.OK);
        }
        else {
            responseMessage.setMessages(recordNotFound);
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }


    @GetMapping(value = "/{tenantId}/currency/{currencyId}")
    public ResponseEntity<Object> getBranchTellerCurrenciesByCurrencyId(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                                        @PathVariable(value = "currencyId", required = true) Long currencyId,
                                                                        @PageableDefault(size = 10) Pageable pageable) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        List <BranchTellerCurrencies> isPresentBranchTellerCurrencies = branchTellerCurrenciesService.findByCurrencyId(currencyId);
        int size = isPresentBranchTellerCurrencies.size();
        if(size>0){
            return new ResponseEntity<>(isPresentBranchTellerCurrencies,HttpStatus.OK);
        }
        else {
            responseMessage.setMessages(recordNotFound);
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }


    @PostMapping(value = "/{tenantId}")
    public ResponseEntity<Object> addBranchTellerCurrencies(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                            @Valid @RequestBody BranchTellerCurrenciesResource branchTellerCurrenciesResource) {
        if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty()) {
            throw new UserNotFound(environment.getProperty(userNotFound));
        }

        SuccessAndErrorDetailsResource successAndErrorDetails = new SuccessAndErrorDetailsResource();
        BranchTellerCurrencies branchTellerCurrencies = branchTellerCurrenciesService.addBranchTellerCurrencies(tenantId, branchTellerCurrenciesResource);
        successAndErrorDetails = new SuccessAndErrorDetailsResource(environment.getProperty(commonSaved),Long.toString(branchTellerCurrencies.getId()));
        return new ResponseEntity<>(successAndErrorDetails,HttpStatus.CREATED);

    }


    @PutMapping(value = "/{tenantId}/{id}")
    public ResponseEntity<Object> updateBranchTellerCurrencies(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                               @PathVariable(value = "id", required = true) Long id,
                                                               @Valid @RequestBody BranchTellerCurrenciesResource branchTellerCurrenciesResource){
        if(LogginAuthentcation.getUserName()==null || LogginAuthentcation.getUserName().isEmpty()){
            throw new UserNotFound(environment.getProperty(userNotFound));
        }
        SuccessAndErrorDetailsResource successAndErrorDetails = new SuccessAndErrorDetailsResource();
        Optional<BranchTellerCurrencies> optionalBranchTellerCurrencies = branchTellerCurrenciesService.findById(id);
        if (optionalBranchTellerCurrencies.isPresent()) {
            BranchTellerCurrencies branchTellerCurrencies = branchTellerCurrenciesService.updateBranchTellerCurrencies(tenantId, id, branchTellerCurrenciesResource);
            successAndErrorDetails = new SuccessAndErrorDetailsResource(environment.getProperty(commonUpdated), branchTellerCurrencies.getId().toString());
            return new ResponseEntity<>(successAndErrorDetails, HttpStatus.OK);

        } else {
            successAndErrorDetails = new SuccessAndErrorDetailsResource(environment.getProperty(recordNotFound));
            return new ResponseEntity<>(successAndErrorDetails, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }



}
