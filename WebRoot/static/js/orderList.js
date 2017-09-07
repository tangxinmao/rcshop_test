/*
 * 动态绑定日期控件
 */
$(function() {
	$( "#orderTimeStart" ).datepicker({
		showOtherMonths: true,
		//defaultDate: "+1w",
		changeMonth: true,
		dateFormat:"yy-mm-dd",
		showButtonPanel: true,
		numberOfMonths: 1,
//		beforeShow:function(){
//			$( "#rackOnStartTimeStr" ).datepicker( "option", "maxDate", new Date() );
//		},
		onClose: function( selectedDate ) {
			$( "#orderTimeStart" ).datepicker( "option", selectedDate );
		}
	});
	$( "#orderTimeEnd" ).datepicker({
		showOtherMonths: true,
		//defaultDate: "+1w",
		changeMonth: true,
		dateFormat:"yy-mm-dd",
		showButtonPanel: true,
		numberOfMonths: 1,
//		beforeShow:function(){
//			$( "#rackOnEndTimeStr" ).datepicker( "option", "minDate", new Date() );
//		},
		onClose: function( selectedDate ) {
			$( "#orderTimeEnd" ).datepicker( "option", selectedDate );
		}
	});

//	$( "#timeupupup" ).datetimepicker({showSecond:true,changeMonth : true,dateFormat:"yy-mm-dd",timeFormat: "HH:mm:ss"});
//	$( "#timedowndowndown" ).datetimepicker({showSecond:true,changeMonth : true,dateFormat:"yy-mm-dd",timeFormat: "HH:mm:ss"});
	$("#updateStorage").click(function(){
		$("#skuStorageTbody").find("tr").each(function(){
		})
	});
	$("#prodStatus").val($("#d_prod_status").val());
	
});
function clearTime(id){
	$("input[id="+id+"]").val("");
}
var goThisPage=function(ta) {
	var currentPage = '';
	if(ta == 'search'){
		currentPage = 1;
	}else if(ta == '+'){//下一页
		//当前页
		currentPage = parseInt($('#currentPage').val()) + 1;
	}else if(ta == '-'){
		currentPage = parseInt($('#currentPage').val()) - 1;
	}else if(ta == '='){
		currentPage = $("#page_currentPage").val();
		var reg =/^\d+$/;
		if(!currentPage.match(reg) || parseInt(currentPage) <= 0 ){
			$("#pageNm_tip").text('输入跳转页数必须为大于0的正整数');
			return;
		}
		var pageSize = $("#page_pageSize").val();
		var totalRow = $("#page_totalRows").val();
		var totalPage = Math.ceil(parseInt(totalRow)/parseInt(pageSize));
		if (parseInt(currentPage) > totalPage) {
			$("#pageNm_tip").text('输入跳转页数大于最大页数' + totalPage);
			return;
		}
	}else{
		currentPage = ta;
	}
	//搜索条件
	//商品名称
	var prodName = $("#prodName").val();
	//商品编号
	var prodId = $("#prodId").val();
	//商品状态
	var prodStatus = $("#prodStatus").val();
	//上架时间
	var rackOnStartTimeStr = $("#rackOnStartTimeStr").val();
	var rackOnEndTimeStr = $("#rackOnEndTimeStr").val();
	//下架时间
	var rackDownStartTimeStr = $("#rackDownStartTimeStr").val();
	var rackDownEndTimeStr = $("#rackDownEndTimeStr").val();
	
	//用于在main_index.vm中查询库存不足的商品的需要
	var isNotEnough = $("#d_is_Not_Enough").val();
$.post(getRootPath()+"/product/product_list",{"currentPage":currentPage,
	"prodName":prodName,
	"prodId":prodId,
	"prodStatus":prodStatus,
	"rackOnStartTimeStr":rackOnStartTimeStr,
	"rackOnEndTimeStr":rackOnEndTimeStr,
	"rackDownStartTimeStr":rackDownStartTimeStr,
	"rackDownEndTimeStr":rackDownEndTimeStr,
	"isNotEnough":isNotEnough
	},
	function(data){
		$("#productListDiv").empty().html(data);
		
		var status = $("#status_hide").val();
		$("select option").each(function (){  
			if($(this).val()==status){   
				$(this).attr("selected",true); 
		}}); 
	});
};

