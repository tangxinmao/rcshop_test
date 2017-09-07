//上传图片
function uploadImage(flag){
	var root = $("#rootsite").val();
	$.ajaxFileUpload
	(
		{
			url:root+'/uploadimage',
			secureuri:false,
			fileElementId:'image',
			dataType: 'json',
			data:{"msgId":$("#msgId").val()},
			success: function (data, status)
			{
				if( data.success )
				{
					CKEDITOR.instances['msgContent'].insertHtml("<img style='max-width: 260px; height: auto;' src="+data.imgUrl+">");
				} else {
					alert("333");
//					alert(data.errorMsg);
					closed_add_shade_layout();
					alertWarn( data.errorMsg != '' ? data.errorMsg : '上传图片出错,请联系管理员');
				}
				if(flag=="new")
					createNewUploadHtml();
				else
					createupdateUploadHtml();
			},
			error: function (data, status, e)
			{
				alert("444");
			}
		}
	);
}
//上传图片
function uploadCover(){
	var root = $("#rootsite").val();
	$.ajaxFileUpload
	(
		{
			url:root+'/uploadcover',
			secureuri:false,
			fileElementId:'cover',
			dataType: 'json',
			data:{"msgId":$("#msgId").val()},
			success: function (data, status)
			{
				if( data.success )
				{
					$("#photocover").prop("src",data.imgUrl);
					$("#photocover").show();
				} else {
					alert("333");
//					alert(data.errorMsg);
					closed_add_shade_layout();
					alertWarn( data.errorMsg != '' ? data.errorMsg : '上传图片出错,请联系管理员');
				}
				createColverUploadHtml();
			},
			error: function (data, status, e)
			{
				alert("444");
			}
		}
	);
}
//重新绑定onchange事件
function createNewUploadHtml(){
	var $new = $('<input t="'+ new Date().getTime() +'" size="1" type="file" name="image" id="image" class="upload link_btn" value="上传图片" style="opacity:0; filter: Alpha(Opacity=0); cursor:pointer;z-index:1;position: absolute;left: 0; top: 0; width: 89px;height: 29px;" />');
	$('#image').replaceWith( $new );
	$new.change(uploadImage);
}
//重新绑定onchange事件
function createupdateUploadHtml(){
	var $new = $('<input t="'+ new Date().getTime() +'" size="1" type="file" name="image" id="image" class="upload link_btn" value="上传图片" style="opacity:0; filter: Alpha(Opacity=0); cursor:pointer;z-index:1;position: absolute;left: -16px;width: 87px;top: -26px;" />');
	$('#image').replaceWith( $new );
	$new.change(uploadImage);
}
//重新绑定onchange事件
function createColverUploadHtml(){
	var $new = $('<input t="'+ new Date().getTime() +'" size="1" type="file" name="image" id="image" class="upload link_btn" value="上传图片" style="opacity:0; filter: Alpha(Opacity=0); cursor:pointer;z-index:1;position: absolute;left: 0;top:0;left: 0; top: 25px; width: 88px;height: 29px;" />');
	$('#image').replaceWith( $new );
	$new.change(uploadCover);
}
function addMessage(){
	var title = $("#msgTitle").val();
	var content = CKEDITOR.instances['msgContent'].getData();
	var msgId = $("#msgId").val();
	var url = $("#rootsite").val()+"/addMsg";
	if(title=="" || title==null){
		prompt("文章标题不能为空！");
		$("#title").focus();
		return 0;
	}
	if(content=="" || content==null){
		prompt("文章内容不能为空！");
		$("#msgContent").focus();
		return 0;
	}
	$.ajax({
		type : "POST",
	//contentType : 'charset=utf-8',
	url : url,
	dataType : 'json',
	cache : false,
	data : {
		"msgTitle": title,
		"msgContent":content,
		"msgId":msgId
	},
	success : function(data) {
		var jsonObjArr = JSON.parse(data);
		if(jsonObjArr.code=="1"){
			alert("添加成功!");
		}
		else{
			alert("添加失败，请联系系统管理员！");
		}
	}
});
}
function removeMsg(msgId){
	if(confirm("是否确认删除")){
		var url = $("#rootsite").val()+"/msg/remove";
		$.ajax({
				type : "POST",
			//contentType : 'charset=utf-8',
			url : url,
			dataType : 'json',
			cache : false,
			data : {
				"msgId": msgId
			},
			success : function(data) {
				var jsonObjArr = JSON.parse(data);
				if(jsonObjArr.code=="1"){
					alert("删除成功!");
					currentPage =  $("#currentPage").val();
					goThisPage(currentPage);
				}
				else{
					alert("删除失败，请联系系统管理员！");
				}
			}
		});
	}
}
function updateMsg(){
	var title = $("#msgTitle").val();
	var content = CKEDITOR.instances['msgContent'].getData();
	var msgId = $("#msgId").val();
	var url = $("#rootsite").val()+"/beginmodifymsg";
	if(title=="" || title==null){
		alert("文章标题不能为空！");
		$("#title").focus();
		return 0;
	}
	if(content=="" || content==null){
		alert("文章内容不能为空！");
		$("#msgContent").focus();
		return 0;
	}
	$.ajax({
		type : "POST",
	//contentType : 'charset=utf-8',
	url : url,
	dataType : 'json',
	cache : false,
	data : {
		"msgTitle": title,
		"msgContent":content,
		"msgId":msgId
	},
	success : function(data) {
		var jsonObjArr = JSON.parse(data);
		if(jsonObjArr.code=="1"){
			alert("修改成功!");
			self.location=$("#rootsite").val()+"/msgview";
		}
		else{
			alert("修改失败，请联系系统管理员！");
		}
	}
});
}
function saveDraft(){
	var title = $("#msgTitle").val();
	var content = encodeURIComponent(CKEDITOR.instances['msgContent'].getData());
	var url = $("#rootsite").val()+"/savedraft?time=" + new Date().getTime();
	if(title==""){
		alert("标题不能为空！");
		return 0;
	}
	if(content == ""){
		alert("内容不能为空！");
		return 0;
	}
	$.ajax({
		type : "POST",
	//contentType : 'charset=utf-8',
	url : url,
	dataType : 'json',
	cache : false,
	data : {
		"title": title,
		"content": content
	},
	success : function(data) {
		var jsonObjArr = JSON.parse(data);
		if(jsonObjArr.code=="1"){
			alert("保存到草稿箱成功!");
			
		}
		else{
			alert("失败，请联系系统管理员！");
		}
	}
	});
}
function getDraft(){
	if(confirm("是否确认读取草稿箱内容，点击确定后，当前的编辑的内容将会被替换！")){
		var url = $("#rootsite").val()+"/getdraft?time=" + new Date().getTime();
		$.ajax({
			type : "POST",
		//contentType : 'charset=utf-8',
		url : url,
		dataType : 'json',
		cache : false,
		data : {
		},
		success : function(data) {
			var jsonObjArr = JSON.parse(data);
			$("#msgTitle").val(jsonObjArr.title);
			CKEDITOR.instances['msgContent'].setData(decodeURIComponent(jsonObjArr.content));
		}
		});
	}
}
/**
 * 推送-广播
 */
