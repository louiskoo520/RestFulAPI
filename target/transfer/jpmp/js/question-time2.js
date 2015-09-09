	var pRange = "";					//时间范围
	var pStartTime = "";				//开始时间
	var pEndTime = "";				//结束时间
	var pTypeArray = new Array();		//问题类型
	var pXAxis = [];					//x轴
	var pSeries = [];
$(document).ready(function(){
	//页面载入加载客户端用户列表
	$.ajax({
		type:"post",
		url:"useropera/getusers",
		dataType:"json",
		success:function(data){
			$(".time-reportor").append("<option value='-1'>全部</option>");
			$(".area-reportor").append("<option value='-1'>全部</option>");
			for(var i in data){
				$(".time-reportor").append("<option value='"+data[i].id+"'>"+data[i].name+"</option>");
				$(".area-reportor").append("<option value='"+data[i].id+"'>"+data[i].name+"</option>");
			}
		}
	});
	/**
	 * 点击tab
	 */
	$(".header a").click(function(){
  		$(this).siblings().removeClass("current");
  		$(this).addClass("current");
  		for(var i=0;i<=$(".header a").length;i++){
  			if(i!=$(this).index()){
  				$(".main-body .question").eq(i).hide();
  			}
  		}    		
  		$(".main-body .question").eq($(this).index()).show();
		state = $(this).html();
		$(".qs2").html(state);
	});
	
	/*
	 * 点击事件,如果checkbox选中,则日期框清空
	 */
		$(".timetable input[name='time-interval']").each(function(){
			$(this).click(function(){
				var index = $(".timetable input[name='time-interval']").index(this);
				$(".timetable input[name='time-interval']:not("+index+")").removeAttr("checked");
				$(this).attr("checked",true);
				if($(this).prop("checked")){
					$(".timetable .start").val("");
					$(".timetable .end").val("");
				}
			});
		});
		
		/*
		 * 点击事件,checkbox选中,取消
		 */
			$(".timetable .q-type").each(function(){
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
	$(".timetable .start,.timetable .end").focus(function(){
		$(".timetable input[name='time-interval']").attr("checked",false);
	});
	
	/*
	 * 加载事件,加载当天数据
	 */
	pRange = $(".timetable input[name='time-interval'][checked='checked']").val();
	//获取问题类型
	$(".timetable .q-type").each(function(){
		if($(this).prop("checked")){
			pTypeArray.push($(this).val());
		}
	});
	//获取当前用户
	$.ajax({
		async:false,
		type:"post",
		url:"question/questioTimeChartData2",
		data:{type:"checkbox",range:pRange,qType:pTypeArray.join(","),userInfo:"-1,全部"},
		dataType:"json",
		success:function(data){
			pXAxis = [];
			if(pRange=="当天"){
				var nowHour = new Date().getHours();
				for(var i = 1;i<=parseInt(nowHour)+1;i++){
					pXAxis.push(i+":00");
				}
			}
			pSeries = data;
		}
	});
	getECharts(pXAxis,pSeries);
	 /*
	 * 获取当前各个状态的问题数量
	 */
	/*
	 * 点击事件,根据条件生成报表
	 */
	$(".timetable button[type='submit']").click(function(){
		var cbkLength = $(".timetable input[name='time-interval'][checked='checked']").length;
		pRange = $(".timetable input[name='time-interval'][checked='checked']").val();
		pStartTime = $(".timetable .start").val();
		pEndTime = $(".timetable .end").val();
		pTypeArray = [];
		var userId = $(".time-reportor").val();
		var userName;
		$(".time-reportor option").each(function(){
			if($(this).attr("value")==userId){
				userName = $(this).html();
			}
		});
		//获取问题类型
		$(".timetable .q-type").each(function(){
			if($(this).prop("checked")){
				pTypeArray.push($(this).val());
			}
		});
		 if(pTypeArray.length==0){
				alert("请选择问题类型");
				return;
		}
		 if(cbkLength > 0 && (pStartTime == "" && pEndTime == "")){
			 $.ajax({
					async:false,
					type:"post",
					url:"question/questioTimeChartData2",
					data:{type:"checkbox",range:pRange,qType:pTypeArray.join(","),userInfo:(userId+","+userName)},
					dataType:"json",
					success:function(data){
						pXAxis = [];
						if(pRange=="当天"){
							var nowHour = new Date().getHours();
							for(var i = 1;i<=parseInt(nowHour)+1;i++){
								pXAxis.push(i+":00");
							}
						}else if(pRange=="本周"){
							var nowWeek = new Date().getDay();
							if(nowWeek==0){
								nowWeek = 7;
							}
							for(var i = 1;i<=nowWeek;i++){
								if(i==1){
									pXAxis.push("周一");
								}else if(i==2){
									pXAxis.push("周二");
								}else if(i==3){
									pXAxis.push("周三");
								}else if(i==4){
									pXAxis.push("周四");
								}else if(i==5){
									pXAxis.push("周五");
								}else if(i==6){
									pXAxis.push("周六");
								}else if(i==7){
									pXAxis.push("周日");
								}
							}
						}else if(pRange=="本月"){
							var nowMonth = parseInt(new Date().getMonth())+1;
							var nowDate = new Date().getDate();
							for(var i=1;i<=nowDate;i++){
								pXAxis.push(nowMonth+"/"+i);
							}
						}
						pSeries = data;
					}
				});
				getECharts(pXAxis,pSeries);
				
				 /*
				 * 获取当前各个状态的问题数量
				 */
		 }else if(cbkLength == 0 && (pStartTime != "" && pEndTime != "")){
			 pStartTime += " 00:00:00";
			 pEndTime += " 59:59:59";
				$.ajax({
					async:false,
					type:"post",
					url:"question/questioTimeChartData2",
					data:{type:"button",start:pStartTime,end:pEndTime,qType:pTypeArray.join(","),userInfo:(userId+","+userName)},
					dataType:"json",
					success:function(data){
						pXAxis = data[0];
						pSeries = data[1];
					}
				});
				getECharts(pXAxis,pSeries);
				
				 /*
				 * 获取当前各个状态的问题数量
				 */
		 }else{
			 
			 alert("请选择正确的时间条件");
				return false;
		 }
	});	
	
	
	
	function getECharts(xAxis,series){
		//渲染数量
		for(var i in series){
			var datas = series[i].data;
			var count = 0;
			for(var j in datas){
				count += datas[j]
			}
			var htrht = $("#greger .gg");
			for(var j=0;j<htrht.length;j++){
				if(j==i){
					$(htrht[j]).html(count);
				}
			}
		}
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
	        function (ec) {
	            // 基于准备好的dom，初始化echarts图表
	            var myChart = ec.init(document.getElementById('questiontime')); 
	            var option = {
					    title : {
					        text: '',
					        subtext: '数量(个)'
					    },
					    tooltip : {
					        trigger: 'axis'
					    },
					    legend: {
					        data:['未分配','通知中','已通知','已接收','已完成','已关闭']
					    },
					    calculable : true,
					    xAxis : [
					        {
					            type : 'category',
					            boundaryGap : false,
//					            data : ['周一','周二','周三','周四','周五','周六','周日']
					        	data : xAxis
					        }
					    ],
					    yAxis : [
					        {
					            type : 'value',
					            axisLabel : {
					                formatter: '{value}'
					            }
					        }
					    ],
					    series : series
					};
	            // 为echarts对象加载数据 
	            myChart.setOption(option); 
	        }
	    );
	}
	
	
});	
	
