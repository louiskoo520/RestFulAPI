package com.lungcare.dicomfile.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.lungcare.dicomfile.entity.BmpPathEntity;
import com.sun.jersey.multipart.FormDataMultiPart;

public interface IRemoteFileTransferDAO {
	
	public void uploadFile(FormDataMultiPart formParams,HttpServletRequest request,String cid);
	public void uploadZipFile(FormDataMultiPart formParams,HttpServletRequest request,String cid);
	public void uploadLeakingData(FormDataMultiPart formParams,HttpServletRequest request,String cid);

	

	public void deleteCompleteData(String id);
	
	
	public List<BmpPathEntity> getAllBmpPath(String path);

	
	public void reHandlerSeg(String id);
	
	
	public String downloadDCM(String id);
	public String downloadSeg(String id);
	public String downloadCTmhd(String id);
	public String downloadLeakingData(String id);
	public void test();

	
	
	
	
	
}