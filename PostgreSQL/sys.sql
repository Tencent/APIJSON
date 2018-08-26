/*
 Navicat PostgreSQL Data Transfer

 Source Server         : localhost
 Source Server Type    : PostgreSQL
 Source Server Version : 100005
 Source Host           : localhost:5432
 Source Catalog        : postgres
 Source Schema         : sys

 Target Server Type    : PostgreSQL
 Target Server Version : 100005
 File Encoding         : 65001

 Date: 26/08/2018 18:50:42
*/


-- ----------------------------
-- Table structure for apijson_user
-- ----------------------------
DROP TABLE IF EXISTS "sys"."apijson_user";
CREATE TABLE "sys"."apijson_user" (
  "id" int8 NOT NULL,
  "name" varchar(30) COLLATE "pg_catalog"."default" NOT NULL,
  "sex" int2 NOT NULL DEFAULT 0,
  "date" timestamp(6),
  "head" varchar(255) COLLATE "pg_catalog"."default" NOT NULL DEFAULT 'http://my.oschina.net/img/portrait.gif?t=1451961935000'::character varying,
  "contactidlist" json NOT NULL DEFAULT '[]'::json,
  "picturelist" json NOT NULL DEFAULT '[]'::json,
  "tag" varchar(50) COLLATE "pg_catalog"."default"
)
;
ALTER TABLE "sys"."apijson_user" OWNER TO "postgres";

-- ----------------------------
-- Records of apijson_user
-- ----------------------------
BEGIN;
INSERT INTO "sys"."apijson_user" VALUES (38710, 'TommyLemon', 0, '2017-02-01 19:21:50', 'http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000', '[82003, 82005, 90814, 82004, 82009, 82002, 82044, 93793, 70793]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]', 'Android&Java');
INSERT INTO "sys"."apijson_user" VALUES (82002, 'Happy~', 1, '2017-02-01 19:21:50', 'http://static.oschina.net/uploads/user/1174/2348263_50.png?t=1439773471000', '[82005, 82001, 38710]', '[]', 'iOS');
INSERT INTO "sys"."apijson_user" VALUES (82001, 'Test', 0, '2017-02-01 19:21:50', 'https://static.oschina.net/uploads/user/19/39085_50.jpg', '[82030, 82025, 82003, 93793, 82006, 1520242280259, 82005, 82024, 1531969715979]', '["http://common.cnblogs.com/images/icon_weibo_24.png"]', 'APIJSON User');
INSERT INTO "sys"."apijson_user" VALUES (70793, 'Strong', 0, '2017-02-01 19:21:50', 'http://static.oschina.net/uploads/user/585/1170143_50.jpg?t=1390226446000', '[38710, 82002]', '["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg", "http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg", "https://camo.githubusercontent.com/788c0a7e11a", "https://camo.githubusercontent.com/f513f67"]', 'djdj');
COMMIT;

-- ----------------------------
-- Table structure for moment
-- ----------------------------
DROP TABLE IF EXISTS "sys"."moment";
CREATE TABLE "sys"."moment" (
  "id" int8 NOT NULL,
  "content" varchar(1000) COLLATE "pg_catalog"."default",
  "userid" int8 NOT NULL DEFAULT 0,
  "praiseuseridlist" json NOT NULL DEFAULT '[]'::json,
  "picturelist" json NOT NULL DEFAULT '[]'::json,
  "date" timestamp(6)
)
;
ALTER TABLE "sys"."moment" OWNER TO "postgres";

-- ----------------------------
-- Records of moment
-- ----------------------------
BEGIN;
INSERT INTO "sys"."moment" VALUES (12, '1111534034', 70793, '[70793, 93793, 82044, 82040, 82055, 90814, 38710, 82002, 82006, 1508072105320, 82001]', '["http://static.oschina.net/uploads/img/201604/22172508_eGDi.jpg", "http://static.oschina.net/uploads/img/201604/22172507_rrZ5.jpg", "https://camo.githubusercontent.com/788c0a7e11a4f5aadef3c886f028c79b4808613a/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343932353935372d313732303737333630382e6a7067", "http://static.oschina.net/uploads/img/201604/22172507_Pz9Y.png", "https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067", "https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067", "https://camo.githubusercontent.com/c98b1c86af136745cc4626c6ece830f76de9ee83/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343930383036362d313837323233393236352e6a7067", "https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067"]', '2017-02-08 08:06:11');
INSERT INTO "sys"."moment" VALUES (15, 'APIJSON is a JSON Transmission Structure Protocol…', 70793, '[82055, 82002, 38710]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]', '2017-02-08 08:06:11');
INSERT INTO "sys"."moment" VALUES (32, NULL, 82002, '[38710, 82002, 82001]', '["https://camo.githubusercontent.com/f513fa631bd780dc0ec3cf2663777e356dc3664f/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343733323232332d3337333933303233322e6a7067", "https://camo.githubusercontent.com/5f5c4e0c4dc539c34e8eae8ac0cbc6dccdfee5d3/687474703a2f2f696d61676573323031352e636e626c6f67732e636f6d2f626c6f672f3636303036372f3230313630342f3636303036372d32303136303431343232343533333831362d323032373434343231382e6a7067", "http://static.oschina.net/uploads/img/201604/22172508_mpwj.jpg"]', '2017-02-08 08:06:11');
INSERT INTO "sys"."moment" VALUES (1531969364022, 'APIJSON, let interfaces go to hell!', 82001, '[82001, 1532188114543, 1534926301956]', '["http://static.oschina.net/uploads/user/1218/2437072_100.jpg?t=1461076033000", "http://common.cnblogs.com/images/icon_weibo_24.png"]', '2018-07-19 11:02:44');
INSERT INTO "sys"."moment" VALUES (1512314438990, 'APIJSON iOS-Swift版发布，自动生成请求代码,欢迎使用^_^   https://github.com/TommyLemon/APIJSON', 38710, '[82001, 82002, 70793, 1512531601485]', '["https://images2018.cnblogs.com/blog/660067/201712/660067-20171203231829476-1202860128.jpg"]', '2017-12-03 23:20:38');
COMMIT;

-- ----------------------------
-- Primary Key structure for table apijson_user
-- ----------------------------
ALTER TABLE "sys"."apijson_user" ADD CONSTRAINT "apijson_user_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table moment
-- ----------------------------
ALTER TABLE "sys"."moment" ADD CONSTRAINT "moment_pkey" PRIMARY KEY ("id");
