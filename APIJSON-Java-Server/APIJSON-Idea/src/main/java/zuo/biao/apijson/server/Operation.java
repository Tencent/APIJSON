package zuo.biao.apijson.server;

import zuo.biao.apijson.StringUtil;

/**对请求JSON的操作
 * @author Lemon
 */
public enum Operation {

	/**
	 * 不允许传的字段
	 */
	DISALLOW,
	/**
	 * 必须传的字段
	 */
	NECESSARY,
	
	/**
	 * 验证是否符合预设的条件
	 */
	VERIFY,
	/**
	 * 验证是否不存在，除了本身的记录
	 */
	UNIQUE,
	
	/**
	 * 添加，当要被添加的对象不存在时
	 */
	ADD,
	/**
	 * 强行放入，不存在时就添加，存在时就修改
	 */
	PUT,
	/**
	 * 替换，当要被替换的对象存在时
	 */
	REPLACE,
	/**
	 * 移除，当要被移除的对象存在时
	 */
	REMOVE;
	
	public static Operation get(String name) {
		try {//Enum.valueOf只要找不到对应的值就会抛异常
			return Operation.valueOf(StringUtil.toUpperCase(name));
		} catch (Exception e) {
			//empty
		}
		return null;
	}
	
}
