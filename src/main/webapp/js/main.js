var ServerVar = {
	st : {
		ok : "ok",
		faild : "faild"
	}

};

String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
};

var SyUtil = {
	getFormVals : function(seltor) {
		var json = {}, p = null;
		if (seltor) {
			p = $(seltor);
		} else {
			p = $("body");
		}
		p.find("input[type=text]").each(function(idx, item) {
			json[$(this).attr("name")] = $(this).val();
		});
		p.find("input[type=hidden]").each(function(idx, item) {
			json[$(this).attr("name")] = $(this).val();
		});
		p.find("input[type=password]").each(function(idx, item) {
			json[$(this).attr("name")] = $(this).val();
		});
		p.find("input[type=radio]:checked").each(function(idx, item) {
			json[$(this).attr("name")] = $(this).val();
		});
		p.find("textarea").each(function(idx, item) {
			json[$(this).attr("name")] = $(this).val();
		});
		p.find("select").each(function(idx, item) {
			json[$(this).attr("name")] = $(this).val();
		});
		p.find("input[type=checkbox]:checked").each(function(idx, item) {
			var obj = $(this);
			var name = obj.attr("name");
			if (!json[name]) {
				json[name] = "";
			}
			json[name] += obj.val() + ",";
		});
		return json;
	}
};

// dataTable wrapper

$.fn.dataTableExt.oApi.fnRepage = function(oSettings, reduceRow) {
	var batchSize = typeof (reduceRow) != "undefined" ? reduceRow : 1;
	var pageinfo = oSettings.oInstance.fnPagingInfo();
	if (oSettings._iDisplayStart > 0
			&& oSettings._iRecordsTotal % oSettings._iDisplayLength == batchSize
			&& ((pageinfo.iPage + 1) == Math.ceil(oSettings._iRecordsTotal
					/ oSettings._iDisplayLength))) {
		pageinfo.iPage = pageinfo.iPage - 1;
	}
	if (oSettings._iDisplayStart > 0
			&& oSettings._iDisplayLength == batchSize
			&& ((pageinfo.iPage + 1) == Math.ceil(oSettings._iRecordsTotal
					/ oSettings._iDisplayLength))) {
		pageinfo.iPage = pageinfo.iPage - 1;
	}
	this.fnPageChange(pageinfo.iPage);
	this.find(":checkbox.tbl_chk_all").removeAttr("checked");
};

$.fn.dataTableExt.oApi.fnGetSelectedRowsId = function(oSettings) {
	var ids = [], self = this;
	this.find(":checked:not(:checkbox.tbl_chk_all)").each(function() {
		var trObj = $(this).parents("tr").get(0);
		ids[ids.length] = self.fnGetData(trObj).id;
	});
	return ids;
};

/* Set the defaults for DataTables initialisation */
$.extend( true, $.fn.dataTable.defaults, {
	"sDom": "<'row-fluid'<'span6'l><'span6'f>r>t<'row-fluid'<'span6'i><'span6'p>>",
	"sPaginationType": "bootstrap",
	"oLanguage": {
		"sLengthMenu": "_MENU_ records per page"
	}
} );


/* Default class modification */
$.extend( $.fn.dataTableExt.oStdClasses, {
	"sWrapper": "dataTables_wrapper form-inline"
} );


/* API method to get paging information */
$.fn.dataTableExt.oApi.fnPagingInfo = function ( oSettings )
{
	return {
		"iStart":         oSettings._iDisplayStart,
		"iEnd":           oSettings.fnDisplayEnd(),
		"iLength":        oSettings._iDisplayLength,
		"iTotal":         oSettings.fnRecordsTotal(),
		"iFilteredTotal": oSettings.fnRecordsDisplay(),
		"iPage":          oSettings._iDisplayLength === -1 ?
			0 : Math.ceil( oSettings._iDisplayStart / oSettings._iDisplayLength ),
		"iTotalPages":    oSettings._iDisplayLength === -1 ?
			0 : Math.ceil( oSettings.fnRecordsDisplay() / oSettings._iDisplayLength )
	};
};


