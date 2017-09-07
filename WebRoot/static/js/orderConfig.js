/**
 * 验证及提示框
 */
function alertWarn(message){
	var htmls = "<div class='layout'>"+
	"<b class='ico_close' onclick='closed()'>关闭</b>"+
	"<div class='main'>"+
	"<div class='bd_p10 clearfix'>"+
	"<div class='popup_tips_title pl150 mt25 mb20 ml20'><b class='ico fl'></b></div><p class='tac fontSize120 colorOrange mb20'>"+message+"</p></div>"+
	"</div>"+
	"<div class='freightTemplet_btns mt15'>"+
	"<input type='button' value='OK' onclick='closed()' class='btn btn-sm btn-sgGreen'>"+
	"</div>"+
	"</div>"+
	"</div>";
	$(".shade_layout").show();
	$("#open_returnPop").css("display","block");
	$('#open_returnPop').empty().html(htmls);
	pop_center ('500');
}
$(function(){
	var root = $("#rootsite").val();
	$.post(root+'/mail/orderConfigValue',{},function(data){
	$('#mailForm input[name="cancelTime"]').val(data.cancelTime);
	$('#mailForm input[name="receiveTime"]').val(data.receiveTime);
	$('#mailForm input[name="receiveServiceTime"]').val(data.receiveServiceTime);
	
	});
	
	$('#readnumbtn').click(function (){
		var serializeArray=$('#mailForm').serializeArray();
	for(i in serializeArray){
		var text=$('#mailForm input[name="'+serializeArray[i].name+'"]').prev('lebel').children().last().text().replace(':','');
		if(text=='')
			text=$('#mailForm textarea[name="'+serializeArray[i].name+'"]').prev('lebel').children().last().text().replace(':','');
		
		if(!serializeArray[i].value&&serializeArray[i].value==''){
			alertWarn(text+' can\'t be empty.');
			return ;
		}else{
			var regInt = /^[0-9]*[1-9][0-9]*$/ ; 
			if(serializeArray[i].name=='cancelTime'||serializeArray[i].name=='receiveTime'||serializeArray[i].name=='receiveServiceTime')
			if(!regInt.test(serializeArray[i].value)){
				alertWarn('please input integer about in '+text+'!');
				return;
			}
		}
	}
		$.post(root+"/mail/saveOrderConfig", serializeArray,
				function(data){
				if(data.code=="1"){
					alertWarn('suceess');
				}
				else{
					alertWarn("fail,please contact the administrator");
				}
				
		});
	});
	
	
	
});