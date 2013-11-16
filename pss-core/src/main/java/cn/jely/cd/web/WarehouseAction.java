package cn.jely.cd.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.tools.Diagnostic;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.jely.cd.domain.Employee;
import cn.jely.cd.domain.Region;
import cn.jely.cd.domain.Warehouse;
import cn.jely.cd.service.IRegionService;
import cn.jely.cd.service.IWarehouseService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.ConstValue;
import cn.jely.cd.util.Pager;

import com.opensymphony.xwork2.interceptor.annotations.InputConfig;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

@Controller("warehouseAction")
@Scope("prototype")
public class WarehouseAction extends JQGridAction<Warehouse> {
	private static final long serialVersionUID = 1L;
	private Warehouse warehouse;
	private IWarehouseService warehouseService;
	private IRegionService regionService;
	private Pager<Warehouse> pager;
	protected List<Warehouse> rows;
	
	@Resource(name="warehouseService")
	public void setWarehouseService(IWarehouseService warehouseService) {
		this.warehouseService = warehouseService;
	}
	
	@Resource(name="regionService")
	public void setRegionService(IRegionService regionService) {
		this.regionService = regionService;
	}
	
	public Pager<Warehouse> getPager() {
		return pager;
	}

	@Override
	public Warehouse getModel() {
		return warehouse;
	}

	@Override
	public String list() {
		logger.debug("Warehouse list.....");
		//将需要在列表页面展示的关联对象放入Context；
		//putContext(key,value);
		//pager=warehouseService.findPager(objectQuery);
		return SUCCESS;
	}
	@SuppressWarnings("unchecked")
	public String listjson(){
		pager=warehouseService.findPager(objectQuery);
		actionJsonResult=new ActionJsonResult(pager);
		return JSONLIST;
	}

	public String listall(){
		actionJsonResult=new ActionJsonResult(warehouseService.getAll());
		return JSONALL;
	}
	

//	@Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
	@InputConfig(methodName="list")
	@Override
	public String save() {
		logger.debug("Warehouse save.....");
		if (StringUtils.isNotBlank(id)) {
			warehouseService.update(warehouse);
		}else{
			warehouseService.save(warehouse);
		}
		actionJsonResult=new ActionJsonResult(warehouse);
		return JSONLIST;
	}

	@Override
	public String delete() {
		logger.debug("Warehouse delete.....");
		//id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			warehouseService.delete(id);
		}
		actionJsonResult=new ActionJsonResult(true, null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
			if (isEditSave()) {
				//新增的时候不需要初始化，保存的时候要有个对象保存值
				warehouse =new Warehouse();
			}
		}else{
				warehouse=warehouseService.getById(id);
			if (isEditSave()) {
				warehouse.setRegion(null);
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
			}
		}
	}
}
