<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="../layouts/header.jsp" %>
<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">
    <div class="row">
			<ol class="breadcrumb">
				<li><a href="#"><span class="glyphicon glyphicon-home"></span></a></li>
				<li class="active">作业流</li>
			</ol>
		</div><!--/.row-->
		
		<div class="row">
			<div class="col-lg-12">
				<div class="panel panel-default">
					<div class="panel-heading">编辑</div>
					<div class="panel-body">
						<div class="col-md-6">
							<input name="node_id" type="hidden" value="${requestScope.jobflownode.nodeId}" >
							<div class="form-group">
								<label>作业ID</label>
								<input name="job_id" class="form-control" value="${requestScope.jobflownode.jobId}">
							</div>
							<div class="form-group">
								<label>依赖节点</label>
								<input name="rely" class="form-control" value="${requestScope.jobflownode.rely}">
							</div>
						   <button type="submit" class="btn btn-primary" onclick="updateJobFlowNode()">保存</button>
						   <button type="reset" class="btn btn-default">取消</button>
					   </div>
					</div>
				</div>
			</div><!-- /.col-->
		</div><!-- /.row -->
	</div>
<%@include file="../layouts/footer.jsp" %>