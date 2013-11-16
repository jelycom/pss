;(function($){
	var methods={
			info:function(msg,fn){
			var dialogid="messageDialogDiv";
			var setting=jQuery.extend({title:'提示'},$.showmsg.defaults);
			var message;
			if(typeof msg ==="object"&&msg["message"]){
				setting=jQuery.extend(setting,msg);
				message=msg["message"];
			}else if(typeof msg ==="string"){
				message=msg;
			}else{
				return false;
			}
			if (!$("body").is(":has(#"+dialogid+")")){
				var messageDialog = $("<div id='"+dialogid+"'></div>");
				messageDialog.appendTo("body");	
			}
			var message="<p><span class=\"ui-icon ui-icon-alert\" style=\"float: left; margin: 0 7px 20px 0;\"></span>"+message+"</p>";
			$("#"+dialogid).html(message);
			$("#"+dialogid).dialog(setting);
			$("#"+dialogid).dialog("open");
		},
		warn:function(){
			
		},
		error:function(){
			
		}
	};
	$.showmsg=function(method){
	  if ( methods[method] ) {
	      return methods[method].apply( this, Array.prototype.slice.call(arguments, 1));
	    } else if ( typeof method === 'object' || ! method ) {
	      return methods.info.apply( this, Array.prototype.slice.call(arguments, 1) );
	    } else {
	      $.error( 'Method ' +  method + ' does not exist on jQuery.showmsg' );
	    }   
	};
	$.showmsg.defaults={
			width:400,
			height:"auto",
			autoOpen: false,
			modal:true,
			resizable:false,
			buttons:{"确定":function(callback){
				$(this).dialog("close");
			}}};
})(jQuery);
//;(function($){
//	$.extend({
//		showmsg:{
//			info:function(setting,msg){
//				var dialogid="messageDialogDiv";
//				setting=$.extend({},$.showmsg.defaults,setting||{});
//				if (!$("body").is(":has(#"+dialogid+")")){
//					var messageDialog = $("<div id='"+dialogid+"'></div>");
//					messageDialog.appendTo("body");	
//				}
//				var msgdlg=$("#"+dialogid);
//				var message="<p><span class=\"ui-icon ui-icon-alert\" style=\"float: left; margin: 0 7px 20px 0;\"></span>"+msg+"</p>";
//				msgdlg.html(message);
//				return msgdlg.dialog(setting);
//			},
//			warn:function(){
//				
//			},
//			error:function(){
//				
//			}
//		}
//	});
//	$.showmsg.defaults=	{
//			width:400,
//			height:"auto",
//			modal:true,
//			resizable:false,
//			buttons:{"确定":function(){
//				$(this).dialog("close");
//			}}};
//})(jQuery);