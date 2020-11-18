package com.truyenhinh24h.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.truyenhinh24h.common.ImportSource;
import com.truyenhinh24h.controller.ScheduleForm;
import com.truyenhinh24h.controller.StatsForm;
import com.truyenhinh24h.dao.StatsData;
import com.truyenhinh24h.model.ChannelDto;
import com.truyenhinh24h.model.Schedule;
import com.truyenhinh24h.utils.CommonUtils;

@Service
public class ScheduleTaskService {

	private final Logger logger = LoggerFactory.getLogger(ScheduleTaskService.class);

	@Autowired
	private ScheduleService scheduleService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private StatsService statsService;
	@Autowired
	private ChannelService channelService;

	@Scheduled(zone = "GMT+7:00", cron = "0 10 1,5,8 ? * *")
	public void autoUpdateSchedule() {
		Calendar cal = Calendar.getInstance();
		Date now = cal.getTime();
		cal.add(Calendar.DATE, 1);
		Date nextDayInGMT7 = cal.getTime();
		List<Date> updateDates = Arrays.asList(now, nextDayInGMT7);
		logger.info("Start auto update schedule {}", now);
		for (Date updateDate : updateDates) {
			StatsForm statsForm = new StatsForm(new Date(CommonUtils.getStartOfDay(updateDate)),
					new Date(CommonUtils.getEndOfDay(updateDate)));
			List<StatsData> statsDataList = statsService.getScheduleChannelStats(statsForm);
			List<ChannelDto> channelList = channelService.getAll();
			List<ChannelDto> importChannelList = channelList.stream().filter(i -> i.getImportSource() != null)
					.collect(Collectors.toList());
			for (ChannelDto channelDto : importChannelList) {
				StatsData foundStatsData = null;
				if (statsDataList != null || (statsDataList != null && !statsDataList.isEmpty())) {
					foundStatsData = statsDataList.stream().filter(i -> i.getChannelId().equals(channelDto.getId()))
							.findFirst().orElse(null);
					if (statsDataList == null || statsDataList.isEmpty() || foundStatsData == null
							|| foundStatsData.getTotal() == 0) {
						ScheduleForm scheduleForm = new ScheduleForm();
						scheduleForm.setChannelId(channelDto.getId());
						scheduleForm.setChannelName(channelDto.getName());
						LocalDate importDate = LocalDate.of(updateDate.getYear() + 1900, updateDate.getMonth() + 1,
								updateDate.getDate());
						scheduleForm.setImportDate(importDate);
						try {
							List<Schedule> scheduleList = new ArrayList<Schedule>();
							if (channelDto.getImportSource().equals(ImportSource.VTV)) {
								scheduleList = commonService.getScheduleListFromVTV(scheduleForm);
							} else if (channelDto.getImportSource().equals(ImportSource.THVL)) {
								scheduleList = commonService.getScheduleListFromTHVL(scheduleForm);
							} else if (channelDto.getImportSource().equals(ImportSource.SCTV)) {
								scheduleList = commonService.getScheduleListFromSCTV(scheduleForm);
							}
							if (!scheduleList.isEmpty()) {
								scheduleService.importMulti(scheduleList);
								logger.info("Import channel {} - date {} - {} schedule", channelDto.getName(),
										updateDate.getTime(), scheduleList.size());
							}
						} catch (Exception e) {
							logger.error(e.getMessage());
						}
					}

				}
			}
		}

	}
}
