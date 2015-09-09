$(document).ready(function(){
	//展示在线用户和离线用户的信息
	var map = new BMap.Map("allMap");
	var point = new BMap.Point(113.438687, 23.175127);
	map.centerAndZoom(point, 15);
	map.enableScrollWheelZoom();
	//获取当前用户
	var currentUserId = getQueryString("userId");
	//刚载入页面，查询所有客户端用户
	$.ajax({
		async:false,
		type:"post",
		url:"user/clientUserList",
		dataType:"json",
		success:function(data){
			var html = "";
			for(var i in data){
				if(data[i].id==currentUserId){
					html += "<option value='"+data[i].id+"' selected='selected'>"+data[i].name+"</option>";
				}else{
					html += "<option value='"+data[i].id+"'>"+data[i].name+"</option>";
				}
			}
			$(".user-select").append(html);
		}
	});
	var polyline = null;
	var markerStart = null;
	var markerEnd = null;
	$(".user-btn").click(function(){
		if(polyline!=null){
			map.removeOverlay(polyline);
		}
		if(markerStart!=null){
			map.removeOverlay(markerStart);
		}
		if(markerEnd!=null){
			map.removeOverlay(markerEnd);
		}
		//获取用户ID
		var userId = $(".user-select").val();
		var start = $(".timestart").val();
		var end = $(".timeend").val();
		if(start==null||start==""||end==null||end==""){
			alert("开始和结束时间不能为空");
			return;
		}
		if(getTimes(start)>=getTimes(end)){
			alert("开始时间不能等于或晚于结束时间");
			return;
		}
		//获取这个用户在这个时间段的每个心跳包
		$.ajax({
			async:false,
			type:"post",
			url:"user/getHeartBeat",
			dataType:"json",
			data:{userId:userId,start:start,end:end},
			success:function(data){
				if (data.length == 0) {
					alert("当前时间段无GPS运动轨迹.");
					return;
				}
				var pointArr = [];
				for(var i in data){
					var usere = data[i];
					var point = new BMap.Point(usere.lon,usere.lat);
					pointArr.push(point);
				}
				var pointS = new BMap.Point(data[0].lon,data[0].lat);
				var pointE = new BMap.Point(data[data.length-1].lon,data[data.length-1].lat);
				var iconS = new BMap.Icon('images/start.png', new BMap.Size(24, 24), {//是引用图标的名字以及大小，注意大小要一样
					anchor: new BMap.Size(12,15)//这句表示图片相对于所加的点的位置
				});
				markerStart = new BMap.Marker(pointS, {
					icon: iconS
				});
				map.addOverlay(markerStart);
				var iconE = new BMap.Icon('images/end.png', new BMap.Size(24, 24), {//是引用图标的名字以及大小，注意大小要一样
					anchor: new BMap.Size(12,15)//这句表示图片相对于所加的点的位置
				});
				markerEnd = new BMap.Marker(pointE, {
					icon: iconE
				});
				map.addOverlay(markerEnd);
				polyline = new BMap.Polyline(
					pointArr,
					{strokeColor:"red",//设置颜色 
					strokeWeight:2, //宽度
					strokeOpacity:0.0}
				);//透明度
				map.addOverlay(polyline);
				map.centerAndZoom(pointS, 15);
			}
		});
	});
});