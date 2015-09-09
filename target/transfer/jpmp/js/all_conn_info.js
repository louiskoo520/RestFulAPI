$(document).ready(function(){
	function load(){
		$.ajax({
			async:false,
			type:"post",
			url:"user/userList",
			dataType:"json",
			success:function(data){
				var html = "";
				$.each(data, function(index, info){
					html += "<tr>";
					var xy = info.xy.split(",");
					html += "<td style='width:100px;text-align:center'>"+info.name+"</td>";
					html += "<td style='width:40px;text-align:center'>"+info.age+"</td>";
					html += "<td style='width:100px;text-align:center'>"+info.department+"</td>";
					html += "<td style='width:150px;text-align:center'>"+info.tel+"</td>";
					html += "<td style='width:150px;text-align:center'>"+info.addr+"</td>";
					html += "<td style='width:150px;text-align:center'>"+info.qq+"</td>";
					html += "<td style='width:100px;text-align:center'>"+xy[0]+"</td>";
					html += "<td style='width:100px;text-align:center'>"+xy[1]+"</td>";
					html += "<td class='hidden-480 user_trace' style='text-align: center;cursor: pointer;' idd="+info.id+"><img src='img/web.png'></img></td>";
					html += "</tr>";
					
				});
				$("tbody").empty();
				$("tbody").append(html);
			}
		});
	}
	load();
	setInterval(function(){
		load();
	},5000);
	
	$(document).on("click",".user_trace",function(){
		//γ��
		var x = $(this).prev().prev().html();
		var y = $(this).prev().html();
		var id = $(this).attr("idd");
		window.location.href = "user_trace.html?id="+id+"&x="+x+"&y="+y;
	});
	
});