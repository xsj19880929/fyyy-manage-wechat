<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<h2>添加/编辑预约</h2>
<div class="row-fluid">
    <div class="span12">
        <button class="btn btn-primary form_submit_btn" data-target="#userForm">保存</button>
        <button class="btn form_reset_btn" data-target="#userForm">重置</button>
    </div>
</div>
<form class="form-horizontal need_validate" id="userForm"
      action="booking/add" method="post">
    <div class="control-group">
			<span class="span4">
			<label class="control-label">科室编号<b class="must">*</b>：</label>
			<div class="controls">
                <select name="triageNo" id="triageNo" data-rule-required="true" data-msg-required="请选择科室。">
                    <option value="">请选择科室</option>
                    <c:forEach var="department" items="${departments}">
                        <option value="${department.triageNo}"
                                <c:if test="${bean.triageNo==department.triageNo}">selected</c:if>>${department.deptName}</option>
                    </c:forEach>
                </select>
            </div>
			</span>
        <input type="hidden" name="deptName" id="deptName"
               <c:if test="${!empty bean.deptName}">value="${bean.deptName}"</c:if> />

			<span class="span4">
			<label class="control-label">医生编号<b class="must">*</b>：</label>
			<div class="controls">
                <select name="doctorId" id="doctorId" data-rule-required="true" data-msg-required="请选择医生。">
                    <option value="">请选择医生</option>
                    <c:forEach var="doctor" items="${doctors}">
                        <option value="${doctor.doctorId}"
                                <c:if test="${bean.doctorId==doctor.doctorId}">selected</c:if>>${doctor.doctor}</option>
                    </c:forEach>
                </select>
            </div>
			</span>
        <input type="hidden" name="doctor" id="doctor"
               <c:if test="${!empty bean.doctor}">value="${bean.doctor}"</c:if> />
    </div>
    <div class="control-group">
			<span class="span4">
			<label class="control-label">预约日期<b class="must">*</b>：</label>
			<div class="controls">
                <input type="text" name="selectDate" id="selectDate" class="date_day" data-rule-required="true"
                       data-msg-required="不能为空"
                       <c:if test="${!empty bean.selectDate}">value="<fmt:formatDate value="${bean.selectDate}" pattern="yyyy-MM-dd"/>"
                </c:if>
                />
            </div>
			</span>
			<span class="span4">
			<label class="control-label">预约时间<b class="must">*</b>：</label>
			<div class="controls">
                <input type="text" name="selectTime" id="selectTime"
                       data-rule-required="true" data-msg-required="预约时间不能为空"
                       <c:if test="${!empty bean.selectTime}">value="${bean.selectTime}"</c:if> />
            </div>
			</span>
    </div>
    <div class="control-group">
			<span class="span4">
			<label class="control-label">卡号<b class="must">*</b>：</label>
			<div class="controls">
                <input type="text" name="cardNo" id="cardNo"
                       data-rule-required="true" data-msg-required="卡号不能为空"
                       <c:if test="${!empty bean.cardNo}">value="${bean.cardNo}"</c:if> />
            </div>
			</span>
			<span class="span4">
			<label class="control-label">身份证<b class="must">*</b>：</label>
			<div class="controls">
                <input type="text" name="icardid" id="icardid"
                       data-rule-required="true" data-msg-required="身份证不能为空"
                       <c:if test="${!empty bean.icardid}">value="${bean.icardid}"</c:if> />
            </div>
			</span>
    </div>
    <div class="control-group">
			
			<span class="span4">
			<label class="control-label">姓名<b class="must">*</b>：</label>
			<div class="controls">
                <input type="text" name="username" id="username"
                       data-rule-required="true" data-msg-required="姓名不能为空"
                       <c:if test="${!empty bean.username}">value="${bean.username}"</c:if> />
            </div>
			</span>
			<span class="span4">
			<label class="control-label">电话<b class="must">*</b>：</label>
			<div class="controls">
                <input type="text" name="phone" id="phone"
                       data-rule-required="true" data-msg-required="电话不能为空"
                       <c:if test="${!empty bean.phone}">value="${bean.phone}"</c:if> />
            </div>
			</span>
    </div>
    <div class="control-group">
			
			<span class="span4">
			<label class="control-label">时段<b class="must">*</b>：</label>
			<div class="controls">
                <select name="timeDesc" id="timeDesc" data-rule-required="true" data-msg-required="请选择时段">
                    <option value="上午" <c:if test="${bean.timeDesc=='上午'}">selected</c:if>>上午</option>
                    <option value="下午" <c:if test="${bean.timeDesc=='下午'}">selected</c:if>>下午</option>
                    <option value="全天" <c:if test="${bean.timeDesc=='全天'}">selected</c:if>>全天</option>
                </select>
            </div>
			</span>
			<span class="span4">
			<label class="control-label">付款<b class="must">*</b>：</label>
			<div class="controls">
                <select name="pay" id="pay" data-rule-required="true" data-msg-required="请选择">
                    <option value="0" <c:if test="${bean.pay=='0'}">selected</c:if>>没付款</option>
                    <option value="1" <c:if test="${bean.pay=='1'}">selected</c:if>>付款</option>
                </select>
            </div>
			</span>
    </div>
    <div class="control-group">
			
			<span class="span4">
			<label class="control-label">匹配时间<b class="must">*</b>：</label>
			<div class="controls">
                <select name="autoTime" id="autoTime" data-rule-required="true" data-msg-required="请选择">
                    <option value="0" <c:if test="${bean.autoTime=='0'}">selected</c:if>>自动</option>
                    <option value="1" <c:if test="${bean.autoTime=='1'}">selected</c:if>>手动</option>
                </select>
            </div>
			</span>
			<span class="span4">
			<label class="control-label">医院<b class="must">*</b>：</label>
			<div class="controls">
                <select name="orgId" id="orgId" data-rule-required="true" data-msg-required="请选择医院。">
                    <option value="">请选择医院</option>
                    <c:forEach var="hospital" items="${hospitals}">
                        <option value="${hospital.orgId}"
                                <c:if test="${bean.orgId==hospital.orgId}">selected</c:if>>${hospital.name}</option>
                    </c:forEach>
                </select>
            </div>
			</span>
    </div>
    <div class="control-group">
			<span class="span4">
			<label class="control-label">密码：</label>
			<div class="controls">
                <input type="text" name="password" id="password"
                       <c:if test="${!empty bean.password}">value="${bean.password}"</c:if> />
            </div>
			</span>

			<span class="span4">
			<label class="control-label">备选医生：</label>
			<div class="controls">
                <select name="backupDoctorId" id="backupDoctorId">
                    <option value="">请选择医生</option>
                    <c:forEach var="doctor" items="${doctors}">
                        <option value="${doctor.doctorId}"
                                <c:if test="${bean.backupDoctorId==doctor.doctorId}">selected</c:if>>${doctor.doctor}</option>
                    </c:forEach>
                </select>
            </div>
			</span>
        <input type="hidden" name="backupDoctor" id="backupDoctor"
               <c:if test="${!empty bean.backupDoctor}">value="${bean.backupDoctor}"</c:if> />
    </div>
    <div class="control-group">
			<span class="span4">
			<label class="control-label">备注：</label>
			<div class="controls">
                <input type="text" name="remark" id="remark"
                       <c:if test="${!empty bean.remark}">value="${bean.remark}"</c:if> />
            </div>
			</span>
        <span class="span4">
			<label class="control-label">openId：</label>
			<div class="controls">
                <input type="text" name="openId" id="openId"
                       <c:if test="${!empty bean.openId}">value="${bean.openId}"</c:if> />
            </div>
			</span>

    </div>
    <input type="hidden" name="id" id="id"
           <c:if test="${!empty bean.id}">value="${bean.id}"</c:if> />
    <input type="hidden" name="numberId" id="numberId"
           <c:if test="${!empty bean.numberId}">value="${bean.numberId}"</c:if> />
</form>
<script>
    $("#triageNo").change(function () {
        $("#deptName").val($("#triageNo").find("option:selected").text());
    });
    $("#doctorId").change(function () {
        $("#doctor").val($("#doctorId").find("option:selected").text());
    });
    $("#backupDoctorId").change(function () {
        $("#backupDoctor").val($("#backupDoctorId").find("option:selected").text());
    });
</script>

