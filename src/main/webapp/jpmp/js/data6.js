var frompage = 1;
$(function(){
	getSysLog();
	$("#prevpage").click(function(){
		frompage = frompage - 1;
		getSysLog();
	});
	$("#nextpage").click(function(){
		frompage = frompage + 1;
		getSysLog();
	});
	$("body").on("click", ".pagenum div", function(){
		frompage = parseInt($(this).attr("id"));
		getSysLog();
	});
	$("#search").click(function(){
		frompage = 1;
		getSysLog();
	});
});
function getSysLog() {	
	var datef = $("#datefrom").val();
	var datet = $("#dateto").val();
	$.ajax({
		url: "useropera/syslog",
		dataType: "json",
		type: "post",
		data: {	dateFrom: datef,
				dateTo: datet,
				frompage: frompage	},
		success: function(data){
			$("tbody").empty();
			$(".pagenum").empty();
			var count = data.count;
			var pagenum = parseInt(data.pagenum);
			$.each(data.list, function(index, info){
				$("tbody").append("<tr><th>"+info["mobleCode"]+"</th><th>"+info["modifyTime"]+"</th><th>"+info["sourceAddr"]+"</th><th>"+info["destAddr"]+"</th>");
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