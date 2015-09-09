$(function(){
	//一条属性的UniteID
	var attrId = 1;		//第一个item对应 '一类问题默认处理人';第二个item对应 '上报人'

	$('.moremenu').hover(function(){
		$(this).addClass('hovermenu')
	},function(){
		$(this).removeClass('hovermenu')	
	})
	//添加属性
	/**
	 * 问题类别添加
	 */
	$("#addProblembtn").click(function(){
		var problemKindName = $("#problemKindName").val();
		if(problemKindName == ""){
			alert("请填写类别名！");
			return;
		}
		var reqParam = {};
		reqParam.problemKindName = problemKindName;
		reqParam.items = [];

		for(var i=1;i<=attrId;++i){
			var item = {};
			var attrName = $("#attrName"+i).val();
			if(attrName == undefined && i != 1)
				continue;
			if(attrName == ""){
				alert("请填写属性名！");
				return;
			}
			
			item.require  = $("#require"+i).attr("checked"); 
			item.attrName = $("#attrName"+i).val();
			item.attrType = $("#attrType"+i).val();
			item.zIndex = i;

			if(i == 1){ //默认处理者 ID,姓名
				item.selectText = $("#defaultDealer").val()+","+$("#defaultDealer").find("option:selected").text();
			}

			if(item.attrType == Constants.TASK_META_TYPE_SELECT){
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
			url:"../task/addCategory",
			type: "POST",
			dataType: "json",
			data:{
				param:jsonStr
			},
			success:function(data){
				alert(data.status)
				if( true == data.status)
					alert("添加成功！");
				else
					alert("添加失败！");

			},
			error:function(data){
				alert("添加失败！");
			}
		});
	});

	var userListOption = "";
	$.ajax({
			url:"../user/listUser",
			type: "POST",
			dataType: "json",
			success:function(data){
				for(var i=0;i<data.length;++i){
					userListOption += '<option value="'+data[i].id+'">'+data[i].name+'</option>';
				}
				if(attrId == 1){
					var tmp = '<div class="kindlist clearfix">'+
					'<div class="col fL lh28"></div><div class="col fL lh28">'+
					'<input type="checkbox" id="require1" checked="true" disabled>必填</div><div class="col fL">'+
					'<label class="tit">属性名：</label>'+
					'<input type="text" class="attrname" id="attrName1" value="默认处理人"/>'+
					'</div><div class="col fL colselect">'+
					'<select id="attrType1" class="kindtype">'+
						'<option value="'+Constants.TASK_META_TYPE_DEALER+'">默认处理人</option>'+
					'</select>'+
					'<select id="defaultDealer" class="kindtype">'+
						userListOption +
					'</select>'+
					'</div></div>';
					$("#main-item").append(tmp);
					++attrId;
					
					tmp = '<div class="kindlist clearfix">'+
					'<div class="col fL lh28"></div><div class="col fL lh28">'+
					'<input type="checkbox" id="require2" checked="true" disabled>必填</div><div class="col fL">'+
					'<label class="tit">属性名：</label>'+
					'<input type="text" class="attrname" id="attrName2" value="上报人"/>'+
					'</div><div class="col fL colselect">'+
					'<select id="attrType2" class="kindtype">'+
						'<option value="'+Constants.TASK_META_TYPE_REPORTER+'">上报人</option>'+
					'</select>'+
					'</div></div>';
					$("#main-item").append(tmp);

					++attrId;
					
					tmp = '<div class="kindlist clearfix">'+
					'<div class="col fL lh28"></div><div class="col fL lh28">'+
					'<input type="checkbox" id="require3" checked="true" disabled>必填</div><div class="col fL">'+
					'<label class="tit">属性名：</label>'+
					'<input type="text" class="attrname" id="attrName3" value="任务标题"/>'+
					'</div><div class="col fL colselect">'+
					'<select id="attrType3" class="kindtype">'+
						'<option value="130">任务标题</option>'+
					'</select>'+
					'</div></div>';
					$("#main-item").append(tmp);

					++attrId;
					
					tmp = '<div class="kindlist clearfix">'+
					'<div class="col fL lh28"></div><div class="col fL lh28">'+
					'<input type="checkbox" id="require4" checked="true" disabled>必填</div><div class="col fL">'+
					'<label class="tit">属性名：</label>'+
					'<input type="text" class="attrname" id="attrName4" value="任务内容"/>'+
					'</div><div class="col fL colselect">'+
					'<select id="attrType4" class="kindtype">'+
						'<option value="129">任务内容</option>'+
					'</select>'+
					'</div></div>';
					$("#main-item").append(tmp);

					++attrId;
					
					tmp = '<div class="kindlist clearfix">'+
					'<div class="col fL lh28"></div><div class="col fL lh28">'+
					'<input type="checkbox" id="require5" checked="true" disabled>必填</div><div class="col fL">'+
					'<label class="tit">属性名：</label>'+
					'<input type="text" class="attrname" id="attrName5" value="发生地点"/>'+
					'</div><div class="col fL colselect">'+
					'<select id="attrType5" class="kindtype">'+
						'<option value="92">发生地点</option>'+
					'</select>'+
					'</div></div>';
					$("#main-item").append(tmp);
				}
			},
			error:function(data){
			}
		});

	$("#addkindbtn").click(function(){
		++attrId;
		var AttrBody='<div class="kindlist clearfix">'+
		'<div class="col fL lh28"><a href="javascript:;" id="delAttr" title="删除">'+
		'</a></div><div class="col fL lh28">'+
		'<input type="checkbox" id="require'+attrId+'">必填</div><div class="col fL">'+
		'<label class="tit">属性名：</label>'+
		'<input type="text" class="attrname" id="attrName'+attrId+'"/>'+
		'</div><div class="col fL colselect">'+
		'<select id="attrType'+attrId+'" class="kindtype">'+
			'<option value="'+Constants.TASK_META_TYPE_L3_MENU+'" selected="selected">三级菜单</option>'+
			'<option value="'+Constants.TASK_META_TYPE_GEO+'">地理位置</option>'+
			'<option value="'+Constants.TASK_META_TYPE_SELECT+'">选择框</option>'+
			'<option value="'+Constants.TASK_META_TYPE_TEXT+'" selected>文本</option>'+
			'<option value="'+Constants.TASK_META_TYPE_PIC+'">照片</option>'+
			'<option value="'+Constants.TASK_META_TYPE_AUDIO+'">音频</option>'+
			'<option value="'+Constants.TASK_META_TYPE_VEDIO+'">视频</option>'+
			'<option value="'+Constants.TASK_META_TYPE_FILE+'">文件</option>'+
			'<option value="'+Constants.TASK_META_TYPE_TIMESTAMP+'">时间戳</option>'+
		'</select></div></div>';
		$(this).parent().parent().parent().append(AttrBody);

	});
	//选择框、三级菜单
	$(".kindtype").live('change',function(){
		$(this).each(function(){
			var attrType = $(this)[0].id;//attrType1
			var tmpAttrId = attrType.substr(8);
			var textare="<div class='col fL addinfo'><input id='selectId"+tmpAttrId+"' type='text' class='attrname' placeholder='多个请用；分开'/></div>",
				third="<div class='col fL thirdmenu' id='sample'></div>";
			if($(this).val()==Constants.TASK_META_TYPE_SELECT){
				$(this).parent().parent().append(textare);
				$(this).parent().next(".thirdmenu").remove();
			}
			else if($(this).val()==Constants.TASK_META_TYPE_L3_MENU){
				$(this).parent().parent().append(third);
				$(this).parent().next(".addinfo").remove();
				loadLinkageData('sample', sampleData);
			}
			else{
				$(this).parent().next(".addinfo").remove();
				$(this).parent().next(".thirdmenu").remove();
			}
		})
	});
	$("#delAttr").live("click",function(){
		$(this).parent().parent().remove();
	})
	$(".uploadimglist li").hover(function(){
		$(this).children(".imgoperate").stop().fadeIn();
	},function(){
		$(this).children(".imgoperate").stop().fadeOut();
	})
	

})
