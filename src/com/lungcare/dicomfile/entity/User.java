package com.lungcare.dicomfile.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {
	public String name;
	public int age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

}
