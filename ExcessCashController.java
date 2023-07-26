package com.fusionx.central.cash.management.controller;

import com.fusionx.central.cash.management.base.MessagePropertyBase;
import com.fusionx.central.cash.management.core.LogginAuthentcation;
import com.fusionx.central.cash.management.domain.TransferExcessCashToCentralCash;
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.exception.ValidateRecordException;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.resource.TransferExcessCashToCentralCashRequestResource;
import com.fusionx.central.cash.management.service.ExcessCashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/excess-cash")
@CrossOrigin(origins = "*")
public class ExcessCashController extends MessagePropertyBase {

    @Autowired
    private Environment environment;

    @Autowired
    private ExcessCashService excessCashService;

    private String commonSaved = "common.saved";

    private String userNotFound = "common.user-not-found";

    private String commonUpdated = "common.updated";

    private String recordNotFound = "common.record-not-found";

    /**
     * Save Excess Cash To Central Cash.
     *
     * @param tenantId                                       the tenantId
     * @param transferExcessCashToCentralCashRequestResource the transfer excess cash to central cash
     * @return the response entity
     */
    @PostMapping(value = "/{tenantId}")
    public ResponseEntity<Object> saveExcessCash(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                 @Valid @RequestBody TransferExcessCashToCentralCashRequestResource transferExcessCashToCentralCashRequestResource) {

        if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
            throw new ValidateRecordException(environment.getProperty(COMMON_NOT_NULL), USERNAME);

        SuccessAndErrorDetailsResource successAndErrorDetails = new SuccessAndErrorDetailsResource();
        TransferExcessCashToCentralCash transferExcessCashToCentralCash = excessCashService.saveExcessCash(tenantId, transferExcessCashToCentralCashRequestResource);
        successAndErrorDetails = new SuccessAndErrorDetailsResource(environment.getProperty(commonSaved), Long.toString(transferExcessCashToCentralCash.getId()));
        return new ResponseEntity<>(successAndErrorDetails, HttpStatus.CREATED);
    }

    /**
     * Update Excess Cash To Central Cash.
     *
     * @param tenantId                                       the tenant id
     * @param id                                             the id
     * @param transferExcessCashToCentralCashRequestResource the transfer excess cash to central cash
     * @return the response entity
     */
    @PutMapping(value = "/{tenantId}/{id}")
    public ResponseEntity<Object> updateExcessCash(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                   @PathVariable(value = "id", required = true) Long id,
                                                   @Valid @RequestBody TransferExcessCashToCentralCashRequestResource transferExcessCashToCentralCashRequestResource) {

        if(LogginAuthentcation.getUserName()==null || LogginAuthentcation.getUserName().isEmpty()){
            throw new UserNotFound(environment.getProperty(userNotFound));
        }
        SuccessAndErrorDetailsResource successAndErrorDetails = new SuccessAndErrorDetailsResource();
        Optional<TransferExcessCashToCentralCash> optionalExcessCashToCentralCash = excessCashService.findById(id);
        if (optionalExcessCashToCentralCash.isPresent()) {
            TransferExcessCashToCentralCash excessCash = excessCashService.updateExcessCash(tenantId, id,transferExcessCashToCentralCashRequestResource);
            successAndErrorDetails = new SuccessAndErrorDetailsResource(environment.getProperty(commonUpdated), excessCash.getId().toString());
            return new ResponseEntity<>(successAndErrorDetails, HttpStatus.OK);
        } else {
            successAndErrorDetails = new SuccessAndErrorDetailsResource(environment.getProperty(recordNotFound));
            return new ResponseEntity<>(successAndErrorDetails, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * Gets All Excess Cash.
     *
     * @param tenantId the tenant id
     * @return the all excess cash
     */
    @GetMapping(value ="/{tenantId}/all")
    public ResponseEntity<Object> getAllExcessCash(@PathVariable (value = "tenantId", required = true) String tenantId) {
        SuccessAndErrorDetailsResource successAndErrorDetails = new SuccessAndErrorDetailsResource();
        List<TransferExcessCashToCentralCash> excessCashList = excessCashService.findAll();
        int size = excessCashList.size();
        if(size>0){
            return new ResponseEntity<>(excessCashList,HttpStatus.OK);
        } else {
            successAndErrorDetails.setMessages(RECORD_NOT_FOUND);
            return new ResponseEntity<>(successAndErrorDetails, HttpStatus.NO_CONTENT);
        }
    }

    /**
     * Gets All Excess Cash By id.
     *
     * @param tenantId the tenant id
     * @param id       the id
     * @return the all excess cash by id
     */
    @GetMapping(value = "/{tenantId}/{id}")
    public ResponseEntity<Object> getExcessCashById(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                    @PathVariable(value = "id", required = true) Long id) {
        SuccessAndErrorDetailsResource successAndErrorDetails = new SuccessAndErrorDetailsResource();
        Optional<TransferExcessCashToCentralCash> isPresentExcessCash = excessCashService.findById(id);
        if (isPresentExcessCash.isPresent()) {
            return new ResponseEntity<>(isPresentExcessCash, HttpStatus.OK);
        } else {
            successAndErrorDetails.setMessages(environment.getProperty(recordNotFound));
            return new ResponseEntity<>(successAndErrorDetails, HttpStatus.NO_CONTENT);
        }
    }

}
