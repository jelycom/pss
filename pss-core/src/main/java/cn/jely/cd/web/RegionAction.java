/*
 * 捷利商业进销存管理系统
 * @(#)Region.java
 * Copyright (c) 2002-2012 Jely Corporation
 */
package cn.jely.cd.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.jely.cd.domain.Region;
import cn.jely.cd.service.IBaseTreeService;
import cn.jely.cd.service.IRegionService;
import cn.jely.cd.util.ActionJsonResult;
import cn.jely.cd.util.ArrayConverter;
import cn.jely.cd.util.ConstValue;
import cn.jely.cd.util.ITreeOperateAction;
import cn.jely.cd.util.Pager;

/**
 * @ClassName:RegionAction 对树的动作实现ITreeOperateAction
 * @Description:Action
 * @author
 * @version 2012-11-09 13:48:12 
 *
 */
@Controller("regionAction")
@Scope("prototype")
public class RegionAction extends TreeOperateAction<Region> {
	private Region region;
	private IRegionService regionService;
	private Pager<Region> pager;
	protected List<Region> rows;
	//List list = new ArrayList();
	@Resource(name="regionService")
	public void setRegionService(IRegionService regionService) {
		this.regionService = regionService;
	}

	public List<Region> getRows() {
		return rows;
	}
	
	public Pager<Region> getPager() {
		return pager;
	}
 
	@Override
	public Region getModel() {
		return region;
	}
	
	@Override
	public String list() {
		logger.debug("Region list.....");
		//将需要在列表页面展示的关联对象放入Context；
		//putContext(key,value);
		//pager=regionService.findPager(objectQuery);
		return SUCCESS;
	}
	public String listjson(){
		pager=regionService.findPager(objectQuery);
		actionJsonResult=new ActionJsonResult(pager);
		return JSONALL;
	}
	
	
	public String treejson(){
		rows=regionService.findTreeNodes(null,true);
		int size = rows.size();
		pager=new Pager<Region>(1, size, size,1,rows);
		actionJsonResult=new ActionJsonResult(pager);
		return JSONLIST;
	}
/*	public String treejson(){
		List<Region> regions=regionService.findRegionsWithDepth(null, null);
		TreeNode root=null;
		for(int i=0;i<regions.size();i++){
			Region region=regions.get(i);
			Integer depth=region.getDepth();
			boolean state=region.getLft().equals(region.getRgt()-1);
			TreeNode child=new TreeNode(region.getId().toString(), region.getName(),state==false?TreeNode.STATE_CLOSED:TreeNode.STATE_OPEN);
			if(i==0&&depth==0){
				root=child;
			}
			if(0<depth){
				root=addChild(root,child,depth);
			}
		}
		putContext("linked", new TreeNode[]{root});
		return JSONALL;
	}
	
	*//**
	 * @Title:addChild
	 * @param @param parent
	 * @param @param child
	 * @param @param depth
	 * @param @return
	 * @return TreeNode
	 * @exception 
	 * @throws 
	 *//*
	private TreeNode addChild(TreeNode parent, TreeNode child, Integer depth) {
		TreeNode tmp=parent;
		//找到child需要加入的节点
		for(int i=0;i<depth;i++){
			if(i==depth-1){
				tmp.getChildren().add(child);
			}else{
			int size=tmp.getChildren().size();
			tmp=tmp.getChildren().get(size-1);
			}
		//将child加到所在父节点最后
//		parent.getChildren().add(child);
		}
		return parent;
	}*/
	
	public String listall(){
		actionJsonResult=new ActionJsonResult(regionService.getAll());
		return JSONALL;
	}
	

//	@Validations(requiredStrings={@RequiredStringValidator(fieldName="name",message="名称必须输入")})
//	@InputConfig(methodName="list")
	@Override
	public String save() {
		logger.debug("Region save.....");
		if (StringUtils.isNotBlank(id)) {
				regionService.update(region,Long.valueOf(pid));
		}else{
			try {
				if(StringUtils.isNotBlank(pid)){
					regionService.save(region,Long.valueOf(pid));
				}else{
					regionService.save(region,null);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
				
		}
		actionJsonResult=new ActionJsonResult(region);
		return JSONLIST;
	}

	@Override
	public String delete() {
		//id对应的记录不存在已经在Dao作了处理
		if (StringUtils.isNotBlank(id)) {
			Long [] ids=ArrayConverter.Strings2Longs(id.split(","));
			regionService.deleteCascade(ids);
		}
		actionJsonResult=new ActionJsonResult(true,null);
		return JSONLIST;
	}

	@Override
	protected void beforInputSave() {
		if (StringUtils.isBlank(id)) {
			if (isEditSave()) {
				//新增的时候不需要初始化，保存的时候要有个对象保存值
				region =new Region();
			}
		}else{
			region=regionService.getById(id);
			if (isEditSave()) {
//				region.setParent(null);
				// 如果是修改后的保存，因为prepare到save前会执行一次Set操作（modelDriven），
				// 所以要在保存前将其关联的对象置空
			}
		}
	}

	@Override
	protected IBaseTreeService<Region> getTreeService() {
		return regionService;
	}




}
