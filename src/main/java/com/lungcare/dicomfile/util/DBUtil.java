package com.lungcare.dicomfile.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 		jdbc工具类，提供了获取连接，
 * 关闭连接的方法。
 *
 */
public class DBUtil {
	private static ThreadLocal<Connection> 
		connections = new ThreadLocal<Connection>();
	private static Properties props = 
		new Properties();
	static{
		InputStream ips = DBUtil.class.getClassLoader()
		.getResourceAsStream("com/lungcare/dicomfile/config/jdbc.properties");
		try {
			props.load(ips);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("读取db.properties文件失败");
		}
	}
	private static String className = props.getProperty("jdbc_driverClassName");
	private static String url = props.getProperty("jdbc_url");
	private static String username =  props.getProperty("jdbc_username");
	private static String pwd = props.getProperty("winteam500");
	
	public static void beginTranaction() throws SQLException{
		try {
			Connection conn = getConnection();
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("启动事务失败");
			throw e;
		}
	}
	public static void commit() throws SQLException{
		try {
			Connection conn = getConnection();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("提交事务失败");
			throw e;
		}finally{
			close();
		}
	}
	public static void rollback() throws SQLException{
		try {
			Connection conn = getConnection();
			conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("回滚事务失败");
			throw e;
		}finally{
			close();
		}
	}
	public static Connection getConnection() throws SQLException{
		Connection conn = connections.get();
		if(conn == null){
			conn = getConn();
			connections.set(conn);
		}
		return conn;
	}
	
	public static void close(){
		Connection conn = connections.get();
		if(conn != null){
			try {
				conn.close();
				connections.set(null);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	private static Connection getConn() throws SQLException{
		Connection conn = null;
		
		try {
			Class.forName(className);
			conn = DriverManager.getConnection(
					url,username,pwd);
		} catch (ClassNotFoundException e) {
			//记日志
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		
		return conn;
	}
	
	/**
	 * 关闭连接
	 */
	private static void close(Connection conn){
		if(conn != null){
			try {
				
				conn.close();
				
			} catch (SQLException e) {
				
				e.printStackTrace();
				
			}
		}
	}
	
	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		
		Connection coon = getConnection();
		close(coon);
		System.out.println(coon);
		
	}

}

