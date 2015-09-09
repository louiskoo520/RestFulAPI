//展示在线用户和离线用户的信息
	var map;
	var map2;
	var map3;
	
	var polyline = null;
	var markerStart = null;
	var markerEnd = null;
$(document).ready(function(){
	
	//获取所有用户
	$.ajax({
		url: "useropera/getusers",
		dataType: "json",
		type: "post",
		success: function(data){
			cur_users = data;
			$.each(data, function(index, info){
				$("#outline").append("<li id='"+info.id+"'><div style='width: 80px'>"+info.name
						+"</div><a href='javascript:;' idd='"+info.id+"' class='gpss'>查看GPS轨迹</a></li>");
			});
		}
	});
	
	map = new BMap.Map("allMap");
	map2 = new BMap.Map("allMap2");
	map3 = new BMap.Map("allMap3");
	
	map.enableScrollWheelZoom();
	map2.enableScrollWheelZoom();
	map3.enableScrollWheelZoom();
	var point = new BMap.Point(113.438687, 23.175127);
	map.centerAndZoom(point, 5);
	map2.centerAndZoom(point, 5);
	map3.centerAndZoom(point, 5);
	
	setting(map);
	setting(map2);
	setting(map3);
	
	var gc = new BMap.Geocoder();
	//用户ID集合
	var userIdArr = [];
	//用户ID和marker对象
	var useridMarker = {};
	loadInfo();
	//每隔20秒获取在离线用户
	setInterval(function(){
		loadInfo();
	},10000);
	function loadInfo(){
		$.ajax({
			async:false,
			type:"post",
			url:"user/getOOUser",
			data:{userIds:userIdArr.join(",")},
			dataType:"json",
			success:function(data){
				var start = new Date().getTime();
				console.log("data的长度:"+data.length);
				userIdArr = [];
				var info = "";
				
				$("#outline li").each(function(){
					$(this).show();
				});
				$("#online").empty();
				
				for(var i in data){
					var user = data[i];
					var userId = user.userId;
					var lon = user.lon;
					var lat = user.lat;
					var online = user.online;
					var username = user.name;
					userIdArr.push(userId);
//					if(user.online==1){
//						info += user.userId+" 在线  ";
//					}else{
//						info += user.userId+" 离线  ";
//					}
					if(useridMarker[userId]==null){
//						console.log(userId+":"+online+":"+lat+":"+lon);
						var icon;
						//离线
						if(online==0){
							icon = new BMap.Icon('images/outline.png', new BMap.Size(24, 24), {//是引用图标的名字以及大小，注意大小要一样
								anchor: new BMap.Size(10, 30)//这句表示图片相对于所加的点的位置
							});
						}else{
							$("#online").append("<li id='"+userId+"'><div title='"+username+"' style='width: 50px; overflow: hidden; margin-right: 0'>"
									+username+"</div>"+"<a id='"+userId+"' class='sendmmm' username='"+username+"'>发送消息</a><a"
									+" idd="+userId+" x="+lon+" y="+lat+" href='javascript:;' class='realLocation'>实时位置</a></li>");	
							$("#outline>li#"+userId).hide();
							
							icon = new BMap.Icon('images/online.png', new BMap.Size(24, 24), {//是引用图标的名字以及大小，注意大小要一样
								anchor: new BMap.Size(10, 30)//这句表示图片相对于所加的点的位置
							});
						}
						var marker = new BMap.Marker(new BMap.Point(lon,lat), {
							icon: icon
						});
						//将用户ID和marker绑定
						marker.userId = userId;
						map.addOverlay(marker);
						useridMarker[userId] = marker;
						
						//给标注物添加监听事件
						addEventListener(marker,map);
						
					}else{
//						console.log(userId+":"+online+":"+lat+":"+lon);
						var marker = useridMarker[userId];
						var icon = marker.getIcon();
						if(online==0){
							if((icon.imageUrl).indexOf("outLine")<0){
								marker.setIcon(new BMap.Icon('images/outline.png', new BMap.Size(24, 24), {//是引用图标的名字以及大小，注意大小要一样
									anchor: new BMap.Size(10, 30)//这句表示图片相对于所加的点的位置
								}));
							}
						}else{
							$("#online").append("<li id='"+userId+"'><div title='"+username+"' style='width: 50px; overflow: hidden; margin-right: 0'>"
									+username+"</div>"+"<a id='"+userId+"' class='sendmmm' username='"+username+"'>发送消息</a><a"
									+" idd="+userId+" x="+lon+" y="+lat+" href='javascript:;' class='realLocation'>实时位置</a></li>");	
							$("#outline>li#"+userId).hide();
							//更换标注物的经纬度和icon
							if((icon.imageUrl).indexOf("outLine")>=0){
								marker.setIcon(new BMap.Icon('images/online.png', new BMap.Size(24, 24), {//是引用图标的名字以及大小，注意大小要一样
									anchor: new BMap.Size(10, 30)//这句表示图片相对于所加的点的位置
								}));
							}
							var position = marker.getPosition();
							if(!(position.lng==lon&&position.lat==lat)){
								marker.setPosition(new BMap.Point(lon,lat));
							}
						}
						useridMarker[userId] = marker;
						//给标注物添加监听事件
						addEventListener(marker,map);
					}
				}
			}
		});
	}
	
	//给标注物添加监听事件
	function addEventListener(currentMarker,map){
		var currentUserId = currentMarker.userId;
		currentMarker.addEventListener("mouseover",function(e){
			var locationInfo = "";
			var nameInfo = "";//姓名
			var deptInfo = "";//部门名
			var telInfo = "";//联系方式				
			var nofinish = "";//未完成的任务
			var reportToday = "";//今天上报的问题
			var windowInfo = "";
			gc.getLocation(currentMarker.getPosition(),function(rs){
				//获取用户名称，部门，联系方式
				$.ajax({
					async:false,
					url:"user/getUserInfo",
					data:{userId:currentUserId},
					dataTyepe:"json",
					success:function(data){
						nameInfo = "用户姓名："+data.name;
						deptInfo = "用户部门："+data.department;
						telInfo = "联系方式："+data.tel;
						nofinish = "未完成任务数："+data.noFinishCount;
						reportToday = "今天上报问题数："+data.todayCount;
					}
				});
				
				//获取位置信息
				var addComp = rs.addressComponents;
				locationInfo = "当前位置："+addComp.province+","+addComp.city+","+addComp.district + ", " + addComp.street + ", " + addComp.streetNumber;
				var opts = {
					width : 200,     // 信息窗口宽度
//					height: 60,     // 信息窗口高度
					title : "窗口信息" , // 信息窗口标题
					enableMessage:false,//设置允许信息窗发送短息
				}
				windowInfo = nameInfo+"<br/>"+deptInfo+"<br/>"+telInfo+"<br/>"+nofinish+"<br/>"+reportToday+"<br/>"
					+locationInfo+"<br/><a  onclick='showGps("+currentUserId+")' idd='"+currentUserId+"' class='gpss'"
					+">查看GPS轨迹</a><a style='margin-left: 20px' href='#' onclick=$('#msg_content').show();$('#msg_content').css('left',"+e.clientX+");$('#msg_content').css('top',"+e.clientY+");$('.send-btn').attr('userId',"+currentUserId+");>发送即时消息</a>";
				var infoWindow = new BMap.InfoWindow(windowInfo, opts);  // 创建信息窗口对象
				map.openInfoWindow(infoWindow,currentMarker.getPosition()); //开启信息窗口
			});
		});
	}
	
	
	//发送信息
	$(".send-btn").click(function(){
		var msgContent = $(".msf-content-text").val().trim();
		var msgUserId = $(this).attr("userId");
		//将此公告保存到数据库中
		$.ajax({
			type:"POST",
			url:"sys/systemMsgAdd",
			data:{msgContent:msgContent,msgUserId:msgUserId},
			dataType:"html",
			success:function(data){
				if(data==SUCCESS){
					//如果保存成功，则开始发送消息
					alert("发送成功");
				}else{
					alert("系统出错");
				}
			}
		});
//		alert(msgContent);
//		alert(msgUserId);
	});
	$(".close-btn").click(function(){
		$("#msg_content").hide();
	});
	
	var pagei;
	$("body").on("click", ".sendmmm", function(){
		var msgUserId = $(this).attr("id");
		var msgUserName = $(this).attr("username");
		pagei = $.layer({
			type: 1,   //0-4的选择,
		    title: "发送即时消息——"+msgUserName,
		    border: [0],
		    closeBtn: [0],
		    shadeClose: true,
		    area: ['360px', '190px'],
		    page: {
		        html: '<\div id="initlayer" style="width:360px; height:150px; background-color:#ffffff;">'
		        +'<\div><label>请输入消息内容：</label><br/><textarea style="width:330px;height:80px;resize: none;margin:10px 15px" id="usernum" type="text"></textarea>'
		        +'<button id="ok" userId="'+msgUserId+'" type="button" style="float:right; margin-right: 15px">发送</button>'
		        +'<button id="cancel" type="button" style="float:right; margin-right: 5px">取消</button><\/div><\/div>'
		    }
		});
	});
	$("body").on("click", "#ok", function(){
		var msgContent = $(this).prev().val().trim();
		var msgUserId = $(this).attr("userId");
		//将此公告保存到数据库中
		$.ajax({
			type:"POST",
			url:"sys/systemMsgAdd",
			data:{msgContent:msgContent,msgUserId:msgUserId},
			dataType:"html",
			success:function(data){
				if(data==SUCCESS){
					//如果保存成功，则开始发送消息
					alert("发送成功");
					layer.close(pagei);
				}else{
					alert("系统出错");
				}
			}
		});	
	});
	$("body").on("click", "#cancel", function(){
		layer.close(pagei);
	});
	
	
	var prevMark = null;
	/**
	 * 点击实时位置跳转
	 * */
	$(document).on("click",".realLocation",function(){
		
		$(".hr input").hide();
		$(".hr select").hide();
		$(".hr label").hide();
		$(".goback").show();
		
		$("#allMap3").css("display","inline-block");
		$("#allMap").css("display","none");
		$("#allMap2").css("display","none");
		
		var x = $(this).attr("x");
		var y = $(this).attr("y");
		var id = $(this).attr("idd");
		
		if(prevMark!=null){
			map3.removeOverlay(prevMark);
		}
		remarkMap(id,y,x,map3);
		
	});
	
	
	function remarkMap(id,xx,yy,map){
		var point = new BMap.Point(yy, xx);
		map.centerAndZoom(point, 10);
		var marker = new BMap.Marker(point);  // 创建标注
		marker.userId = id;
		prevMark = marker;
		map.addOverlay(marker);              // 将标注添加到地图中
		gc = new BMap.Geocoder();
		var label = new BMap.Label("lon:"+yy+" lat:"+xx,{offset:new BMap.Size(20,-10)});
		marker.setLabel(label);
		marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
		addEventListener(marker,map);
	}
	
	function setting(map){
		map.addControl(new BMap.NavigationControl());  //添加默认缩放平移控件
		map.addControl(new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_SMALL}));  //右上角，仅包含平移和缩放按钮
		map.addControl(new BMap.NavigationControl({anchor: BMAP_ANCHOR_BOTTOM_LEFT, type: BMAP_NAVIGATION_CONTROL_PAN}));  //左下角，仅包含平移按钮
		map.addControl(new BMap.NavigationControl({anchor: BMAP_ANCHOR_BOTTOM_RIGHT, type: BMAP_NAVIGATION_CONTROL_ZOOM}));  //右下角，仅包含缩放按钮
		map.enableScrollWheelZoom();    //启用滚轮放大缩小，默认禁用
		map.enableContinuousZoom();    //启用地图惯性拖拽，默认禁用
	}
	
	//过5s获取一个最新的地理位置
	setInterval(function(){
		if(prevMark!=null){
			$.ajax({
				async:false,
				type:"post",
				url:"user/getLocation",
				data:{id:prevMark.userId},
				dataType:"html",
				success:function(data){
					var x = data.split(",")[0];
					var y = data.split(",")[1];
					if(x=="outline"&&y=="outline"){
						alert("该用户已掉线");
					}else{
						map3.removeOverlay(prevMark);
						var point = new BMap.Point(y, x);
						var mark = new BMap.Marker(point);
						mark.userId = prevMark.userId;
						prevMark = mark;
						map3.addOverlay(mark);              // 将标注添加到地图中
						var label = new BMap.Label("lon:"+y+" lat:"+x,{offset:new BMap.Size(20,-10)});
						mark.setLabel(label);
						mark.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
						addEventListener(mark,map3);
					}
				}
			});
		}
		
	},5000);
	
	
	
	
	$(document).on("click",".gpss",function(){
		if(polyline!=null){
			map2.removeOverlay(polyline);
		}
		if(markerStart!=null){
			map2.removeOverlay(markerStart);
		}
		if(markerEnd!=null){
			map2.removeOverlay(markerEnd);
		}
		
		$(".hr input").show();
		$(".hr select").show();
		$(".hr label").show();
		$(".goback").show();
		
		$("#allMap3").css("display","none");
		$("#allMap").css("display","none");
		$("#allMap2").css("display","inline-block");
		
		var point = new BMap.Point(113.438687, 23.175127);
//		map2.centerAndZoom(point, 12);
		map2.centerAndZoom("广州", 13);
		var userId = $(this).attr("idd");
		//查询所有客户端用户
		$.ajax({
			async:false,
			type:"post",
			url:"user/clientUserList",
			dataType:"json",
			success:function(data){
				var html = "";
				for(var i in data){
					if(data[i].id==userId){
						html += "<option value='"+data[i].id+"' selected='selected'>"+data[i].name+"</option>";
					}else{
						html += "<option value='"+data[i].id+"'>"+data[i].name+"</option>";
					}
				}
				$(".user-select").append(html);
			}
		});
	});
	
	$(".user-btn").click(function(){
		if(polyline!=null){
			map2.removeOverlay(polyline);
		}
		if(markerStart!=null){
			map2.removeOverlay(markerStart);
		}
		if(markerEnd!=null){
			map2.removeOverlay(markerEnd);
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
				map2.addOverlay(markerStart);
				var iconE = new BMap.Icon('images/end.png', new BMap.Size(24, 24), {//是引用图标的名字以及大小，注意大小要一样
					anchor: new BMap.Size(12,15)//这句表示图片相对于所加的点的位置
				});
				markerEnd = new BMap.Marker(pointE, {
					icon: iconE
				});
				map2.addOverlay(markerEnd);
				polyline = new BMap.Polyline(
					pointArr,
					{strokeColor:"red",//设置颜色 
					strokeWeight:2, //宽度
					strokeOpacity:0.0}
				);//透明度
				map2.addOverlay(polyline);
				map2.centerAndZoom(pointS, 15);
			}
		});
	});
	
	$(".goback").click(function(){
		$("#allMap3").css("display","none");
		$("#allMap").css("display","inline-block");
		$("#allMap2").css("display","none");
		
		$(".hr input").hide();
		$(".hr select").hide();
		$(".hr label").hide();
		$(this).hide();
	});
});

/**
 * 查看GPS轨迹
 * */
function showGps(userId){

	if(polyline!=null){
		map2.removeOverlay(polyline);
	}
	if(markerStart!=null){
		map2.removeOverlay(markerStart);
	}
	if(markerEnd!=null){
		map2.removeOverlay(markerEnd);
	}
	
	$(".hr input").show();
	$(".hr select").show();
	$(".hr label").show();
	$(".goback").show();
	
	$("#allMap3").css("display","none");
	$("#allMap").css("display","none");
	$("#allMap2").css("display","inline-block");
	
	var point = new BMap.Point(113.438687, 23.175127);
//	map2.centerAndZoom(point, 12);
	map2.centerAndZoom("广州", 13);
	//查询所有客户端用户
	$.ajax({
		async:false,
		type:"post",
		url:"user/clientUserList",
		dataType:"json",
		success:function(data){
			var html = "";
			for(var i in data){
				if(data[i].id==userId){
					html += "<option value='"+data[i].id+"' selected='selected'>"+data[i].name+"</option>";
				}else{
					html += "<option value='"+data[i].id+"'>"+data[i].name+"</option>";
				}
			}
			$(".user-select").append(html);
		}
	});

}























