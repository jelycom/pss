<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>http://stackoverflow.com/q/10850978/315935</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

    <meta http-equiv="X-UA-Compatible" content="IE=edge" />

    <link rel="stylesheet" type="text/css" href="http://ajax.aspnetcdn.com/ajax/jquery.ui/1.8.20/themes/redmond/jquery-ui.css" />
    <link rel="stylesheet" type="text/css" href="http://www.ok-soft-gmbh.com/jqGrid/jquery.jqGrid-4.3.3/css/ui.jqgrid.css" />
    <link rel="stylesheet" type="text/css" href="http://www.ok-soft-gmbh.com/jqGrid/jquery-ui-multiselect/1.12/jquery.multiselect.css" />
    <style type="text/css">
        html, body { font-size: 75%; }
    </style>
    <script type="text/javascript" src="http://ajax.aspnetcdn.com/ajax/jQuery/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="http://ajax.aspnetcdn.com/ajax/jquery.ui/1.8.20/jquery-ui.min.js"></script>
    <script type="text/javascript" src="http://www.ok-soft-gmbh.com/jqGrid/jquery.jqGrid-4.3.3/js/i18n/grid.locale-en.js"></script>
    <script type="text/javascript">
        $.jgrid.no_legacy_api = true;
        $.jgrid.useJSON = true;
    </script>
    <script type="text/javascript" src="http://www.ok-soft-gmbh.com/jqGrid/jquery.jqGrid-4.3.3/js/jquery.jqGrid.min.js"></script>
    <script type="text/javascript" src="http://www.ok-soft-gmbh.com/jqGrid/jquery-ui-multiselect/1.12/jquery.multiselect.js"></script>

    <script type="text/javascript">
    //<![CDATA[
    $(function () {
        'use strict';
        var mydata = [
                { id: "1",  invdate: "2007-10-01", name: "test1",  note: "note1",  amount: "200.00", tax: "10.00", closed: true,  ship_via: "TN", total: "210.00" },
                { id: "2",  invdate: "2007-10-02", name: "test2",  note: "note2",  amount: "300.00", tax: "20.00", closed: false, ship_via: "FE", total: "320.00" },
                { id: "3",  invdate: "2007-09-01", name: "test3",  note: "note3",  amount: "400.00", tax: "30.00", closed: false, ship_via: "FE", total: "430.00" },
                { id: "4",  invdate: "2007-10-04", name: "test4",  note: "note4",  amount: "200.00", tax: "10.00", closed: true,  ship_via: "TN", total: "210.00" },
                { id: "5",  invdate: "2007-10-31", name: "test5",  note: "note5",  amount: "300.00", tax: "20.00", closed: false, ship_via: "FE", total: "320.00" },
                { id: "6",  invdate: "2007-09-06", name: "test6",  note: "note6",  amount: "400.00", tax: "30.00", closed: false, ship_via: "FE", total: "430.00" },
                { id: "7",  invdate: "2007-10-04", name: "test7",  note: "note7",  amount: "200.00", tax: "10.00", closed: true,  ship_via: "TN", total: "210.00" },
                { id: "8",  invdate: "2007-10-03", name: "test8",  note: "note8",  amount: "300.00", tax: "20.00", closed: false, ship_via: "FE", total: "320.00" },
                { id: "9",  invdate: "2007-09-01", name: "test9",  note: "note9",  amount: "400.00", tax: "30.00", closed: false, ship_via: "TN", total: "430.00" },
                { id: "10", invdate: "2007-09-08", name: "test10", note: "note10", amount: "500.00", tax: "30.00", closed: true,  ship_via: "TN", total: "530.00" },
                { id: "11", invdate: "2007-09-08", name: "test11", note: "note11", amount: "500.00", tax: "30.00", closed: false, ship_via: "FE", total: "530.00" },
                { id: "12", invdate: "2007-09-10", name: "test12", note: "note12", amount: "500.00", tax: "30.00", closed: false, ship_via: "FE", total: "530.00" }
            ],
            $grid = $("#list"),
            initDateEdit = function (elem) {
                $(elem).datepicker({
                    dateFormat: 'dd-M-yy',
                    autoSize: true,
                    changeYear: true,
                    changeMonth: true,
                    showButtonPanel: true,
                    showWeek: true
                });
            },
            initDateSearch = function (elem) {
                setTimeout(function () {
                    $(elem).datepicker({
                        dateFormat: 'dd-M-yy',
                        autoSize: true,
                        changeYear: true,
                        changeMonth: true,
                        showWeek: true,
                        showButtonPanel: true
                    });
                }, 100);
            },
            numberTemplate = {formatter: 'number', align: 'right', sorttype: 'number',
                editrules: {number: true, required: true},
                searchoptions: { sopt: ['eq', 'ne', 'lt', 'le', 'gt', 'ge', 'nu', 'nn', 'in', 'ni'] }};
        $grid.jqGrid({
            data: mydata,
            datatype: "local",
                colNames: ['Client', 'Date', 'Amount', 'Tax', 'Total', 'Closed', 'Shipped via', 'Notes'],
                colModel: [
                    { name: 'name', index: 'name', align: 'center', editable: true, width: 65, editrules: {required: true},
                        formoptions:{rowpos: 1, colpos: 1} },
                    { name: 'invdate', index: 'invdate', width: 80, align: 'center', sorttype: 'date',
                        formatter: 'date', formatoptions: { newformat: 'd-M-Y' }, editable: true, datefmt: 'd-M-Y',
                        editoptions: { dataInit: initDateEdit },
                        formoptions:{rowpos: 1, colpos: 2},
                        searchoptions: { sopt: ['eq', 'ne', 'lt', 'le', 'gt', 'ge'], dataInit: initDateSearch } },
                    { name: 'amount', index: 'amount', width: 75, editable: true, template: numberTemplate,
                        formoptions:{rowpos: 2, colpos: 1}},
                    { name: 'tax', index: 'tax', width: 52, editable: true, template: numberTemplate,
                        formoptions:{rowpos: 2, colpos: 2} },
                    { name: 'total', index: 'total', width: 60, editable: true, template: numberTemplate,
                        formoptions:{rowpos: 3, colpos: 1} },
                    {name: 'closed', index: 'closed', width: 70, align: 'center', editable: true, formatter: 'checkbox',
                        edittype: 'checkbox', editoptions: {value: 'Yes:No', defaultValue: 'Yes'},
                        stype: 'select', searchoptions: { sopt: ['eq', 'ne'], value: ':Any;true:Yes;false:No' },
                        formoptions:{rowpos: 3, colpos: 2} },
                    {name: 'ship_via', index: 'ship_via', width: 105, align: 'center', editable: true, formatter: 'select',
                        edittype: 'select', editoptions: {
                            value: 'FE:FedEx;TN:TNT;IN:Intim',
                            defaultValue: 'IN',
                            dataInit: function(elem) { $(elem).css("margin-top", "8px" ); }},
                        stype: 'select', searchoptions: { sopt: ['eq', 'ne'], value: ':Any;FE:FedEx;TN:TNT;IN:IN' },
                        formoptions:{rowpos: 4, colpos: 1} },
                    { name: 'note', index: 'note', width: 60, sortable: false, editable: true, edittype: 'textarea',
                        formoptions:{rowpos: 4, colpos: 2} }
                ],
            pager: '#pager',
            rowNum: 10,
            rowList: [5, 10, 20, 50],
            sortname: 'id',
            sortorder: 'asc',
            viewrecords: true,
            gridview: true,
            height: "100%",
            caption: "Demonstrate the usage of jQuery UI MultiSelect Widget"
        });

        $grid.jqGrid("navGrid", "#pager", {view:true}, {width: 450}, {width: 450},{},{},{});
    });
    //]]>
    </script>
</head>

<body>
<table id="list"><tr><td/></tr></table>
<div id="pager"/>

</body>
</html>