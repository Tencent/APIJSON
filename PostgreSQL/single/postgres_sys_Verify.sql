create table "Verify"
(
    id     bigint                                 not null
        constraint "Verify_pkey"
            primary key,
    type   integer                                not null,
    phone  varchar(11)                            not null,
    verify varchar(32)                            not null,
    date   timestamp(6) default CURRENT_TIMESTAMP not null
);

comment on column "Verify".id is '唯一标识';

comment on column "Verify".type is '类型：
0-登录
1-注册
2-修改登录密码
3-修改支付密码';

comment on column "Verify".phone is '手机号';

comment on column "Verify".verify is '验证码';

comment on column "Verify".date is '创建时间';

alter table "Verify"
    owner to postgres;

INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1527950171719, 1, '13000084444', '10375', '2018-06-02 14:36:11.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528250810515, 1, '15122820115', '2586', '2018-06-06 02:06:50.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528254139866, 1, '15225556855', '8912', '2018-06-06 03:02:19.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528255485691, 1, '15822798927', '2101', '2018-06-06 03:24:45.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528264687329, 1, '15620878773', '3991', '2018-06-06 05:58:07.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528269508031, 1, '18616024605', '4901', '2018-06-06 07:18:28.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528289406640, 1, '13142033348', '3005', '2018-06-06 12:50:06.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528327843188, 0, '15122820115', '4912', '2018-06-06 23:30:43.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528330720259, 2, '15122820115', '5267', '2018-06-07 00:18:40.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528339646013, 1, '15122541683', '6112', '2018-06-07 02:47:26.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528344962707, 1, '15188899797', '4540', '2018-06-07 04:16:02.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528345364195, 2, '15122541683', '10500', '2018-06-07 04:22:44.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528345372998, 0, '15122541683', '9940', '2018-06-07 04:22:53.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528356342784, 2, '15620878773', '2076', '2018-06-07 07:25:42.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528356449927, 1, '15620878772', '4733', '2018-06-07 07:27:29.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1528411937273, 0, '15620878772', '7375', '2018-06-07 22:52:17.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1531793525394, 1, '15629184762', '9737', '2018-07-17 02:12:05.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1531969702694, 1, '13800138000', '8213', '2018-07-19 03:08:22.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1531983017848, 2, '13800138000', '1552', '2018-07-19 06:50:17.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1532188103364, 1, '13977757845', '8753', '2018-07-21 15:48:23.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1532439015405, 1, '18779607703', '10136', '2018-07-24 13:30:15.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1533450371245, 1, '18911061423', '5795', '2018-08-05 06:26:11.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1533618759900, 1, '13977757843', '10204', '2018-08-07 05:12:39.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1533627819054, 1, '13107695518', '7515', '2018-08-07 07:43:39.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1533693421836, 1, '15901373410', '4884', '2018-08-08 01:57:01.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1533698902309, 1, '18664900086', '3654', '2018-08-08 03:28:22.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1533723898511, 1, '8881816', '5272', '2018-08-08 10:24:58.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1533835163777, 1, '13977757846', '9332', '2018-08-09 17:19:23.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1534142797624, 2, '13977757845', '4136', '2018-08-13 06:46:37.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1534142802751, 0, '13977757845', '5754', '2018-08-13 06:46:42.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1534671951719, 1, '13000082023', '5869', '2018-08-19 09:45:51.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1534671960833, 1, '13000082013', '6169', '2018-08-19 09:46:00.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1534671980295, 1, '13000082032', '10171', '2018-08-19 09:46:20.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1534672028733, 0, '13000093793', '6478', '2018-08-19 09:47:08.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1534898613829, 1, '17755531490', '3961', '2018-08-22 00:43:33.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1534926287534, 1, '17602120205', '5297', '2018-08-22 08:24:47.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1535345181813, 1, '13000082022', '8145', '2018-08-27 04:46:21.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1536220749108, 1, '13241042199', '9916', '2018-09-06 07:59:09.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1536583466723, 1, '18013819609', '2483', '2018-09-10 12:44:26.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1536805630726, 0, '13000070793', '1798', '2018-09-13 02:27:10.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1537515268266, 2, '13000038710', '4171', '2018-09-21 07:34:28.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1537515280163, 2, '13000038713', '5877', '2018-09-21 07:34:40.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1537515287973, 2, '13000038714', '10441', '2018-09-21 07:34:47.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1537515337629, 1, '13000033333', '7353', '2018-09-21 07:35:37.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1537521279290, 1, '13000049499', '2854', '2018-09-21 09:14:39.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1538114970328, 1, '15855512382', '10359', '2018-09-28 06:09:30.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1538504245612, 1, '13000087654', '10791', '2018-10-02 18:17:25.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1538504485798, 1, '13000087655', '4776', '2018-10-02 18:21:25.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1538987940551, 1, '18662327672', '1800', '2018-10-08 08:39:00.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1539076064496, 1, '15094295280', '3361', '2018-10-09 09:07:44.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1539076102284, 1, '15094395280', '1562', '2018-10-09 09:08:22.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1540364623910, 1, '13000085001', '4382', '2018-10-24 07:03:43.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1540966375865, 1, '13122091271', '3880', '2018-10-31 06:12:55.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1541427928139, 0, '13759127249', '5913', '2018-11-05 14:25:28.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1541500666143, 1, '15280239960', '9142', '2018-11-06 10:37:46.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1541510152560, 1, '13000099999', '1097', '2018-11-06 13:15:52.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1541510270190, 1, '13000077777', '8192', '2018-11-06 13:17:50.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1541583746916, 1, '18689846285', '8724', '2018-11-07 09:42:26.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1541724338244, 0, '13000038710', '8283', '2018-11-09 00:45:38.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1541757538733, 1, '17717112856', '2268', '2018-11-09 09:58:58.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1542261432641, 1, '15800506515', '2586', '2018-11-15 05:57:12.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1542265654497, 1, '18010001000', '5666', '2018-11-15 07:07:34.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1542337959344, 1, '13000012345', '4981', '2018-11-16 03:12:39.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1542352629436, 1, '13000082002', '10212', '2018-11-16 07:17:09.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1542548523509, 1, '13317833374', '8921', '2018-11-18 13:42:03.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1542702268195, 1, '123123', '8055', '2018-11-20 08:24:28.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1542840424025, 1, '13818118257', '7126', '2018-11-21 22:47:04.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1543377157089, 1, '18622250185', '6620', '2018-11-28 03:52:37.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1543966631575, 1, '13466260815', '5835', '2018-12-04 23:37:11.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1544276193603, 1, '13000087656', '4078', '2018-12-08 13:36:33.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1544276277509, 2, '13000087656', '9356', '2018-12-08 13:37:57.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1544276305800, 0, '13000087656', '8708', '2018-12-08 13:38:25.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1544276475231, 1, '15988125475', '8940', '2018-12-08 13:41:15.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1544503797981, 1, '13000082968', '6965', '2018-12-11 04:49:57.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1545238881566, 1, '13166059778', '4434', '2018-12-19 17:01:21.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1545269417538, 1, '18124099720', '4882', '2018-12-20 01:30:17.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1545464407106, 1, '17755015200', '3870', '2018-12-22 07:40:07.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1545707514503, 1, '13533039558', '3941', '2018-12-25 03:11:54.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1545895656481, 1, '13533039550', '4968', '2018-12-27 07:27:36.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1546701633801, 1, '13534201057', '8487', '2019-01-05 15:20:33.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1547177422559, 1, '18980210241', '7012', '2019-01-11 03:30:22.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1548068010027, 1, '17181595855', '10716', '2019-01-21 10:53:30.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1548310439111, 1, '13059203278', '9438', '2019-01-24 06:13:59.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1548398132694, 1, '15050529772', '9276', '2019-01-25 06:35:32.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1548742004597, 1, '13738007826', '6318', '2019-01-29 06:06:44.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1548742060130, 1, '13000082001', '8387', '2019-01-29 06:07:40.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1548742124507, 6, '13000082001', '4901', '2019-01-29 06:08:44.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1548742151361, 10, '13000082001', '8513', '2019-01-29 06:09:11.000000');
INSERT INTO sys."Verify" (id, type, phone, verify, date) VALUES (1549548031095, 4, '13000082001', '1234', '2019-09-14 22:00:31.115000');