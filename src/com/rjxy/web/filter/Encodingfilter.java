package com.rjxy.web.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Servlet Filter implementation class Encodingfilter
 */
public class Encodingfilter implements Filter {

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
	
		
		
		/*
		 * 
		 * 动态代理！！！！！！！！！！！！！！！！！
		 *     
		 *     
		 * 
		 */
		request.setCharacterEncoding("UTF-8");
		final HttpServletRequest res = (HttpServletRequest) request;
		HttpServletRequest  EnhanceRequst=(HttpServletRequest) Proxy.newProxyInstance(res.getClass().getClassLoader(), res.getClass().getInterfaces(),
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						String name = method.getName();// 获得目标对象的名称
						if ("getParameter".equals(name)) {
                               String invoke = (String) method.invoke(res, args);//执行的就是 res.getParameter //是乱码
                               invoke=new String(invoke.getBytes("iso8859-1"), "UTF-8");
                           	return invoke;
						}
						return method.invoke(res, args);
					}
				});

		/*
		 * EnhanceRequst enhanceRequst= new EnhanceRequst(res);
		 * enhanceRequst.getParameter("username");
		 */

		chain.doFilter(EnhanceRequst, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	public Encodingfilter() {
		// TODO Auto-generated constructor stub
	}

	public void destroy() {
		// TODO Auto-generated method stub
	}

	class EnhanceRequst extends HttpServletRequestWrapper {
		HttpServletRequest request;

		public EnhanceRequst(HttpServletRequest request) {
			super(request);
			this.request = request;
		}

		@Override
		public String getParameter(String name) {
			String name2 = request.getParameter(name); // 这里接收的是一个乱码
			// 将乱码解决
			String a = null;
			try {
				a = new String(name2.getBytes("iso8859-1"), "UTF-8");

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return a;
		}

	}
}
