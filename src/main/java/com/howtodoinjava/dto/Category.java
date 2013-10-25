package com.howtodoinjava.dto;

import javax.xml.bind.annotation.XmlElement;

public class Category {
	@XmlElement(name="id")
	private Integer id;
	
	@XmlElement(name="name")
	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
}