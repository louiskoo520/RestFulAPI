package com.lungcare.dicomfile.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.lungcare.dicomfile.entity.ReceiveEntity;
import com.sun.jersey.multipart.FormDataMultiPart;

public interface IRemoteFileService {
	public void uploadFile(FormDataMultiPart formParams,
			ReceiveEntity receiveEntity);

	public byte[] downloadFile(HttpServletRequest req);

	public ReceiveEntity getReceiveEntity(String id);

	public void test();

	public List<ReceiveEntity> GetAllReceiveEntity();
}
