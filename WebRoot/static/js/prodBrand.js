/**
 * 添加品牌弹窗
 */
function showAddPopWin(){
	var root = $("#rootsite").val();
	$(".shade_layout").show();
	$("#addpopwin").show();
	$("#prodBrandName").val("");
	$("#fileId").val("");
	$("#photocover").attr("src",root+"/static/images/ico/icon_image.png");
	$("#prodBrandDesc").val("");
	pop_center('560');
}

/**
 * 上传品牌logo
 */
function uploadCover(){
	var root = $("#rootsite").val();
	$.ajaxFileUpload
	(
		{
			url:root+'/brand/uploadcover',
			secureuri:false,
			fileElementId:'cover',
			dataType: 'json',
			data:{},
			success: function (data, status)
			{
				if( data.success )
				{
					$("#photocover").prop("src",data.imgUrl);
					$("#fileId").attr("value",data.fileid);
					$("#photocover").show();
				} else {
					alertWarn("333");
//					alertWarn(data.errorMsg);
					closed_add_shade_layout();
					alertWarnWarn( data.errorMsg != '' ? data.errorMsg : '上传图片出错,请联系管理员');
				}
				createColverUploadHtml();
			},
			error: function (data, status, e)
			{
				//alertWarn("444");
			}
		}
	);
}
/**
 * 重新绑定onchange事件
 */
function createColverUploadHtml(){
	var $new = $('<input t="'+ new Date().getTime() +'" size="1" type="file" name="cover" id="cover" value="上传图片" style="opacity:0; filter: Alpha(Opacity=0); cursor:pointer;z-index:1;position: absolute;left: 0;top:18px;left: 163px; top: 18px; width: 89px;height: 29px;" />');
	$('#cover').replaceWith( $new );
	$new.change(uploadCover);
}

/**
 * 新增品牌
 */
function addNewBrand(){
	var root = $("#rootsite").val();
	var fileId = $("#fileId").val();
	var prodBrandName = $("#prodBrandName").val();
	var photocover = $("#photocover").attr("src");
	var prodBrandDesc = $("#prodBrandDesc").val();
	
	if(prodBrandName==""){
		alertWarn("please input the brand name!");
		return 0;
	}
	if(fileId==""){
		alertWarn("please upload the logo file!");
		return 0;
	}
	if(prodBrandDesc==""){
		alertWarn("please input the description!");
		return 0;
	}
	var url = root + "/brand/savebrand";
	$.ajax({
		type : "POST",
	//contentType : 'charset=utf-8',
	url : url,
	dataType : 'json',
	cache : false,
	data : {
		"name": prodBrandName,
		"logoUrl": photocover,
		"description":prodBrandDesc
	},
	success : function(data) {
		var jsonObjArr = JSON.parse(data);
		if(jsonObjArr.code=="1"){
			alertWarn("success!");
			goThisPage($('#currentPage').val());
			close();
		}
		else{
			alertWarn("fail,please contact the administrator");
		}
	}
	});
}
/**
 * 修改品牌弹窗
 * @param prodBrandId 品牌ID
 * @param obj DOM对象
 */
function updatePopWin(prodBrandId,obj){
	$(".shade_layout").show();
	$("#modifypopwin").show();
	var $td = $(obj).parent().parent().find("td");
	$("#prodBrandId").val(prodBrandId);
	$("#updateprodBrandName").val($td.eq(0).text());
	$("#updatephotocover").attr("src",$td.eq(1).find("img").attr("url"));
	$("#updateprodBrandDesc").val($td.eq(2).text());
	pop_center('560');
}

/**
 * 上传品牌logo
 */
function uploadUpdatedCover(){
	var root = $("#rootsite").val();
	$.ajaxFileUpload
	(
		{
			url:root+'/brand/uploadupdatedcover',
			secureuri:false,
			fileElementId:'updatecover',
			dataType: 'json',
			data:{},
			success: function (data, status)
			{
				if( data.success )
				{
					$("#updatephotocover").prop("src",data.imgUrl);
					$("#updatefileId").attr("value",data.fileid);
				} else {
					alertWarn("333");
//					alertWarn(data.errorMsg);
					closed_add_shade_layout();
					alertWarnWarn( data.errorMsg != '' ? data.errorMsg : '上传图片出错,请联系管理员');
				}
				createColverUploadHtml();
			},
			error: function (data, status, e)
			{
			//	alertWarn("444");
			}
		}
	);
}
/**
 * 重新绑定onchange事件
 */
function createColverUploadUpdateHtml(){
	var $new = $('<input t="'+ new Date().getTime() +'" size="1" type="file" name="updatecover" id="updatecover" value="上传图片" style="opacity:0; filter: Alpha(Opacity=0); cursor:pointer;z-index:1;position: absolute;left: 0;top:18px;left: 163px; top: 18px; width: 89px;height: 29px;" />');
	$('#cover').replaceWith( $new );
	$new.change(uploadCover);
}
/**
 * 修改品牌信息
 */
function modifyBrand(){
	var root = $("#rootsite").val();
	var updateprodBrandName = $("#updateprodBrandName").val();
	var updatephotocover = $("#updatephotocover").attr("src");
	var updateprodBrandDesc = $("#updateprodBrandDesc").val();
	var prodBrandId = $("#prodBrandId").val();
    if(updateprodBrandName==""){
        alertWarn("please input the brand name!");
        return 0;
    }
    if(updatephotocover==""){
        alertWarn("please upload the logo file!");
        return 0;
    }
    if(updateprodBrandDesc==""){
        alertWarn("please input the description!");
        return 0;
    }
	/*if(prodBrandName==""){
		alertWarn("please input the brand name!");
		return 0;
	}
	if(fileId==""){
		alertWarn("please upload the logo file!");
		return 0;
	}
	if(prodBrandDesc==""){
		alertWarn("please input the description!");
		return 0;
	}*/
	var url = root + "/brand/savebrand";
	$.ajax({
		type : "POST",
	//contentType : 'charset=utf-8',
	url : url,
	dataType : 'json',
	cache : false,
	data : {
		"name": updateprodBrandName,
		"logoUrl": updatephotocover,
		"description":updateprodBrandDesc,
		"prodBrandId":prodBrandId
	},
	success : function(data) {
		var jsonObjArr = JSON.parse(data);
		if(jsonObjArr.code=="1"){
			alertWarn("success!");
			goThisPage($('#currentPage').val());
			close();
		}
		else{
			alertWarn("fail,please contact the administrator");
		}
	}
	});
}
/**
 * 删除品牌
 */
function deleteBrand(prodBrandId){
	var root = $("#rootsite").val();
	$.post(root+"/brand/deleteOne", {"prodBrandId":prodBrandId},
			function(data){
			var jsonObjArr = JSON.parse(data);
			if(jsonObjArr.code=="1"){
				close();
				goThisPage($('#currentPage').val());
			}
			else if(jsonObjArr.code=="2"){
				alertWarn("Please delete Brand Street data first!");
			}
			else{
				alertWarn("error!");
			}
	});
}