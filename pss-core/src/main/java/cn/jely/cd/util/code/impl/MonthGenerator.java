package cn.jely.cd.util.code.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cn.jely.cd.util.code.DateCoder;

/**
 * @ClassName:MonthGenerator
 * @Description:按月计算编号
 * @author 周义礼
 * @version 2013-3-28 下午4:29:18
 * 
 */
public class MonthGenerator extends AbstractCodeGenerator {


	@Override
	protected boolean checkDate(Date date, Date oldDate) {
		Calendar oldtime = new GregorianCalendar();
		oldtime.setTime(oldDate);
		Calendar newtime = new GregorianCalendar();
		newtime.setTime(date);
		if (oldtime.get(Calendar.YEAR) == newtime.get(Calendar.YEAR)
				&& oldtime.get(Calendar.MONTH) == newtime.get(Calendar.MONTH)) {
			return false;
		}
		return true;
	}

}
