package com.rjxy.web.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.rjxy.domain.Category;
import com.rjxy.domain.Product;
import com.rjxy.service.ProductService;
import com.rjxy.utils.CommonsUtils;

/**
 * Servlet implementation class AddProductServlet
 */
public class AddProductServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Map<String, Object> map= new HashMap<String, Object>();
		Product product = new Product();
		try{
		//1、创建磁盘文件项工厂
		//作用：设置缓存文件的大小  设置临时文件存储的位置
		String path_temp = this.getServletContext().getRealPath("temp");
		//DiskFileItemFactory factory = new DiskFileItemFactory(1024*1024, new File(path_temp));
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(1024*1024);
		factory.setRepository(new File(path_temp));
		//2、创建文件上传的核心类
		ServletFileUpload upload = new ServletFileUpload(factory);
		//设置上传文件的名称的编码
		upload.setHeaderEncoding("UTF-8");

		//ServletFileUpload的API
		boolean multipartContent = upload.isMultipartContent(request);//判断表单是否是文件上传的表单
		if(multipartContent){
			//是文件上传的表单
			//***解析request获得文件项集合
			List<FileItem> parseRequest = upload.parseRequest(request);
			if(parseRequest!=null){
				for(FileItem item : parseRequest){
					//判断是不是一个普通表单项
					boolean formField = item.isFormField();
					if(formField){
						//username=zhangsan
						String fieldName = item.getFieldName();
						String fieldValue = item.getString("UTF-8");//对普通表单项的内容进行编码
						
						System.out.println(fieldName+"----"+fieldValue);
						map.put(fieldName, fieldValue);
						//当表单是enctype="multipart/form-data"时 request.getParameter相关的方法
						//String parameter = request.getParameter("username");
						
					}else{
						//文件上传项
						//文件的名
						String fileName = item.getName();
						int index=fileName.lastIndexOf("\\");
						if(index!=-1){
							fileName=fileName.substring(index+1);
						}
						//获得上传文件的内容
						InputStream in = item.getInputStream();
						String path_store = this.getServletContext().getRealPath("upload");
						OutputStream out = new FileOutputStream(path_store+"/"+fileName);
						
						//上传文件到服务器磁盘
						IOUtils.copy(in, out);
						in.close();
						out.close();
						map.put(fileName,path_store+"/"+fileName);
						
						//删除临时文件
						item.delete();
						
					}
				}
			}

		}else{
			//不是文件上传表单
			//使用原始的表单数据的获得方式 request.getParameter();
		}
		
		try {
			BeanUtils.populate(product, map);
			//是否product对象封装数据完全
			//private String pid;
			product.setPid(CommonsUtils.getUUID());
			//private Date pdate;
			product.setPdate(new Date());
			//private int pflag;
			product.setPflag(0);
			//private Category category;
			Category category = new Category();
			System.out.println(map.get("cid").toString());
			category.setCid(map.get("cid").toString());
			product.setCategory(category);
			ProductService productService =new ProductService();
			productService.AddProduct(product);;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} catch (FileUploadException e) {
		e.printStackTrace();
	}
		
		
		response.sendRedirect(request.getContextPath()+"/admin/product/add.jsp");

	}


	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
