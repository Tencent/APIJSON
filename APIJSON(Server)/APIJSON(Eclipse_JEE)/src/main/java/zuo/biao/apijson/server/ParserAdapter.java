package zuo.biao.apijson.server;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.server.sql.QueryConfig;

public interface ParserAdapter {

	Object getTarget(@NotNull String path);
	
	JSONObject executeSQL(@NotNull String path, @NotNull QueryConfig config) throws Exception;
	
	JSON parseChild(@NotNull String path, @NotNull String key, @NotNull JSON value) throws Exception;
}
