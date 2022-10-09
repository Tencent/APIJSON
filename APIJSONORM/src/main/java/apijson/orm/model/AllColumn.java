/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm.model;

import apijson.MethodAccess;

/**SQL Server 在 sys 下的字段(列名)
 * @author Lemon
 */
@MethodAccess(POST = {}, PUT = {}, DELETE = {})
public class AllColumn {
	public static final String TAG = "AllColumn";
	public static final String TABLE_NAME = "ALL_TAB_COLUMNS";

}
