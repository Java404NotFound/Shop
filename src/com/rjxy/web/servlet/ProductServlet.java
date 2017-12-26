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
 * 模块中的功能同方法进行区分
 * 
 */
	
	//添加商品到购物车的方法
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
      //判断购物项是否已包含购物车 -- 判断key是否已存在
	  //如果购物车中已存在商品 -----将数量跟以前的数量相加
	    Map<String, CartItem> cartIems = cart.getCartIems();
	    double  newsubtotal =0.0;
	   if(cartIems.containsKey(pid)){ //containsKey 检查hashMap中的key是否存在
		   CartItem cartItem2 = cartIems.get(pid);
		int onbuyNum= cartItem2.getBuyNum();
		   onbuyNum+=buyNum;
		cartItem2.setBuyNum(onbuyNum);
		cart.setCartIems(cartIems);
		//修改小计
		//原来商品的小计
		double oldsubtotal= cartItem2.getSubtotal();
		//新的商品的小计
		newsubtotal=buyNum*product.getShop_price();
		cartItem2.setSubtotal(oldsubtotal+newsubtotal);
		   
	   }else{
		   //如果没有 直接放入购物车
		   cart.getCartIems().put(pid, cartItem);
		   newsubtotal=buyNum*product.getShop_price();
	   }
	 //计算总计
	   double total=cart.getTotal()+newsubtotal;
	   cart.setTotal(total);
	  
	   //将购物车再次放入session
	   session.setAttribute("cart", cart);
       
	   //重定向到cart
	   try {
		response.sendRedirect(request.getContextPath()+"/cart.jsp");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
	//删除购物车中的购物项
	public void clearCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
           
               HttpSession session = request.getSession();
                 session.removeAttribute("cart");
              response.sendRedirect(request.getContextPath()+"/cart.jsp");
	}
	
	
	
	//删除购物车中的购物项
	public void delProFromCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
               String pid = request.getParameter("pid");
               HttpSession session = request.getSession();
               Cart  cart= (Cart) session.getAttribute("cart");
               Map<String, CartItem> cartIems = cart.getCartIems();
             //修改总价
               cart.setTotal(cart.getTotal()-cartIems.get(pid).getSubtotal());
              //移出map集合里对应key
               cartIems.remove(pid);
               
               
               session.setAttribute("cart", cart);
              response.sendRedirect(request.getContextPath()+"/cart.jsp");
	}
	
	
//显示商品的类别 的 功能
	public void categoryList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CategoryService  cService = new CategoryService();
		//先缓存中查询categoryList  如果有直接使用，没有就从数据库查询
	  /*Jedis jedis = JedisPoolUtils.getJedis();
		 categoryListJson = jedis.get("categoryListJson");
		 if(categoryListJson==null  ){	
				}
        	*/
	  //准备分类数据
	        List<Category> cList = cService.findCategoryList();
			Gson gson = new Gson();
			String	categoryListJson = gson.toJson(cList);
		//	jedis.set("categoryListJson", categoryListJson);
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(categoryListJson);
	}
	
 //显示首页的功能  分别 提供 最新商品和最热商品	
	public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//准备热门商品
		ProductService  service = new ProductService();
	List<Product> HotProduct=	service.findHotProductList();
		//准备 最新商品
	List<Product> NewProduct=	service.findNewProductList();
	request.setAttribute("hotProduct", HotProduct);
	request.setAttribute("NewProduct", NewProduct);

	request.getRequestDispatcher("/index.jsp").forward(request, response);
	}
	
//根据商品的类别 显示商品的列表
	public void  productList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String cid = request.getParameter("cid");    
	        ProductService  productService= new ProductService();
	        String parameter = request.getParameter("currentPage");
	        if(parameter==null){
	        parameter="1";
	        }
	        int dqy=Integer.parseInt(parameter);
		     
		     int fys= 12;
	        
				//定义一个记录历史商品信息的集合
				List<Product> historyProductList = new ArrayList<Product>();
				
				//获得客户端携带名字叫pids的cookie
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
				
				//将历史记录的集合放到域中
				request.setAttribute("historyProductList", historyProductList);
		   
			PageBean<Product> PPageBean = productService.findProductListBycid(cid,dqy,fys);		
			request.setAttribute("pageBean", PPageBean);
			request.setAttribute("cid", cid);
			request.getRequestDispatcher("product_list.jsp").forward(request, response);
	}
	
//根据商品的ID 显示商品详细信息
	public void product_info(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String cid = request.getParameter("cid");
         String currentPage = request.getParameter("currentPage");
         String pid = request.getParameter("pid");  
       //获得客户端携带cookie---获得名字是pids的cookie
 		String pids = pid;
 		Cookie[] cookies = request.getCookies();
 		if(cookies!=null){
 			for(Cookie cookie : cookies){
 				if("pids".equals(cookie.getName())){
 					pids = cookie.getValue();
 					//1-3-2 本次访问商品pid是8----->8-1-3-2
 					//1-3-2 本次访问商品pid是3----->3-1-2
 					//1-3-2 本次访问商品pid是2----->2-1-3
 					//将pids拆成一个数组
 					String[] split = pids.split("-");//{3,1,2}
 					List<String> asList = Arrays.asList(split);//[3,1,2]
 					LinkedList<String> list = new LinkedList<String>(asList);//[3,1,2]
 					//判断集合中是否存在当前pid
 					if(list.contains(pid)){
 						//包含当前查看商品的pid
 						list.remove(pid);
 						list.addFirst(pid);
 					}else{
 						//不包含当前查看商品的pid 直接将该pid放到头上
 						list.addFirst(pid);
 					}
 					//将[3,1,2]转成3-1-2字符串
 					StringBuffer sb = new StringBuffer();
 					for(int i=0;i<list.size()&&i<7;i++){
 						sb.append(list.get(i));
 						sb.append("-");//3-1-2-
 					}
 					//去掉3-1-2-后的-
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
