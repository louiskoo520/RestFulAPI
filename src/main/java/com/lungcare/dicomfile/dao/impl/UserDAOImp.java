package com.lungcare.dicomfile.dao.impl;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.transaction.annotation.Transactional;

import com.lungcare.dicomfile.dao.IUserDAO;
import com.lungcare.dicomfile.entity.Article;
import com.lungcare.dicomfile.entity.Login;
import com.lungcare.dicomfile.entity.Role;
import com.lungcare.dicomfile.entity.Type;
import com.lungcare.dicomfile.entity.User;

@Transactional
public class UserDAOImp implements IUserDAO {

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public String login(HttpServletRequest request, String user_account,
			String user_password) {

		User user = checkUserAccount(user_account);
		if (user != null) {
			if (checkUserPassword(user, user_password)) {
				addLogin(request, user_account);
				return "0";
			} else {
				return "2";
			}
		}

		return "1";
	}

	public void addLogin(HttpServletRequest request, String user_account) {

		Login login = new Login();
		login.setAccount(user_account);

		login.setLoginTime(new Date());

		String remoteHostString = "";
		if (request.getHeader("x-forwarded-for") == null) {
			remoteHostString = request.getRemoteAddr();// 获取上传者的IP地址
		} else {
			remoteHostString = request.getHeader("x-forwarded-for");
		}

		login.setLoginIP(remoteHostString);

		Session session = this.sessionFactory.getCurrentSession();
		if (session != null) {
			System.out
					.println("this.sessionFactory.getCurrentSession().isOpen()");
			Transaction transaction = session.beginTransaction();
			session.save(login);
			session.flush();
			transaction.commit();
			System.out.println("add login success!!!");
		} else {
			System.out
					.println("this.sessionFactory.getCurrentSession().is null");
		}
	}

	public String register(String user_account, String user_password) {

		User user = new User();
		user.setAccount(user_account);
		user.setPassword(user_password);
		user.setCreateDate(new Date());
		user.setRo(getRoleByRoleNum(2));

		Session session = this.sessionFactory.getCurrentSession();
		if (session != null) {
			System.out
					.println("this.sessionFactory.getCurrentSession().isOpen()");
			Transaction transaction = session.beginTransaction();
			session.save(user);
			session.flush();
			transaction.commit();
			return "1";
		} else {
			System.out
					.println("this.sessionFactory.getCurrentSession().is null");
		}

		return null;
	}

	public String checkUserName(String user_account) {
		User user = checkUserAccount(user_account);
		if (user != null) {
			return "1";
		}
		return "0";
	}

	public User checkUserAccount(String user_account) {
		Session session = this.sessionFactory.getCurrentSession();
		session.beginTransaction();
		Query query = session
				.createQuery("select u from User u where u.account=?");
		query.setParameter(0, user_account);
		User user = (User) query.uniqueResult();
		session.getTransaction().commit();
		if (user != null) {
			return user;
		}
		return null;
	}

	public boolean checkUserPassword(User user, String user_password) {
		if (user_password.equals(user.getPassword())) {
			return true;
		}
		return false;
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

	@Override
	public List<Login> getAllLoginInfo() {
		// TODO Auto-generated method stub
		Session session = this.sessionFactory.getCurrentSession();

		session.beginTransaction();

		@SuppressWarnings("unchecked")
		List<Login> loginInofs = session.createQuery(
				"select a from Login a order by loginTime").list();
		session.getTransaction().commit();

		if (loginInofs != null && loginInofs.size() > 0) {
			return loginInofs;
		}
		return null;

	}

	/**
	 * jdbc方法查询user表和role表关联的信息
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

	public void addUser(String user_account, String user_name,
			String user_password, int user_age, int user_gender, int user_role,
			String user_tel, String user_address) {

		User user = new User();
		user.setAccount(user_account);
		user.setPassword(user_password);
		user.setRole(user_role);
		user.setCreateDate(new Date());
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
