/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson;

/**来访的用户角色
 * @author Lemon
 */
public enum RequestRole {

	/**未登录，不明身份的用户
	 */
	UNKNOWN,

	/**已登录的用户
	 */
	LOGIN,

	/**联系人，必须已登录
	 */
	CONTACT,

	/**圈子成员(CONTACT + OWNER)，必须已登录
	 */
	CIRCLE,

	/**拥有者，必须已登录
	 */
	OWNER,

	/**管理员，必须已登录
	 */
	ADMIN;

	//似乎不管怎么做，外部引用后都是空值。并且如果在注解内的位置不是最前的，还会导致被注解的类在其它类中import报错。
	//虽然直接打印显示正常，但被@MethodAccess内RequestRole[] GET()等方法引用后获取的是空值
	//	public static final RequestRole[] ALL = {RequestRole.UNKNOWN};//values();//所有
	//	public static final RequestRole[] HIGHS;//高级
	//	static {
	//		HIGHS = new RequestRole[] {OWNER, ADMIN};
	//	}

	public static final String[] NAMES = {
			UNKNOWN.name(), LOGIN.name(), CONTACT.name(), CIRCLE.name(), OWNER.name(), ADMIN.name()
	};

	public static RequestRole get(String name) throws Exception {
		if (name == null) {
			return null;
		}
		try { //Enum.valueOf只要找不到对应的值就会抛异常
			return RequestRole.valueOf(name);
		} catch (Exception e) {
			throw new IllegalArgumentException("角色 " + name + " 不存在！只能是[" + StringUtil.getString(NAMES) + "]中的一种！", e);
		}
	}

}
