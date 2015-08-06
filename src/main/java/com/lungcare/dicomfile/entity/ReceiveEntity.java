package com.lungcare.dicomfile.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@Table
public class ReceiveEntity {
	@Id
	private String id;
	private String accessionNumber;
	private String patientName;
	private String studyDate;
	private String ip;
	private int port;
	private String savedFolder;
	private int totalFiles;
	private Date date;
	private int received;
	private int speed;
	private int failed;
	private String message;
	private boolean complete;
	private boolean jpgAxial;
	private boolean bmpAxial;
	private boolean bmpCoronal;
	private boolean bmpSagittal;
	private int completeTime;
	private double pixelSpacing;
	private double sliceThickness;
	private String institutionName;
	
	

	public String getInstitutionName() {
		return institutionName;
	}

	public void setInstitutionName(String institutionName) {
		this.institutionName = institutionName;
	}

	public double getPixelSpacing() {
		return pixelSpacing;
	}

	public void setPixelSpacing(double pixelSpacing) {
		this.pixelSpacing = pixelSpacing;
	}

	public double getSliceThickness() {
		return sliceThickness;
	}

	public void setSliceThickness(double sliceThickness) {
		this.sliceThickness = sliceThickness;
	}

	public int getCompleteTime() {
		return completeTime;
	}

	public void setCompleteTime(int completeTime) {
		this.completeTime = completeTime;
	}

	public boolean isJpgAxial() {
		return jpgAxial;
	}

	public void setJpgAxial(boolean jpgAxial) {
		this.jpgAxial = jpgAxial;
	}

	public boolean isBmpAxial() {
		return bmpAxial;
	}

	public void setBmpAxial(boolean bmpAxial) {
		this.bmpAxial = bmpAxial;
	}

	public boolean isBmpCoronal() {
		return bmpCoronal;
	}

	public void setBmpCoronal(boolean bmpCoronal) {
		this.bmpCoronal = bmpCoronal;
	}

	public boolean isBmpSagittal() {
		return bmpSagittal;
	}

	public void setBmpSagittal(boolean bmpSagittal) {
		this.bmpSagittal = bmpSagittal;
	}

	public String getAccessionNumber() {
		return accessionNumber;
	}

	public void setAccessionNumber(String accessionNumber) {
		this.accessionNumber = accessionNumber;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getStudyDate() {
		return studyDate;
	}

	public void setStudyDate(String studyDate) {
		this.studyDate = studyDate;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getSavedFolder() {
		return savedFolder;
	}

	public void setSavedFolder(String savedFolder) {
		this.savedFolder = savedFolder;
	}

	public int getTotalFiles() {
		return totalFiles;
	}

	public void setTotalFiles(int totalFiles) {
		this.totalFiles = totalFiles;
	}

	public int getReceived() {
		return received;
	}

	public void setReceived(int received) {
		this.received = received;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getFailed() {
		return failed;
	}

	public void setFailed(int failed) {
		this.failed = failed;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


}
