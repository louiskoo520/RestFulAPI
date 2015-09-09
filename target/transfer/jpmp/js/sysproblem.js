$(document).ready(function(){
	var pageb2;
	$.ajax({
		url: "sysproblem/modifyConfig",
		dataType: "json",
		success: function(data){											
			var html = "";
			for(var i in data){
				var settings = data[i];
				html += "<ul class='head clearfix' style='height: 60px;background: none repeat scroll 0% 0% #fff;'>";
				html += "<li style='width: 500px; line-height:60px' class='kind' title='"+settings.detail+"'>"+settings.detail+"</li>";
				html += "<li style='width: 320px; line-height:60px' class='cont' title='"+settings.value+"'>"+settings.value+"</li>";
				html += "<li style='width: 50px; line-height:60px' class='opt'><a class='gray modifys' type='"+settings.type+"' style='cursor:pointer;' idd='"+settings.id+"'>修改</a></li></ul>";
			}
			$("div .con").empty();
			$("div .con").append(html);
		}
	});
	$(document).on("click",".addin",function(){
		var valueArr = [];
		var sava = true;
		$(".md").each(function(){
			if(sava){
				if($(this).val()=="保存"){
					alert("还有值未保存");
					sava = false;
					return;
				}
			}
			valueArr.push($(this).prev().val());
		});
		if(!sava)
			return;
		layer.close(pageb2);
		$("a[type='8']").parent().prev().html(valueArr.join(","));
		//保存到数据库
		var id = $("a[type='8']").attr("idd");
		var settingvalus = $("a[type='8']").parent().prev().html();
		$.ajax({
			type:"post",
			url: "sysproblem/changeModifyConfig",
			data:{id:id,newSetting:settingvalus},
			dataType: "json"
		});
		questionArea = valueArr;
	});
	$(document).on("click",".md",function(){
		var value = $(this).val();
		if(value=="修改"){
			$(this).val("保存");
			$(this).prev().removeAttr("readonly");
			$(this).prev().css("border","1px solid gray");
			$(this).prev().focus();
		}else{
			if($(this).prev().val()==""){
				alert("不能为空");
				return;
			}
			$(this).val("修改");
			$(this).prev().attr("readonly","readonly");
			$(this).prev().css("border","none");
		}
	});
	$(document).on("click",".de",function(){
		if($(this).next().is(":hidden")){
			$(this).parent().next().remove();
			$(this).parent().remove();
		}else{
			$(this).parent().prev().show();
			$(this).parent().prev().prev().find(".ad").show();
			$(this).parent().next().remove();
			$(this).parent().remove();
		}
	});
	$(document).on("click",".ad",function(){
		$(this).hide();
		$(this).parent().next().hide();
		$(".linkbody").append('<div class="linkoption"><input style="border:1px solid gray;background-color:white;color:#6b7485;" type="text" value="" id="addout" class="btn gray-btn" \/><input type="button" value="保存"  class="btn gray-btn md" id="getlinkdate"\/><input type="button" value="删除"  class="btn gray-btn de" id="getlinkdate"\/><input type="button" value="添加"  class="btn gray-btn ad" id="getlinkdate"\/><\/div><div class="linkwrap clearfix"><div class="linklist"><input style="margin-left:140px;width:400px;" type="button" value="确定" class="addin btn gray-btn" step="0" \/><ul><\/ul><\/div><\/div>');
	});
	$(document).on("click",".modifys",function(){
		var type = $(this).attr("type");
		if (type == "7") {
			$(this).parent().prev().html("<select><option>800*500</option><option>800*600</option><option>1000*600</option><option>1000*800</option></select>");
		}else if(type == "8"){
			var fher = $(this).parent().prev().html();
			var html = '<\div class="linkbody clearfix">';
			for(var i in fher.split(",")){
				var ae = fher.split(",")[i];
				if(i==fher.split(",").length-1){
					html += '<div class="linkoption"><input style="border:none;background-color:white;color:#6b7485;" readonly="readonly" type="text" value="'+ae+'" id="addout" class="btn gray-btn" \/><input type="button" value="修改"  class="btn gray-btn md" id="getlinkdate"\/><input type="button" value="删除"  class="btn gray-btn de" id="getlinkdate"\/><input type="button" value="添加"  class="btn gray-btn ad" id="getlinkdate"\/><\/div><div class="linkwrap clearfix"><div class="linklist"><input style="margin-left:140px;width:400px;" type="button" value="确定" class="addin btn gray-btn" step="0" \/><ul><\/ul><\/div><\/div>';
					continue;
				}
				html += '<div class="linkoption"><input style="border:none;background-color:white;color:#6b7485;" readonly="readonly" type="text" value="'+ae+'" id="addout" class="btn gray-btn" \/><input type="button" value="修改"  class="btn gray-btn md" id="getlinkdate"\/><input type="button" value="删除"  class="btn gray-btn de" id="getlinkdate"\/><input type="button" value="添加" style="display:none;"  class="btn gray-btn ad" id="getlinkdate"\/><\/div><div class="linkwrap clearfix" style="display:none;"><div class="linklist"><input style="margin-left:140px;width:400px;" type="button" value="确定" class="addin btn gray-btn" step="0" \/><ul><\/ul><\/div><\/div>';
			}
			html += '<\/div>';
			pageb2 = $.layer({
			    type: 1,   
			    title: '配置区域',
			    border: [0],
			    closeBtn: [0],
			    shadeClose: true,
			    area: ['800px','460px'],
			    page: {
			        html: html 
			    }
			});
			return;
		} else {			
			$(this).parent().prev().html("<input value='"+$(this).parent().prev().html()+"'></input>");
		}
		$(this).html("保存");
		$(this).removeClass("modifys");
		$(this).addClass("saveModifys");
	});
	$(document).on("click",".saveModifys",function(){
		var type = $(this).attr("type");
		var id = $(this).attr("idd");
		var newSetting = "";
		if (type == "7") {
			newSetting = $(this).parent().prev().find("select").find("option:selected").html();
		} else {			
			newSetting = $(this).parent().prev().find("input").val();
			if(newSetting.length>=5){
				alert("参数长度不能超过4");
				return;
			}
			if(!isNaN(newSetting.substring(newSetting.length-1,newSetting.length))){
				alert("参数不能以数字结尾");
				return;
			}
		}
		$.ajax({
			type:"post",
			url: "sysproblem/changeModifyConfig",
			data:{id:id,newSetting:newSetting},
			dataType: "json"
		});
		$(this).parent().prev().html(newSetting);
		$(this).html("修改");
		$(this).addClass("modifys");
		$(this).removeClass("saveModifys");
	});
});










