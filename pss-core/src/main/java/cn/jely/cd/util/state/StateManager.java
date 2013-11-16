/*
 * 捷利商业进销存管理系统
 * @(#)StateManager.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-7-12
 */
package cn.jely.cd.util.state;

/**
 * 单据状态管理类
 * @ClassName:StateManager Description:
 * @author 周义礼 Email:11861744@qq.com
 * @version 2013-7-12 上午11:30:55
 * @param <T>
 * 
 */
public class StateManager {
	public static State[] unCompleteStates=new State[]{State.NEW,State.PROCESS,State.SUSPEND,State.AUDITING,State.AUDITED,State.AUDITFAIL};
	public static State[] completeStates=new State[]{State.COMPLETE,State.DISCARD,State.STOP};
	
	public static State[] getUnCompleteStates() {
		return unCompleteStates;
	}

	public static State[] getCompleteStates() {
		return completeStates;
	}

	public static State getDefaultState(){
		return State.AUDITED;
	}
	/**
	 * 是否允许编辑单据的内容
	 * @param domain
	 * @return boolean
	 */
	public static boolean canEditContent(IStateAble domain) {
		return checkState(domain, new CanEditChecker());
	}

	/**
	 * 判断是否可以提交业务运算.
	 * @param domain
	 * @return boolean
	 */
	public static boolean canPost(IStateAble domain) {
		if(domain!=null){
			if(State.AUDITED.equals(domain.getState())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 *  检查当前状态是否可更改为另一状态
	 * @param domain
	 * @param newState
	 * @return boolean
	 */
	public static boolean canChangeTo(IStateAble domain, State newState) {
		if (domain!=null&&domain.getState() != null) {
			switch (domain.getState()) {
			case NEW:
				if ((State.PROCESS.equals(newState)) || State.DISCARD.equals(newState) || State.COMPLETE.equals(newState)) {
					return true;
				}
				break;
			case PROCESS:
				if ((State.STOP.equals(newState)) || State.COMPLETE.equals(newState)) {
					return true;
				}
				break;
			case SUSPEND:
				if ((State.PROCESS.equals(newState)) || State.STOP.equals(newState)) {// 暂停状态只能是执行和手动停止,不能是完成,因为完成是属于数量完全完成后的,也不能废弃,因为已经处理了.
					return true;
				}
				break;
			case AUDITING:
				if(State.NEW.equals(newState)||State.AUDITFAIL.equals(newState)||State.AUDITED.equals(newState)){
					return true;
				}
			default:
				return false;
			}
		}
		return false;
	}

	public static <T> T checkState(IStateAble domain, IDoWithState<T> checker) {
		return checker.doWithState(domain);
	}

	private static class CanEditChecker implements IDoWithState<Boolean> {
		public Boolean doWithState(IStateAble domain) {
			if(domain!=null&&domain.getState()!=null){
				switch (domain.getState()) {
				case NEW:
					return true;
				case COMPLETE:
					return false;
				case DISCARD:
					return false;
				case STOP:
					return false;
				default:
					return false;
				}
			}
			return false;
		}
	}

}
