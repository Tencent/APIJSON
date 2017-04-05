/*Copyright ©2015 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package zuo.biao.library.base;

import java.io.Serializable;

/**基础Model，继承它可以省去部分代码，也可以不继承
 * *isCorrect可以用于BaseModel子类的数据校验
 * *implements Serializable 是为了网络传输字节流转换
 * @author Lemon
 * @use extends BaseModel
 */
public abstract class BaseModel implements Serializable {

	/**
	 * default, 怎么设置子类都有warning
	 */
	private static final long serialVersionUID = 1L;
	
	public long id;
	
	//对子类不起作用
	//	/**默认构造方法，JSON等解析时必须要有
	//	 */
	//	public BaseModel() {
	//	}
	

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	

	/**数据正确性校验
	 * @param data
	 * @return
	 */
	public static boolean isCorrect(BaseModel data) {
		return data != null && data.isCorrect();
	}

	/**数据正确性校验
	 * @return
	 */
	protected abstract boolean isCorrect();//public导致JSON.toJSONString会添加correct字段

}
