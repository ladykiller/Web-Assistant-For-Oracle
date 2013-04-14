//题目初始化选项
var init_options;
//当前选中的题目的存储序号，新增的话则为已存在最大行数加1
var currentLocation = null;
//选择题型的代码集
var selectTypeCodes = {'0101':'单选','0102':'多选'};
//填空题型的代码集
var textTypeCodes = {'0103':'填空','0104':'数字题'};
//类型代码
var qsnTypes,dspTypes,subCategoryTypes;
/**
 * 选项初始化操作，为后续操作做铺垫
 */
function initOptions(){
	init_options = $('tr[name="select_option"]');
	init_options.each(function(){
		$(this).attr('optionModifyflag','create');
	});	
}

/**
 * 初始化调查模版类型
 */
function initCatagory() {
	$.getJSON(webContext + '/questionnaire/initCatagory.do',{}, function (data){
		qsnTypes = data.qsnTypes; dspTypes = data.dspTypes;mainCategoryTypes=data.mainCategoryTypes;
		subCategoryTypes = data.subCategoryTypes;userGroupTypes = data.userGroupTypes;
		//加载类别大类数据
		$('select[id="lxdl"] option').remove();
		$(mainCategoryTypes).each(function(i){
			$("#lxdl").append("<option value='"+this.value+"'>"+this.option+"</option>");
		});
		//加载类别小类数据
		$('select[id="lxxl"] option').remove();
		$(subCategoryTypes).each(function(i){
			$("#lxxl").append("<option value='"+this.value+"'>"+this.option+"</option>");
		});
		//加载类别小类数据
		$('select[id="userGroup"] option').remove();
		$(userGroupTypes).each(function(i){
			$("#userGroup").append("<option value='"+this.value+"'>"+this.option+"</option>");
		});
		//增加题型数据
		$('select[id="tiXing"] option').remove();
		$(qsnTypes).each(function(){
			$('<option value='+$(this).attr('value')+'>'+$(this).attr('option')+'</option>').appendTo($('select[id="tiXing"]'));
			qsnTypesMapper[$(this).attr('value')]=$(this).attr('option');
		});
		//增加展示方式数据
		$(dspTypes).each(function(){
			dspTypesMapper[$(this).attr('value')]=$(this).attr('option');
		});
		//根据题型改变展示方式和选项类型 
		$('#tiXing').change(function(){questionTypeChange();});
		$('select[name=lxdl]').change(function(){
			
		});
		resetQuestion();
	});
}

/**
 * 根据类别动态联动调查类型下拉框
 * @param chkval 调查类别大类的选值
 * @param subCategoryTypes 子类集合
 */
function changeSubCategorySelect(chkval,subCategoryTypes){
	$('input[name="lxdl"][value="'+chkval+'"]').attr('checked','checked');
	$('select[id="lxxl"] option').remove();
	$(subCategoryTypes).each(function(){
		if(this.pId === chkval){
			$('<option value='+this.id+'>'+this.name+'</option>').appendTo($('select[id="lxxl"]'));
		}
	});
}

/**
 * 在页面上重载问题明细
 * @param question
 */
function restoreQuestion(question){
	var selectedValue = $(textTypeCodes).attr(question.tixing);
	//如果是填空题,因为填空题是没有选项的。只要确保不要增加即可
	if(selectedValue!==null && selectedValue!==undefined && selectedValue!== ''){
		modifyDspType(selectedValue);
	}else{
		selectedValue = $(selectTypeCodes).attr(question.tixing);
		modifyDspType(selectedValue);
	}
	showOptions(question.options);
	var questinId = question.questinId;
	var questionModifyflag = question.questionModifyflag;
	if(typeof(questinId)!=='undefined'){
		$('#title').attr('questinId',questinId);
	}
	if(typeof(questionModifyflag)!=='undefined'){
		$('#title').attr('questionModifyflag',questionModifyflag);
	}
	$('#title').val(question.title);
	$('#tiXing').val(question.tixing);
	$('#zxfs').val(question.zxfs);
	$('#beiZhu').val(question.beizhu);
}

/**
 * 展示选项
 * @param target {Array}
 */
