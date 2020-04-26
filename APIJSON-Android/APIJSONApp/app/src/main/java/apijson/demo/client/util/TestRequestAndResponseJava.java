package apijson.demo.client.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import apijson.demo.client.model.Comment;
import apijson.demo.client.model.Moment;
import apijson.demo.client.model.User;
import zuo.biao.apijson.JSON;
import zuo.biao.apijson.JSONRequest;

public class TestRequestAndResponseJava {

    public static Map<String, Object> request() {
        Map<String, Object> request = new LinkedHashMap<>();


        {   // [] <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("count", 5);
            item.put("page", 0);
            item.put("join", "</User/id@");


            {   // Moment <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                Map<String, Object> moment = new LinkedHashMap<>();
                moment.put("@order", "date+");


                {   // userId{} <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    List<Object> userIdIn = new ArrayList<>();
                    userIdIn.add(82001);
                    userIdIn.add(82002);
                    userIdIn.add(82005);

                    moment.put("userId{}", userIdIn);
                }   // userId{} >>>>>>>>>>>>>>>>>>>>>>>>>>>>>

                item.put("Moment", moment);
            }   // Moment >>>>>>>>>>>>>>>>>>>>>>>>>>>>>


            {   // User <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                Map<String, Object> user = new LinkedHashMap<>();
                user.put("id@", "/Moment/userId");
                user.put("@column", "id,name,head");

                item.put("User", user);
            }   // User >>>>>>>>>>>>>>>>>>>>>>>>>>>>>


            {   // User[] <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                Map<String, Object> userItem2 = new LinkedHashMap<>();
                userItem2.put("count", 10);


                {   // User <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    Map<String, Object> user = new LinkedHashMap<>();
                    user.put("id{}@", "[]/Moment/praiseUserIdList");
                    user.put("@column", "id,name");

                    userItem2.put("User", user);
                }   // User >>>>>>>>>>>>>>>>>>>>>>>>>>>>>

                item.put("User[]", userItem2);
            }   // User[] >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


            {   // [] <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                Map<String, Object> item2 = new LinkedHashMap<>();
                item2.put("count", 6);
                item2.put("join", "</User/id@");


                {   // Comment <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    Map<String, Object> comment = new LinkedHashMap<>();
                    comment.put("@order", "date+");
                    comment.put("momentId@", "[]/Moment/id");

                    item2.put("Comment", comment);
                }   // Comment >>>>>>>>>>>>>>>>>>>>>>>>>>>>>


                {   // User <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    Map<String, Object> user = new LinkedHashMap<>();
                    user.put("id@", "/Comment/userId");
                    user.put("@column", "id,name");

                    item2.put("User", user);
                }   // User >>>>>>>>>>>>>>>>>>>>>>>>>>>>>

                item.put("[]", item2);
            }   // [] >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

            request.put("[]", item);
        }   // [] >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

        request.put(JSONRequest.KEY_FORMAT, true);
        return request;
    }

