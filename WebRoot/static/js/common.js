
$( function() {
	/**
	 * 菜单生成
	 */
	var root=$('#rootsite').val();
	var rcMenuParentJson;
	var rcMenuItemJson
	if($('#rcMenuParent').val()){
		rcMenuParentJson=JSON.parse($('#rcMenuParent').val());
	    rcMenuItemJson=JSON.parse($('#rcMenuItem').val());
	    //存到缓存
			sessionStorage.rcMenuParentJson=$('#rcMenuParent').val();
	
			sessionStorage.rcMenuItemJson=$('#rcMenuItem').val();
	}else{
	    if(sessionStorage.rcMenuParentJson)
		rcMenuParentJson=JSON.parse(sessionStorage.rcMenuParentJson);
	    if(sessionStorage.rcMenuItemJson)
		rcMenuItemJson=JSON.parse(sessionStorage.rcMenuItemJson);
	}
	if($('#loginMember').val()){
		//var loginMember=JSON.parse($('#loginMember').val());
		var itemsChecked=localStorage.getItem('itemsChecked');
		var json=JSON.parse(itemsChecked);
	/*	for(i in json){
			if(json[i].roleId==loginMember.roleId){*/
				generateItem(json[0].data);
			
			/*	break;
			}
		}*/
	
	}
	else
	$.post(root+'/role/menu',{},function(data){
		generateItem(data);
	});
	
	function generateItem(data){
		var tree=convert(data);
		//var home=tree[0];
	  	$('.leftMenu').empty();
		$('.leftMenu').append('<div class="top"><a href="'+/*(home.attributes.location?root+home.attributes.location:*/'javascript:;'/*)*/+'"><b>Home</b></a></div>');
		for(i in tree){
			var str='';
			var tr=tree[i];
			if(!tr.children){
				//str+='<div class="mg selectedz"><div class="mt">';
			if(rcMenuItemJson&&rcMenuItemJson.menuId==tr.id)
				str+='<div class="mg selectedz"><div class="mt"><a class="alink" href="'+(tr.attributes.location?root+tr.attributes.location:'javascript:;')+'"><b>'+tr.text+'</b></a></div><div class="mb" style="display: none;"><ul>';
			else
				str+='<div class="mg"><div class="mt"><a  class="alink" href="'+(tr.attributes.location?root+tr.attributes.location:'javascript:;')+'"><b>'+tr.text+'</b></a></div><div class="mb" style="display: none;"><ul>';
			}else{
			if(rcMenuParentJson&&rcMenuParentJson.menuId==tr.id){
				str+='<div class="mg"><div class="mt ico_opened"><a class="ico"></a><a class="alink" href="'+(tr.attributes.location?root+tr.attributes.location:'javascript:;')+'"><b>'+tr.text+'</b></a></div><div class="mb" style="display: block;"><ul>';
			}else{
			str+='<div class="mg"><div class="mt ico_closed"><a class="ico"></a><a class="alink" href="'+(tr.attributes.location?root+tr.attributes.location:'javascript:;')+'"><b>'+tr.text+'</b></a></div><div class="mb" style="display: none;"><ul>';
			}
			}
			for(j in tr.children){
				var td=tr.children[j];
				if(rcMenuItemJson&&rcMenuItemJson.menuId==td.id)
					str+='<li class="selectedz"><a class="menu_item" href="'+(td.attributes.location?root+td.attributes.location:'javascript:;')+'">'+td.text+'</a></li>'
					else
				str+='<li><a class="menu_item" href="'+(td.attributes.location?root+td.attributes.location:'javascript:;')+'">'+td.text+'</a></li>';
			}
			str+='</ul></div></div>';
			$('.leftMenu').append(str);
		}
		$(".leftMenu .ico").click(function(){
			if($(this).parent().hasClass("ico_opened")){
				$(this).parent().removeClass("ico_opened");
				$(this).parent().addClass("ico_closed");
				$(this).parent().parent().find("div[class='mb']").hide();
			}
			else if($(this).parent().hasClass("ico_closed")){
				$(this).parent().removeClass("ico_closed");
				$(this).parent().addClass("ico_opened");
				$(this).parent().parent().find("div[class='mb']").show();
			}
		});
		$(".leftMenu .alink").click(function(){
			if($(this).parent().hasClass("ico_opened")){
				$(this).parent().removeClass("ico_opened");
				$(this).parent().addClass("ico_closed");
				$(this).parent().parent().find("div[class='mb']").hide();
			}
			else if($(this).parent().hasClass("ico_closed")){
				$(this).parent().removeClass("ico_closed");
				$(this).parent().addClass("ico_opened");
				$(this).parent().parent().find("div[class='mb']").show();
			}
		});
	}
	//行转tree
	function convert(rows) {
		function exists(rows, parentId) {
			for (var i = 0; i < rows.length; i++) {
				if (rows[i].menuId == parentId)
					return true;
			}
			return false;
		}

		var nodes = [];
		// get the top level nodes
		for (var i = 0; i < rows.length; i++) {
			var row = rows[i];
			if (!exists(rows, row.parentId)) {
				nodes.push({
					id : row.menuId,
					text : row.name,
					checked : row.checked,
					attributes:{seq:row.seq,
						location:row.location}
				});

			}
		}

		var toDo = [];
		for (var i = 0; i < nodes.length; i++) {
			toDo.push(nodes[i]);
		}
		while (toDo.length) {
			var node = toDo.shift();
			for (var i = 0; i < rows.length; i++) {
				var row = rows[i];
				if (row.parentId == node.id) {
					var child = {
						id : row.menuId,
						text : row.name,
						checked : row.checked,
						attributes:{seq:row.seq,
							location:row.location}
					};
					if (node.children) {
						node.children.push(child);
					} else {
						node.children = [ child ];
					}
					toDo.push(child);
				}
			}
		}
		return nodes;
	}
	
	var w = $(document).width()-236;
	var w2 = w-60;
	$(".rightContent").css({
		width: w,
		"background-repeat": "repeat-x"
	});
	$(".tpos").css({
		width: w2
	});
	$(".data_logic").css({
		width: w2
	});
    $(".rightContent").css({
        float:'right',
        marginRight:($(document).width()/100)
    });
	$(window).resize(function () {


         $(".rightContent").css({
             width: $(document).width()-236,
             "background-repeat": "repeat-x"
         });
        $(".tpos").css({
            width: $(document).width()-296
        });
        $(".data_logic").css({
            width: $(document).width()-296
        });
        $(".rightContent").css({
            float:'right',
            marginRight:($(document).width()/100)
        });


     })
	
	
	//$( ".pop_windows" ).draggable({cursor: "move",handle:".hd" });
	
	$(".sortArea ul li").click(function(){
		$(".sortArea ul").find("li").each(function(){
			$(this).removeClass("selcted");
		});
		$(this).addClass("selcted");
	});
	
});

