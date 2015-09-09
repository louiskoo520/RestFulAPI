$(document).ready(function(){
	$("button[type='submit']").click(function(){
		var userName = $("input[name='userName']").val();
		var passwd = $("input[name='passwd']").val();
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
			url:"user/login",
			data:{userName:userName,passwd:passwd},
			dataType:"html",
			success:function(data){
				if(data=="error"){
					alert("用户名或者密码错误");
				}else{
					data = eval("("+data+")");
					window.location.href = "userlist.html";
				}
			}
		});
	});
});






























