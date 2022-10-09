/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm.model;

import apijson.MethodAccess;

/**SQL Server 表属性
 * @author Lemon
 */
@MethodAccess(POST = {}, PUT = {}, DELETE = {})
public class AllTable {
	public static final String TAG = "AllTable";
	public static final String TABLE_NAME = "ALL_TABLES";

}
