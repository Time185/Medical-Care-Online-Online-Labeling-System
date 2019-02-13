<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@page import="cn.itcast.goods.user.domain.User"%>
<%@ page import="java.io.File"%>
<%@ page import="java.io.IOException"%>

<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>

<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="refresh" content="５">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"> 
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>upload文件上传</title>

<link href="dist/styles.imageuploader.css" rel="stylesheet" type="text/css">
<link href="plugin/uploadify/uploadify.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="css/webuploader.css" />
<link rel="stylesheet" type="text/css" href="css/style.css" />
    <style>
        .width_auto{
            width: 750px;
            margin: 100px auto;
        }
    </style>
<script src="plugin/uploadify/jquery-1.11.3.js" type="text/javascript"></script>
<script src="plugin/uploadify/jquery.uploadify.js" type="text/javascript"></script>
</head>
<body>

<%!String name1=""; %>
<%
User user = (User) session.getAttribute("sessionUser");
name1 = user.getLoginname();
System.out.println(name1+"--------name1"); 
session.setAttribute("name",name1);%>
<section role="main" class="l-main" style="margin-top:90px;margin-bottom:50px;">
    <div id="flashInstall" align="center" style="display:none;"> 火狐浏览器安装Flash插件: <a href="https://mp.weixin.qq.com/s/sdjdHGt6zw6nRE_b6_6lhA" target="_Blank">点击查看教程</a></div>
    <div class="width_auto" id="flashInstall1"style="display:none;">
    <div id="container">
        <!--头部，相册选择和格式选择-->
        <div id="uploader" >
            <div class="item_container">
                  <div id="" class="queueList" >
                    <div style="position:relative; font:normal 14px/24px 'MicroSoft YaHei';">
                       <p>说明：仅支持大小不超过700MB的zip压缩文件,个数不超过5个， 患者文件夹名称不得含中文和符号</p>        
                                    
                       <div id="dndArea" class="placeholder">
        					<!--用来作为文件队列区域-->
        					<div id="fileQueue"> 
        					   <input type="file" name="uploadify" id="uploadify" />        		
        					</div>   
     			        </div>
     				 </div>
      			 </div>
      		 </div>
          </div>
      </div>
    </div>
</section>
<script type="text/javascript"> 
var flashInstall="";
var swf;
if (navigator.userAgent.indexOf("MSIE") > 0) {
    try {
        var swf = new ActiveXObject("ShockwaveFlash.ShockwaveFlash");
        document.getElementById("flashInstall1").style.display="";
    }
    catch (e) {
    	showFlash();
    }
}
if (navigator.userAgent.indexOf("Firefox") > 0 || navigator.userAgent.indexOf("Chrome") > 0) {
    swf = navigator.plugins["Shockwave Flash"];
    (swf) ? document.getElementById("flashInstall1").style.display="" : showFlash();
}
function showFlash(){
	alert('检测到您的火狐浏览器未安装Flash插件，请先安装Flash插件。');
	document.getElementById("flashInstall").style.display="";
}
</script>
<script type="text/javascript"> 
var username='<%=name1%>';
var uploading="";
var uploadingFile="";
$(document).ready(function() {  
    $("#uploadify").uploadify({  
        'swf'            : 'plugin/uploadify/uploadify.swf', //上传的Flash，不用管，路径对就行
        'uploader'       : 'http://222.24.62.91:8080/BB/UploadServlet1?name='+username,  //处理上传动作的的urljava  
        'folder'         : 'userFile.getPath',  //Post文件到指定的处理文件/upload
        'queueID'        : 'fileQueue', //与下面的id对应
        'cancelImg'      : 'plugin/uploadify/uploadify-cancel.png',//取消按钮的图片路径
        'buttonText'     : '选择文件',
        'auto'           : true, //设置true 自动上传 设置false还需要手动点击按钮 
        'multi'          : true,  //是否允许多文件上传
        'wmode'          : 'transparent', //背景透明transparent与不透明opaque设定。默认不透明  
        'simUploadLimit' : 1,     //一次可传几个文件，默认为1
        'queueSizeLimit' : 5,    //限制在一次队列中的次数（可选定几个文件）
        'removeCompleted': true, //是否自动将已完成任务从队列中删除，如果设置为false则会一直保留此任务显示。
        'fileTypeExts'   : '*.zip',  //控制可上传文件的扩展名，启用本项时需同时声明fileDesc
        'fileTypeDesc'   : 'zip文件',
        'fileSizeLimit'  : '700MB', //限制上传文件大小
        'onSelectError'  : uploadify_onSelectError,
     	'onFallback'     : function () {
        						alert("您未安装FLASH控件，无法上传！请使用其他浏览器或者安装FLASH控件后再试。");}
    });  
}); 
var uploadify_onSelectError = function(file, errorCode, errorMsg) {  
    var msgText = "上传失败\n";  
    switch (errorCode) {    
        case SWFUpload.QUEUE_ERROR.FILE_EXCEEDS_SIZE_LIMIT:  
            msgText += "文件 [" + file.name + "]大小超过限制( " + this.settings.fileSizeLimit + " )";  
            break;  
        case SWFUpload.QUEUE_ERROR.QUEUE_LIMIT_EXCEEDED:      
            msgText += "每次最多上传 " + this.settings.queueSizeLimit + "个文件"; 
            break; 
        case SWFUpload.QUEUE_ERROR.ZERO_BYTE_FILE:  
            msgText += "文件大小为0";  
            break;  
        case SWFUpload.QUEUE_ERROR.INVALID_FILETYPE:  
            msgText += "文件格式不正确，仅限 " + this.settings.fileTypeExts;  
            break;  
        default:  
            msgText += "错误代码：" + errorCode + "\n" + errorMsg;  
    }  
    alert(msgText);  
};  

</script>  
</body>
</html>