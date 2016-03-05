package com.lero.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.lero.model.Counter;
import com.lero.model.DormManager;
import com.lero.model.DrugSeller;
import com.lero.model.GenericType;
import com.lero.model.Login;
import com.lero.model.PageBean;
import com.lero.util.StringUtil;

public class DormManagerDao {

	public List< GenericType<DrugSeller,Counter,String>> dormManagerList(Connection con, PageBean pageBean, DormManager s_dormManager)throws Exception {

		List< GenericType<DrugSeller,Counter,String>> lists = new ArrayList<GenericType<DrugSeller,Counter,String>>();   
		StringBuffer sb = new StringBuffer("SELECT t1.*,t2.name as counterName FROM drugSeller t1 left join counter t2 on  t1.counterId=t2.counterId ");
		if(StringUtil.isNotEmpty(s_dormManager.getName())) {
			sb.append(" where t1.name like '%"+s_dormManager.getName()+"%'");
		} else if(StringUtil.isNotEmpty(s_dormManager.getUserName())) {
			sb.append(" where t1.userName like '%"+s_dormManager.getUserName()+"%'");
		}
		sb.append("ORDER BY t1.name  ");
		if(pageBean != null) {
			sb.append(" limit "+pageBean.getStart()+","+pageBean.getPageSize());
		}
		PreparedStatement pstmt = con.prepareStatement(sb.toString());
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			GenericType<DrugSeller,Counter,String> g =null;
			DrugSeller drugSeller = new DrugSeller();
			Counter counter = new Counter();
			drugSeller.setCounterId(rs.getInt("counterId"));
			drugSeller.setDescription(rs.getString("description"));
			drugSeller.setDrugSellerId(rs.getInt("drugSellerId"));
			drugSeller.setGender(rs.getString("gender"));
			drugSeller.setName(rs.getString("name"));
			drugSeller.setTel(rs.getString("tel"));
			
			counter.setName(rs.getString("counterName"));
			g = new GenericType(drugSeller,counter,"");
			lists.add(g);
		}
		return lists;
	}

	public int getMaxDrugsellerId(Connection con) throws Exception {

		List<GenericType<DrugSeller, Counter, String>> lists = new ArrayList<GenericType<DrugSeller, Counter, String>>();
		StringBuffer sb = new StringBuffer("SELECT max(drugsellerId) as maxId FROM drugSeller ");
		int maxId = 0;

		PreparedStatement pstmt = con.prepareStatement(sb.toString());
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			maxId = rs.getInt("maxId");
		}
		return maxId;
	}
	
	
	public int dormManagerCount(Connection con, DormManager s_dormManager)throws Exception {
		StringBuffer sb = new StringBuffer("select count(*) as total from t_dormManager t1");
		if(StringUtil.isNotEmpty(s_dormManager.getName())) {
			sb.append(" where t1.name like '%"+s_dormManager.getName()+"%'");
		} else if(StringUtil.isNotEmpty(s_dormManager.getUserName())) {
			sb.append(" where t1.userName like '%"+s_dormManager.getUserName()+"%'");
		}
		PreparedStatement pstmt = con.prepareStatement(sb.toString());
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			return rs.getInt("total");
		} else {
			return 0;
		}
	}
	
	public GenericType<DrugSeller,Login,String> dormManagerShow(Connection con, String drugSellerId)throws Exception {
		String sql = "select * from drugseller t1 where t1.drugsellerId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, drugSellerId);
		ResultSet rs=pstmt.executeQuery();
		DrugSeller drugSeller = new DrugSeller();
		
		if(rs.next()) {
			drugSeller.setDrugSellerId(rs.getInt("drugsellerId"));
//			drugSeller.setCounterId(rs.getInt("counterId"));
			drugSeller.setName(rs.getString("name"));
			drugSeller.setGender(rs.getString("gender"));
			drugSeller.setTel(rs.getString("tel"));
		}
		 sql = "select * from login t1 where t1.userId=? and t1.role=?";
		 pstmt=con.prepareStatement(sql);
		pstmt.setString(1, drugSellerId);
		pstmt.setString(2, "drugseller");
		 rs=pstmt.executeQuery();
		 Login login = new Login();
		 if(rs.next()) {
//				drugSeller.setCounterId(rs.getInt("counterId"));
				login.setUserName(rs.getString("userName"));
				login.setUserPassword(rs.getString("userPassword"));
				
			}
		 GenericType<DrugSeller,Login,String>  g = new GenericType<DrugSeller,Login,String> (drugSeller,login,"");
		return g;
	}
	
	public int dormManagerAdd(Connection con, DrugSeller drugSeller)throws Exception {
//		String sql = "insert into t_dormManager values(null,?,?,null,?,?,?)";
		String sql = "insert into drugseller values(null,?,?,?,null,null)";
		PreparedStatement pstmt=con.prepareStatement(sql);
//		pstmt.setString(1, drugSeller.getUserName());
//		pstmt.setString(2, drugSeller.getPassword());
		pstmt.setString(1, drugSeller.getName());
		pstmt.setString(2, drugSeller.getGender());
		pstmt.setString(3, drugSeller.getTel());
		return pstmt.executeUpdate();
		
		
	}
	
	public int loginAdd(Connection con, Login login)throws Exception {
//		String sql = "insert into t_dormManager values(null,?,?,null,?,?,?)";
		String sql = "insert into login values(null,?,?,?,?)";
		PreparedStatement pstmt=con.prepareStatement(sql);
		int maxId=this.getMaxDrugsellerId(con);
		pstmt.setString(1, login.getUserName());
		pstmt.setString(2, login.getUserPassword());
		pstmt.setString(3, "drugseller");
		pstmt.setInt(4, maxId);
		return pstmt.executeUpdate();
	}
	
	public int dormManagerDelete(Connection con, String drugSellerId)throws Exception {
		String sql = "delete from drugseller where drugsellerId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, drugSellerId);
		pstmt.executeUpdate();
		 sql = "delete from login where userId=? and role = ?";
		 pstmt=con.prepareStatement(sql);
		 pstmt.setString(1, drugSellerId); 
		 pstmt.setString(2, "drugseller");
		return  pstmt.executeUpdate();
	}
	
	public int dormManagerUpdate(Connection con, DrugSeller drugSeller)throws Exception {
		String sql = "update drugseller set name=?,gender=?,tel=? where drugsellerId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, drugSeller.getName());
		pstmt.setString(2, drugSeller.getGender());
		pstmt.setString(3, drugSeller.getTel());
		pstmt.setInt(4, drugSeller.getDrugSellerId());
		
		return pstmt.executeUpdate();
		
	}
	
	public int loginUpdate(Connection con, Login login,int drugSellerId)throws Exception {
		String sql = "update login set userName=?,userPassword=? where userId=? and role=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, login.getUserName());
		pstmt.setString(2, login.getUserPassword());
		pstmt.setInt(3, drugSellerId);
		pstmt.setString(4, "drugseller");
		
		return pstmt.executeUpdate();
	}

	public boolean haveManagerByUser(Connection con, String userName) throws Exception {
//		String sql = "select * from t_dormmanager t1 where t1.userName=?"; 
		String sql="select * from login where login.userName=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, userName);
		ResultSet rs=pstmt.executeQuery();
		if(rs.next()) {
			return true;
		}
		return false;
	}
	
	
}
