/**
 * 管理后台js
 */
function ajax_request(type,uri,param,tips){
	tips = tips || false;
	$.ajax({
		type:type,
		dataType : 'json',
		url : BASE_URL + uri,
		data : param,
		success: function(data){
			if(data.status == 0){
				if(tips) alert(data.message);
				location.href = BASE_URL + data.data;
			}else{
				alert(data.message);
			}
		},
	});
}

//添加用户
function addUser(){
	var param = {};
	param.username = $("input[name=username]").val();
	param.password = $("input[name=password]").val();
	var repassword = $("input[name=repassword]").val();
	param.nickname = $("input[name=nickname]").val();
	param.type = $("select[name=type]").val();
	param.group_id = $("select[name=group_id]").val();
	
	if(param.username == ''){
		alert("请输入用户名");return false;
	}
	if(param.password == ''){
		alert("请输入密码");return false;
	}
	if(param.password != repassword){
		alert("2次输入密码不一致");return false;
	}
	param.password = $.md5(param.password);
	if(param.nickname == ''){
		alert("请输入用户昵称");return false;
	}
	if(param.group_id == ''){
		alert("请输入组ID");return false;
	}
	ajax_request('PUT','/api/users/', param);
}

//更新用户
function updateUser(){
	var param = {};
	var uid = $("input[name=user_id]").val();
	param.username = $("input[name=username]").val();
	var password = $("input[name=password]").val();
	var repassword = $("input[name=repassword]").val();
	param.nickname = $("input[name=nickname]").val();
	param.type = $("select[name=type]").val();
	param.group_id = $("select[name=group_id]").val();
	
	if(param.username == ''){
		alert("请输入用户名");return false;
	}
	if(password != '' && password != repassword){
		alert("2次输入密码不一致");return false;
	}else{
		param.password = $.md5(password);
	}
	if(param.nickname == ''){
		alert("请输入用户昵称");return false;
	}
	if(param.group_id == ''){
		alert("请输入组ID");return false;
	}
	ajax_request('POST','/api/users/' + uid, param);
}

//删除用户
function deleteUser(uid){
	var param = {};
	if(confirm("确定删除该用户？")){
		ajax_request('DELETE', '/api/users/' + uid, param);
	}
}

//添加作业
function addJob(){
	var param = {};
	param.title = $("input[name=title]").val();
	param.type = $("select[name=type]").val();
	param.cmd = $("textarea[name=cmd]").val();
	param.desc = $("textarea[name=desc]").val();
	if(param.title == ''){
		alert("请输入作业标题");return false;
	}
	if(param.cmd == ''){
		alert("请输入作业执行命令");return false;
	}
	if(param.desc == ''){
		alert("请输入作业描述");return false;
	}
	ajax_request('PUT','/api/jobs/', param);
}

//更新作业
function updateJob(){
	var jobId = $("input[name=job_id]").val();
	var param = {};
	param.title = $("input[name=title]").val();
	param.type = $("select[name=type]").val();
	param.cmd = $("textarea[name=cmd]").val();
	param.desc = $("textarea[name=desc]").val();
	if(jobId == ''){
		alert("JOBID不存在");return false;
	}
	if(param.title == ''){
		alert("请输入作业标题");return false;
	}
	if(param.cmd == ''){
		alert("请输入作业执行命令");return false;
	}
	if(param.desc == ''){
		alert("请输入作业描述");return false;
	}
	ajax_request('POST','/api/jobs/' + jobId, param);
}

//删除作业
function deleteJob(id){
	if(confirm("确定删除该作业？")){
		ajax_request('DELETE', '/api/jobs/' + id, {});
	}
}

//执行作业
function execJob(){
	var jobId = $("input[name=job_id]").val();
	var param = {};
	param.cmd_param = $("textarea[name=cmd_param]").val();
	param.retry_num = $("input[name=retry_num]").val();
	param.retry_interval = $("input[name=retry_interval]").val();
	param.host_id = $("select[name=host_id]").val();
	
	if(jobId == ''){
		alert("JOB ID不存在");return false;
	}
	ajax_request('POST','/api/jobs/exec/' + jobId, param);
}

