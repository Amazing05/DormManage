package com.lero.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lero.model.Consumerecord;
import com.lero.model.Counter;
import com.lero.model.Drug;
import com.lero.model.GenericType;
import com.lero.model.Student;

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
	public List<Drug> listDrugs(Connection con) {

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
//				drug.setDeadline(rs.getDate("deadline"));
				drug.setQuantity(rs.getInt("quantity"));
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
	public List<GenericType<Drug,Counter,String>> listDrugs(Connection con,Map<String,String> condition) {
		List<Drug> drugList = listDrugs(con);
		CounterDao counterDao = new CounterDao();
		String key = null;
		String value = null;
		for(String index : condition.keySet()) {
			key = index;
		}
		value = condition.get(key);
		
		List<GenericType<Drug,Counter,String>>  list = new ArrayList<GenericType<Drug,Counter,String>> ();
		

		for(Drug drug :drugList) {
			if(key.equals("name")&& drug.getName().equals(value)) {
				Counter counter = counterDao.getCounterById(con, drug.getCounterId());
				list.add(new GenericType<Drug,Counter,String>(drug,counter,""));
			} 
			
			if(key.equals("counter")) {
				Counter counter = counterDao.getCounterById(con, drug.getCounterId());
				if (counter.getName().equals(value)) {
					list.add(new GenericType<Drug,Counter,String>(drug,counter,""));
				}
			}
			
			if(key.equals("null")) {
				Counter counter = counterDao.getCounterById(con, drug.getCounterId());				
					list.add(new GenericType<Drug,Counter,String>(drug,counter,""));

			}
			
			
		}
		
		
		
		return list;
	}	
	
	
	public int drugUpdate(Connection con, Drug drug)throws Exception {
		String sql = "update drug set name=?,buyingPrice=?,sellingPrice=?,counterId=?,description=?,quantity=? where drugId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, drug.getName());
		pstmt.setDouble(2, drug.getBuyingPrice());
		pstmt.setDouble(3, drug.getSellingPrice());
		pstmt.setInt(4, drug.getCounterId());
		pstmt.setString(5, drug.getDescription());
		pstmt.setInt(6, drug.getQuantity());
		pstmt.setInt(7, drug.getDrugId());
//		pstmt.setInt(8, student.getStudentId());
		return pstmt.executeUpdate();
	}
	
	public boolean haveName(Connection con,  String name)throws Exception {
		String sql = "select * from drug t1 where t1.name=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, name);
		ResultSet rs=pstmt.executeQuery();
		if(rs.next()) {
			return true;
		}
		return false;
	}
	
	public int drugAdd(Connection con, Drug drug)throws Exception {
		String sql = "insert into drug values(null,?,?,?,?,?,?)";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, drug.getName());
		pstmt.setDouble(2, drug.getBuyingPrice());
		pstmt.setDouble(3, drug.getSellingPrice());
		pstmt.setInt(4, drug.getCounterId());
		pstmt.setString(5, drug.getDescription());
		pstmt.setInt(6, drug.getQuantity());
		return pstmt.executeUpdate();
	}
	
	
	public static Drug getDrugById(Connection con, int drugId)throws Exception {
		String sql = "select * from drug t1 where t1.drugId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setInt(1, drugId);
		ResultSet rs=pstmt.executeQuery();
		Drug drug = new Drug();
		if(rs.next()) {
			drug.setBuyingPrice(rs.getDouble("buyingPrice"));
			drug.setCounterId(rs.getInt("counterId"));
//			drug.setDeadline(rs.getDate("deadline"));
			drug.setQuantity(rs.getInt("quantity"));
			drug.setDescription(rs.getString("description"));
			drug.setDrugId(rs.getInt("drugId"));
			drug.setName(rs.getString("name"));
			drug.setSellingPrice(rs.getDouble("sellingPrice"));
		}
		return drug;
	}
	
	
	public int drugDelete(Connection con, int drugId)throws Exception {
		String sql = "delete from drug where drugId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setInt(1, drugId);
		return pstmt.executeUpdate();
	}
		
	public static int drugUpdate(Connection con, Drug drug,int sellingCount)throws Exception {
		String sql = "update drug set quantity=? where drugId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setInt(1, drug.getQuantity()-sellingCount);
		pstmt.setInt(2, drug.getDrugId());
		return pstmt.executeUpdate();
	}
	
	public static int drugUpdateDelete(Connection con, Consumerecord consumerecord)throws Exception {
		int sellingCount= getDrugById(con,consumerecord.getDrugId()).getQuantity();
		String sql = "update drug set quantity=? where drugId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setInt(1, consumerecord.getQuantity()+sellingCount);
		pstmt.setInt(2, consumerecord.getDrugId());
		return pstmt.executeUpdate();
	}
	
	public List<Drug> getDrugsByCounterId(Connection con,int counterId) throws Exception {
		String sql = "select * from drug t1 where t1.counterId=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, counterId);
		
		ResultSet rs=pstmt.executeQuery();
		
		List<Drug> drugs = new ArrayList<Drug>();
		while(rs.next()) {
			Drug drug = new Drug();
			drug.setBuyingPrice(rs.getDouble("buyingPrice"));
			drug.setCounterId(rs.getInt("counterId"));
//			drug.setDeadline(rs.getDate("deadline"));
			drug.setQuantity(rs.getInt("quantity"));
			drug.setDescription(rs.getString("description"));
			drug.setDrugId(rs.getInt("drugId"));
			drug.setName(rs.getString("name"));
			drug.setSellingPrice(rs.getDouble("sellingPrice"));
			drugs.add(drug);
		}
		
		return drugs;
	}
}
