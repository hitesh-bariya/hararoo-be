package com.hararoobe.hararoo.model;

import lombok.Data;

@Data
public class JobCategoryDTO {
	
	private Long categoryId;

	private String categoryName;

	private Integer vacancy;
}
