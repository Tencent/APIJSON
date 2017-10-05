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

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**对应方法的请求结构。
 * 仅测试和基本的校验用，实际用Request表里的配置
 * @author Lemon
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface MethodStructure {

	/**@see {@link RequestMethod#HEADS}
	 * @return 该请求方法允许的结构
	 */
	String HEADS() default "";
	
	/**@see {@link RequestMethod#GETS}
	 * @return 该请求方法允许的结构
	 */
	String GETS() default "";

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
