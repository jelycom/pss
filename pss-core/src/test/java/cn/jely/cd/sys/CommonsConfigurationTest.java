/*
 * 捷利商业进销存管理系统
 * @(#)CommonsConfigurationTest.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-4-3
 */
package cn.jely.cd.sys;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName:CommonsConfigurationTest
 * @author 周义礼
 * @version 2013-4-3 下午4:18:34
 *
 */
public class CommonsConfigurationTest {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Test
	public void testCreateConfigFile(){
		try {
			PropertiesConfiguration pConfiguration=new PropertiesConfiguration("jdbc.properties");
			String properties=pConfiguration.getString("hibernate.dialect");
			logger.debug(properties);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		} 
	}
	
	@Test
	public void testLocateConfigFile(){
		String filename = "pss-setting.properties";
		URL url=ConfigurationUtils.locate(filename);
		if(url==null){
			url = this.getClass().getClassLoader().getResource("");
			String path = url.getFile();
			String fullPath = path+File.separator+filename;
			File file=new File(fullPath);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println(url.toString());
	}
}
