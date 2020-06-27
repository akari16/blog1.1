package com.lewyon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FirstPageBlog {
	// Blog
	public Long id;
	private String title;
	private String firstPicture;
	private Integer views;
	private Date updateTime;
	private String description;

	// Type
	private String typeName;

	// User
	private String nickname;
	private String avatar;
}
