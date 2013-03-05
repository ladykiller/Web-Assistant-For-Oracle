<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<style type="text/css">
	<!--
		table{
			width:1200px;
			border-style:solid;
			border-width:thin;
			border-color:#000;
			border-collapse: collapse;
			background-color: #EBF0F9;
		}
		th{
			height:24px;
		}
		td{
			border-width:thin;
			border-style:solid;
			border-color:#000;
			text-align:left;
			font-size:14px;
			height:21px;
			white-space:nowrap;
		}
	-->
	</style>
	<script type="text/javascript" language="javascript" src="<%=basePath %>script/jquery/20121128/jquery.js" ></script>
	<script type="text/javascript">
		var webContext = "${pageContext.request.contextPath}";
		jQuery(document).ready(function(){
			jQuery.ajax({
				url : webContext + "/commonQuery/createDownloadList.do",
				data : {},
				dataType : "json",
				headers : {
					"Accept" : "text/plain;charset=UTF-8"
				},
				success : function(datas, textStatus, jqXHR) {
 						var content='',temp;						
					jQuery(datas).each(function(i){
						content=content+'<tr><td>'+(i+1)+'</td><td><a href="#" onclick="downloadfile(this);" id="'+this.filename+'">'+this.filename+'</a></td><td><a href="#">下载</a></td></tr>';
					});
					jQuery(content).insertAfter(jQuery('#tableHead'));
				},
				timeout : 10000,
				contentType : "application/x-www-form-urlencoded; charset=utf-8"
			});
				
/* 				jQuery.post(webContext+"/commonQuery/createDownloadList.do",{},function(datas){
					
				},"json"); */
		});
		
		function downloadfile(obj){
			jQuery('#downloadfilename').val(obj.id);
			jQuery('#downloadform').submit();
		}
	</script>
</head>
<body>
	<form id="downloadform"  action="/extend/commonQuery/createDownloadProccess.do" method="post">
		<input type="hidden" value="" id="downloadfilename" name="downloadfilename" />
	</form>
	<table border="">
		<tr id='tableHead'><th width="50">序号</th><th width="950">脚本</th><th width="200">操作</th></tr>
	</table>
</body>
</html>

