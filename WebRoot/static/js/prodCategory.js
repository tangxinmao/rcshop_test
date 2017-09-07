/**
 * 父分类弹出层
 */
function addDadPopWin() {
    var root = $("#rootsite").val();
    $(".shade_layout").show();
    $("#dadpopwin").show();
    $("#categoryName").val("");
    $("#catSerNum").val("");
    $("#fileId").val("");
    $("#photocover").prop("src", root + "/static/images/ico/icon_image.png");
    pop_center('560');
}

/**
 * 上传商品分类图标
 */
function uploadCover() {
    var root = $("#rootsite").val();
    $.ajaxFileUpload
    (
        {
            url: root + '/category/uploadcover',
            secureuri: false,
            fileElementId: 'cover',
            dataType: 'json',
            data: {},
            success: function (data, status) {
                if (data.success) {
                    $("#photocover").prop("src", data.imgUrl);
                    $("#fileId").attr("value", data.fileid);
                } else {
                    alertWarn("333");
//					alertWarn(data.errorMsg);
                    closed_add_shade_layout();
                    alertWarnWarn(data.errorMsg != '' ? data.errorMsg : '上传图片出错,请联系管理员');
                }
                createColverUploadHtml();
            },
            error: function (data, status, e) {
                alertWarn("444");
            }
        }
    );
}

/**
 * 重新绑定onchange事件
 */
function createColverUploadHtml() {
    var $new = $('<input t="' + new Date().getTime() + '" size="1" type="file" name="cover" id="cover" value="上传图片" style="opacity:0; filter: Alpha(Opacity=0); cursor:pointer;z-index:1;position: absolute;top:18px;left: 163px; width: 89px;height: 29px;" />');
    $('#cover').replaceWith($new);
    $new.change(uploadCover);
}

/**
 * 添加父分类
 */
function addTopLevelCategory() {
    var root = $("#rootsite").val();
    var categoryName = $("#categoryName").val();
    var iconUrl = $("#photocover").prop("src");
    var catSerNum = $("#catSerNum").val();

    if (categoryName == "") {
        alertWarn("please input the value about categoryName!");
        return 0;
    }
    /*if (catSerNum == "") {
        alertWarn("please input the value about Category No!");
        return 0;
    }*/
    var regInt = /^[0-9]+[0-9]*]*$/;  //正整数
    if (!regInt.test(catSerNum)) {
        if (catSerNum != '') {
            alertWarn("Category No amount must be positive integer!");
            $("#catSerNum").focus();
            return;
        }else{
            alertWarn("please input the value about Category No!");
            $("#catSerNum").focus();
            return;
        }
    }

    var url = root + "/category/addtoplevel";
    $.ajax({
        type: "POST",
        //contentType : 'charset=utf-8',
        url: url,
        dataType: 'json',
        cache: false,
        data: {
            "name": categoryName,
            "icon": iconUrl,
            "seq": catSerNum
        },
        success: function (data) {
            var jsonObjArr = JSON.parse(data);
            if (jsonObjArr.code == "1") {
                alertWarn("success!");
                goThisPage($('#currentPage').val());
                close();
            }
            else {
                alertWarn("fail,please contact the administrator");
            }
        }
    });
}

/**
 * 添加子分类
 * @param parentCatId 父分类ID
 * @param obj DOM对象
 */
function addChildPopWin(parentCatId, obj) {
    var root = $("#rootsite").val();
    $(".shade_layout").show();
    $("#childpopwin").show();
    var $tr = $(obj).parent().parent().find("td");
    $("#parentCatSpan").text($tr.eq(0).text());
    $("#parentCatId").val(parentCatId);
    $("#childcategoryName").val("");
    $("#childcatSerNum").val("");
    $("#photochildcover").prop("src", root + "/static/images/ico/icon_image.png");
    pop_center('560');

}

/**
 * 添加子分类
 */
function addNextLevelCategory() {
    var root = $("#rootsite").val();
    var parentCatId = $("#parentCatId").val();
    var childcategoryName = $("#childcategoryName").val();
    var childfileId = $("#childfileId").val();
    var photochildcover = $("#photochildcover").prop("src");
    var childcatSerNum = $("#childcatSerNum").val();

    if (childcategoryName == "") {
        alertWarn("please input the value about categoryName!");
        return 0;
    }
    if (catSerNum == "") {
        alertWarn("please input the value about categoryName!");
        return 0;
    }

    var url = root + "/category/addnextlevel";
    $.ajax({
        type: "POST",
        //contentType : 'charset=utf-8',
        url: url,
        dataType: 'json',
        cache: false,
        data: {
            "parentId": parentCatId,
            "name": childcategoryName,
            "icon": photochildcover,
            "seq": childcatSerNum
        },
        success: function (data) {
            var jsonObjArr = JSON.parse(data);
            if (jsonObjArr.code == "1") {
                alertWarn("success!");
                goThisPage($('#currentPage').val());
                close();
            }
            else {
                alertWarn("fail,please contact the administrator");
            }
        }
    });
}

/**
 * 上传子商品分类图标
 */