/* Bootstrap style pagination control */
$.extend( $.fn.dataTableExt.oPagination, {
	"bootstrap": {
		"fnInit": function( oSettings, nPaging, fnDraw ) {
			var fnClickHandler = function ( e ) {
				e.preventDefault();
				if ( oSettings.oApi._fnPageChange(oSettings, e.data.action) ) {
					fnDraw( oSettings );
				}
			};

			$(nPaging).addClass('pagination').append(
				'<ul>'+
					'<li class="prev disabled"><a href="#">«</a></li>'+
					'<li class="next disabled"><a href="#">»</a></li>'+
				'</ul>'
			);
			var els = $('a', nPaging);
			$(els[0]).bind( 'click.DT', { action: "previous" }, fnClickHandler );
			$(els[1]).bind( 'click.DT', { action: "next" }, fnClickHandler );
		},

		"fnUpdate": function ( oSettings, fnDraw ) {
			var iListLength = 5;
			var oPaging = oSettings.oInstance.fnPagingInfo();
			var an = oSettings.aanFeatures.p;
			var i, ien, j, sClass, iStart, iEnd, iHalf=Math.floor(iListLength/2);

			if ( oPaging.iTotalPages < iListLength) {
				iStart = 1;
				iEnd = oPaging.iTotalPages;
			}
			else if ( oPaging.iPage <= iHalf ) {
				iStart = 1;
				iEnd = iListLength;
			} else if ( oPaging.iPage >= (oPaging.iTotalPages-iHalf) ) {
				iStart = oPaging.iTotalPages - iListLength + 1;
				iEnd = oPaging.iTotalPages;
			} else {
				iStart = oPaging.iPage - iHalf + 1;
				iEnd = iStart + iListLength - 1;
			}

			for ( i=0, ien=an.length ; i<ien ; i++ ) {
				// Remove the middle elements
				$('li:gt(0)', an[i]).filter(':not(:last)').remove();

				// Add the new list items and their event handlers
				for ( j=iStart ; j<=iEnd ; j++ ) {
					sClass = (j==oPaging.iPage+1) ? 'class="active"' : '';
					$('<li '+sClass+'><a href="#">'+j+'</a></li>')
						.insertBefore( $('li:last', an[i])[0] )
						.bind('click', function (e) {
							e.preventDefault();
							oSettings._iDisplayStart = (parseInt($('a', this).text(),10)-1) * oPaging.iLength;
							fnDraw( oSettings );
						} );
				}

				// Add / remove disabled classes from the static elements
				if ( oPaging.iPage === 0 ) {
					$('li:first', an[i]).addClass('disabled');
				} else {
					$('li:first', an[i]).removeClass('disabled');
				}

				if ( oPaging.iPage === oPaging.iTotalPages-1 || oPaging.iTotalPages === 0 ) {
					$('li:last', an[i]).addClass('disabled');
				} else {
					$('li:last', an[i]).removeClass('disabled');
				}
			}
		}
	}
} );


