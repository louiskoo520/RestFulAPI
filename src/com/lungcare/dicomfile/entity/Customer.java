package com.lungcare.dicomfile.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Customer {
	private int id;  
    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	private String name;  
    private String address; 
}
