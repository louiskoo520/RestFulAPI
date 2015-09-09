var userstate_option = {
    title : {
        text: '在线人数'
    },
    tooltip : {
        trigger: 'axis'
    },
    legend: {
        data:['手机端默认用户','手机端注册用户','服务端默认用户','超级管理员']
    },
    toolbox: {
        show : true,
        feature : {
            mark : {show: true},
            dataView : {show: true, readOnly: false},
            magicType : {show: true, type: ['line', 'bar']},
            restore : {show: true},
            saveAsImage : {show: true}
        }
    },
    calculable : true,
    xAxis : [
        {
            type : 'category',
            boundaryGap : false,
            data : ['周一','周二','周三','周四','周五','周六','周日']
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
    series : [
        {
            name:'手机端默认用户',
            type:'line',
            data:[11, 11, 15, 13, 12, 13, 10],
            markPoint : {
                data : [
                    {type : 'max', name: '最大值'}
                ]
            }
        },
        {
            name:'手机端注册用户',
            type:'line',
            data:[1, 5, 2, 5, 3, 2, 0],
            markPoint : {
        		data : [
                    {type : 'max', name: '最大值'}
                ]
            }
        },
        {
            name:'服务端默认用户',
            type:'line',
            data:[7, 4, 22, 54, 32, 34, 0],
            markPoint : {
        		data : [
                    {type : 'max', name: '最大值'}
                ]
            }
        },
        {
            name:'超级管理员',
            type:'line',
            data:[],
            markPoint : {
        		data : [
                    {type : 'max', name: '最大值'}
                ]
            }
        }
    ]
};
                    