//添加作业流
function addJobFlow(){
	var param = {};
	param.title = $("input[name=title]").val();
	param.desc = $("textarea[name=desc]").val();
	if(param.title == ''){
		alert("请输入作业流标题");return false;
	}
	if(param.desc == ''){
		alert("请输入作业流描述");return false;
	}
	ajax_request('PUT','/api/jobflows/', param);
}

//更新作业流
function updateJobFlow(){
	var flowId = $("input[name=flow_id]").val();
	var param = {};
	param.title = $("input[name=title]").val();
	param.desc = $("textarea[name=desc]").val();
	if(param.title == ''){
		alert("请输入作业流标题");return false;
	}
	if(param.desc == ''){
		alert("请输入作业流描述");return false;
	}
	ajax_request('POST','/api/jobflows/' + flowId, param);
}

//删除作业流
function deleteJobFlow(id){
	if(confirm("确定删除该作业流？")){
		ajax_request('DELETE', '/api/jobflows/' + id, {});
	}
}

//执行作业流
function execJobFlow(){
	var flowId = $("input[name=flow_id]").val();
	var param = {};
	param.cmd_param = $("textarea[name=cmd_param]").val();
	param.retry_num = $("input[name=retry_num]").val();
	param.retry_interval = $("input[name=retry_interval]").val();
	param.host_id = $("select[name=host_id]").val();
	
	if(flowId == ''){
		alert("JOBFLOW ID不存在");return false;
	}
	ajax_request('POST','/api/jobflows/exec/' + flowId, param, true);
}

//添加作业流节点
function addJobFlowNode(flowId){
	var param = {};
	param.flow_id = flowId;
	param.job_id = $("input[name=job_id]").val();
	param.rely = $("input[name=rely]").val();
	if(param.job_id == ''){
		alert("请输入作业ID");return false;
	}
	if(param.rely == ''){
		alert("请输入依赖节点");return false;
	}
	ajax_request('PUT','/api/jobflownodes/', param);
}

//更新作业流节点
function updateJobFlowNode(){
	var nodeId = $("input[name=node_id]").val();
	var param = {};
	param.job_id = $("input[name=job_id]").val();
	param.rely = $("input[name=rely]").val();
	if(param.job_id == ''){
		alert("请输入作业ID");return false;
	}
	if(param.rely == ''){
		alert("请输入依赖节点");return false;
	}
	ajax_request('POST','/api/jobflownodes/' + nodeId, param);
}

//删除作业节点
function deleteJobFlowNode(id){
	if(confirm("确定删除该节点？")){
		ajax_request('DELETE', '/api/jobflownodes/' + id, {});
	}
}

//添加调度
function addSchedule(){
	var param = {};
	param.title = $("input[name=title]").val();
	param.type = $("select[name=type]").val();
	param.start_time = $("input[name=start_time]").val();
	param.end_time = $("input[name=end_time]").val();
	param.cron = $("input[name=cron]").val();
	param.entity_type = $("select[name=entity_type]").val();
	param.entity_id = $("input[name=entity_id]").val();
	param.desc = $("textarea[name=desc]").val();
	param.retry_num = $("input[name=retry_num]").val();
	param.retry_interval = $("input[name=retry_interval]").val();
	param.host_id = $("select[name=host_id]").val();
	param.status = $("select[name=status]").val();
	
	if(param.title == ''){
		alert("请输入调度标题");return false;
	}
	if(param.start_time != ''){
		var start = new Date(param.start_time.replace(/-/g,"\/")); 
		var end = new Date(param.end_time.replace(/-/g,"\/"));
		if(start > end){
			alert("开始时间应该在结束时间之前");return false;
		}
	}
	if(param.entity_id == ''){
		alert("请输入要调度实体ID");return false;
	}
	if(param.desc == ''){
		alert("请输入调度描述");return false;
	}
	
	ajax_request('PUT','/api/schedules',param);
}

