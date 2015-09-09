$(document).ready(function(){
	var metaId = getQueryString("metaId");
	$.ajax({
		type:"post",
		url:"meta/metaView",
		data:{id:metaId},
		async:false,
		success:function(data){
			var meta = data[0];
			$("#metaId").html(meta.id);
			$("#metaName").html(meta.taskMetaName);
			$("#crateUser").html(meta.userName);
			$("#createTime").html(meta.createTime);
		}
	});
});
