function showOptions(target){
	var options =$(target);
	var length = options.size();
	var templete = init_options.eq(0),last,copy;
	if(length!==0){
		$('tr[name="select_option"]').remove();
		if(options.eq(0).attr('xuhao')==='-1'){
			$('tr[name="select_option_header"]').attr('style','display:none');
			//$('tr[name="text_option"]').css('display','block');
			$('tr[name="text_option"]').attr('optionId',options.eq(0).attr('optionId'));
			$('tr[name="text_option"]').attr('optionModifyflag',options.eq(0).attr('optionModifyflag'));
		}else{
			$('tr[name="select_option_header"]').attr('style','display:block');
			//$('tr[name="text_option"]').css('display','none');
			var xuhao,biaoti,csz,cz;
			options.each(function(i){
				
				if($(this).attr('optionModifyflag')!=='deleted'){
					last = $('tr[name="select_option_header"]').parent().children().eq(-1);
					copy = templete.clone(true);
					xuhao = $(this).attr('xuhao');
					biaoti = $(this).attr('biaoti');
					csz = $(this).attr('csz');
					cz = $(this).attr('cz');
					copy.children().eq(1).html(xuhao);
					copy.children().eq(2).children().eq(0).val(biaoti);
					if(typeof(csz)!=='undefined'&&csz!==''){
						copy.children().eq(3).children().eq(0).val(csz);
					}else{
						copy.children().eq(3).children().eq(0).val('0');
					}
					if(typeof(cz)!=='undefined'){
						copy.children().eq(4).children().eq(0).val(cz);
					}else{
						copy.children().eq(4).children().eq(0).val('1');
					}
					copy.attr('optionId',$(this).attr('optionId'));
					//保障同步更新序号
					if($(this).attr('optionModifyflag')==='create'){
						copy.attr('optionModifyflag','create');
					}else{
						copy.attr('optionModifyflag','changed');
					}
					$(copy).change(function(){
						var optionModifyflag = $(this).attr('optionModifyflag');
						if(optionModifyflag!==null && optionModifyflag!==undefined && optionModifyflag!=='undefined' && optionModifyflag!=='create'){
							$(this).attr('optionModifyflag','changed');
						}
					});
					last.after(copy);
				}
			});
			resortOption();
		}
	}
}

/**
 * 获取选项信息
 * @param oldOptions 传递过来的旧选项信息 可选
 * @returns {Array}
 */
function getOptionsInfo(oldOptions){
	var options = [];
	var selectOptions = $('tr[name="select_option"]');
	var textOptions = $('tr[name="text_option"]');
	var questionModifyflag = $('#title').attr('questionModifyflag');
	
	if(selectOptions.size()===0){
		options = traverseOptions(textOptions);
		if(questionModifyflag!=='create'){
			if(typeof(oldOptions)!=='undefined'){
				if(oldOptions[0].xuhao!=='-1'){
					options = options.concat(clearOptionsData(oldOptions));
				}
			}
		}
	}else{
		options = traverseOptions(selectOptions);
		if(questionModifyflag!=='create'){
			if(typeof(oldOptions)!=='undefined'){
				if(oldOptions[0].xuhao==='-1'){
					options = options.concat(clearOptionsData(oldOptions));
				}
			}
		}
	}
	
	return options;
}

/**
 * 遍历option控件
 * @param optionsControl
 * @returns {Array} 返回控件数组上的数据信息
 */
function traverseOptions(optionsControl){
	var options = [];
	var optionId,xuhao,xxmc,csps,cz,optionModifyflag,optionString;
	optionsControl.each(function(i){
		xuhao = $(this).children().eq(1).text();
		xxmc = $(this).children().eq(2).children().eq(0).val();
		csps = $(this).children().eq(3).children().eq(0).val();
		cz = $(this).children().eq(4).children().eq(0).val();
		optionModifyflag = $(this).attr('optionModifyflag');
		optionId = $(this).attr('optionId');
		
		optionString = '{"xuhao":"'+xuhao+'","biaoti":"'+xxmc+'","csz":"'+csps+'","cz":"'+cz+'"';
		optionString+=',"optionModifyflag":"'+optionModifyflag+'"';
		if(optionId!==null && optionId!==undefined && optionId!=='undefined'){
			optionString+=',"optionId":"'+optionId+'"';
		}
		options.push(optionString+'}');
	});
	return options;
}

/**
 * 题目变更时，清理掉原有选项
 * @param oldOptions {Array} 一个包含选项结构的数组
 * @returns {Array}
 */
function clearOptionsData(oldOptions){
	var delOptions =[];
	$(oldOptions).each(function(i){
		var optionId = $(this).attr('optionId');
		var optionString='{"optionModifyflag":"deleted"';
		//获取将要删除选项的ID,没有则不获取。不获取多余属性
		if(optionId!==null && optionId!==undefined && optionId!=='undefined'&& optionId!==''){
			optionString+=',"optionId":"'+optionId+'"';
		}
		delOptions.push(optionString+'}');
	});
	return delOptions;
}

