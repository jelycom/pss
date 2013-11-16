$(function(){														//会计期间脚本
		var datatable_tr = $(".datatable tbody tr");
		$(".date").val("");
		var c_year = new Date().getFullYear();					//获取当前年份
		//下拉框的值改变并且指定日期的单选按钮选中时，将选中的日期自动填充到表中，
		$("select").change(function(){
			if($("#appoint").is(":checked")){
				datatable_tr.each(function(index){
					var fixed_day = $("select").val();
					var start_day = Number(fixed_day) + 1;
					var c_tr = $(this).children(".ordinal").text();							//获取当前行的行号和下行的行号作为开始月份和结束月份
					var p_tr = $(this).prev().children(".ordinal").text();
					if (fixed_day < 10){
						fixed_day = "0" + fixed_day;	
					}
					if (start_day < 10){
						start_day = "0" + start_day;	
					}else if(start_day >= 31){
						start_day = "01";
						p_tr = c_tr;
					}
					if(fixed_day == 31 || fixed_day == 30){
						switch (c_tr){
							case "2":
								fixed_day = 28;
								if (c_year % 4 == 0){
									fixed_day = 29;
								}
								break;
							case "4":
							case "6":
							case "9":
							case "11":
								fixed_day = 30;
								break;
							default:
								fixed_day = 31;
								break;
						}
					}
					if (c_tr < 10){
						c_tr = "0" + c_tr;	
					}
					if (p_tr < 10){
						p_tr = "0" + p_tr;	
					}
					var startdate = c_year + "-" + p_tr + "-" + start_day;
					var enddate = c_year + "-" + c_tr + "-" + fixed_day;
					if(c_tr == 1 && fixed_day < 30){
						p_tr = 12;
						startdate = (c_year-1) + "-" + p_tr + "-" + start_day;
					}
					$(this).find("input[name=startdate]").val(startdate);
					$(this).find("input[name=enddate]").val(enddate);
				});
			}
		});
		
		//选择自定义期间时清空表格数据
		$("#custom").click(function(){
			$(".datatable input").val("");
		});
		
		$(".datatable input").click(function(){
			var inputobj = $(this);
/*			if($("#appoint").is(":checked")){									//选择了固定结账时间，不能再点击表格内手动选择
				alert("固定结账时间，不能手动选择");
				$(this).trigger("blur");
				$("body > div").eq(1).hide();
			}*/
			$("#custom").attr("checked",true);
			var prev_enddate =inputobj.parents("tr").prev().find("input[name=enddate]").val();				//获取到上一行的结束时间来计算本行的开始时间
			if (inputobj.attr("name") == "startdate" && prev_enddate !=""){
				var start_year = Number(prev_enddate.substring(0,4));
				var start_month = Number(prev_enddate.substring(5,7));
				var start_day = Number(prev_enddate.substring(8))+1;
				//日期不足两位加0,大月和小月及闰年的处理
				if (start_day<10){
					start_day = "0" + start_day;
				}else if(start_day ==31 && (start_month == 4 || start_month == 6 || start_month == 9|| start_month == 11)){
					start_day = "01";
					start_month = start_month + 1;
				}else if(start_day ==32){
					start_day = "01";
					start_month = start_month + 1;
					if (start_month == 13){													//当结束时间是12月31日时,将时间设定为下一年
						start_month = 1;
						start_year += 1;
					}
				}else if (start_day ==29 && start_month == 2 && (start_year % 4) != 0){
					start_day = "01";
					start_month = start_month + 1;
				}else if (start_day ==30 && start_month == 2 && (start_year % 4) == 0){
					start_day = "01";
					start_month = start_month + 1;
				}
				
				if (start_month<10){
					start_month = "0" + start_month;
				}
				var start_date = start_year+"-"+start_month+"-"+start_day;
				inputobj.val(start_date);
				$(this).trigger("blur");
				$("body > div").eq(1).hide();
			}
		});
		
		//点击确定提交的时候校验开始日期和结束日期是否正确
		$(".accept").click(function(){
			datatable_tr.each(function(){
				var startdate = $(this).find("input[name=startdate]").val();
				var enddate = $(this).find("input[name=enddate]").val();
				if (enddate != "" && startdate != ""){
					if (enddate < startdate){
						alert("输入的时间有误，请核对！");
						$(this).find("input[name=enddate]").trigger("focus").trigger("select");
					}
				}else if ((enddate == "" && startdate != "") ||( enddate != "" && startdate == "")){
					alert("时间没有填写完整！");
				}
			});
		});
});