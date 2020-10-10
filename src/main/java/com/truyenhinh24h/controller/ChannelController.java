package com.truyenhinh24h.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.truyenhinh24h.common.Utils;
import com.truyenhinh24h.dao.StatsData;
import com.truyenhinh24h.model.AccessLog;
import com.truyenhinh24h.model.Channel;
import com.truyenhinh24h.model.ChannelDto;
import com.truyenhinh24h.service.AccessLogService;
import com.truyenhinh24h.service.ChannelService;

@RestController
@RequestMapping("/rest/v1/channels")
public class ChannelController {

	private static final String CHANNEL_LIST_KEY = "channelList";


	private static final Logger logger = LogManager.getLogger(ChannelController.class);
	

	@Autowired
	private ChannelService channelService;

	@Autowired
	private AccessLogService logService;

	@PostMapping
	public ResponseEntity<ChannelDto> createOrUpdate(@Valid @RequestBody ChannelForm channelForm) {
		logger.info("Create channel");
		Channel channel = mapper(channelForm);
		try {
			ChannelDto channelDto = channelService.createOrUpdate(channel);
			Utils.CACHE_MAP.remove(CHANNEL_LIST_KEY);
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
			Utils.CACHE_MAP.remove(CHANNEL_LIST_KEY);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping(path = "/get-all")
	public ResponseEntity<List<ChannelDto>> getAll(@RequestBody ChannelForm channelForm, HttpServletRequest request) {
		try {
			AccessLog log = new AccessLog();
			log.setCreatedAt(new Date());
			log.setEndPoint("/channels/get-all");
			log.setIp(Utils.getClientIpAddress(request));
			log.setMethod(HttpMethod.POST);
			logService.createOrUpdate(log);
			
			if(Utils.CACHE_MAP.get(CHANNEL_LIST_KEY) == null) {
				List<ChannelDto> result = channelService.getAll();
				Utils.CACHE_MAP.put(CHANNEL_LIST_KEY, result);
				return ResponseEntity.ok(result);
			} else {
				return ResponseEntity.ok((List<ChannelDto>)Utils.CACHE_MAP.get(CHANNEL_LIST_KEY));
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping(path = "/{channelId}")
	public ResponseEntity<ChannelDto> getDetail(@PathVariable Long channelId, HttpServletRequest request) {
		try {
			AccessLog log = new AccessLog();
			log.setCreatedAt(new Date());
			log.setEndPoint("/channels/" + channelId);
			log.setIp(Utils.getClientIpAddress(request));
			log.setMethod(HttpMethod.GET);
			logService.createOrUpdate(log);
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
		channel.setLogo(data.getLogo());
		channel.setNetworkId(data.getNetworkId());
		channel.setVip(data.isVip());
		return channel;
	}

}
