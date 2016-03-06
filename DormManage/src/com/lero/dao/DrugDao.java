package com.lero.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.lero.model.Counter;
import com.lero.model.Drug;
import com.lero.model.GenericType;

/*
 *@author mingyuan li
 *@version 2016年3月6日 上午9:38:36
 */
public class DrugDao {
	
	/**
	 *  查询DB中所有Drug信息
	 * @param con
	 * @return List<Drug>，DB中没有数据时返回一个size为0的List<Drug>
	 */
	private List<Drug> listDrugs(Connection con) {

		List<Drug> list = new ArrayList<Drug>();
		StringBuffer str = new StringBuffer("select * from drug g");
		String sql = str.toString();
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Drug drug = new Drug();
				drug.setBuyingPrice(rs.getDouble("buyingPrice"));
				drug.setCounterId(rs.getInt("counterId"));
				drug.setDeadline(rs.getDate("deadline"));
				drug.setDescription(rs.getString("description"));
				drug.setDrugId(rs.getInt("drugId"));
				drug.setName(rs.getString("name"));
				drug.setSellingPrice(rs.getDouble("sellingPrice"));
				list.add(drug);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;

	}
	
	/**
	 * 按 一定的条件查找 DB中的Drug
	 * @param con
	 * @param condition
	 * @return 
	 */
	public List<GenericType<Drug,Counter,String>> listDrugs(Connection con,Drug condition) {
		List<Drug> drugList = listDrugs(con);
		List<Drug> drugAfterList = new ArrayList<Drug>();
		CounterDao counterDao = new CounterDao();
		List<GenericType<Drug,Counter,String>>  list = new ArrayList<GenericType<Drug,Counter,String>> ();
		for(Drug drug : drugList) {
			if(drug.filter(condition) == true){
				drugAfterList.add(drug);
			}
		}
		
		for(Drug drug :drugAfterList) {
			Counter counter = counterDao.getCounterById(con, drug.getCounterId());
			list.add(new GenericType<Drug,Counter,String>(drug,counter,""));			
		}
		
		return list;
	}	
	
		
	
	
	
}
