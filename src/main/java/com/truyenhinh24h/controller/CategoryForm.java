package com.truyenhinh24h.controller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Value;

@Getter
@Setter
@NoArgsConstructor
public class CategoryForm extends BaseForm {

	private Long categoryId;

	private String name;
}
