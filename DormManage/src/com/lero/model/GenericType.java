package com.lero.model;
/*
 *@author mingyuan li
 *@version 2016年3月5日 下午5:09:19
 */
public class GenericType<T1,T2,T3> {
     private T1 t1;
     private T2 t2;
     private T3 t3;
	public GenericType(T1 t1, T2 t2) {
		super();
		this.t1 = t1;
		this.t2 = t2;
	}
	public GenericType(T1 t1, T2 t2, T3 t3) {
		super();
		this.t1 = t1;
		this.t2 = t2;
		this.t3 = t3;
	}
	
	public void setTwo(T1 t1,T2 t2) {
		this.t1 = t1;
		this.t2 = t2;
	}
	public void setThree(T1 t1,T2 t2,T3 t3) {
		this.t1 = t1;
		this.t2 = t2;
		this.t3 = t3;
	}
	public T1 getT1() {
		return t1;
	}
	public void setT1(T1 t1) {
		this.t1 = t1;
	}
	public T2 getT2() {
		return t2;
	}
	public void setT2(T2 t2) {
		this.t2 = t2;
	}
	public T3 getT3() {
		return t3;
	}
	public void setT3(T3 t3) {
		this.t3 = t3;
	}
	
	
     
}
