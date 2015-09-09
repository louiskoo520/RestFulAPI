var frompage = 1;
$(function(){
	$.ajax({
		async:false,
		type:"post",
		url:"user/clientUserList",
		dataType:"json",
		success:function(data){
			var html = "<option></option>";
			for(var i in data){
				html += "<option value='"+data[i].id+"'>"+data[i].name+"</option>";
			}
			$(".user-select").append(html);
		}
	});
	getUserStateLog();
	$("#search").click(function(){
		frompage = 1;
		getUserStateLog();
	});
	$("#prevpage").click(function(){
		frompage = frompage - 1;
		getUserStateLog();
	});
	$("#nextpage").click(function(){
		frompage = frompage + 1;
		getUserStateLog();
	});
	$("body").on("click", ".pagenum div", function(){
		frompage = parseInt($(this).attr("id"));
		getUserStateLog();
	});
});
function getUserStateLog() {	
	var datef = $("#datefrom").val();
	var datet = $("#dateto").val();
	var username = $("#username").find("option:selected").html();
	$.ajax({
		url: "useropera/userlog",
		dataType: "json",
		type: "post",
		data: {	dateFrom: datef,
				dateTo: datet,
				username: username,
				frompage: frompage	},
		success: function(data){
			$("tbody").empty();
			$(".pagenum").empty();
			var count = data.count;
			var pagenum = parseInt(data.pagenum);
			$.each(data.list, function(index, info){
				var time = "";
				try {					
					var mmSec = new Date(info['outtime'].replace(/-/g,"/")).getTime() - new Date(info['ontime'].replace(/-/g,"/")).getTime();
					time = mmSec/1000/60;
					time = time.toFixed(2);
				} catch(e) {}
				$("tbody").append("<tr><th>"+info["name"]+"</th><th>"+info["ontime"]+"</th><th>"+info["outtime"]+"</th><th>"+time+"</th>");
			});
			if (frompage == 1) {
				$("#prevpage").hide();
			} else {
				$("#prevpage").show();
			}
			if (frompage > 5) {
				var f = frompage-5;
				var t;
				if (pagenum > (frompage+4)) {
					t = frompage+4;
				} else {
					t = pagenum;
				}
				for (var i=f;i<=t;i++) {
					$(".pagenum").append("<div class='record' id='"+i+"'>"+i+"</div>");
				}
			} else {
				var t;
				if (pagenum > 10) {
					t = 10;
				} else {
					t = pagenum;
				}
				for (var i=1;i<=t;i++) {
					$(".pagenum").append("<div class='record' id='"+i+"'>"+i+"</div>");
				}
			}
			$(".pagenum div#"+frompage).addClass("on");
			if (frompage == pagenum) {
				$("#nextpage").hide();
			} else {
				$("#nextpage").show();
			}
		}
	});
}