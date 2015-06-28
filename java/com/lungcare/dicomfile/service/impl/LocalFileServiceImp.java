package com.lungcare.dicomfile.service.impl;

import com.lungcare.dicomfile.dao.ILocalFileTransferDAO;
import com.lungcare.dicomfile.dao.impl.LocalFileTransferDAOImp;
import com.lungcare.dicomfile.service.ILocalFileService;

public class LocalFileServiceImp implements ILocalFileService {
	ILocalFileTransferDAO localFileTransferDAO = new LocalFileTransferDAOImp();

	public void upload() {
		// TODO Auto-generated method stub
		localFileTransferDAO.uploadFile();
	}

	public void download() {
		// TODO Auto-generated method stub
		localFileTransferDAO.downloadFile();
	}
}
