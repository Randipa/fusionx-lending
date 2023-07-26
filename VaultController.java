package com.fusionx.central.cash.management.controller;

import com.fusionx.central.cash.management.base.MessagePropertyBase;
import com.fusionx.central.cash.management.core.LogginAuthentcation;

import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.domain.Vault;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.resource.VaultResource;
import com.fusionx.central.cash.management.service.VaultService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/vault")
@CrossOrigin(origins = "*")
public class VaultController extends MessagePropertyBase {

    @Autowired
    private Environment environment;

    @Autowired
    private VaultService vaultService;

    private String userNotFound = "common.user-not-found";

    private String commonSaved = "common.saved";

    private String commonUpdated = "common.updated";

    private String recordNotFound = "common.record-not-found";

    private String pageableLength = "common.pageable-length";

    @GetMapping(value = "/{tenantId}/all")
    public ResponseEntity<Object> getAllVault(@PathVariable(value = "tenantId", required = true) String tenantId,
                                              @PageableDefault(size = 10) Pageable pageable) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        List <Vault> vault = vaultService.findAll();
        int size = vault.size();
        if(size > 0) {
            return new ResponseEntity<>((Collection<Vault>) vault, HttpStatus.OK);
        } else {
            responseMessage.setMessages(recordNotFound);
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }


    @GetMapping(value = "/{tenantId}/{id}")
    public ResponseEntity<Object> getVaultById(@PathVariable(value = "tenantId", required = true) String tenantId,
                                               @PathVariable(value = "id", required = true) Long id) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        Optional<Vault> isPresentVault = vaultService.findById(id);
        if (isPresentVault.isPresent()) {
            return new ResponseEntity<>(isPresentVault, HttpStatus.OK);
        } else {
            responseMessage.setMessages(environment.getProperty(recordNotFound));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }


    @GetMapping(value = "/{tenantId}/status/{status}")
    public ResponseEntity<Object> getVaultByStatus(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                   @PathVariable(value = "status", required = true) String status,
                                                   @PageableDefault(size = 10) Pageable pageable) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        List <Vault> vault = vaultService.findByStatus(status);
        if(!vault.isEmpty()){
            return new ResponseEntity<>((Collection<Vault>) vault, HttpStatus.OK);
        } else {
            responseMessage.setMessages(environment.getProperty(recordNotFound));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }


    @GetMapping(value = "/{tenantId}/vault-code/{code}")
    public ResponseEntity<Object> getVaultByVaultCode(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                      @PathVariable(value = "code", required = true) String code) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        Optional<Vault> isPresentVault = vaultService.findByVaultCode(code);
        if (isPresentVault.isPresent()) {
            return new ResponseEntity<>(isPresentVault, HttpStatus.OK);
        } else {
            responseMessage.setMessages(environment.getProperty(recordNotFound));
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }



    @GetMapping(value = "/{tenantId}/vault-name/{name}")
    public ResponseEntity<Object> getVaultByVaultName(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                      @PathVariable(value = "name", required = true) String name,
                                                      @PageableDefault(size = 10) Pageable pageable) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        List<Vault> isPresentVault = vaultService.findByVaultName(name);
        int size = isPresentVault.size();
        if(size>0){
            return new ResponseEntity<>(isPresentVault,HttpStatus.OK);
        }
        else {
            responseMessage.setMessages(recordNotFound);
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }



    @GetMapping(value = "/{tenantId}/branch/{branchId}")
    public ResponseEntity<Object> getVaultByBranchId(@PathVariable(value = "tenantId", required = true) String tenantId,
                                                     @PathVariable(value = "branchId", required = true) Long branchId,
                                                     @PageableDefault(size = 10) Pageable pageable) {
        SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
        List<Vault> isPresentVault = vaultService.findByBranchId(branchId);
        int size = isPresentVault.size();
        if(size>0){
            return new ResponseEntity<>(isPresentVault,HttpStatus.OK);
        }
        else {
            responseMessage.setMessages(recordNotFound);
            return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
        }
    }


    @PostMapping(value = "/{tenantId}")
    public ResponseEntity<Object> addVaults(@PathVariable(value = "tenantId", required = true) String tenantId,
                                            @Valid @RequestBody VaultResource vaultResource) {
        if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty()) {
            throw new UserNotFound(environment.getProperty(userNotFound));
        }
        SuccessAndErrorDetailsResource successAndErrorDetails = new SuccessAndErrorDetailsResource();
        Vault vault = vaultService.addVaults(tenantId,LogginAuthentcation.getUserName(), vaultResource);
        successAndErrorDetails = new SuccessAndErrorDetailsResource(environment.getProperty(commonSaved), Long.toString(vault.getId()));
        return new ResponseEntity<>(successAndErrorDetails, HttpStatus.CREATED);
    }



    @PutMapping(value = "/{tenantId}/{id}")
    public ResponseEntity<Object> updateVaults(@PathVariable(value = "tenantId", required = true) String tenantId,
                                               @PathVariable(value = "id", required = true) Long id,
                                               @Valid @RequestBody VaultResource vaultResource){
        if(LogginAuthentcation.getUserName()==null || LogginAuthentcation.getUserName().isEmpty()){
            throw new UserNotFound(environment.getProperty(userNotFound));
        }
        SuccessAndErrorDetailsResource successAndErrorDetails = new SuccessAndErrorDetailsResource();
        Optional<Vault> optionalVault = vaultService.findById(id);
        if (optionalVault.isPresent()) {
            Vault vault = vaultService.updateVaults(tenantId,LogginAuthentcation.getUserName(), id, vaultResource);
            successAndErrorDetails = new SuccessAndErrorDetailsResource(environment.getProperty(commonUpdated), vault.getId().toString());
            return new ResponseEntity<>(successAndErrorDetails, HttpStatus.OK);
        } else {
            successAndErrorDetails = new SuccessAndErrorDetailsResource(environment.getProperty(recordNotFound));
            return new ResponseEntity<>(successAndErrorDetails, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


}

