package com.lero.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.lero.model.Counter;
import com.lero.model.DormManager;
import com.lero.model.DrugSeller;
import com.lero.model.PageBean;
import com.lero.util.StringUtil;

public class DormBuildDao {

	public List<Counter> counterList(Connection con, PageBean pageBean, Counter counter)throws Exception {
		List<Counter> counterList = new ArrayList<Counter>();   
		StringBuffer sb = new StringBuffer("select * from counter t1");
		if(StringUtil.isNotEmpty(counter.getName())) {
			sb.append(" where t1.name like '%"+counter.getName()+"%'");
		}
		if(pageBean != null) {
			sb.append(" limit "+pageBean.getStart()+","+pageBean.getPageSize());
		}
		PreparedStatement pstmt = con.prepareStatement(sb.toString());
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			Counter counterTemp=new Counter();
			counterTemp.setCounterId(rs.getInt("counterId"));
			counterTemp.setName(rs.getString("name"));
			counterTemp.setDescription(rs.getString("description"));
			counterList.add(counterTemp);
		}
		return counterList;
	}
	
	public static String getDrugName(Connection con, int drugId)throws Exception {
		String sql = "select * from drug where drugId=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, drugId);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			return rs.getString("name");
		}
		return null;
	}
	
	public int counterCount(Connection con, Counter counter)throws Exception {
		StringBuffer sb = new StringBuffer("select count(*) as total from counter t1");
		if(StringUtil.isNotEmpty(counter.getName())) {
			sb.append(" where t1.name like '%"+counter.getName()+"%'");
		}
		PreparedStatement pstmt = con.prepareStatement(sb.toString());
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			return rs.getInt("total");
		} else {
			return 0;
		}
	}
	
	public Counter counterShow(Connection con, String counterId)throws Exception { 
		String sql = "select * from counter t1 where t1.counterId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, counterId);
		ResultSet rs=pstmt.executeQuery();
		Counter counter=new Counter();
		if(rs.next()) {
			counter.setCounterId(rs.getInt("counterId"));
			counter.setName(rs.getString("name"));
			counter.setDescription(rs.getString("description"));
		}
		return counter;
	}
	
	public int counterAdd(Connection con, Counter counter)throws Exception {
		String sql = "insert into counter values(null,?,?)";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, counter.getName());
		pstmt.setString(2, counter.getDescription());
		return pstmt.executeUpdate();
	}
	
	public int counterDelete(Connection con, String counterId)throws Exception {
		String sql = "delete from counter where counterId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, counterId);
		return pstmt.executeUpdate();
	}
	
	public int counterUpdate(Connection con, Counter counter)throws Exception {
		String sql = "update counter set name=?,description=? where counterId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1,counter.getName());
		pstmt.setString(2, counter.getDescription());
		pstmt.setInt(3, counter.getCounterId());
		return pstmt.executeUpdate();
	}
	
	public boolean existManOrDormWithId(Connection con, String dormBuildId)throws Exception {
		boolean isExist = false;
//		String sql="select * from t_dormBuild,t_dormManager,t_connection where dormManId=managerId and dormBuildId=buildId and dormBuildId=?";
		String sql = "select *from t_dormManager where dormBuildId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, dormBuildId);
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			isExist = true;
		} else {
			isExist = false;
		}
		String sql1="select * from t_dormBuild t1,t_dorm t2 where t1.dormBuildId=t2.dormBuildId and t1.dormBuildId=?";
		PreparedStatement p=con.prepareStatement(sql1);
		p.setString(1, dormBuildId);
		ResultSet r = pstmt.executeQuery();
		if(r.next()) {
			return isExist;
		} else {
			return false;
		}
	}
	
	public List<DrugSeller> getDrugSellerWithoutBuild(Connection con)throws Exception {
		List<DrugSeller> drugSellerList =new ArrayList<DrugSeller>();
		String sql = "SELECT * FROM drugseller WHERE counterId IS NULL OR counterId=0";
		PreparedStatement pstmt = con.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			DrugSeller drugSeller = new DrugSeller();
			drugSeller.setCounterId(rs.getInt("counterId"));
			drugSeller.setDescription(rs.getString("description"));
			drugSeller.setDrugSellerId(rs.getInt("drugSellerId"));
			drugSeller.setGender(rs.getString("gender"));
			drugSeller.setName(rs.getString("name"));
			drugSeller.setTel(rs.getString("tel"));
		
			drugSellerList.add(drugSeller);
		}
		return drugSellerList;
	}
	
	public List<DrugSeller> getdrugSellerWithcounterId(Connection con, String counterId)throws Exception {
		List<DrugSeller> drugSellerList = new ArrayList<DrugSeller>();
		String sql = "select *from drugseller where counterId=?";
		PreparedStatement pstmt=con.prepareStatement(sql); 
		pstmt.setString(1, counterId);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {

			DrugSeller drugSeller = new DrugSeller();
			drugSeller.setCounterId(rs.getInt("counterId"));
			drugSeller.setDescription(rs.getString("description"));
			drugSeller.setDrugSellerId(rs.getInt("drugSellerId"));
			drugSeller.setGender(rs.getString("gender"));
			drugSeller.setName(rs.getString("name"));
			drugSeller.setTel(rs.getString("tel"));
			drugSellerList.add(drugSeller);
		}
		return drugSellerList;
	}
	
	public int managerUpdateWithId (Connection con, String dormManagerId, String dormBuildId)throws Exception {
		String sql = "update drugseller set counterId=? where drugsellerId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, dormBuildId);
		pstmt.setString(2, dormManagerId);
		return pstmt.executeUpdate();
	}
}
