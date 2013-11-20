///*
// * 捷利商业进销存管理系统
// * @(#)TestExcelSelect.java
// * Copyright (c) 2002-2012 Jely Corporation
// * @date: 2013-11-13
// */
package cn.jely.cd.export.service;
//
///**
// *
// * @author 秋风 Email:623109799@qq.com
// * @version 2013-11-13 下午4:58:39
// */
//public class TestExcelSelect {
//
//}

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.util.CellRangeAddressList;

public class TestExcelSelect {

	
	 public static void main(String [] args) throws IOException {  
         HSSFWorkbook workbook = new HSSFWorkbook();//excel文件对象  
         HSSFSheet userinfosheet1 = workbook.createSheet("用户信息表-1");//工作表对象
         HSSFSheet userinfosheet2 = workbook.createSheet("用户信息表-2");//工作表对象
         //创建一个隐藏页和隐藏数据集
         TestExcelSelect.creatHideSheet(workbook, "hideselectinfosheet");
         //设置名称数据集
         TestExcelSelect.creatExcelNameList(workbook);
         //创建一行数据
         TestExcelSelect.creatAppRow(userinfosheet1, "许果",1);
         TestExcelSelect.creatAppRow(userinfosheet1, "刘德华",2);
         TestExcelSelect.creatAppRow(userinfosheet1, "刘若英",3);
         TestExcelSelect.creatAppRow(userinfosheet2, "张学友",1);
         TestExcelSelect.creatAppRow(userinfosheet2, "林志玲",2);
         TestExcelSelect.creatAppRow(userinfosheet2, "林熙蕾",3);
         
         //生成输入文件
         FileOutputStream out=new FileOutputStream("success.xls");  
         workbook.write(out);  
         out.close();
     }
	 
	 /**
	  * 名称管理
	  * @param workbook
	  */
	 public static void creatExcelNameList(HSSFWorkbook workbook){
		//名称管理
         Name name;
         name = workbook.createName();
         name.setNameName("provinceInfo");
         name.setRefersToFormula("hideselectinfosheet!$A$1:$E$1");
         name = workbook.createName();
         name.setNameName("浙江");
         name.setRefersToFormula("hideselectinfosheet!$B$2:$K$2");
         name = workbook.createName();
         name.setNameName("山东");
         name.setRefersToFormula("hideselectinfosheet!$B$3:$I$3");
         name = workbook.createName();
         name.setNameName("江西");
         name.setRefersToFormula("hideselectinfosheet!$B$4:$E$4");
         name = workbook.createName();
         name.setNameName("江苏");
         name.setRefersToFormula("hideselectinfosheet!$B$5:$I$5");
         name = workbook.createName();
         name.setNameName("四川");
         name.setRefersToFormula("hideselectinfosheet!$B$6:$K$6");
	 }
	 
	 
	 /**
	  * 创建隐藏页和数据域
	  * @param workbook
	  * @param hideSheetName
	  */
	 public static void creatHideSheet(HSSFWorkbook workbook,String hideSheetName){
		 HSSFSheet hideselectinfosheet = workbook.createSheet(hideSheetName);//隐藏一些信息
         //设置下拉列表的内容  
         String[] provinceList = {"浙江","山东","江西","江苏","四川"};
         String[] zjProvinceList = {"浙江","杭州","宁波","温州","台州","绍兴","金华","湖州","丽水","衢州","舟山"};
         String[] sdProvinceList = {"山东","济南","青岛","烟台","东营","菏泽","淄博","济宁","威海"};
         String[] jxProvinceList = {"江西","南昌","新余","鹰潭","抚州"};
         String[] jsProvinceList = {"江苏","南京","苏州","无锡","常州","南通","泰州","连云港","徐州"};
         String[] scProvinceList = {"四川","成都","绵阳","自贡","泸州","宜宾","攀枝花","广安","达州","广元","遂宁"};
         //在隐藏页设置选择信息
         HSSFRow provinceRow = hideselectinfosheet.createRow(0);
         TestExcelSelect.creatRow(provinceRow, provinceList);
         HSSFRow zjProvinceRow = hideselectinfosheet.createRow(1);
         TestExcelSelect.creatRow(zjProvinceRow, zjProvinceList);
         HSSFRow sdProvinceRow = hideselectinfosheet.createRow(2);
         TestExcelSelect.creatRow(sdProvinceRow, sdProvinceList);
         HSSFRow jxProvinceRow = hideselectinfosheet.createRow(3);
         TestExcelSelect.creatRow(jxProvinceRow, jxProvinceList);
         HSSFRow jsProvinceRow = hideselectinfosheet.createRow(4);
         TestExcelSelect.creatRow(jsProvinceRow, jsProvinceList);
         HSSFRow scProvinceRow = hideselectinfosheet.createRow(5);
         TestExcelSelect.creatRow(scProvinceRow, scProvinceList);
         //设置隐藏页标志
         workbook.setSheetHidden(workbook.getSheetIndex(hideSheetName), true);
	 }
	 
