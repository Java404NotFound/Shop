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
		   // ��ü�����
				String activeCode = request.getParameter("activeCode");        
				//���ü�������֤����
				UserService  userservice = new UserService();
				try {
					userservice.active(activeCode);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			// ��֤�ɹ� ��ת�� ��¼ҳ
				response.sendRedirect(request.getContextPath()+"/login.jsp");
				
	
	}
	public void addUser(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	    request.setCharacterEncoding("UTF-8");
	     // ��ñ�����
	    Map<String, String[]>  proer = request.getParameterMap();
	    User  user = new User();
	   /* 
	    	//�Լ��ƶ�����ת����
	    	ConvertUtils.register(new Converter() {
				@Override
				public Object convert(Class arg0, Object value) {
					//��Stringת��date
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
	    //�������ݷ�װ�� user ����
	    try {
	    BeanUtils.populate(user, proer);	
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	  //--------���� user---------
	    
	    //private int uid;
	    user.setUid(CommonsUtils.getUUID());
	    //priavte  String telephine
	    user.setTelephone("11111111");
	    //����״̬
	    user.setState(0);
	    //������
	    String activeCode= CommonsUtils.getUUID();
	    user.setCode(activeCode);
	    
	  //-------------------------------
	    
	    // ���÷����
	     UserService service = new  UserService();
	    boolean isRegist= service.regist(user);
	    
	    if(isRegist){
	    //  ���ͼ����ʼ�
	   String emailMsg ="��ϲ��ע��ɹ��� ������������ӽ��м����˻�<a href='http://localhost:8080/Shop/user?methodName=ActiveUser&activeCode="+activeCode+"'>http://localhost:8080/Shop/user?methodName=ActiveUser&activeCode="+activeCode+"</a>'";
	   try {
		MailUtils.sendMail(user.getEmail(), emailMsg);
	} catch (AddressException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (MessagingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	     // ��ת��ע��ɹ�ҳ��
	    	response.sendRedirect(request.getContextPath()+"/registerSuccess.jsp");
	    }else{
	     // ��ת��ע��ʧ��ҳ��
	    	response.sendRedirect(request.getContextPath()+"/registerFail.jsp");
	    }
		}
	
	//���ע���û����Ƿ��Ѵ���
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

	//�û���¼
		public void login(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			HttpSession session = request.getSession();
			//���������û���������
			String username = request.getParameter("username");
			String password = request.getParameter("password");

			//��������м���
			//password = MD5Utils.md5(password);

			//���û��������봫�ݸ�service��
			UserService service = new UserService();
			User user = null;
			try {
				user = service.login(username,password);
			} catch (SQLException e) {
				e.printStackTrace();
			}

			//�ж��û��Ƿ��¼�ɹ� user�Ƿ���null
			if(user!=null){
				//��¼�ɹ�
				//***************�ж��û��Ƿ�ѡ���Զ���¼*****************
				String autoLogin = request.getParameter("autoLogin");
				if("true".equals(autoLogin)){
					//Ҫ�Զ���¼
					//�����洢�û�����cookie
					 //��Ϊcookie��֧�����Ĵ��룬b���뽫����ת����ٽ������
			             String name = URLEncoder.encode(user.getUsername(),"UTF-8");
					Cookie cookie_username = new Cookie("cookie_username",name);
					cookie_username.setMaxAge(10*60);
					//�����洢�����cookie
					Cookie cookie_password = new Cookie("cookie_password",user.getPassword());
					cookie_password.setMaxAge(10*60);
					//����cookie��Я��·��
                    cookie_password.setPath(request.getContextPath());
                    cookie_username.setPath(request.getContextPath());
					
                     //����cookie
                    response.addCookie(cookie_username);
					response.addCookie(cookie_password);

				}
				

				//***************************************************
				//��user����浽session��
				session.setAttribute("User", user);

				//�ض�����ҳ
				response.sendRedirect(request.getContextPath()+"/index.jsp");
			}else{
				request.setAttribute("loginError", "�û������������");
				request.getRequestDispatcher("/login.jsp").forward(request, response);
			}
		}

}
