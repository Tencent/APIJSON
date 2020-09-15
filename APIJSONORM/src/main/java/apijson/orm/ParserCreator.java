package apijson.orm;

import apijson.NotNull;

/**SQL相关创建器
 * @author Lemon
 */
public interface ParserCreator<T> {
	
	@NotNull
	Parser<T> createParser();
	
	@NotNull
	FunctionParser createFunctionParser();
}
