function Display(LoginEmail, UserName, ID) {
    alert(LoginEmail);
}
Date.prototype.Format = function(fmt)   
{ //author: meizz   
  var o = {   
    "M+" : this.getMonth()+1,                 //月份   
    "d+" : this.getDate(),                    //日   
    "h+" : this.getHours(),                   //小时   
    "m+" : this.getMinutes(),                 //分   
    "s+" : this.getSeconds(),                 //秒   
    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
    "S"  : this.getMilliseconds()             //毫秒   
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
}  
$(document).ready(function(){
	var taskId = getQueryString("taskId");
	getLog(taskId);
	$.ajax({
		async:false,
		type:"post",
		url:"question/questiondetail",
		dataType:"json",
		data:{taskId:taskId},
		success:function(data){
			var html = "";
			for(var i in data){
				var kv = data[i];
				var kvvalue = kv.value;
				if(kv.value==null||kv.value==""){
					kvvalue = "无";
				}
				if(i==0){
					html += "<li class='detaillist'><div class='hd'>任务类型</div><div class='bd'>"+kv.taskMetaName+"</div></li>";
					html += "<li class='detaillist'><div class='hd'>"+kv.attributeName+"</div><div class='bd'>"+kvvalue+"</div></li>";
				}else{
					if (kv.type == 120 || kv.type == 121) {
						kvvalue = kvvalue.split(",")[1];
						html += "<li class='detaillist'><div class='hd'>"+kv.attributeName+"</div><div class='bd'>"+kvvalue+"</div></li>";
					} else if (kv.type == 95) {
						if (kvvalue == "无") {
							html += "<li class='detaillist imggg'><div class='hd'>"+kv.attributeName+"</div><div class='bd'>无</div></li>";	
						} else {
							//获取相对路径
							kvvalue = kv.attache;
							if (!kvvalue) {
								html += "<li class='detaillist'><div class='hd'>"+kv.attributeName+"</div><div class='bd'>附件正在同步中...请稍后查看</div></li>";
							}else {	
								kvvalue = kvvalue.substring(kvvalue.lastIndexOf("WebRoot")+9, kvvalue.length);
								kvvalue = kvvalue.replace("\\\\", "/");
								html += "<li class='detaillist imggg'><div class='hd'>"+kv.attributeName+"</div><div class='bd bigimg-div'><i class='del-i'></i><img src='"+kvvalue+"'/></div><div class='bd'><img width='100px' height='100px' class='smallimg' src='"+kvvalue+"'/></div></li>";
							}
						}
					} else if (kv.type == 96) {
						if (kvvalue == "无") {
							html += "<li class='detaillist'><div class='hd'>"+kv.attributeName+"</div><div class='bd'>无</div></li>";	
						} else {
							//获取相对路径
							kvvalue = kv.attache;
							if (!kvvalue) {
								html += "<li class='detaillist'><div class='hd'>"+kv.attributeName+"</div><div class='bd'>附件正在同步中...请稍后查看</div></li>";
							}else {								
								kvvalue = kvvalue.substring(kvvalue.lastIndexOf("WebRoot")+9, kvvalue.length);
								kvvalue = kvvalue.replace("\\\\", "/");
								var prefix = kvvalue.substr(kvvalue.length-3, 3);
								if(prefix == "amr" || prefix=="AMR"){
									kvvalue = kvvalue.replace("amr", "mp3");
								}
								html += "<li class='detaillist'><div class='hd'>"+kv.attributeName+"</div><div class='bd'>"
									+"<audio controls='controls'><source src='"+kvvalue+"'/></audio></div></li>";
							}
							//base64流编码
							/*
							kvvalue = kvvalue.substring(kvvalue.lastIndexOf("."), kvvalue.length-1);
							var tempSrc = "audio/mpeg";
							if (kvvalue == ".wav") {
								tempSrc = "audio/x-wav";
							} else if(kvvalue == ".wma") {
								tempSrc = "audio/x-ms-wma";
							}
							html += "<li class='detaillist'><div class='hd'>"+kv.attributeName+"</div><div class='bd'>"
								+"<audio controls='controls'><source type='"+tempSrc+"' src='data:"+tempSrc+";base64,"+kv.attache+"'/></audio></div></li>";
							*/
						}
					} else if (kv.type == 97) {
						if (kvvalue == "无") {
							html += "<li class='detaillist'><div class='hd'>"+kv.attributeName+"</div><div class='bd'>无</div></li>";	
						} else {
							//获取相对路径
							kvvalue = kv.attache;
							if (!kvvalue) {
								html += "<li class='detaillist'><div class='hd'>"+kv.attributeName+"</div><div class='bd'>附件正在同步中...请稍后查看</div></li>";
							}else {	
								kvvalue = kvvalue.substring(kvvalue.lastIndexOf("WebRoot")+9, kvvalue.length);
								kvvalue = kvvalue.replace("\\\\", "/");
								html += "<li class='detaillist'><div class='hd'>"+kv.attributeName+"</div><div class='bd'>"
									+"<video controls='controls' width='500px' src='"+kvvalue+"'></video></div></li>";
							}
							//base64流编码
							/*
							kvvalue = kvvalue.substring(kvvalue.lastIndexOf("."), kvvalue.length-1);
							var tempSrc = "video/mp4";
							if (kvvalue == ".avi") {
								tempSrc = "video/x-msvideo";
							} else if(kvvalue == ".mov") {
								tempSrc = "video/quicktime";
							} else if(kvvalue == ".asf") {
								tempSrc = "video/x-ms-asf";
							} else if(kvvalue == ".3gp") {
								tempSrc = "video/3gpp";
							} else if(kvvalue == ".mpg") {
								tempSrc = "video/mpeg";
							}
							html += "<li class='detaillist'><div class='hd'>"+kv.attributeName+"</div><div class='bd'>"
								+"<video controls='controls' width='500px' src='data:"+tempSrc+";base64,"+kv.attache+"'></div></li>";
							*/
						}
					} else if (kv.type == 91 && kvvalue != "无") {
						var ids = kvvalue.split(">");
						var cos = eval(kv.selectColumns);
						var showv = "";
						$.each(ids, function(ind, inf){
							$.each(cos, function(index, info){
								if (inf == info.key) {
									showv += ">"+info.name;
									return false;
								}
							});
						});
						html += "<li class='detaillist'><div class='hd'>"+kv.attributeName+"</div><div class='bd'>"+showv.substring(1, showv.length)+"</div></li>";
					} else {						
						html += "<li class='detaillist'><div class='hd'>"+kv.attributeName+"</div><div class='bd'>"+kvvalue+"</div></li>";
					}
				}
			}
			html += "<li class='detaillist'>";
			html += "<div class='hd'></div>";
			html += "<div class='bd detailbtn'><a class='btnr'></a></div>";
			html += "</li>";
			$("#taskdetail").empty();
			$("#taskdetail").append(html);
		}
	});
	
	$.ajax({
		async:false,
		type:"post",
		url:"question/taskdetail",
		dataType:"json",
		data:{taskId:taskId},
		success:function(data){
			if (data.length > 0) {
				$(".bbbb").show();
				var version = "";
				$.each(data, function(index, info){
					if (version != info.version) {						
						version = info.version;
						$(".bbbb").after("<li class='detaillist'><div class='hd'>更新时间</div><div class='bd'>"+new Date(version).Format("yyyy-MM-dd hh:mm:ss")+"</div></li>");
					}
					var kvvalue = info.value;
					if(info.value==null||info.value==""){
						kvvalue = "无";
					}
					if (info.type == 120 || info.type == 121) {
						kvvalue = kvvalue.split(",")[1];
						$(".bbbb").after("<li class='detaillist'><div class='hd'>"+info.attributeName+"</div><div class='bd'>"+kvvalue+"</div></li>");
					} else if (info.type == 95) {
						if (kvvalue == "无") {
							$(".bbbb").after("<li class='detaillist imggg'><div class='hd'>"+info.attributeName+"</div><div class='bd'>无</div></li>");	
						} else {
//							$(".bbbb").after("<li class='detaillist imggg'><div class='hd'>"+info.attributeName+"</div><div class='bd bigimg-div'><i class='del-i'></i><img src='data:image/*;base64,"+info.attache+"'/></div><div class='bd'><img width='100px' height='100px' class='smallimg' src='data:image/*;base64,"+info.attache+"'/></div></li>");
							//获取相对路径
							kvvalue = info.attache;
							if (!kvvalue) {
								$(".bbbb").after("<li class='detaillist'><div class='hd'>"+info.attributeName+"</div><div class='bd'>附件正在同步中...请稍后查看</div></li>");
							}else {	
								kvvalue = kvvalue.substring(kvvalue.lastIndexOf("WebRoot")+9, kvvalue.length);
								kvvalue = kvvalue.replace("\\\\", "/");
								$(".bbbb").after("<li class='detaillist imggg'><div class='hd'>"+info.attributeName+"</div><div class='bd bigimg-div'><i class='del-i'></i><img src='"+kvvalue+"'/></div><div class='bd'><img width='100px' height='100px' class='smallimg' src='"+kvvalue+"'/></div></li>");
							}
						}
					} else if (info.type == 96) {
						if (kvvalue == "无") {
							$(".bbbb").after("<li class='detaillist'><div class='hd'>"+info.attributeName+"</div><div class='bd'>无</div></li>");	
						} else {
							//获取相对路径
							kvvalue = info.attache;
							if (!kvvalue) {
								$(".bbbb").after("<li class='detaillist'><div class='hd'>"+info.attributeName+"</div><div class='bd'>附件正在同步中...请稍后查看</div></li>");
							}else {	
								kvvalue = kvvalue.substring(kvvalue.lastIndexOf("WebRoot")+9, kvvalue.length);
								kvvalue = kvvalue.replace("\\\\", "/");
								$(".bbbb").after("<li class='detaillist'><div class='hd'>"+info.attributeName+"</div><div class='bd'>"
									+"<audio controls='controls'><source src='"+kvvalue+"'/></audio></div></li>");
							}
							/*
							kvvalue = kvvalue.substring(kvvalue.lastIndexOf("."), kvvalue.length-1);
							var tempSrc = "audio/mpeg";
							if (kvvalue == ".wav") {
								tempSrc = "audio/x-wav";
							} else if(kvvalue == ".wma") {
								tempSrc = "audio/x-ms-wma";
							}
							$(".bbbb").after("<li class='detaillist'><div class='hd'>"+info.attributeName+"</div><div class='bd'>"
							+"<audio controls='controls'><source type='"+tempSrc+"' src='data:"+tempSrc+";base64,"+info.attache+"'/></audio></div></li>");
							*/
						}
					} else if (info.type == 97) {
						if (kvvalue == "无") {
							$(".bbbb").after("<li class='detaillist'><div class='hd'>"+info.attributeName+"</div><div class='bd'>无</div></li>");	
						} else {
							//获取相对路径
							kvvalue = info.attache;
							if (!kvvalue) {
								$(".bbbb").after("<li class='detaillist'><div class='hd'>"+info.attributeName+"</div><div class='bd'>附件正在同步中...请稍后查看</div></li>");
							}else {	
								kvvalue = kvvalue.substring(kvvalue.lastIndexOf("WebRoot")+9, kvvalue.length);
								kvvalue = kvvalue.replace("\\\\", "/");
								html += "<li class='detaillist'><div class='hd'>"+info.attributeName+"</div><div class='bd'>"
									+"<video controls='controls' width='500px' src='"+kvvalue+"'></video></div></li>";
							}
							/*
							kvvalue = kvvalue.substring(kvvalue.lastIndexOf("."), kvvalue.length-1);
							var tempSrc = "video/mp4";
							if (kvvalue == ".avi") {
								tempSrc = "video/x-msvideo";
							} else if(kvvalue == ".mov") {
								tempSrc = "video/quicktime";
							} else if(kvvalue == ".asf") {
								tempSrc = "video/x-ms-asf";
							} else if(kvvalue == ".3gp") {
								tempSrc = "video/3gpp";
							} else if(kvvalue == ".mpg") {
								tempSrc = "video/mpeg";
							}
							$(".bbbb").after("<li class='detaillist'><div class='hd'>"+info.attributeName+"</div><div class='bd'>"
							+"<video controls='controls' width='500px' src='data:"+tempSrc+";base64,"+info.attache+"'></div></li>");
							*/
						}
					} else {						
						$(".bbbb").after("<li class='detaillist'><div class='hd'>"+info.attributeName+"</div><div class='bd'>"+kvvalue+"</div></li>");
					}
				});
			}
		}
	});
	
	/**
	 * 点击图片,显示放大的图片
	 */
	$(document).on("click", ".imggg .smallimg", function(){
		
		$(this).parent().prev().show();
		$(this).parent().hide();
	});
	
	$(document).on("click", ".imggg .del-i", function(){
		$(this).parent().hide();
		$(this).parent().next().show();
		
		
	});
	
	//获取这个任务对应的日志信息
	function getLog(taskId){
		$.ajax({
			type:"post",
			url:"question/log",
			dataType:"json",
			data:{taskId:taskId},
			success:function(data){
				//$(".steplist").empty();
				var reportor;
				var dealer;
				var prevStep = "";
				for(var i=0;i<data.length;i++){
					var step = data[i];
					if (step.desc != prevStep) {						
						var html = "";
						if(i==0){
							reportor = step.reportor;
							dealer = step.dealer;
							reportor = reportor.substring(reportor.lastIndexOf(",")+1,reportor.length);
							if (dealer) {								
								dealer = dealer.substring(dealer.lastIndexOf(",")+1,dealer.length);
							} else {
								dealer = "无";
							}
						}
						var stepName = step.desc;
						stepName = stepName.replace(/A/," <label style='color:#e22122;font-weight:700;'>"+reportor+"</label> ");
						stepName = stepName.replace(/B/," <label style='color:#e22122;font-weight:700;'>"+dealer+"</label> ");
						if(i!=data.length-1){
							html += "<li class='clearfix'>"
								html += "<div class='stepicon fL'><i class=''></i></div>"
									html += "<div class='stepdetail fL'>"
										html += "<p>"+stepName+"</p>"
										html += "<p>"+step.time+"</p>"
										html += "</div>"
											html += "</li>"
						}else{
							html += "<li class='clearfix'>"
								html += "<div class='stepicon fL'><i class='current'></i></div>"
									html += "<div class='stepdetail fL'>"
										html += "<p>"+stepName+"</p>"
										html += "<p>"+step.time+"</p>"
										html += "</div>"
											html += "</li>"
						}
						$("#aaaa").after(html);
						prevStep = step.desc;
					}
				}
			}
		});
	}
});