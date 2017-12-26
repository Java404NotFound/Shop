package com.rjxy.service;

import java.sql.SQLException;
import java.util.List;

import com.rjxy.dao.CategoryDao;
import com.rjxy.domain.Category;

public class CategoryService {
   
	public List<Category> findCategoryList(){
		CategoryDao dao =new CategoryDao();
		List<Category> clist =null;
		try {
			clist= dao.findCategoryList();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return clist;
	}
}
