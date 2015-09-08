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

	public List<User> getAllUsers() {

		Session session = this.sessionFactory.getCurrentSession();

		session.beginTransaction();

		@SuppressWarnings("unchecked")
		List<User> users = session.createQuery(
				"select a from User a order by id").list();
		session.getTransaction().commit();

		if (users != null && users.size() > 0) {
			return users;
		}
		return null;

	}

	/**
	 * jdbc鏂规硶鏌ヨuser琛ㄥ拰role琛ㄥ叧鑱旂殑淇℃伅
	 */

	// public List<?> getAllUsers(){
	// Connection conn = null;
	// Statement st = null;
	// ResultSet resultSet = null;
	// try {
	// conn = JdbcUtils.getConnection();
	// st = conn.createStatement();
	// resultSet =
	// st.executeQuery("select u.name,r.name from user as u,role as r where u.role = r.role");
	// while(resultSet.next()){
	// System.out.println(resultSet.getObject("u.name"));
	// System.out.println(resultSet.getObject("r.name"));
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }finally{
	// JdbcUtils.free(resultSet, st, conn);
	// }
	// }

	public void addUser() {

	}

	// public List<?> getAllUsers(){
	// Connection conn = null;
	// Statement st = null;
	// ResultSet resultSet = null;
	// try {
	// conn = JdbcUtils.getConnection();
	// st = conn.createStatement();
	// resultSet =
	// st.executeQuery("select u.name,r.name from user as u,role as r where u.role = r.role");
	// while(resultSet.next()){
	// System.out.println(resultSet.getObject("u.name"));
	// System.out.println(resultSet.getObject("r.name"));
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }finally{
	// JdbcUtils.free(resultSet, st, conn);
	// }
	// }

	public void addUser(String user_account, String user_name,
			String user_password, int user_age, int user_gender, int user_role,
			String user_tel, String user_address) {

		User user = new User();
		user.setAccount(user_account);
		user.setPassword(user_password);
		user.setRole(user_role);
		user.setCreateDate(new Date());

		user.setName("zhangsan");
		user.setGender(0);
		user.setAge(22);
		user.setTel("13151719131");
		user.setLastLoginTime(new Date());
		user.setAddress("universe");
		user.setRo(getRoleByRoleNum(1));

		user.setName(user_name);
		user.setGender(user_gender);
		user.setAge(user_age);
		user.setTel(user_tel);
		user.setAddress(user_address);
		user.setRo(getRoleByRoleNum(user_role));

		Session session = this.sessionFactory.getCurrentSession();
		if (session != null) {
			System.out
					.println("this.sessionFactory.getCurrentSession().isOpen()");
			Transaction transaction = session.beginTransaction();
			session.save(user);
			session.flush();
			transaction.commit();
			System.out.println("add user success!!!");
		} else {
			System.out
					.println("this.sessionFactory.getCurrentSession().is null");
		}
	}

	public void test() {
		Type type = new Type();
		type.setName("瀛︽湳璁烘枃");

		Article article = new Article();
		article.setType(type);
		article.setName("鏄庢竻鏃朵唬鍙ゅ吀灏忚鐮旂┒");
		article.setContent("  鏄庢竻鏃朵唬鏄腑鍥藉彜鍏稿皬璇寸殑楂樺嘲鏃舵湡锛屾秾鐜颁簡涓�壒缁忓吀鐨勫皬璇淬�鍥涘ぇ鍚嶈憲渚挎槸浜т簬杩欎釜鏃舵湡銆�");

		Session session = this.sessionFactory.getCurrentSession();
		session.beginTransaction();

		session.persist(article);

		@SuppressWarnings("unchecked")
		List<Article> list = session.createQuery(
				" select a from Article a where a.name like '%鏄庢竻%'  ").list();

		for (Article a : list) {
			System.out.println("绫诲埆锛� " + a.getType().getName());
			System.out.println("鏍囬锛� " + a.getName());
			System.out.println("姒傝锛� " + substring(a.getContent(), 50));
			System.out.println("----------------------");
		}

		session.getTransaction().commit();
		// session.close();

	}

	private static String substring(String content, int i) {
		return content == null ? "" : content.length() < i + 1 ? content
				: content.substring(0, i);
	}

	public Role getRoleByRoleNum(int roleNum) {

		Session session = this.sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		Query query = session.createQuery("from Role r where r.roleNum=?");
		query.setParameter(0, roleNum);
		Role role = (Role) query.uniqueResult();
		if (role == null) {
			return null;
		}
		System.out.println(role.getName());
		System.out.println(role.getRoleNum());
		transaction.commit();
		return role;
	}

}
