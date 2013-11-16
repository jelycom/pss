package cn.jely.cd;

import net.sf.json.JSONObject;

import org.junit.Test;

import cn.jely.cd.util.JqGridTool;
import cn.jely.cd.util.query.ObjectQuery;

public class TestJSON {

	@Test
	public void testParse() {
		try {
			StringBuilder builder = new StringBuilder(
					"{\"groupOp\":\"OR\",\"rules\":[{\"field\":\"id\",\"op\":\"eq\",\"data\":\"3\"},{\"field\":\"id\",\"op\":\"eq\",\"data\":\"4\"}]}");
			System.out.println(builder.toString());
			JSONObject jo;
			jo = (JSONObject) JSONObject.fromObject(builder.toString());
			System.out.println(jo.getString("groupOp"));
			System.out.println(jo.containsKey("kk"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testParseSingle(){
		ObjectQuery objectQuery=JqGridTool.parseSingle("name", "eq", "张三");
		System.out.println(objectQuery.getWhere()+":"+objectQuery.getParamValueMap());
	}

	@Test
	public void testParseJqgridJson() {
//		JSONObject jsonObj = JSONObject
//				.fromObject("{\"groupOp\":\"OR\",\"rules\":[{\"field\":\"id\",\"op\":\"eq\",\"data\":\"3\"},{\"field\":\"id\",\"op\":\"eq\",\"data\":\"4\"}]}");
		String jsonObj="{\"groupOp\":\"AND\",\"rules\":[{\"field\":\"id\",\"op\":\"eq\",\"data\":\"3\"}]}";
		ObjectQuery objectQuery = JqGridTool.parseJqgridJson(jsonObj);
		System.out.println(objectQuery.getWhere());
		System.out.println(objectQuery.getParamValueMap());
	}

	@Test
	public void testParseJqgridJsonGroup() {
		JSONObject jsonObj = JSONObject
				.fromObject("{\"groupOp\":\"AND\",\"rules\":[],\"groups\":[{\"groupOp\":\"OR\",\"rules\":[{\"field\":\"a.id\",\"op\":\"eq\",\"data\":\"3\"},{\"field\":\"a.id\",\"op\":\"eq\",\"data\":\"4\"},{\"field\":\"a.id\",\"op\":\"eq\",\"data\":\"5\"}],\"groups\":[]},{\"groupOp\":\"AND\",\"rules\":[{\"field\":\"b.name\",\"op\":\"cn\",\"data\":\"Cli\"}],\"groups\":[]}]}");
		ObjectQuery objectQuery = JqGridTool.parseJqgridJson(jsonObj);
		System.out.println(objectQuery.getWhere());
		System.out.println(objectQuery.getParamValueMap());
		System.out.println(objectQuery.getParamValueMap());
	}
	

}