/**
 * {"单选:0101","多选:0102","填空:0103","数字题:0104"}
 * {"单选框:0201","复选框:0202","文本框:0203","富文本框:0204","下拉单选:0205","下拉复选:0206"}
 * 题型和展现方式联动匹配
 * @param selectedValue 当前题型的中文名称
 */
function modifyDspType(selectedValue){
	if(selectedValue==='单选'){
		$('select[id="zxfs"] option').each(function(){ $(this).remove();});
		$('<option value="0201" >单选框</option>').appendTo($('select[id="zxfs"]'));
		$('<option value="0205" >下拉单选</option>').appendTo($('select[id="zxfs"]'));
	}else if(selectedValue==='多选'){
		$('select[id="zxfs"] option').each(function(){ $(this).remove();});
		$('<option value="0202" >复选框</option>').appendTo($('select[id="zxfs"]'));
		$('<option value="0206" >下拉复选</option>').appendTo($('select[id="zxfs"]'));
	}else if(selectedValue==='填空'){
		$('select[id="zxfs"] option').each(function(){ $(this).remove();});
		$('<option value="0203" >文本框</option>').appendTo($('select[id="zxfs"]'));
		$('<option value="0204" >富文本框</option>').appendTo($('select[id="zxfs"]'));
	}else if(selectedValue==='数字题'){
		$('select[id="zxfs"] option').each(function(){ $(this).remove();});
		$('<option value="0203" >文本框</option>').appendTo($('select[id="zxfs"]'));
	}
}

/**
 * 根据题型改变展示方式和选项类型
 */
function questionTypeChange(){
	var selectedValue = $(textTypeCodes).attr($('#tiXing').val());
	//如果是填空类型 
	if(selectedValue!==null && selectedValue!==undefined && selectedValue!== ''){
		modifyDspType(selectedValue);
		$('tr[name="select_option_header"]').attr('style','display:none');
		//$('tr[name="text_option"]').css('display','block');
		$('tr[name="select_option"]').remove();
	}else{
		selectedValue = $(selectTypeCodes).attr($('#tiXing').val());
		var blankStatus=$('tr[name="select_option_header"]').css('display');
		//如果是单选/多选转换，不刷新选项
		if(blankStatus==='none'){
			$('tr[name="select_option_header"]').attr('style','display:block');
			//$('tr[name="text_option"]').css('display','none');
			resetOption();
		}
		modifyDspType(selectedValue);
	}
}

/**
 * 新增问题
 */
function addquestion(){
	currentLocation = null;
	resetQuestion();
	resetOption();
}

/**
 * 删除问题
 */
function delQuestions() {
	var selectQuestions = $("input[name=checkbox32]:checked");
	if (typeof(selectQuestions) != "undefined"){
		saveobject = confirm("您确定要删除吗？");
		if (saveobject != "0"){
			var submitlen,deltarget,currentNum,j;
			selectQuestions.each(function(i){
				deltarget = $(this).parent().parent();
				//被删除题目的序号
				currentNum = parseInt(deltarget.children().eq(1).text(),10)-i,submitlen=submit_data.length;
				submit_data[currentNum-1].sxh='-1';
				submit_data[currentNum-1].questionModifyflag='deleted';
				//存入删除缓存中
				delQuestionIds.push(submit_data[currentNum-1]);
				submit_data.splice(currentNum-1,1);
				for(j=currentNum;j<submitlen;j++){
					submit_data[j-1].sxh=j;
				}
				deltarget.remove();
			});
			resortQuestion();
			alert("删除成功！");
		}
	}else{
		alert("请选取对象进行操作！");
	}
}

/**
 * 增加选项
 */
function addOption(element){
	var parent = $(element).parent().parent().parent().parent();
	var last = parent.children().eq(-1);
	var copy = last.clone(true);
	copy.children().eq(2).children().eq(0).attr('value',"");
	copy.children().eq(3).children().eq(0).attr('value',"0");
	copy.removeAttr('optionId');
	$(copy).attr('optionModifyflag','create');
	last.after(copy);
	resortOption();
}

/**
 * 删除选项
 */
