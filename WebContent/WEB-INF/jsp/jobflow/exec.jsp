<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">
				<div class="panel-heading">执行作业流</div>
				<div class="panel-body">
					<div class="col-md-6">
						<div class="form-group">
							<label>作业流ID</label>
							<input name="flow_id" type="text" class="form-control" value="${requestScope.jobflow.flowId}" disabled="disabled">
						</div>
						<div class="form-group">
							<label>作业流名称</label>
							<input name="title" type="text" class="form-control" value="${requestScope.jobflow.title}" disabled="disabled">
						</div>
						<div class="form-group">
							<label>命令参数替换</label>
							<textarea name="cmd_param" class="form-control" rows="3"></textarea>
							<p class="help-block" style="font-style:italic;font-size: 14px;">
								例如：{today}=2018-01-01,每个参数一行 <br>
							</p>
						</div>
						<div class="form-group">
							    <label>选择主机&nbsp;&nbsp;&nbsp;&nbsp;</label>
							    <select name="host_id" class="form-control">
							    <c:forEach var="item" items="${requestScope.hostMapping}">
							    	<option value="${item.key}">${item.value}</option>
							    </c:forEach>
							    </select>
							</div>
						<div class="form-group">
							<label>重试次数</label>
							<input name="retry_num" class="form-control" placeholder="" value="3">
						</div>
						<div class="form-group">
							<label>重试时间间隔</label><em> (单位：秒)</em>
							<input name="retry_interval" class="form-control" placeholder="" value="30">
						</div>
					   <button type="submit" class="btn btn-primary" onclick="execJobFlow()">执行</button>
					   <button type="reset" class="btn btn-default" onclick="closeCbox()">取消</button>
				   </div>
				</div>
			</div>
		</div><!-- /.col-->
	</div><!-- /.row -->
</div>