package com.lungcare.dicomfile.dao;

import java.util.List;

import com.lungcare.dicomfile.entity.ReceiveEntity;

public interface IReceiveEntityDAO {
	
	
	public boolean updateReceiveEntity(ReceiveEntity receiveEntity,int receivedNum);
	public boolean updateReceiveEntity(ReceiveEntity receiveEntity,String firstDCMString);
	public ReceiveEntity getReceiveEntity(String id);
	
	public List<ReceiveEntity> getAllReceiveEntity();
	
	public boolean updateReceiveEntityCompleteState(ReceiveEntity receiveEntity);
	public boolean updateReceiveEntityJPGAxial(ReceiveEntity receiveEntity);
	public boolean updateReceiveEntityCompleteTime(ReceiveEntity receiveEntity,int time);
	public boolean updateFailedReceiveEntity(ReceiveEntity receiveEntity,int failedNum);
	
	public List<ReceiveEntity> getCompleteReceiveEntity();
	
	public boolean addReceiveEntity(ReceiveEntity receiveEntity);
	public boolean updateReceiveEntityLeakingFileName(String name,String id);
	
	public void deleteCompleteData(String id,String saveFolder_path,String bmpFolder_path);
	
	
	
}
