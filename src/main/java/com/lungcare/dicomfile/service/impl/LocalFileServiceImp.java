package com.lungcare.dicomfile.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.lungcare.dicomfile.dao.ILocalFileTransferDAO;
import com.lungcare.dicomfile.service.ILocalFileService;

public class LocalFileServiceImp implements ILocalFileService {
	@Autowired
	ILocalFileTransferDAO localFileTransferDAO;

	public void upload() {
		// TODO Auto-generated method stub
		localFileTransferDAO.uploadFile();
	}

	public void download() {
		// TODO Auto-generated method stub
		localFileTransferDAO.downloadFile();
	}
}
