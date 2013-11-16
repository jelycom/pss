package cn.jely.cd;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import junit.framework.Assert;

import org.junit.Test;

import cn.jely.cd.dao.IBaseDataDao;
import cn.jely.cd.domain.BaseData;
import cn.jely.cd.domain.BaseDataType;
import cn.jely.cd.service.IBaseDataService;
import cn.jely.cd.service.IBaseDataTypeService;
import cn.jely.cd.util.ConstValue;
import cn.jely.cd.util.Pager;
import cn.jely.cd.util.query.ObjectQuery;
import cn.jely.cd.util.query.ObjectQueryTool;
import cn.jely.cd.util.query.QueryGroup;

public class BaseDataServiceTest extends BaseServiceTest{

	private IBaseDataService baseDataService;
	private IBaseDataTypeService baseDataTypeService;
	private IBaseDataDao baseDataDao;
	
	@Resource(name = "baseDataDao")
	public void setBaseDataDao(IBaseDataDao baseDataDao) {
		this.baseDataDao = baseDataDao;
	}

	@Resource(name = "baseDataService")
	public void setBaseDataService(IBaseDataService baseDataService) {
		this.baseDataService = baseDataService;
	}

	@Resource(name="baseDataTypeService")
	public void setBaseDataTypeService(IBaseDataTypeService baseDataTypeService) {
		this.baseDataTypeService = baseDataTypeService;
	}
	
	@Test
	public void testSave() {
		List<BaseDataType> allTypes = baseDataTypeService.getAll();
		for (int i = 0; i < 30; i++) {
			BaseData baseData = new BaseData("BaseData"+i);
			BaseDataType bdt=(BaseDataType) getRandomObject(allTypes);
			baseData.setDataType(bdt);
			baseDataService.save(baseData);
		}
	}

	@Test
	public void testSaveBillState(){
		BaseDataType billStateType=baseDataTypeService.findBySN(ConstValue.BASE_BILL_STATE);
		BaseData orderBill=new BaseData("进货订单状态", "OrderBillPurchase","string",true,billStateType);
		baseDataService.save(orderBill);
		Assert.assertNotNull(orderBill.getId());
	}

	@Test
	public void testUpdate() {
		BaseData baseData = (BaseData) getRandomObject(baseDataService.getAll());
		String oldstr=baseData.getName();
		baseData.setName("u"+oldstr);
		baseDataService.update(baseData);
		BaseData baseData2 = baseDataService.findQueryObject(new ObjectQuery().addWhere("name = :name", "name",oldstr));
		Assert.assertTrue(baseData2==null);
	}


	@Test
	public void testGetAll() {
		List<BaseData> baseDatas=baseDataService.getAll();
		int oldSize=baseDatas.size();
		BaseData deletedata=(BaseData) getRandomObject(baseDatas);
		baseDataService.delete(deletedata.getId());
		List<BaseData> newbaseDatas=baseDataService.getAll();
		int newSize=newbaseDatas.size();
		Assert.assertTrue(oldSize>newSize);
	}

	@Test
	public void testFindPager() {
		QueryGroup qg=ObjectQueryTool.QueryGroupPaser.parseJqgridJson("{\"groupOp\":\"AND\",\"rules\":[{\"field\":\"dataType.id\",\"op\":\"eq\",\"data\":\"2\"}]}");
		ObjectQuery objectQuery=new ObjectQuery();
		objectQuery.setQueryGroup(qg);
//		objectQuery.setJqGridCondition(true);
		Pager<BaseData> pager=baseDataService.findPager(objectQuery);
		System.out.println("pageNo:"+pager.getPageNo());
		System.out.println("pageSize:"+pager.getPageSize());
		System.out.println("datas:"+pager.getRows());
		System.out.println("totalPages:"+pager.getTotalPages());
		Assert.assertNotNull(pager);
		Assert.assertTrue(pager.getRows().size()>0);
	}
	
	@Test
	public void testFindBySn(){
		BaseData data=(BaseData) getRandomObject(baseDataService.getAll());
		Pager<BaseData> pager=baseDataService.findPager(new ObjectQuery().addWhere("o.dataType.sn = :dataType_sn", "dataType_sn",data.getDataType().getSn()));
		printList(pager.getRows());
	}
	
	@Test
	public void testFindNull(){
		Map<String,Object> valueMap=new HashMap<String, Object>();
		valueMap.put(null, null);
		List<BaseData> nullDatas=baseDataDao.findByNamedParam("from BaseData where dataType is null", valueMap);
//		List<BaseData> nullDatas=baseDataService.findQuery("from BaseData where dataType = ?",null,null);
		printList(nullDatas);
	}
}