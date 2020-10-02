package com.truyenhinh24h.model;

import java.util.Set;

import lombok.Data;
@Data
public class ChannelDto {

	private Long id;

	private String name;

	private String description;

	Set<String> categories;

	private String logoUrl;

	private boolean vip;

	private Long networkId;
}
