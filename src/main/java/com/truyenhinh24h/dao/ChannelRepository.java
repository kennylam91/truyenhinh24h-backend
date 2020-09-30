package com.truyenhinh24h.dao;


import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.truyenhinh24h.model.Channel;

public interface ChannelRepository extends MongoRepository<Channel, Long> {
	
//	@DeleteQuery()
//	Void deleteMulti(Long[] channelIds);

}
