package com.rjxy.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.rjxy.domain.User;
import com.rjxy.utils.DataSourceUtils;

public class UserDao {
   //��ͨ�˻�
	public int regist(User user) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource(),true);
		String sql = "insert into user_o values('"+ user.getUid()+"','"+user.getUsername()+"','"+user.getPassword()+"','"+user.getName()+"','"
		+user.getEmail()+"','"+user.getTelephone()+"','"+user.getBirthday()+"','"+user.getSex()+"',"+user.getState()+",'"+user.getCode()+"')";
		int update = runner.update(sql);
		return update;
	}
   //��֤�� �����˻� 
	public void active(String activeCode) throws SQLException {
		QueryRunner  runner = new QueryRunner(DataSourceUtils.getDataSource()); 
		String sql = "update  user_o set state=? where Code=?  ";
		runner.update(sql,1,activeCode);
		
		
	}
    //����û����Ƿ��ظ�
	public Long CheckUsername(String username) throws SQLException {
		QueryRunner  runner = new QueryRunner(DataSourceUtils.getDataSource()); 
		String sql = "select count(1)  from user_o where username=? ";
	 Long  a=(Long)runner.query(sql,new  ScalarHandler(),username);
		return a;
	}
	//�û���¼�ķ���
	public User login(String username, String password) throws SQLException {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource());
		String sql = "select * from user_o where username=? and password=?";
		return runner.query(sql, new BeanHandler<User>(User.class), username,password);
	}


}
