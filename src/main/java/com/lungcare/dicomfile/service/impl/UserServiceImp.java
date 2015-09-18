package com.lungcare.dicomfile.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.lungcare.dicomfile.dao.IUserDAO;
import com.lungcare.dicomfile.entity.Login;
import com.lungcare.dicomfile.entity.User;
import com.lungcare.dicomfile.service.IUserService;

public class UserServiceImp implements IUserService {

	@Autowired
	private IUserDAO userEntityDAO;

	public String login(HttpServletRequest request, String user_account,
			String user_password) {
		return userEntityDAO.login(request, user_account, user_password);
	}

	public String checkUserName(String user_account) {
		return userEntityDAO.checkUserName(user_account);
	}

	public String register(String user_account, String user_password) {
		return userEntityDAO.register(user_account, user_password);
	}

	public List<User> getAllUsers() {
		return userEntityDAO.getAllUsers();
	}

	public void addUser(String user_account, String user_name,
			String user_password, int user_age, int user_gender, int user_role,
			String user_tel, String user_address) {
		userEntityDAO.addUser(user_account, user_name, user_password, user_age,
				user_gender, user_role, user_tel, user_address);
	}

	public void test() {
		userEntityDAO.test();
	}

	@Override
	public List<Login> getAllLoginInfo() {
		// TODO Auto-generated method stub
		return userEntityDAO.getAllLoginInfo();
	}

	@Override
	public User getSessionUser() {
		// TODO Auto-generated method stub
		return userEntityDAO.getSessionUser();
	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub
		userEntityDAO.logout();
	}
}
