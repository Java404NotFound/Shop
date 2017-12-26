package com.rjxy.service;

import java.sql.SQLException;
import java.util.List;

import com.rjxy.dao.ProductDao;
import com.rjxy.domain.PageBean;
import com.rjxy.domain.Product;

public class ProductService {
	//获得所有商品
	public List<Product> findAllProductList() {
	  ProductDao dao= new ProductDao();
	  List<Product> list=null;
	  try {
		  list=  dao.findAllProductList();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
		
		return list;
	}
	
    // 获得热门商品
	public List<Product> findHotProductList() {
		 ProductDao  dao = new ProductDao();
		 List<Product> pList =null;
		try {
			pList = dao.findHotProductList();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pList ;
	}
   //获得最新商品
	public List<Product> findNewProductList() {
		 ProductDao  dao = new ProductDao();
		 List<Product> pList=null;
		try {
			pList = dao.findNewProductList();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 
		return pList;
	}
	
	public  PageBean<Product>  findProductListBycid( String cid, int dqy, int fys ){
	  // 获得总行数
		ProductDao dao = new ProductDao();
		PageBean<Product>  pageBean= new PageBean<Product >();

	     List<Product> productList=null;
		try {
			//当前页
			pageBean.setCurrentPage(dqy);
			//分页数
			pageBean.setCurrentCount(fys);
			//总条数
			int totalRows= dao.findProductTotalRows(cid);
			pageBean.setTotalCount(totalRows);
			 int b=(int) Math.ceil(1.0*totalRows/fys) ;
			//总页数
			pageBean.setTotalPage(b);
			//分页查询的数据
			 dao.findProductListPage(cid,dqy,fys);
			 productList=dao.findProductListPage(cid, dqy, fys);
			pageBean.setProductList(productList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pageBean;
		
	}
  public Product findProductByPid(String pid) {
	  ProductDao dao= new ProductDao();
	  Product product=null;
	  try {
		  product=dao.findProductByPid(pid);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
	  return product;
	
}

public void AddProduct(Product product) {
	  ProductDao dao= new ProductDao();
	  try {
		dao.AddProduct(product);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
}




}
