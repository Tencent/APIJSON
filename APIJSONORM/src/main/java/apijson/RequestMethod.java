/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson;

/**请求方法，对应org.springframework.web.bind.annotation.RequestMethod，多出GETS,HEADS方法
 * @author Lemon
 */
public enum RequestMethod {

	/**
	 * 常规获取数据方式
	 */
	GET,
	
	/**
	 * 检查，默认是非空检查，返回数据总数
	 */
	HEAD,
	
	/**Safe, Single, Simple
	 * <br > 限制性GET，通过POST来GET数据，不显示请求内容和返回结果，并且校验请求，一般用于对安全要求比较高的请求
	 */
	GETS,
	
	/**Safe, Single, Simple
	 * <br > 限制性HEAD，通过POST来HEAD数据，不显示请求内容和返回结果，并且校验请求，一般用于对安全要求比较高的请求
	 */
	HEADS,
	
	/**
	 * 新增(或者说插入)数据
	 */
	POST,
	
	/**
	 * 修改数据，只修改传入字段对应的值
	 */
	PUT,
	
	/**
	 * json包含多条语句,支持增删改查,函数调用
	 */
	CRUD,
	
	/**
	 * 删除数据
	 */
	DELETE;
	
	public static final RequestMethod[] ALL = new RequestMethod[]{ GET, HEAD, GETS, HEADS, POST, PUT, CRUD, DELETE};
	
	/**是否为GET请求方法
	 * @param method
	 * @param containPrivate 包含私密(非明文)获取方法GETS
	 * @return
	 */
	public static boolean isGetMethod(RequestMethod method, boolean containPrivate) {
		return method == null || method == GET || (containPrivate && method == GETS);
	}
	
	/**是否为HEAD请求方法
	 * @param method
	 * @param containPrivate 包含私密(非明文)获取方法HEADS
	 * @return
	 */
	public static boolean isHeadMethod(RequestMethod method, boolean containPrivate) {
		return method == HEAD || (containPrivate && method == HEADS);
	}
	
	/**是否为查询的请求方法
	 * @param method
	 * @return 读操作(GET型或HEAD型) - true, 写操作(POST,PUT,DELETE) - false
	 */
	public static boolean isQueryMethod(RequestMethod method) {
		return isGetMethod(method, true) || isHeadMethod(method, true);
	}
	
	/**是否为开放(不限制请求的结构或内容；明文，浏览器能直接访问及查看)的请求方法
	 * @param method
	 * @return
	 */
	public static boolean isPublicMethod(RequestMethod method) {
		/**
		 * modify by boot
		 * 修改所有方法都需要进行request验证
		 */
		return method == null /*|| method == GET || method == HEAD*/;
	}

	public static String getName(RequestMethod method) {
		return method == null ? GET.name() : method.name();
	}
	
}
