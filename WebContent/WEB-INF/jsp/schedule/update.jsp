<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="../layouts/header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
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
					<div class="panel-heading">编辑</div>
					<div class="panel-body">
						<div class="col-md-6">
							<input name="schedule_id" type="hidden" value="${requestScope.schedule.scheduleId}" >
						    <div class="form-group">
								<label>标题</label>
								<input name="title" class="form-control" value="${requestScope.schedule.title}">
							</div>
							<div class="form-group">
							    <label>类型&nbsp;&nbsp;&nbsp;&nbsp;</label>
							    <select name="type" class="form-control">
							    	<option value="2" <c:if test="${requestScope.schedule.type == '2'}"> selected="selected" </c:if>>周期调度</option>
							    	<option value="1" <c:if test="${requestScope.schedule.type == '1'}"> selected="selected" </c:if>>简单调度</option>
							    </select>
							</div>	
							<div class="form-group">
								<label>开始时间</label>
								<input name="start_time" class="form-control" value="${requestScope.schedule.startTime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})">
							</div>
							<div class="form-group">
								<label>结束时间</label>
								<input name="end_time" class="form-control" value="${requestScope.schedule.endTime}" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})">
							</div>
							<div class="form-group">
								<label>CRON</label>
								<input name="cron" class="form-control" value="${requestScope.schedule.cron}">
							</div>
							<div class="form-group">
								<label>实体类型</label>
								<select name="entity_type" class="form-control">
							    	<option value="1" <c:if test="${requestScope.schedule.entityType == '1'}"> selected="selected" </c:if>>JOB</option>
							    	<option value="2" <c:if test="${requestScope.schedule.entityType == '2'}"> selected="selected" </c:if>>JOBFLOW</option>
							    </select>
							</div>
							<div class="form-group">
								<label>实体ID</label>
								<input name="entity_id" class="form-control" value="${requestScope.schedule.entityId}">
							</div>
							<div class="form-group">
								<label>描述</label>
								<textarea name="desc" class="form-control" rows="6">${requestScope.schedule.desc}</textarea>
							</div>
							<div class="form-group">
								<label>重试次数</label>
								<input name="retry_num" class="form-control" placeholder="" value="${requestScope.schedule.retryNum}">
							</div>
							<div class="form-group">
								<label>重试时间间隔</label><em> (单位：秒)</em>
								<input name="retry_interval" class="form-control" placeholder="" value="${requestScope.schedule.retryInterval}">
							</div>
							<div class="form-group">
							    <label>选择主机&nbsp;&nbsp;&nbsp;&nbsp;</label>
							    <select name="host_id" class="form-control">
							    <c:forEach var="item" items="${requestScope.hostMapping}">
							    	<option value="${item.key}" <c:if test="${requestScope.schedule.hostId == item.key}"> selected="selected" </c:if>>${item.value}</option>
							    </c:forEach>
							    </select>
							</div>
							<div class="form-group">
								<label>状态</label>
								<select name="status" class="form-control">
							    	<option value="1" <c:if test="${requestScope.schedule.status == '1'}"> selected="selected" </c:if>>正常</option>
							    	<option value="2" <c:if test="${requestScope.schedule.status == '2'}"> selected="selected" </c:if>>暂停</option>
							    </select>
							</div>
						   <button type="submit" class="btn btn-primary" onclick="updateSchedule()">保存</button>
						   <button type="reset" class="btn btn-default">取消</button>
					   </div>
					</div>
				</div>
			</div><!-- /.col-->
		</div><!-- /.row -->
	</div>
<script src="${requestScope.baseUrl}/static/js/datepicker/WdatePicker.js"></script>
<%@include file="../layouts/footer.jsp" %>