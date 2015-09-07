package com.lungcare.dicomfile.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.lungcare.dicomfile.dao.IUserDAO;
import com.lungcare.dicomfile.entity.User;
import com.lungcare.dicomfile.service.IUserService;

public class UserServiceImp implements IUserService {
	
	@Autowired
	private IUserDAO userEntityDAO;
	
	public List<User> getAllUsers(){
		return userEntityDAO.getAllUsers();
	}
	
	
	public void addUser(){
		userEntityDAO.addUser();
	}
	
	public void test(){
		userEntityDAO.test();
	}
}
