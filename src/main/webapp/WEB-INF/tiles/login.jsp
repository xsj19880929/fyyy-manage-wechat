<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<style type="text/css">
      body {
        padding-top: 40px;
        padding-bottom: 40px;
        background-color: #f5f5f5;
      }

      .form-signin {
        max-width: 300px;
        padding: 19px 29px 29px;
        margin: 0 auto 20px;
        background-color: #fff;
        border: 1px solid #e5e5e5;
        -webkit-border-radius: 5px;
           -moz-border-radius: 5px;
                border-radius: 5px;
        -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
           -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
                box-shadow: 0 1px 2px rgba(0,0,0,.05);
      }
      .form-signin .form-signin-heading,
      .form-signin .checkbox {
        margin-bottom: 10px;
      }
      .form-signin input[type="text"],
      .form-signin input[type="password"] {
        font-size: 16px;
        height: auto;
        margin-bottom: 15px;
        padding: 7px 9px;
      }

    </style>
<div class="container">
      <form class="form-signin need_validate" action="passport/login" method="post">
        <input type="hidden" name="preRequestUrl" value="${preRequestUrl}">
        <h4 class="form-signin-heading"><i class="icon-user"></i> 管理系统</h4>
        <c:if test="${!empty error}">
			<div class="alert alert-error">${error}</div>
		</c:if>
        <input type="text" name="name" class="input-block-level" data-rule-required="true" data-msg-required="请输入用户名" placeholder="用户名">
        <input type="password" name="password" class="input-block-level" placeholder="密码">
        <button class="btn btn-large btn-primary" type="submit">登录</button>
      </form>
    </div>
