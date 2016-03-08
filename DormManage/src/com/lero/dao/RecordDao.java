package com.lero.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.el.parser.ParseException;

import com.lero.model.Consumerecord;
import com.lero.model.DormBuild;
import com.lero.model.Drug;
import com.lero.model.Record;
import com.lero.util.StringUtil;

public class RecordDao {
	public List<Record> recordList(Connection con, Record s_record)throws Exception {
		List<Record> recordList = new ArrayList<Record>();
		StringBuffer sb = new StringBuffer("select * from t_record t1");
		if(StringUtil.isNotEmpty(s_record.getStudentNumber())) {
			sb.append(" and t1.studentNumber like '%"+s_record.getStudentNumber()+"%'");
		} else if(StringUtil.isNotEmpty(s_record.getStudentName())) {
			sb.append(" and t1.studentName like '%"+s_record.getStudentName()+"%'");
		}
		if(s_record.getDormBuildId()!=0) {
			sb.append(" and t1.dormBuildId="+s_record.getDormBuildId());
		}
		if(StringUtil.isNotEmpty(s_record.getDate())) {
			sb.append(" and t1.date="+s_record.getDate());
		}
		if(StringUtil.isNotEmpty(s_record.getStartDate())){
			sb.append(" and TO_DAYS(t1.date)>=TO_DAYS('"+s_record.getStartDate()+"')");
		}
		if(StringUtil.isNotEmpty(s_record.getEndDate())){
			sb.append(" and TO_DAYS(t1.date)<=TO_DAYS('"+s_record.getEndDate()+"')");
		}
		PreparedStatement pstmt = con.prepareStatement(sb.toString().replaceFirst("and", "where"));
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			Record record=new Record();
			record.setRecordId(rs.getInt("recordId"));
			record.setStudentNumber(rs.getString("studentNumber"));
			record.setStudentName(rs.getString("studentName"));
			int dormBuildId = rs.getInt("dormBuildId");
			record.setDormBuildId(dormBuildId);
			//record.setDormBuildName(DormBuildDao.dormBuildName(con, dormBuildId));
			record.setDormName(rs.getString("dormName"));
			record.setDate(rs.getString("date"));
			record.setDetail(rs.getString("detail"));
			recordList.add(record);
		}
		return recordList;
	}
	public List<Consumerecord> recordListWithDrugSellerNoSellerId(Connection con, String drugName, String startDate,String endDate)throws Exception {
		List<Consumerecord> consumerecordList=new ArrayList<Consumerecord>();
		StringBuffer sb = new StringBuffer("select t3.name, t1.*,t3.sellingPrice  from consumerecord t1 ,drug t3  where   t1.drugId=t3.drugId   ");
		if(StringUtil.isNotEmpty(drugName)) {
			sb.append("  and t3.name like '%"+drugName +"%' ");
		} 
		if(StringUtil.isNotEmpty(startDate)){
			sb.append(" and TO_DAYS(t1.date)>=TO_DAYS('"+startDate+"')");
		}
		if(StringUtil.isNotEmpty(endDate)){
			sb.append(" and TO_DAYS(t1.date)<=TO_DAYS('"+endDate+"')");
		}
		//PreparedStatement pstmt = con.prepareStatement(sb.toString().replaceFirst("and", "where"));
		PreparedStatement pstmt = con.prepareStatement(sb.toString());
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			Consumerecord comsumerecord =new Consumerecord();
			comsumerecord.setRecordId(rs.getInt("recordId"));
			comsumerecord.setDrugName(rs.getString("name"));
			comsumerecord.setQuantity(rs.getInt("quantity"));
			comsumerecord.setTotalPrice(rs.getDouble("sellingPrice")*rs.getInt("quantity"));
			comsumerecord.setDate(rs.getDate("date"));
			comsumerecord.setDescription(rs.getString("description"));
			
			consumerecordList.add(comsumerecord);
		}
		return consumerecordList;
	}
	public List<Consumerecord> recordListWithDrugSeller(Connection con, String drugName, int drugSellerId,String startDate,String endDate)throws Exception {
		List<Consumerecord> consumerecordList=new ArrayList<Consumerecord>();
		StringBuffer sb = new StringBuffer("select t3.name, t1.*,t3.sellingPrice  from consumerecord t1 ,drug t3  where   t1.drugId=t3.drugId and  ");
		if(StringUtil.isNotEmpty(drugName)) {
			sb.append("  t3.name like '%"+drugName +"%' and ");
		} 
		sb.append("  t1.drugSellerId="+drugSellerId);
		if(StringUtil.isNotEmpty(startDate)){
			sb.append(" and TO_DAYS(t1.date)>=TO_DAYS('"+startDate+"')");
		}
		if(StringUtil.isNotEmpty(endDate)){
			sb.append(" and TO_DAYS(t1.date)<=TO_DAYS('"+endDate+"')");
		}
		//PreparedStatement pstmt = con.prepareStatement(sb.toString().replaceFirst("and", "where"));
		PreparedStatement pstmt = con.prepareStatement(sb.toString());
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			Consumerecord comsumerecord =new Consumerecord();
			comsumerecord.setRecordId(rs.getInt("recordId"));
			comsumerecord.setDrugSellerId(drugSellerId);
			comsumerecord.setDrugName(rs.getString("name"));
			comsumerecord.setQuantity(rs.getInt("quantity"));
			comsumerecord.setTotalPrice(rs.getDouble("sellingPrice")*rs.getInt("quantity"));
			comsumerecord.setDate(rs.getDate("date"));
			comsumerecord.setDescription(rs.getString("description"));
			
			consumerecordList.add(comsumerecord);
		}
		return consumerecordList;
	}
	public List<Record> recordListWithBuild(Connection con, Record s_record, int buildId)throws Exception {
		List<Record> recordList = new ArrayList<Record>();
		StringBuffer sb = new StringBuffer("select * from t_record t1");
		if(StringUtil.isNotEmpty(s_record.getStudentNumber())) {
			sb.append(" and t1.studentNumber like '%"+s_record.getStudentNumber()+"%'");
		} else if(StringUtil.isNotEmpty(s_record.getStudentName())) {
			sb.append(" and t1.studentName like '%"+s_record.getStudentName()+"%'");
		}
		sb.append(" and t1.dormBuildId="+buildId);
		if(StringUtil.isNotEmpty(s_record.getStartDate())){
			sb.append(" and TO_DAYS(t1.date)>=TO_DAYS('"+s_record.getStartDate()+"')");
		}
		if(StringUtil.isNotEmpty(s_record.getEndDate())){
			sb.append(" and TO_DAYS(t1.date)<=TO_DAYS('"+s_record.getEndDate()+"')");
		}
		PreparedStatement pstmt = con.prepareStatement(sb.toString().replaceFirst("and", "where"));
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			Record record=new Record();
			record.setRecordId(rs.getInt("recordId"));
			record.setStudentNumber(rs.getString("studentNumber"));
			record.setStudentName(rs.getString("studentName"));
			int dormBuildId = rs.getInt("dormBuildId");
			record.setDormBuildId(dormBuildId);
			//record.setDormBuildName(DormBuildDao.dormBuildName(con, dormBuildId));
			record.setDormName(rs.getString("dormName"));
			record.setDate(rs.getString("date"));
			record.setDetail(rs.getString("detail"));
			recordList.add(record);
		}
		return recordList;
	}
	
	public List<Record> recordListWithNumber(Connection con, Record s_record, String studentNumber)throws Exception {
		List<Record> recordList = new ArrayList<Record>();
		StringBuffer sb = new StringBuffer("select * from t_record t1");
		if(StringUtil.isNotEmpty(studentNumber)) {
			sb.append(" and t1.studentNumber ="+studentNumber);
		} 
		if(StringUtil.isNotEmpty(s_record.getStartDate())){
			sb.append(" and TO_DAYS(t1.date)>=TO_DAYS('"+s_record.getStartDate()+"')");
		}
		if(StringUtil.isNotEmpty(s_record.getEndDate())){
			sb.append(" and TO_DAYS(t1.date)<=TO_DAYS('"+s_record.getEndDate()+"')");
		}
		PreparedStatement pstmt = con.prepareStatement(sb.toString().replaceFirst("and", "where"));
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			Record record=new Record();
			record.setRecordId(rs.getInt("recordId"));
			record.setStudentNumber(rs.getString("studentNumber"));
			record.setStudentName(rs.getString("studentName"));
			int dormBuildId = rs.getInt("dormBuildId");
			record.setDormBuildId(dormBuildId);
			//record.setDormBuildName(DormBuildDao.dormBuildName(con, dormBuildId));
			record.setDormName(rs.getString("dormName"));
			record.setDate(rs.getString("date"));
			record.setDetail(rs.getString("detail"));
			recordList.add(record);
		}
		return recordList;
	}
	
	public List<DormBuild> dormBuildList(Connection con)throws Exception {
		List<DormBuild> dormBuildList = new ArrayList<DormBuild>();
		String sql = "select * from t_dormBuild";
		PreparedStatement pstmt = con.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			DormBuild dormBuild=new DormBuild();
			dormBuild.setDormBuildId(rs.getInt("dormBuildId"));
			dormBuild.setDormBuildName(rs.getString("dormBuildName"));
			dormBuild.setDetail(rs.getString("dormBuildDetail"));
			dormBuildList.add(dormBuild);
		}
		return dormBuildList;
	}
