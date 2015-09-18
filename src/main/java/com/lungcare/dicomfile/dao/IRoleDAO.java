package com.lungcare.dicomfile.dao;

import java.util.List;

import com.lungcare.dicomfile.entity.Role;

public interface IRoleDAO {
	public void addRole();

	public boolean addRole(String rolename, String authUpload,
			String authHandle, String authDownload, String authUsers);

	public List<Role> getallRoles();

	public boolean isExistRoleName(String name);
}
