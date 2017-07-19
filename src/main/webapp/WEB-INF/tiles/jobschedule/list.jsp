<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript">
	var oTable=null;
	$(document).ready(function() {
		oTable=$("#systemLogList").syDTbl({
			"aoColumns" : [ {
				"mDataProp":"jobName",
				"sWidth" : "10%"
			}, {
				"mDataProp":"jobClass",
				"sWidth" : "10%"
			},{
				"mDataProp":"cronExpression",
				"sWidth" : "10%"
			},{
				"mDataProp":"jobState",
				"sWidth" : "10%",
				"fnRender" : function(obj) {
					var status=obj.aData.jobState;
					if(status==1){
						return "运行中";
					}
					if(status==0){
						return "未启动";
					}
					if(status==10){
						return "成功";
					}
					if(status==-1){
						return "失败";
					}
					return status;
				}
			},{
				"mDataProp" : "id",
				"sWidth" : "8%",
				"fnRender" : function(obj) {
					return "<div class='btn-group'><a class='btn tbl_edit_btn' href='javascript:void(0);' data-id="+obj.aData.id+" data-url='jobschedule/add/view'>编辑</a><a class='btn tbl_del_btn' href='javascript:void(0);' data-id='"+obj.aData.id+"' data-url='jobschedule/delete?id="+obj.aData.id+"'>删除</a></div>";
				}
			}
			],
			url:"jobschedule/list",
			filterFormId:"searchForm"
		});
	});
</script>
    <h2>任务管理</h2>
            <div class="row-fluid tool-menu" id="searchForm">
				<div class="span12 left">
					   <a class="btn btn-primary" href="jobschedule/add/view?id=">添加任务</a>
				</div>
			</div>
						<table id="systemLogList">
							<thead>
								<tr>
									<th>任务名</th>
									<th>任务类</th>
									<th>cronExpression</th>
									<th>任务状态</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
