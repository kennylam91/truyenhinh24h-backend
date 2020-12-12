package com.truyenhinh24h.controller;

import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.truyenhinh24h.common.Utils;
import com.truyenhinh24h.model.AccessLog;
import com.truyenhinh24h.model.Channel;
import com.truyenhinh24h.model.ChannelDto;
import com.truyenhinh24h.service.AccessLogService;
import com.truyenhinh24h.service.ChannelService;
import com.truyenhinh24h.utils.Mapper;

@RestController
@RequestMapping("/rest/v1/channels")
public class ChannelController {

	private static final Logger logger = LogManager.getLogger(ChannelController.class);

	@Autowired
	private ChannelService channelService;
	@Autowired
	private Mapper mapper;
	@Autowired
	private AccessLogService logService;

	@PostMapping
	public ResponseEntity<ChannelDto> createOrUpdate(@Valid @RequestBody ChannelForm channelForm) {
		logger.info("Create channel");
		Channel channel = mapper.fromTo(channelForm, Channel.class);
		ChannelDto channelDto = channelService.createOrUpdate(channel);
		logger.info("Channel created: {}", channelDto);
		return ResponseEntity.ok(channelDto);
	}

	@PostMapping(path = "/delete-multi")
	public ResponseEntity<Void> deleteMulti(@RequestBody ChannelForm channelForm) {
		channelService.deleteMulti(channelForm.getChannelIds());
		logger.info("Deleted Channel: {}", channelForm.getChannelIds().toString());
		return ResponseEntity.ok().build();
	}

	@PostMapping(path = "/get-all")
	public ResponseEntity<List<ChannelDto>> getAll() {
		List<ChannelDto> result = channelService.getAll();
		return ResponseEntity.ok(result);
	}

	@GetMapping(path = "/{channelId}")
	public ResponseEntity<ChannelDto> getDetail(@PathVariable Long channelId, HttpServletRequest request) {
//		AccessLog log = new AccessLog();
//		log.setCreatedAt(new Date());
//		log.setEndPoint("/channels/" + channelId);
//		log.setIp(Utils.getClientIpAddress(request));
//		log.setMethod(HttpMethod.GET);
//		logService.createOrUpdate(log);
		ChannelDto channelDto = channelService.findById(channelId);
		return ResponseEntity.ok(channelDto);
	}
}
