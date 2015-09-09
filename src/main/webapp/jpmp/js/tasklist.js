$(document).ready(function(){
	var startTime = "";
	var endTime = "";
	var keyWord = "";
	var area = "";
	var dealor = "";
	var pageIndex = 1;
	var pageCount;
	var startOrEnd = "load";
	//通过pageSize,count,pageIndex获取页码数和总页数
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
		var kaishi = $(".start").val();
		var jieshu = $(".end").val();
		var guanjianzi = $(".key").val();
		var quyu = $(".area").val();
		var shangbao = $(".dealor").val();
		questionlist(state);
		$(".start").val(kaishi);
		$(".end").val(jieshu);
		$(".key").val(guanjianzi);
		$(".area").val(quyu);
		$(".dealor").val(shangbao);
	});
	var state = "未同步,通知中,已通知";
	$(".header a").click(function(){
		startTime = "";
		endTime = "";
		keyWord = "";
		area = "";
		dealor = "";
		startOrEnd="load";
		pageIndex = 1;
		$(this).siblings().each(function(){
			$(this).removeClass("current");
		});
		$(this).addClass("current");
		state = $(this).html();
		$(".qs2").html(state);
		if(state=="处理中")
		{
			state = "已接收";
		}
		if(state=="未处理"){
			state="未同步,通知中,已通知";
		}
		if (state=="已完成") {
			state="已完成";
		}
		if (state=="已关闭") {
			state="已关闭";
		}
		questionlist(state);
	});
	questionlist(state);
	$(document).on("click",".searchss .botton",function(){
		var kaishi = $(".start").val();
		var jieshu = $(".end").val();
		var guanjianzi = $(".key").val();
		var quyu = $(".area").val();
		var chuli = $(".dealor").val();
		if(kaishi==""&&jieshu!=""){
			alert("开始时间不能为空");
			return;
		}
		if(kaishi!=""&&jieshu==""){
			alert("结束时间不能为空");
			return;
		}
		startTime = kaishi;
		endTime = jieshu;
		keyWord = guanjianzi;
		area = quyu;
		dealor = chuli;
		startOrEnd = "load";
		questionlist(state);
		$(".start").val(kaishi);
		$(".end").val(jieshu);
		$(".key").val(guanjianzi);
		$(".area").val(quyu);
		$(".dealor").val(chuli);
	});
	function questionlist(state){
		if(startTime!=""){
			startTime = startTime+" 00:00:00";
			endTime = endTime+" 23:59:59";
		}
		$.ajax({
			async:false,
			type:"post",
			url:"question/questionlist2",
			dataType:"json",
			data:{state:state,pageIndex:pageIndex,start:startTime,end:endTime,key:keyWord,area:area,dealor:dealor},
			success:function(data){
				var topHtml = "<div class='searchss'>"+
								"<div class='label timess'>时间：</div>"+
								"<div class='label label1'>从</div>"+
								"<div class='label label2'>"+
									"<input type='text' class='start' onclick='WdatePicker()'></input>"+
								"</div>"+
								"<div class='label label1'>至</div>"+
								"<div class='label label3'>"+
									"<input type='text' class='end' onclick='WdatePicker()'></input>"+
								"</div>"+
								"<div class='label keyss'>关键字：</div><div class='label label2'><input type='text' class='key'></input></div>"+
								"<div class='label label1'>区域：</div><div class='label label3'><input type='text' class='area'></input></div>"+
								"<div class='label label1'>处理人：</div><div class='label label3'><input type='text' class='dealor'></input></div>"+
								"<div class='label botton'>搜索</div></div>";
				$(".main-body").html(topHtml);
				if(data.length!=0){
					pageIndex = data[0].pageIndex;
					pageCount = data[0].pageCount;
				}
				
				var html = "";
				if(state=="未同步,通知中,已通知"){
					html += "<ul class='head clearfix aaaa'>";
					html += "<li class='time bodrb'>接收时间</li>";
					html += "<li class='kind bodrb'>类型</li>";
					html += "<li class='cont bodrb'>内容</li>";
					html += "<li class='up bodrb'>上报人</li>";
					html += "<li class='up bodrb'>处理人</li>";
					html += "<li class='opt'>操作</li>";
					html += "</ul>";
					$(".main-body").append(html); 
					$(".main-body").append("<ul class='aaaaa'></ul>");
					for(var i in data){
//					for(var i=data.length-1;i>=0;i--){
							html = "";
							var task = data[i];
							html = "";
							html += "<div class='con'>";
							html += "<ul class='head clearfix' id='cont'>";
							html += "<li class='time'><label></label>"+task.time+"</li>";
							html += "<li class='kind'>"+task.type+"</li>";
							html += "<li class='cont'>"+task.content+"</li>";
							html += "<li class='up'>"+task.reportor+"</li>";
							html += "<li class='up'>"+task.dealor+"</li>";
							html += "<li class='opt'><a href='#' class='gray showdetail' idd='"+task.qtId
							+"' qs='0'>查看</a><a href='#' class='gray switch' style='margin-left: 10px'>转发</a></li>";
							html += "</ul>";
							html += "</div>";
							$(".aaaaa").before(html); 
					}
				}else if(state=="已接收"){
					html += "<ul class='head clearfix aaaa'>";
					html += "<li class='time bodrb'>处理时间</li>";
					html += "<li class='kind bodrb'>类型</li>";
					html += "<li class='cont bodrb'>内容</li>";
					html += "<li class='up bodrb'>上报人</li>";
					html += "<li class='up bodrb'>处理人</li>";
					html += "<li class='opt'>操作</li>";
					html += "</ul>";
					$(".main-body").append(html); 
					$(".main-body").append("<ul class='aaaaa'></ul>");
					for(var i in data){
//						for(var i=data.length-1;i>=0;i--){
						html = "";
						var task = data[i];
						html = "";
						html += "<div class='con'>";
						html += "<ul class='head clearfix' id='cont'>";
						html += "<li class='time'><label></label>"+task.time+"</li>";
						html += "<li class='kind'>"+task.type+"</li>";
						html += "<li class='cont'>"+task.content+"</li>";
						html += "<li class='up'>"+task.reportor+"</li>";
						html += "<li class='up'>"+task.dealor+"</li>";
						html += "<li class='opt'><a href='#' class='gray showdetail' idd='"+task.qtId+"' qs='0'>查看</a></li>";
						html += "</ul>";
						html += "</div>";
						$(".aaaaa").before(html); 
				}
				}else if(state=="已完成"){
					html += "<ul class='head clearfix aaaa'>";
					html += "<li class='time bodrb'>关闭时间</li>";
					html += "<li class='kind bodrb'>类型</li>";
					html += "<li class='cont bodrb'>内容</li>";
					html += "<li class='up bodrb'>上报人</li>";
					html += "<li class='up bodrb'>处理人</li>";
					html += "<li class='opt'>操作</li>";
					html += "</ul>";
					$(".main-body").append(html); 
					$(".main-body").append("<ul class='aaaaa'></ul>");
					for(var i in data){
//						for(var i=data.length-1;i>=0;i--){
						html = "";
						var task = data[i];
						html = "";
						html += "<div class='con'>";
						html += "<ul class='head clearfix' id='cont'>";
						html += "<li class='time'><label></label>"+task.time+"</li>";
						html += "<li class='kind'>"+task.type+"</li>";
						html += "<li class='cont'>"+task.content+"</li>";
						html += "<li class='up'>"+task.reportor+"</li>";
						html += "<li class='up'>"+task.dealor+"</li>";
						html += "<li class='opt'><a href='#' class='gray showdetail' idd='"+task.qtId+"' qs='0'>查看</a></li>";
						html += "</ul>";
						html += "</div>";
						$(".aaaaa").before(html); 
				}
				}else if(state=="已关闭"){
					html += "<ul class='head clearfix aaaa'>";
					html += "<li class='time bodrb'>关闭时间</li>";
					html += "<li class='kind bodrb'>类型</li>";
					html += "<li class='cont bodrb'>内容</li>";
					html += "<li class='up bodrb'>上报人</li>";
					html += "<li class='up bodrb'>处理人</li>";
					html += "<li class='opt'>操作</li>";
					html += "</ul>";
					$(".main-body").append(html); 
					$(".main-body").append("<ul class='aaaaa'></ul>");
					for(var i in data){
//						for(var i=data.length-1;i>=0;i--){
						html = "";
						var task = data[i];
						html = "";
						html += "<div class='con'>";
						html += "<ul class='head clearfix' id='cont'>";
						html += "<li class='time'><label></label>"+task.time+"</li>";
						html += "<li class='kind'>"+task.type+"</li>";
						html += "<li class='cont'>"+task.content+"</li>";
						html += "<li class='up'>"+task.reportor+"</li>";
						html += "<li class='up'>"+task.dealor+"</li>";
						html += "<li class='opt'><a href='#' class='gray showdetail' idd='"+task.qtId+"' qs='0'>查看</a></li>";
						html += "</ul>";
						html += "</div>";
						$(".aaaaa").before(html); 
				}
				}
				showPage(pageIndex,pageCount,startOrEnd);
			}
		});
	}
	//点击查看查看任务详情
	$(document).on("click",".showdetail",function(){
		var taskId = $(this).attr("idd");
		var teq = $(this).attr("qs");
		window.location.href = "taskstep.html?taskId="+taskId+"&qs="+teq;
	});
	//根据当前页面和总页码展示页码。
	//startOrEnd:load first last start end
	function showPage(pageIndex,pageCount,startOrEnd){
		if(pageCount==null){
			pageCount = 0;
		}
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
	var pagei;
	$("body").on("click", "a.switch", function(){
		var uniteId = $(this).prev().attr("idd");
		var handlers = "";
		$.ajax({
			async: false,
			url: "taskinf/getHandlers",
			dataType: "json",
			typs: "post",
			data: {id: uniteId},
			success: function(data){
				$.each(data, function(index, info){
					handlers += "<option value='"+info.id+"'>"+info.name+"</option>";
				});
			}
		});
		pagei = $.layer({
			type: 1,   //0-4的选择,
		    title: "设置转发",
		    border: [0],
		    closeBtn: [0],
		    shadeClose: true,
		    area: ['360px', '150px'],
		    page: {
		        html: '<\div id="initlayer" style="width:320px; height:100px; background-color:#ffffff; padding: 20px">'
		        +'<\div><label>请选择处理人：</label><select style="margin:10px 15px" id="usernum">'+handlers+'</select>'
		        +'<button id="ok" type="button" uniteId="'+uniteId+'" style="float:right; margin-right: 15px">确认</button>'
		        +'<button id="cancel" type="button" style="float:right; margin-right: 5px">取消</button><\/div><\/div>'
		    }
		});
	});
	$("body").on("click", "#ok", function(){
		var uniteId = $(this).attr("uniteId");
		var userId = $("#usernum").val();
		var userName = $("#usernum").find("option:selected").html();
		$.ajax({
			url: "taskinf/setHandler",
			dataType: "json",
			type: "post",
			data: {	id: uniteId,
					userId: userId,
					userName: userName	},
			success: function(data){
				location.reload();
			}
		});
	});
	$("body").on("click", "#cancel", function(){
		layer.close(pagei);
	});
});