/**
 * 定义CommonTool类
 * 
 * import jQuery.min.js 
 * import jQuery-ui.min.js
 * 
 */

;(function() {
	CommonTool = {
		/*属性 begin */
		dataCache:null,
		/*属性 end */
		
		/*函数 begin */
		/**
		 * 
		 * @param width
		 * @param height
		 * @param postion
		 * @returns
		 */
		createBlock : function(width, height, postion) {
			var bgObj = document.createElement("div");
			bgObj.setAttribute('id', 'bgDiv');
			bgObj.style.position = "absolute";
			bgObj.style.top = 0.5 * (screen.availHeight - height);
			bgObj.style.background = "#cccccc";
			bgObj.style.filter = "progid:DXImageTransform.Microsoft.Alpha(style=3,opacity=25,finishOpacity=75";
			bgObj.style.opacity = "0.6";
			bgObj.style.left = 0.5 * (screen.availWidth - width);
			bgObj.style.width = width + "px";
			bgObj.style.height = height + "px";
			bgObj.style.zIndex = "10000";
			bgObj.onclick = function() {
				document.body.removeChild(bgObj);
			};
			bgObj.innerHTML = '<table align="center"><tr><td>1</td><td>2</td><td>3</td></tr><tr><td>4</td><td>5</td><td>6</td></tr></table>';
			return bgObj;
			// document.body.appendChild(bgObj);
		},
		
		/**
		 * 封装 Ajax 加载数据。内核使用jQuery，只不过加了遮罩层
		 * @param url  请求路径
		 * @param param {key1:value1,key2:value2,......}
		 * @param callback 自定义的回调函数 需要自己解屏。函数里获得传回的数据
		 */
		loadData : function(url, param,callback) {
			$.blockUI({message:'加载数据中......'});
			$.get(url, param,callback,'json');
		},
		
		/**
		 * 封装 Ajax 提交数据 内核使用jQuery，只不过加了遮罩层
		 * @param url 提交路径
		 * @param param {key1:value1,key2:value2,......}
		 * @param callback 自定义的回调函数 需要自己解屏。函数里获得传回的数据
		 */
		commitData:function(url, param,callback){
			$.blockUI({message:'数据提交中......'});
			$.post(url, param,callback,'json');
		},
		/**
		 * 在控件上显示00:00:00样式的时钟
		 * @param targetObject
		 */
		showClock : function(targetObject) {
			var clock;
			if (targetObject !== null && targetObject.id !== 'undefined') {
				clock = targetObject;
			} else {
				clock = document.getElementById(targetObject);
			}

			var Digital = new Date();
			var hours = Digital.getHours();
			var minutes = Digital.getMinutes();
			var seconds = Digital.getSeconds();

			if (minutes <= 9) {
				minutes = "0" + minutes;
			}
			if (seconds <= 9) {
				seconds = "0" + seconds;
			}
			myclock = "<font size='6'><b>" + hours + ":" + minutes + ":" + seconds
					+ "</b></font>";

			if (document.layers) {
				document.layers.liveclock.document.write(myclock);
				document.layers.liveclock.document.close();
			} else if (document.all) {
				clock.innerHTML = myclock;
				setTimeout("showClock(" + targetObject.id + ")", 1000);
			}
		},
		
		/**
		 * 判断是否为数组
		 * @param o
		 * @returns {Boolean}
		 */
		isArray : function (o) {
			return Object.prototype.toString.call(o) === '[object Array]';
		}
		
		/*函数 end */
	};

})();


