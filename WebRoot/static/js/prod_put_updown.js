/*
 * 动态绑定日期控件
 */
$(function() {
	$( "#rackOnStartTimeStr" ).datepicker({
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
			$( "#rackOnEndTimeStr" ).datepicker( "option", "minDate", selectedDate );
		}
	});
	$( "#rackOnEndTimeStr" ).datepicker({
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
			$( "#rackOnStartTimeStr" ).datepicker( "option", "maxDate", selectedDate );
		}
	});
	
	$( "#rackDownStartTimeStr" ).datepicker({
		showOtherMonths: true,
		//defaultDate: "+1w",
		changeMonth: true,
		dateFormat:"yy-mm-dd",
		showButtonPanel: true,
		numberOfMonths: 1,
//		beforeShow:function(){
//			$( "#rackDownStartTimeStr" ).datepicker( "option", "maxDate", new Date() );
//		},
		onClose: function( selectedDate ) {
			//$( "#rackDownEndTimeStr" ).datepicker( "option", "minDate", selectedDate );
		}
	});
	$( "#rackDownEndTimeStr" ).datepicker({
		showOtherMonths: true,
		//defaultDate: "+1w",
		changeMonth: true,
		dateFormat:"yy-mm-dd",
		showButtonPanel: true,
		numberOfMonths: 1,
//		beforeShow:function(){
//			$( "#rackDownEndTimeStr" ).datepicker( "option", "minDate", new Date() );
//		},2
		onClose: function( selectedDate ) {
			$( "#rackDownStartTimeStr" ).datepicker( "option", "maxDate", selectedDate );
		}
	});
//	$( "#timeupupup" ).datetimepicker({showSecond:true,changeMonth : true,dateFormat:"yy-mm-dd",timeFormat: "HH:mm:ss"});
//	$( "#timedowndowndown" ).datetimepicker({showSecond:true,changeMonth : true,dateFormat:"yy-mm-dd",timeFormat: "HH:mm:ss"});
	$("#updateStorage").click(function(){
		$("#skuStorageTbody").find("tr").each(function(){
		});
	});
	
	$("#merchantName").on("keyup",function(){
		var searchKey = $("#searchk").val();
		var root = $("#rootsite").val();
		$.post(root+"/product/searchMer", {"searchKey":searchKey},
				function(data){
				var jsonObjArr = JSON.parse(data);
				$("#merchant_list").html("");
				for(var i in jsonObjArr){
					$("#merchant_list").append('<option value="'+jsonObjArr[i].name+'"/>');
				}
		});
	});
	$("#searchk").on("keyup",function(){
		var searchKey = $("#searchk").val();
		var root = $("#rootsite").val();
		$.post(root+"/product/searchMer", {"searchKey":searchKey},
				function(data){
				var jsonObjArr = JSON.parse(data);
				$("#storeTbody tbody").html("");
				for(var i in jsonObjArr){
					$("#storeTbody tbody").append('<tr><td onclick="choseStores(this)"><input type="checkbox" name="storeCbox" onclick="choseStores($(this).parent())" value="'+jsonObjArr[i].merchantId+'"><span class="ml10">'+jsonObjArr[i].name+'</span></td></tr>');
				}
		});
	});
	$("#prodStatus").val($("#d_prod_status").val());
	
});
function clearTime(id){
	$("input[id="+id+"]").val("");
}
var goThisPage=function(ta) {
	var currentPage = '';
	var isDesc = $("#isDesc").val();
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
			$("#pageNm_tip").text('Please input Positive integer!');
			return;
		}
		var pageSize = $("#page_pageSize").val();
		var totalRow = $("#page_totalRows").val();
		var totalPage = Math.ceil(parseInt(totalRow)/parseInt(pageSize));
		if (parseInt(currentPage) > totalPage) {
			$("#pageNm_tip").text('The total page is :' + totalPage);
			return;
		}
	}else if(ta == 'status0'){
		currentPage = 1;
		$("#prodStatus").find("option[value='']").prop("selected",true);
	}else if(ta == 'status1'){
		currentPage = 1;
		$("#prodStatus").find("option[value='1']").prop("selected",true);
	}else if(ta == 'status2'){
		currentPage = 1;
		$("#prodStatus").find("option[value='2']").prop("selected",true);
	}else if(ta == 'sort'){
		currentPage = 1;
		if($("#isDesc").val()=="0"){
			isDesc = "1";
		}
		else if($("#isDesc").val()=="1"){
			isDesc = "2";
		}else{
			isDesc = "1";
		}
	}
	else{
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
	//商品类型
	var productType = $("#productType").val();
	// 商户ID
	var merchantId = $("#merchantId").val();
	// 商户名称
	var merchantName = $("#merchantName").val();
	// 城市
	var city = $("#city").val();
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
	"isNotEnough":isNotEnough,
	"productType":productType,
	"merchantId":merchantId,
	"merchantName":merchantName,
	"city":city,
	isDesc:isDesc
	},
	function(data){
		$("#productListDiv").empty().html(data);
		$(".sortArea ul").find("li").each(function(){
			$(this).removeClass("selcted");
		});
		if(prodStatus==""){
			$(".sortArea ul li").eq(0).addClass("selcted");
		}
		else if(prodStatus=="2"){
			$(".sortArea ul li").eq(1).addClass("selcted");
		}
		else if(prodStatus=="1"){
			$(".sortArea ul li").eq(2).addClass("selcted");
		}
		
		if(isDesc=="2"){
			$("#isDesc").val("2");
			if(!$(".dot-bottom").hasClass("checked")){
				$(".dot-bottom").addClass("checked");
			}
			$(".dot-top").removeClass("checked");
		}
		else if(isDesc=="1"){
			$("#isDesc").val("1");
			if(!$(".dot-top").hasClass("checked")){
				$(".dot-top").addClass("checked");
			}
			$(".dot-bottom").removeClass("checked");
			
		}
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
 * 查看SKU库存
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
				var info = "<tr><td style='width:20%'>" + jsonArr[i].PROD_ID 
				        + "</td><td style='width:20%'>" + jsonArr[i].PROD_PROP_VAL 
				        + "</td><td style='width:22%'>" + jsonArr[i].PROD_STORAGE
				        + "</td><td style='width:19%'>" + jsonArr[i].SKU_ORIGIN_PRICE 
				        + "</td><td style='width:19%'>" + jsonArr[i].SKU_PRICE 
				        + "</td></tr>";
				returnInfo += info;
			}
			
			$("#skuStorageTbody").html(returnInfo);
			$("#skuStorageDiv1").css("display","block");
			$(".shade_layout").show();
			pop_center('790');
		}
	});
}