//	
//	public int studentCount(Connection con, Student s_student)throws Exception {
//		StringBuffer sb = new StringBuffer("select count(*) as total from t_student t1");
//		if(StringUtil.isNotEmpty(s_student.getName())) {
//			sb.append(" and t1.name like '%"+s_student.getName()+"%'");
//		} else if(StringUtil.isNotEmpty(s_student.getStuNumber())) {
//			sb.append(" and t1.stuNum like '%"+s_student.getStuNumber()+"%'");
//		} else if(StringUtil.isNotEmpty(s_student.getDormName())) {
//			sb.append(" and t1.dormName like '%"+s_student.getDormName()+"%'");
//		}
//		if(s_student.getDormBuildId()!=0) {
//			sb.append(" and t1.dormBuildId="+s_student.getDormBuildId());
//		}
//		PreparedStatement pstmt = con.prepareStatement(sb.toString().replaceFirst("and", "where"));
//		ResultSet rs = pstmt.executeQuery();
//		if(rs.next()) {
//			return rs.getInt("total");
//		} else {
//			return 0;
//		}
//	}
	
	public Consumerecord recordShow(Connection con, String recordId)throws Exception {
		String sql = "select * from consumerecord t1 where t1.recordId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, recordId);
		ResultSet rs=pstmt.executeQuery();
		Consumerecord record = new Consumerecord();
		if(rs.next()) {
			record.setRecordId(rs.getInt("recordId"));
			record.setQuantity(rs.getInt("quantity"));
			record.setDate(rs.getDate("date"));
			record.setTotalPrice(rs.getDouble("totalPrice"));
			record.setDescription(rs.getString("description"));
			int dormBuildId = rs.getInt("drugId");
			record.setDrugName(DormBuildDao.getDrugName(con, dormBuildId));
		}
		return record;
	}
	public Consumerecord getDrugIdByConsumerecordId(Connection con, String recordId)throws Exception {
		String sql = "select * from consumerecord t1 where t1.recordId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, recordId);
		ResultSet rs=pstmt.executeQuery();
		Consumerecord record = new Consumerecord();
		if(rs.next()) {
			record.setRecordId(rs.getInt("recordId"));
			record.setQuantity(rs.getInt("quantity"));
			record.setDate(rs.getDate("date"));
			record.setTotalPrice(rs.getDouble("totalPrice"));
			record.setDescription(rs.getString("description"));
			record.setDrugId(rs.getInt("drugId"));
		}
		return record;
	}
	public int recordAdd(Connection con, Consumerecord record,int drugSellerId)throws Exception {
		Drug drug = StudentDao.getNameById(con, record.getDrugName());
		DrugDao.drugUpdate(con, drug, record.getQuantity());//更新药品数量
		String sql = "insert into consumerecord values(null,?,?,1,?,?,?,?)";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setInt(1, drugSellerId);
		pstmt.setInt(2, drug.getDrugId());
		pstmt.setInt(3, record.getQuantity());
		pstmt.setDouble(4, record.getQuantity()*drug.getBuyingPrice());
		pstmt.setString(5, record.getDescription());
		pstmt.setDate(6, new java.sql.Date(record.getDate().getTime()));
		return pstmt.executeUpdate();
	}
	
	public int recordDelete(Connection con, String recordId)throws Exception {
		String sql = "delete from consumerecord where recordId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setString(1, recordId);
		return pstmt.executeUpdate();
	}
	
	public int recordUpdate(Connection con, Consumerecord record)throws Exception {
		Drug drug = StudentDao.getNameById(con, record.getDrugName());
		DrugDao.drugUpdate(con, drug, record.getQuantity());//更新药品数量
		String sql = "update consumerecord set quantity=?,totalPrice=?,date=?,description=? where recordId=?";
		PreparedStatement pstmt=con.prepareStatement(sql);
		pstmt.setInt(1, record.getQuantity());
		pstmt.setDouble(2, record.getQuantity()*drug.getBuyingPrice());
		pstmt.setDate(3, new java.sql.Date(record.getDate().getTime()));
		pstmt.setString(4, record.getDescription());
		pstmt.setInt(5, record.getRecordId());
		return pstmt.executeUpdate();
	}
	public static Date stringToDate(String str) {  
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
        Date date = new Date();  
        // Fri Feb 24 00:00:00 CST 2012  
		try {
			date = format.parse(str);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        // 2012-02-24  
                                              
        return date;  
    }  
	
}
