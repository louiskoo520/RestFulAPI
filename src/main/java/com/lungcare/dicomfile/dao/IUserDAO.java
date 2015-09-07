package com.lungcare.dicomfile.dao;

import java.util.List;

import com.lungcare.dicomfile.entity.User;

public interface IUserDAO {
	
	public List<User> getAllUsers();
	
	public void addUser();
	
	public void test();
}
