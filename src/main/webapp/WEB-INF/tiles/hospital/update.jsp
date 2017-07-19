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
	action="hospital/add" method="post">
	<div class="control-group">
			<label class="control-label">医院编号<b class="must">*</b>：</label>
			<div class="controls">
				<input type="text" name="orgId" id="orgId"
					data-rule-required="true" data-msg-required="医院编号不能为空"
					<c:if test="${!empty bean.orgId}">value="${bean.orgId}"</c:if> />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">医院名<b class="must">*</b>：</label>
			<div class="controls">
				<input type="text" name="name" id="name"
					data-rule-required="true" data-msg-required="医院名不能为空"
					<c:if test="${!empty bean.name}">value="${bean.name}"</c:if> />
			</div>
		</div>
	<input type="hidden" name="id" id="id"
		<c:if test="${!empty bean.id}">value="${bean.id}"</c:if> />
</form>
