<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="../layouts/header.jsp" %>
<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">
    <div class="row">
			<ol class="breadcrumb">
				<li><a href="#"><span class="glyphicon glyphicon-home"></span></a></li>
				<li class="active">作业</li>
			</ol>
		</div><!--/.row-->
		
		<div class="row">
			<div class="col-lg-12">
				<div class="panel panel-default">
					<div class="panel-heading">添加</div>
					<div class="panel-body">
						<div class="col-md-6">
						    <div class="form-group">
								<label>标题</label>
								<input name="title" class="form-control" placeholder="">
							</div>
							<div class="form-group">
							    <label>类型&nbsp;&nbsp;&nbsp;&nbsp;</label>
							    <select name="type" class="form-control">
							    <c:forEach var="item" items="${requestScope.typeMapping}">
							    	<option value="${item.key}">${item.value}</option>
							    </c:forEach>
							    </select>
							</div>	
							<div class="form-group">
								<label>命令</label>
								<textarea name="cmd" class="form-control" rows="6"></textarea>
								<p class="help-block" style="font-style:italic;font-size: 14px;">
									内置日期变量：<br>
									{today} 今日，格式yyyy-MM-dd <br>
									{today_nodash} 今日，格式yyyyMMdd <br>
									{yesterday} 昨日，格式yyyy-MM-dd <br>
									{yesterday_nodash} 昨日，格式yyyyMMdd <br>
									{tomorrow} 明日，格式yyyy-MM-dd <br>
									{tomorrow_nodash} 明日，格式yyyyMMdd <br>
									{hour} 当前小时，格式HH <br>
									{minute} 当前分钟，格式mm
								</p>
							</div>
							<div class="form-group">
								<label>描述</label>
								<textarea name="desc" class="form-control" rows="6"></textarea>
							</div>
						   <button type="submit" class="btn btn-primary" onclick="addJob()">保存</button>
						   <button type="reset" class="btn btn-default">取消</button>
					   </div>
					</div>
				</div>
			</div><!-- /.col-->
		</div><!-- /.row -->
	</div>
<%@include file="../layouts/footer.jsp" %>