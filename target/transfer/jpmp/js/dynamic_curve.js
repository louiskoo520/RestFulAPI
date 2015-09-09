dynamic_option = {
    title : {
        text: '动态数据',
        subtext: '在线用户数'
    },
    tooltip : {
        trigger: 'axis'
    },
    legend: {
        data:['在线用户数']
    },
    toolbox: {
        show : false,
        feature : {
            mark : {show: false},
            dataView : {show: false, readOnly: false},
            magicType : {show: false, type: ['line']},
            restore : {show: false},
            saveAsImage : {show: false}
        }
    },
    dataZoom : {
        show : false,
        realtime: true,
        start : 50,
        end : 100
    },
    xAxis : [
        {
            type : 'category',
            boundaryGap : true,
            data : (function(){
                var now = new Date();
                var res = [];
                var len = 10;
                while (len--) {
                    res.unshift(now.toLocaleTimeString().replace(/^\D*/,''));
                    now = new Date(now - 2000);
                }
                return res;
            })()
        }/*,
        {
            type : 'category',
            boundaryGap : true,
            splitline : {show : false},
            data : (function(){
                var res = [];
                var len = 10;
                while (len--) {
                    res.push(len + 1);
                }
                return res;
            })()
        }*/
    ],
    yAxis : [
        {
            type : 'value',
            scale: true,
            precision:0,
            power:1,
            name : '',
            boundaryGap: [0.2, 0.2],
            splitArea : {show : true}
        },
        {
            type : 'value',
            scale: true,
            name : '',
            boundaryGap: [0.2, 0.2]
        }
    ],
    series : [
        {
            name:'在线用户数',
            type:'line',
            itemStyle: {
                normal: {
                    // areaStyle: {type: 'default'},
                    lineStyle: {
                        shadowColor : 'rgba(0,0,0,0.4)'
                    }
                }
            },
            data:(function(){
                var res = [];
                var len = 10;
                while (len--) {
                    res.push(0);
                }
                return res;
            })()
        }
    ]
};		