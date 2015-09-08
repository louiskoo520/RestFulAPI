package com.lungcare.dicomfile.service;

import java.util.List;

import com.lungcare.dicomfile.entity.User;

public interface IUserService {
	public List<User> getAllUsers();
	
	
	public void addUser(String user_account,String user_name,String user_password,int user_age,int user_gender,int user_role,String user_tel,String user_address);
	
	public void test();
}
