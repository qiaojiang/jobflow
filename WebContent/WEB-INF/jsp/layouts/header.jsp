<%@page import="org.springframework.web.context.annotation.RequestScope"%>
<%@page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>JOBFLOW</title>
<link rel="icon" type="image/png" href="${requestScope.baseUrl}/static/css/images/icon.png" sizes="32x32">
<link href="${requestScope.baseUrl}/static/css/bootstrap.min.css" rel="stylesheet">
<link href="${requestScope.baseUrl}/static/css/datepicker3.css" rel="stylesheet">
<link href="${requestScope.baseUrl}/static/css/bootstrap-table.css" rel="stylesheet">
<link href="${requestScope.baseUrl}/static/css/styles.css" rel="stylesheet">
<link href="${requestScope.baseUrl}/static/css/colorbox.css" rel="stylesheet">
<!--[if lt IE 9]>
<script src="${requestScope.baseUrl}/static/js/html5shiv.js"></script>
<script src="${requestScope.baseUrl}/static/js/respond.min.js"></script>
<![endif]-->
<script type="text/javascript">
var BASE_URL = "${requestScope.baseUrl}";
</script>
<script src="${requestScope.baseUrl}/static/js/jquery-1.11.1.min.js"></script>
<script src="${requestScope.baseUrl}/static/js/bootstrap.min.js"></script>
<script src="${requestScope.baseUrl}/static/js/bootstrap-table.js"></script>
<script src="${requestScope.baseUrl}/static/js/jquery.colorbox-min.js"></script>
<script src="${requestScope.baseUrl}/static/js/admin.js"></script>
</head>

<body>
	<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#sidebar-collapse">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#"><span>JOB</span>FLOW</a>
				<ul class="user-menu">
					<li class="dropdown pull-right">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown"><span class="glyphicon glyphicon-user"></span>${sessionScope.userinfo.nickname} <span class="caret"></span></a>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#"><span class="glyphicon glyphicon-user"></span> Profile</a></li>
							<li><a href="#"><span class="glyphicon glyphicon-cog"></span> Settings</a></li>
							<li><a href="${requestScope.baseUrl}/passport/logout"><span class="glyphicon glyphicon-log-out"></span> Logout</a></li>
						</ul>
					</li>
				</ul>
			</div>
							
		</div><!-- /.container-fluid -->
	</nav>
	<div id="sidebar-collapse" class="col-sm-3 col-lg-2 sidebar">
		<form role="search">
			<div class="form-group">
				<input type="text" class="form-control" placeholder="Search">
			</div>
		</form>
		<ul class="nav menu">
		    <li <c:if test="${requestScope.type == 'index'}"> class="active" </c:if>><a href="${requestScope.baseUrl}/view/index"><span class="glyphicon glyphicon-list"></span> 总览</a></li>
		    <li <c:if test="${requestScope.type == 'job'}"> class="active" </c:if>><a href="${requestScope.baseUrl}/view/job"><span class="glyphicon glyphicon-list-alt"></span> 作业</a></li>
		    <li <c:if test="${requestScope.type == 'jobflow'}"> class="active" </c:if>><a href="${requestScope.baseUrl}/view/jobflow"><span class="glyphicon glyphicon-th"></span> 作业流</a></li>
			<li <c:if test="${requestScope.type == 'schedule'}"> class="active" </c:if>><a href="${requestScope.baseUrl}/view/schedule"><span class="glyphicon glyphicon-dashboard"></span> 调度</a></li>
			<li <c:if test="${requestScope.type == 'execution'}"> class="active" </c:if>><a href="${requestScope.baseUrl}/view/execution"><span class="glyphicon glyphicon-list"></span> 执行</a></li>
			<li <c:if test="${requestScope.type == 'host'}"> class="active" </c:if>><a href="${requestScope.baseUrl}/view/host"><span class="glyphicon glyphicon-cloud"></span> 主机</a></li>
			<c:if test="${sessionScope.type == 1}">
			<li <c:if test="${requestScope.type == 'user'}"> class="active" </c:if>><a href="${requestScope.baseUrl}/view/user"><span class="glyphicon glyphicon-user"></span> 用户</a></li>
			</c:if>
			<!-- li class="parent">
				<a href="index.php?r=tag/index">
					<span class="glyphicon glyphicon-info-sign"></span> 标签 <span data-toggle="collapse" href="#sub-item-1" class="icon pull-right"><em class="glyphicon glyphicon-s glyphicon-plus"></em></span> 
				</a>
				<ul class="children <?php if($c != 'tag') echo 'collapse'; ?>" id="sub-item-1">
					<li>
						<a class="" href="index.php?r=tag/index&type=1">
							<span class="glyphicon glyphicon-share-alt"></span> 普通标签
						</a>
					</li>
					<li>
						<a class="" href="index.php?r=tag/index&type=2">
							<span class="glyphicon glyphicon-share-alt"></span> 运营标签
						</a>
					</li>
				</ul>
			</li>
			<li><a href="index.php?r=news/index"><span class="glyphicon glyphicon-list-alt"></span> 新闻</a></li -->
			<li role="presentation" class="divider"></li>
			
		</ul>
		<div><br/></div>
	</div><!--/.sidebar-->
	
		