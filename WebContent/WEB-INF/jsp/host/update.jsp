<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="../layouts/header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">
    <div class="row">
			<ol class="breadcrumb">
				<li><a href="#"><span class="glyphicon glyphicon-home"></span></a></li>
				<li class="active">主机</li>
			</ol>
		</div><!--/.row-->
		<div class="row">
			<div class="col-lg-12">
				<div class="panel panel-default">
					<div class="panel-heading">编辑</div>
					<div class="panel-body">
						<div class="col-md-6">
							<input name="host_id" type="hidden" value="${requestScope.host.hostId}" >
						    <div class="form-group">
								<label>HOST</label>
								<input name="host" class="form-control" value="${requestScope.host.host}">
							</div>
							<div class="form-group">
							    <label>类型&nbsp;&nbsp;&nbsp;&nbsp;</label>
							    <select name="type" class="form-control">				    	
							    	<option value="1" <c:if test="${requestScope.host.type == '1'}"> selected="selected" </c:if>>SSH免密码登录</option>
							    </select>
							</div>	
							<div class="form-group">
								<label>用户名</label>
								<input name="username" class="form-control" placeholder="" value="${requestScope.host.username}">
							</div>
							<div class="form-group">
								<label>描述</label>
								<textarea name="desc" class="form-control" rows="6">${requestScope.host.desc}</textarea>
							</div>
						   <button type="submit" class="btn btn-primary" onclick="updateHost()">保存</button>
						   <button type="reset" class="btn btn-default">取消</button>
					   </div>
					</div>
				</div>
			</div><!-- /.col-->
		</div><!-- /.row -->
	</div>
<script src="${requestScope.baseUrl}/static/js/datepicker/WdatePicker.js"></script>
<%@include file="../layouts/footer.jsp" %>