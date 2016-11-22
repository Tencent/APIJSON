package zuo.biao.apijson;

import java.lang.reflect.Field;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Work extends BaseModel {
	public static final String TAG = "Work";

	private static final long serialVersionUID = -7437225320551780084L;


	public long userId;
	public String title;
	public String content;
	public String picture;

	public Work() {
		super();
	}
	public Work(long id, String title) {
		this();
		this.id = id;
		this.title = title;
	}

	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}


//	@Override
//	public boolean isCorrect() {
//		return getId() > 0 && getUserId() > 0
//				&& (StringUtil.isNotEmpty(getTitle(), true) || StringUtil.isNotEmpty(getContent(), true));
//	}

	@Override
	public JSONObject toJSONObject() {
		return toJSONObject(this);
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
