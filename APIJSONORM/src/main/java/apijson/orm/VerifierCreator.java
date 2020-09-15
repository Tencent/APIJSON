package apijson.orm;

import apijson.NotNull;

/**验证器相关创建器
 * @author Lemon
 */
public interface VerifierCreator<T> {
	
	@NotNull
	Verifier<T> createVerifier();
}
