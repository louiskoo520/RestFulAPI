package com.lungcare.dicomfile.dao;

import com.sun.jersey.multipart.FormDataMultiPart;

public interface IRemoteFileTransferDAO {
	public void uploadFile(FormDataMultiPart formParams);

	public void downloadFile();
}
