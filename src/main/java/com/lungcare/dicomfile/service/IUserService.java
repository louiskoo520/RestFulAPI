package com.lungcare.dicomfile.service;

import java.util.List;

import com.lungcare.dicomfile.entity.User;

public interface IUserService {
	public List<User> getAllUsers();
	
	
	public void addUser();
	
	public void test();
}
