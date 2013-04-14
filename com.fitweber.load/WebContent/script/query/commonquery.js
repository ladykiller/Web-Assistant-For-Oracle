var pageNum=1,pageSize=15;
var columnData = [];
jQuery(document).ready(function(){

	jQuery('#planModel').children().eq(5).hover(
			function(){
				jQuery(this).children().attr('style','display:block');
			},
			function(){
				jQuery(this).children().attr('style','display:none');
		});
	
	jQuery('#paramModel').children().eq(3).hover(
			function(){
				jQuery(this).children().attr('style','display:block');
			},
			function(){
				jQuery(this).children().attr('style','display:none');
		});
	
	jQuery.post("/extend/commonQuery/getTableNames.do",{},function(datas){
		$("#tableName").autocomplete(datas, {
			minChars: 0,
			max: 1000,
			scrollHeight: 220
		});
	},"json");
	
	
	var copy = jQuery('#paramModel').clone(true);
	copy.attr('style','display:block').attr('id','param_1');
	copy.children().eq(0).children().eq(0).attr('id','paramName_1');
	copy.children().eq(1).children().eq(0).attr('id','paramValue_1');
	copy.children().eq(2).children().eq(0).attr('id','paramLogic_1');
	jQuery('#paramModel').after(copy);
	
	jQuery('#req_rework').click(function(){
		jQuery('#req_nf_sid').val('');
		jQuery('#req_hx_sid').val('');
		jQuery('#req_nf_hx_jsp').val('');
		jQuery('#req_hx_nf_jsp').val('');
	});
	
	jQuery('#prePage').click(function(){
		if(pageNum>1){
			pageNum=pageNum-1;
			pageSize=15;
			commonQuery(pageNum,pageSize);
		}else{
			alert('已经是第一页了！');
		}
	});
	
	jQuery('#nextPage').click(function(){
		pageNum=pageNum+1;
		if(pageNum>parseFloat(jQuery('#totalNum').val())){
			alert(pageNum+'超出总页数！');
			pageNum=pageNum-1;
			return;
		}else{
			pageSize=15;
			commonQuery(pageNum,pageSize);
		}
	});
	
	jQuery('#jumpPage').click(function(){
		var oldPageNum = pageNum;
		var jumpPageNum = jQuery('#jumpPageNum').val();
		if(parseFloat(jumpPageNum).toString()==='NaN'){
			alert('请输入数值！');
			return;
		}
		pageNum=parseInt(parseFloat(jumpPageNum));
		if(pageNum>parseFloat(jQuery('#totalNum').val())){
			alert(pageNum+'超出总页数！');
			pageNum = oldPageNum;
			return;
		}else{
			pageSize=15;
			commonQuery(pageNum,pageSize);
		}
	});
	
	jQuery('#req_search').click(function(){
		pageNum=1;
		pageSize=15;
		commonQuery(pageNum,pageSize);
	});
	
	jQuery('#param_search').click(function(){
		pageNum=1;
		pageSize=15;
		commonQueryByParam(pageNum,pageSize);
	});

	jQuery('#sql_search').click(function(){
		pageNum=1;
		pageSize=15;
		commonQueryBySQL(pageNum,pageSize);
	});
	
	jQuery('#tableName').blur(function(){
		jQuery('input[id*="paramName_"]').unautocomplete();
		var tableName = jQuery('#tableName').val();
		if(tableName!==null&&tableName!==''){
			jQuery.post("/extend/commonQuery/getColumns.do",{'tableName':tableName},function(datas){
				columnData=datas;
				jQuery('input[id*="paramName_"]').autocomplete(datas, {
					minChars: 0,
					max: 1000,
					scrollHeight: 220
				});
			},"json"); 
		}
	});
	
});

