<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <title>How to implement local form editing</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="../css/themes/le-frog/jquery-ui-1.8.23.custom.css"
	rel="stylesheet" type="text/css" />
<link href="../css/ui.jqgrid.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="../jquery/jquery1.7.2.js"></script>
<script type="text/javascript"
	src="../jquery/jquery-ui-1.8.23.custom.min.js"></script>
<script type="text/javascript" src="../js/i18n/grid.locale-cn.js"></script>
	<script type="text/javascript" src="../js/jquery.jqGrid.min.js"></script>
<!--     <link rel="stylesheet" type="text/css" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/themes/redmond/jquery-ui.css" />
    <link rel="stylesheet" type="text/css" href="http://www.ok-soft-gmbh.com/jqGrid/jquery.jqGrid-3.8.2/css/ui.jqgrid.css" />
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.5.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.8.10/jquery-ui.min.js"></script>
    <script type="text/javascript" src="http://www.ok-soft-gmbh.com/jqGrid/jquery.jqGrid-3.8.2/src/i18n/grid.locale-en.js"></script>
    <script type="text/javascript" src="http://www.ok-soft-gmbh.com/jqGrid/jquery.jqGrid-3.8.2/src/grid.base.js"></script>
    <script type="text/javascript" src="http://www.ok-soft-gmbh.com/jqGrid/jquery.jqGrid-3.8.2/src/grid.common.js"></script>
    <script type="text/javascript" src="http://www.ok-soft-gmbh.com/jqGrid/jquery.jqGrid-3.8.2/src/grid.formedit.js"></script>
    <script type="text/javascript" src="http://www.ok-soft-gmbh.com/jqGrid/jquery.jqGrid-3.8.2/src/grid.inlinedit.js"></script>
    <script type="text/javascript" src="http://www.ok-soft-gmbh.com/jqGrid/jquery.jqGrid-3.8.2/src/grid.celledit.js"></script>
    <script type="text/javascript" src="http://www.ok-soft-gmbh.com/jqGrid/jquery.jqGrid-3.8.2/src/grid.subgrid.js"></script>
    <script type="text/javascript" src="http://www.ok-soft-gmbh.com/jqGrid/jquery.jqGrid-3.8.2/src/grid.treegrid.js"></script>
    <script type="text/javascript" src="http://www.ok-soft-gmbh.com/jqGrid/jquery.jqGrid-3.8.2/src/grid.grouping.js"></script>
    <script type="text/javascript" src="http://www.ok-soft-gmbh.com/jqGrid/jquery.jqGrid-3.8.2/src/grid.custom.js"></script>
    <script type="text/javascript" src="http://www.ok-soft-gmbh.com/jqGrid/jquery.jqGrid-3.8.2/src/jquery.fmatter.js"></script>
    <script type="text/javascript" src="http://www.ok-soft-gmbh.com/jqGrid/jquery.jqGrid-3.8.2/src/jquery.searchFilter.js"></script>
    <script type="text/javascript" src="http://www.ok-soft-gmbh.com/jqGrid/jquery.jqGrid-3.8.2/src/grid.jqueryui.js"></script>
    <script type="text/javascript" src="http://www.ok-soft-gmbh.com/jqGrid/jquery.jqGrid-3.8.2/src/jqDnR.js"></script>
    <script type="text/javascript" src="http://www.ok-soft-gmbh.com/jqGrid/jquery.jqGrid-3.8.2/src/jqModal.js"></script>
    <script type="text/javascript" src="http://www.ok-soft-gmbh.com/jqGrid/jquery.jqGrid-3.8.2/js/i18n/grid.locale-en.js"></script>
    <script type="text/javascript" src="http://www.ok-soft-gmbh.com/jqGrid/jquery.jqGrid-3.8.2/js/jquery.jqGrid.min.js"></script> -->

    <script type="text/javascript">
    //<![CDATA[
        $(document).ready(function () {
            var lastSel, mydata = [
                    {id:"1", invdate:"2007-10-01",name:"test",  note:"note",  amount:"200.00",tax:"10.00",closed:true, ship_via:"TN",total:"210.00"},
                    {id:"2", invdate:"2007-10-02",name:"test2", note:"note2", amount:"300.00",tax:"20.00",closed:false,ship_via:"FE",total:"320.00"},
                    {id:"3", invdate:"2007-09-01",name:"test3", note:"note3", amount:"400.00",tax:"30.00",closed:false,ship_via:"FE",total:"430.00"},
                    {id:"4", invdate:"2007-10-04",name:"test4", note:"note4", amount:"200.00",tax:"10.00",closed:true ,ship_via:"TN",total:"210.00"},
                    {id:"5", invdate:"2007-10-31",name:"test5", note:"note5", amount:"300.00",tax:"20.00",closed:false,ship_via:"FE",total:"320.00"},
                    {id:"6", invdate:"2007-09-06",name:"test6", note:"note6", amount:"400.00",tax:"30.00",closed:false,ship_via:"FE",total:"430.00"},
                    {id:"7", invdate:"2007-10-04",name:"test7", note:"note7", amount:"200.00",tax:"10.00",closed:true ,ship_via:"TN",total:"210.00"},
                    {id:"8", invdate:"2007-10-03",name:"test8", note:"note8", amount:"300.00",tax:"20.00",closed:false,ship_via:"FE",total:"320.00"},
                    {id:"9", invdate:"2007-09-01",name:"test9", note:"note9", amount:"400.00",tax:"30.00",closed:false,ship_via:"TN",total:"430.00"},
                    {id:"10",invdate:"2007-09-08",name:"test10",note:"note10",amount:"500.00",tax:"30.00",closed:true ,ship_via:"TN",total:"530.00"},
                    {id:"11",invdate:"2007-09-08",name:"test11",note:"note11",amount:"500.00",tax:"30.00",closed:false,ship_via:"FE",total:"530.00"},
                    {id:"12",invdate:"2007-09-10",name:"test12",note:"note12",amount:"500.00",tax:"30.00",closed:false,ship_via:"FE",total:"530.00"}
                ],
                grid = $("#list"),
                onclickSubmitLocal = function(options,postdata) {
                    var grid_p = grid[0].p,
                        idname = grid_p.prmNames.id,
                        grid_id = grid[0].id,
                        id_in_postdata = grid_id+"_id",
                        rowid = postdata[id_in_postdata],
                        addMode = rowid === "_empty",
                        oldValueOfSortColumn;

                    // postdata has row id property with another name. we fix it:
                    if (addMode) {
                        // generate new id
                        var new_id = grid_p.records + 1;
                        while ($("#"+new_id).length !== 0) {
                            new_id++;
                        }
                        postdata[idname] = String(new_id);
                    } else if (typeof(postdata[idname]) === "undefined") {
                        // set id property only if the property not exist
                        postdata[idname] = rowid;
                    }
                    delete postdata[id_in_postdata];

                    // prepare postdata for tree grid
                    if(grid_p.treeGrid === true) {
                        if(addMode) {
                            var tr_par_id = grid_p.treeGridModel === 'adjacency' ? grid_p.treeReader.parent_id_field : 'parent_id';
                            postdata[tr_par_id] = grid_p.selrow;
                        }

                        $.each(grid_p.treeReader, function (i){
                            if(postdata.hasOwnProperty(this)) {
                                delete postdata[this];
                            }
                        });
                    }

                    // decode data if there encoded with autoencode
                    if(grid_p.autoencode) {
                        $.each(postdata,function(n,v){
                            postdata[n] = $.jgrid.htmlDecode(v); // TODO: some columns could be skipped
                        });
                    }

                    // save old value from the sorted column
                    oldValueOfSortColumn = grid_p.sortname === "" ? undefined: grid.jqGrid('getCell',rowid,grid_p.sortname);

                    // save the data in the grid
                    if (grid_p.treeGrid === true) {
                        if (addMode) {
                            grid.jqGrid("addChildNode",rowid,grid_p.selrow,postdata);
                        } else {
                            grid.jqGrid("setTreeRow",rowid,postdata);
                        }
                    } else {
                        if (addMode) {
                            grid.jqGrid("addRowData",rowid,postdata,options.addedrow);
                        } else {
                            grid.jqGrid("setRowData",rowid,postdata);
                        }
                    }

                    if ((addMode && options.closeAfterAdd) || (!addMode && options.closeAfterEdit)) {
                        // close the edit/add dialog
                        $.jgrid.hideModal("#editmod"+grid_id,
                                          {gb:"#gbox_"+grid_id,jqm:options.jqModal,onClose:options.onClose});
                    }

                    if (postdata[grid_p.sortname] !== oldValueOfSortColumn) {
                        // if the data are changed in the column by which are currently sorted
                        // we need resort the grid
                        setTimeout(function() {
                            grid.trigger("reloadGrid", [{current:true}]);
                        },100);
                    }

                    // !!! the most important step: skip ajax request to the server
                    this.processing = true;
                    return {};
                },
                editSettings = {
                    //recreateForm:true,
                    jqModal:false,
                    reloadAfterSubmit:false,
                    closeOnEscape:true,
                    savekey: [true,13],
                    closeAfterEdit:true,
                    onclickSubmit:onclickSubmitLocal
                },
                addSettings = {
                    //recreateForm:true,
                    jqModal:false,
                    reloadAfterSubmit:false,
                    savekey: [true,13],
                    closeOnEscape:true,
                    closeAfterAdd:true,
                    onclickSubmit:onclickSubmitLocal
                },
                delSettings = {
                    // because I use "local" data I don't want to send the changes to the server
                    // so I use "processing:true" setting and delete the row manually in onclickSubmit
                    onclickSubmit: function(options, rowid) {
                        var grid_id = $.jgrid.jqID(grid[0].id),
                            grid_p = grid[0].p,
                            newPage = grid[0].p.page;

                        // delete the row
                        grid.delRowData(rowid);
                        $.jgrid.hideModal("#delmod"+grid_id,
                                          {gb:"#gbox_"+grid_id,jqm:options.jqModal,onClose:options.onClose});

                        if (grid_p.lastpage > 1) {// on the multipage grid reload the grid
                            if (grid_p.reccount === 0 && newPage === grid_p.lastpage) {
                                // if after deliting there are no rows on the current page
                                // which is the last page of the grid
                                newPage--; // go to the previous page
                            }
                            // reload grid to make the row from the next page visable.
                            grid.trigger("reloadGrid", [{page:newPage}]);
                        }

                        return true;
                    },
                    processing:true
                },
                initDateEdit = function(elem) {
                    setTimeout(function() {
                        $(elem).datepicker({
                            dateFormat: 'yy-mm-dd',
                            autoSize: true,
                            showOn: 'button', // it dosn't work in searching dialog
                            changeYear: true,
                            changeMonth: true,
                            showButtonPanel: true,
                            showWeek: true
                        });
                        //$(elem).focus();
                 },100);
                },
                initDateSearch = function(elem) {
                    setTimeout(function() {
                        $(elem).datepicker({
                            dateFormat: 'yy-mm-dd',
                            autoSize: true,
                            showOn: 'button', // it dosn't work in searching dialog
                            changeYear: true,
                            changeMonth: true,
                            showButtonPanel: true,
                            showWeek: true
                        });
                        //$(elem).focus();
                 },100);
                };

            grid.jqGrid({
                datatype:'local',
                data: mydata,
                colNames:['Inv No','Date','Client','Amount','Tax','Total','Closed','Shipped via','Notes'],
                colModel:[
                    {name:'id',index:'id',width:70,align:'center',sorttype: 'int',searchoptions:{sopt:['eq','ne']}},
                    {name:'invdate',index:'invdate',width:80, align:'center', sorttype:'date',
                     formatter:'date', formatoptions: {newformat:'d-M-Y'}, editable:true, datefmt: 'd-M-Y',
                     editoptions: {dataInit:initDateEdit},
                     searchoptions: {dataInit:initDateSearch}},
                    {name:'name',index:'name',editable: true, width:70, editrules:{required:true}},
                    {name:'amount',index:'amount',width:100, formatter:'number', editable: true, align:'right'},
                    {name:'tax',index:'tax',width:70, formatter:'number', editable: true, align:'right'},
                    {name:'total',index:'total',width:120, formatter:'number', editable: true, align:'right'},
                    {name:'closed',index:'closed',width:110,align:'center',editable: true, formatter: 'checkbox',
                     edittype:'checkbox',editoptions:{value:'Yes:No',defaultValue:'Yes'},
                     stype: 'select', searchoptions: { sopt:['eq','ne'], value:':All;true:Yes;false:No' }},
                    {name:'ship_via',index:'ship_via',width:120,align:'center',editable: true, formatter:'select',
                     edittype:'select',editoptions:{value:'FE:FedEx;TN:TNT;IN:Intim', defaultValue:'Intime'},
                     stype:'select', searchoptions:{value:':All;FE:FedEx;TN:TNT;IN:Intim'}},
                    {name:'note',index:'note',width:100,sortable:false,editable:true,edittype:'textarea'}
                ],
                rowNum:10,
                rowList:[5,10,20],
                pager: '#pager',
                gridview:true,
                rownumbers:true,
                autoencode:true,
                ignoreCase:true,
                sortname: 'invdate',
                viewrecords: true,
                sortorder: 'desc',
                caption:'How to implement local form editing',
                height: '100%',
                editurl: 'clientArray',
                ondblClickRow: function(rowid, ri, ci) {
                    var p = grid[0].p;
                    if (p.selrow !== rowid) {
                        // prevent the row from be unselected on double-click
                        // the implementation is for "multiselect:false" which we use,
                        // but one can easy modify the code for "multiselect:true"
                        grid.jqGrid('setSelection', rowid);
                    }
                    grid.jqGrid('editGridRow', rowid, editSettings);
                },
                onSelectRow: function(id) {
                    if (id && id !== lastSel) {
                        // cancel editing of the previous selected row if it was in editing state.
                        // jqGrid hold intern savedRow array inside of jqGrid object,
                        // so it is safe to call restoreRow method with any id parameter
                        // if jqGrid not in editing state
                        if (typeof lastSel !== "undefined") {
                            grid.jqGrid('restoreRow',lastSel);
                        }
                        lastSel = id;
                    }
                }
            }).jqGrid('navGrid','#pager',{},editSettings,addSettings,delSettings,
                      {multipleSearch:true,overlay:false,
                       onClose:function(form){
                           // if we close the search dialog during the datapicker are opened
                           // the datepicker will stay opened. To fix this we have to hide
                           // the div used by datepicker
                           $("div#ui-datepicker-div.ui-datepicker").hide();
                       }});
        });
    //]]>
    </script>
</head>
<body>
    <table id="list"><tr><td/></tr></table>
    <div id="pager"/>
</body>
</html>