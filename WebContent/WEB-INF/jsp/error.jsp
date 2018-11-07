<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="layouts/header.jsp" %>

	<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">
		<div class="row">
			<ol class="breadcrumb">
				<li><a href="#"><span class="glyphicon glyphicon-home"></span></a></li>
				<li class="active">error</li>
			</ol>
		</div><!--/.row-->
		<div class="row" style="margin: 100px 0 0 120px;">
			<div class="col-xs-12 col-md-6 col-lg-3">
				<div class="panel panel-orange panel-widget">
					<div class="row no-padding" style="width: 800px;">
						<div class="col-sm-3 col-lg-5 widget-left" style="width: 100px;">
							<em class="glyphicon glyphicon-warning-sign glyphicon-l"></em>
						</div>
						<div class="col-sm-9 col-lg-7 widget-right">
							<div class="large">${requestScope.message}</div>
						</div>
					</div>
				</div>
			</div>
		</div><!--/.row-->
	</div>
<%@include file="layouts/footer.jsp" %>
