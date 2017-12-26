package com.rjxy.service;

import java.sql.SQLException;

import com.rjxy.dao.UserDao;
import com.rjxy.domain.User;

public class UserService {

	//注册用户
	public boolean regist(User user) {
		UserDao dao = new UserDao();
		int row=0;
		try {
			row = dao.regist(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return row>0?true:false;
	}
  //激活用户
	public void active(String activeCode) throws SQLException {
             UserDao dao= new UserDao();
             dao.active(activeCode);
          
	}
  //检查用户名是否已存在
	public boolean CheckUsername(String username) throws SQLException {
		UserDao dao = new UserDao();
	  Long a=	dao.CheckUsername(username);
		return a>0?false:true;
	}
  
	//用户登录的方法
	public User login(String username, String password) throws SQLException {
		UserDao dao = new UserDao();
		return dao.login(username,password);
	}



}
