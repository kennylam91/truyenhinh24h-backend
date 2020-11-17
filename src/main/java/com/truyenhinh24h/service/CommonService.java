package com.truyenhinh24h.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.truyenhinh24h.controller.ScheduleController;
import com.truyenhinh24h.controller.ScheduleForm;
import com.truyenhinh24h.model.Schedule;
import com.truyenhinh24h.model.sctv.SctvEvent;
import com.truyenhinh24h.model.sctv.SctvRequestBody;
import com.truyenhinh24h.model.sctv.SctvResponseBody;
import com.truyenhinh24h.model.sctv.SctvSchedule;

@Service
public class CommonService {

	public List<Schedule> getScheduleListFromSCTV(ScheduleForm form) throws Exception {
		if (form.getChannelId() == null || form.getChannelName() == null || form.getImportDate() == null) {
			throw new Exception("Invalid request body");
		}
		final Map<Long, String> channelCodeMap = new HashMap<>();

		channelCodeMap.put(1L, "23"); // HBO
		channelCodeMap.put(2L, "26"); // Fox Movies
		channelCodeMap.put(4L, "50"); // AXN
		channelCodeMap.put(5L, "96"); // Discovery Asia
		channelCodeMap.put(6L, "92"); // Discovery
		channelCodeMap.put(20L, "60"); // Todaytv

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

	public List<Schedule> getScheduleListFromVTV(ScheduleForm form) throws Exception {
		if (form.getChannelId() == null || form.getChannelName() == null || form.getImportDate() == null) {
			throw new Exception("Invalid request body");
		}
		String url = "https://vtvgo.vn/ajax-get-list-epg?selected_date_epg=" + form.getImportDate().getYear() + "-"
				+ form.getImportDate().getMonthValue() + "-" + form.getImportDate().getDayOfMonth() + "&channel_id="
				+ form.getLastCharOfChannelName();
		Document doc = Jsoup.connect(url).get();
		List<Element> startTimeElements = doc.select(".time-pro").stream().collect(Collectors.toList());
		if (startTimeElements.isEmpty()) {
			throw new Exception("Not found data");
		}
		Elements programElements = doc.select("h3");
		Elements detailElements = doc.select("h4");
		List<String> startTimes = startTimeElements.stream()
				.filter(i -> i.ownText() != null && !i.ownText().trim().contentEquals("")).map(i -> i.ownText())
				.collect(Collectors.toList());
		List<String> programs = programElements.stream().map(p -> p.ownText()).collect(Collectors.toList());
		List<String> details = detailElements.stream().map(p -> p.ownText()).collect(Collectors.toList());
		List<Schedule> scheduleList = new ArrayList<>();
		for (int i = 0; i < startTimes.size(); i++) {
			String timeString = startTimes.get(i);
			long colonIndex = timeString.indexOf(':');
			if (colonIndex == -1) {
				timeString = timeString.substring(0, 2) + ":" + timeString.substring(2);
			}
			String[] timeArr = timeString.split(":");
			if (timeArr.length >= 2) {
				Schedule schedule = new Schedule();

				String programName = details.get(i).trim().contentEquals("") ? programs.get(i)
						: (programs.get(i) + ": " + details.get(i));
				schedule.setChannelName(form.getChannelName());
				schedule.setChannelId(form.getChannelId());
				schedule.setProgramName(programName);
				LocalDateTime startTimeLocalDate = form.getImportDate().atTime(LocalTime.parse(startTimes.get(i)));
				Date startTime = Date
						.from(startTimeLocalDate.atZone(ZoneId.of(ScheduleController.UTC_PLUS_7)).toInstant());
				schedule.setStartTime(startTime);
				if (i >= 1) {
					scheduleList.get(i - 1).setEndTime(startTime);
				}
				if (i == startTimes.size() - 1) {
					Date endTime = Date.from(form.getImportDate().atTime(23, 59, 59)
							.atZone(ZoneId.of(ScheduleController.UTC_PLUS_7)).toInstant());
					schedule.setEndTime(endTime);
				}
				scheduleList.add(schedule);
			}
		}
		return scheduleList;
	}

	public List<Schedule> getScheduleListFromTHVL(ScheduleForm form) throws IOException {
		String url = "";
		url = "https://www.thvl.vn/lich-phat-song/?ngay=" + form.getImportDate().toString() + "&kenh="
				+ form.getChannelName();
		Document doc = Jsoup.connect(url).get();
		List<Element> startTimeElements = doc.select(".time").stream().filter(i -> !i.ownText().contentEquals(""))
				.collect(Collectors.toList());
		Elements programElements = doc.select(".program");
		List<String> rawStartTimes = startTimeElements.stream()
				.filter(i -> i.ownText() != null && !StringUtils.isEmpty(i.ownText().trim())).map(e -> e.ownText())
				.collect(Collectors.toList());
		List<String> startTimes = new LinkedList<>();
		// validate startTime
		for (int j = 0; j < rawStartTimes.size(); j++) {
			if (j > 0) {
				String startTimeStr = rawStartTimes.get(j);
				LocalTime localTime = LocalTime.parse(startTimeStr);
				LocalTime lastLocalTime = LocalTime.parse(rawStartTimes.get(j - 1));
				if (localTime.isAfter(lastLocalTime)) {
					startTimes.add(startTimeStr);
				}
			}
		}

		List<String> programs = programElements.stream().map(e -> e.ownText())
				.filter(i -> i != null && !StringUtils.isEmpty(i.trim())).collect(Collectors.toList());
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
				Date startTime = Date
						.from(startTimeLocalDate.atZone(ZoneId.of(ScheduleController.UTC_PLUS_7)).toInstant());

				schedule.setStartTime(startTime);
				if (i >= 1) {
					scheduleList.get(i - 1).setEndTime(startTime);
				}
				if (i == startTimes.size() - 1) {
					Date endTime = Date.from(form.getImportDate().atTime(23, 59, 59)
							.atZone(ZoneId.of(ScheduleController.UTC_PLUS_7)).toInstant());
					schedule.setEndTime(endTime);
				}
				scheduleList.add(schedule);
			}
		}
		return scheduleList;
	}
}
