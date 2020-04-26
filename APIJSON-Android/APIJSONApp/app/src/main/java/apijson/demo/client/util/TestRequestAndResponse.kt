package apijson.demo.client.util

import apijson.demo.client.model.Comment
import apijson.demo.client.model.Moment
import apijson.demo.client.model.User
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import zuo.biao.apijson.JSON
import java.util.*

fun request(): Map<String, Any> {
    return mapOf(
            "[]" to mapOf(
                    "count" to 5,
                    "page" to 0,
                    "join" to "</User/id@",
                    "Moment" to mapOf(
                            "@order" to "date+",
                            "userId{}" to listOf(
                                    82001,
                                    82002,
                                    82005
                            )
                    ),
                    "User" to mapOf(
                            "id@" to "/Moment/userId",
                            "@column" to "id,name,head"
                    ),
                    "User[]" to mapOf(
                            "count" to 10,
                            "User" to mapOf(
                                    "id{}@" to "[]/Moment/praiseUserIdList",
                                    "@column" to "id,name"
                            )
                    ),
                    "[]" to mapOf(
                            "count" to 6,
                            "join" to "</User/id@",
                            "Comment" to mapOf(
                                    "@order" to "date+",
                                    "momentId@" to "[]/Moment/id"
                            ),
                            "User" to mapOf(
                                    "id@" to "/Comment/userId",
                                    "@column" to "id,name"
                            )
                    )
            ),
            "format" to true
    )
}

fun smartRequest(): Map<String, Any> {
    return mapOf(
            "[]" to mapOf(
                    "count" to 5,
                    "page" to 0,
                    "join" to "</User/id@",
                    "Moment" to mapOf(
                            "@order" to "date+",
                            "userId{}" to listOf(
                                    82001,
                                    82002,
                                    82005
                            )
                    ),
                    "User" to mapOf(
                            "id@" to "/Moment/userId",
                            "@column" to "id,name,head"
                    ),
                    "User[]" to mapOf(
                            "count" to 10,
                            "User" to mapOf(
                                    "id{}@" to "[]/Moment/praiseUserIdList",
                                    "@column" to "id,name"
                            )
                    ),
                    "[]" to mapOf(
                            "count" to 6,
                            "join" to "</User/id@",
                            "Comment" to mapOf(
                                    "@order" to "date+",
                                    "momentId@" to "[]/Moment/id"
                            ),
                            "User" to mapOf(
                                    "id@" to "/Comment/userId",
                                    "@column" to "id,name"
                            )
                    )
            )
    )
}

