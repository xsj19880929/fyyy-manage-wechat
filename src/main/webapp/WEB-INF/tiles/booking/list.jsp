<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script type="text/javascript">
    var oTable = null;
    $(document).ready(function () {
        oTable = $("#systemLogList").syDTbl({
            "aoColumns": [{
                "mDataProp": "deptName",
                "sWidth": "10%"
            }, {
                "mDataProp": "doctor",
                "sWidth": "10%",
                "fnRender": function (obj) {
                    return "<a href='" + obj.aData.taskUrl + "' target='_blank'>" + obj.aData.doctor + "</a>";
                }
            }, {
                "mDataProp": "timeDesc",
                "sWidth": "10%"
            }, {
                "mDataProp": "selectDate",
                "sWidth": "10%",
                "fnRender": function (obj) {
                    return formatter(obj.aData.selectDate);
                }
            }, {
                "mDataProp": "selectTime",
                "sWidth": "10%"
            }, {
                "mDataProp": "cardNo",
                "sWidth": "10%"
            }, {
                "mDataProp": "icardid",
                "sWidth": "10%"
            }, {
                "mDataProp": "username",
                "sWidth": "10%"
            }, {
                "mDataProp": "phone",
                "sWidth": "10%"
            }, {
                "mDataProp": "x",
                "sWidth": "10%",
                "fnRender": function (obj) {
                    var status = obj.aData.status;
                    if (status == "1") {
                        return "<font color='red'>失败</font>";
                    } else if (status == "2") {
                        return "<font color='green'>成功</font>";
                    } else if (status == "3") {
                        return "<font color='#FF7F00'>取消</font>";
                    } else {
                        return "等待";
                    }
                }
            }, {
                "mDataProp": "pay",
                "sWidth": "10%",
                "fnRender": function (obj) {
                    var pay = obj.aData.pay;
                    if (pay == "1") {
                        return "<font color='green'>付款</font>";
                    } else {
                        return "<font color='red'>没付款</font>";
                    }
                }
            }, {
                "mDataProp": "id",
                "sWidth": "8%",
                "fnRender": function (obj) {
                    var status = obj.aData.status;
                    return "<div class='btn-group'><a class='btn tbl_booking_btn' href='javascript:void(0);' data-id=" + obj.aData.id + " onclick='queryBooking(this)'>查询</a><a class='btn tbl_booking_btn' href='javascript:void(0);' data-id=" + obj.aData.id + " onclick='booking(this)'>预约</a><a class='btn tbl_booking_btn' href='javascript:void(0);' data-id=" + obj.aData.id + " onclick='batchBooking(this)'>快速预约</a><a class='btn tbl_booking_btn' href='javascript:void(0);' data-id=" + obj.aData.id + " data-username=" + obj.aData.username + " data-selectdate=" + obj.aData.selectDate + " onclick='cancelBooking(this)'>取消</a><a class='btn tbl_edit_btn' href='javascript:void(0);' data-id=" + obj.aData.id + " data-url='booking/add/view'>编辑</a><a class='btn tbl_del_btn' href='javascript:void(0);' data-id='" + obj.aData.id + "' data-url='booking/delete?id=" + obj.aData.id + "'>删除</a></div>";
                    if (status == "1") {
                        return "<div class='btn-group'><a class='btn tbl_booking_btn' href='javascript:void(0);' data-id=" + obj.aData.id + " onclick='booking(this)'>重新预约</a><a class='btn tbl_edit_btn' href='javascript:void(0);' data-id=" + obj.aData.id + " data-url='booking/add/view'>编辑</a><a class='btn tbl_del_btn' href='javascript:void(0);' data-id='" + obj.aData.id + "' data-url='booking/delete?id=" + obj.aData.id + "'>删除</a></div>";
                    } else if (status == "2") {
                        return "<div class='btn-group'><a class='btn tbl_booking_btn' href='javascript:void(0);' data-id=" + obj.aData.id + " onclick='queryBooking(this)'>查询</a><a class='btn tbl_booking_btn' href='javascript:void(0);' data-id=" + obj.aData.id + " data-username=" + obj.aData.username + " data-selectdate=" + obj.aData.selectDate + " onclick='cancelBooking(this)'>取消</a><a class='btn tbl_edit_btn' href='javascript:void(0);' data-id=" + obj.aData.id + " data-url='booking/add/view'>编辑</a><a class='btn tbl_del_btn' href='javascript:void(0);' data-id='" + obj.aData.id + "' data-url='booking/delete?id=" + obj.aData.id + "'>删除</a></div>";
                    } else if (status == "3") {
                        return "<div class='btn-group'><a class='btn tbl_booking_btn' href='javascript:void(0);' data-id=" + obj.aData.id + " onclick='queryBooking(this)'>查询</a><a class='btn tbl_booking_btn' href='javascript:void(0);' data-id=" + obj.aData.id + " onclick='booking(this)'>预约</a><a class='btn tbl_edit_btn' href='javascript:void(0);' data-id=" + obj.aData.id + " data-url='booking/add/view'>编辑</a><a class='btn tbl_del_btn' href='javascript:void(0);' data-id='" + obj.aData.id + "' data-url='booking/delete?id=" + obj.aData.id + "'>删除</a></div>";
                    } else {
                        return "<div class='btn-group'><a class='btn tbl_booking_btn' href='javascript:void(0);' data-id=" + obj.aData.id + " onclick='booking(this)'>预约</a><a class='btn tbl_edit_btn' href='javascript:void(0);' data-id=" + obj.aData.id + " data-url='booking/add/view'>编辑</a><a class='btn tbl_del_btn' href='javascript:void(0);' data-id='" + obj.aData.id + "' data-url='booking/delete?id=" + obj.aData.id + "'>删除</a></div>";
                    }

                }
            }
            ],
            url: "booking/list",
            filterFormId: "searchForm"
        });
    });
