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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((limit == null) ? 0 : limit.hashCode());
		result = prime * result + ((page == null) ? 0 : page.hashCode());
		result = prime * result + ((sortBy == null) ? 0 : sortBy.hashCode());
		result = prime * result + ((sortDirection == null) ? 0 : sortDirection.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseForm other = (BaseForm) obj;
		if (limit == null) {
			if (other.limit != null)
				return false;
		} else if (!limit.equals(other.limit))
			return false;
		if (page == null) {
			if (other.page != null)
				return false;
		} else if (!page.equals(other.page))
			return false;
		if (sortBy == null) {
			if (other.sortBy != null)
				return false;
		} else if (!sortBy.equals(other.sortBy))
			return false;
		if (sortDirection == null) {
			if (other.sortDirection != null)
				return false;
		} else if (!sortDirection.equals(other.sortDirection))
			return false;
		return true;
	}
	
	
}
