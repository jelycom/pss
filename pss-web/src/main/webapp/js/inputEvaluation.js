  /** jQuery inputEvaluation Plugin 
    * 自动给input(除file)赋值.
	* 好处:不用显式的页面上的input赋值，可以直接从后台传递过来json串
	* 缺点:会导致form的reset的reset方法失去意义.
	* jquery 版本不能低于1.5
	* Copyright(c) 2012 bysag
	* Email:zhengyun_an@163.com
	* msn:zhengyun_an@hotmail.com
	* Version: 2.0  -- 提高点性能
	*/
(function($){
	$.fn.inputEvaluation=function parseOBJ(options){			
			if(options!=null&&$.trim(options).length>0){				
				var options_json_obj ;
				try{					
					if($.type(options)=='string'){
						options_json_obj = $.parseJSON(options);
					}else if($.type(options)=='object'||$.type(options)=='array'){					
						options_json_obj = options;
					}else{
						alert('error options is not string,object,array.');
					}//判断传入的是否为可解析成json的String或者object或者array,如果不能解析就报错误										
					var json_obj_length = options_json_obj.length;//取得元素的个数,有值表示是json对象数组否则为单个对象
					if(json_obj_length!=undefined){//[{},{},{}]...					
						for(var i=0;i<json_obj_length;i++){	
						 //wait Version 2.0 ...						
						}
					}else{//{}										
						 for(var key in options_json_obj){//循环此对象的所有属性	
							
							var input_obj = $(this).find(":input[id='"+key+"']");	
							//:input[name^='']取得json数据的key对应的dom对象,包括所有 input, textarea, select 和 button 元素
							var input_length = input_obj.length;																			
							for(var j=0;j<input_length;j++){		
								var sub_input_obj = $(input_obj[j]);//input obj						
								if($.trim(sub_input_obj.attr("id"))==$.trim(key)){//原来为name
									var type = sub_input_obj.attr("type");		//取当前Input对象的类型																																
									 console.debug(jQuery.type(options_json_obj[key]));
									var json_value =$.trim(options_json_obj[key]);//json value									
									if(type=='text'||type=='hidden'||type=='button'||type=='submit'||type=='reset'||type=='password'){
										$(sub_input_obj).attr("value",json_value);
									}else if(type=='checkbox'){
										var input_value = sub_input_obj.attr("value");	
										if(json_value.indexOf(",")!=-1){// checkbox many
											var array_json_value = 	json_value.split(",");		
											for(var k=0;k<array_json_value.length;k++){					
												if($.trim(input_value)==array_json_value[k]){																	
													$(sub_input_obj).attr("checked","checked");												
												}
											}
										}else{
											if($.trim(input_value)==json_value){																	
											  $(sub_input_obj).attr("checked","checked");												
											}
										}
									}else if(type=='radio'){
										var input_value = sub_input_obj.attr("value");	
										if($.trim(input_value)==json_value){																	
										  $(sub_input_obj).attr("checked","checked");												
										}
									}else if(type=='textarea'){										
										$(sub_input_obj).val(json_value);
									}else if(type=="select-one"||type=="select-multiple"){									
										$.each($(sub_input_obj).children(),function(i,obj){		
												if(json_value.indexOf(",")!=-1){
													var s_array = json_value.split(",");	
													for(var h=0;h<s_array.length;h++){
														if(s_array[h]==$.trim($(obj).attr("value"))){												
															$(obj).attr("selected","selected");
														}		
													}																			
												}else{
													 if(json_value==$.trim($(obj).attr("value"))){												
															$(obj).attr("selected","selected");
													 }	
												}
											});										
									}
								}
							}
     					 }
					}
				}catch(e){
					alert('error: options '+e+'. example:{"title":"1"}');
				}				
			}else{
				alert('error: options is null or length=0.');
			};	
	};
})(jQuery);
