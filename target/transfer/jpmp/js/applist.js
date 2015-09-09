$(document).ready(function(){
	var code = null;
	var pageIndex;
	var pageCount;
	var code = "";
	var pageIndex2 = 1;
	var startOrEnd = "load";
	var start,end;
	//页面刚载入
	getList(code,pageIndex2);
	showPage(pageIndex,pageCount,startOrEnd);
	showAppNum();
	//点击下一页下一个首页尾页
	$(document).on("click",".page-1 div",function(){
		var inner = $(this).html();
		if(isNaN(inner)){
			if(inner=="&lt;&lt;"){
				pageIndex2 = 1;
				startOrEnd = "first";
			}else if(inner=="&gt;&gt;"){
				pageIndex2 = pageCount;
				startOrEnd = "last";
			}
		}else{
			pageIndex2 = inner;
			startOrEnd = null;
			if($(this).prev().attr("class").indexOf("first")>0){
				startOrEnd = "start";
			}else if($(this).next().attr("class").indexOf("last")>0){
				startOrEnd = "end";
			}
		}
		getList(code,pageIndex2);
		showPage(pageIndex,pageCount,startOrEnd);
		showAppNum();
	});
	//点击checkbox
	$(document).on("click",".son",function(){
		showAppNum();
		if(!$(this).prop("checked")){
			$(".father").prop("checked",false);
		}else{
			var allSonSelected = true;
			$(".son").each(function(){
				if(!$(this).prop("checked")){
					allSonSelected = false;
				}
			});
			if(allSonSelected){
				$(".father").prop("checked",true);
			}else{
				$(".father").prop("checked",false);
			}
		}
	});
	$(document).on("click",".father",function(){
		if($(this).prop("checked")){
			$(".son").each(function(){
				$(this).prop("checked",true);
			});
		}else{
			$(".son").each(function(){
				$(this).prop("checked",false);
			});
		}
		showAppNum();
	});
	//删除
	$(".delete-selected").click(function(){
		var idArr = new Array();
		$(".son").each(function(){
			if($(this).prop("checked")){
				idArr.push($(this).val());
			}
		});
		if(idArr.length==0){
			alert("未选择应用！");
			return;
		}
		if(window.confirm("确认删除？")){
			var idString = idArr.join(",");
			del(idString);
			return true;
		}else{
			return false;
		}
	});
	$(document).on("click",".del",function(){
		if(window.confirm("确认删除？")){
			var id = $(this).attr("idd");
			del(id);
			return true;
		}else{
			return false;
		}
	});
	//查看APP详情
	$(document).on("click",".app-logo,.app-name",function(){
		var appId = $(this).attr("idd");
		window.location.href = "appdetail.html?appId="+appId;
	});
	function del(idString){
		$.ajax({
			type:"post",
			url:"app/del",
			data:{idStr:idString},
			dataType:"html",
			success:function(data){
				if(data=="success"){
					getList(code,1);
					showPage(pageIndex,pageCount,"load");
					showAppNum();
				}else{
					alert(data);
				}
			}
		});
	}
	//展示已选择应用，共多少应用
	function showAppNum(){
		var appCount = 0;
		var appSelectedCount = 0;
		$(".son").each(function(){
			if($(this).prop("checked")){
				appSelectedCount ++;
			}
			appCount ++;
		});
		$(".selectedNO").html(appSelectedCount);
		$(".allNO").html(appCount);
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
	//获取app的JSON列表
	function getList(code,pageIndex2){
		$.ajax({
			async:false,
			type:"post",
			url:"app/appList",
			dataType:"json",
			data:{code:code,pageIndex:pageIndex2},
			success:function(data){
				var html = "";
				for(var i in data){
					var app = data[i];
					if(i==0){
						pageIndex = app.pageIndex;
						pageCount = app.pageCount;
					}
					html += "<div class='info-list'>";
					html += "<input type='checkbox' class='son' value='"+app.id+"'/>";
					html += "<img src='."+app.img+"' height='70px' width='70px' idd='"+app.id+"' class='app-logo'/>";
					html += "<ul>";
					html += "<li class='li-top'>";
					html += "<span class='app-name' style='cursor:pointer;' idd='"+app.id+"'>"+app.name+"</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
					html += "<span>类型：<i class='app-type'>"+app.typename+"</i></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
					html += "<span>最近更新：<i class='latest'>"+app.latest+"</i></span>";
					html += "</li>";
					html += "<li class='li-btm'>";
					html += "<p>"+app.appDesc+"</p>";
					html += "<html class='info-btn'>";
					html += "<a class='update'>更新</a>";
					html += "<a class='del' idd='"+app.id+"'>删除</a>";
					html +="</div>";
					html += "</li>";
					html += "</ul>";
					html += "</div>";
				}
				$(".body-cont #according").empty();
				$(".body-cont #according").append(html);
			}
		});
	}
	$(document).on("click",".body-cont .update",function(){
		window.location.href = "updateapp.html?appId="+$(this).next().attr("idd");
	});
	//获取图片的流，展示在前台
	function getFileStream(fileName){
		var stream;
		$.ajax({
			async:false,
			type:"post",
			url:"app/getFileStream",
			dataType:"text",
			data:{fileName:fileName},
			success:function(data){
				alert(data);
				stream = data;
			}
		});
		return stream;
	}
});





























