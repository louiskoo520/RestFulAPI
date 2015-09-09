// 基于准备好的dom，初始化echarts图表
var aaa;
var seriesUser = [[11, 11, 15, 13, 12, 13, 10],[1, 5, 2, 5, 3, 2, 0],[7, 4, 22, 54, 32, 34, 0],[7, 4, 22, 54, 32, 34, 0]];
// 路径配置
require.config({
    paths:{ 
        'echarts' : './js/echarts',
        'echarts/chart/bar' : './js/echarts',
        'echarts/chart/line': './js/echarts'
    }
});
// 使用
require(
    [
        'echarts',
        'echarts/chart/bar' // 使用柱状图就加载bar模块，按需加载
    ],
    function(ec) {
    	aaa = ec.init(document.getElementById('userstate'));
    	aaa.addData([
	        [0, seriesUser[0], false, false, weekaxis],
	        [1, seriesUser[1], false, false, weekaxis],
	        [2, seriesUser[2], false, false, weekaxis],
	        [3, seriesUser[3], false, false, weekaxis],
	    ]);
        // 为echarts对象加载数据 
        aaa.setOption(userstate_option);
    }
);
$(function(){
	$(".usertype input").click(function(){
		if (this.checked) {
			seriesUser[3] = [];
			aaa.addData(seriesUser);
		} else {
			
		}
	});
});
var dayaxis = ["00:00","04:00","08:00","12:00","16:00","20:00","24:00"];
var weekaxis = ["周一","周二","周三","周四","周五","周六","周末"];
var monthaxis = ["00:00","04:00","08:00","12:00","16:00","20:00","24:00"];
function getUserLog(time, datef, datat, type) {
	$.ajax({
		url: "useropera/userlog",
		dataType: "json",
		type: "post",
		data: {
			time: time,
			datef: datef,
			datet: datet,
			type: type
		},
		success: function(data){
			var serData = [];
			$.each(data, function(index, info){
								
			});				
		}
	});
}