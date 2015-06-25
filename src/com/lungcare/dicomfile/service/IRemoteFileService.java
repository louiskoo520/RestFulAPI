package com.lungcare.dicomfile.service;

import com.lungcare.dicomfile.entity.ReceiveEntity;
import com.sun.jersey.multipart.FormDataMultiPart;

public interface IRemoteFileService {
	public void uploadFile(FormDataMultiPart formParams,
			ReceiveEntity receiveEntity);

	public void downloadFile();

	public ReceiveEntity getReceiveEntity(String id);

	public void test();
}
