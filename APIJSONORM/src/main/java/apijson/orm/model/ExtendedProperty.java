/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm.model;

import apijson.MethodAccess;

/**扩展属性，SQL Server 转用
 * @author Lemon
 */
@MethodAccess(POST = {}, PUT = {}, DELETE = {})
public class ExtendedProperty {
	public static final String TAG = "ExtendedProperty";
	public static final String TABLE_NAME = "extended_properties";

}
