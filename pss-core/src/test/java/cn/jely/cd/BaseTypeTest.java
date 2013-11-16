package cn.jely.cd;

import org.testng.Assert;
import org.testng.annotations.Test;


public class BaseTypeTest extends BaseServiceTest{
//	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Test
	public void testString(){
		String field="department.manager.name";
		String pos=".";
		System.out.println(field.indexOf(pos));
		System.out.println(field.substring(0,field.indexOf(pos)));
		System.out.println(field.substring(field.indexOf(pos)+1));
		System.out.println(getlastString(field, pos));
	}
	@Test
	public void testMethod(){
		logger.debug("testMethod");
		Assert.assertTrue(true);
	}
	public String getlastString(String origal,String str){
		int pos = origal.indexOf(str);
		if (pos>0) {
		//	String tmp=origal.substring(0,pos);
			origal=origal.substring(pos+1);
			origal=getlastString(origal,str);
		}
		return origal;
	}
}
