package com.rjxy.domain;

public class OrderItem {
 private String itemid;//订单项id
 private int count;   //订单项的商品数量
 private double subtotal; //订单项的小计
 private Product product; //订单项内部的商品
 private Orders orders;  //订单项属于的订单
public String getItemid() {
	return itemid;
}
public void setItemid(String itemid) {
	this.itemid = itemid;
}
public int getCount() {
	return count;
}
public void setCount(int count) {
	this.count = count;
}
public double getSubtotal() {
	return subtotal;
}
public void setSubtotal(double subtotal) {
	this.subtotal = subtotal;
}
public Product getProduct() {
	return product;
}
public void setProduct(Product product) {
	this.product = product;
}
public Orders getOrders() {
	return orders;
}
public void setOrders(Orders orders) {
	this.orders = orders;
}
 
}
