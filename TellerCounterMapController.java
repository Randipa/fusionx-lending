package com.fusionx.central.cash.management.controller;

import com.fusionx.central.cash.management.Constants;
import com.fusionx.central.cash.management.base.MessagePropertyBase;
import com.fusionx.central.cash.management.core.LogginAuthentcation;
import com.fusionx.central.cash.management.domain.Counter;
import com.fusionx.central.cash.management.exception.PageableLengthException;
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.domain.TellerCounterMap;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.resource.TellerCounterMapResource;
import com.fusionx.central.cash.management.service.TellerCounterMapService;

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
@RequestMapping(value = "/teller-counter-map")
@CrossOrigin(origins = "*")
public class TellerCounterMapController extends MessagePropertyBase {

    @Autowired
    private Environment environment;

    @Autowired
    private TellerCounterMapService tellerCounterMapService;

    private String userNotFound = "common.user-not-found";

    private String commonSaved = "common.saved";

    private String commonUpdated = "common.updated";

    private String recordNotFound = "common.record-not-found";

    private String pageableLength = "common.pageable-length";


    @GetMapping(value = "/{tenantId}/all")
    public Page<TellerCounterMap> getAllTellerCounterMap(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                         @PageableDefault(size = 10) Pageable pageable) {
        if (pageable.getPageSize() > Constants.MAX_PAGEABLE_LENGTH)
            throw new PageableLengthException(environment.getProperty(pageableLength));
        return tellerCounterMapService.findAll(pageable);
    }


    @GetMapping(value = "/{tenantId}/{id}")
    public ResponseEntity<Object> getTellerCounterMapById(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                          @PathVariable(value = "id", required = true) Long id) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        Optional<TellerCounterMap> isPresentTellerCounter = tellerCounterMapService.findById(id);
        if (isPresentTellerCounter.isPresent()) {
            return new ResponseEntity<>(isPresentTellerCounter, HttpStatus.OK);
        } else {
            responseMessage.setMessages(environment.getProperty(recordNotFound));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }


    @GetMapping(value = "/{tenantId}/status/{status}")
    public ResponseEntity<Object> getTellerCounterMapByStatus(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                              @PathVariable(value = "status", required = true) String status,
                                                              @PageableDefault(size = 10) Pageable pageable) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        List<TellerCounterMap> tellerCounter = tellerCounterMapService.findByStatus(status);
        if(!tellerCounter.isEmpty()){
            return new ResponseEntity<>((Collection<TellerCounterMap>) tellerCounter, HttpStatus.OK);
        } else {
            responseMessage.setMessages(environment.getProperty(recordNotFound));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }


    @GetMapping(value = "/{tenantId}/teller-counter-id/{tellerCounterId}")
    public ResponseEntity<Object> getTellerCounterMapByTellerCounterId(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                                       @PathVariable(value = "tellerCounterId", required = true) Long tellerCounterId,
                                                                       @PageableDefault(size = 10) Pageable pageable) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        List<TellerCounterMap> isPresentTellerCounter = tellerCounterMapService.findByTellerCounterId(tellerCounterId);
        int size = isPresentTellerCounter.size();
        if(size>0){
            return new ResponseEntity<>(isPresentTellerCounter,HttpStatus.OK);
        }
        else {
            responseMessage.setMessages(recordNotFound);
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(value = "/{tenantId}/teller-id/{tellerId}")
    public ResponseEntity<Object> getTellerCounterMapByTellerId(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                                @PathVariable(value = "tellerId", required = true) Long tellerId,
                                                                @PageableDefault(size = 10) Pageable pageable) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        List<TellerCounterMap> isPresentTellerCounter = tellerCounterMapService.findByTellerId(tellerId);
        int size = isPresentTellerCounter.size();
        if(size>0){
            return new ResponseEntity<>(isPresentTellerCounter,HttpStatus.OK);
        }
        else {
            responseMessage.setMessages(recordNotFound);
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(value = "/{tenantId}/branch/{branchId}")
    public ResponseEntity<Object> getCounterDetailByBranchId(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                            @PathVariable(value = "branchId", required = true) Long branchId) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        List <Counter> isPresentCounter = tellerCounterMapService.findCounterDetailsByBranchId(branchId);
        if (!isPresentCounter.isEmpty()) {
            return new ResponseEntity<>(isPresentCounter, HttpStatus.OK);
        }
        else {
            responseMessage.setMessages(recordNotFound);
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }


    @PostMapping(value = "/{tenantId}")
    public ResponseEntity<Object> addTellerCounterMap(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                      @Valid @RequestBody TellerCounterMapResource tellerCounterMapResource) {
        if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty()) {
            throw new UserNotFound(environment.getProperty(userNotFound));
        }
        SuccessAndErrorDetailsResource successAndErrorDetails = new SuccessAndErrorDetailsResource();
        TellerCounterMap tellerCounter = tellerCounterMapService.addTellerCounterMap(tenantId, tellerCounterMapResource);
        successAndErrorDetails = new SuccessAndErrorDetailsResource(environment.getProperty(commonSaved), Long.toString(tellerCounter.getId()));
        return new ResponseEntity<>(successAndErrorDetails, HttpStatus.CREATED);
    }



    @PutMapping(value = "/{tenantId}/{id}")
    public ResponseEntity<Object> updateTellerCounterMap(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                         @PathVariable(value = "id", required = true) Long id,
                                                         @Valid @RequestBody TellerCounterMapResource tellerCounterMapResource){
        if(LogginAuthentcation.getUserName()==null || LogginAuthentcation.getUserName().isEmpty()){
            throw new UserNotFound(environment.getProperty(userNotFound));
        }
        SuccessAndErrorDetailsResource successAndErrorDetails = new SuccessAndErrorDetailsResource();
        Optional<TellerCounterMap> optionalTellerCounter = tellerCounterMapService.findById(id);
        if (optionalTellerCounter.isPresent()) {
            TellerCounterMap tellerCounterMap = tellerCounterMapService.updateTellerCounterMap(tenantId, id, tellerCounterMapResource);
            successAndErrorDetails = new SuccessAndErrorDetailsResource(environment.getProperty(commonUpdated), tellerCounterMap.getId().toString());
            return new ResponseEntity<>(successAndErrorDetails, HttpStatus.OK);

        } else {
            successAndErrorDetails = new SuccessAndErrorDetailsResource(environment.getProperty(recordNotFound));
            return new ResponseEntity<>(successAndErrorDetails, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


}
