/*
 * 捷利商业进销存管理系统
 * @(#)Bursary.java
 * Copyright (c) 2002-2013 Jely Corporation
 */
package cn.jely.cd.web;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.domain.Bursary;
import cn.jely.cd.service.IBursaryService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.Pager;

/**
 * @ClassName:BursaryAction
 * @Description:Action
 * @author
 * @version 2013-08-05 10:50:45 
 *
 */
public class BursaryAction extends JQGridAction<Bursary> {//有针对树的操作需继承自TreeOperateAction
	private Bursary bursary;
	private IBursaryService bursaryService;
	private Pager<Bursary> pager;

	public void setBursaryService(IBursaryService bursaryService) {
		this.bursaryService = bursaryService;
	}

	public Pager<Bursary> getPager() {
		return pager;
	}

	@Override
	public Bursary getModel() {
		return bursary;
	}

	@Override
	public String list() {
		//将需要在列表页面展示的关联对象放入Context；
		//putContext(key,value);
		return SUCCESS;
	}
	
	@Override
	public String listjson() {
		pager=bursaryService.findPager(objectQuery);
		actionJsonResult=new ActionJsonResult(pager);
		return JSONALL;
	}
	
	@Override
	public String listall() {
		actionJsonResult=new ActionJsonResult(bursaryService.getAll());
		return JSONALL;
	}
	public String treejson() throws Exception {
		List<Bursary> rows = bursaryService.findTreeNodes(null,true);
		int size = rows.size();
		pager = new Pager<Bursary>(1, size, size, 1, rows);
		actionJsonResult = new ActionJsonResult(pager);
		return JSONLIST;
	}
	
	public String listotherincome(){//其它收入
		actionJsonResult=new ActionJsonResult(bursaryService.findOtherInCome());
		return JSONALL;
	}

	public String listotheroutcome(){//其它支出
		actionJsonResult=new ActionJsonResult(bursaryService.findOtherOutCome());
		return JSONALL;
	}
	public String listallaccount(){//现金银行
		actionJsonResult=new ActionJsonResult(bursaryService.findAccount());
		return JSONALL;
	}
	
//	@Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
//	@InputConfig(methodName="list")
	@Override
	public String save() {
		try{
			if (StringUtils.isNotBlank(id)) {
				bursaryService.update(bursary,Long.valueOf(pid));
			}else{
				bursaryService.save(bursary,Long.valueOf(pid));
			}
			actionJsonResult=new ActionJsonResult(bursary);
		}catch(Exception e){
			actionJsonResult=new ActionJsonResult(e.getMessage());
		}
		return JSONLIST;
	}

	@Override
	public String delete() {
		//id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			bursaryService.delete(id);
		}
		actionJsonResult=new ActionJsonResult(true,null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
			if (isEditSave()) {
				//新增的时候不需要初始化，保存的时候要有个对象保存值
				bursary =new Bursary();
			}
		}else{
			bursary=bursaryService.getById(id);
			if (isEditSave()) {
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
			}
		}
	}

}
