package com.lero.model;
/*
 *@author mingyuan li
 *@version 2016年3月5日 下午5:49:13
 */
public class Counter {
	private int counterId;
	private String name;
	private String description;
	
	public Counter() {
		
	}
	public int getCounterId() {
		return counterId;
	}
	public void setCounterId(int counterId) {
		this.counterId = counterId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