    public static Map<String, Object> smartRequest() {
        JSONRequest request = new JSONRequest();


        {   // [] <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            JSONRequest item = new JSONRequest();


            {   // Moment <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                JSONRequest moment = new JSONRequest();


                {   // userId{} <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    JSONArray userIdIn = new JSONArray();
                    userIdIn.add(82001);
                    userIdIn.add(82002);
                    userIdIn.add(82005);

                    moment.put("userId{}", userIdIn);
                }   // userId{} >>>>>>>>>>>>>>>>>>>>>>>>>>>>>

                moment.setOrder("date+");

                item.put("Moment", moment);
            }   // Moment >>>>>>>>>>>>>>>>>>>>>>>>>>>>>


            {   // User <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                JSONRequest user = new JSONRequest();
                user.put("id@", "/Moment/userId");
                user.setColumn("id,name,head");
                item.put("User", user);
            }   // User >>>>>>>>>>>>>>>>>>>>>>>>>>>>>


            {   // User[] <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                JSONRequest userItem2 = new JSONRequest();


                {   // User <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    JSONRequest user = new JSONRequest();
                    user.put("id{}@", "[]/Moment/praiseUserIdList");
                    user.setColumn("id,name");

                    userItem2.put("User", user);
                }   // User >>>>>>>>>>>>>>>>>>>>>>>>>>>>>

                item.putAll(userItem2.toArray(10, 0, "User"));
            }   // User[] >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


            {   // [] <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                JSONRequest item2 = new JSONRequest();


                {   // Comment <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    JSONRequest comment = new JSONRequest();
                    comment.put("momentId@", "[]/Moment/id");
                    comment.setOrder("date+");

                    item2.put("Comment", comment);
                }   // Comment >>>>>>>>>>>>>>>>>>>>>>>>>>>>>


                {   // User <<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    JSONRequest user = new JSONRequest();
                    user.put("id@", "/Comment/userId");
                    user.setColumn("id,name");

                    item2.put("User", user);
                }   // User >>>>>>>>>>>>>>>>>>>>>>>>>>>>>

                item2.setJoin("</User/id@");
                item.putAll(item2.toArray(6, 0));
            }   // [] >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

            item.setJoin("</User/id@");
            request.putAll(item.toArray(5, 0));
        }   // [] >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

        return request;
    }


    public static void response(String resultJson) {

        JSONObject response = JSON.parseObject(resultJson);


        {   // list <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            JSONArray list = response.getJSONArray("list");
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
                // TODO 你的代码


                {   // moment <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    JSONObject moment = item.getJSONObject("moment");
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

                    {   // praiseUserIdList <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
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
                            // TODO 你的代码

                        }
                    }   //praiseUserIdList >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


                    {   // pictureList <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
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
                            // TODO 你的代码

                        }
                    }   //pictureList >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

                }   //moment >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


                {   // user <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    JSONObject user = item.getJSONObject("user");
                    if (user == null) {
                        user = new JSONObject();
                    }

                    long id = user.getLongValue("id");
                    System.out.println("user.id = " + id);
                    String name = user.getString("name");
                    System.out.println("user.name = " + name);
                    String head = user.getString("head");
                    System.out.println("user.head = " + head);
                }   //user >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