$.fn.extend({
	syDTbl : function(_ops) {
		var ops = $.extend(true, {
			sDom: "t<'row-fluid'<'span6'i><'span6'p>>",
			bAutoWidth : true,
			bFilter : false,
			bSort : false,
			bLengthChange : false,
			bPaginate : true,
			sPaginationType : "bootstrap",
			oLanguage : {
				"sUrl" : "js/i18n/table_zh.txt"
			},
			bServerSide : true,
			extraPostData:{}
		}, _ops);

		var self = this;

		if (self.data("syDTbl")) {
			return self;
		}
		self.removeClass().addClass("table table-striped table-bordered").attr("width","100%");
		self.dataTable($.extend(true, ops, {
			bInfo:ops.bPaginate,
			fnServerData : function(sSource, aoData, fnCallback) {
				var pd = ops.filterFormId ? SyUtil.getFormVals("#"+ ops.filterFormId) : {};
				if (ops.bPaginate) {
					var aoDataMap = {};
					$.each(aoData, function(index, obj) {
						aoDataMap[obj.name] = obj.value;
					});
					pd["offset"] = aoDataMap.iDisplayStart;
					pd["fetchSize"] = aoDataMap.iDisplayLength;
				}
				pd=$.extend(true,pd,ops.extraPostData);
				$.syRequest({
					type : "GET",
					url : ops.url,
					data : pd,
					beforeSend : function() {
					},
					success : function(data) {
						var aoData = ops.bPaginate? {
							iDisplayStart : data.offset,
							iDisplayLength : data.fetchSize,
							aaData : data.data,
							iTotalDisplayRecords : data.total,
							iTotalRecords : data.total
						}:{aaData:data};
						fnCallback(aoData);
					},
					complete : function() {
					}
				});
			}
		})).parent().addClass("response-table");

		var chks = self.find(":checkbox.tbl_chk_all");
		if (chks.length) {
			chks.click(function() {
				var o = $(this), bChk = o.is(":checked");
				self.find(":checkbox").not(o).each(function() {
					var oc = $(this);
					if (bChk && !oc.attr("disabled")) {
						oc.attr("checked", "checked");
					} else {
						oc.removeAttr("checked");
					}
				});

			});

			$((ops.range?ops.range+" ":"")+".tbl_batchdel_btn").bind("click", function() {
				var url = $(this).data("url");
				var sels = self.fnGetSelectedRowsId();
				if (sels.length == 0) {
					alert("请选择要删除的记录.");
					return;
				}
				if (!confirm("你确定删除这些记录吗？")) {
					return;
				}
				$.syRequest({
					type : "POST",
					url : url,
					data : {
						ids : sels.join()
					},
					beforeSend : function() {
					},
					success : function(data) {
						self.fnRepage(sels.length);
					},
					complete : function() {
					}
				});
			});
		}

		self.find(".tbl_del_btn").live("click", function(e) {
			var o = $(e.target);
			var id = o.data("id");
			if (!confirm("你确定要删除ID为 [" + id + "]的记录吗?")) {
				return "";
			}
			var url=o.data("url");
			var idx=url.indexOf("?");
			var postData={};
			if(idx!=-1){
				if(url.substring(idx+1).indexOf("id=")==-1){
					postData["id"]=id;
				}
			}
			$.syRequest({
				type : "POST",
				url : url,
				data : postData,
				beforeSend : function() {
				},
				success : function(data) {
					self.fnRepage();
				},
				complete : function() {
				}
			});
		});
		
		self.find(".tbl_edit_btn").live("click", function(e) {
			var o = $(e.target);
			var id = o.data("id");
			var url=o.data("url");
			if(url.indexOf("?")==-1){
				url+="?id="+id;
			}
			location.href=url;
		});
		
		$((ops.range?ops.range:"")+".tbl_filter_btn").bind("click", function() {
			self.fnPageChange('first');
		});
		
		self.find(".tbl_sort_sec").live("click",function(e){
			var o=$(e.target);
			if(o.data("sortcol")){
				var sortType=o.data("sorttype");
			    if(sortType && sortType=="desc"){
						sortType="asc";
						o.next("img").attr("src","images/sort_asc.png");
				}else{
						sortType="desc";
						o.next("img").attr("src","images/sort_desc.png");
				}
				o.data("sorttype",sortType);
				ops.extraPostData={sortType:sortType,sortCol:o.data("sortcol")};
				self.fnPageChange('first');
			}
		}).after("<img src='images/sort_both.png' />");

		return self.data("syDTbl", true);
	}
});

// ajaxRequest wrapper
$.extend({
	syRequest : function(_ops) {
		$
				.ajax({
					dataType : 'json',
					type : _ops.type ? _ops.type : "GET",
					url : _ops.url,
					data : $.extend(true, {}, _ops.data),
					async : _ops.async == null ? true : _ops.async,
					beforeSend : function(xhr) {
						xhr.setRequestHeader("Accept", "application/json");
						// xhr.setRequestHeader("Content-Type",
						// "application/json");
						if (_ops.beforeSend) {
							_ops.beforeSend(xhr);
						}
					},
					error : function(xhr, msg) {
						if (msg == "error") {
							var st = xhr.status;
							switch (st) {
							case 500:
								var ct = xhr.getResponseHeader("content-type")
										|| "";
								if (!(this.dataType == "json" && ct
										.indexOf("json") >= 0)) {
									break;
								}
								var ex = $.parseJSON(xhr.responseText);
								if (_ops.exception) {
									_ops.exception(ex);// {message,exception-trace,exception-class,request-id}
								} else {
									alert("发生错误：" + ex.message);
								}
								break;
							case 404:
								if (_ops.notfound) {
									_ops.notfound();
								} else {
									alert("所请求资源不存在！");
								}
								break;
							case 401:
								if (_ops.unauthorized) {
									_ops.unauthorized();
								} else {
									alert("没有权限！");
								}
								break;
							case 400:
								break;
							default:
								alert("发生未知错误！");
							}
						} else if (msg == "timeout") {
							if (_ops.timeout) {
								_ops.timeout();
							} else {
								alert("请求超时！");
							}
						}
						if (_ops.error) {
							_ops.error(msg);
						}
					},
					success : function(data) {
						if (_ops.success) {
							_ops.success(data);
						}
					},
					complete : function() {
						if (_ops.complete) {
							_ops.complete();
						}
					}
				});
	}
});

