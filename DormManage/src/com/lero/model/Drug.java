package com.lero.model;

import java.util.Date;

/*
 *@author mingyuan li
 *@version 2016年3月6日 上午9:39:32
 */
public class Drug {
	private int drugId;
	private String name;
	private double buyingPrice;
	private double sellingPrice;
	private int counterId;
	private Date deadline;
	private String description;
	public int getDrugId() {
		return drugId;
	}
	public void setDrugId(int drugId) {
		this.drugId = drugId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getBuyingPrice() {
		return buyingPrice;
	}
	public void setBuyingPrice(double buyingPrice) {
		this.buyingPrice = buyingPrice;
	}
	public double getSellingPrice() {
		return sellingPrice;
	}
	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}
	public int getCounterId() {
		return counterId;
	}
	public void setCounterId(int counterId) {
		this.counterId = counterId;
	}
	public Date getDeadline() {
		return deadline;
	}
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * 通过condition 筛选符合条件的drug
	 * @param condition
	 * @return boolean
	 */
	public boolean filter(Drug condition) {
		if(condition.getBuyingPrice() != 0.0 && (condition.getBuyingPrice() !=this.buyingPrice)) return false; 
		if(condition.getCounterId() != 0 && (condition.getDrugId() != this.getCounterId())) return false;	
		if(condition.getDeadline() != null  && (condition.getDeadline().equals(this.getDeadline()))) return false;
		if(condition.getDescription() != null && (condition.getDescription().equals(this.getDescription()))) return false;
		if(condition.getDrugId() != 0 && (condition.getDrugId() != this.getDrugId())) return false;
		if(condition.getName() != null && (condition.getName().equals( this.getName()) == false)) return false;
		if(condition.getSellingPrice() != 0.0 &&(condition.getSellingPrice() != condition.getSellingPrice())) return false;
		return true;
	}
	
}
