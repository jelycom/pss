package cn.jely.cd;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

//@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager="txManager",defaultRollback=false)//指定事务管理及默认的回滚策略
@Transactional//开启测试事务
//@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,OpenSessionInViewListener.class})
public abstract class BaseServiceTest extends AbstractTransactionalTestNGSpringContextTests{

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected void printList(List<? extends Object> values){
		for(Object o:values){
			logger.debug(o.toString());
			System.out.println(o);
		}
	}
	
	protected List getRandomSubList(List list){
		int size = list.size();
		if(list!=null&&size>0){
			Random random=new Random();
			int from=random.nextInt(size/2);
			int to=random.nextInt(size);
			if(from>to){
				return list.subList(to, from);
			}else{
				return list.subList(from, to);
			}
		}
		return null;
	}
	
	protected Object getRandomObject(List<?> list){
		if(list==null||list.size()<1){
			return null;
		}
		int size=list.size();
		return list.get(new Random().nextInt(size));
	}
}
