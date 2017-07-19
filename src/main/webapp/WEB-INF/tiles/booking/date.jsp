<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<style>
    .dateSpan {
        overflow: hidden;
        padding: 15px;
        text-align: center;
    }

    .datetime {
        color: #000;
        float: left;
        font-size: 14px;
        height: 26px;
        line-height: 26px;
        margin: 4px 0;
        width: 104px;
    }
</style>
<h3>${data.doctor.name}的门诊</h3>
<c:if test="${fn:length(data.doctor.date) == 9}">
    <div class="dateInfo">

        <fmt:parseDate value="${data.doctor.date.date}" var="newDate" pattern="yyyy/MM/dd HH:mm:ss"/>
        <fmt:formatDate value="${newDate}" pattern="yyyy-MM-dd"/> ${data.doctor.date.time=="AM"?"上午":"下午"}

    </div>

    <div class="dateSpan">
        <c:forEach var="section" items="${data.doctor.date.section}">
            <span class="datetime" style="${section.used==0&&section.max==1?"#00FF00":"color: #999;"}">
                <fmt:parseDate value="${section.start_time}" var="newStartTime" pattern="yyyy/MM/dd HH:mm:ss"/>
                <fmt:formatDate value="${newStartTime}" pattern="HH:mm"/>
            </span>
        </c:forEach>
    </div>
</c:if>
<c:if test="${fn:length(data.doctor.date) != 9}">
    <c:forEach var="date" items="${data.doctor.date}">
        <div class="dateInfo">

            <fmt:parseDate value="${date.date}" var="newDate" pattern="yyyy/MM/dd HH:mm:ss"/>
            <fmt:formatDate value="${newDate}" pattern="yyyy-MM-dd"/> ${date.time=="AM"?"上午":"下午"}

        </div>

        <div class="dateSpan">
            <c:forEach var="section" items="${date.section}">
            <span class="datetime" style="${section.used==0&&section.max==1?"#00FF00":"color: #999;"}">
                <fmt:parseDate value="${section.start_time}" var="newStartTime" pattern="yyyy/MM/dd HH:mm:ss"/>
                <fmt:formatDate value="${newStartTime}" pattern="HH:mm"/>
            </span>
            </c:forEach>
        </div>
    </c:forEach>
</c:if>
<c:if test="${!empty doctorDateTime}">
    <div class="dateInfo">
        <fmt:parseDate value="${doctorDateTime.selectDate}" var="newDate" pattern="yyyy-MM-dd HH:mm:ss"/>
        <fmt:formatDate value="${newDate}" pattern="yyyy-MM-dd"/> ${doctorDateTime.timeDesc=="AM"?"上午":"下午"}

    </div>

    <div class="dateSpan">
        <c:forEach var="doctorDateTime" items="${doctorDateTime.doctorDateTimeDetailList}">
            <span class="datetime" style="${doctorDateTime.ifUser==0?"#00FF00":"color: #999;"}">
                    ${doctorDateTime.selectTime}
            </span>
        </c:forEach>
    </div>
</c:if>
<div class="span6 left">
    <a class="btn btn-primary" href='javascript:void(0);'
       onclick='getDateTime(this)'>获取时间</a>
</div>

<script type="text/javascript">
    function getDateTime(obj) {
        $.ajax({
            async: true,
            type: "POST",
            url: "booking/getTime",
            contentType: "application/json",
            dataType: "json",
            data: JSON.stringify(${mapRequest}),
            beforeSend: function () {
                $(obj).text("获取中");
            },
            success: function (data) {

            },
            complete: function () {
            }
        });
    }
</script>


