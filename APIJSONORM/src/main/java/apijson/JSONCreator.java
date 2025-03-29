/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson;

import apijson.orm.SQLConfig;
import apijson.orm.SQLExecutor;

import java.util.List;
import java.util.Map;

/**SQL相关创建器
 * @author Lemon
 */
public interface JSONCreator<M extends Map<String, Object>, L extends List<Object>> {
	
	@NotNull
    M createJSONObject();

//    @NotNull
//    M createJSONObject(String json);

    @NotNull
    default M createJSONObject(Map<String, Object> m) {
        M obj = createJSONObject();
        obj.putAll(m);
        return obj;
    }

	@NotNull
    L createJSONArray();
//    @NotNull
//    L createJSONArray(String json);

    @NotNull
    default L createJSONArray(List<Object> l){
        L arr = createJSONArray();
        arr.addAll(l);
        return arr;
    }
}
