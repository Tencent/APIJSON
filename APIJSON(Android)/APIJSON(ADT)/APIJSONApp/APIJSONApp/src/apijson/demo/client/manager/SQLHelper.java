/*Copyright ©2016 TommyLemon(https://github.com/TommyLemon)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package apijson.demo.client.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import zuo.biao.library.util.Log;
import zuo.biao.library.util.StringUtil;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**SQLite数据库Helper
 * @author Lemon
 */
public class SQLHelper extends SQLiteOpenHelper {
	private static final String TAG = "SQLHelper";

	public static final int TABLE_VERSION = 1;

	public static final String TABLE_NAME = "zblibrary_demo";
	public static final String COLUMN_ID = "_id";//long类型的id不能自增，primary key autoincrement会出错
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_PHONE = "phone";
	public static final String COLUMN_MAIL = "mail";
	public static final String COLUMN_OTHER = "other";


	public SQLHelper(Context context) {
		super(context, TABLE_NAME, null, TABLE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER primary key autoincrement, "
				+ COLUMN_NAME + " text, " + COLUMN_PHONE + " text, " + COLUMN_MAIL + " text, " + COLUMN_OTHER + " text)";

		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}



	/**根据Cursor获取内容
	 * @param cursor
	 * @return
	 */
	public ContentValues getValue(Cursor cursor) {
		ContentValues values = null;
		if (cursor != null) {
			values = new ContentValues();

			if (cursor.moveToFirst()) {
				values.put(COLUMN_ID, cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
				values.put(COLUMN_NAME, cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
				values.put(COLUMN_PHONE, cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)));
				values.put(COLUMN_MAIL, cursor.getString(cursor.getColumnIndex(COLUMN_MAIL)));
				values.put(COLUMN_OTHER, cursor.getString(cursor.getColumnIndex(COLUMN_OTHER)));
			}
			cursor.close();
		}
		return values;
	}
	/**根据Cursor获取内容
	 * @param cursor
	 * @return
	 */
	public List<ContentValues> getValueList(Cursor cursor) {
		List<ContentValues> list = null;
		if (cursor != null) {
			list = new ArrayList<ContentValues>();

			ContentValues values;
			while (cursor.moveToNext()) {
				values = new ContentValues();
				values.put(COLUMN_ID, cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
				values.put(COLUMN_NAME, cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
				values.put(COLUMN_PHONE, cursor.getString(cursor.getColumnIndex(COLUMN_PHONE)));
				values.put(COLUMN_MAIL, cursor.getString(cursor.getColumnIndex(COLUMN_MAIL)));
				values.put(COLUMN_OTHER, cursor.getString(cursor.getColumnIndex(COLUMN_OTHER)));

				list.add(values);
			}
			cursor.close();
		}
		return list;
	}

	/**获取正确的的ContentValues，防止数据库操作出错
	 * @param values
	 * @return
	 */
	public ContentValues getCorrectValues(ContentValues values) {
		if (values == null || values.size() <= 0) {
			return null;
		}

		//去除所有空key
		Set<String> set = values.keySet();
		for (String key : set) {
			if (StringUtil.isNotEmpty(key, true) == false) {
				values.remove(key);
			}
		}

		return values;
	}


	/**插入数据
	 * @param id
	 * @param values - 一行键值对数据
	 * @return
	 */
	public void put(int id, ContentValues values) {
		put(COLUMN_ID, "" + id, values);
	}
	/**插入数据
	 * @param column - 列名称
	 * @param value - 筛选数据的条件值
	 * @param values - 一行键值对数据
	 */
	public void put(String column, String value, ContentValues values) {
		ContentValues oldValues = get(column, value);
		if (oldValues != null && oldValues.containsKey(COLUMN_ID)
				&& StringUtil.isNotEmpty(oldValues.get(COLUMN_ID), true)) {//数据存在且有效
			update(column, value, values);
		} else {
			insert(values);
		}
	}

	/**获取单个数据
	 * @param id
	 * @return
	 */
	public ContentValues get(int id) {
		return getValue(query(id));
	}
	/**获取单个数据
	 * @param column
	 * @param value
	 * @return
	 */
	public ContentValues get(String column, String value) {
		return getValue(query(column, value));
	}
	/**获取数据列表
	 * @param column
	 * @param value
	 * @return
	 */
	public List<ContentValues> getList(String column, String value) {
		return getValueList(query(column, value));
	}
	/**获取所有数据
	 * @param id
	 * @return
	 */
	public List<ContentValues> getAll() {
		return getList(null, null);
	}

	/**插入数据
	 * @param values
	 * @return
	 */
	public long insert(ContentValues values) {
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			return db.insert(TABLE_NAME, null, getCorrectValues(values));
		} catch (Exception e) {
			Log.e(TAG, "update   try { return db.insert(.... } catch (Exception e) {\n " + e.getMessage());
		}
		return -1;
	}

	/**更新数据
	 * @param id
	 * @param values
	 * @return
	 */
	public int update(int id, ContentValues values) {
		return update(COLUMN_ID, "" + id, values);
	}
	/**更新数据
	 * @param column
	 * @param value
	 * @param values
	 * @return
	 */
	public int update(String column, String value, ContentValues values) {
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			return db.update(TABLE_NAME, getCorrectValues(values), getSelection(column), getSelectionArgs(column, value));
		} catch (Exception e) {
			Log.e(TAG, "update   try { return db.update(.... } catch (Exception e) {\n " + e.getMessage());
		}
		return 0;
	}

	/**删除数据
	 * @param id
	 * @return
	 */
	public int delete(int id) {
		return delete(COLUMN_ID, "" + id);
	}
	/**删除数据
	 * @param column
	 * @param value
	 * @return
	 */
	public int delete(String column, String value) {
		SQLiteDatabase db = this.getWritableDatabase();
		try {
			return db.delete(TABLE_NAME, getSelection(column), getSelectionArgs(column, value));
		} catch (Exception e) {
			Log.e(TAG, "update   try { return db.delete(.... } catch (Exception e) {\n " + e.getMessage());
		}
		return 0;
	}

	/**查询所有数据
	 * @return
	 */
	public Cursor queryAll() {
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			return db.query(TABLE_NAME, null, null, null, null, null, null);
		} catch (Exception e) {
			Log.e(TAG, "queryAll  try { return db.query(...} catch (Exception e) {\n" + e.getMessage());
		}
		return null;
	}
	/**查询单个数据
	 * @param id
	 * @return
	 */
	public Cursor query(int id) {
		return query(COLUMN_ID, "" + id);
	}
	/**查询单个数据
	 * @param column
	 * @param value
	 * @return
	 */
	public Cursor query(String column, String value) {
		SQLiteDatabase db = this.getReadableDatabase();
		try {
			return db.query(TABLE_NAME, null, getSelection(column), getSelectionArgs(column, value), null, null, null);
		} catch (Exception e) {
			Log.e(TAG, "query  try { return db.query(...} catch (Exception e) {\n" + e.getMessage());
		}
		return null;
	}



	/**获取过滤条件类型
	 * @param column
	 * @return StringUtil.isNotEmpty(column, false) ? column + " = ?" : null
	 */
	private String getSelection(String column) {
		return StringUtil.isNotEmpty(column, false) ? column + " = ?" : null;
	}
	/**获取过滤条件值
	 * @param column
	 * @return StringUtil.isNotEmpty(column, false) ? new String[]{value} : nul
	 */
	private String[] getSelectionArgs(String column, String value) {
		return StringUtil.isNotEmpty(column, false) ? new String[]{value} : null;
	}
}
