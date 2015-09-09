jQuery(document).ready(function() {
	//页面刚载入
	var x = getQueryString("x");
	var y = getQueryString("y");
	var id = getQueryString("id");
	var prevMark = null;
	//过5s获取一个最新的地理位置
	setInterval(function(){
		$.ajax({
			async:false,
			type:"post",
			url:"user/getLocation",
			data:{id:id},
			dataType:"html",
			success:function(data){
				x = data.split(",")[0];
				y = data.split(",")[1];
				if(x=="outline"&&y=="outline"){
					alert("该用户已掉线");
				}else{
					if(prevMark!=null){
						map.removeOverlay(prevMark);
					}
					var point = new BMap.Point(y, x);
					var mark = new BMap.Marker(point);
					prevMark = mark;
					map.addOverlay(mark);              // 将标注添加到地图中
					var label = new BMap.Label("lon:"+y+" lat:"+x,{offset:new BMap.Size(20,-10)});
					mark.setLabel(label);
					mark.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
					addEventListener(mark);
				}
			}
		});
	},5000);
	//地图
	var map = new BMap.Map("allmap");
	remarkMap(y,x);
	var gc;
	function remarkMap(xx,yy){
		var point = new BMap.Point(yy, xx);
		map.centerAndZoom(point, 10);
		var marker = new BMap.Marker(point);  // 创建标注
		prevMark = marker;
		map.addOverlay(marker);              // 将标注添加到地图中
		gc = new BMap.Geocoder();
		var label = new BMap.Label("lon:"+yy+" lat:"+xx,{offset:new BMap.Size(20,-10)});
		marker.setLabel(label);
		marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
		map.addControl(new BMap.NavigationControl());  //添加默认缩放平移控件
		map.addControl(new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_SMALL}));  //右上角，仅包含平移和缩放按钮
		map.addControl(new BMap.NavigationControl({anchor: BMAP_ANCHOR_BOTTOM_LEFT, type: BMAP_NAVIGATION_CONTROL_PAN}));  //左下角，仅包含平移按钮
		map.addControl(new BMap.NavigationControl({anchor: BMAP_ANCHOR_BOTTOM_RIGHT, type: BMAP_NAVIGATION_CONTROL_ZOOM}));  //右下角，仅包含缩放按钮
		map.enableScrollWheelZoom();    //启用滚轮放大缩小，默认禁用
		map.enableContinuousZoom();    //启用地图惯性拖拽，默认禁用
		addEventListener(marker);
	}
	
	//获取url参数的值：name是参数名
	function getQueryString(name) {
	    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	    var r = window.location.search.substr(1).match(reg);
	    if (r != null) {
	        return (r[2]);
	    }
	    return null;
	}
	
	//给标注物添加监听事件
	function addEventListener(currentMarker){
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
					data:{userId:id},
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
					+locationInfo+"<br/><a href='data3.html?userId="+id
					+"'>查看GPS轨迹</a><a style='margin-left: 20px' href='#' onclick=$('#msg_content').show();$('#msg_content').css('left',"+e.clientX+");$('#msg_content').css('top',"+e.clientY+");$('.send-btn').attr('userId',"+id+");>发送即时消息</a>";
				var infoWindow = new BMap.InfoWindow(windowInfo, opts);  // 创建信息窗口对象
				map.openInfoWindow(infoWindow,currentMarker.getPosition()); //开启信息窗口
			});
		});
	}
});