fun response(resultJson: String?) {
    var response: JSONObject = JSON.parseObject(resultJson)


    run {   // list <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        var list:JSONArray? = response?.getJSONArray("list")
        if (list == null) {
            list = JSONArray();
        }

        var item: JSONObject?
        for (i in 0..list.size - 1) {
            item = list?.getJSONObject(i)
            if (item == null) {
                continue
            }
            println("\nitem = list[" + i + "] = \n" + item + "\n\n")
            // TODO 你的代码


            run {   // moment <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                var moment: JSONObject? = item?.getJSONObject("moment")
                if (moment == null) {
                    moment = JSONObject()
                }

                var id = moment?.getLongValue("id")
                println("moment.id = " + id);
                var userId = moment?.getLongValue("userId")
                println("moment.userId = " + userId);
                var date = moment?.getString("date")
                println("moment.date = " + date);
                var content = moment?.getString("content")
                println("moment.content = " + content);

                run {   // praiseUserIdList <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    var praiseUserIdList1:JSONArray? = moment?.getJSONArray("praiseUserIdList")
                    if (praiseUserIdList1 == null) {
                        praiseUserIdList1 = JSONArray();
                    }

                    var item1: Int?
                    for (i1 in 0..praiseUserIdList1.size - 1) {
                        item1 = praiseUserIdList1?.getInteger(i1)
                        if (item1 == null) {
                            continue
                        }
                        println("\nitem1 = praiseUserIdList1[" + i1 + "] = \n" + item1 + "\n\n")
                        // TODO 你的代码

                    }
                }   // praiseUserIdList >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


                run {   // pictureList <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    var pictureList1:JSONArray? = moment?.getJSONArray("pictureList")
                    if (pictureList1 == null) {
                        pictureList1 = JSONArray();
                    }

                    var item1: String?
                    for (i1 in 0..pictureList1.size - 1) {
                        item1 = pictureList1?.getString(i1)
                        if (item1 == null) {
                            continue
                        }
                        println("\nitem1 = pictureList1[" + i1 + "] = \n" + item1 + "\n\n")
                        // TODO 你的代码

                    }
                }   // pictureList >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

            }   // moment >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


            run {   // user <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                var user: JSONObject? = item?.getJSONObject("user")
                if (user == null) {
                    user = JSONObject()
                }

                var id = user?.getLongValue("id")
                println("user.id = " + id);
                var name = user?.getString("name")
                println("user.name = " + name);
                var head = user?.getString("head")
                println("user.head = " + head);
            }   // user >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


            run {   // userList <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                var userList1:JSONArray? = item?.getJSONArray("userList")
                if (userList1 == null) {
                    userList1 = JSONArray();
                }

                var item1: JSONObject?
                for (i1 in 0..userList1.size - 1) {
                    item1 = userList1?.getJSONObject(i1)
                    if (item1 == null) {
                        continue
                    }
                    println("\nitem1 = userList1[" + i1 + "] = \n" + item1 + "\n\n")
                    // TODO 你的代码

                    var id = item1?.getLongValue("id")
                    println("item1.id = " + id);
                    var name = item1?.getString("name")
                    println("item1.name = " + name);
                }
            }   // userList >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


            run {   // list <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                var list1:JSONArray? = item?.getJSONArray("list")
                if (list1 == null) {
                    list1 = JSONArray();
                }

                var item1: JSONObject?
                for (i1 in 0..list1.size - 1) {
                    item1 = list1?.getJSONObject(i1)
                    if (item1 == null) {
                        continue
                    }
                    println("\nitem1 = list1[" + i1 + "] = \n" + item1 + "\n\n")
                    // TODO 你的代码


                    run {   // comment <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                        var comment: JSONObject? = item1?.getJSONObject("comment")
                        if (comment == null) {
                            comment = JSONObject()
                        }

                        var id = comment?.getLongValue("id")
                        println("comment.id = " + id);
                        var toId = comment?.getLongValue("toId")
                        println("comment.toId = " + toId);
                        var userId = comment?.getLongValue("userId")
                        println("comment.userId = " + userId);
                        var momentId = comment?.getLongValue("momentId")
                        println("comment.momentId = " + momentId);
                        var date = comment?.getString("date")
                        println("comment.date = " + date);
                        var content = comment?.getString("content")
                        println("comment.content = " + content);
                    }   // comment >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


                    run {   // user <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                        var user: JSONObject? = item1?.getJSONObject("user")
                        if (user == null) {
                            user = JSONObject()
                        }

                        var id = user?.getLongValue("id")
                        println("user.id = " + id);
                        var name = user?.getString("name")
                        println("user.name = " + name);
                    }   // user >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

                }
            }   // list >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

        }
    }   // list >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    var ok = response?.getBooleanValue("ok")
    println("response.ok = " + ok);
    var code = response?.getIntValue("code")
    println("response.code = " + code);
    var msg = response?.getString("msg")
    println("response.msg = " + msg);

}



