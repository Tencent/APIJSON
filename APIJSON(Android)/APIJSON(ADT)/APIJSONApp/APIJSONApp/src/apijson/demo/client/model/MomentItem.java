package apijson.demo.client.model;

import java.util.ArrayList;
import java.util.List;

import android.support.annotation.NonNull;
import apijson.demo.client.application.APIJSONApplication;
import apijson.demo.client.server.model.BaseModel;

public class MomentItem extends BaseModel {
	private static final long serialVersionUID = -7437225320551780084L;

	public static final int STATUS_NORMAL = 0;
	public static final int STATUS_PUBLISHING = 1;
	public static final int STATUS_DELETING = 2;

	private int status;
	private User user;
	private Moment moment;
	private List<User> userList;//点赞的用户列表
	private List<CommentItem> commentItemList;

	public MomentItem() {
		super();
	}
	public MomentItem(long id) {
		this();
		setId(id);
	}

	@Override
	public Long getId() {
		return getMomentId();
	}
	@Override
	public MomentItem setId(Long id) {
		return setMomentId(id);
	}

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getStatusString() {
		return getStatusString(getStatus());
	}
	public static String getStatusString(int status) {
		switch (status) {
		case STATUS_PUBLISHING:
			return "正在发布...";
		case STATUS_DELETING:
			return "正在删除...";
		default:
			return null;
		}
	}

	/**if (user == null) >> user = new User();
	 * @return
	 */
	@NonNull
	public User getUser() {
		if (user == null) {
			user = new User();
		}
		return user;
	}

	public MomentItem setUser(User user) {
		this.user = user;
		return this;
	}

	public long getMomentId() {
		return getMoment().getId();
	}

	public MomentItem setMomentId(long id) {
		getMoment().setId(id);
		return this;
	}
	/**if (moment == null) >> moment = new Moment();
	 * @return
	 */
	@NonNull
	public Moment getMoment() {
		if (moment == null) {
			moment = new Moment();
		}
		return moment;
	}
	public MomentItem setMoment(Moment moment) {
		this.moment = moment;
		return this;
	}

	@NonNull
	public List<User> getUserList() {
		if (userList == null) {
			userList = new ArrayList<>();
		}
		return userList;
	}
	public MomentItem setUserList(List<User> userList) {
		this.userList = userList;
		return this;
	}

	@NonNull
	public List<CommentItem> getCommentItemList() {
		if (commentItemList == null) {
			commentItemList = new ArrayList<>();
		}
		return commentItemList;
	}
	public MomentItem setCommentItemList(List<CommentItem> commentItemList) {
		this.commentItemList = commentItemList;
		return this;
	}

	public List<Long> getPraiseUserIdList() {
		return getMoment().getPraiseUserIdList();
	}
	public List<Long> getCommentIdList() {
		return getMoment().getCommentIdList();
	}


	private Boolean isPraised;
	public boolean getIsPraised() {
		return getIsPraised(APIJSONApplication.getInstance().getCurrentUserId());
	}
	public boolean getIsPraised(final long userId) {
		if (userId <= 0) {
			isPraised = false;
		} else if (isPraised == null) {
			isPraised = isContain(getPraiseUserIdList(), userId);
		}
		return value(isPraised);
	}
	public MomentItem setIsPraised(boolean isPraised) {
		return setIsPraised(isPraised, APIJSONApplication.getInstance().getCurrentUserId());
	}
	public MomentItem setIsPraised(boolean isPraised, long userId) {
		this.isPraised = isPraised;

		List<Long> list = getPraiseUserIdList();
		if (list == null) {
			list = new ArrayList<>();
		}
		if (isPraised == false) {
			list.remove(userId);
		} else {
			if (list.contains(userId) == false) {
				list.add(userId);
			}
		}
		getMoment().setPraiseUserIdList(list);

		return this;
	}
	public int getPraiseCount() {
		return count(getPraiseUserIdList());
	}

	private Boolean isCommented;
	public boolean getIsCommented() {
		return getIsCommented(APIJSONApplication.getInstance().getCurrentUserId());
	}
	public boolean getIsCommented(final long userId) {
		if (userId <= 0) {
			isCommented = false;
		} else if (isCommented == null) {
			isCommented = false;
			List<CommentItem> commentItemlist = getCommentItemList();
			if (commentItemlist != null) {
				for (CommentItem comment : commentItemlist) {
					if (comment != null && comment.getComment().getUserId() == userId) {
						isCommented = true;
						break;
					}
				}
			}
		}
		return value(isCommented);
	}
	public MomentItem setIsCommented(boolean isCommented, long userId) {
		this.isCommented = isCommented;

		List<Long> list = getCommentIdList();
		if (list == null) {
			list = new ArrayList<>();
		}
		if (isCommented == false) {
			list.remove(userId);
		} else {
			if (list.contains(userId) == false) {
				list.add(userId);
			}
		}
		getMoment().setCommentIdList(list);
		
		return this;
	}

	//	public int getCommentCount() {
	//		return getMoment().getCommentCount();
	//	}

	private int commentCount;
	public int getCommentCount() {
		return commentCount;
	}
	public MomentItem setCommentCount(int commentCount) {
		this.commentCount = commentCount;
		return this;
	}

}