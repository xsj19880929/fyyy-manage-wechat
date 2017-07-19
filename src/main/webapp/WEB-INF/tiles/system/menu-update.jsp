<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    <h2>添加/编辑 菜单</h2>
			<div class="row-fluid">
				<div class="span12">
					   <button class="btn btn-primary form_submit_btn" data-target="#userForm">保存</button>
		<button class="btn form_reset_btn" data-target="#userForm">重置</button>
				</div>
			</div>
				<form class="form-horizontal need_validate" id="userForm" action="system/menu/add" method="post">
					<div class="control-group">
						<label class="control-label" >上级菜单：</label>
						<div class="controls">
							<select name="parent">
								<option value="0">顶级菜单</option>
								<c:forEach var="parentmenu" items="${parentMenus}" >
									<option value="${parentmenu.id}" <c:if test="${menu.parent==parentmenu.id}">selected</c:if>>${parentmenu.name}</option>
								</c:forEach>
							</select>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" >菜单名称<b class="must">*</b>：</label>
						<div class="controls">
							<input type="text" name="name" id="name" data-rule-required="true" data-msg-required="菜单名称不能为空"
								<c:if test="${!empty menu.name}">value="${menu.name}"</c:if>
							/>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" >url：</label>
						<div class="controls">
							<input type="text" name="url" id="url" 
								<c:if test="${!empty menu.url}">value="${menu.url}"</c:if>
							/>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" >排序<b class="must">*</b>：</label>
						<div class="controls">
							<input type="text" name="sort" id="sort"  data-rule-required="true" data-msg-required="排序不能为空"  data-rule-number="true" data-msg-number="排序必须为数字" 
								<c:if test="${!empty menu.sort}">value="${menu.sort}"</c:if>
							class="required number"/>
                           <p class="text-warning">数值越大排在越前面</p>
						</div>
					</div>
					
					<input type="hidden" name="id" id="id" 
						<c:if test="${!empty menu.id}">value="${menu.id}"</c:if>
					/>
				</form>
