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
        // 基于准备好的dom，初始化echarts图表
        var myChart = ec.init(document.getElementById('main'));
        var initData;
		$.ajax({
	    	async:false,
	    	type:"post",
	    	url:"user/getLineUserNumber",
	    	dataType:"html",
	    	success:function(datas){
	    		initData = datas;	  
	    	}
	    });
		var timeTicket = setInterval(function(){
		    var data = 0;
		    var axisData = (new Date()).toLocaleTimeString().replace(/^\D*/,'');
		    $.ajax({
		    	async:false,
		    	type:"post",
		    	url:"user/getLineUserNumber",
		    	dataType:"html",
		    	success:function(datas){
		    		data = datas;
		    	}
		    });
		    
		    // 动态数据接口 addData
		    myChart.addData([
		        [
		            0,        // 系列索引
		            data, 	  // 新增数据
		            false,    // 新增数据是否从队列头部插入
		            false,    // 是否增加队列长度，false则自定删除原有数据，队头插入删队尾，队尾插入删队头
		            axisData  // 坐标轴标签
		        ]
		    ]);
		}, 5000);  

        // 为echarts对象加载数据 
        myChart.setOption(dynamic_option);  
        var len = 10;
        var now = new Date();
        while (len--) {
        	var axisData = now.toLocaleTimeString().replace(/^\D*/,'');
            myChart.addData([
                             [
                              len,      // 系列索引
                              initData, // 新增数据
                              false,    // 新增数据是否从队列头部插入
                              false,    // 是否增加队列长度，false则自定删除原有数据，队头插入删队尾，队尾插入删队头
                              axisData  // 坐标轴标签
                              ]
                          ]);
            now = new Date(now - 2000);
        }
    }
);