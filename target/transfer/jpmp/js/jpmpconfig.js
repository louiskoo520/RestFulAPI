//定义全局变量
var sessionUserInfo;
function getLoginUser() {
	return sessionUserInfo;
}
var ERROR = "error";
var SUCCESS = "success";
//问题区域
var questionArea = ["越秀区","海珠区","荔湾区","天河区","白云区","黄埔区","花都区","番禺区","萝岗区","南沙区"];
var departList = ["技术部","市场部","开发部","项目部","人事部","财务部","行政部"];
yinru('css/index.css');
yinru('js/jquery.js');
yinru('js/util.js');
function yinru(argument) { //函数可以单独引入一个js或者css
	var file = argument; 
	if (file.match(/.*\.js$/)) { //以任意开头但是以.js结尾正则表达式
		document.write('<script type="text/javascript" src="' + file + '"></script>'); 
	} else if(file.match(/.*\.css$/)) {
		document.write('<link rel="stylesheet" href="'+file+'" type="text/css" />'); 
	}
}