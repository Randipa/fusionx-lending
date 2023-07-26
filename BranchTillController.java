package com.fusionx.central.cash.management.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.fusionx.central.cash.management.resource.KeyHolderResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import com.fusionx.central.cash.management.base.MessagePropertyBase;
import com.fusionx.central.cash.management.core.LogginAuthentcation;
import com.fusionx.central.cash.management.domain.BranchTill;
import com.fusionx.central.cash.management.exception.PageableLengthException;
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.resource.BranchTillResource;
import com.fusionx.central.cash.management.resource.BranchTillUpdateResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.BranchTillService;

/**
 * Branch Till Service
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
@RequestMapping(value = "/branch-till")
@CrossOrigin(origins = "*")
public class BranchTillController extends MessagePropertyBase{
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private BranchTillService branchTillService;
	
	
	private String userNotFound = "common.user-not-found";
	private String commonSaved = "common.saved";
	private String commonUpdated = "common.updated";
	
	private String recordNotFound = "common.record-not-found";
	
	private String pageableLength = "common.pageable-length";
	
	/**
	 * Gets the all BranchTill.
	 *
	 * @param tenantId - the tenant id
	 * @param pageable - the pageable
	 * @return the all BranchTill
	 */
	@GetMapping(value = "/{tenantId}/all")
	public List<BranchTill> getAllBranchTill(@PathVariable(value = "tenantId", required = true) String tenantId,
			@PageableDefault(size = 10) Pageable pageable) {
		if (pageable.getPageSize() > Constants.MAX_PAGEABLE_LENGTH)
			throw new PageableLengthException(environment.getProperty(pageableLength));
		return branchTillService.findAll();
	}
	
	/**
	 * Gets the BranchTill by id.
	 *
	 * @param tenantId - the tenant id
	 * @param id - the id
	 * @return the t=BranchTill by id
	 */
	@GetMapping(value = "/{tenantId}/{id}")
	public ResponseEntity<Object> getBranchTillById(
			@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "id", required = true) Long id) {
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		Optional<BranchTill> isPresentBranchTill = branchTillService.findById(id);
		if (isPresentBranchTill.isPresent()) {
			return new ResponseEntity<>(isPresentBranchTill, HttpStatus.OK);
		} else {
			responseMessage.setMessages(environment.getProperty(recordNotFound));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * Gets the BranchTill by code.
	 *
	 * @param tenantId - the tenant id
	 * @param code - the code
	 * @return the BranchTill by code
	 */
	@GetMapping(value = "/{tenantId}/code/{code}")
	public ResponseEntity<Object> getBranchTillByCode(
			@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "code", required = true) String code) {
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		Optional<BranchTill> isPresentBranchTill = branchTillService.findByCode(code);
		if (isPresentBranchTill.isPresent()) {
			return new ResponseEntity<>(isPresentBranchTill, HttpStatus.OK);
		} else {
			responseMessage.setMessages(environment.getProperty(recordNotFound));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * Gets the BranchTill by status.
	 *
	 * @param tenantId - the tenant id
	 * @param status - the status
	 * @return the BranchTill by status
	 */
	@GetMapping(value = "/{tenantId}/status/{status}")
	public ResponseEntity<Object> getBranchTillByStatus(
			@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "status", required = true) String status) {
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		List<BranchTill> branchTill = branchTillService.findByStatus(status);
		if (!branchTill.isEmpty()) {
			return new ResponseEntity<>((Collection<BranchTill>) branchTill, HttpStatus.OK);
		} else {
			responseMessage.setMessages(environment.getProperty(recordNotFound));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * Gets the BranchTill by name.
	 *
	 * @param tenantId - the tenant id
	 * @param name - the name
	 * @return the BranchTill by name
	 */
	@GetMapping(value = "/{tenantId}/name/{name}")
	public ResponseEntity<Object> getBranchTillByName(
			@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "name", required = true) String name) {
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		List<BranchTill> branchTill = branchTillService.findByName(name);
		if (!branchTill.isEmpty()) {
			return new ResponseEntity<>((Collection<BranchTill>) branchTill, HttpStatus.OK);
		} else {
			responseMessage.setMessages(environment.getProperty(recordNotFound));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * Add BranchTill.
	 *
	 * @param tenantId - the tenant id
	 * @param branchTillResource - the BranchTill  resource
	 * @return the response entity
	 */
	@PostMapping(value = "/{tenantId}")
	public ResponseEntity<Object> addBranchTill(@PathVariable(value = "tenantId", required = true) String tenantId,
			@Valid @RequestBody BranchTillResource branchTillResource) {
		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new UserNotFound(environment.getProperty(userNotFound));
		Long id = branchTillService.addBranchTill(tenantId, LogginAuthentcation.getUserName(), branchTillResource);
		SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(commonSaved), id.toString());
		return new ResponseEntity<>(successDetailsDto,HttpStatus.CREATED);
	}
	
	/**
	 * Update BranchTill.
	 *
	 * @param tenantId - the tenant id
	 * @param id - the id
	 * @param branchTillUpdateResource - the BranchTill update resource
	 * @return the response entity
	 */
	@PutMapping(value = "/{tenantId}/{id}")
	public ResponseEntity<Object> updateBranchTill(@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "id", required = true) Long id,
			@Valid @RequestBody BranchTillUpdateResource branchTillUpdateResource) {
		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new UserNotFound(environment.getProperty(userNotFound));
		if (branchTillService.existsById(id)) {
			branchTillService.updateBranchTill(tenantId, LogginAuthentcation.getUserName(), id, branchTillUpdateResource);
			SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(commonUpdated));
			return new ResponseEntity<>(successDetailsDto, HttpStatus.OK);
		} else {
			SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(recordNotFound));
			return new ResponseEntity<>(successDetailsDto, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@PutMapping(value = "/{tenantId}/key-holder/{id}")
	public ResponseEntity<Object> updateKeyHolder(@PathVariable(value = "tenantId", required = true) String tenantId,
												   @PathVariable(value = "id", required = true) Long id,
												   @Valid @RequestBody KeyHolderResource keyHolderResource) {
		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new UserNotFound(environment.getProperty(userNotFound));
		if (branchTillService.existsByKeyHolderId(id)) {
			branchTillService.updateKeyHolder(tenantId, LogginAuthentcation.getUserName(), id, keyHolderResource);
			SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(commonUpdated));
			return new ResponseEntity<>(successDetailsDto, HttpStatus.OK);
		} else {
			SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(recordNotFound));
			return new ResponseEntity<>(successDetailsDto, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

}
