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
	<title>通用查询</title>
	<script type="text/javascript" language="javascript" src="<%=basePath %>script/jquery/20121128/jquery.js" ></script>
	<script type="text/javascript">
		jQuery(document).ready(function(){
			jQuery('#execel_search').click(function(){
				if($('#execel_param').val()===''){
					alert('请选择execel文件！');
					return;
				}else{
					jQuery('#uploadExecel').submit();
				}
			});
		});
	</script>
</head>
<body>
	<form id="uploadExecel" name="uploadExecel" action="/extend/commonQuery/commonQueryByExcel.do" method="post" enctype="multipart/form-data">
		<table>
			<tr>
				<td  class="search">
					<table>
						<tr>
							<td class="search">Execel配置文件:&nbsp;&nbsp;&nbsp;&nbsp;<input id="execel_param" name="execel_param" type="file" size="50" /></td><td>&nbsp;&nbsp;&nbsp;&nbsp;<a href="#none" id='execel_search' name='execel_search'><span>生成SQL语句</span></a></td>
							<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
							<td><a href="<%=basePath %>view/downloadList.jsp" target="_blank">查看生成历史</a></td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>
 </body>
</html>

