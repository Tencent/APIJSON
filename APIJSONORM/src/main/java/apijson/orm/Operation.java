/*Copyright (C) 2020 THL A29 Limited, a Tencent company.  All rights reserved.

This source code is licensed under the Apache License Version 2.0.*/


package apijson.orm;

/**对请求JSON的操作
 * @author Lemon
 */
public enum Operation {

	/**
	 * 不允许传的字段，结构是
	 * "key0,key1,key2..."
	 * TODO 改成 MUST 减少长度 ?
	 */
	DISALLOW,
	
	/**
	 * 必须传的字段，结构是
	 * "key0,key1,key2..."
	 * TODO 改成 REFUSE 减少长度 ?
	 */
	NECESSARY,
	
	
	/**TODO 是否应该把数组类型写成 BOOLEANS, NUMBERS 等复数单词，以便抽取 enum ？扩展用 VERIFY 或 INSERT/UPDATE 远程函数等
	 * 验证是否符合预设的类型:
	 * BOOLEAN, NUMBER, DECIMAL, STRING, URL, DATE, TIME, DATETIME, OBJECT, ARRAY 
	 * 或它们的数组
	 * BOOLEAN[], NUMBER[], DECIMAL[], STRING[], URL[], DATE[], TIME[], DATETIME[], OBJECT[], ARRAY[]
	 * 结构是
	 * {
	 *   key0: value0,
	 *   key1: value1,
	 *   key2: value2
	 *   ...
	 * }
	 * 例如
	 * {
	 *   "id": "NUMBER", //id 类型必须为 NUMBER
	 *   "pictureList": "URL[]", //pictureList 类型必须为 URL[]
	 * }
	 * @see {@link Structure#type(String, String, Object, boolean)}
	 */
	TYPE,
	
	/**
	 * 验证是否符合预设的条件，结构是
	 * {
	 *   key0: value0,
	 *   key1: value1,
	 *   key2: value2
	 *   ...
	 * }
	 * 例如
	 * {
	 *   "phone~": "PHONE",  //phone 必须满足 PHONE 的格式
	 *   "status{}": [1,2,3],  //status 必须在给出的范围内
	 *   "balance&{}":">0,<=10000"  //必须满足 balance>0 & balance<=10000
	 * }
	 */
	VERIFY,
	
	/**TODO 格式改为 id;version,tag 兼容多个字段联合主键。 ["id", "version,tag"] 也行
	 * 验证是否存在，结构是
	 * "key0,key1,key2..."
	 */
	EXIST,
	
	/**TODO 格式改为 id;version,tag 兼容多个字段联合主键。 ["id", "version,tag"] 也行
	 * 验证是否不存在，除了本身的记录，结构是
	 * "key0,key1,key2..."
	 */
	UNIQUE,
	
	
	/**
	 * 添加，当要被添加的对象不存在时，结构是
	 * {
	 *   key0: value0,
	 *   key1: value1,
	 *   key2: value2
	 *   ...
	 * }
	 */
	INSERT,
	
	/**
	 * 强行放入，不存在时就添加，存在时就修改，结构是
	 * {
	 *   key0: value0,
	 *   key1: value1,
	 *   key2: value2
	 *   ...
	 * }
	 */
	UPDATE,
	
	/**
	 * 替换，当要被替换的对象存在时，结构是
	 * {
	 *   key0: value0,
	 *   key1: value1,
	 *   key2: value2
	 *   ...
	 * }
	 */
	REPLACE,
	
	/**
	 * 移除，当要被移除的对象存在时，结构是
	 * "key0,key1,key2..."
	 */
	REMOVE;
	
}
