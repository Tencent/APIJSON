/*Copyright ©2016 TommyLemon(https://github.com/TommyLemon/APIJSON)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package zuo.biao.apijson;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**请求方法对应的JSON结构
 * GET,HEAD不指定结构，可以用任意结构请求
 * @author Lemon
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface APIJSONRequest {

	/**
	 * @return 允许的请求方法
	 */
	RequestMethod[] method() default {};
	

	/**@see {@link RequestMethod#POST_HEAD}
	 * @return 该请求方法允许的结构
	 */
	String POST_HEAD() default "";
	
	/**@see {@link RequestMethod#POST_GET}
	 * @return 该请求方法允许的结构
	 */
	String POST_GET() default "";

	/**@see {@link RequestMethod#POST}
	 * @return 该请求方法允许的结构
	 */
	String POST() default "";

	/**@see {@link RequestMethod#PUT}
	 * @return 该请求方法允许的结构
	 */
	String PUT() default "";
	
	/**@see {@link RequestMethod#DELETE}
	 * @return 该请求方法允许的结构
	 */
	String DELETE() default "";
}