function getRootPath(){
	return $('#rootPath').val();
}

/**
 * 查看库存
 * @param prodId
 */
function searchSkuStorage(prodId) {
	$.ajax({
		url : getRootPath() + "/product/search_sku_storage?prodId=" + prodId+ '&time='+ new Date().getTime(),
		type : "GET",
		dataType : "json",
		success : function(result) {
			var jsonArr = eval(result);
			var returnInfo = "";
			for(var i = 0; i < jsonArr.length; i++) {
				var info = "<tr><td width='202'>" + jsonArr[i].PROD_ID + "</td><td width='189'>" + jsonArr[i].PROD_PROP_VAL + "</td><td width='105'>" + jsonArr[i].PROD_STORAGE + "</td><td>" + jsonArr[i].SKU_PRICE + "</td></tr>";
				returnInfo += info;
			}
			
			$("#skuStorageTbody").html(returnInfo);
			$("#skuStorageDiv1").css("display","block");
			$(".shade_layout").show();
			pop_center('690');
		}
	});
}


/**
 * 修改SKU库存
 * @param prodId
 */
function updateSkuStorage(prodId) {
	$.ajax({
		url : getRootPath() + "/product/search_sku_storage?prodId=" + prodId + '&time='+ new Date().getTime(), 
		type : "GET",
		dataType : "json",
		success : function(result) {
			var jsonArr = eval(result);
			var returnInfo = "";
			for(var i = 0; i < jsonArr.length; i++) {
				var info = "<tr><td width='202'>" + jsonArr[i].PROD_ID + "</td><td width='189'>" + jsonArr[i].PROD_PROP_VAL + "</td><td width='105'><div class='amount_wrap'><a class='minus_ico' id='reduce' href='javascript:void(0)' onclick='reduceNum(this)'>-</a><input type='text' class='width40' name='prodStorage' id='prodStorage' value='" + jsonArr[i].PROD_STORAGE + "'><a class='plus_ico'  id='increase' href='javascript:void(0)'  onclick='increaseNum(this)'>+</a></div></td><td>" + jsonArr[i].SKU_PRICE + "</td></tr>";
				returnInfo += info;
			}
			
			$("#skuStorageTbody2").html(returnInfo);
			$("#skuStorageDiv2").show();
			$(".shade_layout").show();
			pop_center('690');
		}
	});
}
/**
 * 修改商品库存
 */
function updateProdStorage(Obj){
	var info = "<tr><td width='202'>" + $(Obj).parent().parent().find("td").eq(1).text() + "</td><td width='189'>" + $(Obj).parent().parent().find("td").eq(0).text() + "</td><td width='105'><div class='amount_wrap'><a class='minus_ico' id='reduce' href='javascript:void(0)' onclick='reduceNum(this)'>-</a><input type='text' class='width40' name='prodStorage' id='prodStorage' value='" + trim($(Obj).parent().parent().find("td").eq(3).text()) + "'><a class='plus_ico'  id='increase' href='javascript:void(0)'  onclick='increaseNum(this)'>+</a></div></td><td>" + $(Obj).parent().parent().find("td").eq(3).text() + "</td></tr>";
	$("#skuStorageTbody3").html(info);
	$("#skuStorageDiv3").show();
	$(".shade_layout").show();
	pop_center('690');
}
function reduceNum(e){
	//$("#prodStorage").attr("value",parseInt(value)-1);
	var value = $(e).parent().children("input").val();
	if(parseInt(value)<=0){
		alert("库存不能为负数！");
		return 0;
	}
	 $(e).parent().children("input").attr("value",parseInt(value)-1);
	
}
function increaseNum(e){
	var value = $(e).parent().children("input").val();
	 $(e).parent().children("input").attr("value",parseInt(value)+1);
}

