<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="../layouts/header.jsp" %>
<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">
	<div class="row">
		<ol class="breadcrumb">
			<li><a href="#"><span class="glyphicon glyphicon-home"></span></a></li>
			<li class="active">执行历史</li>
		</ol>
	</div><!--/.row-->
			
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">
				<div class="panel-heading">执行历史</div>
				<p class="help-block" style="margin-left: 15px;"></p>
				<div class="panel-body">
					<table id="table">
					    <thead>
					    <tr>
					        <th data-field="state" data-checkbox="true" >ID</th>
					        <th data-field="id" data-sortable="true">ID</th>
					        <th data-field="token"  data-sortable="true" data-formatter="formatToken">调度TOKEN</th>
					        <th data-field="jobId"  data-sortable="true">作业ID</th>
					        <th data-field="jobTitle"  data-sortable="true">作业名称</th>
					        <th data-field="nodeId"  data-sortable="true">节点ID</th>
					        <th data-field="relyNode"  data-sortable="true">依赖节点</th>
					        <th data-field="createTime"  data-sortable="true">创建时间</th>
					        <th data-field="startTime"  data-sortable="true">开始时间</th>
							<th data-field="endTime"  data-sortable="true">结束时间</th>
							<th data-field="retryNum"  data-sortable="true">重试次数</th>
					        <th data-field="retryInterval"  data-sortable="true">时间间隔</th>
					        <th data-field="execNum"  data-sortable="true">执行次数</th>
					        <th data-field="hostId"  data-sortable="true">HOST</th>
					        <th data-field="groupId"  data-sortable="true" data-formatter="formatGroup">组</th>
					        <th data-field="status"  data-sortable="true" data-formatter="formatStatus">状态</th>
					        <th data-field="action" data-sortable="true" data-formatter="formatAction">操作</th>
					    </tr>
					    </thead>
					   
					</table>
				</div>
			</div>
		</div>
	</div><!--/.row-->
</div>	
<script type="text/javascript">
$(function () {
    $('#table').bootstrapTable({
		url:"${requestScope.baseUrl}/api/executions",
		pageSize:20,
		sidePagination:'server',
		showRefresh:"true",
		showToggle:"true",
		showColumns:"true",
		search:"true",
		itemName:"toolbar1",
		pagination:"true",
		sortName:"name",
		sortOrder:"desc"
    });
});

function formatAction(value, row, index) {
    return [
	    	'<a href="javascript:restartExecution(' + row.id +')" title="重跑">' +
		        '<i class="glyphicon glyphicon-play"></i>' +
		    '</a>',
		    '&nbsp;&nbsp;&nbsp;&nbsp;',
            '<a href="${requestScope.baseUrl}/view/updateexecution/' + row.id +'" title="编辑">' +
                '<i class="glyphicon glyphicon-pencil"></i>' +
            '</a>',
            '&nbsp;&nbsp;&nbsp;&nbsp;',
            '<a href="${requestScope.baseUrl}/view/executiondetail/' + row.id + '" title="详情">' +
                '<i class="glyphicon glyphicon-info-sign"></i>' +
            '</a>'
        ].join('');
}

function formatToken(value, row, index){
	return '<a href="${requestScope.baseUrl}/view/executiondag/' + row.token +'" title="DAG">' + value + '</a>';
}

function formatGroup(value, row, index) {
	var mappings = ${requestScope.groupMapping};
	for( var i in mappings){
		if(value == i){
			return mappings[i];
		}
	}
	return "unknow";
}

function formatStatus(value, row, index) {
	var mappings = ${requestScope.statusMapping};
	for( var i in mappings){
		if(value == i){
			return mappings[i];
		}
	}
	return "unknow";
}
</script>
<%@include file="../layouts/footer.jsp" %>