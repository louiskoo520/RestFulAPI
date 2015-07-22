package com.lungcare.dicomfile.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.lungcare.dicomfile.dao.IRemoteFileTransferDAO;
import com.lungcare.dicomfile.entity.BmpPathEntity;
import com.lungcare.dicomfile.entity.ReceiveEntity;
import com.lungcare.dicomfile.entity.SendEntity;
import com.lungcare.dicomfile.service.IRemoteFileService;
import com.sun.jersey.multipart.FormDataMultiPart;

public class RemoteFileServiceImp implements IRemoteFileService {

	@Autowired
	private IRemoteFileTransferDAO remoteFileTransferDAO;

	public void uploadFile(FormDataMultiPart formParams,HttpServletRequest request, String cid) {
		remoteFileTransferDAO.uploadFile(formParams, request, cid);
	}

	public ReceiveEntity getReceiveEntity(String id) {
		return remoteFileTransferDAO.getReceiveEntity(id);
	}

	public void test() {
		remoteFileTransferDAO.test();
	}

	public List<ReceiveEntity> getAllReceiveEntity() {
		return remoteFileTransferDAO.getAllReceiveEntity();
	}
	
	public List<ReceiveEntity> getCompleteReceiveEntity(){
		return remoteFileTransferDAO.getCompleteReceiveEntity();
	}

	public List<SendEntity> getAllSendEntity(){
		return remoteFileTransferDAO.getAllSendEntity();
	}

	public List<BmpPathEntity> getAllBmpPath(String path) {
		return remoteFileTransferDAO.getAllBmpPath(path);
	}

	public void deleteCompleteData(String id) {
		remoteFileTransferDAO.deleteCompleteData(id);
	}
	
	
}
