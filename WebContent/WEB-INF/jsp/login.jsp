<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>JOBFLOW</title>
<link rel="icon" type="image/png" href="${requestScope.baseUrl}/static/css/images/icon.png" sizes="32x32">
<link href="${requestScope.baseUrl}/static/css/bootstrap.min.css" rel="stylesheet">
<link href="${requestScope.baseUrl}/static/css/datepicker3.css" rel="stylesheet">
<link href="${requestScope.baseUrl}/static/css/styles.css" rel="stylesheet">

<!--[if lt IE 9]>
<script src="${requestScope.baseUrl}/static/js/html5shiv.js"></script>
<script src="${requestScope.baseUrl}/static/js/respond.min.js"></script>
<![endif]-->

</head>

<body>
	<div class="row">
		<div class="col-xs-10 col-xs-offset-1 col-sm-8 col-sm-offset-2 col-md-4 col-md-offset-4">
			<div class="login-panel panel panel-default">
				<div class="panel-heading">JobFlow</div>
				<div class="panel-body">
					<div style="margin: 20px 20px 40px 20px;">
						<div class="form-group">
							<input class="form-control" placeholder="LoginName" name="username" type="username" autofocus="">
						</div>
						<br>
						<div class="form-group">
							<input class="form-control" placeholder="Password" name="password" type="password" value="">
						</div>
						<br>
						<div class="checkbox" style="display: none;">
							<label>
								<input name="remember" type="checkbox" value="1">Remember Me
							</label>
						</div>
						<div class="form-group">
							<a href="javascript:void(0);" class="btn btn-primary form-control" onclick="login()">Login</a>
						</div>
						<br>
					</div>
				</div>
			</div>
		</div><!-- /.col-->
	</div><!-- /.row -->	

	<script src="${requestScope.baseUrl}/static/js/jquery-1.11.1.min.js"></script>
	<script src="${requestScope.baseUrl}/static/js/bootstrap.min.js"></script>
	<script src="${requestScope.baseUrl}/static/js/jquery.md5.js"></script>
	<script>
		!function ($) {
			$(document).on("click","ul.nav li.parent > a > span.icon", function(){		  
				$(this).find('em:first').toggleClass("glyphicon-minus");	  
			}); 
			$(".sidebar span.icon").find('em:first').addClass("glyphicon-plus");
		}(window.jQuery);

		$(window).on('resize', function () {
		  if ($(window).width() > 768) $('#sidebar-collapse').collapse('show')
		})
		$(window).on('resize', function () {
		  if ($(window).width() <= 767) $('#sidebar-collapse').collapse('hide')
		})
		
		$(function(){
			document.onkeydown = function(e){ 
			    var ev = document.all ? window.event : e;
			    if(ev.keyCode==13) {
			    	login();
			    }
			}
		});  
		
		function login(){
			var username = $("input[name=username]").val();
			var password = $("input[name=password]").val();
			var remember = 0;
			if(username == ''){
				alert("请输入用户名");return false;
			}
			if(password == ''){
				alert("请输入密码");return false;
			}
			password = $.md5(password);
			if($("input[name=remember]").is(':checked')){
				remember = 1;
			}
			$.ajax({
				type:'post',
				dataType : 'json',
				url : "${requestScope.baseUrl}/passport/login",
				data : {username:username,password:password,remember:remember},
				success: function(data){
					if(data.status == 0){
						location.href = "${requestScope.baseUrl}" + data.data;
					}else{
						alert(data.message);
					}
				},
			});
		}
	</script>	
</body>

</html>
