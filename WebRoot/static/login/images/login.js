$(function(){
	
	$('#switch_qlogin').click(function(){
		$('#switch_login').removeClass("switch_btn_focus").addClass('switch_btn');
		$('#switch_qlogin').removeClass("switch_btn").addClass('switch_btn_focus');
		$('#switch_bottom').animate({left:'0px',width:'70px'});
		$('#qlogin').css('display','none');
		$('#web_qr_login').css('display','block');
		
		});
	$('#switch_login').click(function(){
		
		$('#switch_login').removeClass("switch_btn").addClass('switch_btn_focus');
		$('#switch_qlogin').removeClass("switch_btn_focus").addClass('switch_btn');
		$('#switch_bottom').animate({left:'154px',width:'70px'});
		
		$('#qlogin').css('display','block');
		$('#web_qr_login').css('display','none');
		});
if(getParam("a")=='0')
{
	$('#switch_login').trigger('click');
}

	});
	
function logintab(){
	scrollTo(0);
	$('#switch_qlogin').removeClass("switch_btn_focus").addClass('switch_btn');
	$('#switch_login').removeClass("switch_btn").addClass('switch_btn_focus');
	$('#switch_bottom').animate({left:'154px',width:'96px'});
	$('#qlogin').css('display','none');
	$('#web_qr_login').css('display','block');
	
}


//根据参数名获得该参数 pname等于想要的参数名 
function getParam(pname) { 
    var params = location.search.substr(1); // 获取参数 平且去掉？ 
    var ArrParam = params.split('&'); 
    if (ArrParam.length == 1) { 
        //只有一个参数的情况 
        return params.split('=')[1]; 
    } 
    else { 
         //多个参数参数的情况 
        for (var i = 0; i < ArrParam.length; i++) { 
            if (ArrParam[i].split('=')[0] == pname) { 
                return ArrParam[i].split('=')[1]; 
            } 
        } 
    } 
}  


var reMethod = "GET",
	pwdmin = 6;

$(document).ready(function() {


	$('#reg').click(function() {

		if ($('#user').val() == "") {
			$('#user').focus().css({
				border: "1px solid red",
				boxShadow: "0 0 2px red"
			});
			$('#userCue').html("<font color='red'><b>×account cannot be empty</b></font>");
			return false;
		}else{
			 $.post($('#regUser').attr('action').substring(0,$('#regUser').attr('action').lastIndexOf("/"))+"/validateAccount",$('#regUser').serializeArray(),function(data){
					var json= JSON.parse(data);
						 if(json.code=='1')
					{}
						 else{
							 $('#user').focus().css({
									border: "1px solid red",
									boxShadow: "0 0 2px red"
								});
								$('#userCue').html("<font color='red'><b>×fail,account is registered</b></font>");
						
							 return false;}
					 }); 
		}



		if ($('#user').val().length < 4 || $('#user').val().length > 16) {

			$('#user').focus().css({
				border: "1px solid red",
				boxShadow: "0 0 2px red"
			});
			$('#userCue').html("<font color='red'><b>×account only four to sixteen</b></font>");
			return false;

		}
		$.ajax({
			type: reMethod,
			url: "/member/ajaxyz.php",
			data: "uid=" + $("#user").val() + '&temp=' + new Date(),
			dataType: 'html',
			success: function(result) {

				if (result.length > 2) {
					$('#user').focus().css({
						border: "1px solid red",
						boxShadow: "0 0 2px red"
					});$("#userCue").html(result);
					return false;
				} else {
					$('#user').css({
						border: "1px solid #D7D7D7",
						boxShadow: "none"
					});
				}

			}
		});


		if ($('#passwd').val().length < pwdmin) {
			$('#passwd').focus();
			$('#userCue').html("<font color='red'><b>×password can not be less than " + pwdmin + "</b></font>");
			return false;
		}
		if ($('#passwd2').val() != $('#passwd').val()) {
			$('#passwd2').focus();
			$('#userCue').html("<font color='red'><b>×two passwords are not consistent ！</b></font>");
			return false;
		}

		var sqq = /^[0-9]{1,20}$/;
		if (!sqq.test($('#mobile').val()) || $('#mobile').val().length < 5 || $('#mobile').val().length > 20) {
			$('#mobile').focus().css({
				border: "1px solid red",
				boxShadow: "0 0 2px red"
			});
			$('#userCue').html("<font color='red'><b>×mobile format is not correct</b></font>");return false;
		} else {
			$('#mobile').css({
				border: "1px solid #D7D7D7",
				boxShadow: "none"
			});
			
		}
		 $.post($('#regUser').attr('action'),$('#regUser').serializeArray(),function(data){
		var json= JSON.parse(data);
			 if(json.code=='1'){
			 alert("register success!");
				$('#switch_login').removeClass("switch_btn_focus").addClass('switch_btn');
				$('#switch_qlogin').removeClass("switch_btn").addClass('switch_btn_focus');
				$('#switch_bottom').animate({left:'0px',width:'70px'});
				$('#qlogin').css('display','none');
				$('#web_qr_login').css('display','block');
				$('#regUser')[0].reset();
			}
			 else
				 alert("fail,please contact the administrator");
		 }); 
	
	});
	

});