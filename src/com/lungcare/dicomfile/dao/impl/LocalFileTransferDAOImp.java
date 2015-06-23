package com.lungcare.dicomfile.dao.impl;

import org.apache.log4j.Logger;

import com.lungcare.dicomfile.dao.ILocalFileTransferDAO;

public class LocalFileTransferDAOImp implements ILocalFileTransferDAO {
	private static Logger logger = Logger
			.getLogger(LocalFileTransferDAOImp.class);

	public void uploadFile() {
		// TODO Auto-generated method stub
		logger.info("uploadFile");
		System.out.println("LocalFileTransferDAOImp uploadFile.........");
	}

	public void downloadFile() {
		// TODO Auto-generated method stub
		logger.info("downloadFile");
	}
}
