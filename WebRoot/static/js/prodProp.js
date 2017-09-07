function showEnumList(pordPropId,obj){
	$("#prodPropId").val(pordPropId);
	var loading = '<div style="height:150px; text-align:center;"><h1 style="'+
		'">loading...</h1></div>';
	$("#enumbody").html(loading);
	if($(obj).hasClass("selectedC")){
		getEnumData(pordPropId);
	}else{
        $(obj).addClass("selectedC")
            getEnumData(pordPropId);
	}
}
function getEnumData(pordPropId){
	var root = $("#rootsite").val();
	$.post(root+"/prodprop/searchEnum", {"pordPropId":pordPropId},
			function(data){
			var jsonObjArr = JSON.parse(data);
			var html = "";
			for(var i in jsonObjArr){
				html = html + "<tr><td style='width:35.8%'>"+jsonObjArr[i].prodPropEnum+"</td><td style='width:31%'>"+jsonObjArr[i].seq+"</td>" + 
				       "<td style='width:35%'>"
					   +"<a href='javascript:void(0)' onclick='editOneEnumPopWin(\""+jsonObjArr[i].prodPropEnumId+"\",this)'>Edit</a> | " 
				       +"<a href='javascript:void(0)' onclick='showConfirm(&apos;Are you sure to delete the property? If you deleted,The Value belong to the property will be delete.&apos;,&quot;deleteOneEnum(&apos;"+jsonObjArr[i].prodPropEnumId+"&apos;)&quot;)'>Delete</a>" 
				       +"</td></tr>";
			}
			$("#enumbody").html(html);
	});
}

/**
 * 添加商品属性弹出层
 */
function addPropertyWin(){
	$(".shade_layout").show();
	$("#prodPropPopWin").show();
	$("#prodPropName").val("");
	$("#isColor").removeAttr("checked");
	$("#isApplicator").removeAttr("checked");
	pop_center ('560');
}

/**
 * 添加商品属性
 */
function saveProperty(){
	var root = $("#rootsite").val();
	var prodPropName = $("#prodPropName").val();
	var isSku = 0;
	var isColor = 0;
	var isApplicator = 0;
	if($("#isSku").prop('checked')){
		isSku = 1;
	}
	if($("#isColor").prop('checked')){
		isColor = 1;
	}
	if($("#isApplicator").prop('checked')){
		isApplicator = 1;
	}
	
	
	if(prodPropName==""){
		alertWarn("please input the value about Property Name!");
		return 0;
	}
	
	if($("#isApplicator").prop("checked") && !$("#isSku").prop("checked")){
		alertWarn("The applicator property also must be SKU proerty!");
		return 0;
	}
	
	
	$.post(root+"/prodprop/saveProperty", {"name":prodPropName,"isSku":isSku,"hasImg":isColor,"isPackService":isApplicator},
			function(data){
			var jsonObjArr = JSON.parse(data);
			if(jsonObjArr.code=="1"){
				goThisPage($('#currentPage').val());
				close();
			}
			else{
				alertWarn("fail,please contact the administrator");
			}
			
	});
}

/**
 * 添加商品属性枚举值弹出层
 */
function addEnumPopWin(){
	$(".shade_layout").show();
	$("#propEnumPopWin").show();
	$("#prodPropEnum").val("");
	$("#serialNum").val("");
	pop_center ('560');
}

/**
 * 添加枚举值
 */
function savePropEnum(){
	var root = $("#rootsite").val();
	var prodPropEnum = $("#prodPropEnum").val();
	var serialNum = $("#serialNum").val();
	var regInt = /^[0-9]+[0-9]*]*$/;  //正整数
	var prodPropId = $("#prodPropId").val();
	if(prodPropId==""){
		alertWarn("please select a Property before you added.");
		return 0;
	}
	
	if(prodPropEnum == ""){
		alertWarn("please input the value about Property Value Name!");
		return 0;
	}
	
	if(!regInt.test(serialNum)){
		alertWarn("please input integer about in value Serial No!");
		return 0;
	}
	
	$.post(root+"/prodprop/savePropEnum", {"prodPropId":prodPropId,"prodPropEnum":prodPropEnum,"seq":serialNum},
			function(data){
			var jsonObjArr = JSON.parse(data);
			if(jsonObjArr.code=="1"){
				getEnumData(prodPropId);
				close();
			}
			else{
				alertWarn("fail,please contact the administrator");
			}
			
	});
}
/**
 * 删除枚举值
 * @param prodPropEnumId 枚举值ID
 * @param obj DOM对象
 */
function deleteOneEnum(prodPropEnumId,obj){
	var root = $("#rootsite").val();
	var prodPropId = $("#prodPropId").val();
	$.post(root+"/prodprop/delPropEnum", {"prodPropId":prodPropId,"prodPropEnumId":prodPropEnumId},
			function(data){
			var jsonObjArr = JSON.parse(data);
			if(jsonObjArr.code=="1"){
				getEnumData(prodPropId);
				close();
			}
			else{
				alertWarn("fail,please contact the administrator");
			}
			
	});
}
/**
 * 修改枚举值弹出层
 * @param prodPropEnumId 枚举值ID
 * @param obj DOM对象
 */
