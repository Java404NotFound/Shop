package com.rjxy.service;

import java.sql.SQLException;
import java.util.List;

import com.rjxy.dao.ProductDao;
import com.rjxy.domain.PageBean;
import com.rjxy.domain.Product;

public class ProductService {
	//���������Ʒ
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
	
    // ���������Ʒ
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
   //���������Ʒ
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
	  // ���������
		ProductDao dao = new ProductDao();
		PageBean<Product>  pageBean= new PageBean<Product >();

	     List<Product> productList=null;
		try {
			//��ǰҳ
			pageBean.setCurrentPage(dqy);
			//��ҳ��
			pageBean.setCurrentCount(fys);
			//������
			int totalRows= dao.findProductTotalRows(cid);
			pageBean.setTotalCount(totalRows);
			 int b=(int) Math.ceil(1.0*totalRows/fys) ;
			//��ҳ��
			pageBean.setTotalPage(b);
			//��ҳ��ѯ������
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