fun smartResponse(resultJson: String?) {
    var response: JSONObject = JSON.parseObject(resultJson)


    run {   // [] <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
        var list:JSONArray? = response?.getJSONArray("[]")
        if (list == null) {
            list = JSONArray();
        }

        var item: JSONObject?
        for (i in 0..list.size - 1) {
            item = list?.getJSONObject(i)
            if (item == null) {
                continue
            }
            println("\nitem = list[" + i + "] = \n" + item + "\n\n")
            // TODO 你的代码


            run {   // Moment <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                var moment:Moment? = item?.getObject("Moment", Moment::class.java)
                if (moment == null) {
                    moment = Moment()
                }

                var id = moment?.getId()
                println("moment.id = " + id)
                var userId = moment?.getUserId()
                println("moment.userId = " + userId)
                var date = moment?.getDate()
                println("moment.date = " + date)
                var content = moment?.getContent()
                println("moment.content = " + content)

                run {   // praiseUserIdList <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    var praiseUserIdList1:List<Long?>? = moment?.getPraiseUserIdList()
                    if (praiseUserIdList1 == null) {
                        praiseUserIdList1 = ArrayList();
                    }

                    var item1: Long?
                    for (i1 in 0..praiseUserIdList1.size - 1) {
                        item1 = praiseUserIdList1?.get(i1)
                        if (item1 == null) {
                            continue
                        }
                        println("\nitem1 = praiseUserIdList1[" + i1 + "] = \n" + item1 + "\n\n")
                        // TODO 你的代码

                    }
                }   // praiseUserIdList >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


                run {   // pictureList <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    var pictureList1:List<String?>? = moment?.getPictureList()
                    if (pictureList1 == null) {
                        pictureList1 = ArrayList();
                    }

                    var item1: String?
                    for (i1 in 0..pictureList1.size - 1) {
                        item1 = pictureList1?.get(i1)
                        if (item1 == null) {
                            continue
                        }
                        println("\nitem1 = pictureList1[" + i1 + "] = \n" + item1 + "\n\n")
                        // TODO 你的代码

                    }
                }   // pictureList >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

            }   // Moment >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


            run {   // User <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                var user:User? = item?.getObject("User", User::class.java)
                if (user == null) {
                    user = User()
                }

                var id = user?.getId()
                println("user.id = " + id)
                var name = user?.getName()
                println("user.name = " + name)
                var head = user?.getHead()
                println("user.head = " + head)
            }   // User >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


            run {   // User[] <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                var userList1:List<User?>? = JSON.parseArray(item?.getString("User[]"), User::class.java)
                if (userList1 == null) {
                    userList1 = ArrayList();
                }

                var item1: User?
                for (i1 in 0..userList1.size - 1) {
                    item1 = userList1?.get(i1)
                    if (item1 == null) {
                        continue
                    }
                    println("\nitem1 = userList1[" + i1 + "] = \n" + item1 + "\n\n")
                    // TODO 你的代码

                    var id = item1?.getId()
                    println("item1.id = " + id)
                    var name = item1?.getName()
                    println("item1.name = " + name)
                }
            }   // User[] >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


            run {   // [] <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                var list1:JSONArray? = item?.getJSONArray("[]")
                if (list1 == null) {
                    list1 = JSONArray();
                }

                var item1: JSONObject?
                for (i1 in 0..list1.size - 1) {
                    item1 = list1?.getJSONObject(i1)
                    if (item1 == null) {
                        continue
                    }
                    println("\nitem1 = list1[" + i1 + "] = \n" + item1 + "\n\n")
                    // TODO 你的代码


                    run {   // Comment <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                        var comment:Comment? = item1?.getObject("Comment", Comment::class.java)
                        if (comment == null) {
                            comment = Comment()
                        }

                        var id = comment?.getId()
                        println("comment.id = " + id)
                        var toId = comment?.getToId()
                        println("comment.toId = " + toId)
                        var userId = comment?.getUserId()
                        println("comment.userId = " + userId)
                        var momentId = comment?.getMomentId()
                        println("comment.momentId = " + momentId)
                        var date = comment?.getDate()
                        println("comment.date = " + date)
                        var content = comment?.getContent()
                        println("comment.content = " + content)
                    }   // Comment >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


                    run {   // User <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                        var user:User? = item1?.getObject("User", User::class.java)
                        if (user == null) {
                            user = User()
                        }

                        var id = user?.getId()
                        println("user.id = " + id)
                        var name = user?.getName()
                        println("user.name = " + name)
                    }   // User >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

                }
            }   // [] >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

        }
    }   // [] >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    var ok = response?.getBooleanValue("ok")
    println("response.ok = " + ok);
    var code = response?.getIntValue("code")
    println("response.code = " + code);
    var msg = response?.getString("msg")
    println("response.msg = " + msg);
}