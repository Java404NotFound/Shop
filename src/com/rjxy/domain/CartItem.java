package com.rjxy.domain;

public class CartItem {
private Product product;  //��Ʒ
private  int buyNum;      //�������� 
private double subtotal;  //С��
public Product getProduct() {
	return product;
}
public void setProduct(Product product) {
	this.product = product;
}
public int getBuyNum() {
	return buyNum;
}
public void setBuyNum(int buyNum) {
	this.buyNum = buyNum;
}
public double getSubtotal() {
	return subtotal;
}
public void setSubtotal(double subtotal) {
	this.subtotal = subtotal;
}

}