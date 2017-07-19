<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript">
	var oTable=null;
	$(document).ready(function() {
		oTable=$("#systemLogList").syDTbl({
			"aoColumns" : [ {
				"mDataProp":"ip",
				"sWidth" : "10%"
			}, {
				"mDataProp":"port",
				"sWidth" : "10%"
			},{
				"mDataProp":"timeLong",
				"sWidth" : "10%"
			},{
				"mDataProp":"status",
				"sWidth" : "10%",
				"fnRender" : function(obj) {
					var status=obj.aData.status;
					if(status=="1"){
						return "<font color='red'>失效</font>";
					}else if(status=="0"){
						return "<font color='green'>可用</font>";
					}
				}
			},{
				"mDataProp":"useStatus",
				"sWidth" : "10%",
				"fnRender" : function(obj) {
					var status=obj.aData.useStatus;
					if(status=="1"){
						return "<font color='red'>正在使用</font>";
					}else if(status=="0"){
						return "<font color='green'>空闲</font>";
					}
				}
			},{
				"mDataProp" : "id",
				"sWidth" : "8%",
				"fnRender" : function(obj) {
					return "<div class='btn-group'><button class='btn btn-primary' onclick='check(this,\""+obj.aData.id+"\")'>检查</button><a class='btn tbl_edit_btn' href='javascript:void(0);' data-id="+obj.aData.id+" data-url='agentip/add/view'>编辑</a><a class='btn tbl_del_btn' href='javascript:void(0);' data-id='"+obj.aData.id+"' data-url='agentip/delete?id="+obj.aData.id+"'>删除</a></div>";
				}
			}
			],
			url:"agentip/list",
			filterFormId:"searchForm"
		});
	});
	function batchCheck(obj){
	if(confirm("确定要批量检查？")){
		$(obj).addClass("disabled");
		$(obj).text("检查中...");
		$.syRequest({
			async:false,
			type : "POST",
			url : "agentip/batchcheck",
			data : {},
			beforeSend : function() {
			},
			success : function(data) {
				if(data){
					$(obj).removeClass("disabled");
					$(obj).text("批量检查");
					oTable.fnRepage();
				}
			},
			complete : function() {
			}
		});
	}
	}
	function check(obj,id){
		$(obj).addClass("disabled");
		$(obj).text("检查中...");
		$.syRequest({
			async:false,
			type : "GET",
			url : "agentip/check?id="+id,
			data : {},
			beforeSend : function() {
			},
			success : function(data) {
				if(data){
					$(obj).removeClass("disabled");
					$(obj).text("检查");
					oTable.fnRepage();
				}
			},
			complete : function() {
			}
		});
		}
</script>
    <h2>代理IP管理</h2>
            <div class="row-fluid tool-menu" id="searchForm">
				<div class="span12 left">
					   <a class="btn btn-primary" href="agentip/add/view?id=">添加IP</a>
					   <button class="btn btn-primary" href="javascript:void(0);" onclick="batchCheck(this)">批量检查</button>
				</div>
			</div>
						<table id="systemLogList">
							<thead>
								<tr>
									<th>IP</th>
									<th>端口号</th>
									<th>请求时间</th>
									<th>状态</th>
									<th>使用状态</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
