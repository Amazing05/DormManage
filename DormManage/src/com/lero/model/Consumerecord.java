package com.lero.model;

import java.util.Date;

public class Consumerecord {
 public int drugId;
	public int getDrugId() {
	return drugId;
}
public void setDrugId(int drugId) {
	this.drugId = drugId;
}
	public String getDrugName() {
		return drugName;
	}
	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}
	public int drugSellerId;
	public int getDrugSellerId() {
		return drugSellerId;
	}
	public void setDrugSellerId(int drugSellerId) {
		this.drugSellerId = drugSellerId;
	}
	private int recordId;
	private int drugsellerId;
	private int quantity;
	private String drugName;
	public int getRecordId() {
		return recordId;
	}
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}
	public int getDrugsellerId() {
		return drugsellerId;
	}
	public void setDrugsellerId(int drugsellerId) {
		this.drugsellerId = drugsellerId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	private double totalPrice;
	private Date  date;
	private String description;
}
