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

package zuo.biao.apijson.server.exception;

import java.util.concurrent.TimeoutException;

/**
 * @author Lemon
 */
public final class ExceptionUtil {
	
	private ExceptionUtil() {}

	
	/**
	 * @param message
	 * @throws NullPointerException 
	 */
	public static final void throwNullPointer(String message) throws NullPointerException {
		throw new NullPointerException(message);
	}

	/**
	 * @param message
	 * @throws UnsupportedOperationException 
	 */
	public static final void throwUnsupportedOperation(String message) throws UnsupportedOperationException {
		throw new UnsupportedOperationException(message);
	}
	
	/**
	 * @param message
	 * @throws IllegalAccessException
	 */
	public static final void throwIllegalAccess(String message) throws IllegalAccessException {
		throw new IllegalAccessException(message);
	}
	
	/**
	 * @param message
	 * @throws IllegalArgumentException 
	 */
	public static final void throwIllegalArgument(String message) throws IllegalArgumentException {
		throw new IllegalArgumentException(message);
	}

	/**
	 * @param message
	 * @throws ConditionNotMatchException
	 */
	public static final void throwConditionNotMatch(String message) throws ConditionNotMatchException {
		throw new ConditionNotMatchException(message);
	}
	
	/**
	 * @param message
	 * @throws NotExistException
	 */
	public static final void throwNotExist(String message) throws NotExistException {
		throw new NotExistException(message);
	}
	
	/**
	 * @param message
	 * @throws ConflictException
	 */
	public static final void throwConflict(String message) throws ConflictException {
		throw new ConflictException(message);
	}

	/**
	 * @param message
	 * @throws TimeoutException
	 */
	public static final void throwTimeout(String message) throws TimeoutException {
		throw new TimeoutException(message);
	}

}
