package com.lungcare.dicomfile.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lungcare.dicomfile.dao.ISendDAO;
import com.lungcare.dicomfile.entity.SendEntity;
import com.lungcare.dicomfile.service.ISendService;

public class SendServiceImp implements ISendService {
	@Autowired
	private ISendDAO sendDAO;
	public List<SendEntity> getAllSendEntity(){
		return sendDAO.getAllSendEntity();
	}
}
