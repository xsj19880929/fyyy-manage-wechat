<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript">
    var oTable = null;
	$(document).ready(function() {
		oTable=$("#userTable").syDTbl({
			"aoColumns" : [ 
			{
				"mDataProp" : "name",
				"sWidth" : "20%"
			},{
				"mDataProp" : "url",
				"sWidth" : "20%"
			},{
				"mDataProp" : "parent",
				"sWidth" : "20%"
			},{
				"mDataProp" : "id",
				"sWidth" : "20%",
				"fnRender" : function(obj){  
					return "<div class='btn-group'><a class='btn tbl_edit_btn' href='javascript:void(0);' data-id="+obj.aData.id+" data-url='system/menu/add/view'>编辑</a><a class='btn tbl_del_btn' href='javascript:void(0);' data-id="+obj.aData.id+" data-url='system/menu/delete?id="+obj.aData.id+"'>删除</a></div>";
				}
			}
			],
			url:"system/menu/list",
			filterFormId:"searchForm"
		});
	});
	
</script>
    <h2>菜单管理</h2>
			<div class="row-fluid tool-menu">
				<div class="span12">
					   <a class="btn btn-primary" href="system/menu/add/view">添加菜单</a>
				</div>
			</div>
						<table id="userTable">
							<thead>
								<tr>
									<th>名称</th>
									<th>url</th>
									<th>上级菜单</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