$(window).resize(function(){
	var w = $(document).width()-216;
	var w2 = w-60;
	$(".rightContent").css({
		width: w,
		"background-repeat": "repeat-x"
	});
	$(".tpos").css({
		width: w2
	});
	$(".data_logic").css({
		width: w2
	});
});

/* 
弹出窗口居中插件
pop_center ('330') 调用
*/
function pop_center(width, height) {
	var $ml = -width / 2,
		$w = $(document).width(),
		$h = $(document).height();
	$(".shade_layout").css({
		width: $w,
		height: $h,
		opacity: 0.5
	});
	$(".shade_layout").show();
	$(".pop_windows").css({
		width: width,
		'margin-left': $ml
	});
	var $height = $(".pop_windows").height();
    if(height) {
        $height = height;
    }
    var $mt = -$height / 2;
	$(".pop_windows").css({
		'margin-top': $mt
	});
};

//正在建设中弹出框
function inBuilding() {
	if ($('#forBuildingDivId').length == 0) {
		$('body').append('<div id="forBuildingDivId" style="display:none;border:green 3px solid;border-radius:5px 5px 5px 5px;width:150px;margin:0 auto;height:50px;text-align:center;line-height:50px;background-color:#FEE;position:absolute;top:50%;left:50%;margin-left:-75px;margin-top:-55px;"><span style="color:#007bc7;margin-top:20px;font-size:16px">该功能正在建设中！</span></div>');
	}
	var top = ($(window).height() - $('#forBuildingDivId').height()) / 2;
	var left = ($(window).width() - $('#forBuildingDivId').width()) / 2 + 75;
	var scrollTop = $(document).scrollTop();
	var scrollLeft = $(document).scrollLeft();
	$('#forBuildingDivId').css({
		position: 'absolute',
		top: top + scrollTop,
		left: left + scrollLeft,
		'z-index': 999
	});
	$('#forBuildingDivId').fadeIn(300);
	setTimeout(function() {
		$('#forBuildingDivId').fadeOut(400);
	}, 800);
};

