package apijson.demo.client.model;

import java.util.List;

import zuo.biao.apijson.JSONObject;
import zuo.biao.apijson.JSONResponse;
import android.support.annotation.NonNull;
import apijson.demo.client.server.model.BaseModel;

public class CommentItem extends BaseModel {
	private static final long serialVersionUID = -1011007127735372824L;

	private Comment comment;
	private User user;
	private User toUser;
	private JSONObject toUserObject;
	private List<CommentItem> childList;

	public CommentItem() {
		super();
	}
	public CommentItem(Comment comment) {
		this();
		setComment(comment);
	}

	@NonNull
	public Comment getComment() {
		if (comment == null) {
			comment = new Comment();
		}
		return comment;
	}
	public CommentItem setComment(Comment comment) {
		this.comment = comment;
		return this;
	}

	@NonNull
	public User getUser() {
		if (user == null) {
			user = new User(getComment().getUserId());
		}
		return user;
	}
	public CommentItem setUser(User user) {
		this.user = user;
		return this;
	}
	@NonNull
	public User getToUser() {
		if (toUser == null) {
			toUser = JSONResponse.toObject(new JSONResponse(toUserObject), User.class);
		}
		if (toUser == null) {
			toUser = new User(getComment().getToUserId());
		}
		return toUser;
	}
	public CommentItem setToUser(User toUser) {
		this.toUser = toUser;
		setToUserObject(new JSONObject(toUser));
		return this;
	}
	public JSONObject getToUserObject() {
		return toUserObject;
	}
	public void setToUserObject(JSONObject toUserObject) {
		this.toUserObject = toUserObject;
	}
	
	public List<CommentItem> getChildList() {
		return childList;
	}
	public CommentItem setChildList(List<CommentItem> childList) {
		this.childList = childList;
		return this;
	}
	

	@Override
	public Long getId() {
		return getComment().getId();
	}
	@Override
	public Long getDate() {
		return getComment().getDate();
	}

	public Long getUserId() {
		return getUser().getId();
	}
	
}

