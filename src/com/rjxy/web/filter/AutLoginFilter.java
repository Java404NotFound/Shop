package com.rjxy.web.filter;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.rjxy.domain.User;
import com.rjxy.service.UserService;

/**
 * Servlet Filter implementation class AutLoginFilte
 */
public class AutLoginFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpSession session = req.getSession();

		Cookie[] cookies = req.getCookies();
		String username = null;
		String userpassword = null;
		String username1 = null;
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				String name = cookie.getName();
				if ("cookie_username".equals(name)) {
					username = cookie.getValue();
					// 对username进行中文解码
					username1 = URLDecoder.decode(username, "UTF-8");
				}
				if ("cookie_password".equals(cookie.getName())) {
					userpassword = cookie.getValue();
				}
			}
		}
		if (username1 != null && userpassword != null) {
			UserService service = new UserService();
			try {
				User user = service.login(username1, userpassword);
				if (user != null) {
					session.setAttribute("User", user);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 放行
		chain.doFilter(request, response);
	}
	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

	

}