function commonQueryByParam(pageNum,pageSize){
	var queryData = {};
	queryData.tableName = jQuery('#tableName').val();
	var paramArray = [];

	jQuery('tr[id*="param_"]').each(function(i){
		var paramObject ={};
		paramObject.paramName = jQuery(this).children().eq(0).children().eq(0).val();
		paramObject.paramValue = jQuery(this).children().eq(1).children().eq(0).val();
		paramObject.paramLogic = jQuery(this).children().eq(2).children().eq(0).val();
		paramArray.push(paramObject);
	});
	queryData.paramArray=paramArray; 
	queryData.pageNum=pageNum;
	queryData.pageSize=pageSize;
	
	var jsondata = JSON.stringify(queryData);
	jQuery.post("/extend/commonQuery/commonQueryByParam.do",{json:jsondata},function(datas){
		jQuery('#pageNum').val(pageNum);
		var totalNum = datas.totalNum;
		if(totalNum%pageSize===0){
			jQuery('#totalNum').val(parseInt(totalNum/pageSize));
		}else{
			jQuery('#totalNum').val(parseInt(totalNum/pageSize)+1);
		}
		//构建一张表，返回数据中包含的列名用作表头
		var columns = datas.columns;
		var resultList = datas.resultList;
		var content='',temp;
		jQuery(columns).each(function(){
			content=content+'<th>'+this+'</th>';
		});
		jQuery('#tableHead').children().remove();
		jQuery('#tableHead').append(content);
		content='';
		var i,j,resultSize=resultList.length,columnSize=columns.length;
		for(i=0;i<resultSize;i++){
			temp = resultList[i];
			content=content+'<tr id="result_'+i+'">';
			for(j=0;j<columnSize;j++){
				content=content+'<td>'+jQuery(temp).attr(columns[j])+'</td>';
			}
			content=content+'</tr>';
		}
		jQuery('tr[id*="result_"]').remove();
		jQuery(content).insertAfter(jQuery('#tableHead'))
	},"json"); 
}

function commonQueryBySQL(pageNum,pageSize){
	var queryData = {};
	queryData.sql=jQuery('#sql_statment').val(); 
	queryData.pageNum=pageNum;
	queryData.pageSize=pageSize;
	
	var jsondata = JSON.stringify(queryData);
	jQuery.post("/extend/commonQuery/commonQueryBySQL.do",{json:jsondata},function(datas){
		jQuery('#pageNum').val(pageNum);
		var totalNum = datas.totalNum;
		if(totalNum%pageSize===0){
			jQuery('#totalNum').val(parseInt(totalNum/pageSize));
		}else{
			jQuery('#totalNum').val(parseInt(totalNum/pageSize)+1);
		}
		//构建一张表，返回数据中包含的列名用作表头
		var columns = datas.columns;
		var resultList = datas.resultList;
		var content='',temp;
		jQuery(columns).each(function(){
			content=content+'<th>'+this+'</th>';
		});
		jQuery('#tableHead').children().remove();
		jQuery('#tableHead').append(content);
		content='';
		var i,j,resultSize=resultList.length,columnSize=columns.length;
		for(i=0;i<resultSize;i++){
			temp = resultList[i];
			content=content+'<tr>';
			for(j=0;j<columnSize;j++){
				content=content+'<td>'+jQuery(temp).attr(columns[j])+'</td>';
			}
			content=content+'</tr>';
		}
		jQuery('tr[id*="result_"]').remove();
		jQuery(content).insertAfter(jQuery('#tableHead'))
	},"json"); 
}

function commonQuery(pageNum,pageSize){
	var submitData = {};
	submitData.req_nf_sid = jQuery('#req_nf_sid').val();
	submitData.req_hx_sid = jQuery('#req_hx_sid').val();
	submitData.req_nf_hx_jsp = jQuery('#req_nf_hx_jsp').val();
	submitData.req_hx_nf_jsp = jQuery('#req_hx_nf_jsp').val();
	submitData.pageNum=pageNum;
	submitData.pageSize=pageSize;
	
	var jsondata = JSON.stringify(submitData);
	jQuery.post("/extend/bwzh/searchbwzhgx.do",{json:jsondata},function(datas){
		jQuery('#pageNum').val(pageNum);
		var totalNum = datas.totalNum;
		if(totalNum%pageSize===0){
			jQuery('#totalNum').val(parseInt(totalNum/pageSize));
		}else{
			jQuery('#totalNum').val(parseInt(totalNum/pageSize)+1);
		}
		var resultSize = jQuery(datas.bwzhList).size();
		var target,copy,n;
		jQuery('tr[id*="bwzh_"]').remove();
		var sample = jQuery('#planModel').clone(true);
		sample.css('display','block').attr('id','bwzh_1');
		jQuery('#planModel').after(sample);
		for(n=1;n<resultSize;n++){
			target = jQuery('#bwzh_'+n);
			copy = target.clone(true);
			target.after(copy);
			resort();
		}
		var bwzhList = datas.bwzhList;
		if(jQuery(bwzhList).size()===0){
			jQuery('#bwzh_1').remove();
		}
		jQuery(bwzhList).each(function(i){
			target = jQuery('#bwzh_'+(i+1));
			target.attr('bwzh_id',this.id);
			target.children().eq(1).children().eq(0).val(this.nf_sid);
			target.children().eq(2).children().eq(0).val(this.hx_sid);
			target.children().eq(3).children().eq(0).val(this.nf_hx_jsp);
			target.children().eq(4).children().eq(0).val(this.hx_nf_jsp);
		});
	},"json"); 
}

