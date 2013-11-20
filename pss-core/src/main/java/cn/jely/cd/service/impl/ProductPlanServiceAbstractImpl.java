package cn.jely.cd.service.impl;

import java.util.List;

import cn.jely.cd.dao.IProductPlanDao;
import cn.jely.cd.domain.ProductPlanDetail;
import cn.jely.cd.domain.ProductPlanMaster;
import cn.jely.cd.service.IProductPlanService;
import cn.jely.cd.util.exception.AttrConflictException;
import cn.jely.cd.util.exception.EmptyException;
import cn.jely.cd.util.state.StateManager;

/**
 * 应该在保存及更新时检查它的状态,根据状态来确定是否更新相关联的属性(如:orderbill是NEW属性还是audited才参与业务运算)
 * @author 秋风 Email:623109799@qq.com
 * @version 2013-7-13 下午11:57:01
 *
 * @param <T>
 */
public abstract class ProductPlanServiceAbstractImpl<T extends ProductPlanMaster> extends BillStateServiceImpl<T> implements IProductPlanService<T>{

	protected abstract IProductPlanDao<T> getProductPlanDao();
	
//	@Override
//	protected Boolean beforeSaveCheck(T master) {
//		if(master!=null){
//			prepareModel(master);
//			return super.beforeSaveCheck(master);
//		}else{
//			return false;
//		}
//	}
//	
//	@Override
//	protected Boolean beforeUpdateCheck(T master) {
//		if (master != null) {
//			T oldMaster=getById(master.getId());
//			State newstate = master.getState();
//			if (StateManager.canEditContent(master)) {
//				// 必须保证从表中的数据多于1条
//				prepareModel(master);
//			} else	if(StateManager.canChangeTo(oldMaster, newstate)){
//				master=oldMaster;
//				master.setState(newstate);
//			}
//			return true;
//		}
//		return false;
//	}
//	
//	@Override
//	protected void afterSave(T t) {
//		doWithState(t);
//	}
//
//
//	@Override
//	protected void afterUpdate(T t) {
//		doWithState(t);
//	}

	@Override
	protected void prepareModel(T master) {
		checkDetails(master);
		if(master.getState()==null){
			master.setState(StateManager.getDefaultState());
		}
		if(master.getPlanEmployee()==null||master.getPlanEmployee().getId()==null){
			throw new EmptyException("制单人");
		}
		if(master.getExecuteEmployee()==null||master.getExecuteEmployee().getId()==null){
			throw new EmptyException("执行人");
		}
		if(master.getStartDate().compareTo(master.getEndDate())>0){
			throw new RuntimeException("开始时间不能大于结束时间");
		}
		if (((IProductPlanDao<T>)getBaseDao()).checkExist(master)) {
			throw new AttrConflictException("编号字段");
		}
	}


	private void checkDetails(T master) {
		List<ProductPlanDetail> details = master.getDetails();
		if(details!=null){
			for(int i=0;i<details.size();i++){
				ProductPlanDetail detail1=details.get(i);
				if(detail1.getQuantity()==null||detail1.getQuantity()<1){
					details.remove(i);
				}
			}
			if (details.size() > 0) {
				int order = 0;
				for (ProductPlanDetail detail : details) {
					detail.setOrders(order++);
					detail.setProductPlanMaster(master);//维护主从关系
				}
			}else {
				throw new EmptyException("无有效的明细.");
			}
		}else{
			throw new EmptyException();
		}
	}

	@Override
	public List<T> findAllUnFinished() {
		return getProductPlanDao().findAllUnFinished(null);
	}

	@Override
	public List<T> findAllFinished() {
		return getProductPlanDao().findAllFinished(null);
	}
	
	@Override
	public Long countAllFinished(){
		return getProductPlanDao().countAllFinished();
	}
	
}
