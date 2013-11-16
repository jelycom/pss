package cn.jely.cd.util;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationUtils;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ProjectConfig {
	private static final String DEFAULTCONFIGLOCATION = "conf";
	private static final String DEFAULTCONFIGPROPERTIES = "pss-settings.properties";
	private static final String DEFAULTCONFIGXML = "pss-settings.xml";
	private final Logger LOGGER = LoggerFactory.getLogger(ProjectConfig.class);
	private static ProjectConfig instance = new ProjectConfig();
	private CompositeConfiguration config = new CompositeConfiguration();
	private PropertiesConfiguration propertiesConfiguration;
	private XMLConfiguration xmlConfiguration;
	private boolean isInited = false;

	private ProjectConfig() {
	}
	
	
	public static ProjectConfig getInstance() {
		instance.init();
		return instance;
	}

	public static ProjectConfig getInstance(String confDir,String...fileNames){
		instance.init(confDir, fileNames);
		return instance;
	}
	public Configuration getReadOnlyConfiguration() {
		return config;
	}

	public ProjectConfig init() {
		return init(DEFAULTCONFIGLOCATION, DEFAULTCONFIGPROPERTIES, DEFAULTCONFIGXML);
	}

	public ProjectConfig init(String confDir, String... fileNames) {
		if (isInited) {
			return instance;
		}
		for (String filename : fileNames) {
			String ext = filename.substring(filename.lastIndexOf(".") + 1);
			if ("properties".equalsIgnoreCase(ext)) {
				this.propertiesConfiguration = loadPropertiesConfiguration(confDir, filename);
			} else if ("xml".equalsIgnoreCase(ext)) {
				this.xmlConfiguration = loadXMLConfiguration(confDir, filename);
			}
		}
		// 加载顺序是从前到后,一个属性在先加载的配置中查找,没有则在后面的配置中查找.
		if (propertiesConfiguration != null) {
			instance.config.addConfiguration(propertiesConfiguration);
			isInited = true;
		}
		if (xmlConfiguration != null) {
			instance.config.addConfiguration(xmlConfiguration);
			isInited = true;
		}
		return instance;
	}

	/**
	 * 获取properties配置文件属性
	 */
	public PropertiesConfiguration loadPropertiesConfiguration(String confDir, String fileName) {
		PropertiesConfiguration fileConfiguration = new PropertiesConfiguration();
		try {
			fileConfiguration.setReloadingStrategy(getFileChangedReloadingStrategy());
			// 加载文件前设置分隔符失效(不使用任何分隔符).
			fileConfiguration.setDelimiterParsingDisabled(true);
			// 加载文件前设置分割符,默认为逗号(,)如果配置项中包含分隔符就会被分割.
			// fileConfiguration.setListDelimiter('_');
			// 如果用户没有指定绝对路径:加载文件顺序为current directory,user home
			// directory,classpath.
			fileConfiguration.setURL(getConfigURL(confDir, fileName));
			fileConfiguration.setThrowExceptionOnMissing(false);
		} catch (Exception e) {
			LOGGER.error("failed to load properties config:" + fileName, e);
		}
		return fileConfiguration;
	}

	/**
	 * 获取XML配置文件属性
	 */
	public XMLConfiguration loadXMLConfiguration(String confDir, String fileName) {
		URL url=getConfigURL(confDir,fileName);
		if(url != null){
			XMLConfiguration xmlConfig = new XMLConfiguration();
			try {
				xmlConfig.setEncoding("utf-8");
				xmlConfig.setReloadingStrategy(getFileChangedReloadingStrategy());
				xmlConfig.setDelimiterParsingDisabled(true);
				xmlConfig.setAttributeSplittingDisabled(true);
				xmlConfig.setURL(url);
//				xmlConfiguration.load();
			} catch (Exception e) {
				LOGGER.error("failed to load xml config:" + fileName, e);
			}
			return xmlConfig;
		}else{
			return null;
		}
	}

	/**
	 * 通过properties文件设置system属性.
	 */
	public void setSystemConfigurationByProp(String fileName) throws Exception {
		SystemConfiguration systemConfiguration = new SystemConfiguration();
		systemConfiguration.setSystemProperties(fileName);
	}

	private URL getConfigURL(String confDir, String fileName) {
		File file = new File(confDir + File.separator + fileName);
		URL url = null;
		if (file.exists()) {
			try {
				url = file.toURI().toURL();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} else {
			/**
			 * ConfigurationUtils.locate: Return the location of the specified
			 * resource by searching the user home directory, the current
			 * classpath and the system classpath.
			 */
			url = ConfigurationUtils.locate(confDir, fileName);
			if (url == null) {
				url = this.getClass().getClassLoader().getResource("");
				String fullPath = null;
				try {
					String path  = url.toURI().getPath();
					if (StringUtils.isNotBlank(confDir)) {
						path = path + File.separator + confDir;
					}
					fullPath = path + File.separator + fileName;
				} catch (URISyntaxException exception) {
					exception.printStackTrace();
				}
//				String path = url.getFile();
				File newFile = new File(fullPath);
				try {
					if (!newFile.exists()) {
						if (!newFile.getParentFile().exists()) {
							newFile.getParentFile().mkdirs();
						}
						newFile.createNewFile();
					}
					url = newFile.toURI().toURL();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			LOGGER.debug("config file url:" + url);
		}
		return url;
	}

	private FileChangedReloadingStrategy getFileChangedReloadingStrategy() {
		FileChangedReloadingStrategy reloadingStrategy = new FileChangedReloadingStrategy();
		reloadingStrategy.setRefreshDelay(10000);// 10s
		return reloadingStrategy;
	}

	public void save() throws ConfigurationException {
		propertiesConfiguration.save(propertiesConfiguration.getURL());
	}
	public void savexml() throws ConfigurationException {
		xmlConfiguration.save(xmlConfiguration.getURL());
	}

	public void setProperties(String key, Object value) {
		propertiesConfiguration.setProperty(key, value);
	}

	public boolean containsKey(String key) {

		return config.containsKey(key);
	}

	public BigDecimal getBigDecimal(String key) {
		return config.getBigDecimal(key);
	}

	public BigDecimal getBigDecimal(String key, BigDecimal defaultValue) {
		return config.getBigDecimal(key, defaultValue);
	}

	public BigInteger getBigInteger(String key) {
		return config.getBigInteger(key);
	}

	public BigInteger getBigInteger(String key, BigInteger defaultValue) {
		return config.getBigInteger(key, defaultValue);
	}

	public boolean getBoolean(String key) {
		return config.getBoolean(key);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return config.getBoolean(key, defaultValue);
	}

	public Boolean getBoolean(String key, Boolean defaultValue) {
		return config.getBoolean(key, defaultValue);
	}

	public byte getByte(String key) {
		return config.getByte(key);
	}

	public byte getByte(String key, byte defaultValue) {
		return config.getByte(key, defaultValue);
	}

	public Byte getByte(String key, Byte defaultValue) {
		return config.getByte(key, defaultValue);
	}

	public double getDouble(String key) {
		return config.getDouble(key);
	}

	public double getDouble(String key, double defaultValue) {
		return config.getDouble(key, defaultValue);
	}

	public Double getDouble(String key, Double defaultValue) {
		return config.getDouble(key, defaultValue);
	}

	public float getFloat(String key) {
		return config.getFloat(key);
	}

	public float getFloat(String key, float defaultValue) {
		return config.getFloat(key, defaultValue);
	}

	public Float getFloat(String key, Float defaultValue) {
		return config.getFloat(key, defaultValue);
	}

	public int getInt(String key) {
		return config.getInt(key);
	}

	public int getInt(String key, int defaultValue) {
		return config.getInt(key, defaultValue);
	}

	public Integer getInteger(String key, Integer defaultValue) {
		return config.getInteger(key, defaultValue);
	}

	public Iterator getKeys(String prefix) {
		return config.getKeys(prefix);
	}

	public Iterator getKeys() {
		return config.getKeys();
	}

	public List getList(String key) {
		return config.getList(key);
	}

	public List getList(String key, List defaultValue) {
		return config.getList(key, defaultValue);
	}

	public long getLong(String key) {
		return config.getLong(key);
	}

	public long getLong(String key, long defaultValue) {
		return config.getLong(key, defaultValue);
	}

	public Long getLong(String key, Long defaultValue) {
		return config.getLong(key, defaultValue);
	}

	public Properties getProperties(String key) {
		return config.getProperties(key);
	}

	public Object getProperty(String key) {
		return config.getProperty(key);
	}

	public short getShort(String key) {
		return config.getShort(key);
	}

	public short getShort(String key, short defaultValue) {
		return config.getShort(key, defaultValue);
	}

	public Short getShort(String key, Short defaultValue) {
		return config.getShort(key, defaultValue);
	}

	public String getString(String key) {
		return config.getString(key);
	}

	public String getString(String key, String defaultValue) {
		return config.getString(key, defaultValue);
	}

	public String[] getStringArray(String key) {
		return config.getStringArray(key);
	}
	
	public boolean isEmpty() {

		return config.isEmpty();
	}

	
	public XMLConfiguration getXmlConfiguration() {
		return xmlConfiguration;
	}


	public static void main(String[] args) {
		System.out.println("Start");
		ProjectConfig project = ProjectConfig.getInstance("conf","default.properties");
		project.init().setProperties("asdf", "asdfasdf");
		project.setProperties("hehaheha", 1238127398);
		Configuration configuration = project.getReadOnlyConfiguration();
		// configuration.setProperty("pricescale", 2);
		configuration.setProperty("haha", 21);
		try {
			project.savexml();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}
}