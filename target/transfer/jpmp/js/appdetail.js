$(document).ready(function(){
	var appId = getQueryString("appId");
	//展示app详情
	showAppDetail(appId);
	//返回
	$(".back").click(function(){
		history.go(-1);
	});
	//到编辑页面
	$(".upload_").click(function(){
		window.location.href = "updateapp.html?appId="+appId;
	});
	function showAppDetail(appId){
		$.ajax({
			async:false,
			type:"post",
			url:"app/appDetail",
			dataType:"json",
			data:{appId:appId},
			success:function(data){
				var appName = data.name;
				var typeName = data.typeName;
				var appIntro = data.appDesc;
				var latest = data.latest;
				var iconPath = data.img;
				$(".app-name-lbl").html(appName);
				$(".app-logo-lbl img").attr("src","."+data.img);
				$(".app-type-lbl").html(typeName);
				$(".app-cont-lbl").html(appIntro);
				$(".update-time-lbl").html(latest);
				var index = 1;
				$(".app-pic-lbl"+index+" img").attr("src","."+data.img1);
				index ++;
				$(".app-pic-lbl"+index+" img").attr("src","."+data.img2);
				index ++;
				$(".app-pic-lbl"+index+" img").attr("src","."+data.img3);
				index ++;
				$(".app-pic-lbl"+index+" img").attr("src","."+data.img4);
			}
		});
	}
});