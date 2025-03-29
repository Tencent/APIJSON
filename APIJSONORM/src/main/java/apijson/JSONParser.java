/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson;

import java.util.List;
import java.util.Map;

/**SQL相关创建器
 * @author Lemon
 */
public interface JSONParser<M extends Map<String, Object>, L extends List<Object>> extends JSONCreator<M, L> {

    M parseJSON(Object json);

    M parseObject(Object json);

    <T> T parseObject(Object json, Class<T> clazz);

    L parseArray(Object json);

    <T> List<T> parseArray(Object json, Class<T> clazz);

    String toJSONString(Object obj);
}
