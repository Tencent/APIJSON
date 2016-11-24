# APIJSON
A Functional JSON Format Protocal.

You can set any JSON structure and your server will return a JSON String with the structure you had set like this:

## Request:
{
    "[]": {                       //request an array
        "page": 1,                //array condition
        "count": 2,        
        "User": {                 //request an object form the table named User
            "range": 1,           //object condition
            "sex": 0       
        },
        "work": {
            "userId": “/User/id”  //rely path with default parent path,starts from the same level object path
        }
        "Comment[]": {            //request an array named Comment
            "page": 0,
            "count": 3,
            "Comment": {
                 "workId": “[]/Work/id”  //full rely path
             }
        },
    }
}

## Response:
{
    "[]":{
        "0":{
            "User":{
                "picture":"",
                "id":"38710",
                "sex":"0",
                "phone":"1300038710",
                "name":"Name-38710",
                "head":"http://www.tooopen.com/view/38710.html"
            },
            "Work":{
                "id":470,
                "title":"Title-470",
                "content":"This is a Content...-470",
                "userId":38710,
                "picture":"http://www.tooopen.com/view/470.html"
            },
            "Comment[]":{
                "0":{
                    "Comment":{
                        "id":4,
                        "parentId":0,
                        "workId":470,
                        "userId":310,
                        "targetUserId":14604,
                        "content":"This is a Content...-4",
                        "targetUserName":"targetUserName-14604",
                        "userName":"userName-93781"
                    }
                },
                "1":{
                    "Comment":{
                        "id":22,
                        "parentId":221,
                        "workId":470,
                        "userId":332,
                        "targetUserId":5904,
                        "content":"This is a Content...-22",
                        "targetUserName":"targetUserName-5904",
                        "userName":"userName-11679"
                    }
                },
                "2":{
                    "Comment":{
                        "id":47,
                        "parentId":4,
                        "workId":470,
                        "userId":10,
                        "targetUserId":5477,
                        "content":"This is a Content...-47",
                        "targetUserName":"targetUserName-5477",
                        "userName":"userName-80271"
                    }
                }
            }
        },
        "1":{
            "User":{
                "picture":"",
                "id":"70793",
                "sex":"0",
                "phone":"1300070793",
                "name":"Name-70793",
                "head":"http://www.tooopen.com/view/70793.html"
            },
            "Work":{
                "id":170,
                "title":"Title-73",
                "content":"This is a Content...-73",
                "userId":70793,
                "picture":"http://www.tooopen.com/view/73.html"
            },
            "Comment[]":{
                "0":{
                    "Comment":{
                        "id":44,
                        "parentId":0,
                        "workId":170,
                        "userId":7073,
                        "targetUserId":6378,
                        "content":"This is a Content...-44",
                        "targetUserName":"targetUserName-6378",
                        "userName":"userName-88645"
                    }
                },
                "1":{
                    "Comment":{
                        "id":54,
                        "parentId":0,
                        "workId":170,
                        "userId":3,
                        "targetUserId":62122,
                        "content":"This is a Content...-54",
                        "targetUserName":"targetUserName-62122",
                        "userName":"userName-82381"
                    }
                },
                "2":{
                    "Comment":{
                        "id":99,
                        "parentId":44,
                        "workId":170,
                        "userId":793,
                        "targetUserId":7166,
                        "content":"This is a Content...-99",
                        "targetUserName":"targetUserName-7166",
                        "userName":"userName-22949"
                    }
                }
            }
        }
    }
}



APIJSON, let interfaces go to hell! 
