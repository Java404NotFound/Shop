package com.rjxy.domain;

public class OrderItem {
 private String itemid;//������id
 private int count;   //���������Ʒ����
 private double subtotal; //�������С��
 private Product product; //�������ڲ�����Ʒ
 private Orders orders;  //���������ڵĶ���
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
