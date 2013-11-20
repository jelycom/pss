/*
 * 捷利商业进销存管理系统
 * @(#)BaseActionTest.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-10
 */
package cn.jely.cd.web;

import org.apache.struts2.StrutsSpringTestCase;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName:BaseActionTest
 * Description:
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-10 下午5:36:17
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager="txManager",defaultRollback=false)//指定事务管理及默认的回滚策略
@Transactional
public class BaseActionTest extends StrutsSpringTestCase {
	
}