function pushBroadcast(){
	var device = $("#device").val();
	var title = $("#title").val();
	var content = $("#content").val();
	if(device == "安卓" && title==""){
		alert("标题不能为空！");
		$("#title").focus();
		return 0;
	}
	if(content==null || content==""){
		alert("消息内容不能为空！");
		$("#content").focus();
		return 0;
	}
	var url = $("#rootsite").val()+"/beginpushBroadcast?time=" + new Date().getTime();
	$.ajax({
		type : "POST",
	//contentType : 'charset=utf-8',
	url : url,
	dataType : 'json',
	cache : false,
	data : {
		"device": device,
		"title": title,
		"content":content
	},
	success : function(data) {
		var jsonObjArr = JSON.parse(data);
		if(jsonObjArr.code=="1"){
			alert("发布成功!");
			
		}
		else{
			alert("发布失败，请联系系统管理员！");
		}
	}
});
}
function addPersionalMsg(){
	var msgId = $("#msgId").val();
	var userId = $("#userId").val();
	var msgContent = $("#msgContent").val();
	var fontColor = $("#fontColor").val();
	var url = $("#rootsite").val()+"/beginaddpersonalMsg?time=" + new Date().getTime();
	$.ajax({
		type : "POST",
	//contentType : 'charset=utf-8',
	url : url,
	dataType : 'json',
	cache : false,
	data : {
		"msgId": msgId,
		"userId": userId,
		"fontColor":fontColor,
		"msgContent":msgContent
	},
	success : function(data) {
		var jsonObjArr = JSON.parse(data);
		if(jsonObjArr.code=="1"){
			alert("发布成功!");
			
		}
		else{
			alert("发布失败，请联系系统管理员！");
		}
	}
});
}
function popup(msgId){
	$(".shade_layout").show();
	$("#readnumwin").show();
	$("#popup_btn_con").attr("myvalue",msgId);
	pop_center ('860');
}
function close(){
	$(".shade_layout").hide();
	$(".pop_windows").hide();
}
function addreadnum(){
	var readnum = $("#readnumber").val();
	var msgId = $("#popup_btn_con").attr("myvalue");
	var regInt = /^[0-9]+[0-9]*]*$/;  //正整数
	if(readnum==""){
		alert("请输入数量！");
		return 0;
	}
	if(!regInt.test(readnum)){
		alert("请输入正整数！");
		return 0;
	}
	$.post("addreadnum", {"readnum":readnum,"msgId":msgId},
			function(data){
		var jsonObjArr = JSON.parse(data);
			if(jsonObjArr.code=="1"){
				close();
				goThisPage($('#currentPage').val());
			}
			else{
				alert("服务器故障！");
			}
	});
}
function commentspopup(msgId){
	$(".shade_layout").show();
	$("#commentswin").show();
	$("#popup_cmt_con").attr("myvalue",msgId);
	pop_center ('560');
}
function openuserwindow(){
	var iTop = (window.screen.availHeight-30-350)/2;
	var iLeft = (window.screen.availWidth-10-500)/2; 
	window.open ($("#rootsite").val()+'/useraddedlist','newwindow','height=600,width=800,top='+iTop+',left='+iLeft+',toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no')
}
function addcmt(){
	var msgId = $("#popup_cmt_con").attr("myvalue");
	var userId = $("#userId").val();
	var cmtContent = $("#cmtContent").val();
	if(userId=="" || userId==null){
		alert("请先选择一个用户！");
		return 0;
	}
	if(cmtContent==""||cmtContent==null){
		alert("请填写评论内容！");
		return 0;
	}
	$.post("addcomments", {"msgId":msgId,"userId":userId,"cmtContent":cmtContent},
			function(data){
			var jsonObjArr = JSON.parse(data);
			if(jsonObjArr.code=="1"){
				alert("评论成功！");
				close();
				goThisPage($('#currentPage').val());
			}
			else{
				alert(jsonObjArr.result);
			}
	});
}

$(function() {
	$(".ico_close").click(function(){
		$(".shade_layout").hide();
		$(".pop_windows").hide();
	});
//	$("#table").find("tr").click(function(){
//		var userId = $(this).find("input[name=userId]").val();
//		window.opener.document.getElementById("userId").value = userId;
////		self.opener=null;
////		self.close();
//	});
});
function choose(obj){
	var userId =$(obj).find("input[name=userId]").val();
	var userName =$(obj).find("input[name=userName]").val();
	window.opener.document.getElementById("userId").value = userId;
	window.opener.document.getElementById("userName").value = userName;
	window.close();
}
//$(document).delegate($("#table").find("tr"),"click",function(){
//	var userId = $(this).find("input[name=userId]").val();
//	window.opener.document.getElementById("userId").value = userId;
//	//window.close();
//});
