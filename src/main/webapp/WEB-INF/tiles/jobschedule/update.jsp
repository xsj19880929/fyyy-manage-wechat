<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    <h2>添加/编辑任务</h2>
<div class="row-fluid">
	<div class="span12">
		<button class="btn btn-primary form_submit_btn" data-target="#userForm">保存</button>
		<button class="btn form_reset_btn" data-target="#userForm">重置</button>
	</div>
</div>
<form class="form-horizontal need_validate" id="userForm"
	action="jobschedule/add" method="post">
	<div class="control-group">
			<label class="control-label">任务名<b class="must">*</b>：</label>
			<div class="controls">
				<input type="text" name="jobName" id="jobName"
					data-rule-required="true" data-msg-required="任务名不能为空"
					<c:if test="${!empty bean.jobName}">value="${bean.jobName}"</c:if> />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">任务类<b class="must">*</b>：</label>
			<div class="controls">
				<input type="text" name="jobClass" id="jobClass"
					data-rule-required="true" data-msg-required="任务类不能为空"
					<c:if test="${!empty bean.jobClass}">value="${bean.jobClass}"</c:if> />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">cronExpression<b class="must">*</b>：</label>
			<div class="controls">
				<input type="text" name="cronExpression" id="cronExpression"
					data-rule-required="true" data-msg-required="cronExpression不能为空"
					<c:if test="${!empty bean.cronExpression}">value="${bean.cronExpression}"</c:if> />
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">任务状态<b class="must">*</b>：</label>
			<div class="controls">
				<select name="jobState" id="jobState" data-rule-required="true" data-msg-required="任务不能为空">
					<option value="0" <c:if test="${bean.jobState==0}">selected</c:if> >未启动</option>
					<option value="1" <c:if test="${bean.jobState==1}">selected</c:if> >运行中</option>
					<option value="10" <c:if test="${bean.jobState==10}">selected</c:if>>成功</option>
					<option value="-1" <c:if test="${bean.jobState==-1}">selected</c:if>>失败</option>
				</select>
			</div>
		</div>
	<input type="hidden" name="id" id="id"
		<c:if test="${!empty bean.id}">value="${bean.id}"</c:if> />
</form>
