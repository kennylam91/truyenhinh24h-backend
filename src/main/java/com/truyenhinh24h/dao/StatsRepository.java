package com.truyenhinh24h.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.truyenhinh24h.controller.StatsForm;
import com.truyenhinh24h.exception.InvalidInputException;


public interface StatsRepository {

	List<StatsData> getScheduleChannelStats(StatsForm form);
	

}
