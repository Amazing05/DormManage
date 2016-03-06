package com.lero.web;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lero.dao.DrugSellerDao;
import com.lero.model.Counter;
import com.lero.model.DrugSeller;
import com.lero.model.GenericType;
import com.lero.model.Login;
import com.lero.model.PageBean;
import com.lero.util.DbUtil;
import com.lero.util.PropertiesUtil;
import com.lero.util.StringUtil;

public class DrugSellerServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	DbUtil dbUtil = new DbUtil();
	DrugSellerDao drugSellerDao = new DrugSellerDao(); 
	
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
		String s_drugSellerText = request.getParameter("s_drugSellerText");
		String searchType = request.getParameter("searchType");
		String page = request.getParameter("page");
		String action = request.getParameter("action");
		DrugSeller drugSeller = new DrugSeller();
		if("preSave".equals(action)) {
			drugSellerPreSave(request, response); 
			return;
		} else if("save".equals(action)){
			drugSellerSave(request, response);
			return;
		} else if("delete".equals(action)){
			drugSellerDelete(request, response);
			return;
		} else 
			if("list".equals(action)) {
			if(StringUtil.isNotEmpty(s_drugSellerText)) {
				if("name".equals(searchType)) {
					drugSeller.setName(s_drugSellerText);
				} else if("userName".equals(searchType)) {
					//dormManager.setUserName(s_drugSellerText);
				}
			}
			session.removeAttribute("s_drugSellerText");
			session.removeAttribute("searchType");
			request.setAttribute("s_drugSellerText", s_drugSellerText);
			request.setAttribute("searchType", searchType);
		} else if("search".equals(action)){
			if (StringUtil.isNotEmpty(s_drugSellerText)) {
				if ("name".equals(searchType)) {
					drugSeller.setName(s_drugSellerText);
				} else if ("userName".equals(searchType)) {
					//dormManager.setUserName(s_drugSellerText);
				}
				session.setAttribute("searchType", searchType);
				session.setAttribute("s_drugSellerText", s_drugSellerText);
			} else {
				session.removeAttribute("s_drugSellerText");
				session.removeAttribute("searchType");
			}
		} else {
			if(StringUtil.isNotEmpty(s_drugSellerText)) {
				if("name".equals(searchType)) {
					drugSeller.setName(s_drugSellerText);
				} else if("userName".equals(searchType)) {
					//dormManager.setUserName(s_drugSellerText);
				}
				session.setAttribute("searchType", searchType);
				session.setAttribute("s_drugSellerText", s_drugSellerText);
			}
			if(StringUtil.isEmpty(s_drugSellerText)) {
				Object o1 = session.getAttribute("s_drugSellerText");
				Object o2 = session.getAttribute("searchType");
				if(o1!=null) {
					if("name".equals((String)o2)) {
						drugSeller.setName((String)o1);
					} else if("userName".equals((String)o2)) {
						//dormManager.setUserName((String)o1);
					}
				}
			}
		}
		if(StringUtil.isEmpty(page)) {
			page="1";
		}
		Connection con = null;
		PageBean pageBean = new PageBean(Integer.parseInt(page), Integer.parseInt(PropertiesUtil.getValue("pageSize")));
		request.setAttribute("pageSize", pageBean.getPageSize());
		request.setAttribute("page", pageBean.getPage());
		try {
			con=dbUtil.getCon();
			List< GenericType<DrugSeller,Counter,String>> drugSellerList = drugSellerDao.dormManagerList(con, pageBean, drugSeller);
			int total=drugSellerDao.dormManagerCount(con, drugSeller);
			String pageCode = this.genPagation(total, Integer.parseInt(page), Integer.parseInt(PropertiesUtil.getValue("pageSize")));
			request.setAttribute("pageCode", pageCode);
			request.setAttribute("drugSellerList", drugSellerList);
			request.setAttribute("mainPage", "admin/drugSeller.jsp");
			request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
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

	private void drugSellerDelete(HttpServletRequest request,
			HttpServletResponse response) {
		String drugSellerId = request.getParameter("drugSellerId");
		Connection con = null;
		try {
			con = dbUtil.getCon();
			drugSellerDao.drugSellerDelete(con, drugSellerId);
			request.getRequestDispatcher("drugSeller?action=list").forward(request, response);
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

	private void drugSellerSave(HttpServletRequest request, 
			HttpServletResponse response)throws ServletException, IOException {
		String drugSellerId = request.getParameter("drugSellerId");
		String userName = request.getParameter("userName");
		String userPassword = request.getParameter("password");
		String name = request.getParameter("name");
		String gender = request.getParameter("gender");
		String tel = request.getParameter("tel");
//		DormManager dormManager = new DormManager(userName, password, name, sex, tel);
		DrugSeller drugSeller=new DrugSeller();
		drugSeller.setName(name);
		drugSeller.setGender(gender);
		drugSeller.setTel(tel);
		Login login=new Login();
		login.setUserName(userName);
		login.setUserPassword(userPassword);
		if(StringUtil.isNotEmpty(drugSellerId)) {
			drugSeller.setDrugSellerId(Integer.parseInt(drugSellerId));
		}
		Connection con = null;
		try {
			con = dbUtil.getCon();
			int saveNum = 0;
			if(StringUtil.isNotEmpty(drugSellerId) && !(drugSellerId.equals("0"))) { //如果drugSellerId不为空，执行更新操作
				saveNum = drugSellerDao.drugSellerUpdate(con, drugSeller);
				saveNum = drugSellerDao.loginUpdate(con, login, Integer.parseInt(drugSellerId));
			} else if(drugSellerDao.haveManagerByUser(con, login.getUserName())){
				 GenericType<DrugSeller,Login,String>  g = new GenericType<DrugSeller,Login,String> (drugSeller,login,"");
				//request.setAttribute("dormManager", drugSeller);
				 g.getT1().setDrugSellerId(0);//重复时设置为0
				 request.setAttribute("genericType", g);
				request.setAttribute("error", "该用户名已存在");
				request.setAttribute("mainPage", "admin/drugSellerSave.jsp");
				request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
				try {
					dbUtil.closeCon(con);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			} else {
				saveNum = drugSellerDao.drugSellerAdd(con, drugSeller);
				drugSellerDao.loginAdd(con, login);//登录表添加用户
			}
			if(saveNum > 0) {
				request.getRequestDispatcher("drugSeller?action=list").forward(request, response);
			} else {
				request.setAttribute("dormManager", drugSeller);
				request.setAttribute("error", "保存失败");
				request.setAttribute("mainPage", "dormManager/drugSellerSave.jsp");
				request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
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

	private void drugSellerPreSave(HttpServletRequest request,
			HttpServletResponse response)throws ServletException, IOException {
		String drugSeller = request.getParameter("drugSellerId");
		if(StringUtil.isNotEmpty(drugSeller)) {
			Connection con = null;
			try { 
				con = dbUtil.getCon();
				GenericType<DrugSeller,Login,String>  g = drugSellerDao.drugSellerShow(con, drugSeller);
				request.setAttribute("genericType", g);
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
		request.setAttribute("mainPage", "admin/drugSellerSave.jsp");
		request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
	}

	private String genPagation(int totalNum, int currentPage, int pageSize){
		int totalPage = totalNum%pageSize==0?totalNum/pageSize:totalNum/pageSize+1;
		StringBuffer pageCode = new StringBuffer();
		pageCode.append("<li><a href='drugSeller?page=1'>首页</a></li>");
		if(currentPage==1) {
			pageCode.append("<li class='disabled'><a href='#'>上一页</a></li>");
		}else {
			pageCode.append("<li><a href='drugSeller?page="+(currentPage-1)+"'>上一页</a></li>");
		}
		for(int i=currentPage-2;i<=currentPage+2;i++) {
			if(i<1||i>totalPage) {
				continue;
			}
			if(i==currentPage) {
				pageCode.append("<li class='active'><a href='#'>"+i+"</a></li>");
			} else {
				pageCode.append("<li><a href='drugSeller?page="+i+"'>"+i+"</a></li>");
			}
		}
		if(currentPage==totalPage) {
			pageCode.append("<li class='disabled'><a href='#'>下一页</a></li>");
		} else {
			pageCode.append("<li><a href='drugSeller?page="+(currentPage+1)+"'>下一页</a></li>");
		}
		pageCode.append("<li><a href='drugSeller?page="+totalPage+"'>尾页</a></li>");
		return pageCode.toString();
	}
	
}
