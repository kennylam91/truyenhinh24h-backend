package com.truyenhinh24h.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.truyenhinh24h.controller.StatsForm;
import com.truyenhinh24h.dao.StatsData;
import com.truyenhinh24h.dao.StatsRepository;

@Service
public class StatsService {

	@Autowired
	private StatsRepository statsRepository;
	
	public List<StatsData> getScheduleChannelStats(StatsForm form){
		return statsRepository.getScheduleChannelStats(form);
	}
}