</script>
<h2>预约管理</h2>
<div class="row-fluid tool-menu" id="searchForm">
    <div class="span6 left">
        <a class="btn btn-primary" href="booking/add/view?id=">添加预约</a>
        <button class="btn btn-primary" href="javascript:void(0);" onclick="handBooking(this)">批量预约</button>
    </div>
    <div class="span6 right">
        <input type="text" name="username" class="input-medium search-query" placeholder="请输入姓名">
        <input type="text" name="beginTime" class="input-small search-query date_day" value="${bookingBeginTime}"/>-
        <input type="text" name="endTime" class="input-small search-query date_day" value="${bookingEndTime}"/>
        <input type="button" class="btn tbl_filter_btn btn-primary" value="搜索">
        <input type="button" class="btn btn-danger" value="清空" href='javascript:void(0);'
               onclick='removeSession(this)'>
    </div>
</div>
<table id="systemLogList">
    <thead>
    <tr>
        <th>科室名称</th>
        <th>医生名字</th>
        <th>时段</th>
        <th>预约日期</th>
        <th>预约时间</th>
        <th>卡号</th>
        <th>身份证</th>
        <th>姓名</th>
        <th>电话</th>
        <th>状态</th>
        <th>付款</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    </tbody>
</table>
<script type="text/javascript">
    function formatter(time) {
        if (time.substring(0, 10).indexOf("/") != -1) {
            var arr = time.substring(0, 10).split("/");
            return arr[2] + "-" + arr[0] + "-" + arr[1];
        }


    }
    function booking(obj) {
        $.syRequest({
            async: false,
            type: "POST",
            url: "booking/booking?id=" + $(obj).data("id"),
            data: {},
            beforeSend: function () {
                $(obj).text("预约中");
            },
            success: function (data) {
                var st = data.st;
                if (st == "ok") {
                    alert(data.msg);
                }
                $(obj).text("重新预约");
            },
            complete: function () {
            }
        });
    }
    function batchBooking(obj) {
        $.syRequest({
            async: false,
            type: "POST",
            url: "booking/batchBooking?id=" + $(obj).data("id"),
            data: {},
            beforeSend: function () {
                $(obj).text("预约中");
            },
            success: function (data) {
                var st = data.st;
                if (st == "ok") {
                    alert(data.msg);
                }
                $(obj).text("重新预约");
            },
            complete: function () {
            }
        });
    }
    function queryBooking(obj) {
        $.syRequest({
            async: false,
            type: "POST",
            url: "booking/queryBooking?id=" + $(obj).data("id"),
            data: {},
            beforeSend: function () {
            },
            success: function (data) {
                var st = data.st;
                if (st == "ok") {
                    alert(data.msg);
                }
            },
            complete: function () {
            }
        });
    }
    function handBooking(obj) {
        if (confirm("确定要批量预约？")) {
            $(obj).addClass("disabled");
            $(obj).text("预约中...");
            $.syRequest({
                async: false,
                type: "POST",
                url: "booking/handBooking",
                data: {},
                beforeSend: function () {
                },
                success: function (data) {
                    if (data) {
                        $(obj).removeClass("disabled");
                        $(obj).text("批量预约");
                    }
                },
                complete: function () {
                }
            });
        }
    }
    function cancelBooking(obj) {
        if (confirm("确定要取消【" + $(obj).data("username") + "(" + $(obj).data("selectdate") + ")】预约吗")) {
            $.syRequest({
                async: false,
                type: "POST",
                url: "booking/cancelBooking?id=" + $(obj).data("id"),
                data: {},
                beforeSend: function () {
                },
                success: function (data) {
                    var st = data.st;
                    if (st == "ok") {
                        alert(data.msg);
                    }
                },
                complete: function () {
                }
            });
        }
    }
    function removeSession(obj) {
        $("input[name = 'beginTime']").val('');
        $("input[name = 'endTime']").val('');
        $.syRequest({
            async: false,
            type: "POST",
            url: "booking/removeSession",
            data: {},
            beforeSend: function () {
            },
            success: function (data) {
            },
            complete: function () {
            }
        });
    }
</script>