	 /**
	  * 创建一列应用数据
	  * @param userinfosheet1
	  * @param userName
	  */
	 public static void creatAppRow(HSSFSheet userinfosheet1,String userName,int naturalRowIndex){
		//构造一个信息输入表单，用户姓名，出生省份，出生城市
         //要求省份是可以下拉选择的，出生城市根据所选择的省份级联下拉选择
         //在第一行第一个单元格，插入下拉框
         HSSFRow row = userinfosheet1.createRow(naturalRowIndex-1);
         HSSFCell userNameLableCell = row.createCell(0);
         userNameLableCell.setCellValue("用户姓名:");
         HSSFCell userNameCell = row.createCell(1);
         userNameCell.setCellValue(userName);
         HSSFCell provinceLableCell = row.createCell(2);
         provinceLableCell.setCellValue("出生省份:");
         HSSFCell provinceCell = row.createCell(3);
         provinceCell.setCellValue("请选择");
         HSSFCell cityLableCell = row.createCell(4);
         cityLableCell.setCellValue("出生城市:");
         HSSFCell cityCell = row.createCell(5);
         cityCell.setCellValue("请选择");
         
         //得到验证对象  
         DataValidation data_validation_list = TestExcelSelect.getDataValidationByFormula("provinceInfo",naturalRowIndex,4);
         //工作表添加验证数据  
         userinfosheet1.addValidationData(data_validation_list);
         DataValidation data_validation_list2 = TestExcelSelect.getDataValidationByFormula("INDIRECT($D"+naturalRowIndex+")",naturalRowIndex,6);
         //工作表添加验证数据  
         userinfosheet1.addValidationData(data_validation_list2);
	 }
	 
	 /**
	  * 创建一列数据
	  * @param currentRow
	  * @param textList
	  */
	 public static void creatRow(HSSFRow currentRow,String[] textList){
		 if(textList!=null&&textList.length>0){
			 int i = 0;
			 for(String cellValue : textList){
				 HSSFCell userNameLableCell = currentRow.createCell(i++);
		         userNameLableCell.setCellValue(cellValue);
			 }
		 }
	 }
     
	 /**
	  * 对Excel自然行列设置一个数据验证（并出现下拉列表选择格式）
	  * @param selectTextList
	  * @param naturalRowIndex
	  * @param naturalColumnIndex
	  * @return
	  */
     public static DataValidation getDataValidationList(String[] selectTextList,int naturalRowIndex,int naturalColumnIndex){
         //加载下拉列表内容  
    	 DVConstraint constraint = DVConstraint.createExplicitListConstraint(selectTextList);
         //设置数据有效性加载在哪个单元格上。  
         //四个参数分别是：起始行、终止行、起始列、终止列  
         int firstRow = naturalRowIndex-1;
         int lastRow = naturalRowIndex-1;
         int firstCol = naturalColumnIndex-1;
         int lastCol = naturalColumnIndex-1;
         CellRangeAddressList regions=new CellRangeAddressList(firstRow,lastRow,firstCol,lastCol);  
         //数据有效性对象
         DataValidation data_validation_list = new HSSFDataValidation(regions,constraint);  
         return data_validation_list;  
     }
     
     /**
      * 使用已定义的数据源方式设置一个数据验证
      * @param formulaString
      * @param naturalRowIndex
      * @param naturalColumnIndex
      * @return
      */
     public static DataValidation getDataValidationByFormula(String formulaString,int naturalRowIndex,int naturalColumnIndex){
         //加载下拉列表内容  
    	 DVConstraint constraint = DVConstraint.createFormulaListConstraint(formulaString); 
         //设置数据有效性加载在哪个单元格上。  
         //四个参数分别是：起始行、终止行、起始列、终止列  
         int firstRow = naturalRowIndex-1;
         int lastRow = naturalRowIndex-1;
         int firstCol = naturalColumnIndex-1;
         int lastCol = naturalColumnIndex-1;
         CellRangeAddressList regions=new CellRangeAddressList(firstRow,lastRow,firstCol,lastCol);  
         //数据有效性对象 
         DataValidation data_validation_list = new HSSFDataValidation(regions,constraint);
         return data_validation_list;  
     }
}

