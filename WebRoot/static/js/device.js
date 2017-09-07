function deviceAdd(){
	var deviceName = $("#deviceName").val();
	var url = $("#rootsite").val() + "/device/adddevicecatagry";
	$.ajax({
		type : "POST",
	//contentType : 'charset=utf-8',
	url : url,
	dataType : 'json',
	cache : false,
	data : {
		"deviceName": deviceName
	},
	success : function(data) {
		var jsonObjArr = JSON.parse(data);
			alert(jsonObjArr.result);
	}
});
}
function deviceVersionAdd(){
	var deviceType = $("#deviceType").val();
	var number = $("#number").val();
//	var infomation = $("#infomation").val();
//	var url = $("#rootsite").val() + "/device/adddeviceVersion";
	if(deviceType==""){
		alert("请先添加设备分类");
		return 0;
	}
	if(number==""){
		alert("请填写版本号");
		return 0;
	}
	$("#form1").submit();
}