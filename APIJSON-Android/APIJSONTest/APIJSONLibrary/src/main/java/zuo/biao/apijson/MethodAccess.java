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
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static zuo.biao.apijson.RequestRole.ADMIN;
import static zuo.biao.apijson.RequestRole.CIRCLE;
import static zuo.biao.apijson.RequestRole.CONTACT;
import static zuo.biao.apijson.RequestRole.LOGIN;
import static zuo.biao.apijson.RequestRole.OWNER;
import static zuo.biao.apijson.RequestRole.UNKNOWN;

/**请求方法权限，只允许某些角色通过对应方法访问
 * @author Lemon
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
@Inherited
public @interface MethodAccess {

	/**@see {@link RequestMethod#GET}
	 * @return 该请求方法允许的角色 default {UNKNOWN, LOGIN, CONTACT, CIRCLE, OWNER, ADMIN};
	 */
	RequestRole[] GET() default {UNKNOWN, LOGIN, CONTACT, CIRCLE, OWNER, ADMIN};

	/**@see {@link RequestMethod#HEAD}
	 * @return 该请求方法允许的角色 default {UNKNOWN, LOGIN, CONTACT, CIRCLE, OWNER, ADMIN};
	 */
	RequestRole[] HEAD() default {UNKNOWN, LOGIN, CONTACT, CIRCLE, OWNER, ADMIN};

	/**@see {@link RequestMethod#GETS}
	 * @return 该请求方法允许的角色 default {LOGIN, CONTACT, CIRCLE, OWNER, ADMIN};
	 */
	RequestRole[] GETS() default {LOGIN, CONTACT, CIRCLE, OWNER, ADMIN};

	/**@see {@link RequestMethod#HEADS}
	 * @return 该请求方法允许的角色 default {LOGIN, CONTACT, CIRCLE, OWNER, ADMIN};
	 */
	RequestRole[] HEADS() default {LOGIN, CONTACT, CIRCLE, OWNER, ADMIN};

	/**@see {@link RequestMethod#POST}
	 * @return 该请求方法允许的角色  default {LOGIN, ADMIN};
	 */
	RequestRole[] POST() default {OWNER, ADMIN};

	/**@see {@link RequestMethod#PUT}
	 * @return 该请求方法允许的角色 default {OWNER, ADMIN};
	 */
	RequestRole[] PUT() default {OWNER, ADMIN};

	/**@see {@link RequestMethod#DELETE}
	 * @return 该请求方法允许的角色 default {OWNER, ADMIN};
	 */
	RequestRole[] DELETE() default {OWNER, ADMIN};

}