function delOption(element){
	var target = $(element).parent().parent().parent();
	var optionNum = $('tr[name="select_option"][optionModifyflag!="deleted"]').size();
	if(optionNum<2){
		alert('至少保留一行！');
	}else{
		var optionModifyflag = $(target).attr('optionModifyflag');
		if(optionModifyflag==='create'){
			target.remove();
		}else if(optionModifyflag!==null && optionModifyflag!==undefined && optionModifyflag!=='undefined'){
			$(target).attr('optionModifyflag','deleted');
			$(target).attr('style','display:none');
		}
		resortOption();
	}
}


/**
 * 问题重排序
 */
function resortQuestion(){
	var select_questions = $('tr[name="submit_question"]');
	//被隐藏的option个数
	var ignore =0;
	select_questions.each(function(i){
		if($(this).css('display')==='none'){
			ignore++;
		}else{
			$(this).children().eq(1).text(i+1-ignore);
			$(this).children().eq(6).html('<a href="#none" name="qsnDown" onclick="questionDown(this);">↓</a>&nbsp;&nbsp;<a href="#none" name="qsnUp" onclick="questionUp(this);">↑</a>');
		}
	});
	select_questions.eq(0).children().eq(6).html('<a href="#none" name="qsnDown" onclick="questionDown(this);">↓</a>');
	select_questions.eq(-1).children().eq(6).html('<a href="#none" name="qsnUp" onclick="questionUp(this);">↑</a>');
	afterScroll();
}

/**
 * 选项重排序 
 */
function resortOption(){
	var select_options = $('tr[name="select_option"][optionModifyflag!="deleted"]');
	select_options.each(function(i){
		$(this).children().eq(1).text(i+1);
		$(this).find('td[name="seqCtrl"]').html('<a href="#none"  name="down" onclick="optionDown(this);">↓</a>&nbsp;&nbsp;<a href="#none" name="up" onclick="optionUp(this);">↑</a>');
	});
	select_options.eq(0).find('td[name="seqCtrl"]').html('<a href="#none" name="down" onclick="optionDown(this);">↓</a>');
	select_options.eq(-1).find('td[name="seqCtrl"]').html('<a href="#none" name="up" onclick="optionUp(this);">↑</a>');
	afterScroll();
}

/**
 * 重置并初始化题目
 */
function resetQuestion(){
	$('#title').val('');
	$('#title').attr('questionModifyflag','create');
	$('#title').removeAttr('questinId');
	//初始化为单选题
	$('select[id="tiXing"]').val('0101');
	$('select[id="zxfs"] option').remove();
	$('<option value="0201" >单选框</option>').appendTo($('select[id="zxfs"]'));
	$('<option value="0205" >下拉单选</option>').appendTo($('select[id="zxfs"]'));
	$('#beiZhu').val('');
}

/**
 * 重置并初始化选项
 */
function resetOption(){
	//隐藏填写项，显示表头
	$('tr[name="select_option_header"]').attr('style','display:block');
	//$('tr[name="text_option"]').css('display','none');
	$('tr[name="select_option"]').remove();
	var temp=$('tr[name="select_option_header"]');
	init_options.each(function(){
		$(this).children().eq(2).children().eq(0).attr('value',"");
		$(this).children().eq(3).children().eq(0).attr('value',"0");
		$(this).children().eq(4).children().eq(0).attr('value',"1");
		$(this).attr('optionModifyflag','create');
		$(this).removeAttr('optionId');
		temp.after($(this));
		temp = $(this);
	});
	resortOption();
}

/**
 * 问题提交前校验
*/
function submitQuestionValidate(){
	var title = $('#title').val();
	if(title===''){
		alert('题目不能为空！');
		return false;
	}

	var optionTitleFields = $('input[name="optionTitleField"]');
	var optionInitCountFields = $('input[name="optionInitCountField"]');
	var optionWeightFields = $('input[name="optionWeightField"]');
	
	var length = optionTitleFields.size(),i;
	for(i=0;i<length;i++){
		if(optionTitleFields.eq(i).val()===''){
			alert('选项第'+(i+1)+"项不能为空！");
			return false;
		}
	}
	var count = optionInitCountFields.size();
	for(i=0;i<count;i++){
		var initCount = optionInitCountFields.eq(i).val();
		if(initCount!==null && initCount!==''){
			if(initCount.match(/\D/) !== null){
				alert('选项第'+(i+1)+"项，初始票数需为大于或等于零的整数！");
				return false;
			}
		}
	}
	count = optionWeightFields.size();
	for(i=0;i<count;i++){
		var weight = optionWeightFields.eq(i).val();
		if(weight!==null && weight!==''){
			if(weight.match(/^(0|([0]{0,}\.\d+)|1|1\.0*)$/) === null){
				alert('选项第'+(i+1)+"项，不符合 0=<意见权重值=<1！");
				return false;
			}
		}
	}
	return true;
}

