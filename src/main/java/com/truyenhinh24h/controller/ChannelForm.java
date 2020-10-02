package com.truyenhinh24h.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelForm extends BaseForm {

	private Long id;

	private String name;

	private String description;

	private String logoUrl;

	private boolean vip;

	private Long networkId;
	
	private Long[] channelIds;
}
