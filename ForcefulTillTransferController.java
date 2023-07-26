package com.fusionx.central.cash.management.controller;

import com.fusionx.central.cash.management.base.MessagePropertyBase;
import com.fusionx.central.cash.management.core.LogginAuthentcation;
import com.fusionx.central.cash.management.domain.ForcefulTillTransfer;
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.resource.ForcefulTillTransferResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.ForcefulTillTransferService;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/forceful-till-transfer")
@CrossOrigin(origins = "*")
public class ForcefulTillTransferController extends MessagePropertyBase {

    @Autowired
    ForcefulTillTransferService forcefulTillTransferService;

    @Autowired
    Environment environment;

    @PostMapping(value = "/{tenantId}")
    public ResponseEntity<Object> addForcefulTillTransfer(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                          @Valid @RequestBody ForcefulTillTransferResource forcefulTillTransferResource) {

        if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty()) {
            throw new UserNotFound(environment.getProperty(USER_NOT_FOUND));
        }

        ForcefulTillTransfer forcefulTillTransfer = forcefulTillTransferService.addForcefulTillTransfer(tenantId, forcefulTillTransferResource);
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource(environment.getProperty(FORCEFUL_TILL_SAVED), Long.toString(forcefulTillTransfer.getId()));
        return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.ACCEPTED);
    }

    @GetMapping(value ="/{tenantId}/all")
    public ResponseEntity<Object> getAllForcefulTillTransfer(@PathVariable(value = "tenantId", required = true) String tenantId){
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
        List<ForcefulTillTransfer> forcefulTillTransferList = forcefulTillTransferService.findAll();

        int size =  forcefulTillTransferList.size();
        if(size>0){
            return new ResponseEntity<>(forcefulTillTransferList,HttpStatus.OK);
        }else{
            successAndErrorDetailsResource.setMessages(RECORD_NOT_FOUND);
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(value ="/{tenantId}/{id}")
    public ResponseEntity<Object> getById(@PathVariable (value = "tenantId", required = true) String tenantId,
                                          @PathVariable (value = "id", required = true) Long id){
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
        Optional<ForcefulTillTransfer> optionalForcefulTillTransfer = forcefulTillTransferService.findById(id);
        if (optionalForcefulTillTransfer.isPresent()) {
            return new ResponseEntity<>(optionalForcefulTillTransfer.get(), HttpStatus.OK);
        } else {
            successAndErrorDetailsResource.setMessages(RECORD_NOT_FOUND);
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(value ="/{tenantId}/tillId/{tillId}")
    public ResponseEntity<Object> getByTillId(@PathVariable (value = "tenantId", required = true) String tenantId,
                                                @PathVariable (value = "tillId", required = true) Long tillId){
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
        List<ForcefulTillTransfer> forcefulTillTransferList = forcefulTillTransferService.findByTillId(tillId);
        int size = forcefulTillTransferList.size();
        if(size>0){
            return new ResponseEntity<>(forcefulTillTransferList,HttpStatus.OK);
        }
        else {
            successAndErrorDetailsResource.setMessages(RECORD_NOT_FOUND);
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(value ="/{tenantId}/vaultId/{vaultId}")
    public ResponseEntity<Object> getByVaultId(@PathVariable (value = "tenantId", required = true) String tenantId,
                                                @PathVariable (value = "vaultId", required = true) Long vaultId){
        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
        List<ForcefulTillTransfer> forcefulTillTransferList = forcefulTillTransferService.findByVaultId(vaultId);
        int size = forcefulTillTransferList.size();
        if(size>0){
            return new ResponseEntity<>(forcefulTillTransferList,HttpStatus.OK);
        }
        else {
            successAndErrorDetailsResource.setMessages(RECORD_NOT_FOUND);
            return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.NO_CONTENT);
        }
    }
}


