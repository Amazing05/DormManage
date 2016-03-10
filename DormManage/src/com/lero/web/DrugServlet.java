package com.lero.web;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lero.dao.CounterDao;
import com.lero.dao.DrugDao;
import com.lero.dao.StudentDao;
import com.lero.model.Counter;
import com.lero.model.Drug;
import com.lero.model.DrugSeller;
import com.lero.model.GenericType;
import com.lero.model.Student;
import com.lero.util.DbUtil;
import com.lero.util.StringUtil;

public class DrugServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	DbUtil dbUtil = new DbUtil();
	StudentDao studentDao = new StudentDao();
	DrugDao drugDao = new DrugDao();
	CounterDao counterDao = new CounterDao();

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		HttpSession session = request.getSession();
		Object currentUserType = session.getAttribute("currentUserType");
		String s_studentText = request.getParameter("s_studentText");
		String dormBuildId = request.getParameter("buildToSelect");
		String searchType = request.getParameter("searchType");
		String action = request.getParameter("action");
		Student student = new Student();
		Drug drug = new Drug();
		if ("preSave".equals(action)) {
			drugPreSave(request, response);
			return;
		} else if ("save".equals(action)) {
			drugSave(request, response);
			return;
		} else if ("delete".equals(action)) {
			drugDelete(request, response);
			return;
		} else if ("list".equals(action)) {
			if (StringUtil.isNotEmpty(s_studentText)) {
				if ("name".equals(searchType)) {
					drug.setName(s_studentText);
					// } else if("number".equals(searchType)) {
					// student.setStuNumber(s_studentText);
					// } else if("dorm".equals(searchType)) {
					// student.setDormName(s_studentText);
					// }
				}
				if (StringUtil.isNotEmpty(dormBuildId)) {
					student.setDormBuildId(Integer.parseInt(dormBuildId));
				}
				session.removeAttribute("s_studentText");
				session.removeAttribute("searchType");
				session.removeAttribute("buildToSelect");
				request.setAttribute("s_studentText", s_studentText);
				request.setAttribute("searchType", searchType);
				request.setAttribute("buildToSelect", dormBuildId);
			} else if ("search".equals(action)) {
				if (StringUtil.isNotEmpty(s_studentText)) {
					if ("name".equals(searchType)) {
						student.setName(s_studentText);
					} else if ("number".equals(searchType)) {
						student.setStuNumber(s_studentText);
					} else if ("dorm".equals(searchType)) {
						student.setDormName(s_studentText);
					}
					session.setAttribute("s_studentText", s_studentText);
					session.setAttribute("searchType", searchType);
				} else {
					session.removeAttribute("s_studentText");
					session.removeAttribute("searchType");
				}
				if (StringUtil.isNotEmpty(dormBuildId)) {
					student.setDormBuildId(Integer.parseInt(dormBuildId));
					session.setAttribute("buildToSelect", dormBuildId);
				} else {
					session.removeAttribute("buildToSelect");
				}
			} else {
				if ("admin".equals((String) currentUserType)) {
					if (StringUtil.isNotEmpty(s_studentText)) {
						if ("name".equals(searchType)) {
							student.setName(s_studentText);
						} else if ("number".equals(searchType)) {
							student.setStuNumber(s_studentText);
						} else if ("dorm".equals(searchType)) {
							student.setDormName(s_studentText);
						}
						session.setAttribute("s_studentText", s_studentText);
						session.setAttribute("searchType", searchType);
					}
					if (StringUtil.isNotEmpty(dormBuildId)) {
						student.setDormBuildId(Integer.parseInt(dormBuildId));
						session.setAttribute("buildToSelect", dormBuildId);
					}
					if (StringUtil.isEmpty(s_studentText)
							&& StringUtil.isEmpty(dormBuildId)) {
						Object o1 = session.getAttribute("s_studentText");
						Object o2 = session.getAttribute("searchType");
						Object o3 = session.getAttribute("buildToSelect");
						if (o1 != null) {
							if ("name".equals((String) o2)) {
								student.setName((String) o1);
							} else if ("number".equals((String) o2)) {
								student.setStuNumber((String) o1);
							} else if ("dorm".equals((String) o2)) {
								student.setDormName((String) o1);
							}
						}
						if (o3 != null) {
							student.setDormBuildId(Integer
									.parseInt((String) o3));
						}
					}
				} else if ("dormManager".equals((String) currentUserType)) {
					if (StringUtil.isNotEmpty(s_studentText)) {
						if ("name".equals(searchType)) {
							student.setName(s_studentText);
						} else if ("number".equals(searchType)) {
							student.setStuNumber(s_studentText);
						} else if ("dorm".equals(searchType)) {
							student.setDormName(s_studentText);
						}
						session.setAttribute("s_studentText", s_studentText);
						session.setAttribute("searchType", searchType);
					}
					if (StringUtil.isEmpty(s_studentText)) {
						Object o1 = session.getAttribute("s_studentText");
						Object o2 = session.getAttribute("searchType");
						if (o1 != null) {
							if ("name".equals((String) o2)) {
								student.setName((String) o1);
							} else if ("number".equals((String) o2)) {
								student.setStuNumber((String) o1);
							} else if ("dorm".equals((String) o2)) {
								student.setDormName((String) o1);
							}
						}
					}
				}
			}
			Connection con = null;
			try {
				con = dbUtil.getCon();
				if ("admin".equals((String) currentUserType)) {
					// List<Student> studentList = studentDao.studentList(con,
					// student);
					Map<String,String> condition = new HashMap<String,String>();
					condition.put("null", "null");
					List<GenericType<Drug, Counter, String>> list = drugDao
							.listDrugs(con,condition);
					request.setAttribute("dormBuildList",
							studentDao.dormBuildList(con));
					request.setAttribute("list", list);
					request.setAttribute("mainPage", "admin/drug.jsp");
					request.getRequestDispatcher("mainAdmin.jsp").forward(
							request, response);
				} else if ("dormManager".equals((String) currentUserType)) {
//					DormManager manager = (DormManager) (session
//							.getAttribute("currentUser"));
					DrugSeller drugSeller = (DrugSeller) (session
							 .getAttribute("currentUser"));
//					int buildId = manager.getDormBuildId();
					int counterId = drugSeller.getCounterId();
					String buildName="";
					//String buildName = DormBuildDao.dormBuildName(con, buildId);
//					List<Student> studentList = studentDao
//							.studentListWithBuild(con, student, buildId);
					List<Drug> drugList = drugDao.getDrugsByCounterId(con, counterId);
					request.setAttribute("dormBuildName", buildName);
					request.setAttribute("drugList", drugList);
					request.setAttribute("mainPage", "dormManager/drug.jsp");
					request.getRequestDispatcher("mainManager.jsp").forward(
							request, response);
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
		} else if("search".equals(action)){
			Map<String,String> condition = new HashMap<String,String>();
			
			if(currentUserType.equals("dormManager")){
				condition.put("name", s_studentText);
				drugSearch1(request,response,condition);
				return;
			}
			if(StringUtil.isNotEmpty(s_studentText)) {
				// 判断查询类型
				if("name".equals(searchType)) { 
					condition.put("name", s_studentText);
				} else if("counter".equals(searchType)) {
					condition.put("counter", s_studentText);
				}
				drugSearch(request,response,condition);
				return;
			} else {
				//如果查询内容为空，默认是list方法
			}
		}
		
		
	}

	private void drugDelete(HttpServletRequest request,
			HttpServletResponse response) {
		String drugId = request.getParameter("drugId");
		Connection con = null;
		try {
			con = dbUtil.getCon();
//			studentDao.studentDelete(con, studentId);
			drugDao.drugDelete(con, Integer.parseInt(drugId));
			request.getRequestDispatcher("drug?action=list").forward(
					request, response);
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

	private void drugSave(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("drugName");
		String buyingPrice = request.getParameter("buyingPrice");
		String sellingPrice = request.getParameter("sellingPrice");
		String counterId = request.getParameter("counterId");
		String quantity = request.getParameter("quantity");
		String description = request.getParameter("description");
		String deadline = request.getParameter("deadline");
		String drugId = request.getParameter("drugId");
//		String tel = request.getParameter("tel");
//		Student student = new Student(userName, password,
//				Integer.parseInt(dormBuildId), dormName, name, sex, tel);
		Drug drug = new Drug();
		drug.setBuyingPrice(Double.parseDouble(buyingPrice));
		drug.setDeadline(deadline);
		drug.setDescription(description);
		drug.setCounterId(Integer.parseInt(counterId));
		drug.setName(name);
		drug.setSellingPrice(Double.parseDouble(sellingPrice));
		if (StringUtil.isNotEmpty(drugId)) {
			drug.setDrugId(Integer.parseInt(drugId));
		}
		Connection con = null;
		try {
			con = dbUtil.getCon();
			int saveNum = 0;
			if (StringUtil.isNotEmpty(drugId)) {
//				saveNum = studentDao.studentUpdate(con, drug);
				saveNum = drugDao.drugUpdate(con,drug);
			} else if (drugId.equals("") && drugDao.haveName(con, drug.getName())) {
				request.setAttribute("drug", drug);
				request.setAttribute("error", "该药品已经存在了！");
				request.setAttribute("mainPage", "admin/studentSave.jsp");
				request.getRequestDispatcher("mainAdmin.jsp").forward(request,
						response);
				try {
					dbUtil.closeCon(con);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			} else {
				saveNum = drugDao.drugAdd(con, drug);
			}
			if (saveNum > 0) {
				request.getRequestDispatcher("drug?action=list").forward(
						request, response);
			} else {
				request.setAttribute("student", drug);
				request.setAttribute("error", "保存失败");
				request.setAttribute("mainPage", "admin/studentSave.jsp");
				request.getRequestDispatcher("mainAdmin.jsp").forward(request,
						response);
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

	private void drugPreSave(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String drugId = request.getParameter("studentId");
		Connection con = null;
		try {
			con = dbUtil.getCon();
			request.setAttribute("counterList", counterDao.listCounter(con));
			if (StringUtil.isNotEmpty(drugId)) {
//				Student student = studentDao.studentShow(con, studentId);
				Drug drug = drugDao.getDrugById(con, Integer.parseInt(drugId));
				request.setAttribute("drug", drug);
//				request.setAttribute("student", student);
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
		request.setAttribute("mainPage", "admin/drugSave.jsp");
		request.getRequestDispatcher("mainAdmin.jsp")
				.forward(request, response);
	}
	
	private void drugSearch(HttpServletRequest request,HttpServletResponse response,Map<String,String> condition) {
		Connection con = null;
		try{
			con = dbUtil.getCon();
			List<GenericType<Drug, Counter, String>> list = drugDao
					.listDrugs(con, condition);
			request.setAttribute("dormBuildList",
					studentDao.dormBuildList(con));
			request.setAttribute("list", list);
			request.setAttribute("mainPage", "admin/student.jsp");
			request.getRequestDispatcher("mainAdmin.jsp").forward(
					request, response);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try{
				dbUtil.closeCon(con);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void drugSearch1(HttpServletRequest request,HttpServletResponse response,Map<String,String> condition) {
		Connection con = null;
		try{
			con = dbUtil.getCon();
			List<GenericType<Drug,Counter,String>> listg = drugDao.listDrugs(con, condition); 
			List<Drug> list = new ArrayList<Drug>();
			for(GenericType<Drug,Counter,String> g : listg) {
				list.add(g.getT1());
			}
			request.setAttribute("dormBuildList",
					studentDao.dormBuildList(con));
			request.setAttribute("drugList", list);
			request.setAttribute("mainPage", "dormManager/student.jsp");
			request.getRequestDispatcher("mainManager.jsp").forward(
					request, response);
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try{
				dbUtil.closeCon(con);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

}
