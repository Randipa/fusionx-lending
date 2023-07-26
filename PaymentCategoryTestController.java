package com.fusionx.central.cash.management.controller;


import com.fusionx.central.cash.management.domain.PaymentCategoryTest;
import com.fusionx.central.cash.management.resource.*;
import com.fusionx.central.cash.management.service.PaymentCategoryTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static com.fusionx.central.cash.management.base.MessagePropertyBase.RECORD_NOT_FOUND;



@RestController
@RequestMapping(value = "/payment-category-test")
public class PaymentCategoryTestController {

    @Autowired
    private Environment environment;

    @Autowired
    private PaymentCategoryTestService paymentCategoryTestService;

    @GetMapping("/{tenantId}/all")
    public ResponseEntity<Object> getAllPaymentCategoryTest(@PathVariable(value = "tenantId", required = true) String tenantId){
        List<PaymentCategoryTest> list = paymentCategoryTestService.findAll();
        if(!list.isEmpty())
            return new ResponseEntity<>(list, HttpStatus.OK);
        else {
            return new ResponseEntity<>(list, HttpStatus.NO_CONTENT);
        }
    }



    @GetMapping(value = "/{tenantId}/{id}")
    public ResponseEntity<Object> getPaymentCategoryTestById(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                         @PathVariable(value = "id", required = true) Long id){
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();

        Optional<PaymentCategoryTest> isPresentPaymentCategoryTest = paymentCategoryTestService.findById(id);
        if(isPresentPaymentCategoryTest.isPresent()) {
            return new ResponseEntity<>(isPresentPaymentCategoryTest.get(), HttpStatus.OK);
        }
        else {
            responseMessage.setMessages(environment.getRequiredProperty(RECORD_NOT_FOUND));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }



    @PostMapping(value = "/{tenantId}")
    public ResponseEntity<Object> getsave(@PathVariable(value = "tenantId", required = true) String tenantId,
                                       @Valid @RequestBody AddPaymentCategoryTestResourse addPaymentCategoryTestResourse) {


        PaymentCategoryTest paymentCategoryTest = paymentCategoryTestService.save(tenantId, addPaymentCategoryTestResourse);
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
        successAndErrorDetailsResource.setMessages(environment.getProperty("common.saved"));
        successAndErrorDetailsResource.setValue(paymentCategoryTest.getCode());


        return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.CREATED);
    }

    @PutMapping (value = "/{tenantId}/{id}")
    public ResponseEntity<Object> getupdate(@PathVariable(value = "tenantId", required = true) String tenantId,
                                         @PathVariable(value = "id", required = true) String id,
                                         @Valid @RequestBody UpdatePaymentCategoryTestResourse updatePaymentCategoryTestResourse) {

        PaymentCategoryTest paymentCategoryTest = paymentCategoryTestService.update(tenantId, updatePaymentCategoryTestResourse,Long.parseLong(id));
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
        successAndErrorDetailsResource.setMessages(environment.getProperty("common.saved"));
        successAndErrorDetailsResource.setValue(paymentCategoryTest.getCode());


        return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.CREATED);
    }

    @GetMapping (value = "/{tenantId}/name/{name}")
    public ResponseEntity<Object> getfindByName(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                @PathVariable(value = "name", required = true) String name) {

        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        List<PaymentCategoryTest> findByName = paymentCategoryTestService.findByName(name);

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
        List<PaymentCategoryTest> findByStatus = paymentCategoryTestService.findByStatus(status);

        if (findByStatus.isEmpty()) {
            return new ResponseEntity<>(findByStatus, HttpStatus.OK);
        } else {
            responseMessage.setMessages(environment.getProperty("recode-not-found"));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }









}
