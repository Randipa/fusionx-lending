package com.fusionx.central.cash.management.controller;

import com.fusionx.central.cash.management.base.MessagePropertyBase;
import com.fusionx.central.cash.management.core.LogginAuthentcation;
import com.fusionx.central.cash.management.domain.BranchTill;
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.resource.AssignTillToTellerUpdateResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.AssignTillToTellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/assign-till-to-teller")
@CrossOrigin(origins = "*")
public class AssignTillToTellerController extends MessagePropertyBase {

    private final AssignTillToTellerService assignTillToTellerService;

    @Autowired
    public AssignTillToTellerController(AssignTillToTellerService assignTillToTellerService) {
        this.assignTillToTellerService = assignTillToTellerService;
    }

    @GetMapping(value = "/{tenantId}/all")
    public ResponseEntity<Object> getAll(@PathVariable(value = "tenantId") String tenantId) {
        List<BranchTill> branchTillList = assignTillToTellerService.findAll(tenantId);
        if (!branchTillList.isEmpty()) {
            return new ResponseEntity<>(branchTillList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(branchTillList,HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(value = "/{tenantId}/till-name/{name}")
    public ResponseEntity<Object> getByTillName(@PathVariable(value = "tenantId") String tenantId,
                                                @PathVariable(value = "name") String name) {
        List<BranchTill> branchTillList = assignTillToTellerService.findByName(tenantId,name);
        if (!branchTillList.isEmpty()) {
            return new ResponseEntity<>(branchTillList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(branchTillList,HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping(value = "/{tenantId}/teller-user-code/{code}")
    public ResponseEntity<Object> getAll(@PathVariable(value = "tenantId") String tenantId,
                                         @PathVariable(value = "code") String code) {
        List<BranchTill> branchTillList = assignTillToTellerService.findByTellerCustomerCode(tenantId, code);
        if (!branchTillList.isEmpty()) {
            return new ResponseEntity<>(branchTillList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(branchTillList,HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping(value = "/{tenantId}")
    public ResponseEntity<Object> update(@PathVariable(value = "tenantId") String tenantId,
                                         @Valid @RequestBody AssignTillToTellerUpdateResource assignTillToTellerUpdateResource) {
        if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty()) {
            throw new UserNotFound(environment.getProperty(USER_NOT_FOUND));
        }
        Long updatedId = assignTillToTellerService.updateBranchTill(tenantId, assignTillToTellerUpdateResource);
        SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(RECORD_UPDATED),updatedId.toString());
        return new ResponseEntity<>(successDetailsDto, HttpStatus.OK);
    }
}
