$(function(){
	getMenu();
})
/**
 * 验证及提示框
 */
function alertWarn(message){
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
}
function getMenu(){
	var root=$('#rootsite').val();
	$.post(root+'/role/menu',{},function(data){
		var tree=convert(data);
		var home=tree[0];
	    var str='<table class="table order_table mt10" id="table"><tbody><th style="width:20%">Menu Name</th><th>Link</th><th>Serial</th><th>Actions</th>';
		for(i in tree){
			var tr=tree[i];
			str+='<tr class="dad" data-id="'+tr.id+'" data-tt-id="'+i+'">'+
			'<td style="text-align: left; padding-left: 30px;">'+tr.text+'</td>'+
			'<td>'+tr.attributes.location+'</td>'+
			'<td>'+tr.attributes.seq+'</td>'+
			'<td>'+
				'<a href="javascript:void(0)" onclick="showEditPopWin(\''+tr.id+'\',this)">Edit</a> | '+
				'<a href="javascript:void(0)" onclick="addChildPopWin(\''+tr.id+'\',this)">Add Sub-menu</a> | '+
				'<a href="javascript:void(0)" onclick="showConfirm(\'Are you sure to delete this menu? If you deleted,this menu belong to the navigation bar will be delete.\',&quot;deleteDad(\''+tr.id+'\')&quot;)">Delete</a>'+
			'</td>'+
			'</tr>';
			for(j in tr.children){
				var td=tr.children[j];
				str+='<tr class="child" data-id="'+td.id+'" data-tt-id="'+i+'-'+j+'" data-tt-parent-id="'+i+'">'+
				'<td style="text-align: left; padding-left:20px;">'+td.text+'</td>'+
				'<td>'+td.attributes.location+'</td>'+
				'<td>'+td.attributes.seq+'</td>'+
				'<td>'+
					'<a href="javascript:void(0)" onclick="showEditPopWin(\''+td.id+'\',this)">Edit</a> | '+
					'<a href="javascript:void(0)" onclick="showConfirm(\'Are you sure to delete this menu? If you deleted,this menu belong to the navigation bar will be delete.\',&quot;deleteChild(\''+td.id+'\')&quot;)">Delete</a>'+
				'</td>'+
			'</tr>';
			}
		
		}
	str+='</table></tbody>';
		$('#reloadSearchMessages').html(str);
	$("#table").treetable({ expandable: true });
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


/**
 * 父分类弹出层
 */
function addDadPopWin(){
	$('#menuWinForm')[0].reset();
	$('#belong').hide()
	$('#menuWinForm input[name="menuId"]').val(null);
	$('#menuWinForm input[name="parentId"]').val(0);
	$('#dadpopwin .hd h2').text('Add Menu');
	$(".shade_layout").show();
	$("#dadpopwin").show();
	pop_center ('560'); 
}

/**
 * 上传商品分类图标
 */
function uploadCover(){
	var root = $("#rootsite").val();
	$.ajaxFileUpload
	(
		{
			url:root+'/category/uploadcover',
			secureuri:false,
			fileElementId:'cover',
			dataType: 'json',
			data:{},
			success: function (data, status)
			{
				if( data.success )
				{
					$("#photocover").prop("src",data.imgUrl);
					$("#fileId").attr("value",data.fileid);
				} else {
					alertWarn("333");
//					alertWarn(data.errorMsg);
					closed_add_shade_layout();
					alertWarn( data.errorMsg != '' ? data.errorMsg : '上传图片出错,请联系管理员');
				}
				createColverUploadHtml();
			},
			error: function (data, status, e)
			{
				alertWarn("444");
			}
		}
	);
}
/**
 * 重新绑定onchange事件
 */
function createColverUploadHtml(){
	var $new = $('<input t="'+ new Date().getTime() +'" size="1" type="file" name="cover" id="cover" value="上传图片" style="opacity:0; filter: Alpha(Opacity=0); cursor:pointer;z-index:1;position: absolute;top:18px;left: 163px; width: 89px;height: 29px;" />');
	$('#cover').replaceWith( $new );
	$new.change(uploadCover);
}
/**
 * 添加父分类
 */
function addTopLevelCategory(){
	var root = $("#rootsite").val();
	var serializeArray=$('#menuWinForm').serializeArray();
	for(i in serializeArray){
		if(serializeArray[i].name=='menuId'||serializeArray[i].name=='location'||serializeArray[i].name=='parentId')
			continue;
		var text=$('#menuWinForm input[name="'+serializeArray[i].name+'"]').prev('lebel').children().last().text().replace(':','');
		if(text=='')
			text=$('#menuWinForm textarea[name="'+serializeArray[i].name+'"]').prev('lebel').children().last().text().replace(':','');
		if(!serializeArray[i].value&&serializeArray[i].value==''){
			alertWarn(text+' can\'t be empty.');
			return ;
		}else{
			var regInt = /^[0-9]*[1-9][0-9]*$/ ; 
			if(serializeArray[i].name=='seq')
			if(!regInt.test(serializeArray[i].value)){
				alertWarn('please input integer about in '+text+'!');
				return;
			}
		}
	}
	console.log(serializeArray);
	$.post(root+'/role/saveMenu', serializeArray,
			function(j){
		var data=JSON.parse(j);
			if(data.code=="1"){
				close();
				location.reload();
				var loginMember=JSON.parse($('#loginMember').val());
				var itemsChecked= localStorage.getItem('itemsChecked')
					var map=JSON.parse(itemsChecked);
					for(i in map){
						if(map[i].roleId==loginMember.roleId){
						       var menus=map[i].data;
						       var j=0;
						       while(j<menus.length){
						    	   if(menus[j].id==serializeArray[0].value){
						    		   menus[j].name=serializeArray[2].value;
						    		   menus[j].location=serializeArray[3].value;
						    		   //如果是排序字段改变则重新更新缓存
						    		   if(menus[j].seq!=serializeArray[4].value){
						    			   $.ajaxSetup({  
						    				    async : false
						    				});  
						    				$.post(root+'/role/menuByRole', {roleId:loginMember.roles[0].roleId},function(data){
						    					map[i].data=data;
												localStorage.setItem('itemsChecked',JSON.stringify(map));
						    				});
						    		   }else{
						    			   localStorage.setItem('itemsChecked',JSON.stringify(map));
						    		   }
						    		   location.reload();
						    		   break;
						    	   }
						    	   j++;
						       }
						       if(j==menus.length){
		                        getMenu();
		                        break;
	                               }
					
						}
					}
			 
			}
			else{
				alertWarn("fail,please contact the administrator");
			}
			
	});
}

/**
 * 添加子分类
 */
function addNextLevelCategory(){
	var root = $("#rootsite").val();
	var parentCatId = $("#parentCatId").val();
	var childcategoryName = $("#childcategoryName").val();
	var childfileId = $("#childfileId").val();
	var photochildcover = $("#photochildcover").prop("src");
	var childcatSerNum = $("#childcatSerNum").val();
	
	if(childcategoryName==""){
		alertWarn("please input the value about categoryName!");
		return 0;
	}
	if(catSerNum==""){
		alertWarn("please input the value about categoryName!");
		return 0;
	}
	
	var url = root + "/category/addNextLevel";
	$.ajax({
		type : "POST",
	//contentType : 'charset=utf-8',
	url : url,
	dataType : 'json',
	cache : false,
	data : {
		"parentId":parentCatId,
		"categoryName": childcategoryName,
		"iconUrl": photochildcover,
		"catSerNum":childcatSerNum
	},
	success : function(data) {
		var jsonObjArr = JSON.parse(data);
		if(jsonObjArr.code=="1"){
			alertWarn("success!");
			goThisPage($('#currentPage').val());
			close();
		}
		else{
			alertWarn("fail,please contact the administrator");
		}
	}
	});
}

/**
 * 上传子商品分类图标
 */
function uploadChildCover(){
	var root = $("#rootsite").val();
	$.ajaxFileUpload
	(
		{
			url:root+'/category/uploadchildcover',
			secureuri:false,
			fileElementId:'childcover',
			dataType: 'json',
			data:{},
			success: function (data, status)
			{
				if( data.success )
				{
					$("#photochildcover").prop("src",data.imgUrl);
					$("#childfileId").attr("value",data.fileid);
				} else {
					alertWarn("333");
//					alertWarn(data.errorMsg);
					closed_add_shade_layout();
					alertWarn( data.errorMsg != '' ? data.errorMsg : '上传图片出错,请联系管理员');
				}
				createChildColverUploadHtml();
			},
			error: function (data, status, e)
			{
				alertWarn("444");
			}
		}
	);
}

/**
 * 重新绑定onchange事件
 */
function createChildColverUploadHtml(){
	var $new = $('<input t="'+ new Date().getTime() +'" size="1" type="file" name="childcover" id="childcover" value="upload" style="opacity:0; filter: Alpha(Opacity=0); cursor:pointer;z-index:1;position: absolute;top:18px;left: 163px;  width: 89px;height: 29px;" />');
	$('#childcover').replaceWith( $new );
	$new.change(uploadChildCover);
}

/**
 * 删除子分类
 * @param prodCatId 分类ID
 * @param obj DOM对象
 */
function deleteChild(prodCatId,obj){
	var root = $("#rootsite").val();
	$.post(root+'/role/deleteMenu',{menuId:prodCatId}, 
			function(j){
		var data=JSON.parse(j);
			if(data.code=="1"){
				close();
				location.reload();
				var loginMember=JSON.parse($('#loginMember').val());
				var itemsChecked= localStorage.getItem('itemsChecked')
				var map=JSON.parse(itemsChecked);
				for(i in map){
					if(map[i].roleId==loginMember.roleId){
					       var menus=map[i].data;
					       var j=0;
					       while(j<menus.length){
					    	   if(menus[j].id==prodCatId){
					    		  //删除第j个元素
					    		   menus.splice(j,1);
					    		   localStorage.setItem('itemsChecked',JSON.stringify(map));
					    		   location.reload();
					    		   break;
					    	   }
					    	   j++;
					       }
					       if(j==menus.length){
	                        getMenu();
	                        break;
                               }
				
					}
				}
			}
			else{
				alertWarn(data.result);
			}
	});
}

/**
 * 删除父分类
 * @param prodCatId 分类ID
 * @param obj DOM对象
 */
function deleteDad(prodCatId,obj){
	var root = $("#rootsite").val();
	$.post(root+'/role/deleteMenu',{menuId:prodCatId}, 
			function(j){
		var data=JSON.parse(j);
	
			if(data.code=="1"){
				close();
				  location.reload();
				var loginMember=JSON.parse($('#loginMember').val());
				var itemsChecked= localStorage.getItem('itemsChecked')
				var map=JSON.parse(itemsChecked);
				for(i in map){
					if(map[i].roleId==loginMember.roleId){
					       var menus=map[i].data;
					       var j=0;
					       while(j<menus.length){
					    	   if(menus[j].id==prodCatId){
					    		   $.ajaxSetup({  
				    				    async : false
				    				});  
				    				$.post(root+'/role/menuByRole', {roleId:loginMember.roleId},function(data){
				    					map[i].data=data;
										localStorage.setItem('itemsChecked',JSON.stringify(map));
				    				});
					    		   location.reload();
					    		   break;
					    	   }
					    	   j++;
					       }
					       if(j==menus.length){
	                        getMenu();
	                        break;
                               }
				
					}
				}
			}
			else{
				alertWarn(data.result);
			}
	});
}

/**
 * 绑定商品属性窗口
 * @param prodCatId
 */
function openPropChoseBox(prodCatId){
	 var root = $("#rootsite").val();
//	 top.topManager.openPage({
//		    id : 'openPropChoseBox',
//		    href : root+'/prop/prodPropChoseBox?msgId='+prodCatId,
//		    title : 'Bind Property'
//		  });
	 var iTop = (window.screen.availHeight-30-350-200)/2;
	 var iLeft = (window.screen.availWidth-10-700)/2; 
	 window.open (root+'/prodprop/prodPropChoseBox?prodCatId='+prodCatId,'newwindow','height=600,width=1000,top='+iTop+',left='+iLeft+',toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
}
/**
 * 添加商品分类页面的品牌
 * @param prodCatId
 */
function openCatBrandChoseBox(prodCatId){
	 var root = $("#rootsite").val();
	 var iTop = (window.screen.availHeight-30-350-200)/2;
	 var iLeft = (window.screen.availWidth-10-700)/2; 
	 window.open (root+'/category/brandChoseBox?prodCatId='+prodCatId,'newwindow','height=600,width=1000,top='+iTop+',left='+iLeft+',toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
}

function showEditPopWin(id,obj){
	$('#belong').hide()
	var tr=$(obj).parent().parent();
	var $td = tr.find("td");
	$('#menuWinForm input[name="menuId"]').val(id);
	var parenttr=tr.prev('tr[data-tt-id="'+tr[0].getAttribute('data-tt-parent-id')+'"]');
	if(parenttr.length==0)
		$('#menuWinForm input[name="parentId"]').val(null);
	else
	$('#menuWinForm input[name="parentId"]').val(parenttr[0].getAttribute('data-id'));
	$('#menuWinForm input[name="name"]').val($td.eq(0).text());
	$('#menuWinForm textarea[name="location"]').val($td.eq(1).text());
	$('#menuWinForm input[name="seq"]').val($td.eq(2).text());
	$('#dadpopwin .hd h2').text('Edit Menu');
	$(".shade_layout").show();
	$("#dadpopwin").show();
	pop_center ('560'); 
}
/**
 * 添加子分类
 * @param parentCatId 父分类ID
 * @param obj DOM对象
 */
function addChildPopWin(parentId,obj){
	$('#menuWinForm #belong').show();
	$('#menuWinForm')[0].reset();
	var $td = $(obj).parent().parent().find("td");
	$('#menuWinForm #belong #parentCatSpan').text($td.eq(0).text());
	$('#menuWinForm input[name="menuId"]').val(null);
	$('#menuWinForm input[name="parentId"]').val(parentId);
	$('#dadpopwin .hd h2').text('Add Sub-menu');
	$(".shade_layout").show();
	$("#dadpopwin").show();
	pop_center ('560'); 	
	
}
function upTopLevelCategory(){
	var root = $("#rootsite").val();
	var dadprodCatId = $("#dadprodCatId").val();
	var upcategoryName = $.trim($("#upcategoryName").val());
	var upcatSerNum = $.trim($("#upcatSerNum").val());
	$.post(root+"/category/modifydad", {"prodCatId":dadprodCatId,
										"categoryName": upcategoryName,
										"catSerNum":upcatSerNum},
			function(data){
			var jsonObjArr = JSON.parse(data);
			if(jsonObjArr.code=="1"){
				alertWarn("success!");
				close();
				goThisPage($('#currentPage').val());
			}
			else{
				alertWarn(jsonObjArr.result);
			}
	});
	
}