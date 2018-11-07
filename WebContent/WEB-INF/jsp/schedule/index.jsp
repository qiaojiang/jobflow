<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="../layouts/header.jsp" %>
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
				<div class="panel-heading">调度列表</div>
				<p class="help-block" style="margin-left: 15px;"></p>
				<div class="panel-body">
				    <a style="float:left;" class="btn btn-primary" href="${requestScope.baseUrl}/view/addschedule">+</a>
					<table id="table">
					    <thead>
					    <tr>
					        <th data-field="state" data-checkbox="true" >ID</th>
					        <th data-field="scheduleId" data-sortable="true">调度ID</th>
					        <th data-field="title"  data-sortable="true">标题</th>
					        <th data-field="desc"  data-sortable="true">描述</th>
					        <th data-field="type"  data-sortable="true" data-formatter="formatType">类型</th>
					        <th data-field="startTime"  data-sortable="true">开始时间</th>
					        <th data-field="endTime"  data-sortable="true">结束时间</th>
					        <th data-field="scheduleTime"  data-sortable="true">上次调度时间</th>
					        <th data-field="cron"  data-sortable="true">CRON</th>
					        <th data-field="entityType"  data-sortable="true" data-formatter="formatEntityType">实体类型</th>
					        <th data-field="entityId"  data-sortable="true">实体ID</th>
					        <th data-field="retryNum"  data-sortable="true">重试次数</th>
					        <th data-field="retryInterval"  data-sortable="true">重试间隔</th>
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
		url:"${requestScope.baseUrl}/api/schedules",
		pageSize:20,
		sidePagination:'server',
		showRefresh:"true",
		showToggle:"true",
		showColumns:"true",
		//search:"true",
		itemName:"toolbar1",
		pagination:"true",
		sortName:"name",
		sortOrder:"desc",
    });
});


function formatAction(value, row, index) {
    return [
            '<a href="${requestScope.baseUrl}/view/updateschedule/' + row.scheduleId +'">' +
                '<i class="glyphicon glyphicon-pencil"></i>' +
            '</a>',
            '&nbsp;&nbsp;&nbsp;&nbsp;',
            '<a href="javascript:deleteSchedule(' + row.scheduleId + ')">' +
                '<i class="glyphicon glyphicon-remove"></i>' +
            '</a>',
            '&nbsp;&nbsp;&nbsp;&nbsp;',
            '<a href="${requestScope.baseUrl}/view/scheduledetail/' + row.scheduleId + '" title="详情">' +
                '<i class="glyphicon glyphicon-info-sign"></i>' +
            '</a>'
        ].join('');
}

function formatType(value, row, index) {
	var mappings = ${requestScope.typeMapping};
	for( var i in mappings){
		if(value == i){
			return mappings[i];
		}
	}
	return "unknow";
}

function formatEntityType(value, row, index) {
	var mappings = ${requestScope.entityTypeMapping};
	for( var i in mappings){
		if(value == i){
			return mappings[i];
		}
	}
	return "unknow";
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