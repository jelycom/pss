function myAutocomplete(){
$(function() {
		var autocomplete = $(".autocomplete"),
		data = autocomplete.attr("id"),
		title = autocomplete.attr("title");
		var getId = $(".getId"),
		value = getId.attr("id");
   		 $("#"+data).autocomplete(title,
   		  { 
			 extraParams:{value:function(){return $("#"+data).val();}},   
         	matchSubset: false,//缓存问题，默认为true，在缓存中读取
         	autoFill: true, 
         //width: 310, 
        	 matchContains: true, 
         	autoFill: false,
         	dataType:'json',//返回数据类型
        	parse:function(data){   
        	var rows=[];   
        	for(var i=0;i<data.length;i++){
             //一定要按以下格式设置数据 
             rows[rows.length]={   
                 data:data[i].name,  
                 value:data[i].id,//值 
                 result:data[i].name//返回结果显示内容 
                 
            };
         } 
         return rows;  
    },   
    formatItem:function(row, i, n) {
         return "<I>"+row+"</I>"; 
    }
  }).result(function(row,i,n){
  		$("#"+value).val(n);
 });
 });
}