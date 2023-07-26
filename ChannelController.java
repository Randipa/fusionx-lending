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
import com.fusionx.central.cash.management.domain.Channel;
import com.fusionx.central.cash.management.exception.PageableLengthException;
import com.fusionx.central.cash.management.exception.UserNotFound;
import com.fusionx.central.cash.management.resource.ChannelAddResource;
import com.fusionx.central.cash.management.resource.ChannelUpdateResource;
import com.fusionx.central.cash.management.resource.SuccessAndErrorDetailsResource;
import com.fusionx.central.cash.management.service.ChannelService;

/**
 * Channel Controller
 * 
 ********************************************************************************************************
 *  ###   Date         Story Point   Task No    Author       Description
 *-------------------------------------------------------------------------------------------------------
 *    1   06-01-2021      		     FX-5334	MiyuruW      Created
 *    
 ********************************************************************************************************
 */

@RestController
@RequestMapping(value = "/channel")
@CrossOrigin(origins = "*")
public class ChannelController {

	@Autowired
	private Environment environment;
	
	@Autowired
	private ChannelService channelService;
	
	private String userNotFound = "common.user-not-found";
	private String commonSaved = "common.saved";
	private String commonUpdated = "common.updated";
	private String pageableLength = "common.pageable-length";
	private String recordNotFound = "common.record-not-found";
	
	/**
	 * Gets the all channels.
	 *
	 * @param tenantId - the tenant id
	 * @param pageable - the pageable
	 * @return the all channels
	 */
	@GetMapping(value = "/{tenantId}/all")
	public Page<Channel> getAllChannels(@PathVariable(value = "tenantId", required = true) String tenantId,
			@PageableDefault(size = 10) Pageable pageable) {
		if (pageable.getPageSize() > Constants.MAX_PAGEABLE_LENGTH)
			throw new PageableLengthException(environment.getProperty(pageableLength));
		return channelService.findAll(pageable);
	}
	
	/**
	 * Gets the channel by id.
	 *
	 * @param tenantId - the tenant id
	 * @param id - the id
	 * @return the channel by id
	 */
	@GetMapping(value = "/{tenantId}/{id}")
	public ResponseEntity<Object> getChannelById(
			@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "id", required = true) Long id) {
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		Optional<Channel> isPresentChannel = channelService.findById(id);
		if (isPresentChannel.isPresent()) {
			return new ResponseEntity<>(isPresentChannel, HttpStatus.OK);
		} else {
			responseMessage.setMessages(environment.getProperty(recordNotFound));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * Gets the channels by status.
	 *
	 * @param tenantId - the tenant id
	 * @param status - the status
	 * @return the channels by status
	 */
	@GetMapping(value = "/{tenantId}/status/{status}")
	public ResponseEntity<Object> getChannelsByStatus(
			@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "status", required = true) String status) {
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		List<Channel> channel = channelService.findByStatus(status);
		if (!channel.isEmpty()) {
			return new ResponseEntity<>((Collection<Channel>) channel, HttpStatus.OK);
		} else {
			responseMessage.setMessages(environment.getProperty(recordNotFound));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * Gets the channels by name.
	 *
	 * @param tenantId - the tenant id
	 * @param name - the name
	 * @return the channels by name
	 */
	@GetMapping(value = "/{tenantId}/name/{name}")
	public ResponseEntity<Object> getChannelsByName(
			@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "name", required = true) String name) {
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		List<Channel> channel = channelService.findByName(name);
		if (!channel.isEmpty()) {
			return new ResponseEntity<>((Collection<Channel>) channel, HttpStatus.OK);
		} else {
			responseMessage.setMessages(environment.getProperty(recordNotFound));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * Gets the channels by code.
	 *
	 * @param tenantId - the tenant id
	 * @param code - the code
	 * @return the channels by code
	 */
	@GetMapping(value = "/{tenantId}/code/{code}")
	public ResponseEntity<Object> getChannelsByCode(
			@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "code", required = true) String code) {
		SuccessAndErrorDetailsResource responseMessage = new SuccessAndErrorDetailsResource();
		Optional<Channel> isPresentChannel = channelService.findByCode(code);
		if (isPresentChannel.isPresent()) {
			return new ResponseEntity<>(isPresentChannel, HttpStatus.OK);
		} else {
			responseMessage.setMessages(environment.getProperty(recordNotFound));
			return new ResponseEntity<>(responseMessage, HttpStatus.NO_CONTENT);
		}
	}
	
	/**
	 * Adds the channel.
	 *
	 * @param tenantId - the tenant id
	 * @param channelAddResource - the channel add resource
	 * @return the response entity
	 */
	@PostMapping(value = "/{tenantId}")
	public ResponseEntity<Object> addChannel(@PathVariable(value = "tenantId", required = true) String tenantId,
			@Valid @RequestBody ChannelAddResource channelAddResource) {
		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new UserNotFound(environment.getProperty(userNotFound));
		Long id = channelService.saveAndValidateChannel(tenantId, LogginAuthentcation.getUserName(), channelAddResource);
		SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(commonSaved), id.toString());
		return new ResponseEntity<>(successDetailsDto,HttpStatus.CREATED);
	}
	
	/**
	 * Update channel.
	 *
	 * @param tenantId - the tenant id
	 * @param id - the id
	 * @param channelUpdateResource - the channel update resource
	 * @return the response entity
	 */
	@PutMapping(value = "/{tenantId}/{id}")
	public ResponseEntity<Object> updateChannel(@PathVariable(value = "tenantId", required = true) String tenantId,
			@PathVariable(value = "id", required = true) Long id,
			@Valid @RequestBody ChannelUpdateResource channelUpdateResource) {
		if (LogginAuthentcation.getUserName() == null || LogginAuthentcation.getUserName().isEmpty())
			throw new UserNotFound(environment.getProperty(userNotFound));
		if (channelService.existsById(id)) {
			channelService.updateAndValidateChannel(tenantId, LogginAuthentcation.getUserName(), id, channelUpdateResource);
			SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(commonUpdated));
			return new ResponseEntity<>(successDetailsDto, HttpStatus.OK);
		} else {
			SuccessAndErrorDetailsResource successDetailsDto = new SuccessAndErrorDetailsResource(environment.getProperty(recordNotFound));
			return new ResponseEntity<>(successDetailsDto, HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
	
	/**
	 * Search channel.
	 *
	 * @param tenantId - the tenant id
	 * @param searchq - the searchq
	 * @param name - the name
	 * @param code - the code
	 * @param type - the type
	 * @param pageable - the pageable
	 * @return the page
	 */
	@GetMapping(value = "/{tenantId}/search")
	public Page<Channel> searchChannel(@PathVariable(value = "tenantId", required = true) String tenantId,
			@RequestParam(value = "searchq", required = false) String searchq,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "code", required = false) String code,
			@RequestParam(value = "type", required = false) String type,
			@PageableDefault(size = 10) Pageable pageable) {
		if(pageable.getPageSize()>Constants.MAX_PAGEABLE_LENGTH) 
			throw new PageableLengthException(environment.getProperty(pageableLength));
		return channelService.searchChannel(searchq, name, code, type, pageable);
	}
}