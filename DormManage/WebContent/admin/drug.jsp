<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script type="text/javascript">

$(document).ready(function(){
	$("ul li:eq(2)").addClass("active");
	$('.datatable').dataTable( {        				
		 "oLanguage": {
				"sUrl": "/DormManage/media/zh_CN.json"
		 },
		"bLengthChange": false, //改变每页显示数据数量
		"bFilter": false, //过滤功能
		"aoColumns": [
			null,
			null,
			null,
			null,
			null,
			{ "asSorting": [ ] },
			{ "asSorting": [ ] }
		]
	});
});

window.onload = function(){ 
	$("#DataTables_Table_0_wrapper .row-fluid").remove();
};
	function drugDelete(drugId) {
		if(confirm("您确定要删除这条记录吗？")) {
			window.location="drug?action=delete&drugId="+drugId;
		}
	}
</script>
<style type="text/css">
	.span6 {
		width:0px;
		height: 0px;
		padding-top: 0px;
		padding-bottom: 0px;
		margin-top: 0px;
		margin-bottom: 0px;
	}

</style>
<div class="data_list">
		<div class="data_list_title">
			药品管理
		</div>
		<form name="myForm" class="form-search" method="post" action="student?action=search" style="padding-bottom: 0px">
				<button class="btn btn-success" type="button" style="margin-right: 50px;" onclick="javascript:window.location='drug?action=preSave'">添加</button>
				<span class="data_search">
					<!--  <select id="buildToSelect" name="buildToSelect" style="width: 110px;">
					<option value="">全部宿舍楼</option>
					<c:forEach var="dormBuild" items="${dormBuildList }">
						<option value="${dormBuild.dormBuildId }" ${buildToSelect==dormBuild.dormBuildId?'selected':'' }>${dormBuild.dormBuildName }</option>
					</c:forEach>
					</select> -->
					<select id="searchType" name="searchType" style="width: 80px;">
					<option value="name">名称</option>
					<!--  <option value="number" ${searchType eq "number"?'selected':'' }>学号</option> -->
					<option value="counter" ${searchType eq "counter"?'selected':'' }>柜台</option>
					</select>
					&nbsp;<input id="s_studentText" name="s_studentText" type="text"  style="width:120px;height: 30px;" class="input-medium search-query" value="${s_studentText }">
					&nbsp;<button type="submit" class="btn btn-info" onkeydown="if(event.keyCode==13) myForm.submit()">搜索</button>
				</span>
		</form>
		<div>
			<table class="table table-striped table-bordered table-hover datatable">
				<thead>
					<tr>
					<!-- <th>编号</th> -->
					<th>名称</th>
					<th>售价</th>
					<th>进价</th>
					<th>柜台名称</th>
<!-- 					<th>有效期</th> -->
					<th>库存</th>
					<th>说明</th>
					<th>操作</th>
				</tr>
				</thead>
				<tbody>
				<c:forEach  varStatus="i" var="item" items="${list }">
					<tr>
						<%-- <td>${i.count+(page-1)*pageSize }</td> --%>
						<td>${item.t1.name }</td>
						<td>${item.t1.buyingPrice }</td>
						<td>${item.t1.sellingPrice }</td>
						<td>${item.t2.name==null?"无":item.t2.name }</td>
<%-- 						<td>${item.t1.deadline }</td> --%>
						<td>${item.t1.quantity }</td>
						<td>${item.t1.description}</td>
<%-- 						<td>${student.tel }</td> --%>
						<td><button class="btn btn-mini btn-info" type="button" onclick="javascript:window.location='drug?action=preSave&studentId=${item.t1.drugId }'">修改</button>&nbsp;
							<button class="btn btn-mini btn-danger" type="button" onclick="drugDelete(${item.t1.drugId })">删除</button></td>
					</tr>
				</c:forEach>
				</tbody>
			</table>
		</div>
		<div align="center"><font color="red">${error }</font></div>
		<%-- <div class="pagination pagination-centered">
			<ul>
				${pageCode }
			</ul>
		</div> --%>
</div>