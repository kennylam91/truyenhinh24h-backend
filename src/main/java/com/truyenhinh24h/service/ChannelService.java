package com.truyenhinh24h.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.truyenhinh24h.dao.ChannelRepository;
import com.truyenhinh24h.model.ChannelDto;
import com.truyenhinh24h.model.Channel;

@Service
public class ChannelService {
	
	@Autowired
	private ChannelRepository channelRepository;
	
	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;

	public ChannelDto create(ChannelDto channelDto) {
		Channel channelEntity = mapper(channelDto);
		channelEntity.setChannelId(sequenceGeneratorService.generateSequence(Channel.SEQUENCE_NAME));
		Channel insertedChannel = channelRepository.save(channelEntity);
		return mapper(insertedChannel);

	}
	
	Channel mapper(ChannelDto channel) {
		if(channel == null) {
			return null;
		}
		Channel channelEntity = new Channel();
		channelEntity.setChannelId(channel.getChannelId());
		channelEntity.setName(channel.getName());
		channelEntity.setCategories(channel.getCategories());
		channelEntity.setDescription(channel.getDescription());
		channelEntity.setLogoUrl(channel.getLogoUrl());
		channelEntity.setNetworkId(channel.getNetworkId());
		channelEntity.setVip(channel.isVip());
		return channelEntity;
	}
	
	ChannelDto mapper(Channel channel) {
		if(channel == null) {
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
