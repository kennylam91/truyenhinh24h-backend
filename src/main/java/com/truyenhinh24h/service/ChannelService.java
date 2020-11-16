package com.truyenhinh24h.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.truyenhinh24h.dao.ChannelRepository;
import com.truyenhinh24h.model.ChannelDto;
import com.truyenhinh24h.model.Channel;

@CacheConfig(cacheNames = { "channels" })
@Service
public class ChannelService {

	@Autowired
	private ChannelRepository channelRepository;

	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;

	@Autowired
	CacheManager cacheManager;

	@CachePut(cacheNames = { "channels" }, key = "#result.id")
	@CacheEvict(cacheNames = { "all-channels", "programs-by-time" }, allEntries = true)
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
		for (Long id : ids) {
			cacheManager.getCache("channels").evictIfPresent(id);
		}
		cacheManager.getCache("all-channels").clear();
		cacheManager.getCache("programs-by-time").clear();
	}

	@Cacheable(cacheNames = { "all-channels" })
	public List<ChannelDto> getAll() {
		return channelRepository.findAll().stream().map(this::mapper).collect(Collectors.toList());
	}

	@Cacheable(cacheNames = { "channels" }, key = "#channelId")
	public ChannelDto findById(Long channelId) {
		Optional<Channel> optional = channelRepository.findById(channelId);
		return optional.isPresent() ? mapper(optional.get()) : null;
	}

	Channel mapper(ChannelDto channel) {
		if (channel == null) {
			return null;
		}
		Channel channelEntity = new Channel();
		channelEntity.setId(channel.getId());
		channelEntity.setName(channel.getName());
		channelEntity.setDescription(channel.getDescription());
		channelEntity.setLogo(channel.getLogo());
		channelEntity.setCategory(channel.getCategory());
		channelEntity.setVip(channel.isVip());
		channelEntity.setHasAutoImport(channel.getHasAutoImport());
		channelEntity.setImportSource(channel.getImportSource());
		return channelEntity;
	}

	ChannelDto mapper(Channel channel) {
		if (channel == null) {
			return null;
		}
		ChannelDto channelDto = new ChannelDto();
		channelDto.setId(channel.getId());
		channelDto.setName(channel.getName());
		channelDto.setDescription(channel.getDescription());
		channelDto.setLogo(channel.getLogo());
		channelDto.setCategory(channel.getCategory());
		channelDto.setVip(channel.isVip());
		channelDto.setHasAutoImport(channel.getHasAutoImport());
		channelDto.setImportSource(channel.getImportSource());
		return channelDto;
	}

}
