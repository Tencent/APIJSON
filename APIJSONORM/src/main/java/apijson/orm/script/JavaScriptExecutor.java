package apijson.orm.script;

import com.alibaba.fastjson.JSONObject;

import apijson.orm.AbstractFunctionParser;

/**
 * JavaScript脚本语言的执行器实现
 */
public class JavaScriptExecutor extends JSR223ScriptExecutor {

    @Override
    protected String scriptEngineName() {
        return "javascript";
    }

	@Override
	protected Object extendParameter(AbstractFunctionParser parser, JSONObject currentObject, String methodName, Object[] args) {
		return null;
	}

	@Override
	protected boolean isLockScript(String methodName) {
		return false;
	}

}