function beginUpdateSkuStorage(Obj) {
	var jsondrr = new Array();
	var i=0;
	$("#skuStorageTbody2").find("tr").each(function(){
		jsondrr[i] = $(this).find("td").eq(0).text()+",,,"+$(this).find("td").find("div input[name=prodStorage]").val();
		i++;
	});
	$.ajax({
			type : "POST",
		//contentType : 'charset=utf-8',
		url : "update_sku_storage",
		dataType : 'json',
		cache : false,
		data : {
			json : JSON.stringify(jsondrr),
		},
		success : function(data) {
			var jsonObjArr = JSON.parse(data);
			if(jsonObjArr.code=="1"){
				goThisPage("1");
				closeStorageDiv();
			}
			else{
				alert("修改失败，请联系系统管理员！");
			}
		}
	});
	
}
function beginUpdateProdStorage(Obj) {
	var jsondrr = new Array();
	var i=0;
	$("#skuStorageTbody3").find("tr").each(function(){
		jsondrr[i] = $(this).find("td").eq(0).text()+",,,"+$(this).find("td").find("div input[name=prodStorage]").val();
		i++;
	});
	$.ajax({
			type : "POST",
		//contentType : 'charset=utf-8',
		url : "update_sku_storage",
		dataType : 'json',
		cache : false,
		data : {
			json : JSON.stringify(jsondrr),
		},
		success : function(data) {
			var jsonObjArr = JSON.parse(data);
			if(jsonObjArr.code=="1"){
				goThisPage($("#currentPage").val());
				closeStorageDiv();
			}
			else{
				alert("修改失败，请联系系统管理员！");
			}
		}
	});
	
}

/**
 * 关闭查看库存DIV
 */
function closeStorageDiv() {
	$(".shade_layout").hide();
	$(".pop_windows").hide();
}
//去左空格;   
function ltrim(s){     
   return s.replace(/(^\s*)/, "");  
}   
//去右空格;   
function rtrim(s){   
 return s.replace(/(\s*$)/, "");  
}   
//去左右空格;   
function trim(s){  
  //s.replace(/(^\s*)|(\s*$)/, "");  
 return rtrim(ltrim(s));   

}

/**
 * 上架商品
 * @param prodId
 */
