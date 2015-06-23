package com.lungcare.dicomfile.service;

import com.sun.jersey.multipart.FormDataMultiPart;

public interface IRemoteFileService {
	public void uploadFile(FormDataMultiPart formParams);

	public void downloadFile();
}
