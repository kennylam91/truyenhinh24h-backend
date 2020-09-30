package com.truyenhinh24h.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseForm {

	private Integer page;
	
	private Integer limit;
	
	public Integer getPage() {
		return page != null ? page : 1;
	}
	
	public Integer getLimit() {
		return limit != null ? limit : 10;
	}
}
