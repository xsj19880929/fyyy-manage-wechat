<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript">
    var oTable = null;
	$(document).ready(function() {
		oTable=$("#userTable").syDTbl({
			"aoColumns" : [ 
			{
				"mDataProp" : "name",
				"sWidth" : "auto"
			},
			{
				"mDataProp" : "createdTime",
				"sWidth" : "auto"
			},{
				"mDataProp" : "id",
				"sWidth" : "auto",
				"fnRender" : function(obj){  
					return "<div class='btn-group'><a class='btn tbl_edit_btn' href='javascript:void(0);' data-id="+obj.aData.id+" data-url='system/role/add/view'>编辑</a><a class='btn tbl_del_btn' data-id='"+obj.aData.id+"' data-url='system/role/delete?id="+obj.aData.id+"'>删除</a></div>";
				}
			}
			],
			url:"system/role/list",
			filterFormId:"searchForm"
		});
	});
	
</script>
    <h2>角色管理</h2>
           <div class="row-fluid tool-menu">
				<div class="span12">
					   <a class="btn btn-primary" href="system/role/add/view">添加角色</a>
				</div>
			</div>
						<table id="userTable">
							<thead>
								<tr>
									<th width="70px">名称</th>
									<th width="100px">创建时间</th>
									<th width="50px">操作</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
