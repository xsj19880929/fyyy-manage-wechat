<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript">
	var oTable=null;
	$(document).ready(function() {
		oTable=$("#systemLogList").syDTbl({
			"aoColumns" : [{
				"mDataProp" : "status",
				"sWidth" : "5px",
				"bUseRendered": false,
				"fnRender" : function(obj){
					return "<input type='checkbox' name='checkbox'>";
				}
			},{
				"mDataProp":"systemUsername",
				"sWidth" : "15%"
			},{
				"mDataProp":"operation",
				"sWidth" : "25%"
			},{
				"mDataProp":"type",
				"sWidth" : "15%"
			}, {
				"mDataProp":"createdTime",
				"sWidth" : "18%"
			}, {
				"mDataProp" : "systemUserId",
				"sWidth" : "10%",
				"fnRender" : function(obj) {
					return "<div class='btn-group'><a class='btn tbl_del_btn' data-id='"+obj.aData.id+"' data-url='systemlog/delete?id="+obj.aData.id+"'>删除</a></div>";
				}
			} ],
			url:"systemlog/list",
			filterFormId:"searchForm"
		});
		
	});
	
</script>
			<div id="menu" class="row-fluid menu">
			<div class="buttons">
					   <button type="button" class="btn tbl_batchdel_btn" data-url="systemlog/batchdelete">批量删除</button>
				    </div>
				<form class="form-search" id="searchForm">
					<input type="text" name="queryParam" id="queryParam" class="input-medium search-query"
						placeholder="请输入系统用户名"> 
						<span class="block">
							<div class="btn-group">
								<input type="button" class="btn tbl_filter_btn btn-primary" value="搜索">
							</div>
						</span>
				</form>
			</div>
			<div class="row-fluid">
				<div class="widget">
					<div class="inner">
						<table id="systemLogList">
							<thead>
								<tr>
									<th width="5px"><input type="checkbox" name='checkAll' class="tbl_chk_all"></th>
									<th>系统用户名</th>
									<th>操作内容</th>
									<th>操作情况</th>
									<th>创建时间</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>
			</div>
