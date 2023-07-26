package com.fusionx.central.cash.management.controller;

import com.fusionx.central.cash.management.Constants;
import com.fusionx.central.cash.management.core.LogginAuthentcation;
import com.fusionx.central.cash.management.domain.CashCourier;
import com.fusionx.central.cash.management.exception.PageableLengthException;
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.resource.*;
import com.fusionx.central.cash.management.service.CashCourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping(value = "/cash_courier")
@CrossOrigin(origins = "*")
public class CashCourierController {

    @Autowired
    private Environment environment;

    @Autowired
    private CashCourierService cashCourierService;

    private String recordNotFound = "common.record-not-found";

    private String userNotFound = "common.user-not-found";

    private String commonUpdated = "common.updated";

    private String commonSaved = "common.saved";

    private String pageableLength = "common.pageable-length";

    @GetMapping(value = "/{tenantId}/all")
    public Page<CashCourier> getAllCashCourier(@PathVariable(value = "tenantId", required = true) String tenantId, @PageableDefault(size = 10)Pageable pageable) {
        if (pageable.getPageSize() > Constants.MAX_PAGEABLE_LENGTH)
            throw new PageableLengthException(environment.getProperty(pageableLength));
        return cashCourierService.findAll(pageable);
    }

    @GetMapping(value = "/{tenantId}/{id}")
    public ResponseEntity<Object> getCashCourierById(
            @PathVariable(value = "tenantId", required = true) String tenantId,
            @PathVariable(value = "id", required = true) Long id) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        Optional<CashCourier> isPresentCashCourier = cashCourierService.findById(id);
        if (isPresentCashCourier.isPresent()) {
            return new ResponseEntity<>(isPresentCashCourier, HttpStatus.OK);
        } else {
            responseMessage.setMessages(environment.getProperty(recordNotFound));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping(value = "/{tenantId}")
    public ResponseEntity<Object> addCashCourier(@PathVariable(value = "tenantId", required = true) String tenantId,
                                             @Valid @RequestBody AddCashCourierResource addCashCourierResource) {
        if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
            throw new UserNotFound(environment.getProperty(userNotFound));
        Long id = cashCourierService.saveCashCourier(tenantId, addCashCourierResource);
        SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(commonSaved), id.toString());
        return new ResponseEntity<>(successDetailsDto,HttpStatus.CREATED);
    }


    @PutMapping(value = "/{tenantId}/{id}")
    public ResponseEntity<Object> updateCashCourier(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                @PathVariable(value = "id", required = true) Long id,
                                                @Valid @RequestBody UpdateCashCourierResource updateCashCourierResource) {
        if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
            throw new UserNotFound(environment.getProperty(userNotFound));

        cashCourierService.updateCashCourier(tenantId, updateCashCourierResource);
        SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(commonUpdated));
        return new ResponseEntity<>(successDetailsDto, HttpStatus.OK);
    }

}
