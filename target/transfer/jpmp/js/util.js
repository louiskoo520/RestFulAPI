//获取url参数的值：name是参数名
function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) {
	    return (r[2]);
	}
	return null;
}
//获取图片的大小
function getFileSize(o) {
    x = window.XMLHttpRequest ? new window.XMLHttpRequest : new ActiveXObject("MSxml2.XMLHTTP");
    x.open("HEAD", o.src, false);
    x.send();
    return x.getResponseHeader("Content-Length")/1024;
}
/**
 * 根据时间字符串获取毫秒数
 * 2014-09-10 00:00:00
 */
function getTimes(timeStr){
	var year = timeStr.substring(0,4);
	var month = timeStr.substring(5,7);
	var date = timeStr.substring(8,10);
	var hour = timeStr.substring(11,13);
	var min = timeStr.substring(14,16);
	var sec = timeStr.substring(17,19);
	var tempDate = new Date();
	if(month.substring(0,1)==0){
		month = month.substring(1,2);
	}
	if(date.substring(0,1)==0){
		date = date.substring(1,2);
	}
	if(hour.substring(0,1)==0){
		hour = hour.substring(1,2);
	}
	if(min.substring(0,1)==0){
		min = min.substring(1,2);
	}
	if(sec.substring(0,1)==0){
		sec = sec.substring(1,2);
	}
	tempDate.setFullYear(year, parseInt(month)-1, date);
	tempDate.setHours(hour, min, sec, 0);
	return tempDate.getTime();
}
//获取部门列表
function getDeptList(){
	deptList = [];
	$.ajax({
		async:false,
		type:"post",
		url:"user/deptList",
		success:function(data){
			deptList = data;
		}
	});
	return deptList;
}

$(function(){
	//加载top
	$.ajax({
		async: false,
		url:"./sys/top.html",
		dataType:"html",
		success:function(data){
			$(".top").html(data);
		}
	});
	//加载left
	$.ajax({
		async: false,
		url:"./sys/left.html",
		dataType:"html",
		success:function(data){
			$(".container .nav").html(data);
		}
	});
	//加载bottom
	$.ajax({
		async: false,
		url:"./sys/bottom.html",
		dataType:"html",
		success:function(data){
			$("body").append(data);
		}
	});	
	$.ajax({
		async: false,
		url:"user/getSessionUser",
		dataType:"json",
		success:function(data){
			if(!data.name){
				alert("你还未登录或者登录已经断开");
				window.location.href = "login.html";
			}else{
				sessionUserInfo = data;
				var t = setTimeout(function(){
					if($(".top .userInfo").length!=0){
						$(".top .userInfo").html(data.name);
						clearTimeout(t);
					}
				},100);
			}
		}
	});
	var qs = getQueryString("qs");
	if (qs) {
		var goback = $(".goback a").attr("href");
		$(".goback a").attr("href", goback+"?qs="+qs);
		if ($(".header a").eq(qs).html()) {
			$(".title em").html($(".header a").eq(qs).html());
			$(".road a:last").html($(".header a").eq(qs).html());
			$(".header a").each(function(){
				$(this).removeClass("current");
			});
			$(".header a").eq(qs).addClass("current");
		} else {			
			$(".title em").html(eqs[qs]);
			$(".road a:last").html(eqs[qs]);
		}
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
	
	$('.moremenu').hover(function(){
		$(this).addClass('hovermenu');
	},function(){
		$(this).removeClass('hovermenu');
	});
	
	$(".uploadimglist li").hover(function(){
		$(this).children(".imgoperate").stop().fadeIn();
	},function(){
		$(this).children(".imgoperate").stop().fadeOut();
	});
})