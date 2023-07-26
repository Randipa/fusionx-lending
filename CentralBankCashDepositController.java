package com.fusionx.central.cash.management.controller;
import com.fusionx.central.cash.management.base.MessagePropertyBase;
import com.fusionx.central.cash.management.core.LogginAuthentcation;
import com.fusionx.central.cash.management.domain.CentralBankCashDeposit;
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.resource.CentralBankCashDepositResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.CentralBankCashDepositService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/central-bank-cash-deposit")
@CrossOrigin(origins = "*")
public class CentralBankCashDepositController extends MessagePropertyBase {

    @Autowired
    Environment environment;

    @Autowired
    CentralBankCashDepositService centralBankCashDepositService;

    @PostMapping(value = "/{tenantId}")
    public ResponseEntity<Object> addCentralBankCashDeposit(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                       @Valid @RequestBody CentralBankCashDepositResource centralBankCashDepositResource){

        if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty()) {
            throw new UserNotFound(environment.getProperty("common.user-not-found"));
        }
        CentralBankCashDeposit centralBankCashDeposit = centralBankCashDepositService.addCentralBankCashDeposit(tenantId, centralBankCashDepositResource);
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource(environment.getProperty("central-bank-cash-deposit.saved"),Long.toString(centralBankCashDeposit.getId()));
        return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.ACCEPTED);

    }
}
