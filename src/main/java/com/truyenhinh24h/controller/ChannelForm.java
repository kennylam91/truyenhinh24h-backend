package com.truyenhinh24h.controller;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.bind.DefaultValue;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelForm extends BaseForm {

	private Long id;

	@NotEmpty(message = "Name must not be empty")
	private String name;

	private String description;

	private String logoUrl;

	private boolean vip = false;

	private Long networkId;
	
	private Long[] channelIds;
}