function begiputUpGoods(prodId,opt){
	var currentPage = $("#currentPage").val();
	var url = getRootPath()+"/product/prod_put_up_down?prodId="+prodId+"&action=up&currentPage="+currentPage;
	$.post(url,{},function(data){
		if(data=="-1"){
			
		}
		if(opt=="putUpPublish"){
			self.location.href = getRootPath()+"/product/init";
		}
		$("#productListDiv").empty().html(data);
		var status = $("#status_hide").val();
		$("select option").each(function (){  
			if($(this).val()==status){   
				$(this).attr("selected",true); 
		}});
		closed();
	});
}
function begiputUpDownGoods(Obj){
	var prodId = $(Obj).attr("myvalue");
	var currentPage = $("#currentPage").val();
	
	var putawayTime = $("#timeupupup").val();
	var putdownTime = $("#timedowndowndown").val();
	var startdate=new Date(putawayTime.replace(/-/g,"/"));
    var enddate=new Date(putdownTime.replace(/-/g,"/"));             
   // var date = new Date(); 
       
       if(startdate>enddate)
       {
    	   $(".shade_layout").hide();
   		   $("#putgoodsup").hide();
    	   prompt("商品下架时间不能在上架时间前！");
           return false;
       }
	var url = getRootPath()+"/product/prod_put_up_down?prodId="+prodId+"&action=up&currentPage="+currentPage+"&putawayTime="+putawayTime
				+"&putdownTime="+putdownTime;
	$.post(url,{},function(data){
		$("#productListDiv").empty().html(data);
		var status = $("#status_hide").val();
		$("select option").each(function (){  
			if($(this).val()==status){   
				$(this).attr("selected",true); 
		}});
		$(".shade_layout").hide();
		$("#putgoodsup").hide();
	});
	
}
//定时上下架
function putUpDownGoodsByTime(prodId){
	$(".shade_layout").show();
	$("#putgoodsup").show();
	pop_center ('560');
	$("#upupsubmit").attr("myvalue",prodId);
}
function putUpGoods(prodId,operation){
	tShowCanclePop(prodId,operation);
	
//	var url=getRootPath()+"/product/checktempate/"+prodId;
//	$.post(url,{},function(data){
//		var jsonObjArr = JSON.parse(data);
//		if(jsonObjArr.code=="1"){
//			checkstore(prodId,operaction);
//		}
//		else{
//			prompt("商品无运费模板，请添加运费模板后执行上架操作！");
//		}
//	});
}
function beginputDownGoods(prodId){
	var currentPage = $("#currentPage").val();
	var url = getRootPath()+"/product/prod_put_up_down?prodId="+prodId+"&action=down&currentPage="+currentPage;
	$.post(url,{},function(data){
		$("#productListDiv").empty().html(data);
		var status = $("#status_hide").val();
		$("select option").each(function (){  
			if($(this).val()==status){   
				$(this).attr("selected",true); 
		}});
		closed();
	});
}
function putDownGoods(prodId,operaction){
	$(".shade_layout").show();
	$("#putgoodsdown").show();
	pop_center ('560');
	$("#downdownsubmit").attr("myvalue",prodId);
}
function checkstore(prodid,operation){
	$.post(getRootPath()+"/product/checkstorestatus",{},function(data){
		var jsonObjArr = JSON.parse(data);
		if(jsonObjArr.code=="-1"){
			closed();
			if(operation=="updategoods"){
				prompt("当前店铺状态不允许修改商品！");
			}
			else {
				prompt("当前店铺状态不允许上架商品！");
			}
			return 0;
		}
		else{
			tShowCanclePop(prodid,operation);
		}
	});
}
//修改、删除、上下架商品确认弹出框
function tShowCanclePop(prodId,operaction){
	$(".shade_layout").show();
	$("#open_returnPop").show();
	var opt="";
	var url="";
	if(operaction=="updategoods"){
		opt="是否确认修改商品";
		$("#formprodId").attr("value",prodId);
		url="javascript:$('#updategoods').submit();";
	}
	if(operaction=="deleteProd"){
		opt="是否确认删除商品";
		url="javascript:self.location='"+getRootPath()+"/product/"+"deleteProd?prodId="+prodId+"'";
	}
	if(operaction=="putUp"){
		opt="Are you sure to put on the product?";
		url="begiputUpGoods('"+prodId+"')";
	}
	if(operaction=="putUpPublish"){
		opt="是否确认上架商品";
		url="begiputUpGoods('"+prodId+"','putUpPublish')";
	}
	if(operaction=="putDown"){
		opt="Are you sure set the product to out of stock?";
		url="beginputDownGoods('"+prodId+"')";
	}
	var htmls = "<div class='layout'>"+
	"<b class='ico_close' onclick='closed()'>关闭</b>"+
	"<div class='main'>"+
		"<div class='bd_p10 clearfix'>"+
        	"<div class='popup_tips_title pl150 mt25 mb20 ml20'>"+
        	"<b class='ico fl'></b><span class='fl ml15'>Prompt</span></div>"+
            "<p class='tac fontSize120 colorOrange mb20'>"+opt+"</p>"+
		"</div>"+
		"<div class='freightTemplet_btns mt15'>"+
			"<input type='button' value='确定' onclick=\""+url+"\"  class='btn btn-sm btn-sgGreen'>"+
			"<input type='button' value='取消' class='btn btn-sm btn-grey' onclick='closed()'>"+
		"</div>"+
	"</div>"+
"</div>";
//	$("#merchantPopIframe").removeClass("height");
	$('#open_returnPop').empty().html(htmls);
//	var height = $("#merchantPopIframe").height();
//	$("#merchantPopIframe").css({height:height});
	pop_center ('560');
}
/**
 * 验证及提示框
 */
function prompt(message){
	var htmls = "<div class='layout'>"+
			"<b class='ico_close' onclick='closed()'>关闭</b>"+
			"<div class='main'>"+
				"<div class='bd_p10 clearfix'>"+
                	"<div class='popup_tips_title pl150 mt25 mb20 ml20'><b class='ico fl'></b><span class='fl ml15'>温馨提示</span></div><p class='tac fontSize120 colorOrange mb20'>"+message+"</p></div>"+
				"</div>"+
				"<div class='freightTemplet_btns mt15'>"+
					"<input type='button' value='确定' onclick='closed()' class='btn btn-sm btn-sgGreen'>"+
				"</div>"+
			"</div>"+
		"</div>";
	$(".shade_layout").show();
	$("#open_returnPop").css("display","block");
	$('#open_returnPop').empty().html(htmls);
	pop_center ('500');
}
function closed(){
	$("#open_returnPop").hide();
	$(".shade_layout").hide();
}

/**
 * 关联商品函数
 */
function relateProducts(prodId) {
	var url = getRootPath() + "/product/prod_relate_prod?prodId=" + prodId;
	window.location.href = url;
}



