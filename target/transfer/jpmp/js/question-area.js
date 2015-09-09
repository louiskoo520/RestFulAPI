var xAxis2 =["崇安区","惠山区","北塘区","南长区","滨湖区","锡山区"];
var series2 = [
              {
                  "name":"未分配",
                  "type":"bar",
                  "data":[220, 500, 1000, 1500, 2000,200]
              },
              {
                  "name":"通知中",
                  "type":"bar",
                  "data":[244, 230, 660, 1200, 1400,399]
              }
          ]
$(document).ready(function(){
	var userNamesss,userIdsss;
	$(".questiontable .area-type-ul").empty();
	for(var i in questionArea){
		$(".questiontable .area-type-ul").append("<li><label><input type=\"checkbox\"  checked='checked' class=\"area-type\"><label>"+questionArea[i]+"</label></label></li>");
	}
	drawQuestionAreaChat(xAxis2,series2);
	$(".questiontable .range").click(function(){
		userIdsss = $(".area-reportor").val();
		$(".area-reportor option").each(function(){
			if($(this).attr("value")==userIdsss){
				userNamesss = $(this).html();
			}
		});
		$(".questiontable .start").val("");
		$(".questiontable .end").val("");
		$(this).parent().parent().siblings().each(function(){
			$(this).find(".range").attr("checked",false);
		});
		$(this).attr("checked",true);
		var qTypeArr = [];
		var areaArr = [];
		$(".questiontable .q-type").each(function(){
			if($(this).prop("checked")){
				qTypeArr.push($(this).next().attr("code"));
			}
		});
		$(".questiontable .area-type").each(function(){
			if($(this).prop("checked")){
				areaArr.push($(this).next().html());
			}
		});
		if(qTypeArr.length==0){
			alert("请选择问题类型");
			return;
		}
		if(areaArr.length==0){
			alert("请选择区域");
			return;
		}
		var range = $(this).next().html();
		//获取当前用户
		$.ajax({
			async:false,
			type:"post",
			url:"question/questioAreaChartData",
			data:{type:"checkbox",range:range,qType:qTypeArr.join(","),areaArr:areaArr.join(","),userInfo:(userIdsss+","+userNamesss)},
			dataType:"json",
			success:function(data){
				xAxis2 = areaArr;
				series2 = data;
				drawQuestionAreaChat(xAxis2,series2);
			}
		});
	});
	$('.questiontable .start,.questiontable .end').bind("click",function(){
		$(".questiontable .range").each(function(){
			$(this).attr("checked",false);
		});
	});
	$(".questiontable button[type='submit']").click(function(){
		userIdsss = $(".area-reportor").val();
		$(".area-reportor option").each(function(){
			if($(this).attr("value")==userIdsss){
				userNamesss = $(this).html();
			}
		});
		var ifContinue = true;
		if($(".questiontable .start").val()==""&&$(".questiontable .end").val()==""){
			var ifHasCondition = false;
			$(".questiontable .range").each(function(){
				if($(this).prop("checked")){
					ifHasCondition = true;
					$(this).trigger("click");
					$(this).attr("checked",true);
					ifContinue  = false;
				}
			});
			if(!ifHasCondition){
				alert("没有选择时间区域");
				return;
			}
		}
		if(!ifContinue){
			return;
		}
		var qTypeArr = [];
		var areaArr = [];
		$(".questiontable .q-type").each(function(){
			if($(this).prop("checked")){
				qTypeArr.push($(this).next().attr("code"));
			}
		});
		$(".questiontable .area-type").each(function(){
			if($(this).prop("checked")){
				areaArr.push($(this).next().html());
			}
		});
		if(qTypeArr.length==0){
			alert("请选择问题类型");
			return;
		}
		if(areaArr.length==0){
			alert("请选择区域");
			return;
		}
		var startTime = $(".questiontable .start").val();
		var endTime = $(".questiontable .end").val();
		startTime += " 00:00:00";
		endTime += " 23:59:59";
		$.ajax({
			async:false,
			type:"post",
			url:"question/questioAreaChartData",
			data:{type:"button",start:startTime,end:endTime,qType:qTypeArr.join(","),areaArr:areaArr.join(","),userInfo:(userIdsss+","+userNamesss)},
			dataType:"json",
			success:function(data){
				xAxis2 = areaArr;
				series2 = data;
				drawQuestionAreaChat(xAxis2,series2);
			}
		});
	});
	
	
	$(".questiontable .range").each(function(){
		if($(this).prop("checked")){
			$(this).trigger("click");
			$(this).attr("checked",true);
			userNamesss = "全部";
			userIdsss = -1;
		}
	});
	
});
/**
 * 问题-区域 报表
 */
function drawQuestionAreaChat(xAxis,series){
	// 路径配置
    require.config({
        paths:{ 
            'echarts' : 'http://echarts.baidu.com/build/echarts',
            'echarts/chart/bar' : 'http://echarts.baidu.com/build/echarts'
        }
    });
    // 使用
    require(
        [
            'echarts',
            'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
        ],
        function (ec) {
            // 基于准备好的dom，初始化echarts图表
            var myChart = ec.init(document.getElementById('questionarea')); 
            
            var option = {
            	title:{
            		text:'',
            		subtext:'数量(个)'
            	},
                tooltip: {
                    show: true
                },
                legend: {
                    data:['未分配','通知中','已通知','已接收','已完成','已关闭']
                },
                xAxis : [
                    {
                        type : 'category',
//                        data : ["崇安区","惠山区","北塘区","南长区","滨湖区","锡山区"]
                        data:xAxis
                    }
                ],
                yAxis : [
                    {
                        type : 'value'
                    }
                ],
                series : series/*[
                    {
                        "name":"本周处理",
                        "type":"bar",
                        "data":[220, 500, 1000, 1500, 2000,200]
                    },
                    {
                        "name":"上周处理",
                        "type":"bar",
                        "data":[244, 230, 660, 1200, 1400,399]
                    }
                ]*/
            };
            // 为echarts对象加载数据 
            myChart.setOption(option); 
        }
    );
}