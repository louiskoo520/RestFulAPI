package com.lungcare.dicomfile.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.lungcare.dicomfile.dao.IRemoteFileTransferDAO;
import com.lungcare.dicomfile.service.IRemoteFileService;
import com.sun.jersey.multipart.FormDataMultiPart;

public class RemoteFileServiceImp implements IRemoteFileService {

	@Autowired
	private IRemoteFileTransferDAO remoteFileTransferDAO;

	public void uploadFile(FormDataMultiPart formParams) {
		remoteFileTransferDAO.uploadFile(formParams);
	}

	public void downloadFile() {

	}
}
