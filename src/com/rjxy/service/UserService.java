package com.rjxy.service;

import java.sql.SQLException;

import com.rjxy.dao.UserDao;
import com.rjxy.domain.User;

public class UserService {

	//ע���û�
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
  //�����û�
	public void active(String activeCode) throws SQLException {
             UserDao dao= new UserDao();
             dao.active(activeCode);
          
	}
  //����û����Ƿ��Ѵ���
	public boolean CheckUsername(String username) throws SQLException {
		UserDao dao = new UserDao();
	  Long a=	dao.CheckUsername(username);
		return a>0?false:true;
	}
  
	//�û���¼�ķ���
	public User login(String username, String password) throws SQLException {
		UserDao dao = new UserDao();
		return dao.login(username,password);
	}



}
