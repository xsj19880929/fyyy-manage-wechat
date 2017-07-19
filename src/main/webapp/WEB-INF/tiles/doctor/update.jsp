<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    <h2>添加/编辑医生</h2>
<div class="row-fluid">
	<div class="span12">
		<button class="btn btn-primary form_submit_btn" data-target="#userForm">保存</button>
		<button class="btn form_reset_btn" data-target="#userForm">重置</button>
	</div>
</div>
<form class="form-horizontal need_validate" id="userForm"
	action="doctor/add" method="post">
	<div class="control-group">
			<label class="control-label">医生编号<b class="must">*</b>：</label>
			<div class="controls">
				<input type="text" name="doctorId" id="doctorId"
					data-rule-required="true" data-msg-required="医生编号不能为空"
					<c:if test="${!empty bean.doctorId}">value="${bean.doctorId}"</c:if> />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">医生加密编号<b class="must">*</b>：</label>
			<div class="controls">
				<input type="text" name="doctorSn" id="doctorSn"
					data-rule-required="true" data-msg-required="医生加密编号"
					<c:if test="${!empty bean.doctorSn}">value="${bean.doctorSn}"</c:if> />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">医生名<b class="must">*</b>：</label>
			<div class="controls">
				<input type="text" name="doctor" id="doctor"
					data-rule-required="true" data-msg-required="医生名不能为空"
					<c:if test="${!empty bean.doctor}">value="${bean.doctor}"</c:if> />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">科室<b class="must">*</b>：</label>
			<div class="controls">
			<select name="deptId" data-rule-required="true" data-msg-required="科室不能为空">
			<c:forEach var="department" items="${departments}" >
				<option value="${department.id}" <c:if test="${bean.deptId==department.id}">selected</c:if>>${department.deptName}</option>
			</c:forEach>
			</select>
			</div>
		</div>
	<input type="hidden" name="id" id="id"
		<c:if test="${!empty bean.id}">value="${bean.id}"</c:if> />
</form>
