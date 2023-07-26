package com.fusionx.central.cash.management.controller;

import com.fusionx.central.cash.management.base.MessagePropertyBase;
import com.fusionx.central.cash.management.core.LogginAuthentcation;
import com.fusionx.central.cash.management.domain.TellerLimit;
import com.fusionx.central.cash.management.exception.ValidateRecordException;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.resource.TellerLimitAddResource;
import com.fusionx.central.cash.management.resource.TellerLimitUpdateResource;
import com.fusionx.central.cash.management.service.TellerLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/teller-limit")
@CrossOrigin(origins = "*")
public class TellerLimitController extends MessagePropertyBase {

    private final TellerLimitService tellerLimitService;

    @Autowired
    public TellerLimitController(TellerLimitService tellerLimitService) {
        this.tellerLimitService = tellerLimitService;
    }

    @GetMapping(value = "/{tenantId}/all")
    public ResponseEntity<Object> getAllTellerLimit(@PathVariable(value = "tenantId") String tenantId) {
        List<TellerLimit> allTellerLimit = tellerLimitService.findAll(tenantId);
        if (!allTellerLimit.isEmpty()){
            return new ResponseEntity<>(allTellerLimit, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(allTellerLimit, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(value = "/{tenantId}/{id}")
    public ResponseEntity<Object> getTellerLimitById(
                                @PathVariable(value = "tenantId") String tenantId,
                                @PathVariable(value = "id") Long id) {
        Optional<TellerLimit> optTellerLimit = tellerLimitService.findById(tenantId, id);
        if (optTellerLimit.isPresent()){
            return new ResponseEntity<>(optTellerLimit.get(), HttpStatus.OK);
        } else {
            SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
            responseMessage.setMessages(RECORD_NOT_FOUND);
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(value = "/{tenantId}/status/{status}")
    public ResponseEntity<Object> getTellerLimitsByStatus(
                                        @PathVariable(value = "tenantId") String tenantId,
                                        @PathVariable(value = "status") String status) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        if (status.equals("ACTIVE") || status.equals("INACTIVE")){
            List<TellerLimit> allTellerLimit = tellerLimitService.findByStatus(tenantId,status);
            if (!allTellerLimit.isEmpty()){
                return new ResponseEntity<>(allTellerLimit, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(allTellerLimit, HttpStatus.NO_CONTENT);
            }
        } else {
            responseMessage.setMessages(environment.getProperty(COMMON_INVALID_VALUE));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }

    }

    @GetMapping(value = "/{tenantId}/designation-name/{designationName}")
    public ResponseEntity<Object> getTellerLimitsByDesignationName(
                                @PathVariable(value = "tenantId") String tenantId,
                                @PathVariable(value = "designationName") String designationName) {
        List<TellerLimit> allTellerLimit = tellerLimitService.findByDesignationName(tenantId,designationName);
        if (!allTellerLimit.isEmpty()){
            return new ResponseEntity<>(allTellerLimit, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(allTellerLimit, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(value = "/{tenantId}/module/{module}")
    public ResponseEntity<Object> getTellerLimitsByModule(
            @PathVariable(value = "tenantId") String tenantId,
            @PathVariable(value = "module") String module) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        if (module.equals("CASA") || module.equals("LENDING") || module.equals("TD")){
            List<TellerLimit> allTellerLimit = tellerLimitService.findByModule(tenantId,module);
            if (!allTellerLimit.isEmpty()){
                return new ResponseEntity<>(allTellerLimit, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(allTellerLimit, HttpStatus.NO_CONTENT);
            }
        } else {
            responseMessage.setMessages(environment.getProperty(COMMON_INVALID_VALUE));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }

    }

    @PostMapping(value = "/{tenantId}")
    public ResponseEntity<Object> createTellerLimit(@PathVariable(value = "tenantId") String tenantId,
                                                    @Valid @RequestBody TellerLimitAddResource request) {

        if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty()) {
            throw new ValidateRecordException(getEnvironment().getProperty(COMMON_NOT_NULL), "username");
        }
        Long createdId = tellerLimitService.addTellerLimit(tenantId, request);
        SuccessAndErrorDetailsResource successDetailsDto=new SuccessAndErrorDetailsResource(environment.getProperty(RECORD_CREATED), createdId.toString());
        return new ResponseEntity<>(successDetailsDto, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{tenantId}/{id}")
    public ResponseEntity<Object> updateTellerLimit(@PathVariable(value = "tenantId") String tenantId,
                                                    @PathVariable(value = "id") Long id,
                                                    @Valid @RequestBody TellerLimitUpdateResource request) {

        if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty()) {
            throw new ValidateRecordException(getEnvironment().getProperty(COMMON_NOT_NULL), "username");
        }
        Long updatedId = tellerLimitService.updateTellerLimit(tenantId,id,request);
        SuccessAndErrorDetailsResource successDetailsDto=new SuccessAndErrorDetailsResource(environment.getProperty(RECORD_UPDATED), updatedId.toString());
        return new ResponseEntity<>(successDetailsDto, HttpStatus.OK);
    }

}
