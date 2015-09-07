package com.lungcare.dicomfile.dao.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.transaction.annotation.Transactional;

import com.lungcare.dicomfile.dao.IUserDAO;
import com.lungcare.dicomfile.entity.Article;
import com.lungcare.dicomfile.entity.Role;
import com.lungcare.dicomfile.entity.Type;
import com.lungcare.dicomfile.entity.User;


@Transactional
public class UserDAOImp implements IUserDAO {
	
	private SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	
	public List<User> getAllUsers(){
		
		Session session = this.sessionFactory.getCurrentSession();
		
		session.beginTransaction();
		
		@SuppressWarnings("unchecked")
		List<User> users = session.createQuery("select a from User a order by id").list();
		session.getTransaction().commit();

		if (users != null && users.size() > 0) {
			return users;
		}
		return null;
		
	}
	
	
	
	/**
	 * jdbc方法查询user表和role表关联的信息
	 */
//	public List<?> getAllUsers(){
//		Connection conn = null;
//		Statement st = null;
//		ResultSet resultSet = null;
//		try {
//			conn = JdbcUtils.getConnection();
//			st = conn.createStatement();
//			resultSet = st.executeQuery("select u.name,r.name from user as u,role as r where u.role = r.role");
//			while(resultSet.next()){
//				System.out.println(resultSet.getObject("u.name"));
//				System.out.println(resultSet.getObject("r.name"));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}finally{
//			JdbcUtils.free(resultSet, st, conn);
//		}
//	}
	
	
	
	public void addUser(){
		
		
		User user = new User();
		user.setId(0002);
		user.setAccount("zhangsan");
		user.setPassword("111111");
		user.setRole(1);
		user.setCreateDate(new Date());
		user.setName("zhangsan");
		user.setGender(0);
		user.setAge(22);
		user.setTel("13151719131");
		user.setLastLoginTime(new Date());
		user.setAddress("universe");
		user.setRo(getRoleByID(1));
		
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
	
	public void test(){
		Type type = new Type();
		type.setName("学术论文");

		Article article = new Article();
		article.setType(type);
		article.setName("明清时代古典小说研究");
		article.setContent("  明清时代是中国古典小说的高峰时期，涌现了一批经典的小说。四大名著便是产于这个时期。");

		Session session = this.sessionFactory.getCurrentSession();
		session.beginTransaction();

		session.persist(article);

		@SuppressWarnings("unchecked")
		List<Article> list = session.createQuery(
				" select a from Article a where a.name like '%明清%'  ").list();

		for (Article a : list) {
			System.out.println("类别：" + a.getType().getName());
			System.out.println("标题：" + a.getName());
			System.out.println("概要：" + substring(a.getContent(), 50));
			System.out.println("----------------------");
		}

		session.getTransaction().commit();
		//session.close();


	}
	
	private static String substring(String content, int i) {
		return content == null ? "" : content.length() < i + 1 ? content
				: content.substring(0, i);
	}

	public Role getRoleByID(int id){
		
		Session session = this.sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("from Role r where r.id=?");
		query.setParameter(0, id);
		Role role = (Role) query.uniqueResult();
		System.out.println(role.getName());
		System.out.println(role.getRole());
		transaction.commit();
		return role;
	}

}
