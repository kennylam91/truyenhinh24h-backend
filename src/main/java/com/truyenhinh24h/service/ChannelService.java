package com.truyenhinh24h.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

	public ChannelDto createOrUpdate(ChannelDto channelDto) {
		Channel channelEntity = mapper(channelDto);
		Channel result = null;
		if (channelEntity.getId() == null) {
			channelEntity.setId(sequenceGeneratorService.generateSequence(Channel.SEQUENCE_NAME));
			result = channelRepository.insert(channelEntity);
		} else {
			result = channelRepository.save(channelEntity);
		}
		return mapper(result);

	}
	
	public void deleteMulti(Long[] ids) {
		channelRepository.deleteByChannelIdIn(ids);
	}
	
	Channel mapper(ChannelDto channel) {
		if(channel == null) {
			return null;
		}
		Channel channelEntity = new Channel();
		channelEntity.setId(channel.getId());
		channelEntity.setName(channel.getName());
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
		channelDto.setId(channel.getId());
		channelDto.setName(channel.getName());
		channelDto.setDescription(channel.getDescription());
		channelDto.setLogoUrl(channel.getLogoUrl());
		channelDto.setNetworkId(channel.getNetworkId());
		channelDto.setVip(channel.isVip());
		return channelDto;
	}

	public Page<ChannelDto> getAll(Pageable pageable) {
		Page<Channel> channelPage = channelRepository.findAll(pageable);
		List<ChannelDto> channelDtoList = null;
		if(channelPage.hasContent()) {
			channelDtoList = channelPage.getContent().stream()
					.map(this::mapper)
					.collect(Collectors.toList());
		}
		Page<ChannelDto> channelDtoPage = new PageImpl<ChannelDto>(channelDtoList, pageable, 
				channelPage.getTotalElements());
		return channelDtoPage;
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
