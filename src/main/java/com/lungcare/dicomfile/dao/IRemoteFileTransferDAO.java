package com.lungcare.dicomfile.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.lungcare.dicomfile.entity.ReceiveEntity;
import com.lungcare.dicomfile.entity.SendEntity;
import com.sun.jersey.multipart.FormDataMultiPart;

public interface IRemoteFileTransferDAO {
	
	public void uploadFile(FormDataMultiPart formParams,HttpServletRequest request,String cid);
	public byte[] downloadFile(HttpServletRequest req);

	
	public boolean addReceiveEntity(ReceiveEntity receiveEntity);
	public ReceiveEntity getReceiveEntity(String id);
	public List<ReceiveEntity> GetAllReceiveEntity();
	public boolean updateReceiveEntity(ReceiveEntity receiveEntity,int receivedNum);




	public void test();

	public void addSendEntity(String id);
	public List<SendEntity> getAllSendEntity();
	
}
