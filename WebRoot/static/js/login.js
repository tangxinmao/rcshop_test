//清除提示框
function closed_add_shade_layout(){
    //清除提示框
    if($('#BDIWFYBUIWBFNQP').length>0){
        $('#BDIWFYBUIWBFNQP').remove();
    }
    if($('#NIUGSIHNAWQIPOHFC').length>0){
        $('#NIUGSIHNAWQIPOHFC').remove();
    }
/*    if($('.shade_layout').length>0){
        $('.shade_layout').hide();
        $(".pop_windowss").hide();
    }*/
}
$(function() {
    //警告提示框
    function alertWarn(message){
        closed_add_shade_layout();
        var htmls = '<div id="BDIWFYBUIWBFNQP" class="shade_layout" ></div><div id="NIUGSIHNAWQIPOHFC" class="pop_windowss" style="width: 560px; margin-left: -280px; margin-top: -81px;"> <div class="layout"> <b class="ico_close" onclick="closed_add_shade_layout()">关闭</b> <div class="main"> <div class="bd_p10 clearfix"> <div class="popup_tips_title mt25 mb20 ml20"> <b class="ico"></b> </div><p class="tac fontSize120 mb20">'+message+'</p>  </div> <div class="freightTemplet_btns mt15"> <input type="button" onclick="closed_add_shade_layout()" value="OK" class="btnG bwt100 bht30 bfz16 btrau">  </div> </div> </div>';
        $('body').append(htmls);
        pop_center2 ('560');
    }
    function pop_center(width) {
        var $ml = -width / 2,
            $w = $(document).width(),
            $h = $(document).height();
      /*  $(".BDIWFYBUIWBFNQP").css({
            width: $w,
            height: $h,
            opacity: 0.5
        });
        $(".BDIWFYBUIWBFNQP").show();*/
        $(".pop_windowss").css({
            width: width,
            'margin-left': $ml
        });
        var $height = $(".pop_windowss").height(),
            $mt = -$height / 2;
        $(".pop_windowss").css({
            'margin-top': $mt
        });
    };
	// 点击获取验证码
	var root = $('#rootsite').val();
	$('#change').click(getCode)
	function getCode() {
		var time = new Date().getTime();
		document.getElementById("imagecode").src = root
				+ "/loginRegister/code?d=" + time;
	}
	// 显示或者影藏密码
	$('#glyphicon-eye-open').click(function() {
		if ($('input[name="password"]').prop('type') == 'password') {
			$('input[name="password"]').prop('type', 'text');
			$(this).find('span').removeClass('glyphicon-eye-open');
			$(this).find('span').addClass('glyphicon-eye-close');
		} else{
			$('input[name="password"]').prop('type', 'password');
			$(this).find('span').removeClass('glyphicon-eye-close');
			$(this).find('span').addClass('glyphicon-eye-open');
		}
	})

    function pop_center2(width) {
        var $ml = -width / 2,
            $w = $(document).width(),
            $h = $(document).height();
        $("#BDIWFYBUIWBFNQP").css({
            width: $w,
            height: $h,
            opacity: 0.5
        });
        // $(".shade_layout").show();
        $(".pop_windowss").css({
            width: width,
            'margin-left': $ml
        });
        var $height = $(".pop_windowss").height(),
            $mt = $height/2;
        $(".pop_windowss").css({
            'margin-top': $mt
        });
    };
    /**
     * 验证及提示框
     */
/*    function alertWarn(message){
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
    }*/
	// 表单验证
	var validator = $('form').validate(
			{   messages : {
				verificationCode: {
				    remote: 'verification code is error!'
				}
			},
				rules:{
				verificationCode:{
					remote: {
				    url:root+ "/user/validateCode",     //后台处理程序
				    type: "post",               //数据发送方式
				    dataType: "json",           //接受数据格式   
				    data: {                     //要传递的数据
				    	verificationCode: function() {
				            return $('input[name="verificationCode"]').val();
				        }
				    }
			}
				}
				
			},
				errorElement : "em",
				errorPlacement : function(error, element) {
					error.addClass("error_class");
					if (element.parent().prop('class') == 'input-group') {
						error.insertAfter(element.parent());
					}
					if (element.parent().prop('class') == 'form-group') {
						error.insertAfter(element);
					}
				},
				submitHandler : function(form) {
					$.post($(form).prop('action'), $(form).serializeArray(),
							function(data) {
								var json = JSON.parse(data);
					
								if (json.code == '1') {
									//将权限菜单放入到浏览器缓存中
								//	var itemsChecked= localStorage.getItem('itemsChecked')
									/*if(itemsChecked){
										var map=JSON.parse(itemsChecked);
										var i=0;
										while(i<map.length){
											if(map[i].roleId==json.result){
												map[i].data=json.data;
												break;
											}
											i++;
										}
										if(i==map.length){
											map.push({roleId:json.result,
												data:json.data
											});
										}
										localStorage.setItem('itemsChecked',JSON.stringify(map));
									}else{*/
										var map=[];
										var obj={
												roleId:json.data.roles?json.data.roles[0].roleId:0,
												data:json.data.menus
										}
										map.push(obj);
										localStorage.setItem('itemsChecked',JSON.stringify(map));
								/*	}*/
									
									window.location = root + "/product/product_list";
								} else {
									alertWarn(json.result);
									getCode();
								}
							});
					return false;
				},

			});

})