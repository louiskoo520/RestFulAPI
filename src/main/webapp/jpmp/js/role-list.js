$(document).ready(function(){
	//加载角色列表
	var pageIndex = 1;
	var pageCount;
	var startOrEnd = "load";
	getRoleList();
	/*//点击下一页下一个首页尾页
	$(document).on("click",".page-1 div",function(){
		var inner = $(this).html();
		if(isNaN(inner)){
			if(inner=="&lt;&lt;"){
				pageIndex = 1;
				startOrEnd = "first";
			}else if(inner=="&gt;&gt;"){
				pageIndex = pageCount;
				startOrEnd = "last";
			}
		}else{
			pageIndex = inner;
			startOrEnd = null;
			if($(this).prev().attr("class").indexOf("first")>0){
				startOrEnd = "start";
			}else if($(this).next().attr("class").indexOf("last")>0){
				startOrEnd = "end";
			}
		}
		getRoleList();
	});*/
	function getRoleList(){
		$.ajax({
			async:false,
			type:"get",
			url:"../rest/role/listRole",
			data:{pageIndex:pageIndex},
			dataType:"json",
			success:function(data){
				/*pageIndex = data[0].pageIndex;
				pageCount = data[0].pageCount;*/
				$.each(data.role, function(index, info){
                    var auth = "权限 : ";
                    if(info.authorities !=null && info.authorities!='undefined'){



                     $.each(info.authorities , function(i , item){
                         auth+=item.name+"  ";
                     });
                 }
					
					if ($("ul#"+info["id"]).attr("class")) {
						$("li#"+info["id"]).append("<li>"+auth+"</li>");
					} 
						$("div.con").append("<ul id='"+info['id']+"' class='head clearfix' style='height:auto;background: none repeat scroll 0% 0% #fff;'>"
								+ "<li style='width: 120px; line-height:60px' class='kind'>"+info['name']+"</li>"
								+ "<li style='width: 200px; line-height:60px' class='cont'>"+info['createDate']+"</li>"
								+ "<li id='"+info['id']+"' style='width: 480px; padding: 10px 0 10px 40px;' class='cont'><ul><li>"+auth+"</li></ul></li>"
								+ "<li style='width: 120px; line-height:60px' class='opt'><a href='addRole.html?id="
								+info['id']+"' class='gray'>修改</a><a href='#' id='"
								+info['id']+"' class='gray del' style='margin-left: 20px'>删除</a></li>"
								+ "</ul>"
						);	
					
				});
				$(".del[id='38']").remove();
				//showPage(pageIndex,pageCount,startOrEnd);
			}
		});
	}
	
	//根据当前页面和总页码展示页码。
	//startOrEnd:load first last start end
	function showPage(pageIndex,pageCount,startOrEnd){
		if(startOrEnd=="load"){
			start = 1;
			end = 6;
			if(pageCount<6){
				end = pageCount;
			}
		}else if(startOrEnd=="first"){
			start = 1;
			end = 6;
			if(pageCount<6){
				end = pageCount;
			}
		}else if(startOrEnd=="last"){
			end = pageCount;
			start = ((pageCount-5)<1)?1:pageCount-5;
		}else if(startOrEnd=="start"){
			if(pageIndex!=1){
				end = pageIndex;
				start = ((pageIndex-5)<1)?1:pageIndex-5;
			}
		}else if(startOrEnd=="end"){
			if(pageIndex!=pageCount){
				start = pageIndex;
				end = (start+5)>pageCount?pageCount:start+5;
			}
		}else if(startOrEnd==null){
			
		}
		var html = "<div class='record first'><<</div>";
//		//在页码展示的开始页码，结束页码
		for(var i=start;i<=end;i++){
			if(i!=pageIndex){
				html += "<div class='record'>"+i+"</div>";
			}else{
				html += "<div class='record on'>"+i+"</div>";
			}
		}
		html += "<div class='record last'>>></div>";
		$(".page-1").empty();
		$(".page-1").append(html);
	}
});