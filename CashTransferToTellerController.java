package com.fusionx.central.cash.management.controller;

import com.fusionx.central.cash.management.core.LogginAuthentcation;
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.resource.*;
import com.fusionx.central.cash.management.service.CashTransferToTellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/cash_transfer_to_teller")
@CrossOrigin(origins = "*")
public class CashTransferToTellerController {

    @Autowired
    private Environment environment;

    @Autowired
    private CashTransferToTellerService cashTransferToTellerService;

    private String recordNotFound = "common.record-not-found";

    private String userNotFound = "common.user-not-found";

    private String commonUpdated = "common.updated";

    private String commonSaved = "common.saved";

    private String pageableLength = "common.pageable-length";

//    @GetMapping(value = "/{tenantId}/all")
//    public Page<CashCourier> getAllCashCourier(@PathVariable(value = "tenantId", required = true) String tenantId, @PageableDefault(size = 10) Pageable pageable) {
//        if (pageable.getPageSize() > Constants.MAX_PAGEABLE_LENGTH)
//            throw new PageableLengthException(environment.getProperty(pageableLength));
//        return cashTransferToTellerService.findAll(pageable);
//    }
//
//    @GetMapping(value = "/{tenantId}/{id}")
//    public ResponseEntity<Object> getCashTransferToTellerById(
//            @PathVariable(value = "tenantId", required = true) String tenantId,
//            @PathVariable(value = "id", required = true) Long id) {
//        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
//        Optional<CashTransferToTeller> isPresentCashTransferToTeller = cashTransferToTellerService.findById(id);
//        if (isPresentCashTransferToTeller.isPresent()) {
//            return new ResponseEntity<>(isPresentCashTransferToTeller, HttpStatus.OK);
//        } else {
//            responseMessage.setMessages(environment.getProperty(recordNotFound));
//            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
//        }
//    }

    @PostMapping(value = "/{tenantId}")
    public ResponseEntity<Object> addCashTransferToTeller(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                 @Valid @RequestBody AddCashTransferToTellerResource addCashTransferToTellerResource) {
        if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
            throw new UserNotFound(environment.getProperty(userNotFound));
        Long id = cashTransferToTellerService.saveCashTransferToTeller(tenantId, addCashTransferToTellerResource);
        SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(commonSaved), id.toString());
        return new ResponseEntity<>(successDetailsDto,HttpStatus.CREATED);
    }

    /*@PostMapping(value = "/{tenantId}")
    public ResponseEntity<Object> transferCurrencies(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                          @Valid @RequestBody TransferCurrenciesResource transferCurrenciesResource) {
        if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
            throw new UserNotFound(environment.getProperty(userNotFound));

        cashTransferToTellerService.saveAndValidateTransferCurrencies(tenantId, transferCurrenciesResource);
        SuccessAndErrorDetailsResource successDetailsDto=new SuccessAndErrorDetailsResource(environment.getProperty("common.saved"));
        return new ResponseEntity<>(successDetailsDto,HttpStatus.CREATED);


    }

    @PostMapping(value = "/{tenantId}")
    public ResponseEntity<Object> transferAmount(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                     @Valid @RequestBody TransferAmountResource transferAmountResource) {
        if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
            throw new UserNotFound(environment.getProperty(userNotFound));

        cashTransferToTellerService.saveAndValidateTransferAmount(tenantId, transferAmountResource);
        SuccessAndErrorDetailsResource successDetailsDto=new SuccessAndErrorDetailsResource(environment.getProperty("common.saved"));
        return new ResponseEntity<>(successDetailsDto,HttpStatus.CREATED);


    }*/







//    @PutMapping(value = "/{tenantId}/{id}")
//    public ResponseEntity<Object> updateCashTransferToTeller(@PathVariable(value = "tenantId", required = true) String tenantId,
//                                                @PathVariable(value = "id", required = true) Long id,
//                                                @Valid @RequestBody UpdateCashTransferToTellerResource updateCashTransferToTellerResource) {
//        if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
//            throw new UserNotFound(environment.getProperty(userNotFound));
//
//        cashTransferToTellerService.updateCashTransferToTeller(tenantId, updateCashTransferToTellerResource);
//        SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(commonUpdated));
//        return new ResponseEntity<>(successDetailsDto, HttpStatus.OK);
//    }

}
