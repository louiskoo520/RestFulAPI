package com.lungcare.dicomfile.dao;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.lungcare.dicomfile.entity.Login;
import com.lungcare.dicomfile.entity.User;

public interface IUserDAO {

	public List<User> getAllUsers();

	public void addUser(String user_account, String user_name,
			String user_password, int user_age, int user_gender, int user_role,
			String user_tel, String user_address);

	public void test();

	public String login(HttpServletRequest request, String user_account,
			String user_password);

	public String register(String user_account, String user_password);

	public String checkUserName(String user_account);

	public List<Login> getAllLoginInfo();

	public User getSessionUser();

	public void logout();
}
