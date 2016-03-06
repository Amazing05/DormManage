package com.lero.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.lero.model.Counter;
import com.lero.model.Drug;
import com.lero.model.DrugSeller;
import com.lero.model.GenericType;
import com.lero.model.PageBean;
import com.lero.util.StringUtil;

/*
 *@author mingyuan li
 *@version 2016年3月6日 上午10:13:58
 */
public class CounterDao {
	
	/**
	 * 查询DB中所有的Counter信息
	 * @param con
	 * @return List<Counter>,DB中没有数据时，返回一个size为0的List<Counter>
	 */
	private List<Counter> listCounter(Connection con) {

		List<Counter> list = new ArrayList<Counter>();
		StringBuffer str = new StringBuffer("select * from counter c");
		String sql = str.toString();
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Counter counter = new Counter();
				counter.setCounterId(rs.getInt("counterId"));
				counter.setName(rs.getString("name"));
				counter.setDescription(rs.getString("description"));
				list.add(counter);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;

	}
	
	/**
	 * 通过counterId 信息查找 counter
	 * @param con
	 * @param counterId
	 * @return counter，如果没有查到返回 null
	 */
	public Counter getCounterById(Connection con,int counterId){
		List<Counter> list = listCounter(con);
		for(Counter counter: list) {
			if(counter.getCounterId() == counterId) {
				return counter;
			}
		}
		
		return null;
	}
}
