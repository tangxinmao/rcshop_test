function addversion(){
	var vernum = $("#vernum").val();
	if(vernum==null || vernum==""){
		alert("请填写版本号");
		return 0;
	}
	$("#form1").submit();
}