<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript">
	var oTable=null;
	$(document).ready(function() {
		oTable=$("#systemLogList").syDTbl({
			"aoColumns" : [ {
				"mDataProp":"doctorId",
				"sWidth" : "10%"
			}, {
				"mDataProp":"doctorSn",
				"sWidth" : "10%"
			},{
				"mDataProp":"doctor",
				"sWidth" : "10%"
			},{
				"mDataProp":"deptId",
				"sWidth" : "10%"
			},{
				"mDataProp":"createdTime",
				"sWidth" : "10%"
			},{
				"mDataProp" : "id",
				"sWidth" : "8%",
				"fnRender" : function(obj) {
					return "<div class='btn-group'><a class='btn tbl_edit_btn' href='javascript:void(0);' data-id="+obj.aData.id+" data-url='doctor/add/view'>编辑</a><a class='btn tbl_del_btn' href='javascript:void(0);' data-id='"+obj.aData.id+"' data-url='doctor/delete?id="+obj.aData.id+"'>删除</a></div>";
				}
			}
			],
			url:"doctor/list",
			filterFormId:"searchForm"
		});
	});
</script>
    <h2>医生管理</h2>
            <div class="row-fluid tool-menu" id="searchForm">
				<div class="span12 left">
					   <a class="btn btn-primary" href="doctor/add/view?id=">添加医生</a>
				</div>
			</div>
						<table id="systemLogList">
							<thead>
								<tr>
									<th>医生编号</th>
									<th>医生加密编号</th>
									<th>医生名</th>
									<th>科室</th>
									<th>创建时间</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
