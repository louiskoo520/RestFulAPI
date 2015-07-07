package com.lungcare.dicomfile.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@Table
public class BmpPathEntity {
	String bmpPath;

	public String getBmpPath() {
		return bmpPath;
	}

	public void setBmpPath(String bmpPath) {
		this.bmpPath = bmpPath;
	}
}