function jscb(param){
	var uuid=param.uuid;
	if(typeof(uuid)!="undefined" && uuid.trim()!=""){
		var clickBtn=$("#"+uuid).button("reset");
		if (param.err) {
			var msg = param.err;
			if (msg.indexOf("size") != -1) {
				msg = "文件太大啦~";
			}
			alert("上传发生了错误:" + msg + "");
			return;
		}
		clickBtn.trigger("callback",param);
	}
}

$.fn.syUpload = function(_ops) {
	
	var ops = $.extend({
		label:"上传文件"
	}, _ops);
	
	return this.each(function(idx) {
		var self = $(this),uuid = "jquery_upload_" + new Date().getTime()+"_"+idx,
		iframe = $("<iframe name=\"' + uuid + '\" style=\"position:absolute;top:-9999px\" />").appendTo("body"),
		form = $("<form target=\"' + uuid + '\" style=\"position:absolute;top:-9999px\" method=\"post\" enctype=\"multipart/form-data\" />").appendTo("body"),
		clickBtn=$("<a href=\"javascript:void(0);\" id=\""+uuid+"\" class=\"btn\" data-loading-text=\"正在努力上传\">"+ops.label+"</a>").insertAfter(self);	
		var url=ops.url,idx=url.indexOf("?");
		if(idx==-1){
			url+="?uuid="+uuid;
		}else{
			url=url.substring(0,idx)+"?uuid="+uuid+"&"+url.substring(idx+1);
		}
		if(url.indexOf("methodName=")==-1){
			url+="&methodName=jscb";
		}
		self.appendTo(form.attr("action", url));
		clickBtn.button().click(function(){
			self.click();
		}).bind("callback",function(e,param){
			if(ops.callback){
				ops.callback(param);
			}
		});
		self.change(function(){
			var val = $(this).val();
			if (val != "") {
				clickBtn.button("loading");
				form.submit();
			}
		});
	});
};

function jscbCKEdit(param) {
	
	var objName=param.objName;
	if(typeof(objName)!="undefined" && objName.trim()!=""){
		var obj=$("textarea[name='"+objName+"']");
		if (param.err) {
			var msg = param.err;
			if (msg.indexOf("size") != -1) {
				msg = "文件太大啦~";
			}
			alert("上传发生了错误:" + msg + "");
			return;
		}
		obj.trigger("callback",param);
	}
}

function buildCkEditor(objName,ops){
	var _ops=$.extend(true,{
		height : 300,
		filebrowserUploadUrl : '',
		filebrowserImageUploadUrl : '',
		language : 'zh-cn',
		toolbar : [{
			name : 'basicstyles',
			groups : ['basicstyles', 'cleanup'],
			items : ['Bold', 'Italic', 'Underline', 'Strike', 'Subscript', 'Superscript', '-', 'RemoveFormat']
		}, {
			name : 'paragraph',
			groups : ['list', 'indent', 'blocks', 'align'],
			items : ['NumberedList', 'BulletedList', '-', 'Outdent', 'Indent', '-', 'Blockquote', '-', 'JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock']
		}, {
			name : 'links',
			items : ['Link', 'Unlink', 'Anchor']
		}, {
			name : 'insert',
			items : ['Image', 'Flash', 'Table', 'HorizontalRule', 'SpecialChar']
		}, '/', {
			name : 'document',
			groups : ['document'],
			items : ['Preview']
		}, {
			name : 'tools',
			items : ['Maximize']
		}, {
			name : 'colors',
			items : ['TextColor']
		}]
	},ops);
	
	var url=_ops.filebrowserImageUploadUrl,idx=url.indexOf("?");
	if(idx==-1){
		url+="?objName="+objName;
	}else{
		url=url.substring(0,idx)+"?objName="+objName+"&"+url.substring(idx+1);
	}
	if(url.indexOf("methodName=")==-1){
		url+="&methodName=jscbCKEdit";
	}
	_ops.filebrowserImageUploadUrl=url;
	$("textarea[name='"+objName+"']").bind("callback",function(e,param){
		if(_ops.buildImgUrl){
			if (param.CKEditorFuncNum) {
				CKEDITOR.tools.callFunction(param.CKEditorFuncNum, _ops.buildImgUrl(param) , '');
			}
		}
	});
	return CKEDITOR.replace(objName, _ops);
}

juicer.register("numberFmt", function(val) {
	var nval = Number(val);
	return Math.round(nval * 100) / 100;
});
juicer.register("notNullFmt", function(val, def) {
	if (typeof (val) == "undefined" || val == null) {
		return def;
	}
	return val;
});
juicer.register("plus", function(val) {
	var rt = 0;
	for ( var i = 0; i < arguments.length; i++) {
		rt += Number(arguments[i]);
	}
	return rt;
});

