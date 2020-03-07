package apijson.framework;

import apijson.orm.FunctionParser;
import apijson.orm.Parser;
import apijson.orm.ParserCreator;
import apijson.orm.SQLConfig;
import apijson.orm.SQLCreator;
import apijson.orm.SQLExecutor;
import apijson.orm.Verifier;
import apijson.orm.VerifierCreator;

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