/*题库操作*/

/**
 * 显示提取题库界面，发出题库查询请求
 */
function addFromQuestionBase(){
	$('#questionBaseFrame').css('display','block');
	var src = $('#questionBasePage').attr('src');
	if(src ===null || src === undefined || src=== '' ){
		$('#questionBasePage').attr('src',webContext+"/ecm/survey/questionManager/selectFromQuestionBase.do?bizCode=008");
	}
	afterScroll();
}

/**
 * 隐藏页面上的题库界面
 */
function hideQuestionBase(){
	$('#questionBaseFrame').css('display','none');
	afterScroll();
}

/**
 * 提取题库中的问题加入当前的编辑页面中去
 */
function extractQuestion() {
	var selectQuestions = $(window.frames["questionBasePage"].document).find("input[name=checkbox32]:checked");
	if (typeof(selectQuestions) != "undefined"&&selectQuestions.size()>0){
		saveobject = confirm("您确定要提取此题目吗？");
		if (saveobject != "0"){
			var questions = [];
			$(selectQuestions).each(function(){
				questions.push($(this).attr('id'));
			});
			$.post(webContext+"/ecm/survey/questionbase/loadQuestionJsons.do",{questions:questions.join(","),type:'model'},function(datas){
				//编序号
				var insertPoint = submit_data.length+1;
				$(datas).each(function(i){
					this.sxh=insertPoint+i;
					this.questinId =null;
					this.questionModifyflag='create';
					$(this.options).each(function(){
						this.optionId=null;
						this.optionModifyflag='create';
						this.timuId=null;
					});
					submit_data.push(this);
				});
				reLoadQuestionList();
				alert("提取成功！");
			},"json"); 
		}
	}else{
		alert("请选取对象进行操作！");
	}
}

/**
 * 增加进题库
 */
function addIntoQuestionBase(){
	var selectQuestions = $("input[name=checkbox32]:checked");
	if (typeof(selectQuestions) != "undefined"&&selectQuestions.size()>0){
		saveobject = confirm("您确定要将题目加入题库吗？");
		if(saveobject != "0"){
			var preAddQuestion = [];
			$(selectQuestions).each(function(){
				var point = parseInt($(this).parent().parent().children().eq(1).text(),10);
				preAddQuestion.push(submit_data[point-1]);
			});
			var jsondata = JSON.stringify(preAddQuestion);
			$.post(webContext+"/ecm/survey/questionbase/saveTikus.do",{json:jsondata},function(datas){
		 		alert('加入题库'+datas.status);
		 	},"json");
		}
	}else{
		alert("请选取对象进行操作！");
	}
}

/**
 * 更新待提交问题列表
 */
function reflashQuestionList(){
	//如果为空 则说明当前没有指定行。提交的数据为新增数据，反之为修改数据
	if(currentLocation === null){
		var QsnCounter = $('tr[name="submit_question"]').size();
		currentLocation=QsnCounter+1;
		var model = $('#model_col');
		var copy = model.clone(true);
		copy.attr('style','display:block');
		copy.attr('name','submit_question');
		copy.children().eq(1).html(QsnCounter+1);
		copy.children().eq(2).html('<a href="#none" class="blue12" onclick="modifyquestion(this)">'+$('#title').val()+'</a>');
		copy.children().eq(3).html(ucGetSelectControlText('tiXing'));
		copy.children().eq(4).html(ucGetSelectControlText('zxfs'));
		copy.children().eq(5).html($('#beiZhu').val());
		model.parent().children().eq(-1).after(copy);
		resortQuestion();
	}else{
		var currentQuestion = $('tr[name="submit_question"]').eq(currentLocation-1);
		currentQuestion.children().eq(2).html('<a href="#none" class="blue12" onclick="modifyquestion(this)">'+$('#title').val()+'</a>');
		currentQuestion.children().eq(3).html(ucGetSelectControlText('tiXing'));
		currentQuestion.children().eq(4).html(ucGetSelectControlText('zxfs'));
		currentQuestion.children().eq(5).html($('#beiZhu').val());
	}
}

/**
 * 重画待提交问题列表
 */
