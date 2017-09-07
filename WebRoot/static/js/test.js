function resister(){
	var account = $("#account").val();
	var pwd = $("#pwd").val();
	var nickname = $("#nickname").val();
	var sex = $("#sex").val();
	var age = $("#age").val();
	$.post("register", {"userAccount":account,"userPassword":pwd,"userName":nickname,"userSex":sex,"userAge":age},
			function(data){
		var jsonObjArr = JSON.parse(data);
		alert(jsonObjArr.result);
	});
}
function userLogin(){
	var username=$("#username").val();
	var password = $("#password").val();
	$.post("login", {"userAccount":username,"password":password},
			function(data){
		var jsonObjArr = JSON.parse(data);
			if(jsonObjArr.code=="0"){
				alert(jsonObjArr.result);
			}
			else{
				alert("登录成功!");
			}
	});
}

function search(){
	$.post("searchfriend", {"userId":"402881874c20becb014c20becb570000","searchkey":"呵呵"},
			function(data){
		var jsonObjArr = JSON.parse(data);
			if(jsonObjArr.code=="0"){
				alert(jsonObjArr.result);
			}
			else{
				alert("成功!");
			}
	});
}
function  firendlist(){
	$.post("getfriendlist", {"userId":"402881874c20becb014c20becb570000"},
			function(data){
		var jsonObjArr = JSON.parse(data);
			if(jsonObjArr.code=="0"){
				alert(jsonObjArr.result);
			}
			else{
				alert("成功!");
			}
	});
}
function removefriend(){
	$.post("removefriend", {"userId":"402881874c20becb014c20becb570000","friendId":"402881904c125bc2014c125bc4d70000"},
			function(data){
		var jsonObjArr = JSON.parse(data);
			if(jsonObjArr.code=="0"){
				alert(jsonObjArr.result);
			}
			else{
				alert("成功!");
			}
	});
}
function seral(){
	$.post("seral", {"userId":"402881874c20becb014c20becb570000","friendId":"402881904c125bc2014c125bc4d70000"},
			function(data){
		var jsonObjArr = JSON.parse(data);
			if(jsonObjArr.code=="0"){
				alert(jsonObjArr.result);
			}
			else{
				alert("成功!");
			}
	});
}