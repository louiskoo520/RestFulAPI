/**
 * 调用方式
 * <div id='sample'></div>
 * loadLinkageData('sample', data);
 */
var sampleData = [
		{'id':1,'idIndex':'1','parentId':0,'name':'物联网','isParent':1},
		{'id':2,'idIndex':'1,2','parentId':1,'name':'行业','isParent':1},
		{'id':3,'idIndex':'1,2,3','parentId':2,'name':'产品','isParent':1},
		{'id':4,'idIndex':'1,2,4','parentId':2,'name':'应用','isParent':1},
		{'id':5,'idIndex':'1,2,3','parentId':3,'name':'RFID','isParent':2},
		{'id':6,'idIndex':'1,2,3','parentId':3,'name':'传感网','isParent':2},
		{'id':7,'idIndex':'1,2,3','parentId':3,'name':'条码识别','isParent':2},
		{'id':8,'idIndex':'1,2,3','parentId':3,'name':'生物识别','isParent':2},
		{'id':9,'idIndex':'1,2,3','parentId':3,'name':'多媒体成像','isParent':2},
		{'id':10,'idIndex':'1,2,3','parentId':3,'name':'实时定位','isParent':2},
		{'id':11,'idIndex':'1,2,3','parentId':3,'name':'感应终端','isParent':2},
		{'id':12,'idIndex':'1,2,3','parentId':3,'name':'无线通信','isParent':2},
		{'id':13,'idIndex':'1,2,3','parentId':3,'name':'有线通信','isParent':2},
		{'id':14,'idIndex':'1,2,3','parentId':3,'name':'测量仪器','isParent':2},
		{'id':15,'idIndex':'1,2,3','parentId':3,'name':'物联网应用','isParent':2},
		{'id':16,'idIndex':'1,2,3','parentId':3,'name':'智能终端','isParent':2},
		{'id':17,'idIndex':'1,2,4','parentId':4,'name':'办公','isParent':2},
		{'id':18,'idIndex':'1,2,4','parentId':4,'name':'安防','isParent':2},
		{'id':19,'idIndex':'1,2,4','parentId':4,'name':'交通','isParent':2},
		{'id':20,'idIndex':'1,2,4','parentId':4,'name':'城市','isParent':2},
		{'id':21,'idIndex':'1,2,4','parentId':4,'name':'建筑','isParent':2},
		{'id':22,'idIndex':'1,2,4','parentId':4,'name':'其它','isParent':2},
		{'id':23,'idIndex':'1,2,23','parentId':2,'name':'实验设备','isParent':1},
		{'id':24,'idIndex':'1,2,23','parentId':23,'name':'传感器无线通讯','isParent':2},
		{'id':25,'idIndex':'1,2,23','parentId':23,'name':'沙盘展示','isParent':2},
		{'id':26,'idIndex':'1,2,23','parentId':23,'name':'平台运行诊断','isParent':2},
		{'id':27,'idIndex':'1,2,23','parentId':23,'name':'传感器及执行器测试','isParent':2},
		{'id':28,'idIndex':'1,2,23','parentId':23,'name':'电子开发仪器','isParent':2},
		{'id':35,'idIndex':'1,2,23','parentId':23,'name':'实验控制','isParent':2}
	];
function loadLinkageData(id, data) {
	if (data) {
		$.each(data, function(index, info){
			var step = info['idIndex'].split(",").length;
			if (!$("#"+id).find("select[step='"+step+"']").html()) {
				$("#"+id).append("<select step='"+step+"'><option onClick='changeSelectValue(this)' id='changeValue'></option></select>");
			}
			$("#"+id).find("select[step='"+step+"']").append("<option style='display: none' id='"+info['id']+"' isp='"+info['isParent']+"' pid='"+info['parentId']+"' idi='"+info['idIndex']+"'>"+info['name']+"</option");
		});
		$("select[step='1'] option").each(function(){
			$(this).show();
		});
	}
}
function changeSelectValue(option) {
	var step = $(option).parent().attr("step");
	editable(step);

}
function editable(step){
	var pid = 0;//父类目ID
	var pIdIndex = "";//父类目的ID索引
	if (step != "1") {//不是一级类目，获取父类目ID
		pid = $("select[step='"+(parseInt(step)-1)+"']").find("option:selected").attr("pid");
		pIdIndex = $("select[step='"+(parseInt(step)-1)+"']").find("option:selected").attr("idi");
	}
	if($("select[step='"+step+"']").val().trim() == "" && pid != null){
		console.log("sdafds");
		var name = showbox();
		if (name) {
			//todo:提交pid,pIdIndex,name到后台

		}
	}
}
function showbox(){
	$(".showboxbg").fadeIn()
	$(".showbox").show()
	$(".showboxbg").click(function(){
		$(".showboxbg").fadeOut()
		$(".showbox").hide()
	})
}
$(function(){
	$("body").on("change", "select", function(){
		var step = $(this).attr("step");
		if (step) {//隐藏子菜单的选项
			$("select").each(function(){
				var tempStep = $(this).attr("step");
				if (tempStep && parseInt(tempStep) > parseInt(step)) {
					$(this).val("");
					$(this).find("option").each(function(){
						if ($(this).html()) {							
							$(this).hide();
						}
					});
				}
			});
		}
		if ($(this).find("option:selected").attr("isp") == "1") {//调出子菜单
			var pid = $(this).find("option:selected").attr("id");
			$("select[step='"+(parseInt(step)+1)+"']").find("option").each(function(){
				if ($(this).attr("pid") == pid) {
					$(this).show();
				}
			});
		}
	});
	$("body").live("change","#changeValue",function(){
		
	})
});
