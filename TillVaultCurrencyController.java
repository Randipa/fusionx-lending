package com.fusionx.central.cash.management.controller;

import com.fusionx.central.cash.management.base.MessagePropertyBase;
import com.fusionx.central.cash.management.core.LogginAuthentcation;
import com.fusionx.central.cash.management.domain.TillVaultCurrency;
import com.fusionx.central.cash.management.enums.TillVaultCurrencyStatus;
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.resource.TillVaultCurrencyResource;
import com.fusionx.central.cash.management.service.TillVaultCurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/tii-vault-currency")
@CrossOrigin(origins = "*")
public class TillVaultCurrencyController  extends MessagePropertyBase {

    @Autowired
    TillVaultCurrencyService tillVaultCurrencyService;

    @Autowired
    Environment environment;

    @PostMapping(value = "/{tenantId}")
    public ResponseEntity<Object> addTillVaultCurrency(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                       @Valid @RequestBody TillVaultCurrencyResource tillVaultCurrencyResource) {

        if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty()) {
            throw new UserNotFound(environment.getProperty("common.user-not-found"));
        }
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
        TillVaultCurrency tillVaultCurrency = tillVaultCurrencyService.addTillVaultCurrency(tenantId, tillVaultCurrencyResource);
        successAndErrorDetailsResource = new SuccessAndErrorDetailsResource(environment.getProperty("till-vault-currency.saved"),Long.toString(tillVaultCurrency.getId()));
        return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.ACCEPTED);
    }

    @GetMapping(value ="/{tenantId}/all")
    public ResponseEntity<Object> getAllTillVaultCurrency(@PathVariable (value = "tenantId", required = true) String tenantId){
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
        List<TillVaultCurrency> tillVaultCurrencyList = tillVaultCurrencyService.findAll();

        int size = tillVaultCurrencyList.size();
        if(size>0){
            return new ResponseEntity<>(tillVaultCurrencyList,HttpStatus.OK);
        }
        else {
            successAndErrorDetailsResource.setMessages(RECORD_NOT_FOUND);
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(value ="/{tenantId}/{id}")
    public ResponseEntity<Object> getById(@PathVariable (value = "tenantId", required = true) String tenantId,
                                          @PathVariable (value = "id", required = true) Long id) {
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
        Optional<TillVaultCurrency> optionalTillVaultCurrency = tillVaultCurrencyService.findById(id);
        if (optionalTillVaultCurrency.isPresent()) {
            return new ResponseEntity<>(optionalTillVaultCurrency.get(), HttpStatus.OK);
        } else {
            successAndErrorDetailsResource.setMessages(RECORD_NOT_FOUND);
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(value ="/{tenantId}/branchId/{branchId}")
    public ResponseEntity<Object> getByBranchId(@PathVariable (value = "tenantId", required = true) String tenantId,
                                                @PathVariable (value = "branchId", required = true) Long branchId){
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
        List<TillVaultCurrency> tillVaultCurrencyList = tillVaultCurrencyService.findByBranchId(branchId);
        int size = tillVaultCurrencyList.size();
        if(size>0){
            return new ResponseEntity<>(tillVaultCurrencyList,HttpStatus.OK);
        }
        else {
            successAndErrorDetailsResource.setMessages(RECORD_NOT_FOUND);
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.NO_CONTENT);
        }

    }


    @GetMapping(value ="/{tenantId}/currencyId/{currencyId}")
    public ResponseEntity<Object> getCurrencyId(@PathVariable (value = "tenantId", required = true) String tenantId,
                                                @PathVariable (value = "currencyId", required = true) Long currencyId){
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
        List<TillVaultCurrency> tillVaultCurrencyList = tillVaultCurrencyService.findByCurrencyId(currencyId);
        int size = tillVaultCurrencyList.size();
        if(size>0){
            return new ResponseEntity<>(tillVaultCurrencyList,HttpStatus.OK);
        }
        else {
            successAndErrorDetailsResource.setMessages(RECORD_NOT_FOUND);
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.NO_CONTENT);
        }

    }


    @GetMapping(value ="/{tenantId}/status/{status}")
    public ResponseEntity<Object> getByStatus(@PathVariable (value = "tenantId", required = true) String tenantId,
                                              @PathVariable (value = "status", required = true) String status){
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();

        if (status.equals("ACTIVE") || status.equals("INACTIVE")) {
            List<TillVaultCurrency> tillVaultCurrencyList = tillVaultCurrencyService.findByStatus(TillVaultCurrencyStatus.valueOf(status));
            int size = tillVaultCurrencyList.size();
            if (size > 0) {
                return new ResponseEntity<>(tillVaultCurrencyList, HttpStatus.OK);
            } else {
                successAndErrorDetailsResource.setMessages(RECORD_NOT_FOUND);
                return new ResponseEntity<>(successAndErrorDetailsResource,HttpStatus.NO_CONTENT);
            }
        }else {
            successAndErrorDetailsResource.setMessages(environment.getProperty("invalid-status"));
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.NO_CONTENT);
        }
    }


    @PutMapping(value ="/{tenantId}/update/{id}")
    public ResponseEntity<Object> updateTillVaultCurrency(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                                      @PathVariable(value = "id", required = true) Long id,
                                                                      @Valid @RequestBody TillVaultCurrencyResource tillVaultCurrencyResource){
        if(LogginAuthentcation.getUserName()==null || LogginAuthentcation.getUserName().isEmpty()){
            throw new UserNotFound(environment.getProperty("common.user-not-found"));
        }
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
        Optional<TillVaultCurrency> optionalTillVaultCurrency = tillVaultCurrencyService.findById(id);
        if (optionalTillVaultCurrency.isPresent()){
            TillVaultCurrency tillVaultCurrency = tillVaultCurrencyService.updateTillVaultCurrency(tenantId,id,tillVaultCurrencyResource);
            successAndErrorDetailsResource = new SuccessAndErrorDetailsResource(environment.getProperty("tii-vault-currency.updates"), tillVaultCurrency.getId().toString());
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.OK);
        }
        else {
            successAndErrorDetailsResource.setMessages(RECORD_NOT_FOUND);
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
