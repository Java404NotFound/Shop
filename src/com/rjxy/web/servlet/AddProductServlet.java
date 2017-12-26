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
		//1�����������ļ����
		//���ã����û����ļ��Ĵ�С  ������ʱ�ļ��洢��λ��
		String path_temp = this.getServletContext().getRealPath("temp");
		//DiskFileItemFactory factory = new DiskFileItemFactory(1024*1024, new File(path_temp));
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setSizeThreshold(1024*1024);
		factory.setRepository(new File(path_temp));
		//2�������ļ��ϴ��ĺ�����
		ServletFileUpload upload = new ServletFileUpload(factory);
		//�����ϴ��ļ������Ƶı���
		upload.setHeaderEncoding("UTF-8");

		//ServletFileUpload��API
		boolean multipartContent = upload.isMultipartContent(request);//�жϱ��Ƿ����ļ��ϴ��ı�
		if(multipartContent){
			//���ļ��ϴ��ı�
			//***����request����ļ����
			List<FileItem> parseRequest = upload.parseRequest(request);
			if(parseRequest!=null){
				for(FileItem item : parseRequest){
					//�ж��ǲ���һ����ͨ����
					boolean formField = item.isFormField();
					if(formField){
						//username=zhangsan
						String fieldName = item.getFieldName();
						String fieldValue = item.getString("UTF-8");//����ͨ��������ݽ��б���
						
						System.out.println(fieldName+"----"+fieldValue);
						map.put(fieldName, fieldValue);
						//������enctype="multipart/form-data"ʱ request.getParameter��صķ���
						//String parameter = request.getParameter("username");
						
					}else{
						//�ļ��ϴ���
						//�ļ�����
						String fileName = item.getName();
						int index=fileName.lastIndexOf("\\");
						if(index!=-1){
							fileName=fileName.substring(index+1);
						}
						//����ϴ��ļ�������
						InputStream in = item.getInputStream();
						String path_store = this.getServletContext().getRealPath("upload");
						OutputStream out = new FileOutputStream(path_store+"/"+fileName);
						
						//�ϴ��ļ�������������
						IOUtils.copy(in, out);
						in.close();
						out.close();
						map.put(fileName,path_store+"/"+fileName);
						
						//ɾ����ʱ�ļ�
						item.delete();
						
					}
				}
			}

		}else{
			//�����ļ��ϴ���
			//ʹ��ԭʼ�ı����ݵĻ�÷�ʽ request.getParameter();
		}
		
		try {
			BeanUtils.populate(product, map);
			//�Ƿ�product�����װ������ȫ
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