                {   // userList <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    JSONArray userList2 = item.getJSONArray("userList");
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
                        // TODO 你的代码

                        long id = item2.getLongValue("id");
                        System.out.println("item2.id = " + id);
                        String name = item2.getString("name");
                        System.out.println("item2.name = " + name);
                    }
                }   //userList >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


                {   // list <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    JSONArray list2 = item.getJSONArray("list");
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
                        // TODO 你的代码


                        {   // comment <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                            JSONObject comment = item2.getJSONObject("comment");
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
                        }   //comment >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


                        {   // user <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                            JSONObject user = item2.getJSONObject("user");
                            if (user == null) {
                                user = new JSONObject();
                            }

                            long id = user.getLongValue("id");
                            System.out.println("user.id = " + id);
                            String name = user.getString("name");
                            System.out.println("user.name = " + name);
                        }   //user >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

                    }
                }   //list >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

            }
        }   //list >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

        boolean ok = response.getBooleanValue("ok");
        System.out.println("response.ok = " + ok);
        int code = response.getIntValue("code");
        System.out.println("response.code = " + code);
        String msg = response.getString("msg");
        System.out.println("response.msg = " + msg);
    }

    public static void smartResponse(String resultJson) {
        JSONObject response = JSON.parseObject(resultJson);


        {   // [] <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
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
                // TODO 你的代码


                {   // Moment <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    Moment moment = item.getObject("Moment", Moment.class);
                    if (moment == null) {
                        moment = new Moment();
                    }

                    long id = moment.getId();
                    System.out.println("moment.id = " + id);
                    long userId = moment.getUserId();
                    System.out.println("moment.userId = " + userId);
                    Long date = moment.getDate();
                    System.out.println("moment.date = " + date);
                    String content = moment.getContent();
                    System.out.println("moment.content = " + content);

                    {   // praiseUserIdList <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                        List<Long> praiseUserIdList3 = moment.getPraiseUserIdList();
                        if (praiseUserIdList3 == null) {
                            praiseUserIdList3 = new ArrayList<>();
                        }

                        Long item3;
                        for (int i3 = 0; i3 < praiseUserIdList3.size(); i3 ++) {
                            item3 = praiseUserIdList3.get(i3);
                            if (item3 == null) {
                                continue;
                            }
                            System.out.println("\nitem3 = praiseUserIdList3[" + i3 + "] = \n" + item3 + "\n\n");
                            // TODO 你的代码

                        }
                    }   //praiseUserIdList >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


                    {   // pictureList <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                        List<String> pictureList3 = moment.getPictureList();
                        if (pictureList3 == null) {
                            pictureList3 = new ArrayList<>();
                        }

                        String item3;
                        for (int i3 = 0; i3 < pictureList3.size(); i3 ++) {
                            item3 = pictureList3.get(i3);
                            if (item3 == null) {
                                continue;
                            }
                            System.out.println("\nitem3 = pictureList3[" + i3 + "] = \n" + item3 + "\n\n");
                            // TODO 你的代码

                        }
                    }   //pictureList >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

                }   //Moment >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


                {   // User <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    User user = item.getObject("User", User.class);
                    if (user == null) {
                        user = new User();
                    }

                    long id = user.getId();
                    System.out.println("user.id = " + id);
                    String name = user.getName();
                    System.out.println("user.name = " + name);
                    String head = user.getHead();
                    System.out.println("user.head = " + head);
                }   //User >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


                {   // User[] <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    List<User> userList2 = JSON.parseArray(item.getString("User[]"), User.class);
                    if (userList2 == null) {
                        userList2 = new ArrayList<>();
                    }

                    User item2;
                    for (int i2 = 0; i2 < userList2.size(); i2 ++) {
                        item2 = userList2.get(i2);
                        if (item2 == null) {
                            continue;
                        }
                        System.out.println("\nitem2 = userList2[" + i2 + "] = \n" + item2 + "\n\n");
                        // TODO 你的代码

                        long id = item2.getId();
                        System.out.println("item2.id = " + id);
                        String name = item2.getName();
                        System.out.println("item2.name = " + name);
                    }
                }   //User[] >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


                {   // [] <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
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
                        // TODO 你的代码


                        {   // Comment <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                            Comment comment = item2.getObject("Comment", Comment.class);
                            if (comment == null) {
                                comment = new Comment();
                            }

                            long id = comment.getId();
                            System.out.println("comment.id = " + id);
                            long toId = comment.getToId();
                            System.out.println("comment.toId = " + toId);
                            long userId = comment.getUserId();
                            System.out.println("comment.userId = " + userId);
                            long momentId = comment.getMomentId();
                            System.out.println("comment.momentId = " + momentId);
                            Long date = comment.getDate();
                            System.out.println("comment.date = " + date);
                            String content = comment.getContent();
                            System.out.println("comment.content = " + content);
                        }   //Comment >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


                        {   // User <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                            User user = item2.getObject("User", User.class);
                            if (user == null) {
                                user = new User();
                            }

                            long id = user.getId();
                            System.out.println("user.id = " + id);
                            String name = user.getName();
                            System.out.println("user.name = " + name);
                        }   //User >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

                    }
                }   //[] >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

            }
        }   //[] >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

        boolean ok = response.getBooleanValue("ok");
        System.out.println("response.ok = " + ok);
        int code = response.getIntValue("code");
        System.out.println("response.code = " + code);
        String msg = response.getString("msg");
        System.out.println("response.msg = " + msg);

    }


}