//窗口滑到指定地方
function theScrollMoveToTargetSpace(po) {
	//以186秒的间隔返回顶部
	$('body,html').animate({
		scrollTop: $(po).offset().top
	}, 286);
};
//弹出成功提示框
function alertSuccess(message){
	closed_add_shade_layout();
	var htmls = '<div id="BDIWFYBUIWBFNQP" class="shade_layout"></div> <div id="NIUGSIHNAWQIPOHFC" class="pop_windows" style="width: 500px; margin-left: -250px; margin-top: -83px;"> <div class="layout"> <b class="ico_close" onclick="closed_add_shade_layout()">关闭</b> <div class="main"> <div class="bd_p10 clearfix"> <div class="popup_tips_title mt25 mb20 ml20"> <b class="ico success"></b> <span class="success_text">'+message+'</span> </div> </div> <div class="freightTemplet_btns mt15"> <input type="button" value="Yes" onclick="closed_add_shade_layout()" class="btnG bwt100 bht30 bfz16"> </div> </div> </div> </div>';
	$('body').append(htmls);
	// 登录弹出居中
	pop_center ('560');
}
//弹出失败提示框
function alertFail(message){
	closed_add_shade_layout();
	var htmls = '<div id="BDIWFYBUIWBFNQP" class="shade_layout" ></div><div id="NIUGSIHNAWQIPOHFC" class="pop_windows" style="width: 560px; margin-left: -280px; margin-top: -81px;"> <div class="layout"> <b class="ico_close" onclick="closed_add_shade_layout()">关闭</b> <div class="main"> <div class="bd_p10 clearfix"> <div class="popup_tips_title mt25 mb20 ml20"> <b class="ico failure"></b> <span class="success_text">'+message+'</span> </div> </div> <div class="freightTemplet_btns mt15"> <input type="button" onclick="closed_add_shade_layout()" value="确定" class="btn btn-sm btn-sgGreen"> </div> </div> </div> </div>';
	$('body').append(htmls);
	// 登录弹出居中
	pop_center ('560');
}
//警告提示框
function alertWarn(message){
	closed_add_shade_layout();
	var htmls = '<div id="BDIWFYBUIWBFNQP" class="shade_layout" ></div><div id="NIUGSIHNAWQIPOHFC" class="pop_windowss" style="width: 560px; margin-left: -280px; margin-top: -81px;"> <div class="layout"> <b class="ico_close" onclick="closed_add_shade_layout()">关闭</b> <div class="main"> <div class="bd_p10 clearfix"> <div class="popup_tips_title mt25 mb20 ml20"> <b class="ico"></b> </div><p class="tac fontSize120 mb20">'+message+'</p>  </div> <div class="freightTemplet_btns mt15"> <input type="button" onclick="closed_add_shade_layout()" value="OK" class="btnG bwt100 bht30 bfz16 btrau">  </div> </div> </div>';
	$('body').append(htmls);
	pop_center2 ('560');
}
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
//清除提示框
function closed_add_shade_layout(){
	//清除提示框
	if($('#BDIWFYBUIWBFNQP').length>0){
		$('#BDIWFYBUIWBFNQP').hide();
	}
	if($('#NIUGSIHNAWQIPOHFC').length>0){
		$('#NIUGSIHNAWQIPOHFC').hide();
	}
	if($('.shade_layout').length>0){
		 $('.shade_layout').hide();
		 $(".pop_windows").hide();
	}
}
/**
 * 上传等待框
 * @param message 等待显示信息
 */
function processWait(message){
	closed_add_shade_layout();
	var htmls = '<div id="BDIWFYBUIWBFNQP" class="shade_layout" ></div><div id="NIUGSIHNAWQIPOHFC" class="pop_windows" style="width: 500px; margin-left: -250px; margin-top: -83px;"><div class="layout"><div class="main"><div class="bd_p10 clearfix"><div class="popup_tips_title pl150 mt25 mb20 ml20"><b class="ico waiting fl"></b><span class="fl ml_65">'+message+'</span></div></div><div class="freightTemplet_btns mt15"></div></div></div></div>';
	$('body').append(htmls);
	pop_center ('560');
}
/**
 * 显示确认框
 * @param ta 要显示的信息
 * @param tb 点击确定之后的函数名
 */
