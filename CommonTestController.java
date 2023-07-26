package com.fusionx.central.cash.management.controller;

import com.fusionx.central.cash.management.base.MessagePropertyBase;
import com.fusionx.central.cash.management.core.LogginAuthentcation;
import com.fusionx.central.cash.management.domain.CashTest;
import com.fusionx.central.cash.management.domain.CommonList;
import com.fusionx.central.cash.management.domain.CommonTest;
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.resource.*;
import com.fusionx.central.cash.management.service.CommonTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/common-list")
public class CommonTestController extends MessagePropertyBase {

    @Autowired
    CommonTestService commonTestService;

    @GetMapping(value = "/{tenantId}/all")
    public ResponseEntity<Object> getAllCommonTest(@PathVariable(value = "tenantId", required = true) String tenantId) {

        List<CommonTest> test = commonTestService.findAll();
        if(!test.isEmpty())
            return new ResponseEntity<>(test, HttpStatus.OK);
        else {
            return new ResponseEntity<>(test, HttpStatus.NO_CONTENT);
        }
    }


    @GetMapping(value = "/{tenantId}/{id}")
    public ResponseEntity<Object> getCommonTestById(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                    @PathVariable(value = "id", required = true) Long id) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();

        Optional<CommonTest> commonTest = commonTestService.findById(id);
        if (commonTest.isPresent()) {
            return new ResponseEntity<>(commonTest.get(), HttpStatus.OK);
        }
        else {
            responseMessage.setMessages(environment.getProperty("record-not-found"));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(value = "/{tenantId}/code/{code}")
    public ResponseEntity<Object> getCommonListByCode(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                      @PathVariable(value = "code", required = true) String code) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();

        Optional<CommonTest> commonCode = commonTestService.findByCode(code);
        if (!commonCode.isEmpty()) {
            return new ResponseEntity<>(commonCode, HttpStatus.OK);
        }
        else {
            responseMessage.setMessages(environment.getProperty("record-not-found"));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }
    @GetMapping(value = "/{tenantId}/refcode/{referenceCode}")
    public ResponseEntity<Object> getCommonListByRefCode(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                         @PathVariable(value = "referenceCode", required = true) String referenceCode) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();

        Optional<CommonTest> commonRef = commonTestService.findByReferenceCode(referenceCode);

        if (!commonRef.isEmpty()) {
            return new ResponseEntity<>(commonRef, HttpStatus.OK);
        }
        else {
            responseMessage.setMessages(environment.getProperty("record-not-found"));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }

    }

    @GetMapping(value = "/{tenantId}/status/{status}")
    public ResponseEntity<Object> getCommonListByStatus(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                        @PathVariable(value = "status", required = true) String status) {

        List<CommonTest> commonStatus = commonTestService.findByStatus(status);
        if (!commonStatus.isEmpty())
            return new ResponseEntity<>(commonStatus, HttpStatus.OK);
        else {
            return new ResponseEntity<>(commonStatus, HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping(value = "/{tenantId}")
    public ResponseEntity<Object> save(@PathVariable(value = "tenantId", required = true) String tenantId,
                                       @Valid @RequestBody AddCommonTestResource addCommonTestResource) {
        if(LogginAuthentcation.getUserName()==null || LogginAuthentcation.getUserName().isEmpty()) {
            throw new UserNotFound(environment.getProperty("common.user-not-found"));
        }

        CommonTest commonTest=commonTestService.save(tenantId, addCommonTestResource);
        SuccessAndErrorDetailsResource successDetailsDto=new SuccessAndErrorDetailsResource(environment.getProperty("rec.saved"),commonTest.getId().toString());
        return new ResponseEntity<>(successDetailsDto,HttpStatus.CREATED);
    }



}