//更新调度
function updateSchedule(){
	var scheduleId = $("input[name=schedule_id]").val();
	var param = {};
	param.title = $("input[name=title]").val();
	param.type = $("select[name=type]").val();
	param.start_time = $("input[name=start_time]").val();
	param.end_time = $("input[name=end_time]").val();
	param.cron = $("input[name=cron]").val();
	param.entity_type = $("select[name=entity_type]").val();
	param.entity_id = $("input[name=entity_id]").val();
	param.desc = $("textarea[name=desc]").val();
	param.retry_num = $("input[name=retry_num]").val();
	param.retry_interval = $("input[name=retry_interval]").val();
	param.host_id = $("select[name=host_id]").val();
	param.status = $("select[name=status]").val();
	if(param.title == ''){
		alert("请输入调度标题");return false;
	}
	if(param.start_time != ''){
		var start = new Date(param.start_time.replace(/-/g,"\/")); 
		var end = new Date(param.end_time.replace(/-/g,"\/"));
		if(start > end){
			alert("开始时间应该在结束时间之前");return false;
		}
	}
	if(param.entity_id == ''){
		alert("请输入要调度实体ID");return false;
	}
	if(param.desc == ''){
		alert("请输入调度描述");return false;
	}
	
	ajax_request('POST','/api/schedules/' + scheduleId, param);
}

//删除调度
function deleteSchedule(id){
	if(confirm("确定删除该调度？")){
		ajax_request('DELETE', '/api/schedules/' + id, {});
	}
}

//更新执行历史
function updateExecution(){
	var id = $("input[name=id]").val();
	var param = {};
	param.status = $("select[name=status]").val();
	
	ajax_request('POST','/api/executions/' + id, param);
}

function restartExecution(id){
	ajax_request('POST','/api/executions/restart/' + id, {}, true);
}

function getExecutionLog(id){
	var param = {};
	param.file_type = $("input[name=file_type]").val();
	param.page = parseInt($("input[name=page]").val()) + 1;
	$.ajax({
		type: 'POST',
		dataType : 'json',
		url : BASE_URL + '/api/executions/log/' + id,
		data : param,
		success: function(data){
			if(data.status == 0){
				if(data.data.rows != ""){
					$("input[name=page]").val(data.data.page);
					$("#content").append(data.data.rows);
				}else{
					if($("input[name=tips]").val() == 1){
						alert("没有更多数据了");
					}else{
						$("input[name=tips]").val(1);
					}
				}
			}else{
				alert(data.message);
			}
		},
	});
}

//添加HOST
function addHost(){
	var param = {};
	param.host = $("input[name=host]").val();
	param.type = $("select[name=type]").val();
	param.username = $("input[name=username]").val();
	param.desc = $("textarea[name=desc]").val();
	
	if(param.host == ''){
		alert("请输入HOST");return false;
	}
	if(param.username == ''){
		alert("请输入用户名");return false;
	}
	
	ajax_request('PUT','/api/hosts',param);
}

//更新HOST
function updateHost(){
	var hostId = $("input[name=host_id]").val();
	var param = {};
	param.host = $("input[name=host]").val();
	param.type = $("select[name=type]").val();
	param.username = $("input[name=username]").val();
	param.desc = $("textarea[name=desc]").val();
	
	if(param.host == ''){
		alert("请输入HOST");return false;
	}
	if(param.username == ''){
		alert("请输入用户名");return false;
	}
	
	ajax_request('POST','/api/hosts/' + hostId, param);
}

//删除Host
function deleteHost(id){
	if(confirm("删除主机信息可能导致任务执行失败，确定删除吗？")){
		ajax_request('DELETE', '/api/hosts/' + id, {});
	}
}

