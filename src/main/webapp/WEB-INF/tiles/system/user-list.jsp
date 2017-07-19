<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript">
	var oTable=null;
	$(document).ready(function() {
		oTable=$("#systemLogList").syDTbl({
			"aoColumns" : [ {
				"mDataProp":"name",
				"sWidth" : "10%"
			}, {
				"mDataProp":"phone",
				"sWidth" : "10%"
			},{
				"mDataProp":"status",
				"sWidth" : "10%",
				"fnRender" : function(obj){
					if(obj.aData.status=="0"){
						return "不可用";
					}
					return "可用";
				}
			},
			{
					"mDataProp":"updateTime",
					"sWidth" : "10%"
				},
				{
						"mDataProp":"createTime",
						"sWidth" : "10%"
					},
			{
				"mDataProp" : "id",
				"sWidth" : "8%",
				"fnRender" : function(obj) {
					return "<div class='btn-group'><a class='btn tbl_edit_btn' href='javascript:void(0);' data-id="+obj.aData.id+" data-url='system/user/add/view'>编辑</a><a class='btn tbl_del_btn' href='javascript:void(0);' data-id='"+obj.aData.id+"' data-url='system/user/delete?id="+obj.aData.id+"'>删除</a></div>";
				}
			}
			],
			url:"system/user/list",
			filterFormId:"searchForm"
		});
	});
</script>
    <h2>用户管理</h2>
            <div class="row-fluid tool-menu" id="searchForm">
				<div class="span6 left">
					   <a class="btn btn-primary" href="system/user/add/view?id=">添加用户</a>
				</div>
				<div class="span6 right">
					<input type="text" name="name" class="input-medium search-query" placeholder="请输入名称"> 
					<input type="button" class="btn tbl_filter_btn btn-primary" value="搜索">
				</div>
			</div>
						<table id="systemLogList">
							<thead>
								<tr>
									<th>用户名</th>
									<th>手机号码</th>
									<th>状态</th>
									<th>更新时间</th>
									<th>创建时间</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
