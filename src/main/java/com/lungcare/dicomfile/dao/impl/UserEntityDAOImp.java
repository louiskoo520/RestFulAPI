package com.lungcare.dicomfile.dao.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.transaction.annotation.Transactional;

import com.lungcare.dicomfile.dao.IUserEntityDAO;
import com.lungcare.dicomfile.entity.User;


@Transactional
public class UserEntityDAOImp implements IUserEntityDAO {
	
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public List<?> getAllUsers(){
		Session session = this.sessionFactory.getCurrentSession();
		if (session != null) {
			Transaction transaction = session.beginTransaction();
			Query query = session
					.createQuery("select myUser.name,myRole.name from User as myUser,Role as myRole where myUser.role = myRole.role");
			List<?> users = query.list();
			transaction.commit();
			
			for (Iterator<?> iterator = users.iterator(); iterator.hasNext();) {
				User user = (User) iterator.next();
				System.out.println(user.getID() + "  "
						+ user.getRole());
			}
			
			if (users != null && users.size() > 0) {
				return users;
			}
			
		} else {
			System.out.println("this.sessionFactory.getCurrentSession().is null");
			return null;
		}

		return null;
	}
	
	
	public void addUser(){
		User user = new User();
		user.setID(0001);
		user.setAccount("admin");
		user.setPassword("admin");
		user.setRole(0);
		user.setCreateDate(new Date());
		user.setName("zhangsan");
		user.setGender(0);
		user.setAge(22);
		user.setTel("12345678901");
		user.setLastLoginTime(new Date());
		user.setAddress("universe");
		
		
		Session session = this.sessionFactory.getCurrentSession();
		if (session != null) {
			System.out.println("this.sessionFactory.getCurrentSession().isOpen()");
			Transaction transaction = session.beginTransaction();
			session.save(user);
			session.flush();
			transaction.commit();
		} else {
			System.out.println("this.sessionFactory.getCurrentSession().is null");
		}
	}
	
	
	
	
}
