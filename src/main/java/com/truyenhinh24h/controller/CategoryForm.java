package com.truyenhinh24h.controller;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryForm extends BaseForm {

	private Long id;

	@NotEmpty(message = "Name must not be null")
	private String name;
}