function editOneEnumPopWin(prodPropEnumId,obj){
	$(".shade_layout").show();
	$("#updateEnumPopWin").show();
	$("#updateprodPropEnumId").val(prodPropEnumId);
	var $td = $(obj).parent().parent().find("td");
	$("#updatePropEnum").val($td.eq(0).text());
	$("#updateserialNum").val($td.eq(1).text());
	pop_center ('560');
}

/**
 * 修改枚举值
 */
function modifyPropEnum(){
	var root = $("#rootsite").val();
	var updateprodPropEnumId = $("#updateprodPropEnumId").val();
	var updatePropEnum = $("#updatePropEnum").val();
	var updateserialNum = $("#updateserialNum").val();
	var regInt = /^[0-9]+[0-9]*]*$/;  //正整数
	var prodPropId = $("#prodPropId").val();
	
	if(prodPropEnum == ""){
		alertWarn("please input the value about Property Value Name!");
		return 0;
	}
	
	if(!regInt.test(updateserialNum)){
		alertWarn("please input integer about in value Serial No!");
		return 0;
	}
	
	$.post(root+"/prodprop/modifyPropEnum", {"prodPropEnumId":updateprodPropEnumId,"prodPropEnum":updatePropEnum,"seq":updateserialNum},
			function(data){
			var jsonObjArr = JSON.parse(data);
			if(jsonObjArr.code=="1"){
				getEnumData(prodPropId);
				close();
			}
			else{
				alertWarn("fail,please contact the administrator");
			}
			
	});
}

/**
 * 删除商品属性及所属枚举值
 * @param prodPropId 商品属性ID
 * @param obj DOM对象
 */
function deleteOneProp(prodPropId,obj){
	var root = $("#rootsite").val();
	var isSku = 0;
	if($("#isSku").prop('checked')){
		isSku = 1;
	}
	$.post(root+"/prodprop/delProp", {"prodPropId":prodPropId,"isSku":isSku},
			function(data){
			var jsonObjArr = JSON.parse(data);
			if(jsonObjArr.code=="1"){
				goThisPage($('#currentPage').val());
				close();
			}
			else{
				alertWarn("fail,please contact the administrator");
			}
			
			
	});
}

/**
 * 设置是否是施工队
 */
function changeAppliStatus(){
	if($("#isApplicator").prop("checked")){
		$("#isSku").prop("checked",true);
	}
}

function editSkuPropPopup(prodPropId,obj){
	$(".shade_layout").show();
	$("#updatePopWin").show();
	$("#upprodPropId").val(prodPropId);
	var $td = $(obj).parent().parent().find("td");
	$("#upprodPropName").val($td.eq(0).text());
	var isColor = $.trim($td.eq(1).text());
	var isApplicator = $.trim($td.eq(2).text());
	if(isColor=="yes"){
		$("#upisColor").prop("checked","checked");
	}
	if(isApplicator=="yes"){
		$("#upisApplicator").prop("checked","checked");
	}
	pop_center ('560');
}

function editProdPropPopup(prodPropId,obj){
	$(".shade_layout").show();
	$("#updatePopWin").show();
	$("#upprodPropId").val(prodPropId);
	var $td = $(obj).parent().parent().find("td");
	$("#upprodPropName").val($td.eq(0).text());
	pop_center ('560');
}

/**
 * 添加商品属性
 */
function modifyProperty(){
	var root = $("#rootsite").val();
	var prodPropName = $("#upprodPropName").val();
	var isSku = 0;
	var isColor = 0;
	var isApplicator = 0;
	var prodPropId = $("#upprodPropId").val();
	if($("#upisSku").prop('checked')){
		isSku = 1;
	}
	if($("#upisColor").prop('checked')){
		isColor = 1;
	}
	if($("#upisApplicator").prop('checked')){
		isApplicator = 1;
	}
	
	
	if(prodPropName==""){
		alertWarn("please input the value about Property Name!");
		return 0;
	}
	
	if($("#upisApplicator").prop("checked") && !$("#upisSku").prop("checked")){
		alertWarn("The applicator property also must be SKU proerty!");
		return 0;
	}
	
	
	$.post(root+"/prodprop/modifyProperty", {"prodPropId":prodPropId,"name":prodPropName,"isSku":isSku,"hasImg":isColor,"isPackService":isApplicator},
			function(data){
			var jsonObjArr = JSON.parse(data);
			if(jsonObjArr.code=="1"){
				goThisPage($('#currentPage').val());
				close();
			}
			else{
				alertWarn("fail,please contact the administrator");
			}
			
	});
}
