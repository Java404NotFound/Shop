package com.rjxy.web.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.rjxy.domain.Cart;
import com.rjxy.domain.CartItem;
import com.rjxy.domain.Category;
import com.rjxy.domain.PageBean;
import com.rjxy.domain.Product;
import com.rjxy.service.CategoryService;
import com.rjxy.service.ProductService;


public class ProductServlet extends BaseServlet {


//	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//             String methodName = request.getParameter("methodName");
//	    if("productList".equals(methodName)){
//	    	productList(request,response);
//	    }else if ("categoryList".equals(methodName)){
//	    	categoryList(request,response);
//	    }else if ("index".equals(methodName)){
//	    	index(request,response);
//	    }else if ("product_info".equals(methodName)){
//	    	product_info(request,response);
//	    }
//	
//	}
//
//
//	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		doGet(request, response);
//	}

	
/*
 *
 * ģ���еĹ���ͬ������������
 * 
 */
	
	//�����Ʒ�����ﳵ�ķ���
	public  void   AddProdctCart(HttpServletRequest request, HttpServletResponse response){
	   String pid =request.getParameter("pid");
	   int  buyNum=Integer.parseInt(request.getParameter("buyNum"));
	   ProductService productService = new ProductService();
	   Product product = productService.findProductByPid(pid);
	   CartItem cartItem= new CartItem();
	   cartItem.setProduct(product);
	   cartItem.setBuyNum(buyNum);
	   double subtotal=buyNum*product.getShop_price();
	   cartItem.setSubtotal(subtotal);
	   

	   HttpSession session = request.getSession();
	   Cart cart = (Cart) session.getAttribute("cart");
	   if(cart==null){
		    cart = new Cart();
	   }
      //�жϹ������Ƿ��Ѱ������ﳵ -- �ж�key�Ƿ��Ѵ���
	  //������ﳵ���Ѵ�����Ʒ -----����������ǰ���������
	    Map<String, CartItem> cartIems = cart.getCartIems();
	    double  newsubtotal =0.0;
	   if(cartIems.containsKey(pid)){ //containsKey ���hashMap�е�key�Ƿ����
		   CartItem cartItem2 = cartIems.get(pid);
		int onbuyNum= cartItem2.getBuyNum();
		   onbuyNum+=buyNum;
		cartItem2.setBuyNum(onbuyNum);
		cart.setCartIems(cartIems);
		//�޸�С��
		//ԭ����Ʒ��С��
		double oldsubtotal= cartItem2.getSubtotal();
		//�µ���Ʒ��С��
		newsubtotal=buyNum*product.getShop_price();
		cartItem2.setSubtotal(oldsubtotal+newsubtotal);
		   
	   }else{
		   //���û�� ֱ�ӷ��빺�ﳵ
		   cart.getCartIems().put(pid, cartItem);
		   newsubtotal=buyNum*product.getShop_price();
	   }
	 //�����ܼ�
	   double total=cart.getTotal()+newsubtotal;
	   cart.setTotal(total);
	  
	   //�����ﳵ�ٴη���session
	   session.setAttribute("cart", cart);
       
	   //�ض���cart
	   try {
		response.sendRedirect(request.getContextPath()+"/cart.jsp");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	//ɾ�����ﳵ�еĹ�����
	public void clearCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
           
               HttpSession session = request.getSession();
                 session.removeAttribute("cart");
              response.sendRedirect(request.getContextPath()+"/cart.jsp");
	}
	
	
	
	//ɾ�����ﳵ�еĹ�����
	public void delProFromCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
               String pid = request.getParameter("pid");
               HttpSession session = request.getSession();
               Cart  cart= (Cart) session.getAttribute("cart");
               Map<String, CartItem> cartIems = cart.getCartIems();
             //�޸��ܼ�
               cart.setTotal(cart.getTotal()-cartIems.get(pid).getSubtotal());
              //�Ƴ�map�������Ӧkey
               cartIems.remove(pid);
               
               
               session.setAttribute("cart", cart);
              response.sendRedirect(request.getContextPath()+"/cart.jsp");
	}
	
	
//��ʾ��Ʒ����� �� ����
	public void categoryList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CategoryService  cService = new CategoryService();
		//�Ȼ����в�ѯcategoryList  �����ֱ��ʹ�ã�û�оʹ����ݿ��ѯ
	  /*Jedis jedis = JedisPoolUtils.getJedis();
		 categoryListJson = jedis.get("categoryListJson");
		 if(categoryListJson==null  ){	
				}
        	*/
	  //׼����������
	        List<Category> cList = cService.findCategoryList();
			Gson gson = new Gson();
			String	categoryListJson = gson.toJson(cList);
		//	jedis.set("categoryListJson", categoryListJson);
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(categoryListJson);
	}
	
 //��ʾ��ҳ�Ĺ���  �ֱ� �ṩ ������Ʒ��������Ʒ	
	public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//׼��������Ʒ
		ProductService  service = new ProductService();
	List<Product> HotProduct=	service.findHotProductList();
		//׼�� ������Ʒ
	List<Product> NewProduct=	service.findNewProductList();
	request.setAttribute("hotProduct", HotProduct);
	request.setAttribute("NewProduct", NewProduct);

	request.getRequestDispatcher("/index.jsp").forward(request, response);
	}
	