function toAoDataMap(aoData) {
	var map = [];
	$.each(aoData, function(index, obj) {
		map[obj.name] = obj.value;
	});
	return map;
}

jQuery.validator.addMethod("remotecheck",  
        function(value, element, param) {
            var result=false;
            var url=param,store=$(element).data("store");
            if(store!=null && store==value){
            	result=true;
            }else{
              if(url!=null && url.trim()!=""){
            	  var pd = {};
      			  pd[element.name] = value;
            	  $.syRequest({
            		async:false,
      				type : "POST",
      				url : url,
      				data : pd,
      				beforeSend : function() {
      				},
      				success : function(data) {
      					if(data.msg&&data.msg=="true"){
      						result=true;
      					}
      				},
      				complete : function() {
      					
      				}
      			 });
              }
            }
            return result;
        },
"值已存在！");

jQuery.validator.addMethod("phonecheck",function(value,element,param){
	return /^0?(13[0-9]|15[012356789]|18[0236789]|14[57])[0-9]{8}$/.test(value);
});

var ImgShower=(function(){
    var _imgShower=null,_curScrollTop=0,_scroller=null,_canZoom=('ontouchstart' in window),_handler=function(e){e.preventDefault();};
    
    function _getShower(){
    	if(_imgShower==null){
    		_imgShower=$(
    			    "<div style='display:none;'><div class='imgctn container'><table style='width:100%;height:100%;'><tr><td id='td_wrapper' style='text-align:center;overflow:auto;'><img style='max-width:100%;' /></td></tr></table></div>"
    			    +"<div style='width:100%;position:fixed;left:0px;bottom:1px;'><div class='container'><button class='btn btn-block btn-success' style='opacity:0.7;' onclick='ImgShower.close();'>&times; 关闭图片</button>"
    			    +"</div></div></div>"
    			    ).appendTo("body");
    	 if(_canZoom)
    		_scroller= new iScroll('td_wrapper', { zoom:true,hScrollbar: false,vScrollbar: false });
    	}
    	return _imgShower;
    }
    
    function _show(url){
    	var o=_getShower();
    	_curScrollTop=$(document).scrollTop();
    	o.siblings().hide();
    	$("body").addClass("modal_body");
    	o.find("div.imgctn").height($(document).height());
    	o.fadeIn().find("img").attr("src",url);
    	if(_canZoom){
    	  _scroller.refresh();
    	  document.addEventListener('touchmove', _handler, false);
    	}
    }
    
    function _close(){
    	_getShower().fadeOut('fast',function(){
    		$("body").removeClass("modal_body");
        	_getShower().siblings().not(".never_show").show(); 
        	if(_canZoom){
        	   document.removeEventListener('touchmove', _handler,false);
        	}
        	window.scrollTo(0, _curScrollTop);
        	_curScrollTop=0;
    	});
    }
    
    return {show:_show,close:_close};
})();

$(function(){
	$("input[placeholder], textarea[placeholder]").placeholder();
	$(".date_day2").datetimepicker({
		language:"zh-CN",
		minView:2,
		maxView:2,
		startView:2,
		autoclose:true,
		format:"yyyy-m-d"
	}).attr("readonly","readonly");
	$(".date_day").datetimepicker({
		language:"zh-CN",
		minView:2,
		maxView:2,
		startView:2,
		autoclose:true,
		format:"yyyy-mm-dd"
	}).attr("readonly","readonly");
	$(".date_month").datetimepicker({
		language:"zh-CN",
		minView:3,
		maxView:3,
		startView:3,
		autoclose:true,
		format:"mm/yyyy"
	}).attr("readonly","readonly");
	$(".date_year").datetimepicker({
		language:"zh-CN",
		minView:4,
		maxView:4,
		startView:4,
		autoclose:true,
		format:"yyyy"
	}).attr("readonly","readonly");
	$("form.need_validate").validate();
	$(".form_reset_btn").bind("click",function(e){
		var o = $(e.target);
		if(o.data("target")){
			var tform=$(o.data("target"));
			tform.find("textarea").val("");
			tform.find("input[type='text'],input[type='hidden'],input[type='password']").val("");
			tform.find("select").val("");
			tform.find(":checkbox:checked").removeAttr("checked");
		}
	});
	$(".form_submit_btn").bind("click",function(e){
		var o = $(e.target);
		if(o.data("target")){
			$(o.data("target")).submit();
		}
	});
});