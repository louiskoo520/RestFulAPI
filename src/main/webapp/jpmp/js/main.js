yinru('js/Constants.js');
var id = null;
$(function(){
	
	//一条属性的UniteID
	var attrId = 1;		//第一个item对应 '一类问题默认处理人';第二个item对应 '上报人'

	var userListOption = "";
	$.ajax({
		async: false,
		url:"useropera/getusers",
		type: "POST",
		dataType: "json",
		success:function(data){
			for(var i=0;i<data.length;++i){
				userListOption += '<option value="'+data[i].id+'">'+data[i].name+'</option>';
			}
			userListOption = '<select id="defaultDealer" style="margin-left: 40px" class="kindtype">'+userListOption +'</select>';
		}
	});
	
	id = getQueryString("id");
	if (id) {
		$("#problemKindName").val(unescape(getQueryString("name")));
		$.ajax({
			url: "useropera/gettypeitems",
			dataType: "json",
			data: {id: id},
			type: "post",
			success: function(data){
				$.each(data, function(index, info){
					if (info.type == 121) {
						if (info.selectColumns) {							
							addItem(1, "默认处理人");
							var tmpDealer = info.selectColumns.split(",")[0];
							$("#defaultDealer").val(tmpDealer);
							$("select#attrType1").attr("itemid", info["id"]);
						}
					} else if (info["zIndex"] != "-1") {
						++attrId;
						var tmp = '<div class="kindlist clearfix">'+
							'<div class="col fL lh28"><a href="javascript:;" id="delAttr" title="删除">'+
							'</a></div><div class="col fL lh28">'+
							'<select id="level'+attrId+'"><option value="1" selected>上报问题</option><option value="2">更新任务</option></select>'+
							'<div><div class="col fL lh28">'+
							'<input type="checkbox" id="require'+attrId+'">必填</div><div class="col fL">'+
							'<label class="tit">属性名：</label>'+
							'<input type="text" class="attrname" id="attrName'+attrId+'" value="'+info["attributeName"]+'"/>'+
							'</div><div class="col fL colselect">'+
							'<select itemid="'+info["id"]+'" id="attrType'+attrId+'" class="kindtype">'+
							'<option value="130">标题</option>'+
							'<option value="129">内容</option>'+
							'<option value="'+Constants.TASK_META_TYPE_DEALER+'">默认处理人</option>'+
							'<option value="'+Constants.TASK_META_TYPE_L3_MENU+'">多级菜单</option>'+
							'<option value="'+Constants.TASK_META_TYPE_GEO+'">地理位置</option>'+
							'<option value="'+Constants.TASK_META_TYPE_SELECT+'">选择框</option>'+
							'<option value="'+Constants.TASK_META_TYPE_TEXT+'">文本</option>'+
							'<option value="'+Constants.TASK_META_TYPE_PIC+'">照片</option>'+
							'<option value="'+Constants.TASK_META_TYPE_AUDIO+'">音频</option>'+
							'<option value="'+Constants.TASK_META_TYPE_VEDIO+'">视频</option>'+
							'<option value="'+Constants.TASK_META_TYPE_FILE+'">文件</option>'+
							'<option value="'+Constants.TASK_META_TYPE_TIMESTAMP+'">时间戳</option>'+
							'</select>'+
							'</div>';
						if (info["type"] == Constants.TASK_META_TYPE_SELECT) {
							tmp += '<div class="col fL addinfo"><input id="selectId'
								+attrId+'" class="attrname" type="text" placeholder="多个请用;分开" value="'
								+info["selectColumns"]+'"/>';
						}
						if (info["type"] == Constants.TASK_META_TYPE_L3_MENU) {
							tmp += '<div class="col fL addinfo"><input type="button" tmpId="selectId'+attrId
								+'" class="attrname abcd" title='+info["selectColumns"]+' value="编辑多级菜单"/><input id="selectId'
								+attrId+'" class="attrname" type="hidden" value='+info["selectColumns"]+'>';
						}
						tmp += '</div>';
						$("#main-item").append(tmp);
						$("select#level"+attrId).val(info["level"]);
						$("select#attrType"+attrId).val(info["type"]);
						if (info["required"] == "1") {						
							$("input#require"+attrId).attr("checked", "checked");
						}
					}
				});				
			}
		});
	} else {
		loadKindList();
	}

	//添加属性
	/**
	 * 问题类别添加
	 */
	$("#addProblembtn").click(function(){
		var problemKindName = $("#problemKindName").val();
		var userId = sessionUserInfo['id'];
		if(problemKindName == ""){
			alert("请填写类别名！");
			return;
		}
		var reqParam = {};
		reqParam.id = id;
		reqParam.problemKindName = problemKindName;
		reqParam.userId = userId;
		reqParam.items = [];
		
		for(var i=1;i<=attrId;++i){
			var item = {};
			var attrName = $("#attrName"+i).val();
			if(attrName == undefined)
				continue;
			if(attrName == ""){
				alert("请填写属性名！");
				return;
			}
			
			item.id       = $("#attrType"+i).attr("itemid");
			item.require  = $("#require"+i).attr("checked"); 
			item.attrName = $("#attrName"+i).val();
			item.attrType = $("#attrType"+i).val();
			item.zIndex   = i;
			item.level    = $("#level"+i).val();

			if(item.attrType == Constants.TASK_META_TYPE_DEALER){ //默认处理者 ID,姓名
				item.selectText = $("#defaultDealer").val()+","+$("#defaultDealer").find("option:selected").text();
			}

			if(item.attrType == Constants.TASK_META_TYPE_SELECT || item.attrType == Constants.TASK_META_TYPE_L3_MENU){
				var selectText = $("#selectId"+i).val();
				if(selectText == 0){
					alert("请填写选着框类型中的值！");
					return;
				}
				item.selectText = selectText;
			}
			reqParam.items.push(item);
		}
		var jsonStr = JSON.stringify(reqParam);

		$.ajax({
			url:"task/addCategory",
			type: "POST",
			dataType: "json",
			data:{
				param:jsonStr
			},
			success:function(data){
				if(true == data.status) {
					if (!id) {						
						alert("添加成功！");
					} else {
						alert("修改成功！");
					}
					window.location.href = "sysproblem.html";
				} else {
					if (!id) {						
						alert("添加失败！");
					} else {
						alert("修改失败！");
					}
				}
			},
			error:function(data){
				if (!id) {						
					alert("添加失败！");
				} else {
					alert("修改失败！");
				}
			}
		});
	});
	
	function addItem(itemId, value) {
		var tmp = '<div class="kindlist clearfix">'+
			'<div class="col fL lh28"><a href="javascript:;" id="delAttr" title="删除">'+
			'</a></div><div class="col fL lh28">'+
			'<select id="level'+itemId+'"><option value="1" selected>上报问题</option><option value="2">更新任务</option></select>'+
			'<div><div class="col fL lh28">'+
			'<input type="checkbox" id="require'+itemId+'" checked="true">必填</div><div class="col fL">'+
			'<label class="tit">属性名：</label>'+
			'<input type="text" class="attrname" id="attrName'+itemId+'" value="文本"/>'+
			'</div><div class="col fL colselect">'+
			'<select id="attrType'+itemId+'" class="kindtype">'+
			'<option value="130">标题</option>'+
			'<option value="129">内容</option>'+
			'<option value="'+Constants.TASK_META_TYPE_DEALER+'">默认处理人</option>'+
			'<option value="'+Constants.TASK_META_TYPE_L3_MENU+'">多级菜单</option>'+
			'<option value="'+Constants.TASK_META_TYPE_GEO+'">地理位置</option>'+
			'<option value="'+Constants.TASK_META_TYPE_SELECT+'">选择框</option>'+
			'<option value="'+Constants.TASK_META_TYPE_TEXT+'" selected>文本</option>'+
			'<option value="'+Constants.TASK_META_TYPE_PIC+'">照片</option>'+
			'<option value="'+Constants.TASK_META_TYPE_AUDIO+'">音频</option>'+
			'<option value="'+Constants.TASK_META_TYPE_VEDIO+'">视频</option>'+
			'<option value="'+Constants.TASK_META_TYPE_FILE+'">文件</option>'+
			'<option value="'+Constants.TASK_META_TYPE_TIMESTAMP+'">时间戳</option>'+
			'</select>'+
			'</div></div>';
		$("#main-item").append(tmp);
		if (value) {
			$("input#attrName"+itemId).val(value);
			$("select#attrType"+itemId+" option").each(function(){
				if ($(this).html() == value) {
					$(this).parent().val($(this).val());
					return false;
				}
			});
		}
		if (value == "默认处理人") {
			$("select#attrType"+1).parent().append(userListOption);
		}
	}
	
	function loadKindList(){
		addItem(attrId, "默认处理人");
		
		++attrId;
		addItem(attrId, "标题");
		
		++attrId;
		addItem(attrId, "内容");
		
		++attrId;
		addItem(attrId, "地理位置");
	}

	$("#addkindbtn").click(function(){
		++attrId;
		addItem(attrId);

	});
	//选择框、多级菜单
	$(".kindtype").live('change',function(){
		if ($(this).attr("id") != "defaultDealer") {			
			$(this).parent().prev().find("input").val($(this).find("option:selected").text());
		}
		$(this).each(function(){
			var attrType = $(this)[0].id;//attrType1
			var tmpAttrId = attrType.substr(8);
			var textare="<div class='col fL addinfo'><input id='selectId"+tmpAttrId+"' type='text' class='attrname' placeholder='多个请用;分开'/></div>",
				third="<div class='col fL addinfo'><input type='button' tmpId='selectId"+tmpAttrId+"' class='attrname abcd' value='编辑多级菜单'/><input id='selectId"+tmpAttrId+"' type='hidden'/></div>";			
			if($(this).val()==Constants.TASK_META_TYPE_SELECT){
				$(this).parent().next(".addinfo").remove();
				$(this).next(".kindtype").remove();
				$(this).parent().parent().append(textare);
			} else if($(this).val()==Constants.TASK_META_TYPE_L3_MENU){
				$(this).parent().next(".addinfo").remove();
				$(this).next(".kindtype").remove();
				$(this).parent().parent().append(third);
			} else if($(this).val()==Constants.TASK_META_TYPE_DEALER){
				if ($("select#defaultDealer").html()) {
					alert("默认处理人已存在！");
					$(this).val(Constants.TASK_META_TYPE_TEXT);
					$(this).parent().prev().find("input").val($(this).find("option:selected").text());
				} else {				
					$(this).parent().next(".addinfo").remove();
					$(this).parent().append(userListOption);
				}
			} else{
				$(this).parent().next(".addinfo").remove();
				$(this).next(".kindtype").remove();
				$(this).parent().next(".thirdmenu").remove();
			}
		})
	});
	$("#delAttr").live("click",function(){
		$(this).parent().parent().remove();
	});

})
