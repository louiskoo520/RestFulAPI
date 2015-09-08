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
	
	
	public void addUser(String user_account,String user_name,String user_password,int user_age,int user_gender,int user_role,String user_tel,String user_address){
		userEntityDAO.addUser(user_account,user_name,user_password,user_age,user_gender,user_role,user_tel,user_address);
	}
	
	public void test(){
		userEntityDAO.test();
	}
}
