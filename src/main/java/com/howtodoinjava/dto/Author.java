package com.howtodoinjava.dto;

import java.sql.Date;

import javax.xml.bind.annotation.XmlElement;

public class Author {	
	
	@XmlElement(name="id")
	private Integer id;
	
	@XmlElement(name="name")
	private String name;
	
	@XmlElement(name="lastname")
	private String lastName;
	
	@XmlElement(name="birthday")
	private Date birthday;

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

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
}