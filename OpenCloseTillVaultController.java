package com.fusionx.central.cash.management.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fusionx.central.cash.management.base.MessagePropertyBase;
import com.fusionx.central.cash.management.core.LogginAuthentcation;
import com.fusionx.central.cash.management.exception.ValidateRecordException;
import com.fusionx.central.cash.management.resource.OpenTillVaultRequestResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.resource.TillResponse;
import com.fusionx.central.cash.management.resource.VaultsResponse;
import com.fusionx.central.cash.management.service.OpenCloseTillVaultService;
//
@RestController
@RequestMapping("/OpenCloseTillVault")
@CrossOrigin("*")
public class OpenCloseTillVaultController extends MessagePropertyBase {
	
	@Autowired
	OpenCloseTillVaultService openCloseTillVaultService;
	
	@GetMapping("/{tenantId}/all")
	public ResponseEntity<Object> getAllTillOrValut(@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "branchId", required = true) Long branchId,
			 @PathVariable(value = "status", required = true) String status, @PathVariable(value = "type", required = true) String type){
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		if(type.equalsIgnoreCase("Till")) {
			List<TillResponse> tillResponse=openCloseTillVaultService.getAllTills(tenantId, type, branchId,status);
			if(!tillResponse.isEmpty()) {
				return new ResponseEntity<>(tillResponse,HttpStatus.OK);
			}else {
				responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
				return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
			}			
		}
		else {
			List<VaultsResponse> vaultsResponse=openCloseTillVaultService.getAllVaults(tenantId, type, branchId,status);
			if(!vaultsResponse.isEmpty()) {
				return new ResponseEntity<>(vaultsResponse,HttpStatus.OK);
			}else {
				responseMessage.setMessages(getEnvironment().getProperty(RECORD_NOT_FOUND));
				return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);	
			}
		}
	}
	
	   @PostMapping(value = "/{tenantId}/open/{operation-type}")
	    public ResponseEntity<Object> openTillOrVault(@PathVariable(value = "tenantId", required = true) String tenantId,
	                                                        @Valid @RequestBody OpenTillVaultRequestResource openTillVaultRequestResource) {

	        if (LogginAuthentcation.getUserName()==null || LogginAuthentcation.getUserName().isEmpty() )
	            throw new ValidateRecordException(environment.getProperty(COMMON_NOT_NULL), "username");
	        SuccessAndErrorDetailsResource successAndErrorDetailsResource = new SuccessAndErrorDetailsResource();
	      Boolean isResponse=  openCloseTillVaultService.openTillOrVault(openTillVaultRequestResource, tenantId, LogginAuthentcation.getUserName());
		if(isResponse!=false) {
			successAndErrorDetailsResource.setMessages(environment.getProperty("common.saved"));
			return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.CREATED);
		}else {
			return new ResponseEntity<>(successAndErrorDetailsResource, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	   }
}
