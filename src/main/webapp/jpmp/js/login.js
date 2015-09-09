$(document).ready(function(){
	$(".sbtn").click(function(){
		var userName = $("input[name='user_account']").val();
		var passwd = $("input[name='user_password']").val();
		if(userName==""||userName==null){
			alert("用户名不能为空");
			return;
		}
		if(passwd==""||passwd==null){
			alert("密码不能为空");
			return;
		}
		$.ajax({
			type:"post",
			url:"../rest/user/login",
			data:{user_account:userName,user_password:passwd,param:"server"},
			dataType:"html",
			success:function(data){
				if(data=="1"){
					alert("用户名不存在");
				}else if(data=="2"){
					alert("密码不正确");
				}else{
					data = eval("("+data+")");
					window.location.href = "../htmls/User.html";
				}
			}
		});
		return false;
	});
	
	/*
	 * 点击事件,显示二维码
	 */
	 $(".erweima-a").click(function(){
		 $(".saomiao").show();
		 $(".login_box").hide();
	 });
	 
	 
	 /*
	  *
	  */
	  
	  $(".saomiao .del-a").click(function(){
		  $(".saomiao").hide();
		 $(".login_box").show();
		  
		  
		});
});






