function uploadChildCover() {
    var root = $("#rootsite").val();
    $.ajaxFileUpload
    (
        {
            url: root + '/category/uploadchildcover',
            secureuri: false,
            fileElementId: 'childcover',
            dataType: 'json',
            data: {},
            success: function (data, status) {
                if (data.success) {
                    $("#photochildcover").prop("src", data.imgUrl);
                    $("#childfileId").attr("value", data.fileid);
                } else {
                    alertWarn("333");
//					alertWarn(data.errorMsg);
                    closed_add_shade_layout();
                    alertWarnWarn(data.errorMsg != '' ? data.errorMsg : '上传图片出错,请联系管理员');
                }
                createChildColverUploadHtml();
            },
            error: function (data, status, e) {
                alertWarn("444");
            }
        }
    );
}

/**
 * 重新绑定onchange事件
 */
function createChildColverUploadHtml() {
    var $new = $('<input t="' + new Date().getTime() + '" size="1" type="file" name="childcover" id="childcover" value="upload" style="opacity:0; filter: Alpha(Opacity=0); cursor:pointer;z-index:1;position: absolute;top:18px;left: 163px;  width: 89px;height: 29px;" />');
    $('#childcover').replaceWith($new);
    $new.change(uploadChildCover);
}

/**
 * 删除子分类
 * @param prodCatId 分类ID
 * @param obj DOM对象
 */
function deleteChild(prodCatId, obj) {
    var root = $("#rootsite").val();
    $.post(root + "/category/deleteChild", {"prodCatId": prodCatId},
        function (data) {
            var jsonObjArr = JSON.parse(data);
            if (jsonObjArr.code == "1") {
                close();
                goThisPage($('#currentPage').val());
            }
            else {
                alertWarn(jsonObjArr.result);
            }
        });
}

/**
 * 删除父分类
 * @param prodCatId 分类ID
 * @param obj DOM对象
 */
function deleteDad(prodCatId, obj) {
    var root = $("#rootsite").val();
    $.post(root + "/category/deleteDad", {"prodCatId": prodCatId},
        function (data) {
            var jsonObjArr = JSON.parse(data);
            if (jsonObjArr.code == "1") {
                close();
                goThisPage($('#currentPage').val());
            }
            else {
                alertWarn(jsonObjArr.result);
            }
        });
}

/**
 * 绑定商品属性窗口
 * @param prodCatId
 */
function openPropChoseBox(prodCatId, i) {
    var root = $("#rootsite").val();
//	 top.topManager.openPage({
//		    id : 'openPropChoseBox',
//		    href : root+'/prop/prodPropChoseBox?msgId='+prodCatId,
//		    title : 'Bind Property'
//		  });
    var iTop = (window.screen.availHeight - 30 - 350 - 200) / 2;
    var iLeft = (window.screen.availWidth - 10 - 700) / 2;
    window.open(root + '/prodprop/prodPropChoseBox?prodCatId=' + prodCatId + '&type=' + i, 'newwindow', 'height=600,width=1000,top=' + iTop + ',left=' + iLeft + ',toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
}

/**
 * 添加商品分类页面的品牌
 * @param prodCatId
 */
function openCatBrandChoseBox(prodCatId) {
    var root = $("#rootsite").val();
    var iTop = (window.screen.availHeight - 30 - 350 - 200) / 2;
    var iLeft = (window.screen.availWidth - 10 - 700) / 2;
    window.open(root + '/category/brandChoseBox?prodCatId=' + prodCatId, 'newwindow', 'height=600,width=1000,top=' + iTop + ',left=' + iLeft + ',toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no');
}

function showEditPopWin(prodCatId, obj) {
    var $td = $(obj).parent().parent().find("td");
    $("#upcategoryName").val($.trim($td.eq(0).text()));
    $("#upcatSerNum").val($td.eq(1).text());
    $("#dadprodCatId").val(prodCatId);
    $(".shade_layout").show();
    $("#daduppopwin").show();
    pop_center('560');
}

function upTopLevelCategory() {
    var root = $("#rootsite").val();
    var dadprodCatId = $("#dadprodCatId").val();
    var upcategoryName = $.trim($("#upcategoryName").val());
    var upcatSerNum = $.trim($("#upcatSerNum").val());
    if (upcategoryName == "") {
        alertWarn("Please input Category Name!");
        $("#upcategoryName").focus();
        return;
    }
    var regInt = /^[0-9]+[0-9]*]*$/;  //正整数
    if (!regInt.test(upcatSerNum)) {
        if (upcatSerNum != '') {
            alertWarn("Category No amount must be positive integer!");
            $("#upcatSerNum").focus();
            return;
        }else{
            alertWarn("Please input Category No!");
            $("#upcatSerNum").focus();
            return;
        }
    }

    var icon = $("#updatePhotocover").prop("src");

    $.post(root + "/category/modifydad", {
            "prodCatId": dadprodCatId,
            "name": upcategoryName,
            "seq": upcatSerNum,
            "icon":icon
        },
        function (data) {
            var jsonObjArr = JSON.parse(data);
            if (jsonObjArr.code == "1") {
                alertWarn("success!");
                close();
                goThisPage($('#currentPage').val());
            }
            else {
                alertWarn(jsonObjArr.result);
            }
        });

}