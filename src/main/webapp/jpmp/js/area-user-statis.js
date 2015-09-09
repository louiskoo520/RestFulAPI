var dateQuery = "";  		//时间点
var startTime = "";
var endTime = "";
var xAxisAU = ["越秀区","海珠区","荔湾区","天河区","白云区","黄埔区","花都区","番禺区","萝岗区","南沙区"];
var dataArr = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
$(document).ready(function(){
	
	dateQuery = getNowFormatDate();
	$(".areaUser .start").val(dateQuery.split(" ")[0]);
	//alert(dateQuery);
	startTime = dateQuery.split(" ")[0] + " 00:00:00";
	endTime = dateQuery.split(" ")[0] + " 04:00:00";
	
	//alert(endTime);
	$.ajax({
		async: false,
		type: "post",
		url: "static/getTraceByQuery",
		data:{startTime: startTime, endTime: endTime },
		dataType: "json",
		success: function(data){
			//$("#areaUser").empty();
			var length = data.length;
			if(length == 0){
				dataArr = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
				getChart();
			}else{
				var yuexiu = 0;
				var haizhu = 0;
				var liwan = 0;
				var tianhe = 0;
				var baiyun = 0;
				var huangpu = 0;
				var huadu = 0;
				var fanyu = 0;
				var luogang = 0;
				var nansha = 0;
				for(var i = 0; i< length;i++){
					//alert(data[i].id + "---" + data[i].lon + "---" +data[i].lat);
					var point = new BMap.Point(data[i].lon,data[i].lat);
					var geoc = new BMap.Geocoder(); 
					geoc.getLocation(point, function(rs){
						var addComp = rs.addressComponents;
						//alert(addComp.district);
						if(addComp.district == "越秀区"){
							yuexiu++;
							dataArr[0] = yuexiu;
						}
						if(addComp.district == "海珠区"){
							haizhu++;
							dataArr[1] = haizhu;
						}
						if(addComp.district == "荔湾区"){
							liwan++;
							dataArr[2] = liwan;
						}
						if(addComp.district == "天河区"){
							tianhe++;
							dataArr[3] = tianhe;
						}
						if(addComp.district == "白云区"){
							baiyun++;
							dataArr[4] = baiyun;
						}
						if(addComp.district == "黄埔区"){
							huangpu++;
							dataArr[5] = huangpu;
						}
						if(addComp.district == "花都区"){
							huadu++;
							dataArr[6] = huadu;
						}
						if(addComp.district == "番禺区"){
							fanyu++;
							dataArr[7] = fanyu;
						}
						if(addComp.district == "萝岗区"){
							luogang++;
							dataArr[8] = luogang;
						}
						if(addComp.district == "南沙区"){
							nansha++;
							dataArr[9] = nansha;
						}
					});
					
					if(i == length-1){
						$("#loading").show();
						//setTimeout(null,3000);
						setTimeout(function(){
							//alert(dataArr[3]);
							//配置文件引用路径
							require.config({
								paths:{
									'echarts' : './js/echarts',
									'echarts/chart/bar' : './js/echarts',
									'echarts/chart/line' : './js/echarts'
								}
							});
							//
							require(
								[
								 	'echarts',
								 	'echarts/chart/bar',	//按需加载所需图表
								 	'echarts/chart/line'
								 ],
								 function(ec){
									var myChart = ec.init(document.getElementById('areaUser'));
									var option = {
										    title : {
										        text: '区域用户在线情况',
										        subtext: ''
										    },
										    tooltip : {
										        trigger: 'axis'
										    },
										    legend: {
										        data:['在线人数']
										    },
										    calculable : true,
										    xAxis : [
										        {
										            type : 'category',
										            data : xAxisAU
										        }
										    ],
										    yAxis : [
										        {
										            type : 'value'
										        }
										    ],
										    series : [
										        {
										            name:'在线人数',
										            type:'bar',
										           // data:[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6]
										        	data:dataArr
										        }
										          
										    ]
										};
									myChart.setOption(option);
									$("#loading").hide();
								}
							);
						},3000);
						
					}
				}
			}
		}
	});

	/**
	 * 点击事件,按区域查询符合时间条件的在线用户数量
	 */
	$(".areaUser button[type='submit']").click(function(){
		var yuexiu = 0;
		var haizhu = 0;
		var liwan = 0;
		var tianhe = 0;
		var baiyun = 0;
		var huangpu = 0;
		var huadu = 0;
		var fanyu = 0;
		var luogang = 0;
		var nansha = 0;
		var timeQuery = "";
		//获取日期
		dateQuery = $(".areaUser .start").val();
		//获取时间区间
		$(".areaUser .q-type").each(function(){
			if($(this).prop("checked")){
				timeQuery = $(this).val();
			}
		});
		startTime =dateQuery + " " + timeQuery.split("-")[0];
		endTime =dateQuery + " " + timeQuery.split("-")[1];
		if(timeQuery == ""){
			alert("请选择时间点");
			return;
		}
	
		$.ajax({
			async: false,
			type: "post",
			url: "static/getTraceByQuery",
			data:{startTime: startTime, endTime: endTime },
			dataType: "json",
			success: function(data){
				//$("#areaUser").empty();
				var length = data.length;
				//alert(length);
				if(length == 0){
					dataArr = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
					getChart();
				}else{
					for(var i = 0; i< length;i++){

						//alert(data[i].id + "---" + data[i].lon + "---" +data[i].lat);
						var point = new BMap.Point(data[i].lon,data[i].lat);
						var geoc = new BMap.Geocoder(); 
						geoc.getLocation(point, function(rs){
							var addComp = rs.addressComponents;
							//alert(addComp.district);
							if(addComp.district == "越秀区"){
								yuexiu++;
								dataArr[0] = yuexiu;
							}
							if(addComp.district == "海珠区"){
								haizhu++;
								dataArr[1] = haizhu;
							}
							if(addComp.district == "荔湾区"){
								liwan++;
								dataArr[2] = liwan;
							}
							if(addComp.district == "天河区"){
								tianhe++;
								dataArr[3] = tianhe;
							}
							if(addComp.district == "白云区"){
								baiyun++;
								dataArr[4] = baiyun;
							}
							if(addComp.district == "黄埔区"){
								huangpu++;
								dataArr[5] = huangpu;
							}
							if(addComp.district == "花都区"){
								huadu++;
								dataArr[6] = huadu;
							}
							if(addComp.district == "番禺区"){
								fanyu++;
								dataArr[7] = fanyu;
							}
							if(addComp.district == "萝岗区"){
								luogang++;
								dataArr[8] = luogang;
							}
							if(addComp.district == "南沙区"){
								nansha++;
								dataArr[9] = nansha;
							}
					
							
						});
						//alert(length + "-----------")
						if(i == length-1){
							$("#loading").show();
							//setTimeout(null,3000);
							setTimeout(function(){
								//alert(dataArr[3]);
								//配置文件引用路径
								require.config({
									paths:{
										'echarts' : './js/echarts',
										'echarts/chart/bar' : './js/echarts',
										'echarts/chart/line' : './js/echarts'
									}
								});
								//
								require(
									[
									 	'echarts',
									 	'echarts/chart/bar',	//按需加载所需图表
									 	'echarts/chart/line'
									 ],
									 function(ec){
										var myChart = ec.init(document.getElementById('areaUser'));
										var option = {
											    title : {
											        text: '区域用户在线情况',
											        subtext: ''
											    },
											    tooltip : {
											        trigger: 'axis'
											    },
											    legend: {
											        data:['在线人数']
											    },
											    calculable : true,
											    xAxis : [
											        {
											            type : 'category',
											            data : xAxisAU
											        }
											    ],
											    yAxis : [
											        {
											            type : 'value'
											        }
											    ],
											    series : [
											        {
											            name:'在线人数',
											            type:'bar',
											           // data:[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6]
											        	data:dataArr
											        }
											          
											    ]
											};
										myChart.setOption(option);
										$("#loading").hide();
									}
								);
							},3000);
							
						}
					}
				}
			}
		});
	});
	
	function getChart(){
		//alert(dataArr[3]);
		//配置文件引用路径
		require.config({
			paths:{
				'echarts' : './js/echarts',
				'echarts/chart/bar' : './js/echarts',
				'echarts/chart/line' : './js/echarts'
			}
		});
		//
		require(
			[
			 	'echarts',
			 	'echarts/chart/bar',	//按需加载所需图表
			 	'echarts/chart/line'
			 ],
			 function(ec){
				var myChart = ec.init(document.getElementById('areaUser'));
				var option = {
					    title : {
					        text: '区域用户在线情况',
					        subtext: ''
					    },
					    tooltip : {
					        trigger: 'axis'
					    },
					    legend: {
					        data:['在线人数']
					    },
					    calculable : true,
					    xAxis : [
					        {
					            type : 'category',
					            data : xAxisAU
					        }
					    ],
					    yAxis : [
					        {
					            type : 'value'
					        }
					    ],
					    series : [
					        {
					            name:'在线人数',
					            type:'bar',
					           // data:[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6]
					        	data:dataArr
					        }
					          
					    ]
					};
				myChart.setOption(option);
			}
		);
	}
	
	function getNowFormatDate() {
	    var date = new Date();
	    var seperator1 = "-";
	    var seperator2 = ":";
	    var month = date.getMonth() + 1;
	    var strDate = date.getDate();
	    var strMinutes = date.getMinutes();
	    var strSec = date.getSeconds();
	    if (month >= 1 && month <= 9) {
	        month = "0" + month;
	    }
	    if (strDate >= 0 && strDate <= 9) {
	        strDate = "0" + strDate;
	    }
	    if(strMinutes >= 0 && strMinutes <=9){
	    	strMinutes = "0" + strMinutes;
	    	
	    }
	    if(strSec >= 0 && strSec <=9){
	    	strSec = "0" + strSec;
	    }
	    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
	            + " " + date.getHours() + seperator2 + strMinutes
	            + seperator2 + strSec;
	    return currentdate;
	} 
	
	
	
	
});