Date.prototype.format = function(fmt) { 
    var o = { 
       "M+" : this.getMonth()+1,                 //月份 
       "d+" : this.getDate(),                    //日 
       "h+" : this.getHours(),                   //小时 
       "m+" : this.getMinutes(),                 //分 
       "s+" : this.getSeconds(),                 //秒 
       "q+" : Math.floor((this.getMonth()+3)/3), //季度 
       "S"  : this.getMilliseconds()             //毫秒 
   }; 
   if(/(y+)/.test(fmt)) {
           fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
   }
    for(var k in o) {
       if(new RegExp("("+ k +")").test(fmt)){
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        }
    }
   return fmt; 
} 

function time2str(time){
	if(time == "" || time == 0) return "";
	return new Date(1514975403000).format("yyyy-MM-dd hh:mm:ss");
}


function changeClass(type){
	var html = '<option value="">请选择</option>';
	var cid = '';
	if(type == 1){
		cid = $("select[name='one_class']").val();
	}else{
		cid = $("select[name='two_class']").val();
		if(cid == ''){
			$("select[name='three_class']").html();
			return false;
		}
	}
	$.post("index.php?r=class/sub-list",{cid:cid},function(msg){
		var data = msg.data;
		for(var i in data){
			html += '<option value="'+data[i].class_id+'">'+data[i].title+'</option>';
		}
		if(type == 1){
			$("select[name='two_class']").html(html);
		}else{
			$("select[name='three_class']").html(html);
		}
	},'json');
}

function optClass(){
	var one_id = $("select[name=one_class]").val();
	var two_id = $("select[name=two_class]").val();
	var three_id = $("select[name=three_class]").val();
	var one_text = $("select[name=one_class] option:selected").text();
	var two_text = $("select[name=two_class] option:selected").text();
	var three_text = $("select[name=three_class] option:selected").text();
	var text = one_text;
	if(two_id != '' && two_text != '') text += "-" + two_text;
	if(three_id != '' && three_text != '') text += "-" + three_text;
	var id = one_id;
	if(two_id != '') id = two_id;
	if(three_id != '') id = three_id;
	var html = '<div id="c_'+id+'" class="btn btn-default" style="margin:5px 5px 0 0">'+text+'<input type="hidden" name="class" value="'+id+'"/><a href="javascript:delOption('+id+');" class="trash"><span class="glyphicon glyphicon-trash"></span></a></div>';
	if($("#c_"+id).length == 0){
		$("#class_show").append(html);
	}
	$('#cboxClose').click();
}

function delOption(id){
	$("#c_"+id).remove();
}

function closeCbox(){
	$('#cboxClose').click();
}

//删除分类
function deleteFeeds(fids){
	var fids = '';
	$(".selected").each(function(){
		var c = $(this).children().eq(1).text();
		fids += "," + c;
	});
	if(fids != '') fids = fids.substring(1);
	if(confirm("确定删除该feed？")){
		$.post("index.php?r=feed/do-delete",
			{
			fid:fids
			},
			function(data){
				if(data.status == 0){
					location.reload();
				}else{
					alert(data.msg);
				}
		},'json');
	}
}

function searchUser(){
	var keyword = $("#search").val();
	if(keyword == '' || keyword.length < 2) return false;
	var html = '<div><ul class="sey-list sey-show">';
	var show = '';
	$.post('index.php?r=user/do-search',{keyword:keyword},function(data){
		if(data.status == 0){
			for(var i in data.data){
				show = "UID：" + data.data[i].uid + "，昵称：" + data.data[i].nick_name;
				html += '<li class="sey-item" onclick="searchShow(\''+show+'\');">'+data.data[i].nick_name+'</li>';
			}
			html += '</ul></div>';
			$("#search_btn").after(html);
		}
	},'json');
	return true;
}

function searchShow(show){
	$("#search_show").html(show);
	$(".sey-list").hide();
}

