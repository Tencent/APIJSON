/**
 * Created by Lemon on 17/5/8.
 */
const TAG_REQUEST_UTIL = 'RequestUtil';

const URL_BASE = "http://39.108.143.172:8080"; // 基地址
const URL_GET = URL_BASE + "/get"; // 常规获取数据方式
const URL_HEAD = URL_BASE + "/head"; // 检查，默认是非空检查，返回数据总数
const URL_GETS = URL_BASE + "/gets"; // 通过POST来GET数据，不显示请求内容和返回结果，一般用于对安全要求比较高的请求
const URL_HEADS = URL_BASE + "/heads"; // 通过POST来HEAD数据，不显示请求内容和返回结果，一般用于对安全要求比较高的请求
const URL_POST = URL_BASE + "/post"; // 新增(或者说插入)数据
const URL_PUT = URL_BASE + "/put"; // 修改数据，只修改传入字段对应的值
const URL_DELETE = URL_BASE + "/delete"; // 删除数据


/**请求，全走HTTP POST
 * @param url
 * @param rq
 */
function request(url, json, notAlertRequest, onreadystatechange) {
  if (json == null || json instanceof Array || (json instanceof Object) == false) {
    alertOfDebug("request  json == null || json instanceof Array || (json instanceof Object) == false !!! >> return null;");
    return null;
  }

  if (url == null || (typeof url != "string")) {
    alertOfDebug("request  url == null || typeof url != string !!! >> return null;");
    return null;
  }
  if (url.length < 3 || url.indexOf(".") < 0) {
    alertOfDebug("request  url.length < 3 || url.indexOf(.) < 0 !!! >> return null;");
    return null;
  }


  var rqf = format(JSON.stringify(json));

  var branch = url.substring(URL_BASE.length + 1, url.length);
  var end = branch.indexOf("/");
  var method = branch.substring(0, end < 0 ? branch.length : end);

  var METHOD = method.toUpperCase();

  if (! notAlertRequest) {
    alert("Request(" + METHOD + "):\n" + rqf);
  }

  var request = new XMLHttpRequest();
  request.open("POST", url, true);
  request.setRequestHeader("Content-type", "application/json");

  request.onreadystatechange = onreadystatechange != null ? onreadystatechange : function () {
    if (request.readyState !== 4) {
      return;
    }

    if (request.status === 200) {
      alert("Response(" + METHOD + "):\n" + format(request.responseText));
    } else {
      alert("Response(" + METHOD + "):\nstatus" + request.status + "\nerror:" + request.error);
    }
  }

  request.send(rqf);
  //原生请求>>>>>>>>>>>>>>>>>>>>>>>>>>


  //JQuery ajax请求<<<<<<<<<<<<<<<<<<<<<<<<<<
  // $.ajax({
  //     type: isGet ? "GET" : "POST",
  //     url: isGet ? url + "/" + rq : url,
  //     contentType: "application/json", //必须
  //     dataType: "json", //返回值类型，非必须
  //     data: isGet ? null : rq,
  //     success: function (response) {
  //         alertOfDebug(response);
  //     }
  // });
  //JQuery ajax请求>>>>>>>>>>>>>>>>>>>>>>>>>>


  //VUE axios请求<<<<<<<<<<<<<<<<<<<<<<<<<<
  // if (isGet) {
  //     axios.get(url + "/" + rq, null)
  //         .then(function (response)    {
  //             console.log(response);
  //         })
  //         .catch(function (error) {
  //             console.log(error);
  //         });
  // } else {
  //     axios({
  //         method: 'post',
  //         url: url + "/",
  //         data: json
  //     }).then(function (response) {
  //         alertOfDebug(response);
  //     }).catch(function (error) {
  //         alertOfDebug(error);
  //     });
  // }
  //VUE axios请求>>>>>>>>>>>>>>>>>>>>>>>>>>

  return request;
}

/**编码JSON，转义所有String
 * @param json 任意类型
 */
function encode(json) {
  // alertOfDebug("encode  before:\n" + format(JSON.stringify(json)));

  if (typeof json == "string") { //json instanceof String) {
    json = encodeURIComponent(json);
  }
  else if (json instanceof Array) {
    // alertOfDebug("encode  json instanceof Array");

    for (var i = 0; i < json.length; i ++) {
      // alertOfDebug("json[" + i + "] = " + format(JSON.stringify(json[i])));
      json[i] = encode(json[i]);
    }
  }
  else if (json instanceof Object) {
    // alertOfDebug("encode  json instanceof Object");
    for (var key in json) {
      // alertOfDebug("encode  json[" + key + "] = " + format(JSON.stringify(json[key])));
      json[key] = encode(json[key]);
    }
  }
  // alertOfDebug("encode  after:\n" + format(JSON.stringify(json)));

  return json;
}


/**格式化JSON串
 * @param json
 */
