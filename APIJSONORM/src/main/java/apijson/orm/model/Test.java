/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm.model;

import apijson.MethodAccess;

/**条件测试。最早可能 4.5.0 移除。AbstractSQLConfig 已支持 SELECT 2>1 这种简单条件表达式，
 * 相当于是 SELECT 后只有 WHERE 条件表达式，其它全都没有，这样就可以去掉仅用来动态执行校验逻辑 Test 表了。
 * @author Lemon
 */
@Deprecated
@MethodAccess(POST = {}, PUT = {}, DELETE = {})
public class Test {
}
