package com.truyenhinh24h.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.truyenhinh24h.model.ChannelDto;
import com.truyenhinh24h.service.ChannelService;

@RestController
@RequestMapping("/rest/v1/channels")
public class ChannelController {

	private static final Logger logger = LogManager.getLogger(ChannelController.class);

	@Autowired
	private ChannelService channelService;

	@PostMapping
	public ResponseEntity<ChannelDto> create(@RequestBody ChannelForm channelForm) {
		ChannelDto channelDto = mapper(channelForm);
		try {
			ChannelDto createdChannel = channelService.create(channelDto);
			return ResponseEntity.ok(createdChannel);
		} catch (Exception e) {
			return ResponseEntity.status(500).body(null);
		}
	}
	
//	@PostMapping(path = "/{delete-multi}")
//	public ResponseEntity<Void> deleteMulti(@RequestBody ChannelForm channelForm){
//		
//	}

	private ChannelDto mapper(ChannelForm channel) {
		if (channel == null) {
			return null;
		}
		ChannelDto channelDto = new ChannelDto();
		channelDto.setChannelId(channel.getChannelId());
		channelDto.setName(channel.getName());
		channelDto.setCategories(channel.getCategories());
		channelDto.setDescription(channel.getDescription());
		channelDto.setLogoUrl(channel.getLogoUrl());
		channelDto.setNetworkId(channel.getNetworkId());
		channelDto.setVip(channel.isVip());
		return channelDto;
	}
}