function addParam(element){
	var target = jQuery(element).parent().parent().parent();
	var copy = target.clone(true);
	copy.children().eq(0).children().eq(0).val("");
	copy.children().eq(1).children().eq(0).val("");
	//copy.children().eq(2).children().eq(0).val("");
	copy.children().eq(3).children().attr('style','display:none');
	target.after(copy);
	jQuery('input[id*="paramName_"]').autocomplete(columnData, {
		minChars: 0,
		max: 1000,
		scrollHeight: 220
	});
	resortParam();
}

function delParam(element){
	var target = jQuery(element).parent().parent().parent();
	target.remove();
	resortParam();
}

function modify(element){
	var target = jQuery(element).parent().parent().parent();
	var bwzh = {};
	bwzh.id=target.attr('bwzh_id');
	bwzh.nf_sid=target.children().eq(1).children().eq(0).val();
	bwzh.hx_sid=target.children().eq(2).children().eq(0).val();
	bwzh.nf_hx_jsp=target.children().eq(3).children().eq(0).val();
	bwzh.hx_nf_jsp=target.children().eq(4).children().eq(0).val();
	var jsondata = JSON.stringify(bwzh);
	jQuery.post("/extend/bwzh/modifybwzhgx.do",{json:jsondata},function(datas){
		if(datas.status!==null&&datas.status!==undefined){
			alert(datas.status);
		}
	},"json");
}

function del(element){
	var target = jQuery(element).parent().parent().parent();
	var bwzh = {};
	bwzh.id=target.attr('bwzh_id');
	var jsondata = JSON.stringify(bwzh);
	jQuery.post("/extend/bwzh/delbwzhgx.do",{json:jsondata},function(datas){
		if(datas.status!==null&&datas.status!==undefined){
			alert(datas.status);
		}
	},"json");
	target.remove();
	resort();
}

function resort(){
	var index;
	jQuery('tr[id*="bwzh_"]').each(function(i){
		index=i+1;
		jQuery(this).children().eq(0).text(index+(pageNum-1)*pageSize);
		jQuery(this).attr('id','bwzh_'+index);
		jQuery(this).children().eq(1).children().eq(0).attr('id','nf_sid_'+index).attr('name','nf_sid_'+index);
		jQuery(this).children().eq(2).children().eq(0).attr('id','hx_sid_'+index).attr('name','hx_sid_'+index);
		jQuery(this).children().eq(3).children().eq(0).attr('id','nf_hx_jsp_'+index).attr('name','nf_hx_jsp_'+index);
		jQuery(this).children().eq(4).children().eq(0).attr('id','hx_nf_jsp_'+index).attr('name','hx_nf_jsp_'+index);
	});
}

function resortParam(){
	var index;
	jQuery('tr[id*="param_"]').each(function(i){
		index=i+1;
		jQuery(this).attr('id','param_'+index);
		jQuery(this).children().eq(0).children().eq(0).attr('id','paramName_'+index);
		jQuery(this).children().eq(1).children().eq(0).attr('id','paramValue_'+index);
		jQuery(this).children().eq(2).children().eq(0).attr('id','paramLogic_'+index);
	});
}