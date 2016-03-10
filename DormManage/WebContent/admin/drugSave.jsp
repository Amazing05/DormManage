<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<script type="text/javascript">
	function checkForm(){
		var drugName=document.getElementById("drugName").value;
		var buyingPrice=document.getElementById("buyingPrice").value;
		var sellingPrice=document.getElementById("sellingPrice").value;
		var counterName=document.getElementById("counterName").value;
// 		var dormName=document.getElementById("dormName").value;
		var quantity=document.getElementById("quantity").value;
// 		var sex=document.getElementById("sex").value;
// 		var deadline=document.getElementById("deadline").value;
		if(drugName==""||buyingPrice==""||sellingPrice==""||counterName==""||quantity==""){
			document.getElementById("error").innerHTML="信息填写不完整！";
			return false;
		} else if(password!=rPassword){
			document.getElementById("error").innerHTML="密码填写不一致！";
			return false;
		}
		return true;
	}
	
	$(document).ready(function(){
		$("ul li:eq(2)").addClass("active");
	});
</script>
<div class="data_list">
		<div class="data_list_title">
		<c:choose>
			<c:when test="${student.studentId!=null }">
				修改药品信息
			</c:when>
			<c:otherwise>
				添加新药品
			</c:otherwise>
		</c:choose>
		</div>
		<form action="drug?action=save" method="post" onsubmit="return checkForm()">
			<div class="data_form" >
				<input type="hidden" id="studentId" name="drugId" value="${drug.drugId }"/>
					<table align="center">
						<tr>
							<td><font color="red">*</font>名称：</td>
							<td><input type="text" id="drugName"  name="drugName" value="${drug.name }"  style="margin-top:5px;height:30px;" /></td>
						</tr>
						<tr>
							<td><font color="red">*</font>进价：</td>
							<td><input type="text" id="buyingPrice"  name="buyingPrice" value="${drug.buyingPrice }"  style="margin-top:5px;height:30px;" /></td>
						</tr>
						<tr>
							<td><font color="red">*</font>售价：</td>
							<td><input type="text" id="sellingPrice"  name="sellingPrice" value="${drug.sellingPrice }"  style="margin-top:5px;height:30px;" /></td>
						</tr>
						<!-- <tr>
							<td><font color="red">*</font>柜台：</td>
							<td><input type="text" id="counterName"  name="counterId" value="${student.name }"  style="margin-top:5px;height:30px;" /></td>
						</tr> -->
						<!--<tr>
							<td><font color="red">*</font>性别：</td>
							<td>
								<select id="sex" name="sex" style="width: 90px;">
									<option value="">请选择...</option>
									<option value="男" ${student.sex eq "男"?'selected':'' }>男</option>
									<option value="女" ${student.sex eq "女"?'selected':'' }>女</option>
								</select>
							</td>
						</tr> -->
						<tr>
							<td><font color="red">*</font>柜台：</td>
							<td>
								<select id="counterName" name="counterId" style="width: 90px;">
									<c:forEach var="counter" items="${counterList }">
										<option value="${counter.counterId }" ${drug.counterId==counter.counterId?'selected':'' }>${counter.name }</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>数量：</td>
							<td><input type="text" id="quantity"  name="quantity" value="${drug.quantity }"  style="margin-top:5px;height:30px;" /></td>
						</tr>
					<!--	<tr>
							<td><font color="red">*</font>有效期：</td>
							<td><input type="text" id="deadline"  name="deadline" value="${drug.deadline }"  style="margin-top:5px;height:30px;" /></td>
						</tr> -->
						<tr>
							<td>说明：</td>
							<td><input type="text" id="description"  name="description" value="${drug.description }"  style="margin-top:5px;height:30px;" /></td>		
						</tr>
					</table>
					<div align="center">
						<input type="submit" class="btn btn-primary" value="保存"/>
						&nbsp;<button class="btn btn-primary" type="button" onclick="javascript:window.location=history.back()">返回</button>
					</div>
					<div align="center">
						<font id="error" color="red">${error }</font>
					</div>
			</div>
		</form>
</div>