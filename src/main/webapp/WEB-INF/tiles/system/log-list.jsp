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
				"mDataProp":"operation",
				"sWidth" : "10%"
			},{
				"mDataProp":"createTime",
				"sWidth" : "10%"
			}
			],
			url:"system/log/list",
			filterFormId:"searchForm"
		});
	});
</script>
    <h2>系统用户操作日志</h2>
            <div class="row-fluid tool-menu" id="searchForm">
			</div>
						<table id="systemLogList">
							<thead>
								<tr>
									<th>用户名</th>
									<th>操作</th>
									<th>操作时间</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
