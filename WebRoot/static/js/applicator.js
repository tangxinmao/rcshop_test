
/**
 * 上传商户logo
 */
function uploadCover(){
	var root = $("#context").val();
	$.ajaxFileUpload
	(
		{
			url:root+'/merchant/applicatorUpload',
			secureuri:false,
			fileElementId:'"imagefile"',
			dataType: 'json',
			data:{},
			success: function (data, status)
			{
				if( data.success )
				{
					$("#photocover").prop("src",data.imgUrl);
					$("#memberLogo").attr("value",data.fileid);
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
/**
 * 重新绑定onchange事件
 */
function createColverUploadHtml(){
	var $new = $('<input t="'+ new Date().getTime() +'" size="1" type="file" name="cover" id="cover" class="upload link_btn" value="上传图片" style="opacity:0; filter: Alpha(Opacity=0); cursor:pointer;z-index:1;position: absolute;left: 0;top:18px;left: 163px; top: 25px; width: 88px;height: 29px;" />');
	$('#image').replaceWith( $new );
	$new.change(uploadCover);
}
/**
 * 保存商户资料
 */
function save(){
	var root = $("#rootsite").val();
	var memberId = $("#memberId").val();
	var memberAccount = $("#memberAccount").val();
	var memberNickname = $("#memberNickname").val();
	var memberLogo = $("#memberLogo").val();
	var email = $("#email").val();
	var sex = $("input:radio:checked").val();
	var memberType =$("select[name='memberType']").val();
	var mobile = $("#mobile").val();
	
	if(memberAccount==""){
		alert("please input the value about memberAccount!");
		return 0;
	}
	if(memberNickname==""){
		alert("please input the value about memberNickname!");
		return 0;
	}
	if(email==""){
		alert("please input the value about email!");
		return 0;
	}
	if(sex==""){
		alert("please check the value about sex!");
		return 0;
	}
	if(memberLogo==""){
		alert("please upload the logo file!");
		return 0;
	}
	if(mobile==""){
		alert("please input the value about mobile!");
		return 0;
	}
	if(memberType==""){
		alert("please input the value about Type!");
		return 0;
	}
	var url = root + "/memberinfo/saveMember";
	$.ajax({
		type : "POST",
	//contentType : 'charset=utf-8',
	url : url,
	dataType : 'json',
	cache : false,
	data : {
		"memberId": memberId,
		"memberAccount": memberAccount,
		"memberNickname":memberNickname,
		"memberLogo":memberLogo,
		"email":email,
		"sex":sex,
		"memberType":memberType,
		"mobile":mobile
	},
	success : function(data) {
		var jsonObjArr = JSON.parse(data);
		if(jsonObjArr.code=="1"){
			alert("success!");
			goThisPage($('#currentPage').val());
		}
		else{
			alert("fail,please contact the administrator");
		}
	}
});
}
/**
 * 弹出商户资料窗口
 * @param merchantId 商户编号
 */
function showModifyPopupWin(memberId,obj){
	$(".shade_layout").show();
	$("#popwin").show();
	$("#memberId").val(memberId);
	var $td = $(obj).parent().parent().find("td");
	$("#memberAccount").val($td.eq(1).text());
	$("#memberNickname").val($td.eq(2).text());
	var src=$td.eq(6).find("img").attr('src');
	if(src){
	$("#memberLogo").val(src.substring(src.lastIndexOf("/")+1,src.lastIndexOf(".jpg")));}
	$("#email").val($td.eq(3).text());
	$("#mobile").val($td.eq(5).text());
	   $("input:radio").each(function(i){
           if( $(this).val()==$td.eq(4).find('input').val())
                $(this).prop("checked","checked");
           else
        	    $(this).removeProp("checked");
     
            });
	$("select[name='memberType']").val($td.eq(7).find('input').val());
	$("#photocover").prop("src",$td.eq(6).find("img").attr('src'));
	pop_center ('560');
}

/**
 * 修改商户资料
 */
function modify(){
	var root = $("#rootsite").val();
	var merchantId = $("#merchantId").val();
	var merchantName = $("#merchantName").val();
	var fileId = $("#fileId").val();
	var merchantDesc = $("#merchantDesc").val();
	var mobile = $("#mobile").val();
	
	if(merchantName==""){
		alert("please input the value about merchantName!");
		return 0;
	}
	if(mobile==""){
		alert("please input the value about mobile!");
		return 0;
	}
	if(merchantDesc==""){
		alert("please input the value about merchantDesc!");
		return 0;
	}
	var url = root + "/manager/modifyMerchant"
	$.ajax({
		type : "POST",
	//contentType : 'charset=utf-8',
	url : url,
	dataType : 'json',
	cache : false,
	data : {
		"merchantId": merchantId,
		"merchantName": merchantName,
		"fileId":fileId,
		"merchantDesc":merchantDesc,
		"mobile":mobile
	},
	success : function(data) {
		var jsonObjArr = JSON.parse(data);
		if(jsonObjArr.code=="1"){
			alert("success!");
			goThisPage($('#currentPage').val());
			close();
		}
		else{
			alert("fail,please contact the administrator");
		}
	}
});
}

function deleteOne(memberId){
	if(confirm("Are you sure to delete the member? If you deleted,The data can not be restore.")){
		var root = $("#rootsite").val();
		$.post(root+"/memberinfo/deleteMember", {"memberId":memberId+''},
				function(data){
				var jsonObjArr = JSON.parse(data);
				if(jsonObjArr.code=="1"){
					alert("success!");
					close();
					goThisPage($('#currentPage').val());
				}
				else{
					alert("this data is already used");
				}
		});
	}
}
