<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    <h2>添加/编辑用户</h2>
<div class="row-fluid">
	<div class="span12">
		<button class="btn btn-primary form_submit_btn" data-target="#userForm">保存</button>
		<button class="btn form_reset_btn" data-target="#userForm">重置</button>
	</div>
</div>
<form class="form-horizontal need_validate" id="userForm"
	action="system/user/add" method="post">
	<div class="control-group">
		<label class="control-label">角色：</label>
		<div class="controls">
			<select name="roleId" data-rule-required="true"
				data-msg-required="角色不能为空">
				<c:forEach var="role" items="${roles}">
					<option value="${role.id}"
						<c:if test="${!empty bean.roleId} and ${role.id eq bean.roleId}">selected="selected"</c:if>>${role.name}</option>
				</c:forEach>
			</select>
		</div>
	</div>
	<div class="control-group">
			<label class="control-label">名称<b class="must">*</b>：</label>
			<div class="controls">
				<input type="text" name="name" id="name"
					data-rule-required="true" data-msg-required="用户名不能为空"
					<c:if test="${!empty bean.name}">value="${bean.name}"</c:if> />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">手机号码<b class="must">*</b>：</label>
			<div class="controls">
				<input type="text" name="phone" id="phone"
					data-store="<c:if test="${!empty bean.phone}">${bean.phone}</c:if>"
					data-rule-required="true" data-msg-required="手机号码不能为空"
					data-rule-remotecheck="system/user/checkPhone"
					data-msg-remotecheck="手机号码已存在"
					<c:if test="${!empty bean.phone}">value="${bean.phone}"</c:if> />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">是否可用：</label>
			<div class="controls">
				<select name="status">
					<option value="1"
						<c:if test="${(!empty bean.status) and (bean.status eq '1')}">selected="selected"</c:if>>可用</option>
					<option value="0"
						<c:if test="${(!empty bean.status) and (bean.status eq '0')}">selected="selected"</c:if>>不可用</option>
				</select>
			</div>
		</div>
	<input type="hidden" name="id" id="id"
		<c:if test="${!empty bean.id}">value="${bean.id}"</c:if> />
</form>
