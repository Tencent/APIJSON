package apijson.framework;

import apijson.server.FunctionParser;
import apijson.server.Parser;
import apijson.server.ParserCreator;
import apijson.server.SQLConfig;
import apijson.server.SQLCreator;
import apijson.server.SQLExecutor;
import apijson.server.Verifier;
import apijson.server.VerifierCreator;

/**APIJSON相关创建器
 * @author Lemon
 */
public class APIJSONCreator implements ParserCreator<Long>, VerifierCreator<Long>, SQLCreator {

	@Override
	public Parser<Long> createParser() {
		return new APIJSONParser();
	}

	@Override
	public FunctionParser createFunctionParser() {
		return new APIJSONFunctionParser();
	}

	@Override
	public Verifier<Long> createVerifier() {
		return new APIJSONVerifier();
	}
	
	@Override
	public SQLConfig createSQLConfig() {
		return new APIJSONSQLConfig();
	}

	@Override
	public SQLExecutor createSQLExecutor() {
		return new APIJSONSQLExecutor();
	}

}
