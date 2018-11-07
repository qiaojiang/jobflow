<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@include file="../layouts/header.jsp" %>
<script src="${requestScope.baseUrl}/static/js/g6.js"></script>
<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">
	<div class="row">
		<ol class="breadcrumb">
			<li><a href="#"><span class="glyphicon glyphicon-home"></span></a></li>
			<li class="active">作业流—${requestScope.jobflow.title}</li>
		</ol>
	</div><!--/.row-->
			
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">
				<div class="panel-heading">
					<a href="${requestScope.baseUrl}/view/jobflowdetail/${requestScope.jobflow.flowId}">节点列表</a> |  
					<a href="${requestScope.baseUrl}/view/jobflowdag/${requestScope.jobflow.flowId}">节点DAG</a>
				</div>
				<p class="help-block" style="margin-left: 15px;"></p>
				<div class="panel-body" id="dag">
					
				</div>
			</div>
		</div>
	</div><!--/.row-->
</div>	
<script type="text/javascript">
//DAG
const data = {
	nodes: ${requestScope.dag.nodes},
	edges: ${requestScope.dag.edges}	
}; 
const graph = new G6.Graph({
  container: 'dag',
  fitView: 'tc',
  width:1024,
  height:800
});
graph.edge({
  style: function style() {
    return {
      endArrow: true
    };
  }
});
graph.read(data);
let node;
let dx;
let dy;
graph.on('node:dragstart', ev=>{
  const {item} = ev;
  const model = item.getModel();
  node = item;
  dx = model.x - ev.x;
  dy = model.y - ev.y;
});
graph.on('node:drag', ev=>{
  node && graph.update(node, {
    x: ev.x+dx,
    y: ev.y+dy
  });
});
graph.on('node:dragend', ev=>{
  node = undefined;
});
G6.track(false);
</script>
<%@include file="../layouts/footer.jsp" %>