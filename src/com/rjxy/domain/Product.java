package com.rjxy.domain;

import java.util.Date;

public class Product {
	/*[pid] [varchar](32) NOT NULL,
	[pname] [varchar](50) NULL,
	[market_price] [float] NULL,
	[shop_price] [float] NULL,
	[pimage] [varchar](200) NULL,
	[pdate] [date] NULL,
	[is_hot] [int] NULL,
	[pdesc] [varchar](255) NULL,
	[pflag] [int] NULL,
	[cid] [varchar](32) NULL,
	*/
	
	private String pid;
	private String pname;
	private double market_price;
	private double shop_price;
	private String pimage;
	private Date  pdate;
	private int is_not;
	private String pdesc;
	private int  pflag;
	private Category category;
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public double getMarket_price() {
		return market_price;
	}
	public void setMarket_price(double market_price) {
		this.market_price = market_price;
	}
	public double getShop_price() {
		return shop_price;
	}
	public void setShop_price(double shop_price) {
		this.shop_price = shop_price;
	}
	public String getPimage() {
		return pimage;
	}
	public void setPimage(String pimage) {
		this.pimage = pimage;
	}
	public Date getPdate() {
		return pdate;
	}
	public void setPdate(Date pdate) {
		this.pdate = pdate;
	}
	public int getIs_not() {
		return is_not;
	}
	public void setIs_not(int is_not) {
		this.is_not = is_not;
	}
	public String getPdesc() {
		return pdesc;
	}
	public void setPdesc(String pdesc) {
		this.pdesc = pdesc;
	}
	public int getPflag() {
		return pflag;
	}
	public void setPflag(int pflag) {
		this.pflag = pflag;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	
}
