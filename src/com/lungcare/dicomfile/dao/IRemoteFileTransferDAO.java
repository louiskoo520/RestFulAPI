package com.lungcare.dicomfile.dao;

import com.lungcare.dicomfile.entity.ReceiveEntity;
import com.sun.jersey.multipart.FormDataMultiPart;

public interface IRemoteFileTransferDAO {
	public void uploadFile(FormDataMultiPart formParams,
			ReceiveEntity receiveEntity);

	public void downloadFile();
}
