package com.fusionx.central.cash.management.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.fusionx.central.cash.management.service.TellerTransactionLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fusionx.central.cash.management.Constants;
import com.fusionx.central.cash.management.core.LogginAuthentcation;
import com.fusionx.central.cash.management.domain.Teller;
import com.fusionx.central.cash.management.exception.PageableLengthException;
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.resource.TellerResource;
import com.fusionx.central.cash.management.resource.TellerUpdateResource;
import com.fusionx.central.cash.management.service.TellerService;

/**
 * Teller Service
 * @author Sanatha
 * @Date 04-JAN-2021
 *
 *********************************************************************************************************************
 *  ###        Date                  Story Point         Task No              Author           Description
 *-------------------------------------------------------------------------------------------------------
 *    1        04-JAN-2021   		 FX-2437             FX-5253              Sanatha          Initial Development.
 *
 *********************************************************************************************************************
 */

@RestController
@RequestMapping(value = "/teller")
@CrossOrigin(origins = "*")
public class TellerController {

	@Autowired
	private Environment environment;

	@Autowired
	private TellerService tellerService;

	@Autowired
	private TellerTransactionLimitService tellerTransactionLimitService;

	private String userNotFound = "common.user-not-found";
	private String commonSaved = "common.saved";
	private String commonUpdated = "common.updated";
	private String pageableLength = "common.pageable-length";
	private String recordNotFound = "common.record-not-found";

	/**
	 * Gets the all Teller.
	 *
	 * @param tenantId - the tenant id
	 * @param pageable - the pageable
	 * @return the all Teller
	 */
	@GetMapping(value = "/{tenantId}/all")
	public Page<Teller> getAllTeller(@PathVariable(value = "tenantId", required = true) String tenantId,
									 @PageableDefault(size = 10) Pageable pageable) {
		if (pageable.getPageSize() > Constants.MAX_PAGEABLE_LENGTH)
			throw new PageableLengthException(environment.getProperty(pageableLength));
		return tellerService.findAll(pageable);
	}

	/**
	 * Gets the Teller by id.
	 *
	 * @param tenantId - the tenant id
	 * @param id - the id
	 * @return the t=Teller by id
	 */
	@GetMapping(value = "/{tenantId}/{id}")
	public ResponseEntity<Object> getTellerById(
			@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "id", required = true) Long id) {
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		Optional<Teller> isPresentTeller = tellerService.findById(id);
		if (isPresentTeller.isPresent()) {
			return new ResponseEntity<>(isPresentTeller, HttpStatus.OK);
		} else {
			responseMessage.setMessages(environment.getProperty(recordNotFound));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * Gets the Teller by code.
	 *
	 * @param tenantId - the tenant id
	 * @param code - the code
	 * @return the Teller by code
	 */
	@GetMapping(value = "/{tenantId}/code/{code}")
	public ResponseEntity<Object> getTellerByCode(
			@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "code", required = true) String code) {
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		Optional<Teller> isPresentTeller = tellerService.findByCode(code);
		if (isPresentTeller.isPresent()) {
			return new ResponseEntity<>(isPresentTeller, HttpStatus.OK);
		} else {
			responseMessage.setMessages(environment.getProperty(recordNotFound));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * Gets the Teller by status.
	 *
	 * @param tenantId - the tenant id
	 * @param status - the status
	 * @return the Teller by status
	 */
	@GetMapping(value = "/{tenantId}/status/{status}")
	public ResponseEntity<Object> getChannelsByStatus(
			@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "status", required = true) String status) {
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		List<Teller> teller = tellerService.findByStatus(status);
		if (!teller.isEmpty()) {
			return new ResponseEntity<>((Collection<Teller>) teller, HttpStatus.OK);
		} else {
			responseMessage.setMessages(environment.getProperty(recordNotFound));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}

	/**
	 * Add Teller.
	 *
	 * @param tenantId - the tenant id
	 * @param tellerResource - the Teller  resource
	 * @return the response entity
	 */
	@PostMapping(value = "/{tenantId}")
	public ResponseEntity<Object> addTeller(@PathVariable(value = "tenantId", required = true) String tenantId,
											@Valid @RequestBody TellerResource tellerResource) {
		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new UserNotFound(environment.getProperty(userNotFound));
		Long id = tellerService.addTeller(tenantId, LogginAuthentcation.getUserName(), tellerResource);
		SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(commonSaved), id.toString());
		return new ResponseEntity<>(successDetailsDto,HttpStatus.CREATED);
	}

	/**
	 * Update Teller.
	 *
	 * @param tenantId - the tenant id
	 * @param id - the id
	 * @param tellerUpdateResource - the Teller update resource
	 * @return the response entity
	 */
	@PutMapping(value = "/{tenantId}/{id}")
	public ResponseEntity<Object> updateTeller(@PathVariable(value = "tenantId", required = true) String tenantId,
											   @PathVariable(value = "id", required = true) Long id,
											   @Valid @RequestBody TellerUpdateResource tellerUpdateResource) {
		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new UserNotFound(environment.getProperty(userNotFound));
		if (tellerService.existsById(id)) {
			tellerService.updateTeller(tenantId, LogginAuthentcation.getUserName(), id, tellerUpdateResource);
			SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(commonUpdated));
			return new ResponseEntity<>(successDetailsDto, HttpStatus.OK);
		} else {
			SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(recordNotFound));
			return new ResponseEntity<>(successDetailsDto, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	/**
	 * Validate Teller transaction limit by designation.
	 *
	 * @param tenantId - the tenant id
	 * @param currencyId - the currency id
	 * @param tellerEvent - the transaction event
	 * @param module - the module
	 * @return the approval YES or NO
	 * @apiNote This API will inform the teller that they can perform the transaction for the current date using their designation.
	 * @apiNote This API will process every transaction made by any teller in any module. Unnecessary validations will have an effect on performance.
	 */
	@GetMapping(value = "/{tenantId}/currency/{currencyId}/teller-event/{tellerEvent}/module/{module}")
	public ResponseEntity<Object> getTellerTransactionApprovalByDesignation(
			@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "currencyId", required = true) String currencyId,
			@PathVariable(value = "tellerEvent", required = true) String tellerEvent,
			@PathVariable(value = "module", required = true) String module) {
		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new UserNotFound(environment.getProperty(userNotFound));
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		String approval = tellerTransactionLimitService.validateByUserDesignation(tenantId, LogginAuthentcation.getUserName(), currencyId, tellerEvent, module);
		responseMessage.setMessages(approval);
		return new ResponseEntity<>(responseMessage, HttpStatus.OK);
	}

}
