package com.lero.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.lero.model.Admin;
import com.lero.model.DormManager;
import com.lero.model.DrugSeller;
import com.lero.model.Login;
import com.lero.model.Student;

public class UserDao {

	public Admin Login(Connection con, Admin admin)throws Exception {
		Admin resultAdmin = null;
		String sql = "select * from login where userName=? and userPassword=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, admin.getUserName());
		pstmt.setString(2, admin.getPassword());
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			resultAdmin = new Admin();
//			resultAdmin.setAdminId(rs.getInt("id"));
			resultAdmin.setUserName(rs.getString("userName"));
			resultAdmin.setPassword(rs.getString("userPassword"));
//			resultAdmin.setName(rs.getString("name"));
//			resultAdmin.setSex(rs.getString("sex"));
//			resultAdmin.setTel(rs.getString("tel"));
		}
		return resultAdmin;
	}
	public Login loginAdmin(Connection con, String name,String password )throws Exception {
		String sql = "select * from login where userName=? and userPassword=? and role=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, name);
		pstmt.setString(2, password);
		pstmt.setString(3, "admin");
		ResultSet rs = pstmt.executeQuery();
		Login login=new Login();
		if(rs.next()) {
			login.setUserName(name);
			login.setUserPassword(password);
			login.setUserId(rs.getInt("userId"));
			login.setLoginId(rs.getInt("loginId"));
		}
		return login;
	}
	public DrugSeller login(Connection con, String name,String password )throws Exception {
		String sql = "select * from login where userName=? and userPassword=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, name);
		pstmt.setString(2, password);
		ResultSet rs = pstmt.executeQuery();
		DrugSeller drugSeller=new DrugSeller();
		if(rs.next()) {
			int userId=rs.getInt("userId");
			String sqlTemp = "select * from drugseller where drugsellerId=? ";
			PreparedStatement pstmtTemp = con.prepareStatement(sqlTemp);
			pstmtTemp.setInt(1, userId);
			ResultSet rsTemp = pstmtTemp.executeQuery();
			if(rsTemp.next()) {
				drugSeller.setDrugSellerId(userId);
				drugSeller.setName(rsTemp.getString("name"));
				drugSeller.setCounterId(rsTemp.getInt("counterId"));
			}
		}
		return drugSeller;
	}
	public DormManager Login(Connection con, DormManager dormManager)throws Exception {
		DormManager resultDormManager = null;
		String sql = "select * from t_dormmanager where userName=? and password=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, dormManager.getUserName());
		pstmt.setString(2, dormManager.getPassword());
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			resultDormManager = new DormManager();
			resultDormManager.setDormManagerId(rs.getInt("dormManId"));
			resultDormManager.setUserName(rs.getString("userName"));
			resultDormManager.setPassword(rs.getString("password"));
			resultDormManager.setDormBuildId(rs.getInt("dormBuildId"));
			resultDormManager.setName(rs.getString("name"));
			resultDormManager.setSex(rs.getString("sex"));
			resultDormManager.setTel(rs.getString("tel"));
		}
		return resultDormManager;
	}
	
	public Student Login(Connection con, Student student)throws Exception {
		Student resultStudent = null;
		String sql = "select * from t_student where stuNum=? and password=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, student.getStuNumber());
		pstmt.setString(2, student.getPassword());
		ResultSet rs = pstmt.executeQuery();
		if(rs.next()) {
			resultStudent = new Student();
			resultStudent.setStudentId(rs.getInt("studentId"));
			resultStudent.setStuNumber(rs.getString("stuNum"));
			resultStudent.setPassword(rs.getString("password"));
			int dormBuildId = rs.getInt("dormBuildId");
			resultStudent.setDormBuildId(dormBuildId);
			//resultStudent.setDormBuildName(DormBuildDao.dormBuildName(con, dormBuildId));
			resultStudent.setDormName(rs.getString("dormName"));
			resultStudent.setName(rs.getString("name"));
			resultStudent.setSex(rs.getString("sex"));
			resultStudent.setTel(rs.getString("tel"));
		}
		return resultStudent;
	}
	
	public int adminUpdate(Connection con, int adminId, String password)throws Exception {
		String sql = "update login set userPassword=? where loginId=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, password);
		pstmt.setInt(2, adminId);
		return pstmt.executeUpdate();
	}
	
	
	public int managerUpdate(Connection con, int managerId, String password)throws Exception {
		String sql = "update t_dormmanager set password=? where dormManId=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, password);
		pstmt.setInt(2, managerId);
		return pstmt.executeUpdate();
	}
	
	public int studentUpdate(Connection con, int studentId, String password)throws Exception {
		String sql = "update t_student set password=? where studentId=?";
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, password);
		pstmt.setInt(2, studentId);
		return pstmt.executeUpdate();
	}
	
}
