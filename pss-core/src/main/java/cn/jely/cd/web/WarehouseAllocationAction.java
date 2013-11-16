package cn.jely.cd.web;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.jely.cd.domain.WarehouseAllocation;
import cn.jely.cd.service.IWarehouseAllocationService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.Pager;

import com.opensymphony.xwork2.interceptor.annotations.InputConfig;

@Controller("warehouseAllocationAction")
@Scope("prototype")
public class WarehouseAllocationAction extends JQGridAction<WarehouseAllocation> {
	private static final long serialVersionUID = 1L;
	private WarehouseAllocation warehouseAllocation;
	private IWarehouseAllocationService warehouseAllocationService;
	private Pager<WarehouseAllocation> pager;
	private List<WarehouseAllocation> rows;
	
	public List<WarehouseAllocation> getRows() {
		return rows;
	}
	
	@Resource(name="warehouseAllocationService")
	public void setWarehouseAllocationService(IWarehouseAllocationService warehouseAllocationService) {
		this.warehouseAllocationService = warehouseAllocationService;
	}

	public Pager<WarehouseAllocation> getPager() {
		return pager;
	}


	
	@Override
	public WarehouseAllocation getModel() {
		return warehouseAllocation;
	}

	@Override
	public String list() {
		logger.debug("WarehouseAllocation list.....");
		//将需要在列表页面展示的关联对象放入Context；
		//putContext(key,value);
		//pager=warehouseAllocationService.findPager(objectQuery);
		return SUCCESS;
	}
	@SuppressWarnings("unchecked")
	public String listjson(){
		pager=warehouseAllocationService.findPager(objectQuery);
		actionJsonResult=new ActionJsonResult(pager);
		return JSONLIST;
	}
	
	public String showjson(){
		rows = warehouseAllocationService.findChild(id);
		return JSONTREE;
	}
	
	public String listall(){
		actionJsonResult=new ActionJsonResult(warehouseAllocationService.getAll());
		return JSONALL;
	}
	

//	@Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="必须输入")})
	@InputConfig(methodName="list")
	@Override
	public String save() {
		logger.debug("WarehouseAllocation save.....");
		if (StringUtils.isNotBlank(id)) {
				warehouseAllocationService.update(warehouseAllocation);
		}else{
				warehouseAllocationService.save(warehouseAllocation);
		}
		actionJsonResult=new ActionJsonResult(warehouseAllocation);
		return JSONLIST;
	}

	@Override
	public String delete() {
		logger.debug("WarehouseAllocation delete.....");
		//id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			warehouseAllocationService.delete(id);
		}
		actionJsonResult=new ActionJsonResult(true, null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
			if (isEditSave()) {
				//新增的时候不需要初始化，保存的时候要有个对象保存值
				warehouseAllocation =new WarehouseAllocation();
			}
		}else{
			warehouseAllocation=warehouseAllocationService.getById(id);
			if (isEditSave()) {
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
				warehouseAllocation.setWarehouse(null);
			}
		}
	}

}
