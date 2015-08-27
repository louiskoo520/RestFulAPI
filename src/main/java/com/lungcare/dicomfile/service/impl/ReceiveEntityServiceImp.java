package com.lungcare.dicomfile.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lungcare.dicomfile.dao.IReceiveEntityDAO;
import com.lungcare.dicomfile.entity.ReceiveEntity;
import com.lungcare.dicomfile.service.IReceiveEntityService;

public class ReceiveEntityServiceImp implements IReceiveEntityService {
	
	@Autowired
	private IReceiveEntityDAO receiveEntityDAO;
	public ReceiveEntity getReceiveEntity(String id){
		return receiveEntityDAO.getReceiveEntity(id);
	}
	
	public List<ReceiveEntity> getAllReceiveEntity(){
		return receiveEntityDAO.getAllReceiveEntity();
	}
	
	public List<ReceiveEntity> getCompleteReceiveEntity(){
		return receiveEntityDAO.getCompleteReceiveEntity();
	}
}
