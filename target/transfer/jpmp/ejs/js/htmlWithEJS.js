// JavaScript Document
$(function(){
	var data = {
			title : '关于EJS的使用',
			steps : ['1、下载ejs_production.js','2、在HTML中引入ejs_production.js','3、新建一个module.ejs模板','4、在html文件对应的js文件中给模板注入数据。','5、将带有动态数据的模板注入hml相应的位置','6,这是ejs的链接组件的应用link_to(steps[i],"steps/i");'],
			imgLabel : '<%= img_tag("images/girl001.jpg")%>'
			},
		innerHtml = new EJS({url:'ejs/module.ejs'}).render(data);
		
	$("#container").html(innerHtml);//
	});
	