function format(json) {
  try {
    return JSON.stringify(JSON.parse(json), null, "\t");
  } catch(e) {
    log(TAG_REQUEST_UTIL, 'format  try { ... } catch (err) { \n ' + e);
    return json;
  }

  // 导致格式化后代码很难看，像没格式化一样
  // if (json == null || json == '') {
  //   console.log('format  json == null || json == "" >>  return json;');
  //   return json;
  // }
  //
  // if (json instanceof Object) { //避免赋值影响传进来的json
  //   return JSON.stringify(json, null, "\t");
  // }
  //
  // var jsonObj;
  // if (typeof json == 'string'){
  //   try {
  //     jsonObj = JSON.parse(json);
  //   } catch (err) {
  //     console.log('format  try { jsonObj = JSON.parse(json); } catch (err) { \n ' + err);
  //     return json;
  //   }
  // }
  // else {
  //   console.log('format  json type error !');
  //   return json;
  // }
  // return JSON.stringify(jsonObj, null, "\t");
}

function log(tag, msg) {
  console.log(tag + '.' + msg);
}

/**将json字符串转为JSON对象
 * @param s
 */
function parseJSON(s) {
  if (s instanceof Object) {
    alertOfDebug("parseJSON  s instanceof JSON >> return s;");
    return s;
  }

  if (typeof s != "string") {
    alertOfDebug("parseJSON  typeof json != string >> s = \"\" + s;");
    s = "" + s;
  }
  // alertOfDebug("parseJSON  s = \n" + s);

  return JSON.parse(s);
}

/**测试用的提示
 * @param s
 */
function alertOfDebug(s) {
  // alert(s); //注释掉就都不会弹窗了
}

/**是否为空
 * @param s
 * @returns {boolean}
 */
function isEmpty(s) {
  return s == null || s.trim() == '';
}




/**转为JSON对象
 * @param s
 * @return {*}
 */
