package com.truyenhinh24h.dao;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.truyenhinh24h.model.Channel;

public interface ChannelRepository extends MongoRepository<Channel, Long> {
	
	void deleteByChannelIdIn(Long[] ids);

}
