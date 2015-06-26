package com.lungcare.dicomfile.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lungcare.dicomfile.dao.IRemoteFileTransferDAO;
import com.lungcare.dicomfile.entity.ReceiveEntity;
import com.lungcare.dicomfile.service.IRemoteFileService;
import com.sun.jersey.multipart.FormDataMultiPart;

public class RemoteFileServiceImp implements IRemoteFileService {

	@Autowired
	private IRemoteFileTransferDAO remoteFileTransferDAO;

	public void uploadFile(FormDataMultiPart formParams,
			ReceiveEntity receiveEntity) {
		remoteFileTransferDAO.uploadFile(formParams, receiveEntity);
	}

	public void downloadFile() {

	}

	public ReceiveEntity getReceiveEntity(String id) {
		// TODO Auto-generated method stub

		return remoteFileTransferDAO.getReceiveEntity(id);
	}

	public void test() {
		// TODO Auto-generated method stub
		System.out.println("RemoteFileServiceImp test()");
		remoteFileTransferDAO.test();
	}

	public List<ReceiveEntity> GetAllReceiveEntity() {
		// TODO Auto-generated method stub
		return null;
	}
}
