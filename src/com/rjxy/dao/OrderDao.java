package com.rjxy.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.hibernate.sql.ordering.antlr.OrderingSpecification;

import com.rjxy.domain.Category;
import com.rjxy.domain.OrderItem;
import com.rjxy.domain.Orders;
import com.rjxy.utils.DataSourceUtils;

public class OrderDao {
    
	//向订单项表里添加 订单项
	public  void  AddOrderItem(Orders orders) throws SQLException{
		QueryRunner  runner = new QueryRunner(true); 
		Connection connection = DataSourceUtils.getConnection();
		String sql = " insert into orderitem  values (?,?,?,?,?)";
	
		for(OrderItem ordersItem: orders.getOrderItems()){
		runner.update(connection,sql, ordersItem.getItemid(),ordersItem.getCount(),ordersItem.getSubtotal(),
				ordersItem.getProduct().getPid(),ordersItem.getOrders().getOid());
	}
	}
	//向订单表插入数据
	public  void  AddOrder(Orders orders) throws SQLException{
	  	QueryRunner  runner = new QueryRunner(true); 
		Connection connection = DataSourceUtils.getConnection();
		String sql = " insert into orders values (?,?,?,?,?,?,?,?)";
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String Ordertime = format.format(orders.getOrdertime());
	 runner.update(connection,sql, orders.getOid(), Ordertime,
		  orders.getTotal(), orders.getState(), orders.getAddress(),
		  orders.getName(), orders.getTelephone(), orders.getUser().getUid());
		
		System.out.println(Ordertime);
	
	}
	// 确认订单 更新 收货人信息
	public  void  updateOrderAddress(Orders orders) throws SQLException{
		QueryRunner  runner = new QueryRunner(true); 
		Connection connection = DataSourceUtils.getConnection();
		String sql ="update orders set address=? , name=? , telephone=?  where oid=? ";
		runner.update(connection, sql, orders.getAddress(),orders.getName(),orders.getTelephone(),orders.getOid());
	}
	//确认付款  更新订单状态
	public  void  updateOrderState(String oid) throws SQLException{
		QueryRunner  runner = new QueryRunner(true); 
		Connection connection = DataSourceUtils.getConnection();
		String sql ="update orders set State=1   where oid=? ";
		runner.update(connection, sql,oid);
	}
	
	
	public  List<Orders>  findMyOrder(String uid) throws SQLException 
	{
		QueryRunner runner =new QueryRunner(DataSourceUtils.getDataSource());
		String sql ="select * from  orders  where uid=?";
		return runner.query(sql, new BeanListHandler<Orders>(Orders.class),uid);
	}

	public List<Map<String, Object>> findMyOrderItem(String oid) throws SQLException {
		QueryRunner runner =new QueryRunner(DataSourceUtils.getDataSource(),true);
		String sql ="select * from  orderitem i, product p where p.pid=i.pid and oid=?";
		List<Map<String, Object>> list = runner.query(sql, new MapListHandler(),oid);
		return list;
	}
	
	
}