function showConfirm(ta,tb){
	closed_add_shade_layout();
	close();
	var htmls = '<div id="BDIWFYBUIWBFNQP" class="shade_layout" ></div><div id="NIUGSIHNAWQIPOHFC" class="pop_windows" style="width: 560px; margin-left: -280px; margin-top: -101px;"> <div class="layout"> <b class="ico_close" onclick="closed_add_shade_layout()">关闭</b> <div class="main"> <div class="bd_p10 clearfix"> <div class="popup_tips_title mt25 mb20 ml20"> <b class="ico"></b> </div> <p class="tac fontSize120 mb20">'+ta+'</p> </div> <div class="freightTemplet_btns mt15"> <input type="button" value="Yes" class="btnG bwt100 bht30 bfz16 btrau" onclick="closed_add_shade_layout('+tb+')"> <input type="button" value="No" onclick="closed_add_shade_layout()" class="canbtn"> </div> </div> </div> </div>';
	$('body').append(htmls);
	// 登录弹出居中
	pop_center2('560');
}
//清除确认框
function closed_add_shade_layout(callback){
	if(callback==null){
		if($('#BDIWFYBUIWBFNQP').length>0){
			$('#BDIWFYBUIWBFNQP').detach();
		}
		if($('#NIUGSIHNAWQIPOHFC').length>0){
			$('#NIUGSIHNAWQIPOHFC').detach();
		}
	/*	if($('.shade_layout').length>0){
			 $('.shade_layout').hide();
		}*/
	}
	eval(callback);
}
function close(){
	$(".shade_layout").not('#BDIWFYBUIWBFNQP').hide();
	$(".pop_windows").hide();
}

function outputmoney(number) {
	number = number.replace(/\,/g, "");
	if(isNaN(number) || number == "")return "";
	number = Math.round(number * 100) / 100;
	if (number < 0)
		return '-' + outputdollars(Math.floor(Math.abs(number) - 0) + '') + outputcents(Math.abs(number) - 0);
	else
		return outputdollars(Math.floor(number - 0) + '') + outputcents(number - 0);
	} 
//格式化金额
function outputdollars(number) {
	if (number.length <= 3)
		return (number == '' ? '0' : number);
	else {
		var mod = number.length % 3;
		var output = (mod == 0 ? '' : (number.substring(0, mod)));
		for (i = 0; i < Math.floor(number.length / 3); i++) {
		if ((mod == 0) && (i == 0))
			output += number.substring(mod + 3 * i, mod + 3 * i + 3);
		else
			output += ',' + number.substring(mod + 3 * i, mod + 3 * i + 3);
		}
		return (output);
	}
}
function outputcents(amount) {
	amount = Math.round(((amount) - Math.floor(amount)) * 100);
	return (amount < 10 ? '.0' + amount : '.' + amount);
}
function changeProvince(){
	var root = $("#rootsite").val();
	var provinceName = $("#province").val();
	$("#city").html("");
	$.post(root+"/manager/changeprovince", {"provinceName":provinceName},
		function(data){
			var jsonObjArr = JSON.parse(data);
		    for(var i in jsonObjArr){
		    	 $("#city").append("<option value='"+jsonObjArr[i].cityId+"'>"+jsonObjArr[i].name+"</option>");  
		    }
	    }
	);
}
function changePopProvince(obj){
	var root = $("#rootsite").val();
	var provinceName = $(obj).val();
	$(obj).parent().find("select[name='city']").html("");
	$.post(root+"/manager/changeprovince", {"provinceName":provinceName},
		function(data){
	  	console.log(data)
			var jsonObjArr = JSON.parse(data);
		    for(var i in jsonObjArr){
		  
		    	$(obj).parent().find("select[name='city']").append("<option value='"+jsonObjArr[i].cityId+"'>"+jsonObjArr[i].name+"</option>");  
		    }
		    $(obj).parent().find("select[name='city']").change();
	    }
	);
}
/**
 * 生成APP首页数据
 */
function generator(){
	var root = $("#rootsite").val();
	$.post(root+"/general/generating", {},
			function(data){
				var jsonObjArr = JSON.parse(data);
				if(jsonObjArr.code=="1"){
					alertWarn("Generate success!");
				}
				else{
					alertWarn("Error!");
				}
		    }
	);
}

/**
 * 列表全选功能
 */
function chooseAllCheckbox(){
	var flag = 1;
	$("input[name=radio]").each(function(){
		if(!$(this).prop("checked") && !$(this).prop("disabled")){
            $(this).prop("checked",true);
			flag = 0;
		}
	});
	if(flag == 1){
		$("input[name=radio]").each(function(){
			$(this).prop("checked",false);
		});
	}
}
//侧边栏
$(function () { 
	$('#toppanel').css({"right":($(document.body).outerWidth(true)-1200)/2-30+"px", "bottom":"0px"});  
	$(window).scroll(function(){  
		if ($(window).scrollTop()>100){  
			$("#sidepanel").fadeIn(1000);
		}  
		else  
		{  
			$("#sidepanel").fadeOut(1000);  
		}  
	});  

	//当点击跳转链接后，回到页面顶部位置  
	$("#sidepanel a.gototop").click(function(){  
		$('body,html').animate({scrollTop:0},1000);  
		return false;  
	});  
	
	$(".like_type_list span").click(function() {
		var $item = $(this).attr("class");
		if ($item == "selected1") {
			$(this).removeClass("selected1");
		} else {
			$(this).addClass("selected1");
		}
	});
});  

