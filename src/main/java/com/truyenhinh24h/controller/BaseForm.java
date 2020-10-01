package com.truyenhinh24h.controller;

import java.util.Objects;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import lombok.Setter;

@Setter
public class BaseForm {

	private Integer page;

	private Integer limit;

	private String sortBy;

	private String sortDirection;

	public Integer getPage() {
		return page != null ? page : 1;
	}

	public Integer getLimit() {
		return limit != null ? limit : 10;
	}

	public String getSortBy() {
		return this.sortBy;
	}

	public Direction getSortDirectionObj() {
		if (Objects.equals(sortDirection, "DESC")) {
			return Sort.Direction.DESC;
		}
		return Sort.Direction.ASC;
	}
}
