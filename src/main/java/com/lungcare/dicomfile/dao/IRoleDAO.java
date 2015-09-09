package com.lungcare.dicomfile.dao;

import java.util.List;

import com.lungcare.dicomfile.entity.Role;

public interface IRoleDAO {
	public void addRole();

	public List<Role> getallRoles();
}
