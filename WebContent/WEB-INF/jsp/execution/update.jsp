<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="../layouts/header.jsp" %>
<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">
    <div class="row">
			<ol class="breadcrumb">
				<li><a href="#"><span class="glyphicon glyphicon-home"></span></a></li>
				<li class="active">exection</li>
			</ol>
		</div><!--/.row-->
		
		<div class="row">
			<div class="col-lg-12">
				<div class="panel panel-default">
					<div class="panel-heading">编辑</div>
					<div class="panel-body">
						<div class="col-md-6">
							<input name="id" type="hidden" value="${requestScope.execution.id}" >
						    <div class="form-group">
								<label>TOKEN</label>
								<input name="token" class="form-control" value="${requestScope.execution.token}" disabled="disabled">
							</div>
							<div class="form-group">
								<label>作业ID</label>
								<input name="job_id" class="form-control" value="${requestScope.execution.jobId}" disabled="disabled">
							</div>
							<div class="form-group">
								<label>作业名称</label>
								<input name="job_title" class="form-control" value="${requestScope.execution.jobTitle}" disabled="disabled">
							</div>
							<div class="form-group">
							    <label>状态&nbsp;&nbsp;&nbsp;&nbsp;</label>
							    <select name="status" class="form-control">
							    	<c:forEach var="item" items="${requestScope.statusMapping}">
							    	<c:choose>
								    	<c:when test="${item.key == requestScope.execution.status}">
								    	<option value="${item.key}" selected="selected">${item.value}</option>
								    	</c:when>
								    	<c:otherwise>
								    	<option value="${item.key}">${item.value}</option>
								    	</c:otherwise>
							    	</c:choose>
							    	</c:forEach>
							    </select>
							</div>	
						   <button type="submit" class="btn btn-primary" onclick="updateExecution()">保存</button>
						   <button type="reset" class="btn btn-default">取消</button>
					   </div>
					</div>
				</div>
			</div><!-- /.col-->
		</div><!-- /.row -->
	</div>
<%@include file="../layouts/footer.jsp" %>