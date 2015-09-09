// JavaScript Document
//定义全局变量
var ERROR = "error";
var SUCCESS = "success";

//问题区域
var questionArea = ["崇安区","惠山区","北塘区","南长区","滨湖区","锡山区","新区"];
//获取url参数的值：name是参数名
function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) {
	    return (r[2]);
	}
	return null;
}
//获取登录用户信息
function getLoginUser(){
	var userInfo = null;
	$.ajax({
		async:false,
		type:"post",
		url:"user/getSessionUser",
		dataType:"json",
		success:function(data){
			if(data!=null){
				userInfo = data;
			}else{
				
			}
		}
	});
	return userInfo;
}
//获取图片的大小
function getFileSize(o) {
    x = window.XMLHttpRequest ? new window.XMLHttpRequest : new ActiveXObject("MSxml2.XMLHTTP");
    x.open("HEAD", o.src, false);
    x.send();
    return x.getResponseHeader("Content-Length")/1024;
  }
$(function(){
	//加载top
	$.ajax({
		url:"./sys/top.html",
		dataType:"html",
		success:function(data){
			$(".top").html(data);
		}
	});
	//加载top
	$.ajax({
		url:"./sys/left.html",
		dataType:"html",
		success:function(data){	
//			$(".left").html(data);
		}
	});
	//left点击链接
	$("a").click(function(){
		if($(this).html()=="用户管理<i></i>"){
			window.location.href = "./userlist.html";
		}
	});
	$("a").click(function(){
		if($(this).html()=="实时监控<i></i>"){
			window.location.href = "./conn_monitor.html";
		}
	});
	$("a").click(function(){
		if($(this).html()=="APP管理<i></i>"){
			window.location.href = "./applist.html";
		}
	});
	$("a").click(function(){
		if($(this).html()=="类别添加<i></i>"){
			window.location.href = "addkind.html";
		}
	});
	$("a").click(function(){
		if($(this).html()=="问题管理<i></i>"){
			window.location.href = "./questionlist.html";
		}
	});
	$("a").click(function(){
		if($(this).html()=="任务管理<i></i>"){
			window.location.href = "./tasklist.html";
		}
	});
	$("a").click(function(){
		if($(this).html()=="公告管理<i></i>"){
			window.location.href = "./notice_add.html";
		}
	});
	$("a").click(function(){
		if($(this).html()=="首页<i></i>"){
			window.location.href = "./index.html";
		}
	});
	$("a").click(function(){
		alert($(this).html()=="数据统计<i></i>");
		if($(this).html()=="数据统计<i></i>"){
			window.location.href = "./date.html";
		}
	});
	
	//检测用户是否断线
	var userInfo = getLoginUser();
	if(userInfo==null){
		alert("你还未登录或者登录已经断开");
		window.location.href = "login.html";
	}else{
		var t = setTimeout(function(){
			if($(".top .userInfo").length!=0){
				$(".top .userInfo").html(userInfo.name);
				clearTimeout(t);
			}
		},1000);
	}
	
	//用户退出
	$(".top .quit").parent().click(function(){
		$.ajax({
			type:"post",
			url:"user/logout",
			dataType:"html",
			success:function(){
				window.location.href="login.html";
			}
		});
	});
})



























