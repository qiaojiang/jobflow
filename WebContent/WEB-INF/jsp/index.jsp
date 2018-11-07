<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="layouts/header.jsp" %>
	<script src="../static/js/chart.min.js"></script>
	<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">
		<div class="row">
			<ol class="breadcrumb">
				<li><a href="#"><span class="glyphicon glyphicon-home"></span></a></li>
				<li class="active">Dashboard</li>
			</ol>
		</div><!--/.row-->
		

		<div class="row">
			<div class="panel-heading">Overview
				<div style="margin-right: 40px;float: right;font-size: 14px;">
					<a href="${requestScope.baseUrl}/view/index?date=today">今天</a> |
					<a href="${requestScope.baseUrl}/view/index?date=yesterday">昨天</a> |
					<a href="${requestScope.baseUrl}/view/index?date=recent1w">最近1周</a>
				</div>
			</div>
			<div class="col-xs-12 col-md-6 col-lg-3">
				<div class="panel panel-blue panel-widget ">
					<div class="row no-padding">
						<div class="col-sm-3 col-lg-5 widget-left">
							<em class="glyphicon glyphicon-ok glyphicon-l"></em>
						</div>
						<div class="col-sm-9 col-lg-7 widget-right">
							<div class="large">${requestScope.execStatusCount.get("1")}</div>
							<div class="text-muted">success</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-xs-12 col-md-6 col-lg-3">
				<div class="panel panel-orange panel-widget">
					<div class="row no-padding">
						<div class="col-sm-3 col-lg-5 widget-left">
							<em class="glyphicon glyphicon-time glyphicon-l"></em>
						</div>
						<div class="col-sm-9 col-lg-7 widget-right">
							<div class="large">${requestScope.execStatusCount.get("2")}</div>
							<div class="text-muted">waiting</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-xs-12 col-md-6 col-lg-3">
				<div class="panel panel-teal panel-widget">
					<div class="row no-padding">
						<div class="col-sm-3 col-lg-5 widget-left">
							<em class="glyphicon glyphicon-refresh glyphicon-l"></em>
						</div>
						<div class="col-sm-9 col-lg-7 widget-right">
							<div class="large">${requestScope.execStatusCount.get("3")}</div>
							<div class="text-muted">running</div>
						</div>
					</div>
				</div>
			</div>
			<div class="col-xs-12 col-md-6 col-lg-3">
				<div class="panel panel-red panel-widget">
					<div class="row no-padding">
						<div class="col-sm-3 col-lg-5 widget-left">
							<em class="glyphicon glyphicon-warning-sign glyphicon-l"></em>
						</div>
						<div class="col-sm-9 col-lg-7 widget-right">
							<div class="large">${requestScope.execStatusCount.get("0")}</div>
							<div class="text-muted">failed</div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="col-lg-12">
				<div class="panel panel-default">
					
					<div class="panel-body">
						
						<div class="canvas-wrapper">
							<canvas class="main-chart" id="line-chart" height="200" width="600"></canvas>
						</div>
					</div>
				</div>
			</div>
		</div><!--/.row-->
		
		<div class="row">
			
		</div><!--/.row-->
		
		<div class="row">
			<div class="col-xs-6 col-md-3">
				<div class="panel panel-default">
					<div class="panel-body easypiechart-panel">
						<h4>作业</h4>
						<div class="easypiechart" id="easypiechart-blue" data-percent="92" ><span class="percent">${requestScope.jobCount}</span>
						</div>
					</div>
				</div>
			</div>
			<div class="col-xs-6 col-md-3">
				<div class="panel panel-default">
					<div class="panel-body easypiechart-panel">
						<h4>作业流</h4>
						<div class="easypiechart" id="easypiechart-orange" data-percent="65" ><span class="percent">${requestScope.jobFlowCount}</span>
						</div>
					</div>
				</div>
			</div>
			<div class="col-xs-6 col-md-3">
				<div class="panel panel-default">
					<div class="panel-body easypiechart-panel">
						<h4>调度</h4>
						<div class="easypiechart" id="easypiechart-teal" data-percent="56" ><span class="percent">${requestScope.scheduleCount}</span>
						</div>
					</div>
				</div>
			</div>
			<div class="col-xs-6 col-md-3">
				<div class="panel panel-default">
					<div class="panel-body easypiechart-panel">
						<h4>执行</h4>
						<div class="easypiechart" id="easypiechart-red" data-percent="27" ><span class="percent">${requestScope.execCount}</span>
						</div>
					</div>
				</div>
			</div>
		</div><!--/.row-->
	</div>
<script type="text/javascript">
var randomScalingFactor = function(){ return Math.round(Math.random()*1000)};

var lineChartData = {
	labels : ${requestScope.hourExecKeys},
	datasets : [
		{
			label: "My Second dataset",
			fillColor : "rgba(48, 164, 255, 0.2)",
			strokeColor : "rgba(48, 164, 255, 1)",
			pointColor : "rgba(48, 164, 255, 1)",
			pointStrokeColor : "#fff",
			pointHighlightFill : "#fff",
			pointHighlightStroke : "rgba(48, 164, 255, 1)",
			data : ${requestScope.hourExecValues}
		}
	]
};
	
window.onload = function(){
	var chart1 = document.getElementById("line-chart").getContext("2d");
	window.myLine = new Chart(chart1).Line(lineChartData, {
		responsive: true
	});
};
</script>
<%@include file="layouts/footer.jsp" %>
