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

package apijson.demo.client.view;

import java.util.ArrayList;
import java.util.Random;

import apijson.demo.client.model.Moment;
import apijson.demo.client.model.MomentItem;
import apijson.demo.client.model.User;

import zuo.biao.apijson.JSON;
import zuo.biao.library.util.Log;


public class ShowUtil {
	private static final String TAG = "ShowUtil";

	public static final String[] URLS = {
		"https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067",
		"http://static.oschina.net/uploads/img/201604/22172507_Pz9Y.png",
		"https://camo.githubusercontent.com/5f5c4e0c4dc539c34e8eae8ac0cbc6dccdfee5d3/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343533333831362d323032373434343231382e6a7067",
		"http://static.oschina.net/uploads/img/201604/22172507_aMmH.jpg",
		"https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067",
		"http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg",
		"http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg",
		"https://camo.githubusercontent.com/788c0a7e11a4f5aadef3c886f028c79b4808613a/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343932353935372d313732303737333630382e6a7067",
		"http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg"
	};
	

	public static MomentItem getShow(long id) {
		User user = new User(id);
		user.setHead("http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000");
		user.setName("Tommy");

		Moment moment = new Moment(id);
		moment.setId(id);
		moment.setDate(System.currentTimeMillis() - id*id*id*100000);
		moment.setContent("传统方法是继承现有View再重写方法，在实现、预览、路径等方面存在很多问题。而ZBLibrary使用原生组件组合的方式，省去了大量麻烦。");

		ArrayList<String> pictureList = new ArrayList<String>();

		Random random = new Random();
		int length = random.nextInt(9);
		for (int i = 0; i < length; i++) {
			pictureList.add(URLS[random.nextInt(URLS.length)]);
		}
		
		Log.d(TAG, "\n\n getShow  pictureList = \n" + JSON.toJSONString(pictureList) + "\n\n");

		moment.setPictureList(pictureList);
		
		MomentItem item = new MomentItem(id);
//		item.setUser(user);
		item.setMoment(moment);
		
//		List<User> userList = new ArrayList<>();
//		length = random.nextInt(20);
//		for (int i = 0; i < length; i++) {
//			user = new User(i);
//			user.setName("name" + i);
//			userList.add(user);
//		}
//		item.setUserList(userList);
//		
//		List<CommentItem> commentList = new ArrayList<>();
//		length = random.nextInt(10);
//		for (int i = 0; i < length; i++) {
//			commentList.add(new CommentItem(new Comment("content" + i)));
//		}
//		item.setCommentList(commentList);
		
		return item;
	}

}
