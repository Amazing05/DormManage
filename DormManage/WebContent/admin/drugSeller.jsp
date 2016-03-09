<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script type="text/javascript">
	function dormManagerDelete(drugSellerId) {
		if(confirm("您确定要删除这个宿管吗？")) {
			window.location="drugSeller?action=delete&drugSellerId="+drugSellerId;
		}
	}
	
	$(document).ready(function(){
		$("ul li:eq(1)").addClass("active");
	});
</script>
<div class="data_list">
		<div class="data_list_title">
			售药员员管理
		</div>
		<form name="myForm" class="form-search" method="post" action="drugSeller?action=search">
				<button class="btn btn-success" type="button" style="margin-right: 50px;" onclick="javascript:window.location='drugSeller?action=preSave'">添加</button>
				<span class="data_search">
					<select id="searchType" name="searchType" style="width: 80px;">
					<option value="name">姓名</option>
					<!--  <option value="userName" ${searchType eq "userName"?'selected':'' }>用户名</option>-->
					</select>
					&nbsp;<input id="s_drugSellerText" name="s_drugSellerText" type="text"  style="width:120px;height: 30px;" class="input-medium search-query" value="${s_drugSellerText }">
					&nbsp;<button type="submit" class="btn btn-info" onkeydown="if(event.keyCode==13) myForm.submit()">搜索</button>
				</span>
		</form>
		<div>
			<table class="table table-hover table-striped table-bordered">
				<tr>
					<th>编号</th>
					<th>姓名</th>
					<th>性别</th>
					<th>电话</th>
					<th>柜台</th>
<!-- 					<th>用户名</th> -->
					<th>操作</th>
				</tr>
				<c:forEach  varStatus="i" var="drugSellerList" items="${drugSellerList }">
					<tr>
						<td>${i.count+(page-1)*pageSize }</td>
						<td>${drugSellerList.t1.name }</td>
						<td>${drugSellerList.t1.gender }</td>
						<td>${drugSellerList.t1.tel }</td>
						<td>${drugSellerList.t2.name==null?"无":drugSellerList.t2.name }</td>
<%-- 						<td>${drugSellerList.name }</td> --%>
						<td><button class="btn btn-mini btn-info" type="button" onclick="javascript:window.location='drugSeller?action=preSave&drugSellerId=${drugSellerList.t1.drugSellerId }'">修改</button>&nbsp;
							<button class="btn btn-mini btn-danger" type="button" onclick="dormManagerDelete(${drugSellerList.t1.drugSellerId})">删除</button></td>
					</tr>
				</c:forEach>
			</table>
		</div>
		<div align="center"><font color="red">${error }</font></div>
		<div class="pagination pagination-centered">
			<ul>
				${pageCode }
			</ul>
		</div>
</div>