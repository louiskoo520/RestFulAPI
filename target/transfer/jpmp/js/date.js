	var range = "";					//时间范围
	var startTime = "";				//开始时间
	var endTime = "";				//结束时间
	var userId = "";    			//用户id
	var pTypeArr = new Array();		//问题类型
	var xAxis = [];					//x轴
	var series = [];					
$(document).ready(function(){
	/*
	 * 加载事件,显示用户列表
	 */
	$.ajax({
			async:false,
			type:"post",
			url:'user/getAllUser',
			dataType:'json',
			success:function(data){
				var html = "";
				$(".infoExchange .userid").empty();
				for(var i in data){
					html += "<option value='" + data[i].id+ "'>" + data[i].name +"</option>";
				}
				$(".infoExchange .userid").html(html);
			}
		});
	/*
	 * 点击事件,如果checkbox选中,则日期框清空
	 */
		$(".infoExchange input[name='time-interval']").each(function(){
			$(this).click(function(){
				var index = $(".infoExchange input[name='time-interval']").index(this);
				$(".infoExchange input[name='time-interval']:not("+index+")").removeAttr("checked");
				$(this).attr("checked",true);
				if($(this).prop("checked")){
					$(".infoExchange .start").val("");
					$(".infoExchange .end").val("");
				}
			});
		});
		
		/*
		 * 点击事件,checkbox选中,取消
		 */
			$(".infoExchange .q-type").each(function(){
				$(this).click(function(){
					if($(this).prop("checked")){
						$(this).attr("checked",true);
					}else{
						$(this).attr("checked",false);
					}
				});
			});
			
	/*
	 * 判断事件,如果日期框获取光标,则checkbox不选中
	 */
	$(".infoExchange .start,.infoExchange .end").focus(function(){
		$(".infoExchange input[name='time-interval']").attr("checked",false);
	});
	
	/*
	 * 加载事件,加载当天数据
	 */
	range = $(".infoExchange input[name='time-interval'][checked='checked']").val();
	//获取问题类型
	$(".infoExchange .q-type").each(function(){
		if($(this).prop("checked")){
			pTypeArr.push($(this).val());
		}
	});
	
	
	//获取当前用户
	userId = $(".infoExchange .userid").val();
	$.ajax({
		async:false,
		type:"post",
		url:"static/getStatisticData",
		data:{type:"checkbox",range:range,qType:pTypeArr.join(","),userId:userId},
		dataType:"json",
		success:function(data){
			if(range=="当天"){
				var nowHour = new Date().getHours();
				for(var i = 1;i<=parseInt(nowHour)+1;i++){
					xAxis.push(i+":00");
				}
			}
			series = data;
		}
	});
	getECharts(xAxis,series);
	 /*
	 * 获取当前各个状态的问题数量
	 */
	$.ajax({
		async:false,
		type:"post",
		url:"static/getStatisticCountByRange",
		data:{range:range,qType:pTypeArr.join(","),userId:userId},
		dataType:"json",
		success:function(data){
			for(var i in data){
				$(".infoExchange .totalques li").each(function(){
					var index = $(".infoExchange .totalques li").index(this);
					if(index == i){
						$(this).find("span:nth-child(2)").text(data[i].num);
					}
				});
			}
			pTypeArr = [];
		}
	});
	
	
	/*
	 * 点击事件,根据条件生成报表
	 */
	$(".infoExchange button[type='submit']").click(function(){
		var radioLength = $(".infoExchange input[name='time-interval'][checked='checked']").length;
		range = $(".infoExchange input[name='time-interval'][checked='checked']").val();
		 startTime = $(".infoExchange .start").val();
		 endTime = $(".infoExchange .end").val();
		 userId = $(".infoExchange .userid").val();
		 pTypeArr = [];
		//获取问题类型
		$(".infoExchange .q-type").each(function(){
			if($(this).prop("checked")){
				pTypeArr.push($(this).val());
			}
		});
		 if(pTypeArr.length==0){
				alert("请选择问题类型");
				return;
		}
		
			
		if(radioLength > 0 && (startTime == "" && endTime == "")){
			$.ajax({
				async:false,
				type:"post",
				url:"static/getStatisticData",
				data:{type:"checkbox",range:range,qType:pTypeArr.join(","),userId:userId},
				dataType:"json",
				success:function(data){
					xAxis = [];
					if(range=="当天"){
						var nowHour = new Date().getHours();
						for(var i = 1;i<=parseInt(nowHour)+1;i++){
							xAxis.push(i+":00");
						}
					}else if(range=="本周"){
						var nowWeek = new Date().getDay();
						if(nowWeek==0){
							nowWeek = 7;
						}
						for(var i = 1;i<=nowWeek;i++){
							if(i==1){
								xAxis.push("周一");
							}else if(i==2){
								xAxis.push("周二");
							}else if(i==3){
								xAxis.push("周三");
							}else if(i==4){
								xAxis.push("周四");
							}else if(i==5){
								xAxis.push("周五");
							}else if(i==6){
								xAxis.push("周六");
							}else if(i==7){
								xAxis.push("周日");
							}
						}
					}else if(range=="本月"){
						var nowMonth = parseInt(new Date().getMonth())+1;
						var nowDate = new Date().getDate();
						for(var i=1;i<=nowDate;i++){
							xAxis.push(nowMonth+"/"+i);
						}
					}
					series = data;
				}
			});
			getECharts(xAxis,series);
			
			 /*
			 * 获取当前各个状态的问题数量
			 */
			$.ajax({
				async:false,
				type:"post",
				url:"static/getStatisticCountByRange",
				data:{range:range,qType:pTypeArr.join(","),userId:userId},
				dataType:"json",
				success:function(data){
					for(var i in data){
						$(".infoExchange .totalques li").each(function(){
							var index = $(".infoExchange .totalques li").index(this);
							if(index == i){
								$(this).find("span:nth-child(2)").text(data[i].num);
							}
						});
					}
					pTypeArr = [];
				}
			});
			
		}else if(radioLength == 0 && (startTime != "" && endTime != "")){
			startTime += " 00:00:00";
			endTime += " 00:00:00";
			$.ajax({
				async:false,
				type:"post",
				url:"static/getStatisticData",
				data:{type:"button",start:startTime,end:endTime,qType:pTypeArr.join(","), userId:userId},
				dataType:"json",
				success:function(data){
					xAxis = data[0];
					series = data[1];
				}
			});
			getECharts(xAxis,series);
			 /*
			 * 获取当前各个状态的问题数量
			 */
			$.ajax({
				async:false,
				type:"post",
				url:"static/getStatisticCount",
				data:{start:startTime,end:endTime,qType:pTypeArr.join(","), userId:userId},
				dataType:"json",
				success:function(data){
					for(var i in data){
						$(".infoExchange .totalques li").each(function(){
							var index = $(".infoExchange .totalques li").index(this);
							if(index == i){
								$(this).find("span:nth-child(2)").text(data[i].num);
							}
						});
					}
					pTypeArr = [];
				}
			});
			
		}else{
			alert("请选择正确的时间条件");
			return false;
		}
		
		
	});
	 
	
	
	/*
	 * 报表引入echarts事件
	 */
	function getECharts(xAxis,series){
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
				
				var myChart = ec.init(document.getElementById('infoExchange'));
				var option = {
						title: {
							text : '信息交互统计报表',
							subtext : '单位(个)'
						},
						 tooltip : {
						        trigger: 'axis'
						    },
						legend: {
							data:['未分配','通知中','已通知','已接收','已完成','已关闭']
						},
						calculable : true,
						xAxis:{
							type:'category',
							boundaryGap: false,
							//data: ['周一','周二','周三','周四','周五','周六','周日']
							data : xAxis
						},
						yAxis:{
							type: 'value',
							axisLabel: {
								formatter: '{value}'
							}
						},
						series: series
				};
				myChart.setOption(option);
			}
		);
	}

});

