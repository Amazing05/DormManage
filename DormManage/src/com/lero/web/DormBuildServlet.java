package com.lero.web;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lero.dao.DormBuildDao;
import com.lero.model.Counter;
import com.lero.model.DormBuild;
import com.lero.model.DormManager;
import com.lero.model.DrugSeller;
import com.lero.model.PageBean;
import com.lero.util.DbUtil;
import com.lero.util.PropertiesUtil;
import com.lero.util.StringUtil;

public class DormBuildServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	DbUtil dbUtil = new DbUtil();
	DormBuildDao dormBuildDao = new DormBuildDao();
	
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
		String s_dormBuildName = request.getParameter("s_dormBuildName");
		String page = request.getParameter("page");
		String action = request.getParameter("action");
		//DormBuild dormBuild = new DormBuild();
		Counter counter=new Counter();
		if("preSave".equals(action)) {
			counterPreSave(request, response);
			return;
		} else if("save".equals(action)){
			counterSave(request, response);
			return;
		} else if("delete".equals(action)){
			counterDelete(request, response);
			return;
		} else if("manager".equals(action)){
			counterManager(request, response);
			return;
		} else if("addManager".equals(action)){
			counterAddDrugSeller(request, response);
		} else if("move".equals(action)){
			managerMove(request, response);
		} else if("list".equals(action)) {
			if(StringUtil.isNotEmpty(s_dormBuildName)) {
				//dormBuild.setDormBuildName(s_dormBuildName);
				counter.setName(s_dormBuildName);
			}
			session.removeAttribute("s_dormBuildName");
			request.setAttribute("s_dormBuildName", s_dormBuildName);
		} else if("search".equals(action)){
			if(StringUtil.isNotEmpty(s_dormBuildName)) {
				//dormBuild.setDormBuildName(s_dormBuildName);
				counter.setName(s_dormBuildName);
				session.setAttribute("s_dormBuildName", s_dormBuildName);
			}else {
				session.removeAttribute("s_dormBuildName");
			}
		} else {
			if(StringUtil.isNotEmpty(s_dormBuildName)) {
				//dormBuild.setDormBuildName(s_dormBuildName);
				counter.setName(s_dormBuildName);
				session.setAttribute("s_dormBuildName", s_dormBuildName);
			}
			if(StringUtil.isEmpty(s_dormBuildName)) {
				Object o = session.getAttribute("s_dormBuildName");
				if(o!=null) {
					//dormBuild.setDormBuildName((String)o);
					counter.setName(s_dormBuildName);
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
			List<Counter> counterList = dormBuildDao.counterList(con, pageBean, counter);
			int total=dormBuildDao.counterCount(con, counter);
			String pageCode = this.genPagation(total, Integer.parseInt(page), Integer.parseInt(PropertiesUtil.getValue("pageSize")));
			request.setAttribute("pageCode", pageCode);
			request.setAttribute("counterList", counterList);
			request.setAttribute("mainPage", "admin/dormBuild.jsp");
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
	
	private void managerMove(HttpServletRequest request,
			HttpServletResponse response) {
		String dormBuildId = request.getParameter("dormBuildId");
		String dormManagerId = request.getParameter("dormManagerId");
		Connection con = null;
		try {
			con = dbUtil.getCon();
			dormBuildDao.managerUpdateWithId(con, dormManagerId, "0");
			request.getRequestDispatcher("dormBuild?action=manager&dormBuildId="+dormBuildId).forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void counterAddDrugSeller(HttpServletRequest request,
			HttpServletResponse response) {
		String dormBuildId = request.getParameter("dormBuildId");
		String dormManagerId = request.getParameter("dormManagerId");
		Connection con = null;
		try {
			con = dbUtil.getCon();
			dormBuildDao.managerUpdateWithId(con, dormManagerId, dormBuildId);
			request.getRequestDispatcher("dormBuild?action=manager&dormBuildId="+dormBuildId).forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void counterManager(HttpServletRequest request, 
			HttpServletResponse response) {
		String counterId = request.getParameter("dormBuildId");
		Connection con = null;
		try {
			con = dbUtil.getCon();
			List<DrugSeller> drugSellerListWithId = dormBuildDao.getdrugSellerWithcounterId(con, counterId);//获取该柜台所有售药员
			List<DrugSeller> drugSellerrListToSelect = dormBuildDao.getDrugSellerWithoutBuild(con);//获取所有售药员
			request.setAttribute("dormBuildId", counterId);
			request.setAttribute("managerListWithId", drugSellerListWithId);
			request.setAttribute("managerListToSelect", drugSellerrListToSelect);
			request.setAttribute("mainPage", "admin/selectManager.jsp"); 
			request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void counterDelete(HttpServletRequest request, 
			HttpServletResponse response) {
		String dormBuildId = request.getParameter("dormBuildId");
		Connection con = null;
		try {
			con = dbUtil.getCon();
			/*if(dormBuildDao.existManOrDormWithId(con, dormBuildId)) {
				request.setAttribute("error", "宿舍楼下有宿舍或宿管，不能删除该宿舍楼");
			} else {*/
				dormBuildDao.counterDelete(con, dormBuildId);
			//}
			request.getRequestDispatcher("dormBuild?action=list").forward(request, response);
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

	private void counterSave(HttpServletRequest request,
			HttpServletResponse response)throws ServletException, IOException {
		String counterId = request.getParameter("dormBuildId"); 
		String name = request.getParameter("dormBuildName");
		String description = request.getParameter("detail");
		//DormBuild dormBuild = new DormBuild(name, description); 
		Counter counter=new Counter();
		counter.setDescription(description);
		counter.setName(name);
		if(StringUtil.isNotEmpty(counterId)) {
			//dormBuild.setDormBuildId(Integer.parseInt(counterId));
			counter.setCounterId(Integer.parseInt(counterId));
		}
		Connection con = null;
		try {
			con = dbUtil.getCon();
			int saveNum = 0;
			if(StringUtil.isNotEmpty(counterId)) {
				saveNum = dormBuildDao.counterUpdate(con, counter);
			} else {
				saveNum = dormBuildDao.counterAdd(con, counter);
			}
			if(saveNum > 0) {
				request.getRequestDispatcher("dormBuild?action=list").forward(request, response);
			} else {
				request.setAttribute("dormBuild", counter);
				request.setAttribute("error", "保存失败");
				request.setAttribute("mainPage", "dormBuild/dormBuildSave.jsp");
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

	private void counterPreSave(HttpServletRequest request, 
			HttpServletResponse response)throws ServletException, IOException {
		String counterId = request.getParameter("dormBuildId");
		if(StringUtil.isNotEmpty(counterId)) {
			Connection con = null;
			try {
				con = dbUtil.getCon();
				Counter counter = dormBuildDao.counterShow(con, counterId);
				request.setAttribute("dormBuild", counter); 
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
		request.setAttribute("mainPage", "admin/dormBuildSave.jsp");
		request.getRequestDispatcher("mainAdmin.jsp").forward(request, response);
	}

	private String genPagation(int totalNum, int currentPage, int pageSize){
		int totalPage = totalNum%pageSize==0?totalNum/pageSize:totalNum/pageSize+1;
		StringBuffer pageCode = new StringBuffer();
		pageCode.append("<li><a href='dormBuild?page=1'>首页</a></li>");
		if(currentPage==1) {
			pageCode.append("<li class='disabled'><a href='#'>上一页</a></li>");
		}else {
			pageCode.append("<li><a href='dormBuild?page="+(currentPage-1)+"'>上一页</a></li>");
		}
		for(int i=currentPage-2;i<=currentPage+2;i++) {
			if(i<1||i>totalPage) {
				continue;
			}
			if(i==currentPage) {
				pageCode.append("<li class='active'><a href='#'>"+i+"</a></li>");
			} else {
				pageCode.append("<li><a href='dormBuild?page="+i+"'>"+i+"</a></li>");
			}
		}
		if(currentPage==totalPage) {
			pageCode.append("<li class='disabled'><a href='#'>下一页</a></li>");
		} else {
			pageCode.append("<li><a href='dormBuild?page="+(currentPage+1)+"'>下一页</a></li>");
		}
		pageCode.append("<li><a href='dormBuild?page="+totalPage+"'>尾页</a></li>");
		return pageCode.toString();
	}
	
}
