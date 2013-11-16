/*
 * 捷利商业进销存管理系统
 * @(#)OtherTest.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import cn.jely.cd.domain.BaseData;
import cn.jely.cd.domain.BusinessUnits;
import cn.jely.cd.domain.Contacts;
import cn.jely.cd.domain.Department;
import cn.jely.cd.domain.Employee;
import cn.jely.cd.domain.FundAccount;
import cn.jely.cd.domain.Product;
import cn.jely.cd.domain.ProductCommonDetail;
import cn.jely.cd.domain.ProductPurchaseMaster;
import cn.jely.cd.domain.Warehouse;
import cn.jely.cd.pagemodel.PageModel;
import cn.jely.cd.pagemodel.ProductCommonDetailPM;
import cn.jely.cd.pagemodel.ProductCommonMasterPM;
import cn.jely.cd.sys.domain.SystemSetting;
import cn.jely.cd.util.PinYinUtils;
import cn.jely.cd.util.math.SystemCalUtil;


/**
 * @ClassName:OtherTest
 * @author 周义礼
 * @version 2013-2-6 上午11:02:46
 * 
 */
public class OtherTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	@Test
	public void testLine(){
		 String property = System.getProperty("line.separator");
		 System.out.println(property);
		 try {
			String path = this.getClass().getClassLoader().getResource("").toURI().getPath();
			System.out.println(path);
			System.out.println(this.getClass().getClassLoader().getResource("").getFile());
		} catch (URISyntaxException exception) {
			exception.printStackTrace();
		}
	}
	@Test
	public void testMap(){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("123", "123");
		System.out.println(map);
		for(String key:map.keySet()){
			map.put(key, "234");
		}
		System.out.println(map);
	}
	@Test
	public void testLongMaxMinValue() {
		System.out.println("Long maxValue:" + Long.MAX_VALUE);
		System.out.println("Long minValue:" + Long.MIN_VALUE);
		System.out.println("0-Long minValue:" + (0 - Long.MIN_VALUE));
	}

	@Test
	public void testLong() {
		System.out.println(System.getProperty("java.home"));
//		System.out.println(new Long(null));
		// System.out.println(Long.valueOf(""));
	}
	
	@Test
	public void testLong2Int(){
		System.out.println(new Long(Long.MAX_VALUE).intValue()); //-1
	}
	@Test
	public void testDate(){
		GregorianCalendar date=new GregorianCalendar(2013, 0, 8);
		DateFormat format=DateFormat.getDateInstance(DateFormat.MEDIUM,Locale.CHINA);
		System.out.println(date.getTime());
		date.add(Calendar.MONTH, 12);
		System.out.println(format.format(date.getTime()));
		date.set(Calendar.DAY_OF_MONTH, 1);
		System.out.println(format.format(date.getTime()));
		Calendar calendar=Calendar.getInstance();
		Date billDate=new Date();
		calendar.setTime(billDate);
//		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)+1);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
//		System.out.println(calendar.getTime());
		System.out.println("Month "+(calendar.get(Calendar.MONTH)+1)+"maxDay:"+calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		System.out.println("calendar:"+format.format(calendar.getTime()));
		System.out.println("simpleDateformate:"+new SimpleDateFormat("yyyy-MM-dd").format(billDate));
	}
	
	@Test
	public void testLocale(){
		Locale locale = Locale.getDefault();
		System.out.println(locale.toString());
	}
	@Test
	public void testArray() {
		String sour = "1,2,3,4,5";
		String[] ids = sour.split(",");
		Long[] los;
		// los=Arrays.copyOf(ids, ids.length,String.class);

	}

	@Test
	public void testSet(){
		Set<String> strs= new TreeSet<String>();
		System.out.println(strs.toString());
		strs.add("PP1239123");
		strs.add("PP1231231");
		System.out.println(strs.toString());
		
	}
	@Test
	public void testmath() {
		System.out.println("Math.ceil(-11.45)" + Math.ceil(-11.45));
		System.out.println("Math.floor(-11.45" + Math.floor(-11.45));
		System.out.println("Math.round(-11.45)" + Math.round(-11.45));
		System.out.println("Math.ceil(-11.5)" + Math.ceil(-11.5));
		System.out.println("Math.floor(-11.5" + Math.floor(-11.5));
		System.out.println("Math.round(-11.5)" + Math.round(-11.5));
	}

	@Test
	public void testParten() {
		System.out.println(String.format("%05d", 123));
		Calendar c = Calendar.getInstance();
		String s1 = String.format("Duke's Birthday: %1$tm %1$te,%1$tY", c);
		System.out.println(s1);
		System.out.println(new SimpleDateFormat("MMdd").format(new Date()));
	}

	@Test
	public void testSimpleDateFormat() {
		System.out.println(new SimpleDateFormat("yyMM").format(new Date()));
		logger.debug(Boolean.valueOf(StringUtils.isBlank(null)).toString());
	}

	@Test
	public void testCharacter() {
		String str = "中";
		byte[] bytes = str.getBytes(Charset.defaultCharset());
		logger.debug(str);
		System.out.println(Arrays.toString(bytes) + ":" + bytes.length);
	}

	@Test
	public void testPinYin() {
		logger.debug(PinYinUtils.getPinYinShengMu("外(合)资企业"));
		String string = "曾c";
//		String str = PinYin.getPinYin(string);
//		logger.debug(PinYinUtils.getPinYinShengMu("数据字典"));
//		logger.debug(PinYin.getPinYin(string, ToneType.SHENG_MU));
//		logger.debug(PinYin.getPinYin(string, ToneType.CI_ZU));
//		logger.debug(PinYin.getPinYin(string, ToneType.YU_JU));
//		logger.debug(Arrays.toString(PinYin.zhuYin('曾')));
//		String sbCchange = Utils.SBCchange(string);
//		logger.debug(sbCchange + ":" + str);
//		logger.debug(Boolean.valueOf(Utils.isChinese('c')).toString());
//		logger.debug(Boolean.valueOf(Utils.isChinese('中')).toString());
//		Assert.assertTrue(sbCchange != null);
	}

	@Test
	public void testparam() {
		StringBuffer a = new StringBuffer("I am a ");
		StringBuffer b = a;
		a.append("after append");
		a = b;
		System.out.println("a=" + a);
	}

	@Test
	public void testparam1() {
		StringBuffer sb = new StringBuffer("Hello ");
		System.out.println("Before change， sb = " + sb);
		changeData(sb);
		System.out.println("After changeData(n)， sb = " + sb);

	}

	public static void changeData(StringBuffer strBuf) {
		StringBuffer sb2 = new StringBuffer("Hi ");
		strBuf = sb2;
		sb2.append("World！");
	}

	@Test
	public void testString() {
		String s = null;
		s = s + "word";
		System.out.println("hello " + s);
	}
	
	@Test
	public void testSettoArray(){
		String s="asdfsdf";
		Set<String> sets=new HashSet<String>();
		sets.add(s);
		sets.add("1231232");
		System.out.println(sets.toArray().toString());
	}
	@Test
	public void testChartoBinary(){
		String cd="成";
		try {
			byte[] bytes = cd.getBytes("gbk");
			logger.debug(Arrays.toString(bytes));
			for(byte b:bytes){
				System.out.println(Integer.toBinaryString(b&0xff));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testCollection(){
		List<Object> list=new ArrayList<Object>();
		System.out.println(list instanceof Collection);
		Collection c =new ArrayList();
		System.out.println(c.size());
	}

	@Test
	public void testadd() {
		int b = 5, c = 8;
		int a = ++b + ++c;
		System.out.println(a);
		System.out.println("this.properties".lastIndexOf("."));
	}
	@Test
	public void test99(){
		for(int i=1;i<10;i++){
			for(int j=1;j<=i;j++){
				System.out.print(j+"*"+i+"="+i*j+"	");
			}
			System.out.println();
		}
	}
	@Test
	public void testRandom(){
		Random random=new Random();
		System.out.println(random.nextInt(10));
		System.out.println(random.nextInt(10));
	}
	@Test
	public void testMathContext(){
		BigDecimal amount=new BigDecimal(100000);
		BigDecimal divisor = new BigDecimal("7");
		BigDecimal price=amount.divide(divisor,4,RoundingMode.HALF_UP);
//		price.setScale(4, BigDecimal.ROUND_HALF_UP);
		System.out.println(price);
		NumberFormat format=NumberFormat.getInstance(Locale.CHINA);
		format.setCurrency(Currency.getInstance(Locale.CHINA));
		format.setMaximumFractionDigits(4);
		format.setMinimumFractionDigits(4);
		format.setMinimumIntegerDigits(1);
		String result=format.format(price);
		System.out.println("NumberFormat:"+result);
	}

	@Test
	public void testBigDecimal(){
		Double d=new Double(1.000001/1);
		System.out.println(d);
		Double doub = 1.0/3;
		BigDecimal ret=new BigDecimal(doub.toString());
		System.out.println(ret);
	}
	
	@Test
	public void testBigDecimalAdd(){
		BigDecimal value=BigDecimal.TEN;
		System.out.println(value.add(SystemCalUtil.checkValue(null)));
		System.out.println(value.compareTo(BigDecimal.ZERO));
	}
	
	@Test
	public void testRegx(){
		Pattern p=Pattern.compile("\\S*mployee");
		System.out.println(p.matcher("executEmployee").matches());
	}
	
	@Test
	public void testSystemSetting(){
		SystemSetting setting=SystemSetting.getInstance();
		System.out.println(setting.getCurrentDay());
	}
	@Test
	public void testJsonWriter(){
//		Pattern p1=Pattern.compile("\\S*mployee");
//		System.out.println(p1.matcher("executEmployee").matches());
		
		Pattern p2=Pattern.compile("manager.*");
		Pattern p3=Pattern.compile("manager.roles");
		Collection<Pattern> pts=Arrays.asList((new Pattern[]{p2}));
		Collection<Pattern> excl=Arrays.asList((new Pattern[]{p3}));
		JSONWriter writer=new JSONWriter();
		Department dept=new Department("dept", "dept", "memo");
		dept.setManager(new Employee("zhangsan"));
		try {
			String ret=writer.write(dept, excl, pts, true);
			System.out.println(ret);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDomain2PageModel() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{
		ProductPurchaseMaster master=new ProductPurchaseMaster();
		master.setAmount(BigDecimal.TEN);
		master.setArap(BigDecimal.TEN);
		master.setBillDate(new Date());
		master.setEmployee(new Employee());
		master.setFundAccount(new FundAccount());
		master.setWarehouse(new Warehouse());
		master.setBusinessUnit(new BusinessUnits(2L));
		master.setAmount(new BigDecimal("500.5"));
		master.setPaid(new BigDecimal("200"));
		List<ProductCommonDetail> details=new ArrayList<ProductCommonDetail>();
		ProductCommonDetail detail=new ProductCommonDetail(new Product(),5,new BigDecimal(20));
		details.add(detail);
		master.setDetails(details);
		ProductCommonMasterPM masterpm=(ProductCommonMasterPM) domain2PageModel(master);
		System.out.println(masterpm);
	}
	
	protected PageModel domain2PageModel(ProductPurchaseMaster t) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		ProductCommonMasterPM masterPM=new ProductCommonMasterPM();
//		BeanUtils.copyProperties(masterPM,t);
//		PropertyUtils.copyProperties(masterPM, t);
		org.springframework.beans.BeanUtils.copyProperties(t,masterPM,new String[]{"details"});
		BusinessUnits businessUnit = t.getBusinessUnit();
		Contacts contactor = t.getContactor();
		Warehouse warehouse = t.getWarehouse();
		FundAccount fd=t.getFundAccount();
		Employee employee = t.getEmployee();
		BaseData invoicType = t.getInvoiceType();
		masterPM.setBusinessUnitId(businessUnit.getId());
		masterPM.setBusinessUnitName(businessUnit.getName());
		masterPM.setWarehouseId(warehouse.getId());
		masterPM.setWarehouseName(warehouse.getName());
		masterPM.setEmployeeId(employee.getId());
		masterPM.setEmployeeName(employee.getName());
		if(contactor!=null){
			masterPM.setContactorId(contactor.getId());
			masterPM.setContactorName(contactor.getName());
		}
		if(fd!=null){
			masterPM.setFundAccountId(fd.getId());
			masterPM.setFundAccountName(fd.getName());
		}
		if(invoicType!=null){
			masterPM.setInvoiceTypeId(invoicType.getId());
			masterPM.setInvoiceTypeName(invoicType.getName());
		}
		List<ProductCommonDetail> details = t.getDetails();
		int size = details.size();
		System.out.println(size);
		for(int i=0;i<size;i++){
			ProductCommonDetailPM detailpm=new ProductCommonDetailPM();
			ProductCommonDetail detail=details.get(i);
			org.springframework.beans.BeanUtils.copyProperties(detail, detailpm);
			Product product = detail.getProduct();
			detailpm.setProductId(product.getId());
			detailpm.setFullName(product.getFullName());
			detailpm.setSpecification(product.getSpecification());
			detailpm.setColor(product.getColor());
			detailpm.setUnit(product.getUnit());
			masterPM.getDetails().add(detailpm);
		}
		return masterPM;
	}
	
	@Test
	public void testEncrypt(){
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		try {
			Cipher ci=Cipher.getInstance("DES");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testList(){
		List<String> strs=null;
		for (String string : strs) {
			System.out.println(string);
		}
	}
	
	@Test
	public void testUUID(){
		System.out.println();
	}
}
