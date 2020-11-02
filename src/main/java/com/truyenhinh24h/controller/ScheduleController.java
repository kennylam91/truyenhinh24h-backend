package com.truyenhinh24h.controller;

import java.io.IOException;
import java.security.cert.CollectionCertStoreParameters;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
import com.fasterxml.jackson.databind.util.JSONWrappedObject;
import com.truyenhinh24h.dao.StatsData;
import com.truyenhinh24h.model.ChannelDto;
import com.truyenhinh24h.model.Schedule;
import com.truyenhinh24h.model.ScheduleDto;
import com.truyenhinh24h.model.sctv.SctvEvent;
import com.truyenhinh24h.model.sctv.SctvRequestBody;
import com.truyenhinh24h.model.sctv.SctvResponseBody;
import com.truyenhinh24h.model.sctv.SctvSchedule;
import com.truyenhinh24h.service.ChannelService;
import com.truyenhinh24h.service.ScheduleService;

@RestController
@RequestMapping(path = "/rest/v1/schedules")
public class ScheduleController {

	private static final String UTC_PLUS_7 = "UTC+07:00";

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
		if (form.getChannelName().contains("THVL")) {
			scheduleList = getScheduleListFromTHVL(form);
		} else if (form.getApiSource().contentEquals("SCTV")) {
			scheduleList = getScheduleListFromSCTV(form);
		} else if(form.getApiSource().contentEquals("VTV")) {
			scheduleList = getScheduleListFromVTV(form);
		}

