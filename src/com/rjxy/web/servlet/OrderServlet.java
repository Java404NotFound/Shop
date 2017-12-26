package com.rjxy.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.persistence.criteria.Order;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import com.rjxy.domain.Cart;
import com.rjxy.domain.CartItem;
import com.rjxy.domain.OrderItem;
import com.rjxy.domain.Orders;
import com.rjxy.domain.Product;
import com.rjxy.domain.User;
import com.rjxy.service.OrderService;
import com.rjxy.utils.CommonsUtils;
import com.rjxy.utils.PaymentUtil;

/**
 * Servlet implementation class OrderServlet
 */
public class OrderServlet extends BaseServlet {
	// 提交订单的方法
	public void submitOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 封装Order 对象
		 */
		// 首先 判断用户是否已登录
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("User");
		Cart cart = (Cart) session.getAttribute("cart");
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}
		Orders orders = new Orders();
		orders.setOid(CommonsUtils.getUUID());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String Pdate = format.format(new Date());
		orders.setOrdertime(Pdate);
		orders.setState(0);
		orders.setUser(user);
		orders.setTotal(cart.getTotal());

		Map<String, CartItem> cartIems = cart.getCartIems();
		// 将session域里的 Cart 对象 for循环封装到 OrderItem
		for (Map.Entry<String, CartItem> e : cartIems.entrySet()) {
			CartItem cartItem = e.getValue();

			OrderItem orderItem = new OrderItem();
			orderItem.setItemid(CommonsUtils.getUUID());
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setSubtotal(cartItem.getSubtotal());
			orderItem.setCount(cartItem.getBuyNum());

			orderItem.setOrders(orders);

			orders.getOrderItems().add(orderItem);

		}

		OrderService orderService = new OrderService();
		boolean param = orderService.SubmitOrder(orders);
		if (param) {
			session.setAttribute("order", orders);
			response.sendRedirect(request.getContextPath() + "/order_info.jsp");
		}
	}

	/*
	 * 
	 * 确认订单 的方法 获得地址 + 在线支付
	 * 
	 */
	public void confirmOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 更新收货人信息
		Map<String, String[]> map = request.getParameterMap();
		Orders orders = new Orders();
		try {
			BeanUtils.populate(orders, map);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OrderService oService = new OrderService();
		try {
			oService.updateOrderAddress(orders);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 在线支付
		// 获得选择的银行
		String pd_frpId = request.getParameter("pd_FrpId");
		if ("ABC-NET-B2C".equals(pd_frpId)) {
			// 接入银行接口
		}
		// 只接入一个接口 第三方支付平台接口提供的
		// 接入易宝接口
		// 获得 支付必须基本数据
		String orderid = orders.getOid();
		String money = "0.01";
		// 银行
		String pd_FrpId = request.getParameter("pd_FrpId");

		// 发给支付公司需要哪些数据
		String p0_Cmd = "Buy";
		String p1_MerId = ResourceBundle.getBundle("merchantInfo").getString("p1_MerId");
		String p2_Order = orderid;
		String p3_Amt = money;
		String p4_Cur = "CNY";
		String p5_Pid = "";
		String p6_Pcat = "";
		String p7_Pdesc = "";
		// 支付成功回调地址 ---- 第三方支付公司会访问、用户访问
		// 第三方支付可以访问网址
		String p8_Url = ResourceBundle.getBundle("merchantInfo").getString("callback");
		String p9_SAF = "";
		String pa_MP = "";
		String pr_NeedResponse = "1";
		// 加密hmac 需要密钥
		String keyValue = ResourceBundle.getBundle("merchantInfo").getString("keyValue");
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc,
				p8_Url, p9_SAF, pa_MP, pd_FrpId, pr_NeedResponse, keyValue);

		String url = "https://www.yeepay.com/app-merchant-proxy/node?pd_FrpId=" + pd_FrpId + "&p0_Cmd=" + p0_Cmd
				+ "&p1_MerId=" + p1_MerId + "&p2_Order=" + p2_Order + "&p3_Amt=" + p3_Amt + "&p4_Cur=" + p4_Cur
				+ "&p5_Pid=" + p5_Pid + "&p6_Pcat=" + p6_Pcat + "&p7_Pdesc=" + p7_Pdesc + "&p8_Url=" + p8_Url
				+ "&p9_SAF=" + p9_SAF + "&pa_MP=" + pa_MP + "&pr_NeedResponse=" + pr_NeedResponse + "&hmac=" + hmac;

		// 重定向到第三方支付平台
		response.sendRedirect(url);
	}

	// 查看我的订单
	public void FindAllOrder(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		/*
		 * 封装Order 对象
		 */
		// 首先 判断用户是否已登录
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("User");
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/login.jsp");
			return;
		}
		// 根据用户ID 获得 order
		OrderService service = new OrderService();
		List<Orders> orderList = service.findMyOrder(user.getUid());
		if (orderList != null) {
			for (Orders o : orderList) {
				// maplist 里存的是 product表和 orderItem表 里两者需要的 数据
				List<Map<String, Object>> maplist = service.findMyOrderItem(o.getOid());
				for (Map<String, Object> oMap : maplist) {
					try {
						// 将maplist里orderItem的数据 提取并封装
						OrderItem orderItem = new OrderItem();
						BeanUtils.populate(orderItem, oMap);
						// 将maplist里product的数据 提取并封装
						Product product = new Product();
						BeanUtils.populate(product, oMap);
						orderItem.setProduct(product);
						o.getOrderItems().add(orderItem);

					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

		}

		request.setAttribute("myorder", orderList);
		request.getRequestDispatcher("/order_list.jsp").forward(request, response);
	}

}