function parseObject(s) {
  if (s == null || s instanceof Object) {
    return s;
  }
  if (typeof s == 'string') {
    if (s.indexOf("'") >= 0) {
      s = s.replace(/'/g, "\'");
    }
    return JSON.parse(s);
  }
  return parseObject(s.toString());
}

// /**格式化JSON串
//  * @param json
//  */
// function format(json) {
//   try {
//     return JSON.stringify(JSON.parse(json), null, "\t");
//   } catch(e) {
//     log(TAG_REQUEST_UTIL, 'format  try { ... } catch (err) { \n ' + err);
//     return json;
//   }
//
//   // 导致格式化后代码很难看，像没格式化一样
//   // if (json == null || json == '') {
//   //   console.log('format  json == null || json == "" >>  return json;');
//   //   return json;
//   // }
//   //
//   // if (json instanceof Object) { //避免赋值影响传进来的json
//   //   return JSON.stringify(json, null, "\t");
//   // }
//   //
//   // var jsonObj;
//   // if (typeof json == 'string'){
//   //   try {
//   //     jsonObj = JSON.parse(json);
//   //   } catch (err) {
//   //     console.log('format  try { jsonObj = JSON.parse(json); } catch (err) { \n ' + err);
//   //     return json;
//   //   }
//   // }
//   // else {
//   //   console.log('format  json type error !');
//   //   return json;
//   // }
//   // return JSON.stringify(jsonObj, null, "\t");
// }

function log(tag, msg) {
  console.log(tag + '.' + msg);
}





//常用请求<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<



/**获取单个对象
 * @param table    String, 对象名，如 "User"
 * @param id       Long, 对象id，如 1
 * @param notAlert 不弹窗显示
 * @param callBack 请求成功回调
 */
function getObject(table, id, notAlert, callBack) {
  alertOfDebug("getObject  table = " + table + "; id = " + id);

  return request(URL_GET, newSingleJSON(table, { "id": id }, null), notAlert, callBack);
}

/**获取数组
 * @param table    String, 对象名，如 "User"
 * @param json     {}, 对象内容，如 {"sex":1}
 * @param count    int, 每页数量
 * @param page     int, 页码
 * @param notAlert 不弹窗显示
 * @param callBack 请求成功回调
 */
function getArray(table, json, count, page, notAlert, callBack) {
  alertOfDebug("getArray  table = " + table + "; count = " + count + "; page = " + page
    + "; json = \n" + format(json));

  return request(URL_GET, newArrayJSON(table, json, count, page), notAlert, callBack);
}

/**新增单个对象
 * @param table    String, 对象名，如 "User"
 * @param json     {}, 对象内容，如 {"sex":1}
 * @param notAlert 不弹窗显示
 * @param callBack 请求成功回调
 */
function postObject(table, json, notAlert, callBack) {
  alertOfDebug("postObject  table = " + table
    + "; json = \n" + format(json));
  var id = json == null ? null : json['id'];
  if (id != null) {
    alertOfDebug('postObject  POST 请求不能设置 id ！');
    return;
  }

  return request(URL_POST, newSingleJSON(table, json, table), notAlert, callBack);
}
/**修改单个对象
 * @param table    String, 对象名，如 "User"
 * @param json     {}, 对象内容，如 {"sex":1}
 * @param notAlert 不弹窗显示
 * @param callBack 请求成功回调
 */
function putObject(table, json, notAlert, callBack) {
  alertOfDebug("putObject  table = " + table
    + "; json = \n" + format(json));
  if (json == null) {
    alertOfDebug('putObject  PUT 请求必须设置 table对象 ！');
    return;
  }
  if (json["id"] == null || json["id"] <= 0) {
    alertOfDebug('putObject  PUT 请求必须设置 id 且 id > 0 ！');
    return;
  }

  return request(URL_PUT, newSingleJSON(table, json, table), notAlert, callBack);
}
/**删除单个对象
 * @param table    String, 对象名，如 "User"
 * @param id       Long, 对象id，如 1
 * @param notAlert 不弹窗显示
 * @param callBack 请求成功回调
 */
function deleteObject(table, id, notAlert, callBack) {
  alertOfDebug("deleteObject  table = " + table + "; id = " + id);
  if (id == null || id <= 0) {
    alertOfDebug('deleteObject  DELETE 请求必须设置 id 且 id > 0 ！');
    return;
  }

  return request(URL_DELETE, newSingleJSON(table, { "id": id }, table), notAlert, callBack);
}

/**修改多个对象
 * @param table    String, 对象名，如 "User"
 * @param json     {}, 对象内容，如 {"sex":1}
 * @param notAlert 不弹窗显示
 * @param callBack 请求成功回调
 */
function putArray(table, json, notAlert, callBack) {
  alertOfDebug("putArray  table = " + table
    + "; json = \n" + format(json));
  var idArray = json == null ? null : json["id{}"];
  if (idArray == null || (idArray instanceof Array) == false) {
    alertOfDebug('putArray  idArray == null || (idArray instanceof Array) == false' +
      '  >>  return;  PUT 请求必须设置 id{}:[] ！');
    return;
  }

  return request(URL_PUT, newSingleJSON(table, json, table + "[]"), notAlert, callBack);
}
/**删除多个对象
 * @param table    String, 对象名，如 "User"
 * @param idArray  [], 对象id数组，如 [1, 2, 3]
 * @param notAlert 不弹窗显示
 * @param callBack 请求成功回调
 */
function deleteArray(table, idArray, notAlert, callBack) {
  alertOfDebug("deleteArray  table = " + table
    + "; idArray = \n" + format(idArray));
  if (idArray == null || (idArray instanceof Array) == false) {
    alertOfDebug('deleteArray  idArray == null ! DELETE 请求必须设置 id{}:[] ！');
    return;
  }

  return request(URL_DELETE, newSingleJSON(table, { "id{}": idArray }, table + "[]"), notAlert, callBack);
}



/**新建单个对象请求
 * @param table    String, 对象名，如 "User"
 * @param json     {}, 对象内容，如 {"sex":1}
 * @param tag      String, 写操作标识，一般来说，操作单个对象时和table相同，操作多个对象时是 table[]
 */
function newSingleJSON(table, json, tag) {
  // alertOfDebug("newSingleJSON  table = " + table + "; tag = " + tag
  //     + "; json = \n" + format(json));

  return parseJSON(newSingleString(table, json, tag));
}
/**新建数组请求
 * @param table    String, 对象名，如 "User"
 * @param json     {}, 对象内容，如 {"sex":1}
 * @param count    int, 每页数量
 * @param page     int, 页码
 */
function newArrayJSON(table, json, count, page) {
  // alertOfDebug("newArrayJSON  table = " + table + "; count = " + count + "; page = " + page
  //     + "; json = \n" + format(json));

  return parseJSON(newArrayString(table, json, count, page));
}


/**新建单个对象请求
 * @param table    String, 对象名，如 "User"
 * @param json     {}, 对象内容，如 {"sex":1}
 * @param tag      String, 写操作标识，一般来说，操作单个对象时和table相同，操作多个对象时是 table[]
 */
function newSingleString(table, json, tag) {
  if (json == null) {
    alertOfDebug('newSingleString  json == null >> return;');
    return;
  }
  // alertOfDebug("newSingleString  table = " + table + "; tag = " + tag
  //     + "; json = \n" + format(json));

  return "{\""
    + table + "\":"
    + JSON.stringify(json)
    + (isEmpty(tag) ? "" : ",\"tag\":\"" + tag + "\"")
    + "}";
}
/**新建数组请求
 * @param table    String, 对象名，如 "User"
 * @param json     {}, 对象内容，如 {"sex":1}
 * @param count    int, 每页数量
 * @param page     int, 页码
 */
function newArrayString(table, json, count, page) {
  if (json == null) {
    alertOfDebug('newArrayString  json == null >> return;');
    return;
  }
  // alertOfDebug("newArrayString  table = " + table + "; count = " + count + "; page = " + page
  //     + "; json = \n" + format(json));

  return "{\"" + table + "[]\":{" + "\"count\":" + count + "," + "\"page\":" + page + ",\""
    + table + "\":" + JSON.stringify(json) + "}}";
}


//常用请求>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>