$(document).ready(function(){
	
	//数据回调
	var appId = getQueryString("appId");
	$("#appId").val(appId);
	$.ajax({
		async:false,
		type:"post",
		url:"app/appDetail",
		data:{appId:appId},
		dataType:"json",
		success:function(data){
			var appName = data.name;
			var typeName = data.typeName;
			var version = data.version;
			var appIntro = data.appDesc;
			var apkname = data.path.substring(data.path.lastIndexOf("/")+1);
			$("#app-name").val(appName);
			$("#app-type").val(typeName);
			$("#app-version").val(version);
			$("#app-intro").html(appIntro);
			$("#apkname").html(apkname);
			$("#img").attr("src","."+data.img);
			$("#img1").attr("src","."+data.img1);
			$("#img2").attr("src","."+data.img2);
			$("#img3").attr("src","."+data.img3);
			$("#img4").attr("src","."+data.img4);
		}
	});
	showImageAdd();
	//点击添加图片按钮
	$(".image-add").click(function(){
		var parentNextId = $(this).next().next().attr("id");
		if(parentNextId=="img1"){
			$("#file1").click();
		}
		if(parentNextId=="img2"){
			$("#file2").click();
		}
		if(parentNextId=="img3"){
			$("#file3").click();
		}
		if(parentNextId=="img4"){
			$("#file4").click();
		}
		showImageAdd();
	});
	//点击修改说明按钮触发
	$("#update-intro").click(function(){
		var innerHtml = $(this).html();
		if(innerHtml=="修改"){
			$("#appIntro-textarea").show();
			$("#app-intro").hide();
			var introDetail = $("#app-intro").html();
			$("#appIntro-textarea textarea").val(introDetail);
			$("#appIntro-textarea textarea").attr("name","appIntro");
			$(this).html("保存");
		}else{
			var introDetail = $("#appIntro-textarea textarea").val();
			if(introDetail==null||introDetail==""){
				$(this).next().html("介绍不能为空").addClass("warm").removeClass("normal");
			}else{
				$(this).html("修改");
				$("#appIntro-textarea").hide();
				$("#app-intro").show();
				$("#app-intro").html(introDetail);
				$(this).next().html("").addClass("normal").removeClass("warm");
			}
		}
	});
	//点击修改文件按钮
	$("#update-apk").click(function(){
		$("#path").click();
	});
	$("#path").change(function(){
		$(this).next().html($(this).val());
		$(this).attr("name","appApk");
	});
	
	//点击更换图标
	$("#update-icon").click(function(){
		$("#app-logo").click();
	});
	$("#app-logo").change(function(){
		$(this).attr("name","appLogo");
	});
	
	$("#file1,#file2,#file3,#file4").change(function(){
		$(this).attr("name",$(this).attr("id"));
	});
	$(".box").mouseover(function(){
		if ($(this).find(".img").attr("src")) {			
			$(this).find("div").eq(1).show();
		}
	});
	$(".image-cancel-update").mouseover(function(){
		$(this).show();
	});
	$(".box").mouseout(function(){
		if ($(this).find(".img").attr("src")) {			
			$(this).find("div").eq(1).hide();
		}
	});
	$(".image-cancel-update").mouseout(function(){
		$(this).hide();
	});
	//修改
	$(".image-update").click(function(){
		var parentNextId = $(this).parent().next().attr("id");
		if(parentNextId=="img1"){
			$("#file1").click();
		}
		if(parentNextId=="img2"){
			$("#file2").click();
		}
		if(parentNextId=="img3"){
			$("#file3").click();
		}
		if(parentNextId=="img4"){
			$("#file4").click();
		}
	});
	//删除
	$(".image-cancel").click(function(){
		var parentNextId = $(this).parent().next().attr("id");
		$(this).parent().next().attr("src",null);
		if(parentNextId=="img1"){
			var obj = document.getElementById("file1");
			obj.outerHTML=obj.outerHTML;
			$("#file1").attr("name","file1");
		}
		if(parentNextId=="img2"){
			var obj = document.getElementById("file2");
			obj.outerHTML=obj.outerHTML;
			$("#file2").attr("name","file2");
		}
		if(parentNextId=="img3"){
			var obj = document.getElementById("file3");
			obj.outerHTML=obj.outerHTML;
			$("#file3").attr("name","file3");
		}
		if(parentNextId=="img4"){
			var obj = document.getElementById("file4");
			obj.outerHTML=obj.outerHTML;
			$("#file4").attr("name","file4");
		}
		showImageAdd();
		$(this).parent().hide();
	});
	//提交按钮
	$(".upload_").click(function(){
		var name;
		name = $("#appIntro-textarea textarea").attr("name");
		if(name!=null){
			if($("#appIntro-textarea textarea").val()==null||$("#appIntro-textarea textarea").val()==""){
				$("#update-intro").next().html("介绍不能为空").addClass("warm").removeClass("normal");
				return;
			}
		}
		name = $("#path").attr("name");
		if(name!=null){
			var suffix = $("#path").val().substring($("#path").val().lastIndexOf(".")+1,$("#path").val().length);
			if(suffix!="apk"){
				$("#update-apk").next().html("文件格式不正确，请选择apk文件").addClass("warm").removeClass("normal");
				return;
			}
		}
		name = $("#app-version").val();
		if(name==""||name==null){
			$("#app-version").next().addClass("warm").html("请输入app版本号（与此app版本号一致）");
			return;
		}
		name = $("#app-logo").attr("name");
		if(name!=null){
			var suffix = $("#app-logo").val().substring($("#app-logo").val().lastIndexOf(".")+1,$("#app-logo").val().length);
			if(suffix!="jpg"&&suffix!="png"&&suffix!="icon"&&suffix!="gif"){
				$("#update-icon").next().html("文件格式只能为jpg / png / icon / gif").addClass("warm").removeClass("normal");
				return;
			}
		}
		$("form").submit();
	});
	
	function getImgSrc(file){
		var src = null;
		if(file==null||file==""){
		}else{
			var fileName = file.substring(file.lastIndexOf("\\")+1,file.length);
			var filePath = file.substring(0,file.lastIndexOf("\\"));
			var parentPath = filePath.substring(filePath.lastIndexOf("\\")+1,filePath.length);
			src = parentPath+"/"+fileName;
		}
		return src;
	}
	//展示新增APP
	function showImageAdd(){
		$(".img").each(function(){
			if ($(this).attr("src") == "." || $(this).attr("src") == ".null") {
				$(this).attr("src", "");
			}
			if(!$(this).attr("src")){
				$(this).parent().find(".image-add").show();
			}else{
				$(this).parent().find(".image-add").hide();
			}
		});
	}
	
});