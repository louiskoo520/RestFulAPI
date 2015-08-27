package com.lungcare.dicomfile.service;

import java.util.List;

import com.lungcare.dicomfile.entity.ReceiveEntity;

public interface IReceiveEntityService {
	public ReceiveEntity getReceiveEntity(String id);
	
	public List<ReceiveEntity> getAllReceiveEntity();
	
	public List<ReceiveEntity> getCompleteReceiveEntity();
}
