package com.rjxy.domain;


import java.util.HashMap;
import java.util.Map;

public class Cart {
	//���ﳵ����
  private Map<String,CartItem>  cartIems =new HashMap<String,CartItem>();
  
  //�ܼ�
  private double total;
  
public double getTotal() {
	return total;
}

public void setTotal(double total) {
	this.total = total;
}

public Map<String, CartItem> getCartIems() {
	return cartIems;
}

public void setCartIems(Map<String, CartItem> cartIems) {
	this.cartIems = cartIems;
}
}
