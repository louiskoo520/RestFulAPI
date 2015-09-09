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
		fun();
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
	$("#app-name").blur(function(){
		var appName = $("#app-name").val();
		if(appName==""||appName==null){
			$("#app-name").next().addClass("warm").html("请设置应用名称");
		}else{
			$("#app-name").next().removeClass("warm").html("");
		}
	});
	$("#app-version").blur(function(){
		var appVersion = $("#app-version").val();
		if(appVersion==""||appVersion==null){
			$("#app-version").next().addClass("warm").html("请输入app版本号（与此app版本号一致）");
		}else{
			$("#app-version").next().removeClass("warm").html("与此app版本号一致");
		}
	});
	$("#app-intro").blur(function(){
		var appVersion = $("#app-intro").val();
		if(appVersion==""||appVersion==null){
			$("#app-intro").next().addClass("warm").html("请填写应用简介");
		}else{
			$("#app-intro").next().removeClass("warm").html("");
		}
	});
	$("#app-apk").change(function(){
		var apkName = $("#app-apk").val();
		if(apkName==""||apkName==null){
			$("#app-apk").next().next().addClass("warm").html("请上传应用文件（apk）");
		}else{
			if(apkName.substring(apkName.lastIndexOf(".")+1,apkName.length)!="apk"){
				$("#app-apk").next().next().addClass("warm").html("<i class='normal'>（"+apkName+"）</i> 只能上传apk类型的文件");
			}else{
				$("#app-apk").next().next().removeClass("warm").html(apkName);
			}
		}
	});
	$("#app-logo").change(function(){
		var apkName = $("#app-logo").val();
		var imgType = apkName.substring(apkName.lastIndexOf(".")+1,apkName.length);
		if(apkName==""||apkName==null){
			$("#app-apk").next().next().addClass("warm").html("请上传应用logo");
		}else{
			if(imgType!="jpg"&&imgType!="png"&&imgType!="ico"){
				$("#app-logo").next().next().addClass("warm").html("应用logo文件格式必须符合 .jpg , .png , .ico");
			}else{
				$("#app-logo").next().next().removeClass("warm").html(apkName);
			}
		}
	});
	//提交按钮
	$(".upload_").click(function(){
		var appName = $("#app-name").val();
		var appType = $("#app-type").val();
		var appIntro = $("#app-intro").val();
		var apkName = $("#app-apk").val();
		var logoName = $("#app-logo").val();
		var imgType = logoName.substring(logoName.lastIndexOf(".")+1,logoName.length);
		var appVersion = $("#app-version").val();
		if(appName==""||appName==null){
			$("#app-name").next().addClass("warm").html("请设置应用名称");
			return;
		}else{
			$("#app-name").next().removeClass("warm");
		}
		if(appVersion==""||appVersion==null){
			$("#app-version").next().addClass("warm").html("请输入app版本号（与此app版本号一致）");
			return;
		}else{
			$("#app-version").next().removeClass("warm");
		}
		if(appType==""||appType==null){
			$("#app-type").next().addClass("warm").html("请设置应用类别");
			return;
		}else{
			$("#app-type").next().removeClass("warm");
		}
		if(appIntro==""||appIntro==null){
			$("#app-intro").next().addClass("warm").html("请填写应用简介");
			return;
		}else{
			$("#app-intro").next().removeClass("warm");
		}
		if(apkName==""||apkName==null){
			$("#app-apk").next().next().addClass("warm").html("请上传应用文件（apk）");
			return;
		}else{
			$("#app-apk").next().next().removeClass("warm");
		}
		if(logoName==""||logoName==null){
			$("#app-logo").next().next().addClass("warm").html("请上传应用logo");
			return;
		}else{
			$("#app-logo").next().next().removeClass("warm");
		}
		if(imgType!="jpg"&&imgType!="png"&&imgType!="ico"){
			$("#app-logo").next().next().addClass("warm").html("应用logo文件格式必须符合 .jpg , .png , .ico");
			return;
		}else{
			$("#app-logo").next().next().removeClass("warm");
		}
		if(apkName.substring(apkName.lastIndexOf(".")+1,apkName.length)!="apk"){
			$("#app-apk").next().next().addClass("warm").html("<i class='normal'>（"+apkName+"）</i> 只能上传apk类型的文件");
			return;
		}else{
			$("#app-apk").next().next().removeClass("warm");
		}
		//验证详细图片大小大小
		var imgSize1 = getFileSize(document.getElementById("img1"));
		var imgSize2 = getFileSize(document.getElementById("img2"));
		var imgSize3 = getFileSize(document.getElementById("img3"));
		var imgSize4 = getFileSize(document.getElementById("img4"));
		var detailNotice = "第";
		var detailArr = [];
		if(imgSize1>500){
			detailArr.push("一");
		}
		if(imgSize2>500){
			detailArr.push("二");
		}
		if(imgSize3>500){
			detailArr.push("三");
		}
		if(imgSize4>500){
			detailArr.push("四");
		}
		if(detailArr.length!=0){
			detailNotice += detailArr.join(",")+"张图片大小超过500K，请重新上传";
			$(".notice-detail").addClass("warm").html("（"+detailNotice+"）");
			return;
		}
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
	//重置
	$(".reset").click(function(){
		document.getElementById("form1").reset();
		$("#app-name").next().removeClass("warm").html("");
		$("#app-version").next().removeClass("warm").html("与此APP版本一致");
		$("#app-type").next().removeClass("warm").html("");
		$("#app-intro").next().removeClass("warm").html("");
		$("#app-apk").next().next().removeClass("warm").html("请上传apk类型文件");
		$("#app-logo").next().next().removeClass("warm").html("请上传100px*100px的图片");
		$(".notice-detail").removeClass("warm").html("请上传500K以内的图片，最多可上传4张");
		$("#img1").attr("src",null);
		$("#img2").attr("src",null);
		$("#img3").attr("src",null);
		$("#img4").attr("src",null);
		fun();
		$("#file1").show();
		$("#file2").show();
		$("#file3").show();
		$("#file4").show();
	});
});
























