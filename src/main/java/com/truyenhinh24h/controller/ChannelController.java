package com.truyenhinh24h.controller;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.truyenhinh24h.model.Channel;
import com.truyenhinh24h.model.ChannelDto;
import com.truyenhinh24h.service.ChannelService;

@RestController
@RequestMapping("/rest/v1/channels")
public class ChannelController {

	private static final Logger logger = LogManager.getLogger(ChannelController.class);

	@Autowired
	private ChannelService channelService;

	@PostMapping
	public ResponseEntity<ChannelDto> createOrUpdate(@Valid @RequestBody ChannelForm channelForm) {
		logger.info("Create channel");
		Channel channel = mapper(channelForm);
		try {
			ChannelDto channelDto = channelService.createOrUpdate(channel);
			logger.info("Channel created: {}", channelDto);
			return ResponseEntity.ok(channelDto);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping(path = "/delete-multi")
	public ResponseEntity<Void> deleteMulti(@RequestBody ChannelForm channelForm) {
		try {
			channelService.deleteMulti(channelForm.getChannelIds());
			logger.info("Deleted Channel: {}", channelForm.getChannelIds().toString());
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping(path = "/get-all")
	public ResponseEntity<Page<ChannelDto>> getAll(@RequestBody ChannelForm channelForm) {
		try {
			Sort sort = Sort.by(Sort.Direction.ASC, "name");
			Pageable pageable = PageRequest.of(channelForm.getPage() - 1, channelForm.getLimit(), sort);
			Page<ChannelDto> result = channelService.getAll(pageable);
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GetMapping(path = "/{channelId}")
	public ResponseEntity<ChannelDto> getDetail(@PathVariable Long channelId){
		try {
			ChannelDto channelDto = channelService.findById(channelId);
			return ResponseEntity.ok(channelDto);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	private Channel mapper(ChannelForm data) {
		if (data == null) {
			return null;
		}
		Channel channel = new Channel();
		channel.setId(data.getId());
		channel.setName(data.getName());
		channel.setDescription(data.getDescription());
		channel.setLogoUrl(data.getLogoUrl());
		channel.setNetworkId(data.getNetworkId());
		channel.setVip(data.isVip());
		return channel;
	}
}
