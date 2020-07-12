package com.navdeep.pixogram.web.dto;

import org.hibernate.validator.constraints.NotEmpty;

public class ImageUpdationDao {
	
	@NotEmpty
	private String name;

	@NotEmpty
	private String caption;

	@NotEmpty
	private String description;

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	

}
