package cn.jely.cd;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import org.apache.commons.beanutils.ConvertUtils;
import org.junit.Test;

import cn.jely.cd.domain.Employee;

public class EntityClassTest {

	@Test
	public void testEntity(){
		try {
			 BeanInfo beanInfo=Introspector.getBeanInfo(Employee.class, Object.class);
			 PropertyDescriptor[] properties=beanInfo.getPropertyDescriptors();
			 for (int i = 0; i < properties.length; i++) {
				System.out.println(properties[i].getName()+properties[i].getPropertyType().getName());
				System.out.println("----------------");
				System.out.println(ConvertUtils.convert("1", properties[i].getPropertyType()).getClass());
				if (properties[i].getPropertyType().equals(String.class)) {
					System.out.println("这是个String类型的!");
				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
	}
}
