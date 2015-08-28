package com.lungcare.dicomfile.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lungcare.dicomfile.dao.IUserEntityDAO;
import com.lungcare.dicomfile.service.IUserEntityService;

public class UserEntityServiceImp implements IUserEntityService {
	
	@Autowired
	private IUserEntityDAO userEntityDAO;
	
	public List<?> getAllUsers(){
		return userEntityDAO.getAllUsers();
	}
	
	
	public void addUser(){
		userEntityDAO.addUser();
	}
}
