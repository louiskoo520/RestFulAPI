$(document).ready(function(){
	var typeArr = [];
	var pageIndex = 1;
	var pageCount;
	var startOrEnd = "load";
	userList(typeArr);
	//点击下一页下一个首页尾页
	$(document).on("click",".page-1 div",function(){
		var inner = $(this).html();
		if(isNaN(inner)){
			if(inner=="&lt;&lt;"){
				pageIndex = 1;
				startOrEnd = "first";
			}else if(inner=="&gt;&gt;"){
				pageIndex = pageCount;
				startOrEnd = "last";
			}
		}else{
			pageIndex = inner;
			startOrEnd = null;
			if($(this).prev().attr("class").indexOf("first")>0){
				startOrEnd = "start";
			}else if($(this).next().attr("class").indexOf("last")>0){
				startOrEnd = "end";
			}
		}
		userList(typeArr);
	});
	$(".header a").click(function(){
		startOrEnd="load";
		pageIndex = 1;
		$(this).siblings().each(function(){
			$(this).removeClass("current");
		});
		$(this).addClass("current");
		if($(this).html()=="全部用户"){
			typeArr = [];
		}else if($(this).html()=="服务端管理员"){
			typeArr = [];
			typeArr.push(3);
			typeArr.push(4);
		}else if($(this).html()=="手机端用户"){
			typeArr = [];
			typeArr.push(1);
			typeArr.push(2);
		}
		$(".qs2").html($(this).html());
		userList(typeArr);
	});
	//改变用户角色
	$(document).on("click",".change-role",function(){
		var type = $(this).attr("typee");
		if(type!=1&&type!=2){
			alert("服务端用户无需分配处理问题和任务的角色");
			return;
		}
		var userId = $(this).attr("idd");
		window.location.href = "user_role.html?userId="+userId;
	});
	
	//查看用户详情
	$(document).on("click",".user-mag",function(){
		window.location.href = "usermag.html?userId="+$(this).attr("idd");
	});
	
	//获取用户列表
	function userList(typeArr){
		var typeStr = typeArr.join(",");
		$.ajax({
			async:false,
			type:"get",
			url:"../rest/user/getAllLoginInfo",
			data:{type:typeStr,pageIndex:pageIndex},
			dataType:"json",
			success:function(data){
				data = data.login;
				var html = "";
				for(var i=0;i<data.length;i++){
					var user = data[i];
					if(i==0){
						pageIndex = user.pageIndex;
						pageCount = user.pageCount;
					}
					var type;
					//端
					var extremity;
					if(user.type==1 || user.type==2){
						type="手机端用户";
						extremity = "手机端";
					}else if(user.type==3 || user.type==4){
						type="服务端管理员";
						extremity = "PC端";
					}else{
						type="未知";
					}
					
					html += "<ul class='head clearfix' id='cont'>";
					//html += "<li class='time '>"+type+"</li>";
					//html += "<li class='kind '>"+user.id+"</li>";
					html += "<li class='area '>"+user.account+"</li>";
					html += "<li class='jurisdiction'>"+user.role+"</li>";
					html += "<li class='area ' style='margin-left: 5px'>"+user.loginIP+"</li>";
					html += "<li class='singed'>"+user.loginTime+"</li>";
					html += "<li class='img '>"+user.browser+"</li>";
					html += "<li class='img '>"+user.location+"</li>";
					if(user.type!=1&&user.type!=2){
						html += "<li class='opt' style='margin-left: 30px'><a class='blue change-role' idd='"+user.id+"' javascript:void(0); typee='"+user.type
							+"' style='background-color:#969696;cursor:pointer;'>更改角色</a><a class='gray user-mag' idd='"+user.id+"' style='background-color:#4b8df8'>修改</a></li>";
					}else{
						html += "<li class='opt' style='margin-left: 30px; width: 200px'><a class='blue change-role' idd='"+user.id+"' typee='"+user.type+"' style='cursor:pointer;'>更改角色</a><a class='gray user-mag' idd='"
							+user.id+"' style='background-color:#4b8df8'>修改</a><a class='gray user-del' style='margin-left: 10px;cursor:pointer;' idd='"+user.id+"' user-name='"+user.name+"'>删除</a></li>";
					}
					html += "</ul>";
				}
				$(".con").empty();
				$(".con").append(html);
				showPage(pageIndex,pageCount,startOrEnd);
			}
		});
	}
	//根据当前页面和总页码展示页码。
	//startOrEnd:load first last start end
	function showPage(pageIndex,pageCount,startOrEnd){
		if(startOrEnd=="load"){
			start = 1;
			end = 6;
			if(pageCount<6){
				end = pageCount;
			}
		}else if(startOrEnd=="first"){
			start = 1;
			end = 6;
			if(pageCount<6){
				end = pageCount;
			}
		}else if(startOrEnd=="last"){
			end = pageCount;
			start = ((pageCount-5)<1)?1:pageCount-5;
		}else if(startOrEnd=="start"){
			if(pageIndex!=1){
				end = pageIndex;
				start = ((pageIndex-5)<1)?1:pageIndex-5;
			}
		}else if(startOrEnd=="end"){
			if(pageIndex!=pageCount){
				start = pageIndex;
				end = (start+5)>pageCount?pageCount:start+5;
			}
		}else if(startOrEnd==null){
			
		}
		var html = "<div class='record first'><<</div>";
//		//在页码展示的开始页码，结束页码
		for(var i=start;i<=end;i++){
			if(i!=pageIndex){
				html += "<div class='record'>"+i+"</div>";
			}else{
				html += "<div class='record on'>"+i+"</div>";
			}
		}
		html += "<div class='record last'>>></div>";
		$(".page-1").empty();
		$(".page-1").append(html);
	}
});
$(function(){
	$("body").on("click", ".user-del", function(){
		var c = confirm("确认删除该用户？");
		if (c) {			
			var id = $(this).attr("idd");
			var name = $(this).attr("user-name");
			$.ajax({
				url: "useropera/delUser",
				dataType: "json",
				type: "post",
				data: {	id: id,
						name: name	},
				success: function(data){
					if (data == "1") {
						alert("该用户是默认处理人,不能删除!");
					} else if (data == "2") {
						alert("该用户尚有问题/任务未关闭,不能删除!");
					} else if (data == "3") {
						alert("删除成功!");
						location.reload();
					}
				}				
			});
		}
	});	
});