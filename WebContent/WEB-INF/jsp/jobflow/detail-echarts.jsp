<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="../layouts/header.jsp" %>
<script src="${requestScope.baseUrl}/static/js/echarts.min.js"></script>

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
				<div class="panel-heading">任务列表</div>
				<p class="help-block" style="margin-left: 15px;"></p>
				<div class="panel-body">
				    <a style="float:left;" class="btn btn-primary" href="${requestScope.baseUrl}/view/addjobflownode/${requestScope.jobflow.flowId}">+</a>
					<table id="table">
					    <thead>
					    <tr>
					        <th data-field="state" data-checkbox="true" >ID</th>
					        <th data-field="nodeId" data-sortable="true">节点ID</th>
					        <th data-field="jobId" data-sortable="true">作业ID</th>
					        <th data-field="jobTitle"  data-sortable="true">作业标题</th>
					        <th data-field="rely"  data-sortable="true">依赖节点</th>
					        <th data-field="action" data-sortable="true"  data-formatter="formatter">操作</th>
					    </tr>
					    </thead>
					   
					</table>
				</div>
			</div>
		</div>
	</div><!--/.row-->

	<div class="row" id="main" style="width: ${requestScope.canvas.width}px;height:${requestScope.canvas.height}px;">
		
	</div><!-- /.row -->
</div>
<script type="text/javascript">
$(function () {
    $('#table').bootstrapTable({
		url:"${requestScope.baseUrl}/api/jobflownodesbyfid/${requestScope.jobflow.flowId}",
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


function formatter(value, row, index) {
    return [
            '<a href="${requestScope.baseUrl}/view/updatejobflownode/' + row.nodeId +'" title="编辑">' +
                '<i class="glyphicon glyphicon-pencil"></i>' +
            '</a>',
            '&nbsp;&nbsp;&nbsp;&nbsp;',
            '<a href="javascript:deleteJobFlowNode(' + row.nodeId + ')" title="删除">' +
                '<i class="glyphicon glyphicon-remove"></i>' +
            '</a>'
        ].join('');
}


// 基于准备好的dom，初始化echarts实例
var myChart = echarts.init(document.getElementById('main'));
var datas = ${requestScope.forest.datas};
var links = ${requestScope.forest.links};
// 指定图表的配置项和数据
var option = {
    title: {
        text: '有向图'
    },
    tooltip: {},
    animationDurationUpdate: 1500,
    animationEasingUpdate: 'quinticInOut',
    series : [
        {
            type: 'graph',
            name : "JOB",
            layout: 'none',
            symbolSize: 50,
            roam: true,
            label: {
                normal: {
                    show: true
                }
            },
            edgeSymbol: ['circle', 'arrow'],
            edgeSymbolSize: [4, 10],
            edgeLabel: {
                normal: {
                    textStyle: {
                        fontSize: 20
                    }
                }
            },
            data: datas,
            links: links,
            lineStyle: {
                normal: {
                    opacity: 0.9,
                    width: 2,
                    curveness: 0
                }
            }
        }
    ]
};

// 使用刚指定的配置项和数据显示图表。
myChart.setOption(option);

$("#addnode").colorbox({width:"60%",close:"X"});
</script>
<%@include file="../layouts/footer.jsp" %>