package com.truyenhinh24h.common;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class Utils {
	
	

	private Utils() {
	}

	private static final String[] HEADERS_TO_TRY = { "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP",
			"HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP",
			"HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR" };
	
//	public static final String SYMBOL_REGEX = "[!@#$%^&*()_+~;',./<>?:\\\"-=]";
	public static final String SYMBOL_REGEX = "[\\!\\:\\-\\&\\.\\(\\)\\,\\']";

	public static String getClientIpAddress(HttpServletRequest request) {
		for (String header : HEADERS_TO_TRY) {
			String ip = request.getHeader(header);
			if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
				return ip;
			}
		}

		return request.getRemoteAddr();
	}
	
	public static final Map<String, Object> CACHE_MAP = new HashMap<String, Object>();
}
