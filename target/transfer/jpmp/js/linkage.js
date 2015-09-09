var linkid = 1 ,step = 0, parentId = 0, pagei, pages, pageb;
$(function(){
	//点击触发多级菜单
	$("body").on("click", ".abcd", function(){
		pageb = $.layer({
		    type: 1,   
		    title: '添加多级分类',
		    border: [0],
		    closeBtn: [0],
		    shadeClose: true,
		    area: ['800px','460px'],
		    page: {
		        html: '<\div class="linkbody clearfix"><div class="linkoption"><input type="button" value="添加下级大类" id="addout" class="btn gray-btn" \/><input type="button" value="保存"  class="btn gray-btn" id="getlinkdate"\/><\/div><div class="linkwrap clearfix"><div class="linklist"><input type="button" value="添加类别" class="addin btn gray-btn" step="0" \/><ul><\/ul><\/div><\/div><\/div>' 
		    }
		});
		//通过title属性读取已存在的多级菜单数据
		var title = eval($(this).attr("title"));
		if (title) {			
			var temStep = 0;
			$.each(title, function(index, info){
				temStep = info.idIndex.split(",").length - 1;
				if (!$(".addin[step="+temStep+"]").val()) {
					$(".linkwrap").append("<div class='linklist'><input class='addin btn gray-btn' type='button' step='"
							+ temStep+"' value='添加类别'></input><a class='delkind' title='删除分类'></a><ul></ul></div>");
				}
				var temParent = "";
				if ($("li#"+info.parentId).attr("name")) {
					temParent = "("+$("li#"+info.parentId).attr("name")+")";
				}
				$(".addin[step="+temStep+"]").parent().find("ul").append('<li id="'+info.key+'" name="'+info.name+'" idindex="'+info.idIndex
						+ '" parentid="'+info.parentId+'"><a></a><span class="innerTxt">'
						+ info.name+temParent+'</span></li>');
			});
		}
	});
	$("body").on("click",".linklist ul li a",function(){//删除分类及子分类
		var parentThis = $(this);
		var attrArr , attrSingle , delLi;
		var level = $(this).parent().attr("idIndex").split(",").length - 1;
		var thisattr = $(this).parent("li").attr("id");
		$(".linklist li").each(function(){
			delLi = $(this);
			attrSingle = $(this).attr("idindex");
			attrArr = attrSingle.split(",");
			if(attrArr[level] && (thisattr == attrArr[level])){
				delLi.remove();
			}
		})
			parentThis.parent("li").remove();
	})
	$("body").on("click",".linklist a.delkind",function(){//删除大分类
		var thisNextAll = $(this).parent().nextAll(".linklist");
		if(thisNextAll.length !="0"){
			thisNextAll.remove();
			$(this).parent().remove();
		}else{
			$(this).parent().remove();
		}
	})
	$("body").on("click", ".addin", function(){
		step = $(this).attr("step");
		if (step != "0") {
			pages = $.layer({
			    type: 1,   
			    title: '新增',
			    border: [0],
			    closeBtn: [0],
			    shadeClose: true,
			    area: ['410px','200px'],
			    page: {
			        html: '<\div class="diabox"><\div class="selectbox">父类别名称：<\select><\/select><\/div><\div class="diaboxinner">'+
			        '<input placeholder="key" type="text" \/><input placeholder="value" type="text" \/><button class="subrecord btn gray-btn">提交<\/button><\/div><\/div>' 
			    }
			})
			$("div.linklist").eq(parseInt(step)-1).find("li").each(function(){
				$(".diabox select").append('<option id='+$(this).attr('id')+' parentid='+$(this).attr('parentid')+' idindex='+$(this).attr('idindex')+' value='+$(this).attr('name')+'>'+$(this).attr('name')+'</option>')
			});
		}else{
			pagei = $.layer({
			    type: 1,   
			    title: '新增',
			    border: [0],
			    closeBtn: [0],
			    shadeClose: true,
			    area: ['410px','260px'],
			    page: {
			        html: '<\div class="diabox"><\div class="diaboxinner"><input placeholder="key" type="text" \/>'+
			        '<input placeholder="value" type="text" \/><button class="subrecord btn gray-btn">提交<\/button><\/div><\/div>' 
			    }
			});
		}
	});
	$("body").on("click","#addout",function(){
		var prevLen = $(this).parent(".linkoption").next(".linkwrap").children(".linklist:last-child").children("ul").children("li").length;
		if(prevLen != "0"){
			$(".linkwrap").append("<div class='linklist'><input type='button' value='添加类别' class='addin btn gray-btn' step="+$("div.linklist").length+" /><a class='delkind' title='删除分类'></a><ul></ul></div>");
		}else{
			layer.tips('先添加父类', ".linklist:last-child", {guide: 2, time: 1});
		}
	});
	$("body").on("click", ".subrecord", function(){
		var key = $(this).prev().prev().val().trim();
		var instxt = $(this).prev().val().trim();
		var instxt2 = $(this).prev();
		var idindex = $(".diabox select").find("option:selected").attr("idindex");
		var parent = $(".diabox select").find("option:selected").text();
		var parentId = $(".diabox select").find("option:selected").attr("id");
		var thishtml, datacor=true;
		if (!key) {
			datacor = false;
			layer.tips('key不能为空或值重复', $(".subrecord").prev() , {guide: 2, time: 1});
			return;
		}
		if (!instxt) {
			datacor = false;
			layer.tips('值不能为空或值重复', $(".subrecord").prev() , {guide: 2, time: 1});
			return;
		} 
		$("div.linklist").find("ul li").each(function(){
			thishtml = $(this).attr("id");
			if(key == thishtml){
				datacor=false;
				layer.tips('key不能为空或值重复', $(".subrecord").prev() , {guide: 2, time: 1});
				return false;
			}
		})
		$("div.linklist").eq(step).find("ul li span.innerTxt").each(function(){
			thishtml = $(this).html();
			if(instxt == thishtml){
				datacor=false;
				layer.tips('值不能为空或值重复', $(".subrecord").prev() , {guide: 2, time: 1});
				return false;
			}
		})
		if(datacor){
			if (!idindex) {
				$("div.linklist").eq(step).find("ul").append("<li id='"+key+"' parentid='"+step+"' idindex='"+key+"' name='"+instxt+"'>"+"<a></a>"+"<span class='innerTxt'>"+instxt+"</span>"+"</li>");
			} else {
				$("div.linklist").eq(step).find("ul").append("<li id='"+key+"' parentid='"+parentId+"' idindex='"+idindex+","+key+"' name='"+instxt+"'>"+"<a></a>"+"<span class='innerTxt'>"+instxt+"</span>"+"("+parent+")"+"</li>");
			}
			layer.close(pagei);
			layer.close(pages);
		}
	});
	$("body").on("click","#getlinkdate",function() {
		var new_json="";
		var _json = "";
		$.each($(".linklist ul li"), function() {
			_json += "{'key':'"+ $(this).attr('id') +"','parentId':" +"'"+ $(this).attr('parentid')+ "'"+",'idIndex':'"+$(this).attr("idindex") +"','name':'"+$(this).attr("name")+"'},";
		});
		if (_json != "") {
			_json = _json.substring(0, _json.length-1);
		}
		new_json = '['+_json+']';
		var tmpId = $(".abcd").attr("tmpId");
		$("#"+tmpId).val(new_json);
		layer.close(pageb);
	})
});