/**
* 查看商品库存
* @param prodId
*/
function searchProdStorage(prodId) {
	$.ajax({
		url : getRootPath() + "/product/search_prod_storage?prodId=" + prodId+ '&time='+ new Date().getTime(),
		type : "GET",
		dataType : "json",
		success : function(result) {
			var jsonArr = eval(result);
			var returnInfo = "";
			for(var i = 0; i < jsonArr.length; i++) {
				var info = "<tr><td style='width:20%'>" + jsonArr[i].PROD_ID  
						+ "</td><td style='width:20%'>" + jsonArr[i].PROD_PROP_VAL 
						+ "</td><td style='width:20%'>" + jsonArr[i].PROD_STORAGE 
						+ "</td><td style='width:20%'>" + jsonArr[i].SKU_ORIGIN_PRICE 
						+ "</td><td style='width:20%'>" + jsonArr[i].SKU_PRICE 
				        + "</td></tr>";
				returnInfo += info;
			}
			
			$("#skuStorageTbody4").html(returnInfo);
			$("#skuStorageDiv4").css("display","block");
			$(".shade_layout").show();
			pop_center('790');
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
				var info = "<tr><td style='width:20%'>" + jsonArr[i].PROD_ID + "<input type='hidden' name='prodStorageId' value='"+jsonArr[i].PROD_STORAGE_ID+"'></td>" 
				         + "<td style='width:20%'>" + jsonArr[i].PROD_PROP_VAL 
				         + "</td><td style='width:20%'><div class='amount_wrap'><a class='minus_ico' id='reduce' href='javascript:void(0)' onclick='reduceNum(this)'>-</a><input type='text' maxlength='9' class='width40' name='prodStorage' id='prodStorage' value='" + jsonArr[i].PROD_STORAGE + "'><a class='plus_ico'  id='increase' href='javascript:void(0)'  onclick='increaseNum(this)'>+</a></div></td>" 
				         + "<td style='width:20%'><div class='amount_wrap'><input style='width:70px' maxlength='10' type='text' name='SskuOriginPrice' value='" + jsonArr[i].SKU_ORIGIN_PRICE + "'></div></td>"
				         + "<td style='width:20%'><div class='amount_wrap'><input style='width:70px' maxlength='10' type='text' name='SskuPrice' value='" + jsonArr[i].SKU_PRICE + "'></div>"
				         + "</td></tr>";
				returnInfo += info;
			}
			
			$("#skuStorageTbody2").html(returnInfo);
			$("#skuStorageDiv2").show();
			$(".shade_layout").show();
			pop_center('790');
		}
	});
}
/**
 * 修改商品库存
 */
