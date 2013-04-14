/**
 * 定义Qsnnaire类
 * 
 * import jQuery.min.js
 * import jQuery-ui.min.js
 */

;(function(){
		Qsnnaire = {
		
		/* query */
		
		get:function(id){
			return document.getElementById(id);
		},
		$:function(id){
			return document.getElementById(id);
		},
		getTG:function(elem, tagName){
			return elem.getElementsByTagName(tagName);
		},
		
		query:function(string){
			return document.querySelector(string);
		},
		create:function(tagName){
			return document.createElement(tagName);
		},
		createBlock:function(width,height,postion){
			var bgObj=document.createElement("div");
			bgObj.setAttribute('id','bgDiv');
			bgObj.style.position="absolute";
			bgObj.style.top=0.5*(screen.availHeight-height);
			bgObj.style.background="#cccccc";
			bgObj.style.filter="progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
			bgObj.style.opacity="0.6";
			bgObj.style.left=0.5*(screen.availWidth-width);
			bgObj.style.width=width+"px";
			bgObj.style.height=height+"px";
			bgObj.style.zIndex = "10000";
			bgObj.onclick=function(){document.body.removeChild(bgObj);};
			bgObj.innerHTML='<table align="center"><tr><td>1</td><td>2</td><td>3</td></tr><tr><td>4</td><td>5</td><td>6</td></tr></table>';
			return bgObj;
			//document.body.appendChild(bgObj);
		},
		/**
		 * Ajax 加载数据
		 * @param url 请求路径
		 * @param param {key1:value1,key2:value2,......}
		 * @returns data 返回json格式数据
		 */
		loadData:function(url,param){
			$.get(url,param,function(data){return data;},'json');
		}
		
	 };
	 
  })();