
// 删除上传到临时文件的图片
function deleteuploadimage(e){
	var fileid = $(e).parent().find("img").attr("rel");
	$(e).parent().remove();
//	$.ajax({
//    url:'deltempimage',
//    type:'POST',
//    dataType: "text",
//    data:jQuery.param({"fileid":fileid}) + '&time='+ new Date().getTime(),
//    cache: false,
//    contentType:  'application/x-www-form-urlencoded;charset=utf-8',
//    success:function(data){
//    	var jsonObjArr = JSON.parse(data); 
//    	if(jsonObjArr.code==0){
//    	}
//    	else{
//    		$(e).parent().remove();
//    	}
//    }
//  });
}
function showTree(parentid,buz){
	dataForSend = {parentid:parentid,"merchantid":merchantid};
	$("#dirpos").attr("rel",parentid);
	$("#dirpos").attr("buz",buz);
	if(parentid=="0"){
		$("#dirpos").html("position:root");
	}
	else{
		$("#dirpos").html("position:"+$("img[rel="+parentid+"]").parent().parent().parent().find("p > a").text());
	}
	$.post("init", {"currentPage":"1","parentId":parentid},
			function(data){
			    $("#reloadSearchMessages").empty().html(data);
	});
//	$.post("queryimage",jQuery.param(dataForSend),function(json){ 
//		var jsonObjArr = JSON.parse(json);
//		$(".imglist_wrap").html("");
//		if(parentid!="0")
//			$(".imglist_wrap").html('<div class="list_item"><div class="img_con"><a href="javascript:void(0)" >' +
//					  '<img alt="" src="'+dirurl+'/static/images/sellercenter_bg/imgs/imgitem1.png"  ondbclick="$(\'.imglist_wrap\').find(\'a > img\').trigger(\'dbclick\');" buz="'+parentid+'" rel="'+buz+'" /></a></div>' +
//					  '<p><a href="javascript:void(0)">返回上一级</a></p></div>');
//		
//		for(var i in jsonObjArr){
//			var jsonObj = jsonObjArr[i];
//			if(jsonObj["FILE_TYPE"]==0){
//			$(".imglist_wrap").append('<div class="list_item"><div class="img_con"><a href="javascript:void(0)" >' +
//					  '<img alt="" src="'+dirurl+'/static/images/sellercenter_bg/imgs/imgitem1.png"  ondbclick="showTree($(this).attr("buz"),$(this).attr("rel"))" buz="'+parentid+'" rel="'+jsonObj["IMAGESPACE_ID"]+'" /></a></div>' +
//					  '<p><input type="radio" name="radio" id="radio" value="'+jsonObj["IMAGESPACE_ID"]+'"><a href="#">'+jsonObj["DIR_PATH"]+'</a></p></div>');
//			}
//			else{
//				$(".imglist_wrap").append('<div class="list_item"><div class="img_con"><a href="javascript:void(0)" >' +
//						  '<img alt="" src="'+jsonObj["URL"]+'" rel="'+jsonObj["IMAGESPACE_ID"]+'" /></a></div>' +
//						  '<p><input type="radio" name="radio" id="radio" value="'+jsonObj["IMAGESPACE_ID"]+'"><a href="javascript:void(0)">'+jsonObj["FILENAME"]+'</p></div>');
//			}
//			
//		}
//		$(".imglist_wrap").find("a > img").bind("dblclick",function(){
//			parentid = $(this).attr("rel");
//			buz=$(this).attr("buz");
//			
//			showTree(parentid,buz);
//		});	
//		
//		
////		$(".imglist_wrap").append("<div class='list_item'><div class='img_con'><a href='#'>" +
////								  "<img alt='' src='"+jsonObjArr.url+"' /></a></div>" +
////								  "<p><a href='#'></a></p></div>");
//	});
}
function sendAndRefresh(url, dataForSend,  type) {
	
	if (typeof(type) == 'undefined') {
		type = 'text';
	}
	//dataForSend.dir = encodeURIComponent(dataForSend.dir);
	jQuery.ajax({
		  type: 'POST',
		  url: url,
		  data: jQuery.param(dataForSend),
		  dataType: type,
		  contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		  beforeSend : function(xhr) {
		       xhr.setRequestHeader('Accept', "text/html; charset=utf-8");
		   },
		  success: function(data) {
			  	var jsonObjArr = JSON.parse(data);
		    	$(".upload_img_list").html(""); // 清除上传略缩图信息
		    	$("#popuploading").hide();  
		    	if(jsonObjArr.code=="-1"){
		    		alertWarn(jsonObjArr.result);
		    		return 0;
		    	}
		    	if($("#showdir").val()!=null){
					list_showListOrImg();
					return 0;
				}
				else{
					goThisPage("search");
					listdirformoving();
				}
	   }});
}
$( function(){
//	dataForSend = {parentid:"0"};
//	$.post("queryimage",jQuery.param(dataForSend),function(json){ 
//		var jsonObjArr = JSON.parse(json); 
//		$(".imglist_wrap").append("<div class='list_item'><div class='img_con'><a href='#'>" +
//								  "<img alt='' src='"+jsonObjArr.url+"' /></a></div>" +
//								  "<p><a href='#'></a></p></div>");
//	});
	$( "#beginTime" ).datepicker({
		showOtherMonths: true,
		//defaultDate: "+1w",
		changeMonth: true,
		showButtonPanel: true,
		numberOfMonths: 1,
//		beforeShow:function(){
//			$( "#rackOnStartTimeStr" ).datepicker( "option", "maxDate", new Date() );
//		},
		onClose: function( selectedDate ) {
			$( "#rackOnEndTimeStr" ).datepicker( "option", "minDate", selectedDate );
		}
	});
	$( "#endTime" ).datepicker({
		showOtherMonths: true,
		//defaultDate: "+1w",
		changeMonth: true,
		showButtonPanel: true,
		numberOfMonths: 1,
//		beforeShow:function(){
//			$( "#rackOnStartTimeStr" ).datepicker( "option", "maxDate", new Date() );
//		},
		onClose: function( selectedDate ) {
			$( "#rackOnEndTimeStr" ).datepicker( "option", "minDate", selectedDate );
		}
	});
		$("#dirpos").html("position:root");
		$("#dirpos").attr("rel","0");
	var siteMerchantUrl = $('#merchant_orderManager').val();
	$(".ico_close").click(function(){
		$("#popuploading").hide();
	});
	$(".uploadpic").click(function(){  //上传文件按钮
		$("#popuploading").show();
		$("#newfoldershow").hide();
		$("#renameshow").hide();
		$("#moveshow").hide();
		$("#uploadshow").show();
		pop_center ('540');
	});
	$(".newfolder").click(function(){  //新建文件夹按钮
		$("#newfoldername").val("");
		$("#popuploading").show();
		$("#uploadshow").hide();
		$("#renameshow").hide();
		$("#moveshow").hide();
		$("#newfoldershow").show();
		pop_center ('540');
	});
	$.post( getRootPath() + "/imageopt/listalldir",null,    
			function(data){
				var jsonObjArr = JSON.parse(data); 
				for(var i in jsonObjArr)
				$("#movepos").append('<option value="'+jsonObjArr[i].imageSpaceId+'">'+jsonObjArr[i].name+'</option>');
	});
	
	$("#newfoldersubmit").click(function(){  //新建文件夹-确定按钮
		var dir = $("#newfoldername").val();
		$("#dirpos").attr("rel","0");
		if(dir=="" || dir==null){
			alertWarn("folder name can not be null!");
			return 0;
		}
		var re =/[~#^$@%&!*]/gi;
        if (re.test(dir)) {
            alertWarn("folder name can not Contains special characters!");
            return 0;
        }
		dataForSend={"dirname":dir,"parentId":0,"merchantid":merchantid};
		sendAndRefresh("newfolder",dataForSend);
	});
	$("#renamesubmit").click(function(){  //新建文件夹-确定按钮
		var dir = $("#rename").val();
		var buzid = $("input[name=radio]:checked").val();
		if(typeof($("input[name=radio]:checked").parent().parent().find("a > img").attr("buz"))!='undefined'){
			if(dir=="" || dir==null){
				alertWarn("folder name can not be null!");
				return 0;
			}
		}
		dataForSend={"newname":dir,"buzid":buzid,"merchantid":merchantid};
		//sendAndRefresh("rename",dataForSend);
		jQuery.ajax({
			  type: 'POST',
			  url: "rename",
			  data: jQuery.param(dataForSend) + '&time='+ new Date().getTime(),
			  dataType: "text",
			  contentType : 'application/x-www-form-urlencoded;charset=utf-8',
			  beforeSend : function(xhr) {
			       xhr.setRequestHeader('Accept', "text/html; charset=utf-8");
			   },
			  success: function(data) {
				  var jsonObjArr = JSON.parse(data); 
				  if(jsonObjArr.code=="1"){
					  $("#popuploading").hide();
					  $("#rename").attr("value","");
					  $("input[name=radio]:checked").parent().find("a").html(dir);
				  }
		   }});
	});
	$("#searchbutton").click(function(){  //按文件夹or图片名称查询
		var searchkey = $("#searchkey").val();
		dataForSend={"key":searchkey,"merchantid":merchantid};
		jQuery.ajax({
			  type: 'POST',
			  url: "queryinfobykey",
			  data: jQuery.param(dataForSend) + '&time='+ new Date().getTime(),
			  dataType: "text",
			  contentType : 'application/x-www-form-urlencoded;charset=utf-8',
			  beforeSend : function(xhr) {
			       xhr.setRequestHeader('Accept', "text/html; charset=utf-8");
			   },
			  success: function(data) {
				  var jsonObjArr = JSON.parse(data); 
				  $(".imglist_wrap").html("");
				  for(var i in jsonObjArr){
						var jsonObj = jsonObjArr[i];
						if(jsonObj["FILE_TYPE"]==0){
						$(".imglist_wrap").append('<div class="list_item"><div class="img_con"><a href="javascript:void(0)" >' +
								  '<img alt="" src="'+dirurl+'/static/images/sellercenter_bg/imgs/imgitem1.png"  ondbclick="showTree($(this).attr("buz"),$(this).attr("rel"))" buz="0"  rel="'+jsonObj["IMAGESPACE_ID"]+'" /></a></div>' +
								  '<p><input type="radio" name="radio" id="radio" value="'+jsonObj["IMAGESPACE_ID"]+'"><a href="#">'+jsonObj["DIR_PATH"]+'</a></p></div>');
						}
						else{
							$(".imglist_wrap").append('<div class="list_item"><div class="img_con"><a href="javascript:void(0)">' +
									  '<img alt="" src="'+jsonObj["URL"]+'" rel="'+jsonObj["IMAGESPACE_ID"]+'" /></a></div>' +
									  '<p><input type="radio" name="radio" id="radio" value="'+jsonObj["IMAGESPACE_ID"]+'"><a href="javascript:void(0)">'+jsonObj["FILENAME"]+'</p></div>');
						}
						
					}
				  $(".imglist_wrap").find("a > img").bind("dblclick",function(){
						parentid = $(this).attr("rel");
						buz=$(this).attr("buz");
						
						showTree(parentid,buz);
					});	
		   }});
	});
	$("#inputfile").change(function(){  
		$("#path").attr("value",$("#dirpos").attr("rel"));
		var filename = $("#inputfile").val().split("\\").pop();
		var parm = /\.(GIF|gif|jpg|JPG|jpeg|JPEG|png|PNG|BMP|bmp)$/;
		if(parm.test(filename)){
			$("#picUploadForm").submit();
		}
		else{
			alertWarn("The file you upload is not the correct image format.");
		}
    });
//	$(".imglist_wrap").find("a > img").bind("click",function(){
//		parentid = $(this).attr("rel");
//		buz=$(this).attr("buz");
//		showTree(parentid,buz);
//	});
	$("#saveimageinfo").click(function(){
		var fileids="";
		var parentid = $("#dirpos").attr("rel");
		$(".upload_img_list img").each(function(){
			if($(this).attr("rel")!="")
				fileids+=$(this).attr("rel")+",,,";
		});
		if(fileids==""){
			alertWarn("Please select the picture to upload or wait for the completion of the picture upload!");
			return 0;
		}
		dataForSend ={"fileids":fileids,"parentid":parentid,"merchantid":merchantid};
		//sendAndRefresh("saveimageinfo",dataForSend);
		jQuery.ajax({
			  type: 'POST',
			  url: "saveimageinfo",
			  data: jQuery.param(dataForSend) + '&time='+ new Date().getTime(),
			  dataType: 'text',
			  contentType : 'application/x-www-form-urlencoded;charset=utf-8',
			  beforeSend : function(xhr) {
			       xhr.setRequestHeader('Accept', "text/html; charset=utf-8");
			   },
			  success: function(data) {
					$("#upload_img_list").find("li").each(function(){
						$('#file_upload').uploadify("cancel", $(this).attr("id"));
					});
					$("#beginTime").val("");
					$("#endTime").val("");
					$("#searchkey").val("");
			    	$("#popuploading").hide();  
					if($("#showdir").val()!=null){
						list_showListOrImg();
						return 0;
					}
					else{
						//$(".upload_img_list").html(""); // 清除上传略缩图信息
						goThisPage("search");
					}
				
		   }});
		
	});
} );
/**
 * 移动按钮
 */
function showMove(){
	if(typeof($("input[name=radio]:checked").val()) == 'undefined'){
		alertWarn("Please select a picture or folder");
		return 0;
	}
	$("#popuploading").show();
	$("#uploadshow").hide();
	$("#newfoldershow").hide();
	$("#renameshow").hide();
	$("#moveshow").show();
	pop_center ('540');
}
function showMoveForList(){
	if(typeof($("input[name=radio]:checked").val()) == 'undefined'){
		alertWarn("Please select a picture");
		return 0;
	}
	$("#popuploading").show();
	$("#uploadshow").hide();
	$("#newfoldershow").hide();
	$("#renameshow").hide();
	$("#moveshow").show();
	pop_center ('540');
}
/**
 * 重命名按钮
 */
function showRename(){
	$("#rename").val("");
	if(parseInt($("input[name='radio']:checked").length)==0){
		alertWarn("Please select a picture or folder");
		return 0;
	}
	if(parseInt($("input[name='radio']:checked").length)>1){
		alertWarn("Please select a picture or folder");
		return 0;
	}
	$("#popuploading").show();
	$("#uploadshow").hide();
	$("#newfoldershow").hide();
	$("#renameshow").show();
	$("#moveshow").hide();
	pop_center ('440');
}
/**
 * 删除按钮
 */
function showDelete(){
	if(parseInt($("input[name='radio']:checked").length)==0){
		alertWarn("Please select a picture or folder");
		return 0;
	}
	$("input[name='radio']:checked").each(function(){
		var	buzid=$(this).attr('value');
		deleteimg(buzid);
	});
	
//		if(confirm("确定要删除这条数据")){
//			var buzid = $("input[name=radio]:checked").val();
//			dataForSend={"buzid":buzid,"merchantid":merchantid};
//			//sendAndRefresh("rename",dataForSend);
//			jQuery.ajax({
//				  type: 'POST',
//				  url: "delinfo",
//				  data: jQuery.param(dataForSend) + '&time='+ new Date().getTime(),
//				  dataType: "text",
//				  contentType : 'application/x-www-form-urlencoded;charset=utf-8',
//				  beforeSend : function(xhr) {
//				       xhr.setRequestHeader('Accept', "text/html; charset=utf-8");
//				   },
//				  success: function(data) {
//					  var jsonObjArr = JSON.parse(data); 
//					  if(jsonObjArr.code=="1"){
//						  alert(jsonObjArr.result);
//						  $("input[name=radio]:checked").parent().parent().remove();
//					  }
//			   }});
//		}
}
function listrenameDia(id){
	$("#renamelistsubmit").attr("myvalue",id);
	$("#popuploading").show();
	$("#uploadshow").hide();
	$("#newfoldershow").hide();
	$("#renameshow").show();
	pop_center ('540');
}
function renamelistSubmit(){
	var dir = $("#rename").val();
	var buzid = $("#renamelistsubmit").attr("myvalue");
	dataForSend={"newname":dir,"buzid":buzid};
	//sendAndRefresh("rename",dataForSend);
	jQuery.ajax({
		  type: 'POST',
		  url: "rename",
		  data: jQuery.param(dataForSend) + '&time='+ new Date().getTime(),
		  dataType: "text",
		  contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		  beforeSend : function(xhr) {
		       xhr.setRequestHeader('Accept', "text/html; charset=utf-8");
		   },
		  success: function(data) {
			  var jsonObjArr = JSON.parse(data); 
			  if(jsonObjArr.code=="1"){
				  if($("#showdir").val()!=null){
					  list_showListOrImg();
					  $("#popuploading").hide();
					  $("#renameshow").hide();
					  return 0;
				  }
				  location.reload();
			  }
	   }});
}
//删除图片或文件夹
function deleteimg(buzid){
	dataForSend={"buzid":buzid};
	jQuery.ajax({
		  type: 'POST',
		  url: "delinfo",
		  data: jQuery.param(dataForSend) + '&time='+ new Date().getTime(),
		  dataType: "text",
		  contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		  beforeSend : function(xhr) {
		       xhr.setRequestHeader('Accept', "text/html; charset=utf-8");
		   },
		  success: function(data) {
			  var jsonObjArr = JSON.parse(data);
			  if(jsonObjArr.code=="2")
				  alertWarn(jsonObjArr.result);
			  else if(jsonObjArr.code=="1"){
				  if($("#showdir").val()!=null){
						list_showListOrImg();
					}
					else{
						$("#popuploading").hide();
						$("#uploadshow").hide();
						$("#newfoldershow").hide();
						$("#renameshow").hide();
						$("#open_returnPop").hide();
						$("#moveshow").hide();
						goThisPage("search");
						listdirformoving();
						
					}
			  }
			
	   }});
}
 //恢复图片
function restoreimg(buzid){
	dataForSend={"buzid":buzid};
	jQuery.ajax({
		  type: 'POST',
		  url: "restoreinfo",
		  data: jQuery.param(dataForSend) + '&time='+ new Date().getTime(),
		  dataType: "text",
		  contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		  beforeSend : function(xhr) {
		       xhr.setRequestHeader('Accept', "text/html; charset=utf-8");
		   },
		  success: function(data) {
			  var jsonObjArr = JSON.parse(data); 
			  if(jsonObjArr.code=="1"){
				  location.reload();
			  }
	   }});
}
//彻底删除图片
function physicaldelimg(buzid){
	dataForSend={"buzid":buzid};
	jQuery.ajax({
		  type: 'POST',
		  url: "physicaldelete",
		  data: jQuery.param(dataForSend) + '&time='+ new Date().getTime(),
		  dataType: "text",
		  contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		  beforeSend : function(xhr) {
		       xhr.setRequestHeader('Accept', "text/html; charset=utf-8");
		   },
		  success: function(data) {
			  var jsonObjArr = JSON.parse(data); 
			  if(jsonObjArr.code=="1"){
				  location.reload();
			  }
	   }});
}
//删除前验证图片状态
function validation(buzid,operation){
	dataForSend={"buzid":buzid};
	jQuery.ajax({
		  type: 'POST',
		  url: "validation",
		  data: jQuery.param(dataForSend) + '&time='+ new Date().getTime(),
		  dataType: "text",
		  contentType : 'application/x-www-form-urlencoded;charset=utf-8',
		  beforeSend : function(xhr) {
		       xhr.setRequestHeader('Accept', "text/html; charset=utf-8");
		   },
		  success: function(data) {
			  var jsonObjArr = JSON.parse(data); 
			  if(jsonObjArr.code=="11"){
				  tShowCanclePop(buzid,"deleteS",jsonObjArr.result);
			  }
			  else if(jsonObjArr.code=="12"){
				  alertWarn(jsonObjArr.result);
				  return false;
			  }
			  else{
				  deleteimg(buzid);
			  }
	   }});
}
function tShowCanclePop(buzid,operaction,result){
	var opt="";
	var url = "";
	if(operaction=="update"){
		opt="Do you confirm the changes?";
	}
	if(operaction=="delete"){
		opt="Are you sure you will delete it?";
		url ="showDelete()";
	}
	if(operaction=="deleteS"){
		opt=result;
		url ="deleteimg('"+buzid+"')";
	}
	if(operaction=="batchdelete"){
    	if(parseInt($("input[name='radio']:checked").length)==0){
    		alertWarn("Please select at least one!");
    		return 0;
    	}
		opt="Are you sure you are executing batch delete operations?";
		url ="batchDelete()";
	}
	if(operaction=="restore"){
		opt="Whether to confirm the recovery?";
		url ="restoreimg('"+buzid+"')";
	}
	if(operaction=="physicaldel"){
		opt="Whether to determine the complete delete operation, delete will not be restored!";
		url ="physicaldelimg('"+buzid+"')";
	}
	var htmls = "<div class='layout'>"+
	"<b class='ico_close' onclick='closed()'>close</b>"+
	"<div class='main'>"+
		"<div class='bd_p10 clearfix'>"+
        	"<div class='popup_tips_title pl150 mt25 mb20 ml20'>"+
        	"<b class='ico fl'></b><span class='fl ml15'>Reminder</span></div>"+
            "<p class='tac fontSize120 colorOrange mb20'>"+opt+"</p>"+
		"</div>"+
		"<div class='freightTemplet_btns mt15'>"+
			"<input type='button' value='ok' onclick="+url+"  class='btn btn-sm btn-sgGreen'>"+
			"<input type='button' value='cancel' class='btn btn-sm btn-grey' onclick='closed()'>"+
		"</div>"+
	"</div>"+
"</div>";
	$("#popuploading").show();
	$("#uploadshow").hide();
	$("#newfoldershow").hide();
	$("#renameshow").hide();
	$("#open_returnPop").show();
	$('#open_returnPop').empty().html(htmls);
	pop_center ('560');
}
/**
 * 验证及提示框
 */
function alertWarn(message){
	var htmls = "<div class='layout'>"+
			"<b class='ico_close' onclick='closed()'>close</b>"+
			"<div class='main'>"+
				"<div class='bd_p10 clearfix'>"+
                	"<div class='popup_tips_title pl150 mt25 mb20 ml20'><b class='ico fl'></b><span class='fl ml15'>Reminder</span></div><p class='tac fontSize120 colorOrange mb20'>"+message+"</p></div>"+
				"</div>"+
				"<div class='freightTemplet_btns mt15'>"+
					"<input type='button' value='ok' onclick='closed()' class='btn btn-sm btn-sgGreen'>"+
				"</div>"+
			"</div>"+
		"</div>";
	$("#popuploading").show();
	$("#uploadshow").hide();
	$("#newfoldershow").hide();
	$("#renameshow").hide();
	$("#open_returnPop").show();
	$('#open_returnPop').empty().html(htmls);
	pop_center ('500');
}
//处理中提示框
function processPrompt(message){
	var htmls = "<div class='layout'>"+
			"<b class='ico_close' onclick='closed()'>关闭</b>"+
			"<div class='main'>"+
				"<div class='bd_p10 clearfix'>"+
                	"<div class='popup_tips_title pl150 mt25 mb20 ml20'><b class='ico success fl'></b><span class='fl ml15'>"+message+"！</span></div>"+
				"</div>"+
				"<div class='freightTemplet_btns mt15'>"+
					"<input type='button' value='ok'  onclick='closed()' class='btn btn-sm btn-sgGreen'>"+
				"</div>"+
			"</div>"+
		"</div>";
	$("#popuploading").show();
	$("#uploadshow").hide();
	$("#newfoldershow").hide();
	$("#renameshow").hide();
	$("#open_returnPop").show();
	$('#open_returnPop').empty().html(htmls);
	pop_center ('500');
}
function closed(){
	$("#popuploading").hide();
	$("#open_returnPop").hide();
}
/**
 * 移动图片
 */
function movesubmit(){
	var parentId = $("#movepos").val();
	var imgids = "";
	$("input[name='radio']:checked").each(function(){
		if(imgids=="")
			imgids=$(this).attr('value');
		else
			imgids=imgids+",,,"+$(this).attr('value');
});
	$.post(getRootPath()+"/imageopt/changedir",{"imgIds":imgids,"parentId":parentId},
			function(data){
				location.reload();
	});
}
/**
 * 批量删除
 */
function batchDelete(){
	var imgids = "";
	var parentId = $("#showdir").val();
	$("input[name='radio']:checked").each(function(){
	    		if(imgids=="")
	    			imgids=$(this).attr('value');
	    		else
	    			imgids=imgids+",,,"+$(this).attr('value');
	});
	$.post(getRootPath()+"/imageopt/batchdelete",{"imgids":imgids,"currentPage":$("#currentPage").val(),"parentId":parentId},
			function(data){
				$("#reloadSearchMessages").empty().html(data);
				closed();
	});
}
/**
 * 列表选择显示图片还是文件夹
 */
function list_showListOrImg(){
	var parentId = $("#showdir").val();
	var currentPage = $("#currentPage").val();
	$("#dirpos").attr("rel",parentId);
	$("#dirpos").text("Position: "+$("#showdir").find("option:selected").text());
	var searchKey=$("#searchkey").val();
	$.post(getRootPath() + "/imageopt/initlist",{"currentPage":currentPage,"parentId":parentId,"searchKey":searchKey},function(data){
		$("#reloadSearchMessages").empty().html(data);
		closed();
		$.post( getRootPath() + "/imageopt/listalldir",null,    
				function(data){
					var jsonObjArr = JSON.parse(data); 
					for(var i in jsonObjArr)
					$("#showdir").append('<option value="'+jsonObjArr[i].imageSpaceId+'">'+jsonObjArr[i].name+'</option>');
					$("#showdir").find("option[value="+parentId+"]").attr("selected","selected");
		});
	});
}
/**
 * 清楚日期控件值
 */
function clearTime(val){
	if(val=="begin"){
		$("#beginTime").val("");
	}
	else if(val=="end"){
		$("#endTime").val("");
	}
}
/**
 * 大图模式移动文件夹下拉列表数据组装
 */
function listdirformoving(){
	$("#movepos").html('');
	$("#movepos").append('<option value="0">ROOT</option>');
	$.post( getRootPath() + "/imageopt/listalldir",null,    
			function(data){
				var jsonObjArr = JSON.parse(data); 
				for(var i in jsonObjArr)
				$("#movepos").append('<option value="'+jsonObjArr[i].imageSpaceId+'">'+jsonObjArr[i].name+'</option>');
	});
}
/**
 * 列表全选功能
 */
function chooseAllCheckbox(){
	$("input[name=radio]").each(function(){
		if($(this).attr("disabled")!="disabled")
			$(this).attr("checked",true);
	});
}
/**
 * 清空搜索条件显示全部
 */
function searchback(){
	$("#beginTime").val("");
	$("#endTime").val("");
	$("#searchkey").val("");
	goThisPage("search");
}
/*
 * ztree
 */
var setting = {
    check: {
		enable: false,
		chkStyle: "checkbox"
	},
	view: {
		dblClickExpand: false
	},
	data: {
		key: {
			name: "DIR_PATH" // 树节点名称绑定json对象的属性名
		},
		simpleData: {
			enable: true,
			idKey: "IMAGESPACE_ID",   // 数ID绑定json对象的属性名
			pIdKey: "PARENT_ID", // 父节点ID绑定json对象的属性名
			rootPId: "0"  // 根节点的父节点ID
		}
	},
	callback: {
		onClick: onClickTree,
		onCheck: onCheckTree
	}
};
var onClickTree = function(treeId, treeNode) {  
	 alert("beforeClick");  
};  
var onCheckTree =	function(e, treeId, treeNode) {  
	 alert("onCheck");  
};

var loadTree = function() {
	$.post( getRootPath() + "/imageopt/function_list",null,    
		function(data){
	    	$.fn.zTree.init($("#myTree"), setting, eval(data));
		}
	);
};
//根上下文路径
var getRootPath = function() {
	return $('#rootPath').val();
};
