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

package zuo.biao.apijson.client;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

/**基础Model
 * *isCorrect可以用于BaseModel子类的数据校验
 * *implements Serializable 是为了网络传输字节流转换
 * @author Lemon
 * @use extends BaseModel
 */
public abstract class BaseModel implements Serializable {
	private static final String TAG = "BaseModel";

	/**
	 * default, 怎么设置子类都有warning
	 */
	private static final long serialVersionUID = 1L;




	public Long id;


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}


//	/**数据正确性校验
//	 * @param data
//	 * @return
//	 */
//	public static boolean isCorrect(BaseModel data) {
//		return data != null && data.isCorrect();
//	}
//
//	/**数据正确性校验
//	 * @return
//	 */
//	public abstract boolean isCorrect();

	/**把这个类的实例转为JSONObject
	 * @return
	 */
	public JSONObject toJSONObject() {
		return toJSONObject(this);
	}
	/**通过反射把这个类的实例转为JSONObject<变量类型名, 变量值>
	 * @param <T> 类型
	 * @param object 实现类
	 * @return
	 */
	public <T> JSONObject toJSONObject(T object) {
		return object == null ? null : JSON.parseObject(object.toString());

		//		JSONObject jsonObject = new JSONObject(true);
		//
		//		Field[] fields = object == null ? null : object.getClass().getDeclaredFields();
		//		if (fields != null) {
		//			Object value;
		//			for (Field field : fields) {
		//				field.setAccessible(true);
		//				try {
		//					value = field.get(object);
		//					if (value != null) {
		//						jsonObject.put(field.getName(), field.get(object));
		//					}
		//				} catch (IllegalAccessException e) {
		//					e.printStackTrace();
		//				} catch (IllegalArgumentException e) {
		//					e.printStackTrace();
		//				}
		//			}
		//		}
		//
		//		return jsonObject;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	public static String getFieldPath(String fieldName) {
		return fieldName == null ? null : TAG + "/" + fieldName;
	}

	//	public void getFieldName(Object object) {
	//		Field field = Field.DECLARED;
	//return object.get
	//	}
}
