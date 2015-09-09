$(document).ready(function(){
	//获取未处理问题的数量
	$.ajax({
		async:false,
		type:"post",
		url:"question/questionlist",
		dataType:"json",
		data:{state:"未处理,通知中,已通知"},
		success:function(data){
			var count = data.length;
			$(".undeal").html(count);
		}
	});
	//获取已完成任务的数量
	$.ajax({
		async:false,
		type:"post",
		url:"question/questionlist",
		dataType:"json",
		data:{state:"已完成,已关闭"},
		success:function(data){
			var count = data.length;
			$(".finished").html(count);
		}
	});
	//获取最新APP数量（最近一个月的）
	$.ajax({
		async:false,
		type:"post",
		url:"app/latestAppCount",
		dataType:"html",
		success:function(data){
			var count = data.length;
			$(".latestApp").html(count);
		}
	});
	//获取在线用户数
	$.ajax({
		async:false,
		type:"post",
		url:"user/getLineUserNumber",
		dataType:"html",
		success:function(data){
			$(".zaixianyonghu").html(data);
		}
	});
	function time(){
		var today=new Date();
		var year = today.getFullYear();
		var month = today.getMonth()+1;
		var date = today.getDate();
		var h=today.getHours();
		var m=today.getMinutes();
		var s=today.getSeconds();
		if(month<10){
			month = "0"+month;
		}
		if(date<10){
			date = "0"+date;
		}
		if(h<10){
			h = "0"+h;
		}
		if(m<10){
			m = "0"+m;
		}
		if(s<10){
			s = "0"+s;
		}
		$(".nowtime").html(year+"年"+month+"月"+date+"日 "+h+"时"+m+"分"+s+"秒");
		setTimeout(time,1000);
	}
	time();
});