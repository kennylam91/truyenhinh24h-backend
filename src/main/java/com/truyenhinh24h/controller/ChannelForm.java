package com.truyenhinh24h.controller;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelForm extends BaseForm {

	private Long channelId;

	private String name;

	private String description;

	private String logoUrl;

	private boolean vip;

	private Long networkId;
	
	private Long[] channelIds;
}
