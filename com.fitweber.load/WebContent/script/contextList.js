jQuery(document).ready(function(){
	$('#content').css('width',screen.availWidth-220);
	$('#commonqueryManagement').click(function(){
		$('#childFrame').attr('src','commonquery/commonquery.html');
	});
	$('#workdailyManagement').click(function(){
		$('#childFrame').attr('src','workdaily/workdaily.html');
	});
	$('#surveyManagement').click(function(){
		$('#childFrame').attr('src','commonquery/commonquery.html');
	});
});


