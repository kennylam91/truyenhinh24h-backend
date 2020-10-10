package com.truyenhinh24h.service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.truyenhinh24h.controller.ChannelForm;
import com.truyenhinh24h.dao.ChannelRepository;
import com.truyenhinh24h.dao.StatsData;
import com.truyenhinh24h.model.ChannelDto;
import com.truyenhinh24h.model.Channel;

@Service
public class ChannelService {
	
	@Autowired
	private ChannelRepository channelRepository;
	
	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;

	public ChannelDto createOrUpdate(Channel channel) {
		Channel result = null;
		if (channel.getId() == null) {
			channel.setId(sequenceGeneratorService.generateSequence(Channel.SEQUENCE_NAME));
			result = channelRepository.insert(channel);
		} else {
			result = channelRepository.save(channel);
		}
		return mapper(result);

	}
	
	public void deleteMulti(Long[] ids) {
		channelRepository.deleteByIdIn(ids);
	}
	
	Channel mapper(ChannelDto channel) {
		if(channel == null) {
			return null;
		}
		Channel channelEntity = new Channel();
		channelEntity.setId(channel.getId());
		channelEntity.setName(channel.getName());
		channelEntity.setDescription(channel.getDescription());
		channelEntity.setLogo(channel.getLogo());
		channelEntity.setNetworkId(channel.getNetworkId());
		channelEntity.setVip(channel.isVip());
		return channelEntity;
	}
	
	ChannelDto mapper(Channel channel) {
		if(channel == null) {
			return null;
		}
		ChannelDto channelDto = new ChannelDto();
		channelDto.setId(channel.getId());
		channelDto.setName(channel.getName());
		channelDto.setDescription(channel.getDescription());
		channelDto.setLogo(channel.getLogo());
		channelDto.setNetworkId(channel.getNetworkId());
		channelDto.setVip(channel.isVip());
		return channelDto;
	}

	public List<ChannelDto> getAll(){
		return channelRepository.findAll().stream().map(this::mapper).collect(Collectors.toList())
				;
		
	}

	public ChannelDto findById(Long channelId) {
		Optional<Channel> optional = channelRepository.findById(channelId);
		if (optional.isPresent()) {
			return mapper(optional.get());
		} else {
			return null;
		}
	}


}
