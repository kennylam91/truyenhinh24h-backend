package com.truyenhinh24h.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.truyenhinh24h.dao.StatsData;
import com.truyenhinh24h.model.ChannelDto;
import com.truyenhinh24h.model.Schedule;
import com.truyenhinh24h.model.ScheduleDto;
import com.truyenhinh24h.service.ChannelService;
import com.truyenhinh24h.service.ScheduleService;

@RestController
@RequestMapping(path = "/rest/v1/schedules")
public class ScheduleController {

	private static final Logger logger = LogManager.getLogger(ScheduleController.class);

	@Autowired
	private ScheduleService scheduleService;
	
	@Autowired
	private ChannelService channelService;

	@PostMapping
	public ResponseEntity<ScheduleDto> createOrUpdate(@Valid @RequestBody ScheduleForm scheduleForm) {
		logger.info("Create Schedule");
		Schedule schedule = mapper(scheduleForm);
		ScheduleDto result = scheduleService.createOrUpdate(schedule);
		logger.info("Schedule created: {}", result);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/import")
	public ResponseEntity<Void> importMulti(@Valid @RequestBody List<ScheduleForm> forms) {
		List<Schedule> schedules = forms.stream().map(this::mapper).collect(Collectors.toList());
		scheduleService.importMulti(schedules);
		return ResponseEntity.ok().build();
	}
	
	@PostMapping(path ="/auto-update")
	public ResponseEntity<Void> autoUpdateSchedules(@RequestBody ScheduleForm form) throws Exception {
		List<Schedule> scheduleList = new ArrayList<>();
		if(form.getChannelName().contains("THVL")) {
			scheduleList = getScheduleListFromTHVL(form);
		}
		
		if(!scheduleList.isEmpty()) {
			scheduleService.importMulti(scheduleList);			
		}
		return ResponseEntity.ok().build();
	}
	
	private List<Schedule> getScheduleListFromTHVL(ScheduleForm form) throws IOException {
		String url = "";
		url = "https://www.thvl.vn/lich-phat-song/?ngay=" + form.getUpdateDate() + "&kenh=" + form.getChannelName();
		Document doc = Jsoup.connect(url).get();
		Elements startTimeElements = doc.select(".time");
		Elements programElements = doc.select(".program");
		List<String> startTimes = startTimeElements.stream().map(e -> e.ownText())
				.filter(i -> i != null && i != "" && i.contentEquals(" ")).collect(Collectors.toList());
		List<String> programs = programElements.stream().map(e -> e.ownText())
				.filter(i -> i != null && i != "" && i.contentEquals(" ")).collect(Collectors.toList());
		List<Schedule> scheduleList = new ArrayList<>();
		for (int i = 0; i < startTimes.size(); i++) {
			String timeString = startTimes.get(i);
			long colonIndex = timeString.indexOf(':');
			if(colonIndex == -1) {
				timeString = timeString.substring(0, 2) + ":" + timeString.substring(2);
			}
			String[] timeArr = timeString.split(":");
			String[] dateArr = form.getUpdateDate().split("-");
			if (timeArr.length >= 2 && dateArr.length >= 3) {
				Calendar scheduleTime = Calendar.getInstance();
				scheduleTime.setTimeZone(TimeZone.getTimeZone("GMT+7"));
				scheduleTime.set(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1]) - 1,
						Integer.parseInt(dateArr[2]), Integer.parseInt(timeArr[0]), Integer.parseInt(timeArr[1]));
				Schedule schedule = new Schedule();
				List<ChannelDto> channelDtoList = channelService.getAll();
				ChannelDto foundChannelDto = channelDtoList.stream()
						.filter(c -> c.getName().equalsIgnoreCase(form.getChannelName())).findFirst().orElse(null);
				if (foundChannelDto != null) {
					schedule.setChannelId(foundChannelDto.getId());
					schedule.setChannelName(foundChannelDto.getName());
					schedule.setProgramName(programs.get(i));
					schedule.setStartTime(scheduleTime.getTime());
					if (i >= 1) {
						scheduleList.get(i - 1).setEndTime(scheduleTime.getTime());
					}
					if (i == startTimes.size() - 1) {
						Calendar endOfDay = Calendar.getInstance();
						endOfDay.setTimeZone(TimeZone.getTimeZone("GMT+7"));
						endOfDay.set(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1]),
								Integer.parseInt(dateArr[2]), 24, 0);
						schedule.setEndTime(endOfDay.getTime());
					}
					scheduleList.add(schedule);
				}
			}
		}
		return scheduleList;
	}

	@PostMapping(path = "/search")
	public ResponseEntity<Page<ScheduleDto>> search(@RequestBody ScheduleForm scheduleForm,
			HttpServletRequest request) {
		Page<ScheduleDto> scheduleDtoPage = scheduleService.search(scheduleForm);
		return ResponseEntity.ok(scheduleDtoPage);
	}

	@PostMapping(path = "/delete-multi")
	public ResponseEntity<Void> deleteMulti(@RequestBody ScheduleForm scheduleForm) {
		if (scheduleForm.getScheduleIds() == null || scheduleForm.getScheduleIds().isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
		scheduleService.deleteMulti(scheduleForm.getScheduleIds());
		logger.info("Deleted Program : {}", scheduleForm.getScheduleIds());
		return ResponseEntity.ok().build();
	}

	@PostMapping(path = "/stats")
	public ResponseEntity<List<StatsData>> getScheduleStats(@RequestBody ScheduleForm form) {
		return ResponseEntity.ok(scheduleService.getScheduleStats(form));
	}
	
	@PostMapping(path = "/clear-cache")
	public ResponseEntity<Void> clearScheduleCache(){
		scheduleService.clearCache();
		return ResponseEntity.ok().build();
	}

	private Schedule mapper(ScheduleForm data) {
		if (data == null) {
			return null;
		}
		Schedule schedule = new Schedule();
		schedule.setChannelId(data.getChannelId());
		schedule.setChannelName(data.getChannelName());
		schedule.setEndTime(data.getEndTime());
		schedule.setProgramId(data.getProgramId());
		schedule.setProgramName(data.getProgramName());
		schedule.setId(data.getId());
		schedule.setStartTime(data.getStartTime());
		return schedule;

	}
	

}
