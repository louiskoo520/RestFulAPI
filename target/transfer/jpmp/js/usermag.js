$(document).ready(function(){
	var userId = getQueryString("userId");
	$.ajax({
		async:false,
		type:"post",
		url:"user/listUser",
		data:{userId:userId},
		dataType:"json",
		success:function(data){
			data = data[0];
			$("#userId").html(data.id);
			$("#userName").html(data.name);
			var html = "";
			if(data.type==1){
				html += "<option value=1 selected='selected'>系统默认用户</option>";
				html += "<option value=2>手机端注册用户</option>";
				html += "<option value=3>服务端注册用户</option>";
				html += "<option value=4>系统管理员</option>";
			}else if(data.type==2){
				html += "<option value=1>系统默认用户</option>";
				html += "<option value=2 selected='selected'>手机端注册用户</option>";
				html += "<option value=3>服务端注册用户</option>";
				html += "<option value=4>系统管理员</option>";
			}else if(data.type==3){
				html += "<option value=1>系统默认用户</option>";
				html += "<option value=2>手机端注册用户</option>";
				html += "<option value=3 selected='selected'>服务端注册用户</option>";
				html += "<option value=4>系统管理员</option>";
			}else if(data.type==4){
				html += "<option value=1>系统默认用户</option>";
				html += "<option value=2>手机端注册用户</option>";
				html += "<option value=3>服务端注册用户</option>";
				html += "<option value=4 selected='selected'>系统管理员</option>";
			}
			$("#level").empty();
			$("#level").append(html);
			$.ajax({
				async:false,
				type:"post",
				url:"role/listRole",
				dataType:"json",
				success:function(datas){
					var html2 = "";
					for(var i in datas){
						var role = datas[i];
						if(role.id==data.roleId){
							html2 += "<option selected='selected' value="+data.roleId+">"+role.name+"</option>";
						}else{
							html2 += "<option value="+role.id+">"+role.name+"</option>";
						}
					}
					$("#userRole").empty();
					$("#userRole").append(html2);
				}
			});
			$("#last").html(data.last);
			if(data.type==1||data.type==2){
				$("#extremity").html("手机端");
			}else{
				$("#extremity").html("PC端");
			}
			$("#userPhone").html(data.tel);
			$("#userQQ").html(data.qq);
			var html3 = "";
			var departmentArr = [];
			departmentArr = getDeptList();
			for(var i in departmentArr){
				if(data.department==departmentArr[i]){
					html3 += "<option selected='selected' value="+data.department+">"+data.department+"</option>";
				}else{
					html3 += "<option value="+departmentArr[i]+">"+departmentArr[i]+"</option>";
				}
			}
			$("#department").empty();
			$("#department").append(html3);
		}
	});
	//点击修改按钮修改
	$(".btna").click(function(){
		var newLevel = $("#level").val();
		var newRole = $("#userRole").val();
		var newDept = $("#department").val();
		$.ajax({
			async:false,
			type:"post",
			url:"user/update",
			data:{userId:userId,newLevel:newLevel, newRole:newRole, newDept:newDept},
			dataType:"html",
			success:function(data){
				if(data=="error"){
					alert("出现错误");
				}else{
					alert("修改成功");
				}
			}
		});
	});
	
});