package com.rjxy.dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.rjxy.domain.Product;
import com.rjxy.utils.DataSourceUtils;

public class ProductDao {
// 获得热门商品
	public List<Product> findHotProductList() throws SQLException {
		QueryRunner  runner = new QueryRunner(DataSourceUtils.getDataSource()); 
		String  sql ="select TOP 9 * from product  where  is_hot =1  ";
		return	runner.query(sql, new BeanListHandler<Product>(Product.class));
	
	}
//获得最新商品
	public List<Product> findNewProductList() throws SQLException {
		QueryRunner  runner = new QueryRunner(DataSourceUtils.getDataSource()); 
		String  sql ="select TOP 9 * from product order by pdate desc ";
		return	runner.query(sql, new BeanListHandler<Product>(Product.class));
	
	}
//根据 cid 获得商品 总条数
	public int findProductTotalRows(String cid) throws SQLException {
		QueryRunner  runner = new QueryRunner(DataSourceUtils.getDataSource()); 
		String  sql ="select count(1) from product where cid =? ";
		Number query = (Number) runner.query(sql,new ScalarHandler(),cid);
		return query.intValue();
	}
	//根据cid  分页查询
	public List<Product> findProductListPage(String cid, int dqy, int fys) throws SQLException {
		
		QueryRunner  runner = new QueryRunner(DataSourceUtils.getDataSource()); 
		String  sql ="select TOP "+fys+" * from product where pid  not in (select TOP "+(dqy-1)*fys+"  pid from  product  where cid="+cid+" order by pdate desc)  and cid="+cid+" order by pdate desc";
		return	runner.query(sql, new BeanListHandler<Product>(Product.class));
	
	}
	//根据pid 获得详细商品信息
	  public Product findProductByPid(String pid) throws SQLException {
		  QueryRunner  runner = new QueryRunner(DataSourceUtils.getDataSource()); 
			String  sql ="select * from product where pid =? ";
			
		  return runner.query(sql,new BeanHandler<Product>(Product.class),pid);
		
	}
	  
	//获得所有商品
	public List<Product> findAllProductList() throws SQLException {
		QueryRunner  runner = new QueryRunner(DataSourceUtils.getDataSource()); 
		String  sql ="select TOP 9 * from product ";
		return	runner.query(sql, new BeanListHandler<Product>(Product.class));
	}
	public void AddProduct(Product product)throws SQLException  {
		QueryRunner runner = new QueryRunner(DataSourceUtils.getDataSource(),true);
		String sql = "insert into product values(?,?,?,?,?,?,?,?,?,?)";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String Pdate = format.format(product.getPdate());
		runner.update(sql, product.getPid(),product.getPname(),product.getMarket_price(),
				product.getShop_price(),product.getPimage(),Pdate,product.getIs_not(),product.getPdesc(),product.getPflag(),product.getCategory().getCid());
				
		
	}
	
	
}
