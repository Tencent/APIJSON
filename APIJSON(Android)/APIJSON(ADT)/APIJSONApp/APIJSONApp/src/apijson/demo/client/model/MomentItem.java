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

package apijson.demo.client.model;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import apijson.demo.client.application.APIJSONApplication;
import zuo.biao.apijson.BaseModel;

public class MomentItem extends BaseModel {
	private static final long serialVersionUID = -7437225320551780084L;

	public static final int STATUS_NORMAL = 0;
	public static final int STATUS_PUBLISHING = 1;
	public static final int STATUS_DELETING = 2;
	public static final int STATUS_DELETED = 3;

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

	public Long getUserId() {
		return getUser().getId();
	}

	public int getMyStatus() {
		return status;
	}
	public void setMyStatus(int status) {//status莫名其妙变成状态码status 200
		this.status = status;
	}
	public String getStatusString() {
		return getStatusString(getMyStatus());
	}
	public static String getStatusString(int status) {
		switch (status) {
		case STATUS_PUBLISHING:
			return "正在发布...";
		case STATUS_DELETING:
			return "正在删除...";
		default:
			return "删除";
		}
	}

	/**if (user == null) >> user = new User();
	 * @return
	 */
	@NonNull
	public User getUser() {
		if (user == null) {
			user = new User(getMoment().getUserId());
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

	//	@NonNull
	public List<User> getUserList() {
		//		if (userList == null) {
		//			userList = new ArrayList<>();
		//		}
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
		this.isPraised = isPraised;

		User currentUser = APIJSONApplication.getInstance().getCurrentUser();
		long userId = currentUser == null ? 0 : currentUser.getId();

		List<Long> list = getPraiseUserIdList();
		if (list == null) {
			list = new ArrayList<>();
		}
		if (userList == null) {
			userList = new ArrayList<User>();
		}
		if (isPraised == false) {
			list.remove(userId);
			if (userList.isEmpty() == false) {
				User[] users = userList.toArray(new User[]{});
				for (User user : users) {
					if (user != null && user.getId() == userId) {
						userList.remove(user);
						break;
					}
				}
			}
		} else {
			if (list.contains(userId) == false) {
				list.add(userId);
				userList.add(currentUser);
			}
		}
		getMoment().setPraiseUserIdList(list);


		return this;
	}
	private int praiseCount;
	public int getPraiseCount() {
		return praiseCount;
	}
	public void setPraiseCount(int praiseCount) {
		int idCount = count(getPraiseUserIdList());
		if (praiseCount < idCount) {
			praiseCount = idCount;
		}
		this.praiseCount = praiseCount;
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
			List<CommentItem> commentItemList = getCommentItemList();
			if (commentItemList != null) {
				for (CommentItem comment : commentItemList) {
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

	private int commentCount;
	public int getCommentCount() {
		return commentCount;
	}
	public MomentItem setCommentCount(int commentCount) {
		int idCount = count(getCommentIdList());
		if (commentCount < idCount) {
			commentCount = idCount;
		}
		this.commentCount = commentCount;
		return this;
	}

}