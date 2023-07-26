package com.fusionx.central.cash.management.controller;

import com.fusionx.central.cash.management.domain.CashTest;
import com.fusionx.central.cash.management.resource.AddCashTestResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.resource.UpdateCashTestResource;
import com.fusionx.central.cash.management.service.CashTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/cash-test")
public class CashTestController {



    @Autowired
    private CashTestService cashTestService;




    @PostMapping(value = "/{tenantId}")
    public ResponseEntity<Object> saveCashTest(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                  @Valid @RequestBody AddCashTestResource addCashTestResource) {

        CashTest cashTest = cashTestService.saveCashTest(tenantId,addCashTestResource);
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
        successAndErrorDetailsResource.setMessages(environment.getProperty("common.saved"));
        successAndErrorDetailsResource.setValue(cashTest.getCode());


        return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.CREATED);
    }



    @PutMapping (value = "/{tenantId}/{id}")
    public ResponseEntity<Object> updateCashTest(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                 @PathVariable(value = "id", required = true) String id,
                                                 @Valid @RequestBody UpdateCashTestResource updateCashTestResource) {

        CashTest cashTest = cashTestService.updateCashTest(tenantId,updateCashTestResource,Long.parseLong(id));
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
        successAndErrorDetailsResource.setMessages(environment.getProperty("common.saved"));
        successAndErrorDetailsResource.setValue(cashTest.getCode());


        return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.CREATED);
    }

    @GetMapping (value = "/{tenantId}/name/{name}")
    public ResponseEntity<Object> getfindByName(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                @PathVariable(value = "name", required = true) String name) {

        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        List<CashTest> findByName = cashTestService.findByName(name);

        if (findByName.isEmpty()) {
            return new ResponseEntity<>(findByName, HttpStatus.OK);
        } else {
            responseMessage.setMessages(environment.getProperty("recode-not-found"));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping (value = "/{tenantId}/status/{status}")
    public ResponseEntity<Object> getfindByStatus(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                @PathVariable(value = "status", required = true) String status) {

        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        List<CashTest> findByStatus = cashTestService.findByStatus(status);

        if (findByStatus.isEmpty()) {
            return new ResponseEntity<>(findByStatus, HttpStatus.OK);
        } else {
            responseMessage.setMessages(environment.getProperty("recode-not-found"));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping (value = "/{tenantId}/description/{description}")
    public ResponseEntity<Object> getfindByDescription(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                @PathVariable(value = "description", required = true) String description) {

        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        List<CashTest> findByDescription = cashTestService.findByDescription(description);

        if (findByDescription.isEmpty()) {
            return new ResponseEntity<>(findByDescription, HttpStatus.OK);
        } else {
            responseMessage.setMessages(environment.getProperty("recode-not-found"));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }


    }


