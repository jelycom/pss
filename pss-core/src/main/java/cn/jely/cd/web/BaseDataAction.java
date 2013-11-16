package cn.jely.cd.web;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.domain.BaseData;
import cn.jely.cd.service.IBaseDataService;
import cn.jely.cd.service.IBaseDataTypeService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.Pager;

import com.opensymphony.xwork2.interceptor.annotations.InputConfig;



public class BaseDataAction extends JQGridAction<BaseData> {
	private static final long serialVersionUID = 1L;
	private BaseData baseData;
	private IBaseDataService baseDataService;
	private IBaseDataTypeService baseDataTypeService;
	private Pager<BaseData> pager;
	

	public void setBaseDataService(IBaseDataService baseDataService) {
		this.baseDataService = baseDataService;
	}

	public void setBaseDataTypeService(IBaseDataTypeService baseDataTypeService) {
		this.baseDataTypeService = baseDataTypeService;
	}

	public Pager<BaseData> getPager() {
		return pager;
	}


	@Override
	public BaseData getModel() {
		return baseData;
	}

	@SuppressWarnings("unchecked")
	@Override
	public String list() {
		return NONE;
	}

//	@Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="数据名称必须输入")})
	@InputConfig(methodName="list")
	@Override
	public String save() {
//		因为是必须选择数据的类型，所以不用，并且在页面也没有-1的选项
//		if (baseData.getType()!=null&&baseData.getType().getId()==-1l) {
//			baseData.setType(null);
//		}
		if (StringUtils.isNotBlank(id)) {
			baseDataService.update(baseData);
		}else{
			baseDataService.save(baseData);
		}
		actionJsonResult=new ActionJsonResult(baseData);
		return JSONALL;
	}

	@Override
	public String delete()  {
		//id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			baseDataService.delete(id);
		}
		actionJsonResult=new ActionJsonResult(true,null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isNotBlank(id)) {
			baseData=baseDataService.getById(id);
			if (isEditSave()) {
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
				baseData.setDataType(null);
			}
		}else{
			baseData =new BaseData();
		}
	}



	@SuppressWarnings("unchecked")
	@Override
	public String listjson() {
		pager=baseDataService.findPager(objectQuery);
		actionJsonResult=new ActionJsonResult(pager);
		return JSONLIST;
	}
 
	@Override
	public String listall() {
		actionJsonResult=new ActionJsonResult(baseDataService.getAll());
		return JSONALL;
	}

}
