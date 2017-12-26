package com.rjxy.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.rjxy.dao.OrderDao;
import com.rjxy.domain.OrderItem;
import com.rjxy.domain.Orders;
import com.rjxy.utils.DataSourceUtils;

public class OrderService {
 /* 
  * 事物
  */
	//添加 购物车里的商品到 订单
	public  boolean  SubmitOrder(Orders orders){
		OrderDao orderDao = new OrderDao();
		try {
		//开启事物
			DataSourceUtils.startTransaction();
			//忘订单表里插入数据 状态为未支付
			 orderDao.AddOrder(orders);
			//往 订单项表里插入数据 
			 orderDao.AddOrderItem(orders);
		} catch (SQLException e) {
			try {
				DataSourceUtils.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
			return false;
		}
		try {
			DataSourceUtils.commitAndRelease();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	// 确认订单 更新 收货人信息
		public  void  updateOrderAddress(Orders orders) throws SQLException{
			OrderDao orderDao = new OrderDao();
			orderDao.updateOrderAddress(orders);
			
		
		}
		// 确定 付款  更新 状态信息
		public  void  updateOrderState(String oid) throws SQLException{
			OrderDao orderDao = new OrderDao();
			orderDao.updateOrderState(oid);
			
		
		}

		public List<Orders> findMyOrder(String uid) {
			OrderDao dao = new OrderDao();
			List<Orders> myOrder=null;
			try {
				myOrder = dao.findMyOrder(uid);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return myOrder;
		}

		public List<Map<String, Object>> findMyOrderItem(String oid) {
			OrderDao dao = new OrderDao();
			List<Map<String, Object>> orderItems=null;
		try {
		orderItems=	dao.findMyOrderItem(oid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			return orderItems;
		}
	
}
