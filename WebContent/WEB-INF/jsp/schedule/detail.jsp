<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="../layouts/header.jsp" %>
<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">
	<div class="row">
		<ol class="breadcrumb">
			<li><a href="#"><span class="glyphicon glyphicon-home"></span></a></li>
			<li class="active">调度</li>
		</ol>
	</div><!--/.row-->
			
	<div class="row">
			<div class="col-lg-12">
				<div class="panel panel-default">
					<div class="panel-heading">详细信息</div>
					<div class="panel-body">
						<div class="col-md-6">
						    <div class="form-group">
								<label>ID:</label><span style="margin-left: 20px;">${requestScope.schedule.scheduleId}</span>
							</div>
							<div class="form-group">
								<label>名称:</label><span style="margin-left: 20px;">${requestScope.schedule.title}</span>
							</div>
							<div class="form-group">
								<label>描述:</label><span style="margin-left: 20px;">${requestScope.schedule.desc}</span>
							</div>
							<div class="form-group">
								<label>实体类型:</label>
								<span style="margin-left: 20px;">
									<c:if test="${requestScope.schedule.entityType == 1}">作业</c:if>
									<c:if test="${requestScope.schedule.entityType == 2}">作业流</c:if>
								</span>
							</div>
							<div class="form-group">
								<label>实体:</label>
								<span style="margin-left: 20px;">${requestScope.entity}</span>
							</div>
							<div class="form-group">
								<label>调度类型:</label>
								<span style="margin-left: 20px;">
									<c:if test="${requestScope.schedule.type == 1}">简单调度</c:if>
									<c:if test="${requestScope.schedule.type == 2}">周期调度</c:if>
								</span>
							</div>
							<div class="form-group">
								<label>CRON:</label><span style="margin-left: 20px;">${requestScope.schedule.cron}</span>
							</div>
							<div class="form-group">
								<label>创建时间:</label><span style="margin-left: 20px;">${requestScope.schedule.createTime}</span>
							</div>
							<div class="form-group">
								<label>更新时间:</label><span style="margin-left: 20px;">${requestScope.schedule.updateTime}</span>
							</div>
							<div class="form-group">
								<label>开始时间:</label><span style="margin-left: 20px;">${requestScope.schedule.startTime}</span>
							</div>
							<div class="form-group">
								<label>结束时间:</label><span style="margin-left: 20px;">${requestScope.schedule.endTime}</span>
							</div>
							<div class="form-group">
								<label>上次调度时间:</label><span style="margin-left: 20px;">${requestScope.schedule.scheduleTime}</span>
							</div>
							<div class="form-group">
								<label>重试次数:</label><span style="margin-left: 20px;">${requestScope.schedule.retryNum}</span>
							</div>
							<div class="form-group">
								<label>重试时间间隔:</label><span style="margin-left: 20px;">${requestScope.schedule.retryInterval}</span>
							</div>
							<div class="form-group">
								<label>主机:</label>
								<span style="margin-left: 20px;">
									<c:if test="${requestScope.host != null}">
									[id: ${requestScope.host.hostId}, host: ${requestScope.host.host}, user: ${requestScope.host.username}]
									</c:if>
								</span>
							</div>
							<div class="form-group">
								<label>所属组:</label>
								<span style="margin-left: 20px;">
								<c:forEach var="item" items="${requestScope.groupMapping}">
									<c:if test="${item.key == requestScope.schedule.groupId}">
									${item.value}
									</c:if>
								</c:forEach>
								</span>
							</div>
							<div class="form-group">
								<label>状态:</label>
								<span style="margin-left: 20px;">
								<c:forEach var="item" items="${requestScope.statusMapping}">
									<c:if test="${item.key == requestScope.schedule.status}">
									${item.value}
									</c:if>
								</c:forEach>
								</span>
							</div>
						   <a class="btn btn-primary" href="javascript:void(0);" onclick="history.go(-1);">返回</a>
					   </div>
					</div>
				</div>
			</div><!-- /.col-->
		</div><!-- /.row -->
</div>	
<%@include file="../layouts/footer.jsp" %>