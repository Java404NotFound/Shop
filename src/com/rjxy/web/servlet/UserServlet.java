package com.rjxy.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import com.rjxy.domain.User;
import com.rjxy.service.UserService;
import com.rjxy.utils.CommonsUtils;
import com.rjxy.utils.MailUtils;

/**
 * Servlet implementation class UserServlet
 */
public class UserServlet extends  BaseServlet {
	public void ActiveUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException { 
		   // 获得激活码
				String activeCode = request.getParameter("activeCode");        
				//调用激活码验证服务
				UserService  userservice = new UserService();
				try {
					userservice.active(activeCode);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			// 验证成功 跳转到 登录页
				response.sendRedirect(request.getContextPath()+"/login.jsp");
				
	
	}
	public void addUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	    request.setCharacterEncoding("UTF-8");
	     // 获得表单数据
	    Map<String, String[]>  proer = request.getParameterMap();
	    User  user = new User();
	   /* 
	    	//自己制定类型转换器
	    	ConvertUtils.register(new Converter() {
				@Override
				public Object convert(Class arg0, Object value) {
					//将String转成date
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				    Date parse=null;
					try {
						parse = format.parse(value.toString());
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
							return parse;
				}
			}, Date.class);
			*/
	    //将表单数据封装到 user 对象
	    try {
	    BeanUtils.populate(user, proer);	
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	  //--------补充 user---------
	    
	    //private int uid;
	    user.setUid(CommonsUtils.getUUID());
	    //priavte  String telephine
	    user.setTelephone("11111111");
	    //激活状态
	    user.setState(0);
	    //激活码
	    String activeCode= CommonsUtils.getUUID();
	    user.setCode(activeCode);
	    
	  //-------------------------------
	    
	    // 调用服务层
	     UserService service = new  UserService();
	    boolean isRegist= service.regist(user);
	    
	    if(isRegist){
	    //  发送激活邮件
	   String emailMsg ="恭喜你注册成功， 请点击下面的连接进行激活账户<a href='http://localhost:8080/Shop/user?methodName=ActiveUser&activeCode="+activeCode+"'>http://localhost:8080/Shop/user?methodName=ActiveUser&activeCode="+activeCode+"</a>'";
	   try {
		MailUtils.sendMail(user.getEmail(), emailMsg);
	} catch (AddressException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	     // 跳转到注册成功页面
	    	response.sendRedirect(request.getContextPath()+"/registerSuccess.jsp");
	    }else{
	     // 跳转到注册失败页面
	    	response.sendRedirect(request.getContextPath()+"/registerFail.jsp");
	    }
		}
	
	//检查注册用户名是否已存在
	public void CheckUsername(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		   String username = request.getParameter("username");
   		
			UserService  userservice = new UserService();
	      try {
			boolean path=  userservice.CheckUsername(username);
			
			String join ="\"isExist\" :" +path+"";
		      response.getWriter().write(join);	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	//用户登录
		public void login(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			HttpSession session = request.getSession();
			//获得输入的用户名和密码
			String username = request.getParameter("username");
			String password = request.getParameter("password");

			//对密码进行加密
			//password = MD5Utils.md5(password);

			//将用户名和密码传递给service层
			UserService service = new UserService();
			User user = null;
			try {
				user = service.login(username,password);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			//判断用户是否登录成功 user是否是null
			if(user!=null){
				//登录成功
				//***************判断用户是否勾选了自动登录*****************
				String autoLogin = request.getParameter("autoLogin");
				if("true".equals(autoLogin)){
					//要自动登录
					//创建存储用户名的cookie
					 //因为cookie不支持中文存入，b必须将中文转码后再将其存入
			             String name = URLEncoder.encode(user.getUsername(),"UTF-8");
					Cookie cookie_username = new Cookie("cookie_username",name);
					cookie_username.setMaxAge(10*60);
					//创建存储密码的cookie
					Cookie cookie_password = new Cookie("cookie_password",user.getPassword());
					cookie_password.setMaxAge(10*60);
					//设置cookie的携带路径
                    cookie_password.setPath(request.getContextPath());
                    cookie_username.setPath(request.getContextPath());
					
                     //发送cookie
                    response.addCookie(cookie_username);
					response.addCookie(cookie_password);

				}
				

				//***************************************************
				//将user对象存到session中
				session.setAttribute("User", user);

				//重定向到首页
				response.sendRedirect(request.getContextPath()+"/index.jsp");
			}else{
				request.setAttribute("loginError", "用户名或密码错误");
				request.getRequestDispatcher("/login.jsp").forward(request, response);
			}
		}

}
