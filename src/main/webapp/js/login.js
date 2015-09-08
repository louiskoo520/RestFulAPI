$(document).ready(function(){
	$(".sbtn").click(function(){
		var userName = $("input[name='userName']").val();
		var passwd = $("input[name='passwd']").val();
		if(userName==""||userName==null){
			alert("鐢ㄦ埛鍚嶄笉鑳戒负绌�");
			return;
		}
		if(passwd==""||passwd==null){
			alert("瀵嗙爜涓嶈兘涓虹┖");
			return;
		}
		$.ajax({
			type:"post",
			url:"user/login",
			data:{userName:userName,passwd:passwd,param:"server"},
			dataType:"html",
			success:function(data){
				if(data==1){
					alert("鐢ㄦ埛鍚嶄笉瀛樺湪");
				}else if(data==2){
					alert("瀵嗙爜涓嶆纭�");
				}else{
					data = eval("("+data+")");
					window.location.href = "index.html";
				}
			}
		});
		return false;
	});
	
	/*
	 * 鐐瑰嚮浜嬩欢,鏄剧ず浜岀淮鐮�
	 */
	 $(".erweima-a").click(function(){
		 $(".saomiao").show();
		 $(".login_box").hide();
	 });
	 
	 
	 /*
	  *
	  */
	  
	  $(".saomiao .del-a").click(function(){
		  $(".saomiao").hide();
		 $(".login_box").show();
		  
		  
		});
});






























