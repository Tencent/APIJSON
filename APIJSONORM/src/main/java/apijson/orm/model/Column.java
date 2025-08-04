/*Copyright (C) 2020 Tencent.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm.model;

import apijson.MethodAccess;

/**字段(列名)属性
 * @author Lemon
 */
@MethodAccess(POST = {}, PUT = {}, DELETE = {})
public class Column {
	public static final String TAG = "Column";
	public static final String TABLE_NAME = "columns";

}
