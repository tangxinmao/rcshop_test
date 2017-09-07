$( function(){
	$("#yhtest").click(function(){
		
	});
	$(".secBtn span").click(function(){
		$(".secBtn span").each(function(){
			$(this).removeClass("selectedd");
		});
		$(this).addClass("selectedd");
		if($(".secBtn span").eq(0).hasClass("selectedd")){
			$("#prodType").val("1");
		}
		else{
			$("#prodType").val("2");
		}
	});
// 根据店铺查询商品分类，放入商品分类选择框
//	prodCatId=-1;
//	storeId = serverstoreId;
	dataForSend={"storeId":"123"};
	$.post("querytopcat",jQuery.param(dataForSend),function(json){ 
		var jsonObjArr = JSON.parse(json);
		for(var i in jsonObjArr){
			$("#topItems ul").append("<li rel='"+jsonObjArr[i].prodCatId+"'>"+jsonObjArr[i].name+"</li>");
			//var jsonObjArr1 = JSON.parse(jsonObjArr[i].subList);
			if(jsonObjArr[i].subList==0){
				$("#topItems ul").find("li").each(function(){
					$(this).css("background-image","none");
				});
				
			}
//		for(var j in jsonObjArr1){
//			$("#topItems ul").append("<li rel='"+jsonObjArr1[j].prodCatId+"'>"+jsonObjArr1[j].catName+"</li>");
//		}
		} 
		$("#zeroItems ul li").bind("click",function(){
			$(this).parent().find("li").each(function(){
				$(this).removeClass("selected");
			});
			$(this).addClass("selected");
			var type = $(this).attr("rel");
			if(type=="1"){
				$("#thirdItems").hide();
				$("#prodCatId").val("-1");
				$("#subprodCatId").val("-1");
				$("#prodType").val("1");
				$("#topItems ul li").parent().find("li").each(function(){
					$(this).removeClass("selected");
				});
				$("#thirdItems ul li").parent().find("li").each(function(){
					$(this).removeClass("selected");
				});
				$("#pos").html("");
				$("#ptype").html("product | ");
			}
			else if(type=="2"){
				$("#thirdItems").hide();
				$("#prodCatId").val("-1");
				$("#subprodCatId").val("-1");
				$("#prodType").val("2");
				$("#topItems ul li").parent().find("li").each(function(){
					$(this).removeClass("selected");
				});
				$("#thirdItems ul li").parent().find("li").each(function(){
					$(this).removeClass("selected");
				});
				$("#pos").html("");
				$("#ptype").html("service | ");
			}
			
		});
		$("#topItems ul li").bind("click",function(){
			$("#thirdItems ul").html("");
//			$("#fourItems ul").html("");
//			$("#fiveItems ul").html("");
			$(this).parent().find("li").each(function(){
				$(this).removeClass("selected");
			});
			topText = $(this).text();
			$("#pos").text(topText);  //显示当前位置
			var parentCatgId = $(this).attr("rel");
			//prodCatId=parentCatgId;
			$("#prodCatId").val(parentCatgId);
			dataForSend3={"parentCatgId":parentCatgId};
			$(this).addClass("selected");
			$.post("queryNextcat",jQuery.param(dataForSend3),function(json3){ 
				var jsonObjArr3 = JSON.parse(json3);
				if(jsonObjArr3.code==0){
					isEndCata=1;
					return 0;
				}
				for(var k in jsonObjArr3){
					$("#thirdItems ul").append("<li rel='"+jsonObjArr3[k].prodCatId+"'>"+jsonObjArr3[k].name+"</li>");
					if(jsonObjArr3[k].subList==0){
						$("#thirdItems ul").find("li").each(function(){
							$(this).css("background-image","none");
						});
					}
				}
				$("#thirdItems").show();
				$("#thirdItems ul li").bind("click",function(){
					$("#fourthItems ul").html("");
					$("#fiveItems ul").html("");
					$(this).parent().find("li").each(function(){
						$(this).removeClass("selected");
					});
					thirdText = $(this).text();
					$("#pos").html(topText + " > " + thirdText);
					var parentCatgId4 = $(this).attr("rel");
				
					$("#subprodCatId").val(parentCatgId4);
					dataForSend4={"parentCatgId":parentCatgId4};
					$(this).addClass("selected");
					$.post("queryNextcat",jQuery.param(dataForSend4),function(json4){ 
						var jsonObjArr4 = JSON.parse(json4);
						if(jsonObjArr4.code==0){
							isEndCata=1;
							return 0;
						}
						for(var k in jsonObjArr4){
							$("#fourthItems ul").append("<li rel='"+jsonObjArr4[k].prodCatId+"'>"+jsonObjArr4[k].name+"</li>");
						}
					});
				});
			});
		});
	});
	
	
	$("#addpro").click(function(){
		var prodType = $("#prodType").val();
		var prodCatId = $("#prodCatId").val();
		var subprodCatId = $("#subprodCatId").val();
		var merchantId = $("#merchantId").val();
		var promptText = $("#ptype").text() + $("#pos").text() + $("#mer").text();
		if(prodCatId=="-1"){
			alertWarn("Please select a top level category!");
			return 0;
		}
		if(subprodCatId=="-1"){
			alertWarn("Please select a sub category!");
			return 0;
		}
		if(merchantId=="-1" || merchantId==""){
			alertWarn("Please select a store!");
			return 0;
		}
		self.location="addGoods?prodType="+prodType+"&prodCatId="+prodCatId+"&subprodCatId="+subprodCatId+"&merchantId="+merchantId;
	});
	
	$("#searchk").on("keyup",function(){
		var searchKey = $("#searchk").val();
		var root = $("#rootsite").val();
		$.post(root+"/product/searchMer", {"searchKey":searchKey},
				function(data){
				var jsonObjArr = JSON.parse(data);
				$("#merchantItems ul").html("");
				for(var i in jsonObjArr){
					var jsonObj = jsonObjArr[i];
					$("#merchantItems ul").append('<li rel="'+jsonObjArr[i].merchantId+'">'+jsonObjArr[i].name+'</li>')
				}
				$
		});
	});
});

function nextStep(){
	$("#addpro").show();
	$("#next").hide();
	$("#back").show();
	
	$("#zeroItems").hide();
	$("#topItems").hide();
	$("#thirdItems").hide();
	$("#merchantItems").show();
}

function backStep(){
	$("#addpro").hide();
	$("#next").show();
	$("#back").hide();
	$("#merchantId").val("");
	
	$("#zeroItems").show();
	$("#topItems").show();
	$("#thirdItems").show();
	$("#merchantItems").hide();
	
	$("#mer").text("");
	
	$("#merchantItems ul").find("li").each(function(){
		if($(this).hasClass("selected")){
			$(this).removeClass("selected");
		}
	});
}

$(document).delegate("#merchantItems ul li","click",function(){  
//$("#merchantItems ul li").bind("click",function(){
//	$("#fourItems ul").html("");
//	$("#fiveItems ul").html("");
	$(this).parent().find("li").each(function(){
		$(this).removeClass("selected");
	});
	$(this).addClass("selected");
	var merchantId = $(this).attr("rel");
	var merchantName = $(this).text();
	$("#merchantId").val(merchantId);
	$("#mer").text(" | " + merchantName);
});