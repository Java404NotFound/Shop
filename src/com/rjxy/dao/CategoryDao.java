package com.rjxy.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.rjxy.domain.Category;

import com.rjxy.utils.DataSourceUtils;

public class CategoryDao {

	public List<Category> findCategoryList() throws SQLException {
		QueryRunner  runner = new QueryRunner(DataSourceUtils.getDataSource()); 
		String  sql ="select  * from Category   ";
		return	runner.query(sql, new BeanListHandler<Category>(Category.class));
	}

}
