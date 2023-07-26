package com.fusionx.central.cash.management.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.fusionx.central.cash.management.base.MessagePropertyBase;
import com.fusionx.central.cash.management.core.LoggerRequest;
import com.fusionx.central.cash.management.domain.Transaction;
import com.fusionx.central.cash.management.enums.CreditDebitIndicator;
import com.fusionx.central.cash.management.repo.TransactionRepository;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.resource.VaultDayEndResource;
import com.fusionx.central.cash.management.service.VaultService;

@RestController
@RequestMapping("/vault-day-end")
@CrossOrigin("*")
public class VaultDayEndController extends MessagePropertyBase{
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private VaultService vaultService;
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	private String recordNotFound = "common.record-not-found";

	@GetMapping(value = "/{tenantId}/{vaultId}")
	public ResponseEntity<Object> getVaultDayEndList(@PathVariable(value = "tenantId", required = true) String tenantId,@PathVariable(value = "vaultId", required = true) String vaultId) {
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		VaultDayEndResource dayEnd = vaultService.findVaultAtDayEnd(Long.parseLong(vaultId));
		if (dayEnd!=null) {
			return new ResponseEntity<>(dayEnd, HttpStatus.OK);
		} else {
			responseMessage.setMessages(environment.getProperty(recordNotFound));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping(value = "/{tenantId}/details/{vaultId}/{transactionType}/{entry}")
	public ResponseEntity<Object> getVaultDetalisByTransactionType(@PathVariable(value = "tenantId", required = true) String tenantId,@PathVariable(value = "vaultId", required = true) String vaultId,
			@PathVariable(value = "transactionType", required = true) String transactionType,@PathVariable(value = "entry", required = true) String entry) {
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date cuurentDate = null;
		try {
			cuurentDate = formatter.parse(formatter.format(new Date()));
		} catch (ParseException e) {
			LoggerRequest.getInstance().logInfo(e.toString());
		}
		
		List<Transaction> transactionList = transactionRepository.getVaultDetalisByTransactionType(Long.parseLong(vaultId),transactionType,CreditDebitIndicator.valueOf(entry),cuurentDate);
		if (transactionList!=null) {
			return new ResponseEntity<>(transactionList, HttpStatus.OK);
		} else {
			responseMessage.setMessages(environment.getProperty(recordNotFound));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	

}
