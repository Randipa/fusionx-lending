package com.fusionx.central.cash.management.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fusionx.central.cash.management.Constants;
import com.fusionx.central.cash.management.core.LogginAuthentcation;
import com.fusionx.central.cash.management.domain.SubChannel;
import com.fusionx.central.cash.management.exception.PageableLengthException;
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.resource.SubChannelAddResource;
import com.fusionx.central.cash.management.resource.SubChannelUpdateResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.SubChannelService;


/**
 * Sub Channel Controller
 * 
 ********************************************************************************************************
 *  ###   Date         Story Point   Task No    Author       Description
 *-------------------------------------------------------------------------------------------------------
 *    1   07-01-2021      		     FX-5335	MiyuruW      Created
 *    
 ********************************************************************************************************
 */

@RestController
@RequestMapping(value = "/sub-channel")
@CrossOrigin(origins = "*")
public class SubChannelController {

	@Autowired
	private Environment environment;
	
	@Autowired
	private SubChannelService subChannelService;
	
	private String userNotFound = "common.user-not-found";
	private String commonSaved = "common.saved";
	private String commonUpdated = "common.updated";
	private String pageableLength = "common.pageable-length";
	private String recordNotFound = "common.record-not-found";
	
	
	/**
	 * Gets the all sub channels.
	 *
	 * @param tenantId - the tenant id
	 * @param pageable - the pageable
	 * @return the all sub channels
	 */
	@GetMapping(value = "/{tenantId}/all")
	public Page<SubChannel> getAllSubChannels(@PathVariable(value = "tenantId", required = true) String tenantId,
			@PageableDefault(size = 10) Pageable pageable) {
		if (pageable.getPageSize() > Constants.MAX_PAGEABLE_LENGTH)
			throw new PageableLengthException(environment.getProperty(pageableLength));
		return subChannelService.findAll(pageable);
	}
	
	/**
	 * Gets the sub channel by id.
	 *
	 * @param tenantId - the tenant id
	 * @param id - the id
	 * @return the sub channel by id
	 */
	@GetMapping(value = "/{tenantId}/{id}")
	public ResponseEntity<Object> getSubChannelById(
			@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "id", required = true) Long id) {
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		Optional<SubChannel> isPresentSubChannel = subChannelService.findById(id);
		if (isPresentSubChannel.isPresent()) {
			return new ResponseEntity<>(isPresentSubChannel, HttpStatus.OK);
		} else {
			responseMessage.setMessages(environment.getProperty(recordNotFound));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * Gets the sub channels by status.
	 *
	 * @param tenantId - the tenant id
	 * @param status - the status
	 * @return the sub channels by status
	 */
	@GetMapping(value = "/{tenantId}/status/{status}")
	public ResponseEntity<Object> getSubChannelsByStatus(
			@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "status", required = true) String status) {
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		List<SubChannel> subchannel = subChannelService.findByStatus(status);
		if (!subchannel.isEmpty()) {
			return new ResponseEntity<>((Collection<SubChannel>) subchannel, HttpStatus.OK);
		} else {
			responseMessage.setMessages(environment.getProperty(recordNotFound));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * Gets the sub channels by channel name.
	 *
	 * @param tenantId - the tenant id
	 * @param channelName - the channel name
	 * @return the sub channels by channel name
	 */
	@GetMapping(value = "/{tenantId}/channelName/{channelName}")
	public ResponseEntity<Object> getSubChannelsByChannelName(
			@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "channelName", required = true) String channelName) {
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		List<SubChannel> subChannel = subChannelService.findByChannelName(channelName);
		if (!subChannel.isEmpty()) {
			return new ResponseEntity<>((Collection<SubChannel>) subChannel, HttpStatus.OK);
		} else {
			responseMessage.setMessages(environment.getProperty(recordNotFound));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * Gets the sub channels by name.
	 *
	 * @param tenantId - the tenant id
	 * @param name - the name
	 * @return the sub channels by name
	 */
	@GetMapping(value = "/{tenantId}/name/{name}")
	public ResponseEntity<Object> getSubChannelsByName(
			@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "name", required = true) String name) {
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		List<SubChannel> subchannel = subChannelService.findByName(name);
		if (!subchannel.isEmpty()) {
			return new ResponseEntity<>((Collection<SubChannel>) subchannel, HttpStatus.OK);
		} else {
			responseMessage.setMessages(environment.getProperty(recordNotFound));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * Gets the sub channels by code.
	 *
	 * @param tenantId - the tenant id
	 * @param code - the code
	 * @return the sub channels by code
	 */
	@GetMapping(value = "/{tenantId}/code/{code}")
	public ResponseEntity<Object> getSubChannelsByCode(
			@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "code", required = true) String code) {
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		List<SubChannel> subchannel = subChannelService.findByCode(code);
		if (!subchannel.isEmpty()) {
			return new ResponseEntity<>((Collection<SubChannel>) subchannel, HttpStatus.OK);
		} else {
			responseMessage.setMessages(environment.getProperty(recordNotFound));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * Adds the sub channel.
	 *
	 * @param tenantId - the tenant id
	 * @param subChannelAddResource - the sub channel add resource
	 * @return the response entity
	 */
	@PostMapping(value = "/{tenantId}")
	public ResponseEntity<Object> addSubChannel(@PathVariable(value = "tenantId", required = true) String tenantId,
			@Valid @RequestBody SubChannelAddResource subChannelAddResource) {
		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new UserNotFound(environment.getProperty(userNotFound));
		Long id = subChannelService.saveAndValidateSubChannel(tenantId, LogginAuthentcation.getUserName(), subChannelAddResource);
		SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(commonSaved), id.toString());
		return new ResponseEntity<>(successDetailsDto,HttpStatus.CREATED);
	}
	
	/**
	 * Update sub channel.
	 *
	 * @param tenantId - the tenant id
	 * @param id - the id
	 * @param subChannelUpdateResource - the sub channel update resource
	 * @return the response entity
	 */
	@PutMapping(value = "/{tenantId}/{id}")
	public ResponseEntity<Object> updateSubChannel(@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "id", required = true) Long id,
			@Valid @RequestBody SubChannelUpdateResource subChannelUpdateResource) {
		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new UserNotFound(environment.getProperty(userNotFound));
		if (subChannelService.existsById(id)) {
			subChannelService.updateAndValidateSubChannel(tenantId, LogginAuthentcation.getUserName(), id, subChannelUpdateResource);
			SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(commonUpdated));
			return new ResponseEntity<>(successDetailsDto, HttpStatus.OK);
		} else {
			SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(recordNotFound));
			return new ResponseEntity<>(successDetailsDto, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
	/**
	 * Search sub channel.
	 *
	 * @param tenantId - the tenant id
	 * @param searchq - the searchq
	 * @param name - the name
	 * @param code - the code
	 * @param pageable - the pageable
	 * @return the page
	 */
	@GetMapping(value = "/{tenantId}/search")
	public Page<SubChannel> searchSubChannel(@PathVariable(value = "tenantId", required = true) String tenantId,
			@RequestParam(value = "searchq", required = false) String searchq,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "code", required = false) String code,
			@PageableDefault(size = 10) Pageable pageable) {
		if(pageable.getPageSize()>Constants.MAX_PAGEABLE_LENGTH) 
			throw new PageableLengthException(environment.getProperty(pageableLength));
		return subChannelService.searchSubChannel(searchq, name, code, pageable);
	}
}