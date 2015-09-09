jQuery(document).ready(function() {
	//地图初始化
    var bm = new BMap.Map("allmap");
	//页面刚载入
	var x = getQueryString("x");
	var y = getQueryString("y");
	var id = getQueryString("id");
	map(x, y);
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
					map(x, y);
				}
			}
		});
	},5000);
    var prevMark = null;
	function map(xx, yy){
	    //GPS坐标
	    var gpsPoint = new BMap.Point(yy,xx);
	    if(prevMark==null){
	    	bm.centerAndZoom(gpsPoint, 15);
	    }
	    bm.addControl(new BMap.NavigationControl());
//	    alert(bm.Rn);
//	    alert(bm.Qn.lat);
//	    alert(bm.Qn.lng);
	    
	    //坐标转换完之后的回调函数
	    translateCallback = function (point){
	        var marker = new BMap.Marker(point);
	        if(prevMark==null){
	        	prevMark = marker;
	        }else{
	        	bm.removeOverlay(prevMark);
//	        	var currentX = bm.Qn.lat;
//	        	var currentY = bm.Qn.lng;
	        	var currentRoom = bm.Rn;
	        	var gpsPoint2 = new BMap.Point(yy,xx);
	        	//bm.centerAndZoom(gpsPoint2, currentRoom);
	        	bm.setCenter(gpsPoint2);
	        }
	        bm.addOverlay(marker);
	        prevMark = marker;
	        marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
	        bm.addControl(new BMap.NavigationControl()); 
	        bm.addControl(new BMap.NavigationControl());    
	        bm.addControl(new BMap.ScaleControl());    
	        bm.addControl(new BMap.OverviewMapControl());    
	        bm.addControl(new BMap.MapTypeControl()); 
	        bm.enableScrollWheelZoom();    //启用滚轮放大缩小，默认禁用
	        bm.enableContinuousZoom();    //启用地图惯性拖拽，默认禁用   

	    }
	    
	    BMap.Convertor.translate(gpsPoint,0,translateCallback);     //真实经纬度转成百度坐标
	    // initiate layout and plugins
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
});