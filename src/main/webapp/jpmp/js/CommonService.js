var CommonService = function(){
	var serverAddr = "192.168.2.103";
	//web服务器端口
	var port = 10000;
	//与客户端监控Websocket端口
	var wsPort = 8082;
	//Node视频流转发服务器端口
	var nodePort = 8001;
	var projectName = "jpmp";

	return {
		getNodeAddr:function(){
			return "ws://"+serverAddr+":"+nodePort;
		},
		getWsAddr : function(){
			return "ws://"+serverAddr+":"+wsPort+"/websocket";
		},

		getServiceBaseAddr : function(){
			return "http://"+serverAddr+":"+port+"/"+projectName+"/";
		},
		sleep:function(sleepTime){
			for(var start = Date.now(); Date.now() - start <= sleepTime; ) { } 
		},
		getNowTime:function(){
			var myDate = new Date();
			var time = myDate.getFullYear()+"-";
			var mouth = myDate.getMonth()+1;
			if(mouth < 10)
				mouth = "0"+mouth;
			time += mouth+"-";
			var date = myDate.getDate();
			if(date < 10)
				date = "0"+date;
			time += date+" ";
			
			//上午标志位
			var flag = false;
			var hour = myDate.getHours();
			if(hour >= 12){
				flag = true;
				hour -= 12;
			}
			if(hour < 10)
				hour = "0"+hour;
			time += hour+":";

			var minute = myDate.getMinutes();
			if(minute < 10)
				minute = "0"+minute;
			time += minute+" ";
			if(flag)
				time += "PM";
			else
				time += "AM";
			return time;
		},
		/**
		 *判断str1字符串是否以str2为结束
		 *@param str1 原字符串
		 *@param str2 子串
		 *@return 是-true,否-false
		 */
		endWith:function(str1, str2){
			 if(str1 == null || str2 == null){
				return false;
			 }
			 if(str1.length < str2.length){
				return false;
			 }else if(str1 == str2){
				return true;
			 }else if(str1.substring(str1.length - str2.length) == str2){
				return true;
			 }
			 return false;
		},
		/**
		 * JSON对象转字符串
		 * @param obj 待转换字符串对象
		 * @return 转换后的字符串
		 */
		json2str:function(obj)
		{
		  var S = [];
		  for(var i in obj){
				obj[i] = typeof obj[i] == 'string'?'"'+obj[i]+'"':(typeof obj[i] == 'object'?json2str(obj[i]):obj[i]);
				S.push(i+':'+obj[i]); 
			}
			return '{'+S.join(',')+'}';
		}
	}
}();