function updateProdStorage(prodId){
	$.ajax({
		url : getRootPath() + "/product/search_prod_storage?prodId=" + prodId+ '&time='+ new Date().getTime(),
		type : "GET",
		dataType : "json",
		success : function(result) {
			var jsonArr = eval(result);
			var returnInfo = "";
			for(var i = 0; i < jsonArr.length; i++) {
				var info = "<tr><td style='width:20%'>" + jsonArr[i].PROD_ID + "<input type='hidden' name='prodStorageId' value='"+jsonArr[i].PROD_STORAGE_ID
				       +"'></td><td style='width:20%'><input type='hidden' name='cityId' value='"+jsonArr[i].ADDRESS_ID+"'>" + jsonArr[i].PROD_PROP_VAL 
				        + "</td><td style='width:22%'><div class='amount_wrap'><a class='minus_ico' id='reduce' href='javascript:void(0)' onclick='reduceNum(this)'>-</a><input type='text' maxlength='9' class='width40' name='prodStorage' id='prodStorage' value='" + jsonArr[i].PROD_STORAGE + "'><a class='plus_ico'  id='increase' href='javascript:void(0)'  onclick='increaseNum(this)'>+</a></div></td>" 
				        + "</td><td style='width:19%'><div class='amount_wrap'><input style='width:70px' maxlength='10' type='text' name='SprodOriginPrice' value='" + jsonArr[i].SKU_ORIGIN_PRICE + "'></div>"
						+ "</td><td style='width:19%'><div class='amount_wrap'><input style='width:70px' maxlength='10' type='text' name='SprodPrice' value='" + jsonArr[i].SKU_PRICE+ "'></div>"
						+ "</td></tr>";
				returnInfo += info;
			}
			
			$("#skuStorageTbody3").html(returnInfo);
			$("#skuStorageDiv3").css("display","block");
			$(".shade_layout").show();
			pop_center('790');
		}
	});
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
		jsondrr[i] = $(this).find("td").eq(0).text()
		           + ",,,"+$(this).find("td").eq(0).find("input[name='prodStorageId']").val()
		           + ",,,"+$(this).find("td").find("div input[name=prodStorage]").val()
		 		   + ",,,"+$(this).find("td").find("div input[name=SskuOriginPrice]").val()
		 		   + ",,,"+$(this).find("td").find("div input[name=SskuPrice]").val();
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
/**
 * 修改商品库存
 * @param Obj
 */
function beginUpdateProdStorage(Obj) {
	var jsondrr = new Array();
	var i=0;
	$("#skuStorageTbody3").find("tr").each(function(){
		jsondrr[i] = $(this).find("td").eq(0).text()+",,,"+$(this).find("td").eq(0).find("input[name='prodStorageId']").val()
		      	   + ",,,"+$(this).find("td").find("div input[name=prodStorage]").val()
		      	   + ",,,"+$(this).find("td").find("div input[name=SprodOriginPrice]").val()
		      	   + ",,,"+$(this).find("td").find("div input[name=SprodPrice]").val();
		i++;
	});
	$.ajax({
			type : "POST",
		//contentType : 'charset=utf-8',
		url : "update_prod_storage",
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
	var url = getRootPath()+"/product/prod_put_up_down";
	$.post(url,{"prodId":prodId,"action":"up","currentPage":currentPage},function(data){
		goThisPage('search');
		var status = $("#status_hide").val();
		$("select option").each(function (){  
			if($(this).val()==status){   
				$(this).attr("selected",true); 
		}});
		//closed();
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
		goThisPage('search');
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
	var url = getRootPath()+"/product/prod_put_up_down";
	$.post(url,{"prodId":prodId,"action":"down","currentPage":currentPage},function(data){
		goThisPage('search');
		var status = $("#status_hide").val();
		$("select option").each(function (){  
			if($(this).val()==status){   
				$(this).attr("selected",true); 
		}});
		//closed();
	});
}
function putDownGoods(prodId,operaction){
	$(".shade_layout").show();
	$("#putgoodsdown").show();
	pop_center ('560');
	$("#downdownsubmit").attr("myvalue",prodId);
}
function checkstore(prodid,operation){
	$.post(getRootPath()+"/product/checkstorestatus",{"prodId":prodid},function(data){
		var jsonObjArr = JSON.parse(data);
		if(jsonObjArr.code=="-1"){
			//closed();
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
function deleteProd(prodId){
	var url = getRootPath()+"/product/deleteProd?prodId="+prodId;
	$.post(url,{},function(data){
		goThisPage('search');
	});
}
//修改、删除、上下架商品确认弹出框
function tShowCanclePop(prodId,operaction){
	$(".shade_layout").show();
	$("#open_returnPop").show();
	var opt="";
	var url="";
	if(operaction=="updategoods"){
		opt="Are you sure to edit product?";
		$("#formprodId").attr("value",prodId);
		url="javascript:$('#updategoods').submit();";
	}
	if(operaction=="deleteProd"){
		opt="Are you sure to delete product?";
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
			"<input type='button' value='OK' onclick=\""+url+"\"  class='btn btn-sm btn-sgGreen'>"+
			"<input type='button' value='Cancel' class='btn btn-sm btn-grey' onclick='closed()'>"+
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
                	"<div class='popup_tips_title pl150 mt25 mb20 ml20'><b class='ico fl'></b><span class='fl ml15'>Prompt</span></div><p class='tac fontSize120 colorOrange mb20'>"+message+"</p></div>"+
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

/**
 * 关联商品函数
 */
function relateProducts(prodId) {
	var url = getRootPath() + "/product/prod_relate_prod?prodId=" + prodId;
	window.location.href = url;
}

$(document).delegate(".sortDiv","click",function(){  
});
function batsPutOn(){
	var flag = 0;
	var prodIds = "";
	$("input[name=radio]").each(function(){
		if($(this).prop("checked")){
			flag = 1;
			if(prodIds == ""){
				prodIds = $(this).val();
			}
			else{
				prodIds =prodIds + ",,," + $(this).val();
			}
		}
	});
	if(flag ==0){
		closed_add_shade_layout();
		alertWarn("Please at lease chose a Product");
		return 0;
	}
	showConfirm('Are you sure to put on these product?','begiputUpGoods(\''+prodIds+'\')');
	//$(".shade_layout").show();
	
}
function batsPutDown(){
	var flag = 0;
	var prodIds = "";
	$("input[name=radio]").each(function(){
		if($(this).prop("checked")){
			flag = 1;
			if(prodIds == ""){
				prodIds = $(this).val();
			}
			else{
				prodIds =prodIds + ",,," + $(this).val();
			}
		}
	});
	if(flag ==0){
		alertWarn("Please at lease chose a Product");
		return 0;
	}
	showConfirm('Are you sure to put to out of stock?','beginputDownGoods(\''+prodIds+'\')');
}

function batsDelete(){
	var flag = 0;
	var prodIds = "";
	$("input[name=radio]").each(function(){
		if($(this).prop("checked")){
			flag = 1;
			if(prodIds == ""){
				prodIds = $(this).val();
			}
			else{
				prodIds =prodIds + ",,," + $(this).val();
			}
		}
	});
	if(flag ==0){
		alertWarn("Please at lease chose a Product");
		return 0;
	}
	showConfirm('Are you sure to delete these product?','deleteProd(\''+prodIds+'\')');
}
function batsCopy(){
	var flag = 0;
	$("input[name=radio]").each(function(){
		if($(this).prop("checked")){
			flag = 1;
		}
	});
	if(flag ==0){
		alertWarn("Please at lease chose a Product");
		return 0;
	}
	$(".shade_layout").show();
	$("#copypopwin").show();
	$("#storeTbody").html("");
	$.post(getRootPath()+"/product/getAllStore",{},function(data){
		var jsonObjArr = JSON.parse(data);
		for(var i in jsonObjArr){
			$("#storeTbody").append('<tr><td onclick="choseStores(this)"><input type="checkbox" name="storeCbox" onclick="choseStores($(this).parent())" value="'+jsonObjArr[i].merchantId+'"><span class="ml10">'+jsonObjArr[i].name+'<span></td></tr>');
		}
	});
	pop_center('460');
	
}

function choseStores(obj){
	if($(obj).find("input[type='checkbox']").prop("checked")){
		$(obj).find("input[type='checkbox']").prop("checked",false);
		$(obj).css("background-color","#ffffff");
	}
	else{
		$(obj).find("input[type='checkbox']").prop("checked",true);
		$(obj).css("background-color","#f7f7f7");
	}
}

function beginCopyOpt(){
	close();
	processWait("process...");
	var prodIds = "";
	var merchantIds = "";
	$("input[name=radio]").each(function(){
		if($(this).prop("checked")){
			if(prodIds == ""){
				prodIds = $(this).val();
			}
			else{
				prodIds = prodIds + ",,," + $(this).val();
			}
		}
	});
	
	$("input[name=storeCbox]").each(function(){
		if($(this).prop("checked")){
			if(merchantIds == ""){
				merchantIds = $(this).val();
			}
			else{
				merchantIds = merchantIds + ",,," + $(this).val();
			}
		}
	});
	$.post(getRootPath()+"/product/beginCopyOpt",{"prodIds":prodIds,"merchantIds":merchantIds},function(data){
		var jsonObjArr = JSON.parse(data);
		if(jsonObjArr.code=="1"){
			goThisPage('search');
			close();
			alertWarn("success");
		}
	});
	
}
