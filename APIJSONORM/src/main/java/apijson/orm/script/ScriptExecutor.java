package apijson.orm.script;

import com.alibaba.fastjson.JSONObject;

import apijson.orm.AbstractFunctionParser;

public interface ScriptExecutor {

    ScriptExecutor init();

    void load(String name, String script);

    Object execute(AbstractFunctionParser parser, JSONObject currentObject, String methodName, Object[] args) throws Exception;

    void cleanCache();
    
}
