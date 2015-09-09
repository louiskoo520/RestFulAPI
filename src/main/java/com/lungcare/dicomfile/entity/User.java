package com.lungcare.dicomfile.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
@Table
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String account;//账户
	
	private String password;//密码
	private int role;//权限 用数字表示
	private String name;//用户姓名
	private Date createDate;//帐号创建时间
	private int age;//用户年龄
	private String address;//用户地址
	private String tel;//用户电话
	private Date lastLoginTime;//最后一次登陆时间
	private int gender;//性别 0表示男 1表示女
	
	private String email;//用户邮箱
	
	@ManyToOne(cascade = { CascadeType.PERSIST }, fetch = FetchType.EAGER)
	private Role ro;
	
	
	
	
	
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Role getRo() {
		return ro;
	}
	public void setRo(Role ro) {
		this.ro = ro;
	}

	
	public int getRole() {
		return role;
	}
	public void setRole(int role) {
		this.role = role;
	}

	
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	
	
	
	
	
	
	
}
