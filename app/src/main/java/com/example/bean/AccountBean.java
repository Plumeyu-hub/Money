package com.example.bean;

import java.io.Serializable;

public class AccountBean implements Serializable {
	private String category;// 类别
	private String money;// 金额
	private String account;// 账户
	private String remarks;// 备注
	private String daytime;// 时间
	private int id;
	private int userid;

	public AccountBean(String category, String money, String account,
					   String remarks, String daytime, int num, int useridnum) {
		super();
		this.category = category;
		this.money = money;
		this.account = account;
		this.remarks = remarks;
		this.daytime = daytime;
		this.id = num;
		this.userid = useridnum;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDaytime() {
		return daytime;
	}

	public void setDaytime(String daytime) {
		this.daytime = daytime;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}
}
