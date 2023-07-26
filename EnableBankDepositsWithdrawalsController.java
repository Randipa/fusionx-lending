package com.fusionx.central.cash.management.controller;

import com.fusionx.central.cash.management.base.MessagePropertyBase;
import com.fusionx.central.cash.management.core.LogginAuthentcation;
import com.fusionx.central.cash.management.domain.EnableBankDepositsWithdrawals;
import com.fusionx.central.cash.management.enums.BankDeposit;
import com.fusionx.central.cash.management.enums.BankWithdrawal;
import com.fusionx.central.cash.management.enums.EnableBankDepositWithdrawalStatus;
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.resource.EnableBankDepositsWithdrawalsResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.EnableBankDepositsWithdrawalsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/bank-deposits-withdrawal")
@CrossOrigin(origins = "*")
public class EnableBankDepositsWithdrawalsController extends MessagePropertyBase {

    @Autowired
    EnableBankDepositsWithdrawalsService enableBankDepositsWithdrawalsService;

    @Autowired
    Environment environment;

    @PostMapping(value = "/{tenantId}")
    public ResponseEntity<Object> addEnableBankDepositsWithdrawals(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                                   @Valid @RequestBody EnableBankDepositsWithdrawalsResource enableBankDepositsWithdrawalsResource) {

        if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty()) {
            throw new UserNotFound(environment.getProperty("common.user-not-found"));
        }
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
        EnableBankDepositsWithdrawals enableBankDepositsWithdrawals = enableBankDepositsWithdrawalsService.addEnableBankDepositsWithdrawals(tenantId, enableBankDepositsWithdrawalsResource);
        successAndErrorDetailsResource = new SuccessAndErrorDetailsResource(environment.getProperty("bank-deposit-withdrawals.saved"),Long.toString(enableBankDepositsWithdrawals.getId()));
        return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.ACCEPTED);
    }

    @GetMapping(value ="/{tenantId}/all")
    public ResponseEntity<Object> getAllEnableBankDepositsWithdrawals(@PathVariable (value = "tenantId", required = true) String tenantId){
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
        List<EnableBankDepositsWithdrawals> enableBankDepositsWithdrawalsList = enableBankDepositsWithdrawalsService.findAll();

        int size = enableBankDepositsWithdrawalsList.size();
        if(size>0){
            return new ResponseEntity<>(enableBankDepositsWithdrawalsList,HttpStatus.OK);
        }
        else {
            successAndErrorDetailsResource.setMessages(RECORD_NOT_FOUND);
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(value ="/{tenantId}/{id}")
    public ResponseEntity<Object> getById(@PathVariable (value = "tenantId", required = true) String tenantId,
                                          @PathVariable (value = "id", required = true) Long id){
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
        Optional<EnableBankDepositsWithdrawals> optionalEnableBankDepositsWithdrawals = enableBankDepositsWithdrawalsService.findById(id);
        if(optionalEnableBankDepositsWithdrawals.isPresent()){
            return new ResponseEntity<>(optionalEnableBankDepositsWithdrawals.get(),HttpStatus.OK);
        }
        else {
            successAndErrorDetailsResource.setMessages(RECORD_NOT_FOUND);
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(value ="/{tenantId}/branchId/{branchId}")
    public ResponseEntity<Object> getByBranchId(@PathVariable (value = "tenantId", required = true) String tenantId,
                                                @PathVariable (value = "branchId", required = true) Long branchId){
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
        List<EnableBankDepositsWithdrawals> enableBankDepositsWithdrawalsList = enableBankDepositsWithdrawalsService.findByBranchId(branchId);
        int size = enableBankDepositsWithdrawalsList.size();
        if(size>0){
            return new ResponseEntity<>(enableBankDepositsWithdrawalsList,HttpStatus.OK);
        }
        else {
            successAndErrorDetailsResource.setMessages(RECORD_NOT_FOUND);
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.NO_CONTENT);
        }

    }

    @GetMapping(value ="/{tenantId}/bank-accountId/{bankAccountId}")
    public ResponseEntity<Object> getByBankAccount(@PathVariable (value = "tenantId", required = true) String tenantId,
                                                   @PathVariable (value = "bankAccountId", required = true) Long bankAccountId){
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
        List<EnableBankDepositsWithdrawals> enableBankDepositsWithdrawalsList = enableBankDepositsWithdrawalsService.findByBankAccountId(bankAccountId);
        int size = enableBankDepositsWithdrawalsList.size();
        if(size>0){
            return new ResponseEntity<>(enableBankDepositsWithdrawalsList,HttpStatus.OK);
        }
        else {
            successAndErrorDetailsResource.setMessages(RECORD_NOT_FOUND);
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(value ="/{tenantId}/bank-account-code/{bankAccountCode}")
    public ResponseEntity<Object> getByBankAccountCode(@PathVariable (value = "tenantId", required = true) String tenantId,
                                                       @PathVariable (value = "bankAccountCode", required = true) String bankAccountCode){
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
        List<EnableBankDepositsWithdrawals> enableBankDepositsWithdrawalsList = enableBankDepositsWithdrawalsService.findByBankAccountCode(bankAccountCode);
        int size = enableBankDepositsWithdrawalsList.size();
        if(size>0){
            return new ResponseEntity<>(enableBankDepositsWithdrawalsList,HttpStatus.OK);
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
            List<EnableBankDepositsWithdrawals> enableBankDepositsWithdrawalsList = enableBankDepositsWithdrawalsService.findByStatus(EnableBankDepositWithdrawalStatus.valueOf(status));
            int size = enableBankDepositsWithdrawalsList.size();
            if (size > 0) {
                return new ResponseEntity<>(enableBankDepositsWithdrawalsList, HttpStatus.OK);
            } else {
                successAndErrorDetailsResource.setMessages(RECORD_NOT_FOUND);
                return new ResponseEntity<>(successAndErrorDetailsResource,HttpStatus.NO_CONTENT);
            }
        }else {
            successAndErrorDetailsResource.setMessages(environment.getProperty("invalid-status"));
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(value ="/{tenantId}/bankDeposit/{bankDeposit}")
    public ResponseEntity<Object> getByBankDeposit(@PathVariable (value = "tenantId", required = true) String tenantId,
                                              @PathVariable (value = "bankDeposit", required = true) String bankDeposit){
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();

        if (bankDeposit.equals("YES") || bankDeposit.equals("NO")) {
            List<EnableBankDepositsWithdrawals> enableBankDepositsWithdrawalsList = enableBankDepositsWithdrawalsService.findByBankDeposit(BankDeposit.valueOf(bankDeposit));
            int size = enableBankDepositsWithdrawalsList.size();
            if (size > 0) {
                return new ResponseEntity<>(enableBankDepositsWithdrawalsList, HttpStatus.OK);
            } else {
                successAndErrorDetailsResource.setMessages(RECORD_NOT_FOUND);
                return new ResponseEntity<>(successAndErrorDetailsResource,HttpStatus.NO_CONTENT);
            }
        }else {
            successAndErrorDetailsResource.setMessages(environment.getProperty("invalid-BankDeposit"));
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(value ="/{tenantId}/bankWithdrawal/{bankWithdrawal}")
    public ResponseEntity<Object> getByBankWithdrawal(@PathVariable (value = "tenantId", required = true) String tenantId,
                                                   @PathVariable (value = "bankWithdrawal", required = true) String bankWithdrawal){
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();

        if (bankWithdrawal.equals("YES") || bankWithdrawal.equals("NO")) {
            List<EnableBankDepositsWithdrawals> enableBankDepositsWithdrawalsList = enableBankDepositsWithdrawalsService.findByBankWithdrawal(BankWithdrawal.valueOf(bankWithdrawal));
            int size = enableBankDepositsWithdrawalsList.size();
            if (size > 0) {
                return new ResponseEntity<>(enableBankDepositsWithdrawalsList, HttpStatus.OK);
            } else {
                successAndErrorDetailsResource.setMessages(RECORD_NOT_FOUND);
                return new ResponseEntity<>(successAndErrorDetailsResource,HttpStatus.NO_CONTENT);
            }
        }else {
            successAndErrorDetailsResource.setMessages(environment.getProperty("invalid-BankDeposit"));
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping(value ="/{tenantId}/update/{id}")
    public ResponseEntity<Object> updateEnableBankDepositsWithdrawals(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                                      @PathVariable(value = "id", required = true) Long id,
                                                                      @Valid @RequestBody EnableBankDepositsWithdrawalsResource enableBankDepositsWithdrawalsResource){
        if(LogginAuthentcation.getUserName()==null || LogginAuthentcation.getUserName().isEmpty()){
            throw new UserNotFound(environment.getProperty("common.user-not-found"));
        }
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
        Optional<EnableBankDepositsWithdrawals> optionalEnableBankDepositsWithdrawals = enableBankDepositsWithdrawalsService.findById(id);
        if (optionalEnableBankDepositsWithdrawals.isPresent()){
            EnableBankDepositsWithdrawals enableBankDepositsWithdrawals = enableBankDepositsWithdrawalsService.updateEnableBankDepositsWithdrawals(tenantId,id,enableBankDepositsWithdrawalsResource);
            successAndErrorDetailsResource = new SuccessAndErrorDetailsResource(environment.getProperty("bank-deposit-withdrawals.updates"), enableBankDepositsWithdrawals.getId().toString());
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.OK);
        }
        else {
            successAndErrorDetailsResource.setMessages(RECORD_NOT_FOUND);
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
