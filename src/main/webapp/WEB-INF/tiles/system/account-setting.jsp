<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    <h2>密码设置</h2>
			<div class="row-fluid">
				<div class="span12">
					   <button class="btn btn-primary form_submit_btn" data-target="#accountForm">保存</button>
		<button class="btn form_reset_btn" data-target="#accountForm">重置</button>
				</div>
			</div>
			
				<form class="form-horizontal need_validate" id="accountForm" action="account/changepassword" method="post">
					<c:if test="${!empty error}" >
                         <div class="alert alert-error">
                             ${error}
                         </div>
                     </c:if>
                     <c:if test="${!empty message}" >
                          <div class="alert alert-success">
                              ${message}
                          </div>
                     </c:if>
					<div class="control-group">
						<label class="control-label" >旧密码<b class="must">*</b>：</label>
						<div class="controls">
							<input type="password" name="oldPassword" id="oldPassword" data-rule-required="true" data-msg-required="不能为空" />
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" >新密码<b class="must">*</b>：</label>
						<div class="controls">
							<input type="password" name="newPassword" id="newPassword" data-rule-required="true" data-msg-required="不能为空" />
						</div>
					</div>		
					<div class="control-group">
						<label class="control-label" >确认密码<b class="must">*</b>：</label>
						<div class="controls">
							<input type="password" name="confirmPassword" id="confirmPassword" data-rule-equalTo="#newPassword" data-msg-equalTo="新密码不一致" data-rule-required="true" data-msg-required="不能为空" />
						</div>
					</div>
				</form>
