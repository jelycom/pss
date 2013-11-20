/*
 * 捷利商业进销存管理系统
 * @(#)ConfigProjectTest.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-6-20
 */
package cn.jely.cd.sys;

import java.math.RoundingMode;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.configuration.DefaultConfigurationBuilder;
import org.apache.commons.configuration.FileSystem;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.struts2.json.JSONException;
import org.apache.struts2.json.JSONUtil;
import org.junit.Test;

import cn.jely.cd.util.ProjectConfig;

/**
 * @ClassName:ConfigProjectTest
 * Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-6-20 上午10:43:42
 *
 */
public class ProjectConfigTest {
	
	@Test
	public void testPropertiesConfiguration(){
		DefaultConfigurationBuilder builder=new DefaultConfigurationBuilder();
		try {
			System.out.println(builder.getConfigurationBasePath());
			System.out.println(FileSystem.getDefaultFileSystem().getFileName("pss-settings.properties").toString());
			URL locate = ConfigurationUtils.locate("E:/study/jelyworkspace/jelypss/pss-core/target/test-classes/conf", "pss-settings.properties");
			builder.load(locate);
			Configuration config=builder.getConfiguration();
			config.setProperty("1123", "asdf");
			
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	@Test
	public void testCompositeConfiguration() throws URISyntaxException{
		CompositeConfiguration configuration=new CompositeConfiguration();
		try {
			PropertiesConfiguration propertiesConfiguration=new PropertiesConfiguration("conf/pss-settings.properties");
//			propertiesConfiguration.setFileName("E:/study/jelyworkspace/jelypss/pss-core/target/test-classes/conf/pss-settings.properties");
			propertiesConfiguration.load("E:/study/jelyworkspace/jelypss/pss-core/target/test-classes/conf/pss-settings.properties");
			configuration.addConfiguration(propertiesConfiguration);
//			propertiesConfiguration.setProperty("haha", 1235);
			configuration.setProperty("asdf", "aaaaaaaaa");
			System.out.println(configuration.getInt("haha"));
			System.out.println(this.getClass().getClassLoader().getResource("").toURI().toString());
			System.out.println(this.getClass().getClassLoader().getResource("").toString());
			propertiesConfiguration.save();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testConfigObject() throws JSONException{
		ProjectConfig cp=ProjectConfig.getInstance().init();
		cp.setProperties("aaaa", RoundingMode.HALF_EVEN);
		try {
			cp.save();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		Object property = cp.getReadOnlyConfiguration().getProperty("aaaa");
		System.out.println(property+": instanceof of RoundingMode:"+(property instanceof RoundingMode));
		System.out.println(JSONUtil.serialize(cp.getReadOnlyConfiguration()));
	}
}
