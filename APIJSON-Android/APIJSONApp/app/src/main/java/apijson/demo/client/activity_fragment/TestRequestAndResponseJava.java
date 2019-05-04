package apijson.demo.client.activity_fragment;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

import zuo.biao.apijson.JSON;

public class TestRequestAndResponseJava {

    public static Map<String, Object> request() {
        Map<String, Object> request = new LinkedHashMap<>();
        request.put("emptyList", new Object[]{});
        {   //[]<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("count", 5);
            item.put("page", 0);
            item.put("join", "&/User/id@");
            {   //Moment<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                Map<String, Object> moment = new LinkedHashMap<>();
                item.put("Moment", moment);
            }   //Moment>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            {   //User<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                Map<String, Object> user = new LinkedHashMap<>();
                user.put("id{}", new Object[]{82001, 82002});
                user.put("id@", "/Moment/userId");
                user.put("@column", "id,name,head");
                item.put("User", user);
            }   //User>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            {   //User[]<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                Map<String, Object> userItem2 = new LinkedHashMap<>();
                userItem2.put("count", 10);
                {   //User<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    Map<String, Object> user = new LinkedHashMap<>();
                    user.put("id{}@", "[]/Moment/praiseUserIdList");
                    user.put("@column", "id,name");
                    userItem2.put("User", user);
                }   //User>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                item.put("User[]", userItem2);
            }   //User[]>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            {   //[]<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                Map<String, Object> item2 = new LinkedHashMap<>();
                item2.put("count", 6);
                item2.put("join", "</User/id@");
                {   //Comment<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    Map<String, Object> comment = new LinkedHashMap<>();
                    comment.put("@order", "date+");
                    comment.put("momentId@", "[]/Moment/id");
                    item2.put("Comment", comment);
                }   //Comment>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                {   //User<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    Map<String, Object> user = new LinkedHashMap<>();
                    user.put("id@", "/Comment/userId");
                    user.put("@column", "id,name");
                    item2.put("User", user);
                }   //User>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                item.put("[]", item2);
            }   //[]>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            request.put("[]", item);
        }   //[]>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

        return request;
    }

    public static void response(String resultJson) {
        JSONObject response = JSON.parseObject(resultJson);


        {  //[]<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            JSONArray list = response.getJSONArray("[]");
            if (list == null) {
                list = new JSONArray();
            }

            JSONObject item;
            for (int i = 0; i < list.size(); i ++) {
                item = list.getJSONObject(i);
                if (item == null) {
                    continue;
                }
                System.out.println("\nitem = list[" + i + "] = \n" + item + "\n\n");
                //TODO 你的代码


                {  //Moment<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    JSONObject moment = item.getJSONObject("Moment");
                    if (moment == null) {
                        moment = new JSONObject();
                    }

                    long id = moment.getLongValue("id");
                    System.out.println("moment.id = " + id);
                    long userId = moment.getLongValue("userId");
                    System.out.println("moment.userId = " + userId);
                    String date = moment.getString("date");
                    System.out.println("moment.date = " + date);
                    String content = moment.getString("content");
                    System.out.println("moment.content = " + content);

                    {  //praiseUserIdList<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                        JSONArray praiseUserIdList3 = moment.getJSONArray("praiseUserIdList");
                        if (praiseUserIdList3 == null) {
                            praiseUserIdList3 = new JSONArray();
                        }

                        Integer item3;
                        for (int i3 = 0; i3 < praiseUserIdList3.size(); i3 ++) {
                            item3 = praiseUserIdList3.getInteger(i3);
                            if (item3 == null) {
                                continue;
                            }
                            System.out.println("\nitem3 = praiseUserIdList3[" + i3 + "] = \n" + item3 + "\n\n");
                            //TODO 你的代码

                        }
                    }  //praiseUserIdList>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


                    {  //pictureList<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                        JSONArray pictureList3 = moment.getJSONArray("pictureList");
                        if (pictureList3 == null) {
                            pictureList3 = new JSONArray();
                        }

                        String item3;
                        for (int i3 = 0; i3 < pictureList3.size(); i3 ++) {
                            item3 = pictureList3.getString(i3);
                            if (item3 == null) {
                                continue;
                            }
                            System.out.println("\nitem3 = pictureList3[" + i3 + "] = \n" + item3 + "\n\n");
                            //TODO 你的代码

                        }
                    }  //pictureList>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

                }  //Moment>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


                {  //User<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    JSONObject user = item.getJSONObject("User");
                    if (user == null) {
                        user = new JSONObject();
                    }

                    long id = user.getLongValue("id");
                    System.out.println("user.id = " + id);
                    String name = user.getString("name");
                    System.out.println("user.name = " + name);
                    String head = user.getString("head");
                    System.out.println("user.head = " + head);
                }  //User>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


                {  //User[]<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    JSONArray userList2 = item.getJSONArray("User[]");
                    if (userList2 == null) {
                        userList2 = new JSONArray();
                    }

                    JSONObject item2;
                    for (int i2 = 0; i2 < userList2.size(); i2 ++) {
                        item2 = userList2.getJSONObject(i2);
                        if (item2 == null) {
                            continue;
                        }
                        System.out.println("\nitem2 = userList2[" + i2 + "] = \n" + item2 + "\n\n");
                        //TODO 你的代码

                        long id = item2.getLongValue("id");
                        System.out.println("item2.id = " + id);
                        String name = item2.getString("name");
                        System.out.println("item2.name = " + name);
                    }
                }  //User[]>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


                {  //[]<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    JSONArray list2 = item.getJSONArray("[]");
                    if (list2 == null) {
                        list2 = new JSONArray();
                    }

                    JSONObject item2;
                    for (int i2 = 0; i2 < list2.size(); i2 ++) {
                        item2 = list2.getJSONObject(i2);
                        if (item2 == null) {
                            continue;
                        }
                        System.out.println("\nitem2 = list2[" + i2 + "] = \n" + item2 + "\n\n");
                        //TODO 你的代码


                        {  //Comment<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                            JSONObject comment = item2.getJSONObject("Comment");
                            if (comment == null) {
                                comment = new JSONObject();
                            }

                            long id = comment.getLongValue("id");
                            System.out.println("comment.id = " + id);
                            long toId = comment.getLongValue("toId");
                            System.out.println("comment.toId = " + toId);
                            long userId = comment.getLongValue("userId");
                            System.out.println("comment.userId = " + userId);
                            long momentId = comment.getLongValue("momentId");
                            System.out.println("comment.momentId = " + momentId);
                            String date = comment.getString("date");
                            System.out.println("comment.date = " + date);
                            String content = comment.getString("content");
                            System.out.println("comment.content = " + content);
                        }  //Comment>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


                        {  //User<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                            JSONObject user = item2.getJSONObject("User");
                            if (user == null) {
                                user = new JSONObject();
                            }

                            long id = user.getLongValue("id");
                            System.out.println("user.id = " + id);
                            String name = user.getString("name");
                            System.out.println("user.name = " + name);
                        }  //User>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

                    }
                }  //[]>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

            }
        }  //[]>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


        {  //list<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            JSONArray list = response.getJSONArray("list");
            if (list == null) {
                list = new JSONArray();
            }

            Object item;
            for (int i = 0; i < list.size(); i ++) {
                item = list.get(i);
                if (item == null) {
                    continue;
                }
                System.out.println("\nitem = list[" + i + "] = \n" + item + "\n\n");
                //TODO 你的代码

            }
        }  //list>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

        int code = response.getIntValue("code");
        System.out.println("response.code = " + code);
        String msg = response.getString("msg");
        System.out.println("response.msg = " + msg);
    }

}
