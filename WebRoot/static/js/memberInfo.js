/**
 * 上传商户logo
 */
function uploadCover(){
	var root = $("#rootsite").val();
	$.ajaxFileUpload
	(
		{
			url:root+'/memberinfo/uploadcover',
			secureuri:false,
			fileElementId:'cover',
			dataType: 'json',
			data:{"memberId":$("#memberId").val()},
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
			goThisPage($('#currentPage').val());
			$("#popwin").hide();
			$(".shade_layout").hide();
			alert("success!");
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
/*	$("#memberAccount").val($td.eq(1).text());*/
	$("#memberNickname").val($td.eq(1).text());
	var src=$td.eq(5).find("img").attr('src');

	if(src&&src!=''){
	$("#memberLogo").val(src.substring(src.lastIndexOf("/")+1,src.lastIndexOf(".jpg")));}
	$("#email").val($td.eq(2).text());
	$("#mobile").val($td.eq(4).text());
	   $("input:radio").each(function(i){
           if( $(this).val()==$td.eq(3).find('input').val())
                $(this).prop("checked","checked");
           else
        	    $(this).removeProp("checked");
     
            });
	$("select[name='memberType']").val($td.eq(6).find('input').val());
	var root = $("#rootsite").val();
	if(src&&src!='')
	$("#photocover").prop("src",src);
	else
	$("#photocover").prop("src",root+'/static/images/sellercenter_bg/imgs/nopicture.gif');	
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
function formatOrderPayment(val,row){ 
	return '<span>'+(val==1?'online payment':val==2?'offline payment':'')+'</span>';
}
function formatOrderStatus(val,row){ 
	 return '<span>'+(val==1?'Unpaid':val==2?'Pick Up':val==3&&!row.deliveryStatus?'Undelivered':val==3&&row.deliveryStatus?'Deliveries':val==4?'Received':val==5?'Completed':val==12?'Canceled':val)+'</span>';
}
function formatIsDelivery(val,row){
	return '<span>'+(val==0?'pick up':'delivery')+'</span>';
}
function orderInfo(memberId){
	var root=$('#rootsite').val();
//			    $('#dg').datagrid('load',root+'/order/orderList?memberId='+memberId);
//			    $('#applicatorHot').window('open');
	var iTop = (window.screen.availHeight-30-350)/2-100;
	var iLeft = (window.screen.availWidth-10-500)/2-200; 
	window.open (root+'/memberinfo/orderList/'+memberId,'newwindow','height=540,width=1000,top='+iTop+',left='+iLeft+',toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
		

}

function walletInfo(memberId){
	var root=$('#rootsite').val();
//			    $('#dgWallet').datagrid('load',root+'/memberinfo/rechargeRecordList?rechargeMemberId='+memberId);
//			    $('#windowWallet').window('open');
	var iTop = (window.screen.availHeight-30-350)/2-100;
	var iLeft = (window.screen.availWidth-10-500)/2-200; 
	window.open (root+'/memberinfo/rechargeRecord/'+memberId,'newwindow','height=540,width=1000,top='+iTop+',left='+iLeft+',toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
		

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
$(function(){
//	$('#dg').datagrid({
//		onLoadSuccess:function(){
//			$('.datagrid-pager').find('td').find('a').css({'padding-left':'1px','padding-right':'1px','border-width':'1px'});
//		}
//	})
//	$('#dgWallet').datagrid({
//		onLoadSuccess:function(){
//			$('.datagrid-pager').find('td').find('a').css({'padding-left':'1px','padding-right':'1px','border-width':'1px'});
//		}
//	})
	//样式冲突修复

	
})
