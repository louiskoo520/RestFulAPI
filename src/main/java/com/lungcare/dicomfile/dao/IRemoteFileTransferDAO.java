package com.lungcare.dicomfile.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.lungcare.dicomfile.entity.BmpPathEntity;
import com.lungcare.dicomfile.entity.ReceiveEntity;
import com.lungcare.dicomfile.entity.SendEntity;
import com.sun.jersey.multipart.FormDataMultiPart;

public interface IRemoteFileTransferDAO {
	
	public void uploadFile(FormDataMultiPart formParams,HttpServletRequest request,String cid);
	public void uploadSingleFile(FormDataMultiPart formParams,HttpServletRequest request,String cid);

	
	public boolean addReceiveEntity(ReceiveEntity receiveEntity);
	public ReceiveEntity getReceiveEntity(String id);
	public List<ReceiveEntity> getAllReceiveEntity();
	public List<ReceiveEntity> getCompleteReceiveEntity();
	public boolean updateReceiveEntity(ReceiveEntity receiveEntity,int receivedNum);

	public void deleteCompleteData(String id);
	
	public List<BmpPathEntity> getAllBmpPath(String path);

	public void reHandlerSeg(String id);
	
	public List<SendEntity> getAllSendEntity();
	
	public void test();
	
	public String downloadDCM(String id);
	public String downloadSeg(String id);
}
