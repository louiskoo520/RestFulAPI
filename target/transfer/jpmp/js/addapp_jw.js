$(document).ready(function(){
	fun();
	$("#file1,#file2,#file3,#file4").change(function(){
		$(this).hide();
		fun();
	});
	$("#file5").click(function(){
		alert("最多只能选4张图片");
	});
	$("#img1,#img2,#img3,#img4").mouseover(function(){
		$(this).prev().show();
	});
	$(".image-cancel-update").mouseover(function(){
		$(this).show();
	});
	$("#img1,#img2,#img3,#img4").mouseout(function(){
		$(this).prev().hide();
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
			$("#file1").show();
		}
		if(parentNextId=="img2"){
			$("#file2").show();
		}
		if(parentNextId=="img3"){
			$("#file3").show();
		}
		if(parentNextId=="img4"){
			$("#file4").show();
		}
	});
	//上传文件change事件
	$("#app-apk").change(function(){
		var appName = $(this).val();
		$(this).next().next().html(appName);
		$(this).next().next().removeClass("warm");
	});
	$("#app-logo").change(function(){
		var logoName = $(this).val();
		$(this).next().next().html(logoName);
		$(this).next().next().removeClass("warm");
	});
	//提交按钮
	$(".upload-btn").click(function(){
		var appName = $("#app-name").val();
		var appType = $("#app-type").val();
		var appIntro = $("#app-intro").val();
		var apkName = $("#app-apk").val();
		var logoName = $("#app-logo").val();
		var appVersion = $("#app-version").val();
		if(appName==""||appName==null){
			$("#app-name").next().addClass("warm").html("请设置应用名称");
			return;
		}
		if(appVersion==""||appVersion==null){
			$("#app-version").next().addClass("warm").html("请输入app版本号（与此app版本号一致）");
			return;
		}
		if(appType==""||appType==null){
			$("#app-type").next().addClass("warm").html("请设置应用类别");
			return;
		}
		if(appIntro==""||appIntro==null){
			$("#app-intro").next().addClass("warm").html("请填写应用简介");
			return;
		}
		if(apkName==""||apkName==null){
			$("#app-apk").next().next().addClass("warm").html("请上传应用文件（apk）");
			return;
		}
		if(logoName==""||logoName==null){
			$("#app-logo").next().next().addClass("warm").html("请上传应用logo");
			return;
		}
		if(apkName.substring(apkName.lastIndexOf(".")+1,apkName.length)!="apk"){
			$("#app-apk").next().next().addClass("warm").html("<i class='normal'>（"+apkName+"）</i> 只能上传apk类型的文件");
			return;
		}
		//验证logo大小
		var logWidth = document.getElementById("app-logo").files[0];
		//alert(logWidth);
		$("form").submit();
	});
	//显示已经上传的图片数量和还可以上传的数量
	function fun(){
		var selectedNO = 0;//已经选择的图片数量
		var maySelect = 4;//还可以上传图片的数量
		$("#img1,#img2,#img3,#img4").each(function(){
			if($(this).attr("src")!=null&&$(this).attr("src")!=""){
				selectedNO++;
			}
		});
		maySelect = 4-selectedNO;
		$("#selected").html(selectedNO);
		$("#may-select").html(maySelect);
	}
});
























