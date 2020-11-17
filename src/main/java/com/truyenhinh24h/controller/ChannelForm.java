package com.truyenhinh24h.controller;

import javax.validation.constraints.NotEmpty;
import com.truyenhinh24h.common.ImportSource;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelForm extends BaseForm {

	private Long id;

	@NotEmpty(message = "Name must not be empty")
	private String name;

	private String description;

	private String logo;

	private boolean vip = false;

	private String category;

	private Long[] channelIds;

	private Boolean hasAutoImport;

	private ImportSource importSource;

	private String sctvChannelCode;
	
	private Boolean isProgramAutoSearch = false;


}
