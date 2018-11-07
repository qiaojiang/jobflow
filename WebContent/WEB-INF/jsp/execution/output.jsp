<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<div class="row" style="margin: 20px;">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">
			<c:if test="${requestScope.file_type == 'log'}">标准输出</c:if>
			<c:if test="${requestScope.file_type == 'err'}">错误输出</c:if>
			</div>
			<div class="panel-body">
				<div class="col-md-6">
					<input type="hidden" name="page" value="0" />
					<input type="hidden" name="tips" value="0" />
					<input type="hidden" name="file_type" value="${requestScope.file_type}" />
					<div id="content">
						<c:if test="${requestScope.hasLogFile == 0}">
						没有日志数据
						</c:if>
					</div>
					<c:if test="${requestScope.hasLogFile == 1}">
				    <button id="more" class="btn btn-default" onclick="getExecutionLog(${requestScope.execution.id})" style="width:300px;margin-left: 100px;">More</button>
				    </c:if>
			   </div>
			</div>
		</div>
	</div><!-- /.col-->
</div><!-- /.row -->

<script type="text/javascript">
$("#more").click();
</script>