		if (!scheduleList.isEmpty()) {
			scheduleService.importMulti(scheduleList);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
	
	private List<Schedule> getScheduleListFromSCTV(ScheduleForm form) throws Exception {
		if (form.getChannelId() == null || form.getChannelName() == null || form.getImportDate() == null) {
			throw new Exception("Invalid request body");
		}
		final Map<Long, String> channelCodeMap = new HashMap<Long, String>();

		channelCodeMap.put(4L, "50");
		channelCodeMap.put(5L, "96");
		channelCodeMap.put(6L, "92");
		channelCodeMap.put(20L, "60");
		channelCodeMap.put(2L, "26");
		channelCodeMap.put(1L, "23");
		

		String url = "https://www.sctv.com.vn/WebMain/LichPhatSong/LayLichPhatSong";
		RestTemplate restTemplate = new RestTemplate();
		SctvRequestBody requestBody = new SctvRequestBody(channelCodeMap.get(form.getChannelId()),
				form.getImportDate().toString());
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json; charset=UTF-8");
		HttpEntity<SctvRequestBody> request = new HttpEntity<>(requestBody, headers);
		String response = restTemplate.postForObject(url, request, String.class);
		SctvResponseBody bodyObj = new ObjectMapper().readValue(response, SctvResponseBody.class);
		SctvSchedule sctvSchedule = bodyObj.getLichPhatSong();
		if (sctvSchedule == null) {
			throw new Exception("No schedule found");
		}
		List<SctvEvent> eventList = sctvSchedule.getEventList();
		if (eventList != null && !eventList.isEmpty()) {
			List<Schedule> scheduleList = eventList.stream().map(event -> event.mapper(form))
					.collect(Collectors.toList());
			return scheduleList;
		}

		return Collections.emptyList();
	}
	
	private List<Schedule> getScheduleListFromVTV(ScheduleForm form) throws Exception {
		if (form.getChannelId() == null || form.getChannelName() == null || form.getImportDate() == null) {
			throw new Exception("Invalid request body");
		}
		String url = "https://vtvgo.vn/ajax-get-list-epg?selected_date_epg=" + form.getImportDate().getYear() + "-" +
				form.getImportDate().getMonthValue() + "-" + form.getImportDate().getDayOfMonth()+ "&channel_id=" + form.getLastCharOfChannelName();
		Document doc = Jsoup.connect(url).get();
		List<Element> startTimeElements = doc.select(".time-pro").stream().collect(Collectors.toList());
		if(startTimeElements.isEmpty()) {
			throw new Exception("Not found data");
		}
		Elements programElements = doc.select("h3");
		Elements detailElements = doc.select("h4");
		List<String> startTimes = startTimeElements.stream()
				.filter(i -> i.ownText() != null && !i.ownText().trim().contentEquals(""))
				.map(i -> i.ownText())
				.collect(Collectors.toList());
		List<String> programs = programElements.stream().map(p -> p.ownText()).collect(Collectors.toList());
		List<String> details = detailElements.stream().map(p -> p.ownText()).collect(Collectors.toList());
		List<Schedule> scheduleList = new ArrayList<>();
		for(int i = 0; i< startTimes.size(); i++) {
			String timeString = startTimes.get(i);
			long colonIndex = timeString.indexOf(':');
			if(colonIndex == -1) {
				timeString = timeString.substring(0, 2) + ":" + timeString.substring(2);
			}
			String[] timeArr = timeString.split(":");
			if(timeArr.length >= 2) {
				Schedule schedule = new Schedule();
				
				String programName = details.get(i).trim().contentEquals("") ? 
						programs.get(i) : (programs.get(i) + ": " + details.get(i));
				schedule.setChannelName(form.getChannelName());
				schedule.setChannelId(form.getChannelId());
				schedule.setProgramName(programName);
				LocalDateTime startTimeLocalDate = form.getImportDate().atTime(LocalTime.parse(startTimes.get(i)));
				Date startTime = Date.from(startTimeLocalDate.atZone(ZoneId.of(UTC_PLUS_7)).toInstant());
				schedule.setStartTime(startTime);
				if (i >= 1) {
					scheduleList.get(i - 1).setEndTime(startTime);
				}
				if (i == startTimes.size() - 1) {
					Date endTime = Date.from(form.getImportDate().atTime(23, 59, 59).atZone(ZoneId.of(UTC_PLUS_7))
							.toInstant());
					schedule.setEndTime(endTime);
				}
				scheduleList.add(schedule);
			}
		}
		return scheduleList;
	}

	private List<Schedule> getScheduleListFromTHVL(ScheduleForm form) throws IOException {
		String url = "";
		url = "https://www.thvl.vn/lich-phat-song/?ngay=" + form.getImportDate().toString() + "&kenh="
				+ form.getChannelName();
		Document doc = Jsoup.connect(url).get();
		List<Element> startTimeElements = doc.select(".time").stream().filter(i -> !i.ownText().contentEquals(""))
				.collect(Collectors.toList());
		Elements programElements = doc.select(".program");
		List<String> startTimes = startTimeElements.stream()
				.filter(i -> i.ownText() != null && !StringUtils.isEmpty(i.ownText().trim()))
				.map(e -> e.ownText()).collect(Collectors.toList());
		List<String> programs = programElements.stream().map(e -> e.ownText())
				.filter(i -> i != null && !StringUtils.isEmpty(i.trim()))
				.collect(Collectors.toList());
		List<Schedule> scheduleList = new ArrayList<>();
		for (int i = 0; i < startTimes.size(); i++) {
			String timeString = startTimes.get(i).replaceAll("[^0-9:]", "");
			long colonIndex = timeString.indexOf(':');
			if (colonIndex == -1) {
				timeString = timeString.substring(0, 2) + ":" + timeString.substring(2);
			}
			String[] timeArr = timeString.split(":");
			if (timeArr.length >= 2) {
				Schedule schedule = new Schedule();
				schedule.setChannelId(form.getChannelId());
				schedule.setChannelName(form.getChannelName());
				schedule.setProgramName(programs.get(i));
				LocalDateTime startTimeLocalDate = form.getImportDate().atTime(LocalTime.parse(timeString));
				Date startTime = Date.from(startTimeLocalDate.atZone(ZoneId.of(UTC_PLUS_7)).toInstant());

				schedule.setStartTime(startTime);
				if (i >= 1) {
					scheduleList.get(i - 1).setEndTime(startTime);
				}
				if (i == startTimes.size() - 1) {
					Date endTime = Date
							.from(form.getImportDate().atTime(23, 59, 59).atZone(ZoneId.of(UTC_PLUS_7)).toInstant());
					schedule.setEndTime(endTime);
				}
				scheduleList.add(schedule);
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