//������Ʒ����� ��ʾ��Ʒ���б�
	public void  productList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String cid = request.getParameter("cid");    
	        ProductService  productService= new ProductService();
	        String parameter = request.getParameter("currentPage");
	        if(parameter==null){
	        parameter="1";
	        }
	        int dqy=Integer.parseInt(parameter);
		     
		     int fys= 12;
	        
				//����һ����¼��ʷ��Ʒ��Ϣ�ļ���
				List<Product> historyProductList = new ArrayList<Product>();
				
				//��ÿͻ���Я�����ֽ�pids��cookie
				Cookie[] cookies = request.getCookies();
				if(cookies!=null){
					for(Cookie cookie:cookies){
						if("pids".equals(cookie.getName())){
							String pids = cookie.getValue();//3-2-1
							String[] split = pids.split("-");
							for(String pid : split){
								Product pro = productService.findProductByPid(pid);
								historyProductList.add(pro);
							}
						}
					}
				}
				
				//����ʷ��¼�ļ��Ϸŵ�����
				request.setAttribute("historyProductList", historyProductList);
		   
			PageBean<Product> PPageBean = productService.findProductListBycid(cid,dqy,fys);		
			request.setAttribute("pageBean", PPageBean);
			request.setAttribute("cid", cid);
			request.getRequestDispatcher("product_list.jsp").forward(request, response);
	}
	
//������Ʒ��ID ��ʾ��Ʒ��ϸ��Ϣ
	public void product_info(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String cid = request.getParameter("cid");
         String currentPage = request.getParameter("currentPage");
         String pid = request.getParameter("pid");  
       //��ÿͻ���Я��cookie---���������pids��cookie
 		String pids = pid;
 		Cookie[] cookies = request.getCookies();
 		if(cookies!=null){
 			for(Cookie cookie : cookies){
 				if("pids".equals(cookie.getName())){
 					pids = cookie.getValue();
 					//1-3-2 ���η�����Ʒpid��8----->8-1-3-2
 					//1-3-2 ���η�����Ʒpid��3----->3-1-2
 					//1-3-2 ���η�����Ʒpid��2----->2-1-3
 					//��pids���һ������
 					String[] split = pids.split("-");//{3,1,2}
 					List<String> asList = Arrays.asList(split);//[3,1,2]
 					LinkedList<String> list = new LinkedList<String>(asList);//[3,1,2]
 					//�жϼ������Ƿ���ڵ�ǰpid
 					if(list.contains(pid)){
 						//������ǰ�鿴��Ʒ��pid
 						list.remove(pid);
 						list.addFirst(pid);
 					}else{
 						//��������ǰ�鿴��Ʒ��pid ֱ�ӽ���pid�ŵ�ͷ��
 						list.addFirst(pid);
 					}
 					//��[3,1,2]ת��3-1-2�ַ���
 					StringBuffer sb = new StringBuffer();
 					for(int i=0;i<list.size()&&i<7;i++){
 						sb.append(list.get(i));
 						sb.append("-");//3-1-2-
 					}
 					//ȥ��3-1-2-���-
 					pids = sb.substring(0, sb.length()-1);
 				}
 			}
 		}
 		Cookie cookie_pids = new Cookie("pids",pids);
 		response.addCookie(cookie_pids);
         
         ProductService productService = new ProductService();
         Product product = productService.findProductByPid(pid);             
         request.setAttribute("product", product);
         request.setAttribute("cid", cid);
         request.setAttribute("currentPage", currentPage);
         
         request.getRequestDispatcher("/product_info.jsp").forward(request, response);
         	}	
}
