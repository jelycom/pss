package cn.jely.cd.web;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import cn.jely.cd.domain.BaseDataType;
import cn.jely.cd.service.IBaseDataTypeService;
import cn.jely.cd.service.IBaseTreeService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.Pager;


public class BaseDataTypeAction extends TreeOperateAction<BaseDataType> {
	private static final long serialVersionUID = 1L;
	private BaseDataType baseDataType;
	private IBaseDataTypeService baseDataTypeService;
	private Pager<BaseDataType> pager;
	protected List<BaseDataType> rows;
	
	@Resource(name="baseDataTypeService")
	public void setBaseDataTypeService(IBaseDataTypeService baseDataTypeService) {
		this.baseDataTypeService = baseDataTypeService;
	}

	public List<BaseDataType> getRows() {
		return rows;
	}
	
	public Pager<BaseDataType> getPager() {
		return pager;
	}

	@Override
	public BaseDataType getModel() {
		return baseDataType;
	}

	@Override
	public String list() {
//		pager=baseDataTypeService.findPager(objectQuery);
		return SUCCESS;
	}
	
	/* (non-Javadoc)
	 * @see cn.jely.cd.web.CRUDAction#listjson()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String listjson()  {
		pager=baseDataTypeService.findPager(objectQuery);
		actionJsonResult=new ActionJsonResult(pager);
		return JSONALL;
	}

	public String treejson() throws Exception{
		rows=baseDataTypeService.findTreeNodes(null,true);
		int size = rows.size();
		pager=new Pager<BaseDataType>(1, size, size,1,rows);
		actionJsonResult=new ActionJsonResult(pager);
		return JSONLIST;
	}
	
	/* (non-Javadoc)
	 * @see cn.jely.cd.web.CRUDAction#listall()
	 */
	@Override
	public  String listall()  {
		actionJsonResult=new ActionJsonResult(baseDataTypeService.getAll());
		return JSONALL;
	}
	
	
//	@Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
//	@InputConfig(methodName="input")
	@Override
	public String save()  {
		if (StringUtils.isNotBlank(id)) {
			baseDataTypeService.update(baseDataType,Long.valueOf(pid));
		}else{
			if(StringUtils.isNotBlank(pid)){
				baseDataTypeService.save(baseDataType,Long.valueOf(pid));
			}else{
				baseDataTypeService.save(baseDataType,null);
			}
		}
		actionJsonResult=new ActionJsonResult(baseDataType);
		return JSONLIST;
	}

	@Override
	public String delete() {
		//id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			baseDataTypeService.delete(id);
		}
		actionJsonResult=new ActionJsonResult(true,null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
			baseDataType =new BaseDataType();
		}else{
			baseDataType=baseDataTypeService.getById(id);
			if (isEditSave()) {
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
			}
		}
	}
	
	@Override
	protected IBaseTreeService<BaseDataType> getTreeService() {
		return baseDataTypeService;
	}

}
