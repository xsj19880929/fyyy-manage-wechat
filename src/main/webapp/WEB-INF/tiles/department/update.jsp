<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    <h2>添加/编辑科室</h2>
<div class="row-fluid">
	<div class="span12">
		<button class="btn btn-primary form_submit_btn" data-target="#userForm">保存</button>
		<button class="btn form_reset_btn" data-target="#userForm">重置</button>
	</div>
</div>
<form class="form-horizontal need_validate" id="userForm"
	action="department/add" method="post">
	<div class="control-group">
			<label class="control-label">科室编号<b class="must">*</b>：</label>
			<div class="controls">
				<input type="text" name="triageNo" id="triageNo"
					data-rule-required="true" data-msg-required="科室编号不能为空"
					<c:if test="${!empty bean.triageNo}">value="${bean.triageNo}"</c:if> />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">科室名<b class="must">*</b>：</label>
			<div class="controls">
				<input type="text" name="deptName" id="deptName"
					data-rule-required="true" data-msg-required="科室名不能为空"
					<c:if test="${!empty bean.deptName}">value="${bean.deptName}"</c:if> />
			</div>
		</div>
	<input type="hidden" name="id" id="id"
		<c:if test="${!empty bean.id}">value="${bean.id}"</c:if> />
</form>
