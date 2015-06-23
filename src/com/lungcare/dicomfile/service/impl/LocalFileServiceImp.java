package com.lungcare.dicomfile.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.lungcare.dicomfile.dao.ILocalFileTransferDAO;
import com.lungcare.dicomfile.service.ILocalFileService;

public class LocalFileServiceImp implements ILocalFileService {

	@Autowired
	private ILocalFileTransferDAO localFileTransferDAO;

	public void uploadFile() {
		// TODO Auto-generated method stub
		localFileTransferDAO.uploadFile();
	}

	public void downloadFile() {
		// TODO Auto-generated method stub
		localFileTransferDAO.downloadFile();
	}

}
