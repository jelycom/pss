package cn.jely.cd;

import java.util.List;

import javax.annotation.Resource;

import org.testng.Assert;
import org.testng.annotations.Test;

import cn.jely.cd.domain.BaseDataType;
import cn.jely.cd.service.IBaseDataTypeService;
import cn.jely.cd.util.ConstValue;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;

public class BaseDataTypeServiceTest extends BaseServiceTest{

	private IBaseDataTypeService baseDataTypeService;

	@Resource(name = "baseDataTypeService")
	public void setBaseDataTypeService(IBaseDataTypeService baseDataTypeService) {
		this.baseDataTypeService = baseDataTypeService;
	}

	@Test
	public void testSave() {
		BaseDataType baseDataType = new BaseDataType("数据字典", "systemDict");
		baseDataTypeService.save(baseDataType,null);
		baseDataType = new BaseDataType("供应商级别",ConstValue.BASE_SUPPLIER_LEVEL);
		ObjectQuery objectQuery=new ObjectQuery().addWhere("name=:name", "name", "数据字典");
		List<BaseDataType> result=baseDataTypeService.findAll(objectQuery);
		if(result!=null&&result.size()>0){
			BaseDataType root=result.get(0);
			Long rootId=root.getId();
			baseDataTypeService.save(baseDataType,rootId);
			baseDataType = new BaseDataType("供应商类别",ConstValue.BASE_SUPPLIER_TYPE);
			baseDataTypeService.save(baseDataType,rootId);
			baseDataType = new BaseDataType("客户级别",ConstValue.BASE_CUSTOMER_LEVEL);
			baseDataTypeService.save(baseDataType,rootId);
			baseDataType = new BaseDataType("客户类别",ConstValue.BASE_CUSTOMER_TYPE);
			baseDataTypeService.save(baseDataType,rootId);
			baseDataType = new BaseDataType("产品品牌",ConstValue.BASE_PRODUCT_BRAND);
			baseDataTypeService.save(baseDataType,rootId);
			baseDataType = new BaseDataType("产品单位",ConstValue.BASE_PRODUCT_UNIT);
			baseDataTypeService.save(baseDataType,rootId);
			baseDataType = new BaseDataType("权限分组",ConstValue.BASE_ROLE_GROUP);
			baseDataTypeService.save(baseDataType,rootId);
			baseDataType = new BaseDataType("单据状态",ConstValue.BASE_BILL_STATE);
			baseDataTypeService.save(baseDataType,rootId);
		}
	}

	@Test
	public void testUpdate() {
		BaseDataType baseDataType = (BaseDataType) getRandomObject(baseDataTypeService.getAll());
		Assert.assertNotNull(baseDataType);
		String oldstr=baseDataType.getName();
		baseDataType.setName("u"+oldstr);
		baseDataTypeService.update(baseDataType);
		BaseDataType baseDataType2 = baseDataTypeService.findQueryObject(new ObjectQuery().addWhere("name=:name", "name", oldstr));
		Assert.assertNull(baseDataType2);
	}


	@Test
	public void testFindPager() {
		Pager<BaseDataType> pager=baseDataTypeService.findPager(null);
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}

}