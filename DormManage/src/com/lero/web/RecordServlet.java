package com.lero.web;

import java.io.IOException;
import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lero.dao.DormBuildDao;
import com.lero.dao.DrugDao;
import com.lero.dao.RecordDao;
import com.lero.dao.StudentDao;
import com.lero.model.Consumerecord;
import com.lero.model.DormManager;
import com.lero.model.Drug;
import com.lero.model.DrugSeller;
import com.lero.model.Record;
import com.lero.model.Student;
import com.lero.util.DbUtil;
import com.lero.util.StringUtil;

public class RecordServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	DbUtil dbUtil = new DbUtil();
	RecordDao recordDao = new RecordDao();
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();
		Object currentUserType = session.getAttribute("currentUserType");
		String s_studentText = request.getParameter("s_studentText");
		String dormBuildId = request.getParameter("buildToSelect");
		String searchType = request.getParameter("searchType");
		String action = request.getParameter("action");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String drugName="";
		Record record = new Record();
		Consumerecord consumerecord=new Consumerecord();
		if("preSave".equals(action)) {
			recordPreSave(request, response);
			return;
		} else if("save".equals(action)){
	
					recordSave(request, response);
				
			return;
		} else if("delete".equals(action)){
			recordDelete(request, response);
			return;
		} else if("list".equals(action)) {
			if(StringUtil.isNotEmpty(s_studentText)) {
				if("name".equals(searchType)) {
					//record.setStudentName(s_studentText);
					drugName=s_studentText;
				} else if("number".equals(searchType)) {
					record.setStudentNumber(s_studentText);
				} else if("dorm".equals(searchType)) {
					record.setDormName(s_studentText);
				}
			}
			if(StringUtil.isNotEmpty(dormBuildId)) {
				record.setDormBuildId(Integer.parseInt(dormBuildId));
			}
			session.removeAttribute("s_studentText");
			session.removeAttribute("searchType");
			session.removeAttribute("buildToSelect");
			request.setAttribute("s_studentText", s_studentText);
			request.setAttribute("searchType", searchType);
			request.setAttribute("buildToSelect", dormBuildId);
		} else if("search".equals(action)){
			if(StringUtil.isNotEmpty(s_studentText)) {
				if("name".equals(searchType)) {
					drugName=s_studentText;
					record.setStudentName(s_studentText);
				} else if("number".equals(searchType)) {
					record.setStudentNumber(s_studentText);
				} else if("dorm".equals(searchType)) {
					record.setDormName(s_studentText);
				}
				session.setAttribute("s_studentText", s_studentText);
				session.setAttribute("searchType", searchType);
			} else {
				session.removeAttribute("s_studentText");
				session.removeAttribute("searchType");
			}
			if(StringUtil.isNotEmpty(startDate)) {
				record.setStartDate(startDate);
				session.setAttribute("startDate", startDate);
			} else {
				session.removeAttribute("startDate");
			}
			if(StringUtil.isNotEmpty(endDate)) {
				record.setEndDate(endDate);
				session.setAttribute("endDate", endDate);
			} else {
				session.removeAttribute("endDate");
			}
			if(StringUtil.isNotEmpty(dormBuildId)) {
				record.setDormBuildId(Integer.parseInt(dormBuildId));
				session.setAttribute("buildToSelect", dormBuildId);
			}else {
				session.removeAttribute("buildToSelect");
			}
		} 
		Connection con = null;
		try {
			con=dbUtil.getCon();
			if("admin".equals((String)currentUserType)) {
				List<Consumerecord> consumerecordList=recordDao.recordListWithDrugSellerNoSellerId(con, drugName,startDate, endDate);
				request.setAttribute("recordList", consumerecordList);
				request.setAttribute("mainPage", "admin/record.jsp");
				request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
			} else if("dormManager".equals((String)currentUserType)) {
				//DormManager manager = (DormManager)(session.getAttribute("currentUser"));
				DrugSeller drugSeller=(DrugSeller)(session.getAttribute("currentUser"));
				//int buildId = manager.getDormBuildId();
				int drugSellerId=drugSeller.getDrugSellerId();
				//String buildName = DormBuildDao.dormBuildName(con, buildId);
				String drugSellerName=drugSeller.getName();
				//List<Record> recordList = recordDao.recordListWithBuild(con, record, buildId);
				List<Consumerecord> consumerecordList=recordDao.recordListWithDrugSeller(con, drugName, drugSellerId, startDate, endDate);
				request.setAttribute("dormBuildName", drugSellerName);
				request.setAttribute("recordList", consumerecordList);
				request.setAttribute("mainPage", "dormManager/record.jsp");
				request.getRequestDispatcher("mainManager.jsp").forward(request, response);
			} else if("student".equals((String)currentUserType)) {
				Student student = (Student)(session.getAttribute("currentUser"));
				List<Record> recordList = recordDao.recordListWithNumber(con, record, student.getStuNumber());
				request.setAttribute("recordList", recordList);
				request.setAttribute("mainPage", "student/record.jsp");
				request.getRequestDispatcher("mainStudent.jsp").forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void recordDelete(HttpServletRequest request,
			HttpServletResponse response) {
		String recordId = request.getParameter("recordId");
		Connection con = null;
		try {
			con = dbUtil.getCon();
			Consumerecord consumerecord  =recordDao.getDrugIdByConsumerecordId(con, recordId);
			DrugDao.drugUpdateDelete(con, consumerecord);
			recordDao.recordDelete(con, recordId);
			request.getRequestDispatcher("record?action=list").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void recordSave(HttpServletRequest request,
			HttpServletResponse response) {
		String recordId = request.getParameter("recordId");
		String drugName=request.getParameter("drugName");
		String quantity = request.getParameter("quantity");
		String totalPrice = request.getParameter("totalPrice");
		java.text.SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd ");
		String date = request.getParameter("date");
		String description = request.getParameter("description");
		Consumerecord record = new Consumerecord(); 
		record.setDrugName(drugName);
	
		record.setQuantity(Integer.parseInt(quantity));
		record.setDescription(description);
			record.setDate(RecordDao.stringToDate(date));
		if(StringUtil.isNotEmpty(recordId)) {
			if(Integer.parseInt(recordId)!=0) {
				record.setRecordId(Integer.parseInt(recordId));
			}
		}
		Connection con = null;
		try {
			con = dbUtil.getCon();
			int saveNum = 0;
			HttpSession session = request.getSession();
			DrugSeller drugSeller=(DrugSeller)(session.getAttribute("currentUser"));
			Drug drug = StudentDao.getNameById(con,drugName);
			if(drug ==null) {
				request.setAttribute("record", record);
				request.setAttribute("error", "该药品不存在");
				request.setAttribute("mainPage", "dormManager/recordSave.jsp");
				request.getRequestDispatcher("mainManager.jsp").forward(request, response);
			} else {
				record.setDrugSellerId(drugSeller.getCounterId());

				if(StringUtil.isNotEmpty(recordId) && Integer.parseInt(recordId)!=0) {
					if(drug.getQuantity()-Integer.parseInt(quantity)<0){
						request.setAttribute("record", record);
						SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");   
						String sysDatetime = fmt.format(record.getDate());
						request.setAttribute("date", sysDatetime);
						request.setAttribute("error", "该药品余量不足");
						request.setAttribute("mainPage", "dormManager/recordSave.jsp");
						request.getRequestDispatcher("mainManager.jsp").forward(request, response);
						return;
					}
					saveNum = recordDao.recordUpdate(con, record);
				} else {
					if(drug.getQuantity()-Integer.parseInt(quantity)<0){
						SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");   
						String sysDatetime = fmt.format(record.getDate());
						request.setAttribute("date", sysDatetime);
						request.setAttribute("record", record);
						request.setAttribute("error", "该药品余量不足");
						request.setAttribute("mainPage", "dormManager/recordSave.jsp");
						request.getRequestDispatcher("mainManager.jsp").forward(request, response);
						return;
					}
					saveNum = recordDao.recordAdd(con, record,drugSeller.getDrugSellerId());
				}
				if(saveNum > 0) {
					request.getRequestDispatcher("record?action=list").forward(request, response);
				} else {
					request.setAttribute("record", record);
					request.setAttribute("error", "保存失败");
					request.setAttribute("mainPage", "dormManager/recordSave.jsp");
					request.getRequestDispatcher("mainManager.jsp").forward(request, response);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void recordPreSave(HttpServletRequest request,
			HttpServletResponse response)throws ServletException, IOException {
		String recordId = request.getParameter("recordId");
		String studentNumber = request.getParameter("studentNumber");
		Connection con = null;
		try {
			con = dbUtil.getCon();
			if (StringUtil.isNotEmpty(recordId)) {
				Consumerecord record = recordDao.recordShow(con, recordId);
				request.setAttribute("record", record);
			} else {
				Calendar rightNow = Calendar.getInstance();       
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");   
				String sysDatetime = fmt.format(rightNow.getTime());
				request.setAttribute("studentNumber", studentNumber);
				request.setAttribute("date", sysDatetime);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				dbUtil.closeCon(con);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		request.setAttribute("mainPage", "dormManager/recordSave.jsp");
		request.getRequestDispatcher("mainManager.jsp").forward(request, response);
	}
	/*else {
	if("admin".equals((String)currentUserType)) {
		if(StringUtil.isNotEmpty(s_studentText)) {
			if("name".equals(searchType)) {
				record.setStudentName(s_studentText);
			} else if("number".equals(searchType)) {
				record.setStudentNumber(s_studentText);
			} else if("dorm".equals(searchType)) {
				record.setDormName(s_studentText);
			}
			session.setAttribute("s_studentText", s_studentText);
			session.setAttribute("searchType", searchType);
		}
		if(StringUtil.isNotEmpty(dormBuildId)) {
			record.setDormBuildId(Integer.parseInt(dormBuildId));
			session.setAttribute("buildToSelect", dormBuildId);
		}
		if(StringUtil.isEmpty(s_studentText) && StringUtil.isEmpty(dormBuildId)) {
			Object o1 = session.getAttribute("s_studentText");
			Object o2 = session.getAttribute("searchType");
			Object o3 = session.getAttribute("buildToSelect");
			if(o1!=null) {
				if("name".equals((String)o2)) {
					record.setStudentName((String)o1);
				} else if("number".equals((String)o2)) {
					record.setStudentNumber((String)o1);
				} else if("dorm".equals((String)o2)) {
					record.setDormName((String)o1);
				}
			}
			if(o3 != null) {
				record.setDormBuildId(Integer.parseInt((String)o3));
			}
		}
	} else if("dormManager".equals((String)currentUserType)){
		if(StringUtil.isNotEmpty(s_studentText)) {
			if("name".equals(searchType)) {
				record.setStudentName(s_studentText);
			} else if("number".equals(searchType)) {
				record.setStudentNumber(s_studentText);
			} else if("dorm".equals(searchType)) {
				record.setDormName(s_studentText);
			}
			session.setAttribute("s_studentText", s_studentText);
			session.setAttribute("searchType", searchType);
		}
		if(StringUtil.isEmpty(s_studentText)) {
			Object o1 = session.getAttribute("s_studentText");
			Object o2 = session.getAttribute("searchType");
			if(o1!=null) {
				if("name".equals((String)o2)) {
					record.setStudentName((String)o1);
				} else if("number".equals((String)o2)) {
					record.setStudentNumber((String)o1);
				} else if("dorm".equals((String)o2)) {
					record.setDormName((String)o1);
				}
			}
		}
	}
}*/
	
}
