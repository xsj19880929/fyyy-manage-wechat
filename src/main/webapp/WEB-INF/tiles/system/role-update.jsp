<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
    <h2>添加/编辑角色</h2>
			<div class="row-fluid">
				<div class="span12">
					   <button class="btn btn-primary form_submit_btn" data-target="#userForm">保存</button>
		<button class="btn form_reset_btn" data-target="#userForm">重置</button>
				</div>
			</div>
				<form class="form-horizontal need_validate" id="userForm" action="system/role/add" method="post">
					<div class="control-group">
						<label class="control-label" >名称<b class="must">*</b>：</label>
						<div class="controls">
							<input type="text" name="name" id="name"  data-rule-required="true" data-msg-required="菜单名称不能为空"
								<c:if test="${!empty role}">value="${role.name}"</c:if>
							/>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" >菜单<b class="must">*</b>：</label>
						<div class="controls">
						    <label for="menus" class="error"></label>
							<input type="checkbox" name='checkAll' class="tbl_chk_all" id='checkAll' onClick='selectAll()'>全部<br/>
							<c:forEach var="parentmenu" items="${parentMenus}" >
							 <label style="padding-left:${parentmenu.level*20}px">
								<input data-rule-required="true" data-msg-required="菜单项不能为空" type="checkbox" value="${parentmenu.id}" data-parent="${parentmenu.parent}" data-id="${parentmenu.id}" name="menus" <c:forEach var="puMenu" items="${roleMenus}" >
									<c:if test="${puMenu.menuId==parentmenu.id}">checked</c:if>
								</c:forEach> onchange="changeMenus(this);"/>&nbsp;${parentmenu.name}
							 </label>
							</c:forEach>
						</div>
					</div>
					<input type="hidden" name="id" id="id" 
						<c:if test="${!empty role}">value="${role.id}"</c:if>
					/>					
				</form>
<script type="text/javascript">
function changeMenus(obj){
	var parent=$(obj).attr("data-parent");
	var id=$(obj).attr("data-id");
	if($(obj).is(':checked')==true){
		var fater=$("input[data-id="+parent+"]");
		fater.attr("checked",true);
		var grandFater=fater.attr("data-parent");
		$("input[data-id="+grandFater+"]").attr("checked",true);
	}else{
		 $("input[data-id="+parent+"]").attr("checked",false);
		 var grandFater=$("input[data-id="+$("input[data-id="+parent+"]").attr("data-parent")+"]");
		 grandFater.attr("checked",false);
		 $("input[data-parent="+parent+"]").each(function(){
			if($(this).is(':checked')){
				$("input[data-id="+parent+"]").attr("checked",true);
			}
		 });
		 $("input[data-parent="+grandFater.attr("data-id")+"]").each(function(){
			if($(this).is(':checked')){
				grandFater.attr("checked",true);
			}
		 });
		 $("input[data-parent="+id+"]").each(function(){
			 $(this).attr("checked",false);
			 $("input[data-parent="+$(this).val()+"]").each(function(){
				 $(this).attr("checked",false);
			 });
		 });
	}
}
	function selectAll(){
	    if($("#checkAll").attr("checked")=="checked"){
	   		$("input[type='checkbox']").each(function (e){
	   		      if($(this).attr('disabled') != 'disabled'){
	   		          $(this).attr("checked", true);
	   		      }
	   		});
	     }else{
	   		$("input[type='checkbox']").attr("checked", false);
	   		}
	  }
</script>
