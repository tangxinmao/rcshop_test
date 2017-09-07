function login(){
	var root = $("#rootsite").val();
	var username = $("#username").val();
	var password = $("#password").val();
	var verificationCode=$('#verificationCode').val()
	if(verificationCode=="" || verificationCode==null){
		alert("code not allow null！");
		$("#username").focus();
		return 0;
	}
	if(username=="" || username==null){
		alert("用户名不能为空！");
		$("#username").focus();
		return 0;
	}
	if(password=="" || password==null){
		alert("密码不能为空！");
		$("#password").focus();
		return 0;
	}
	$.ajax({
		type : "POST",
	//contentType : 'charset=utf-8',
	url : $('#login_form').attr('action'),
	dataType : 'json',
	cache : false,
	data : {
		"memberAccount": username,
		"memberPwd":password,
		"verificationCode":verificationCode
	},
	success : function(data) {
		var jsonObjArr = JSON.parse(data);
		if(jsonObjArr.code=="1"){
			self.location=$("#rootsite").val()+"/manager/main";
		}
		else{
			  var time = new Date().getTime();
		      document.getElementById("imagecode").src = root+"/loginRegister/code?d=" + time;
			alert(jsonObjArr.result);
		}
	}
});
}
function uploadicon(){
	var root = $("#rootsite").val();
	$.ajaxFileUpload
	(
		{
			url:root+'/uploadicon',
			secureuri:false,
			fileElementId:'cover',
			dataType: 'json',
			data:{"userId":$("#userId").val()},
			success: function (data, status)
			{
				if( data.success )
				{
					$("#photocover").prop("src",data.imgUrl);
					$("#imageId").prop("value",data.fileid);
					$("#photocover").show();
				} else {
					alert("333");
//					alert(data.errorMsg);
					closed_add_shade_layout();
					alertWarn( data.errorMsg != '' ? data.errorMsg : '上传图片出错,请联系管理员');
				}
				createIconUploadHtml();
			},
			error: function (data, status, e)
			{
				//alert("444");
			}
		}
	);
}
function useradd(){
	var userName = $("#userName").val();
	var userAccount = $("#userAccount").val();
	var userPassword = $("#userPassword").val();
	var userSex = $("#userSex").val();
	var userId = $("#userId").val();
	var imageId = $("#imageId").val();
	var userMark = $("#userMark").val();
	$.ajax({
		type : "POST",
	//contentType : 'charset=utf-8',
	url : "beginadduser",
	dataType : 'json',
	cache : false,
	data : {
		"userName": userName,
		"userAccount":userAccount,
		"userPassword": userPassword,
		"userSex":userSex,
		"userId": userId,
		"imageId":imageId,
		"userMark":userMark
	},
	success : function(data) {
		var jsonObjArr = JSON.parse(data);
		if(jsonObjArr.code=="1"){
			alert("添加成功！");
		}
		else{
			alert(jsonObjArr.result);
		}
	}
});
}
function createIconUploadHtml(){
	var $new = $('<input t="'+ new Date().getTime() +'" onclick="uploadicon()" size="1" type="file" name="cover" id="cover" class="upload link_btn" value="上传图片" style="opacity:0; filter: Alpha(Opacity=0); cursor:pointer;z-index:1;position: absolute;left: 0;top:-17px ;left: -25px; width: 88px;height: 29px;" />');
	$('#cover').replaceWith( $new );
	$new.change(uploadImage);
}
$('#imagecode').click(function(){
	var root = $("#rootsite").val();
	  var time = new Date().getTime();
      document.getElementById("imagecode").src = root+"/loginRegister/code?d=" + time;
})