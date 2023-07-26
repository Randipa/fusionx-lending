package com.fusionx.central.cash.management.controller;

import com.fusionx.central.cash.management.base.MessagePropertyBase;
import com.fusionx.central.cash.management.core.LogginAuthentcation;
import com.fusionx.central.cash.management.domain.PrinterSetup;
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.resource.*;
import com.fusionx.central.cash.management.service.PrinterSetupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/printer-setup")
@CrossOrigin("*")
public class PrinterSetupController extends MessagePropertyBase {

    @Autowired
    private PrinterSetupService printerSetupService;

    private String userNotFound = "common.user-not-found";

    private String commonSaved = "common.saved";


    /**
     * get printer setup by id
     * @param @PathVariable{tenantId}
     * @param @PathVariable{id}
     * @return List
     **/
    @GetMapping(value = "/{tenantId}/{id}")
    public ResponseEntity<Object> findByPrinterSetupId(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                       @PathVariable(value = "id", required = true) Long id){
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();

        Optional<PrinterSetup> isPresentPrinterSetup = printerSetupService.findById(id);
        if(isPresentPrinterSetup.isPresent()) {
            return new ResponseEntity<>(isPresentPrinterSetup.get(), HttpStatus.OK);
        }
        else {
            responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }

    /**
     * get all printer setup details
     * @param @PathVariable{tenantId}
     * @param @PathVariable{all}
     * @return List
     **/

    @GetMapping("/{tenantId}/all")
    public ResponseEntity<Object> findAllPrinterSetup(@PathVariable(value = "tenantId", required = true) String tenantId){
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        List<PrinterSetup> isPresentPrinterSetup= printerSetupService.findAllPrinterSetup();
        if(!isPresentPrinterSetup.isEmpty()) {
            return new ResponseEntity<>((Collection<PrinterSetup>)isPresentPrinterSetup, HttpStatus.OK);
        }
        else {
            responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }

    /**
     * get printer setup by branch
     * @param @pathVariable{tenantId}
     * @param @pathVariable {branch}
     * @return List
     **/
    @GetMapping(value = "/{tenantId}/branch/{branchId}")
    public ResponseEntity<Object> findByBranchId(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                 @PathVariable(value = "branchId", required = true) Long branchId){
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        List<PrinterSetup> isPresentPrinterSetup = printerSetupService.findByBranchId(branchId);
        if(!isPresentPrinterSetup.isEmpty()) {
            return new ResponseEntity<>((Collection<PrinterSetup>)isPresentPrinterSetup, HttpStatus.OK);
        }
        else {
            responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }

    /**
     * get printer setup by department
     * @param @pathVariable{tenantId}
     * @param @pathVariable {department}
     * @return List
     **/
    @GetMapping(value = "/{tenantId}/department/{departmentId}")
    public ResponseEntity<Object> findByDepartmentId(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                     @PathVariable(value = "departmentId", required = true) Long departmentId){
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        List<PrinterSetup> isPresentPrinterSetup = printerSetupService.findByDepartmentId(departmentId);
        if(!isPresentPrinterSetup.isEmpty()) {
            return new ResponseEntity<>((Collection<PrinterSetup>)isPresentPrinterSetup, HttpStatus.OK);
        }
        else {
            responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }

    /**
     * get printer setup by branch & department
     * @param @pathVariable{tenantId}
     * @param @pathVariable {branch}
     * @param @pathVariable {department}
     * @return List
     **/
    @GetMapping(value = "/{tenantId}/branch/{branch}/department/{department}")
    public ResponseEntity<Object> findByBranchIdAndDepartmentId(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                                @PathVariable(value = "branch", required = true) Long branchId,
                                                                @PathVariable(value = "department", required = true) Long departmentId) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        List<PrinterSetup> isPresentPrinterSetup = printerSetupService.findByBranchIdAndDepartmentId(branchId, departmentId);
        if(!isPresentPrinterSetup.isEmpty()) {
            return new ResponseEntity<>((Collection<PrinterSetup>)isPresentPrinterSetup, HttpStatus.OK);
        }
        else {
            responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }

    /**
     * find printer setup by status
     * @param @PathVariable{tenantId}
     * @param @RequestBody{PrinterSetupAddResource}
     * @return SuccessAndErrorDetailsDto
     */
    @GetMapping(value = "/{tenantId}/status/{status}")
    public ResponseEntity<Object> findByPrinterSetupStatus(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                           @PathVariable(value = "status", required = true) String status) {

        List<PrinterSetup> list = printerSetupService.findByStatus(status);
        if (!list.isEmpty())
            return new ResponseEntity<>(list, HttpStatus.OK);
        else {
            return new ResponseEntity<>(list, HttpStatus.NO_CONTENT);
        }
    }

    /**
     * save Printer Setup
     * @param @PathVariable{tenantId}
     * @param @RequestBody{PrinterSetupAddResource}
     * @return SuccessAndErrorDetailsDto
     */
    @PostMapping("/{tenantId}")
    public ResponseEntity<Object> savePrinterSetup(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                   @Valid @RequestBody PrinterSetupAddResource printerSetupAddResource){
        if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty()) {
            throw new UserNotFound(environment.getProperty(userNotFound));
        }
        SuccessAndErrorDetailsResource successAndErrorDetails = new SuccessAndErrorDetailsResource();
        PrinterSetup printerSetup = printerSetupService.savePrinterSetup(tenantId, printerSetupAddResource);
        successAndErrorDetails = new SuccessAndErrorDetailsResource(environment.getProperty(commonSaved), Long.toString(printerSetup.getId()));
        return new ResponseEntity<>(successAndErrorDetails, HttpStatus.CREATED);
    }

    /**
     * update PaymentMethod
     * @param @PathVariable{tenantId}
     * @param @RequestBody{CommonUpdateResource}
     * @return SuccessAndErrorDetailsDto
     */
    @PutMapping(value = "{tenantId}/{id}")
    public ResponseEntity<Object> updatePrinterSetup(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                      @PathVariable(value = "id", required = true) Long id,
                                                      @Valid @RequestBody PrinterSetupUpdateResource printerSetupUpdateResource){
        SuccessAndErrorDetailsResource successAndErrorDetailsResource=new SuccessAndErrorDetailsResource();
        Optional<PrinterSetup>isPresentPrinterSetup = printerSetupService.findById(id);

        if(isPresentPrinterSetup.isPresent()) {
            printerSetupUpdateResource.setId(id.toString());
            PrinterSetup printerSetup = printerSetupService.updatePrinterSetup(tenantId, printerSetupUpdateResource);
            successAndErrorDetailsResource = new SuccessAndErrorDetailsResource(getEnvironment().getProperty(RECORD_UPDATED), printerSetup.getId().toString());
            return new ResponseEntity<>(successAndErrorDetailsResource,HttpStatus.OK);
        }
        else {
            successAndErrorDetailsResource.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


}