function reLoadQuestionList(){
	$('tr[name="submit_question"]').remove();
	// 页面效果的展示
	var model = $('#model_col'),copy;
	$(submit_data).each(function(i){
		copy = model.clone(true);
		copy.attr('style','display:block');
		copy.attr('name','submit_question');
		copy.children().eq(1).html(this.sxh);
		copy.children().eq(2).html('<a href="#none" class="blue12" onclick="modifyquestion(this)">'+submit_data[i].title+'</a>');
		copy.children().eq(3).html($(qsnTypesMapper).attr(this.tixing));
		copy.children().eq(4).html($(dspTypesMapper).attr(this.zxfs));
		copy.children().eq(5).html(this.beizhu);
  		copy.attr('questionModifyflag',this.questionModifyflag);
		model.parent().children().eq(-1).after(copy);
	});
	resortQuestion();
}

/**
*   锁定页面
* @param hintMsg
*/
function blockPage(hintMsg){
 var html = '<div id = "loading" style = "text-align:center;height:150px; background-image: url('
	+ webContext
	+ '/style/images-common/waiting/loading.gif);background-position: center;background-repeat: no-repeat;" >'
	+ '<div id = "hintDiv" style="position: relative; top:65%;"><span id = "hint" style="">'+hintMsg+'</span></div></div>';
  $.blockUI({
	 message : html,
	 css : {
		 "border-style" : "none",
		 height : '150px',
		 cursor : 'wait'
		 },
	overlayCSS : {
	   backgroundColor : '#000',
	   opacity : 0.3
	   }
	  });  		  
}
/**
*  解锁页面
*/
function  unBlockPage(){
    $.unblockUI(); 
}

/**
 * 得到下拉框中文值
 * @param sc
 * @returns
 */
function ucGetSelectControlText(sc){
	var oSel = document.getElementById(sc);
	return oSel.options[oSel.selectedIndex].innerHTML;
}

/**
 * 跟随滚动条 滑动
 */
function afterScroll() {
	$("#divSuspended").css("top", $(window).scrollTop());
	$("#sideBar").css("top", $(window).scrollTop());
};

$(window).scroll(afterScroll);

$(window).load(function(){
	 afterScroll();
});

/**
 * 
 * @param element
 */
function optionDown(element){
	var optionNum = $('tr[name="select_option"][optionModifyflag!="deleted"]').size();
	if(optionNum<2){
		alert('只剩一行，不必调序！');
	}else{
		var target = $(element).parents('tr[name=select_option]');
		var transformer =target.clone(true);
		target.next().after(transformer);
		target.remove();
		resortOption();
	}
}

/**
 * 
 * @param element
 */
function optionUp(element){
	var optionNum = $('tr[name="select_option"][optionModifyflag!="deleted"]').size();
	if(optionNum<2){
		alert('只剩一行，不必调序！');
	}else{
		var target = $(element).parents('tr[name=select_option]');
		var transformer =target.prev().clone(true);
		target.after(transformer);
		target.prev().remove();
		resortOption();
	}
}

/**
 * 
 * @param element
 */
function questionDown(element){
	var optionNum = $('tr[name="submit_question"][questionModifyflag!="deleted"]').size();
	if(optionNum<2){
		alert('只剩一行，不必调序！');
	}else{
		var target = $(element).parents('tr[name=submit_question]');
		var pointer = parseInt(target.children().eq(1).text(),10);
		var transformer = submit_data[pointer-1];
		submit_data[pointer-1] = submit_data[pointer];
		submit_data[pointer] = transformer;
		submit_data[pointer-1].sxh=pointer;
		submit_data[pointer].sxh=pointer+1;
		
		transformer =target.clone(true);
		target.next().after(transformer);
		target.remove();
	}
	resortQuestion();
}

/**
 * 
 * @param element
 */
function questionUp(element){
	var optionNum = $('tr[name="submit_question"][questionModifyflag!="deleted"]').size();
	if(optionNum<2){
		alert('只剩一行，不必调序！');
	}else{
		var target = $(element).parents('tr[name=submit_question]');
		var pointer = parseInt(target.children().eq(1).text(),10);
		var transformer = submit_data[pointer-1];
		submit_data[pointer-1] = submit_data[pointer-2];
		submit_data[pointer-2] = transformer;
		submit_data[pointer-1].sxh=pointer;
		submit_data[pointer-2].sxh=pointer-1;
		
		transformer =target.prev().clone(true);
		target.after(transformer);
		target.prev().remove();
	}
	resortQuestion();
}

