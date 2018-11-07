<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="../layouts/header.jsp" %>
<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">
	<div class="row">
		<ol class="breadcrumb">
			<li><a href="#"><span class="glyphicon glyphicon-home"></span></a></li>
			<li class="active">执行</li>
		</ol>
	</div><!--/.row-->
			
	<div class="row">
			<div class="col-lg-12">
				<div class="panel panel-default">
					<div class="panel-heading">详细信息</div>
					<div class="panel-body">
						<div class="col-md-6">
						    <div class="form-group">
								<label>ID:</label><span style="margin-left: 20px;">${requestScope.execution.id}</span>
							</div>
							<div class="form-group">
								<label>TOKEN:</label><span style="margin-left: 20px;">${requestScope.execution.token}</span>
							</div>
							<div class="form-group">
								<label>作业:</label>
								<span style="margin-left: 20px;">
								[id: ${requestScope.execution.jobId}, title: ${requestScope.execution.jobTitle}]
								</span>
							</div>
							<div class="form-group">
								<label>作业流:</label>
								<span style="margin-left: 20px;">
									<c:if test="${requestScope.jobflow != null}">
									[id: ${requestScope.jobflow.flowId}, title: ${requestScope.jobflow.title}]
									</c:if>
								</span>
							</div>
							<div class="form-group">
								<label>节点ID:</label><span style="margin-left: 20px;">${requestScope.execution.nodeId}</span>
							</div>
							<div class="form-group">
								<label>依赖节点:</label><span style="margin-left: 20px;">${requestScope.execution.relyNode}</span>
							</div>
							<div class="form-group">
								<label>执行命令:</label><span style="margin-left: 20px;">${requestScope.execution.cmd}</span>
							</div>
							<div class="form-group">
								<label>创建时间:</label><span style="margin-left: 20px;">${requestScope.execution.createTime}</span>
							</div>
							<div class="form-group">
								<label>执行开始时间:</label><span style="margin-left: 20px;">${requestScope.execution.startTime}</span>
							</div>
							<div class="form-group">
								<label>执行结束时间:</label><span style="margin-left: 20px;">${requestScope.execution.endTime}</span>
							</div>
							<div class="form-group">
								<label>重试次数:</label><span style="margin-left: 20px;">${requestScope.execution.retryNum}</span>
							</div>
							<div class="form-group">
								<label>重试时间间隔:</label><span style="margin-left: 20px;">${requestScope.execution.retryInterval}</span>
							</div>
							<div class="form-group">
								<label>执行次数:</label><span style="margin-left: 20px;">${requestScope.execution.execNum}</span>
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
									<c:if test="${item.key == requestScope.execution.groupId}">
									${item.value}
									</c:if>
								</c:forEach>
								</span>
							</div>
							<div class="form-group">
								<label>状态:</label>
								<span style="margin-left: 20px;">
								<c:forEach var="item" items="${requestScope.statusMapping}">
									<c:if test="${item.key == requestScope.execution.status}">
									${item.value}
									</c:if>
								</c:forEach>
								</span>
							</div>
							<div class="form-group">
								<a class="colorbox" href="${requestScope.baseUrl}/view/executionout/${requestScope.execution.id}/stdout">标准输出</a> | 
								<a class="colorbox" href="${requestScope.baseUrl}/view/executionout/${requestScope.execution.id}/stderr">错误输出</a>
							</div>
						   <a class="btn btn-primary" href="javascript:void(0);" onclick="history.go(-1);">返回</a>
					   </div>
					</div>
				</div>
			</div><!-- /.col-->
		</div><!-- /.row -->
</div>	
<script type="text/javascript">
$(".colorbox").colorbox({width:"60%",height:"60%",close:"X"});
</script>
<%@include file="../layouts/footer.jsp" %>