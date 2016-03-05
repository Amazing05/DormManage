package com.lero.model;
/*
 *@author mingyuan li
 *@version 2016年3月5日 下午3:43:16
 */
public class DrugSeller {
	private int drugSellerId;
	private String name;
	private String gender;
	private String tel;
	private String description;
	private int counterId;
	
	public DrugSeller() {
		
	}

	public int getDrugSellerId() {
		return drugSellerId;
	}

	public void setDrugSellerId(int drugSellerId) {
		this.drugSellerId = drugSellerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCounterId() {
		return counterId;
	}

	public void setCounterId(int counterId) {
		this.counterId = counterId;
	}
	
	
}
