<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="../layouts/header.jsp" %>
<script src="${requestScope.baseUrl}/static/js/jquery.md5.js"></script>
<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">
    <div class="row">
			<ol class="breadcrumb">
				<li><a href="#"><span class="glyphicon glyphicon-home"></span></a></li>
				<li class="active">用户</li>
			</ol>
		</div><!--/.row-->
		
		<div class="row">
			<div class="col-lg-12">
				<div class="panel panel-default">
					<div class="panel-heading">编辑</div>
					<div class="panel-body">
						<div class="col-md-6">
							<input name="user_id" type="hidden" value="${requestScope.user.userId}" >
						    <div class="form-group">
								<label>用户名</label>
								<input name="username" class="form-control" value="${requestScope.user.username}">
							</div>
							<div class="form-group">
								<label>密码</label>
								<input name="password" class="form-control" placeholder="仅需要修改密码时填写" value="" type="password">
							</div>
							<div class="form-group">
								<label>确认密码</label>
								<input name="repassword" class="form-control" placeholder="仅需要修改密码时填写" value="" type="password">
							</div>
							<div class="form-group">
								<label>昵称</label>
								<input name="nickname" class="form-control" value="${requestScope.user.nickname}">
							</div>
							<div class="form-group">
							    <label>类型&nbsp;&nbsp;&nbsp;&nbsp;</label>
							    <select name="type" class="form-control">
							    	<option value="2" <c:if test="${user.type == 2}">selected</c:if>>用户</option>
							    	<option value="1" <c:if test="${user.type == 1}">selected</c:if>>管理员</option>
							    </select>
							</div>
							<div class="form-group">
								<label>所属组</label>
								<select name="group_id" class="form-control">
									<c:forEach var="group" items="${groupList}">
									<option value="${group.groupId}" <c:if test="${user.groupId == group.groupId}">selected</c:if>>${group.title}</option>
									</c:forEach>
								</select>
							</div>
							
						   <button type="submit" class="btn btn-primary" onclick="updateUser()">保存</button>
						   <button type="reset" class="btn btn-default">取消</button>
					   </div>
					</div>
				</div>
			</div><!-- /.col-->
		</div><!-- /.row -->
	</div>
<%@include file="../layouts/footer.jsp" %>