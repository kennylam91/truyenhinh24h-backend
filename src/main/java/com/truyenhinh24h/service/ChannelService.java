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
import com.truyenhinh24h.utils.Mapper;
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
	@Autowired
	private Mapper mapper;

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
		return mapper.fromTo(result, ChannelDto.class);
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
		return mapper.fromToList(channelRepository.findAll(), ChannelDto.class);
	}

	@Cacheable(cacheNames = { "channels" }, key = "#channelId")
	public ChannelDto findById(Long channelId) {
		Optional<Channel> optional = channelRepository.findById(channelId);
		return optional.isPresent() ? mapper.fromTo(optional.get(), ChannelDto.class) : null;
	}
}
