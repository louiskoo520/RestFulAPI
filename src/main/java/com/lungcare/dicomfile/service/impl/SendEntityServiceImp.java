package com.lungcare.dicomfile.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lungcare.dicomfile.dao.ISendEntityDAO;
import com.lungcare.dicomfile.entity.SendEntity;
import com.lungcare.dicomfile.service.ISendEntityService;

public class SendEntityServiceImp implements ISendEntityService {
	@Autowired
	private ISendEntityDAO sendEntityDAO;
	public List<SendEntity> getAllSendEntity(){
		return sendEntityDAO.getAllSendEntity();
	}
}
