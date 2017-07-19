<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" type="text/css" href="css/component.css" />
<script src="js/modernizr.custom.js"></script>
<script src="js/jquery.dlmenu.js"></script>
<div class="navbar navbar-fixed-top">
  <div class="navbar-inner">
    <div class="container-fluid">
      <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </a>
      <div class="logo">管理系统</div>
      <div class="top-menu visible-desktop">
            <ul class="pull-left">
              <li><a href="account/setting"><i class="icon-cog"></i> 账户设置</a></li>
            </ul>
            <ul class="pull-right">  
              <li><a href="passport/logout"><i class="icon-off"></i> 退出</a></li>
            </ul>
          </div>

          <div class="top-menu visible-phone visible-tablet">
            <ul class="pull-right">  
              <li><a href="account/setting"><i class="icon-cog"></i></a></li>
              <li><a href="passport/logout"><i class="icon-off"></i></a></li>
            </ul>
          </div>
    </div>
  </div>
</div>

<div class="container-fluid">
  <div class="sidebar-nav nav-collapse collapse" style="height: 0px;">
      <div class="user_side clearfix"><i class="icon-user"></i> ${systemUser.name}</div>
      <div id="dl-menu" class="menu_side dl-menuwrapper">
      <ul class="dl-menu" id="sideMenu" >
      </ul>
      </div>
  </div>
<div class="main_container">
    <tiles:insertAttribute name="body" />
</div>
</div>
<script type="text/javascript">
	$(function() {
		var menus = ${systemMenus};
		for ( var i = 0; i < menus.length; i++) {
			var p;
			if (menus[i].url) {
				url = "href='" + menus[i].url
						+ (menus[i].url.indexOf("?") == -1 ? "?" : "&")
						+ "menuId=" + menus[i].id + "'";
			} else {
				url = "href='#'";
			}
			var menustr = "<li id='"+menus[i].id+"'><a "+url+">"
					+ menus[i].name + "</a></li>";
			if (menus[i].level == 0) {
				p = $("#sideMenu");
			} else {
				p = $("#" + menus[i].parent + ">ul");
				if (p.length == 0) {
					p = $("<ul class='dl-submenu'></ul>").appendTo(
							$("#" + menus[i].parent));
					p.append('<li class="dl-back"><a href="#">返回</a></li>');
				}
			}
			p.append(menustr);
		}
		$( '#dl-menu' ).dlmenu();
		var selMenu=$("#${menuId}");
		if(selMenu.length){
			selMenu.parents("ul.dl-submenu").siblings("a").click();
		}
	});
</script>