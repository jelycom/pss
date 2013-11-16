package cn.jely.cd.util.exception;
/**
 * 自定义逻辑异常,因为组合关系中从对象不能为空
 * @author 周义礼
 *
 */
public class EmptyException extends RuntimeException {
	private static String message="对象不能为空";

	public EmptyException() {
		super(message);
	}

	public EmptyException(String message) {
		super(message);
	}
	
	
}
