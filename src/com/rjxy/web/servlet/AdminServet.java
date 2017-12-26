package com.rjxy.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.rjxy.domain.Category;
import com.rjxy.domain.Product;
import com.rjxy.service.CategoryService;
import com.rjxy.service.ProductService;

/**
 * Servlet implementation class AdminServet
 */
public class AdminServet extends BaseServlet {

	//获得商品总信息
	public void   findAllProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ProductService  pService = new ProductService();
		
		List<Product> productList = pService.findAllProductList();
		
		request.setAttribute("plist", productList);
		request.getRequestDispatcher("/admin/product/list.jsp").forward(request, response);
		
		
		
	}
	
	
	
	//在add.jsp页 异步获得分类数据
	public void findCatge(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CategoryService c = new CategoryService();
		List<Category> categoryList = c.findCategoryList();
		Gson gson = new Gson();
		response.setContentType("");
		response.setContentType("text/html;charset=UTF-8");
		String json = gson.toJson(categoryList);
		response.getWriter().write(json);
	}
}
