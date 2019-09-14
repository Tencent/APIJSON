create table apijson_privacy
(
    id             bigint         not null
        constraint apijson_privacy_pkey
            primary key,
    certified      smallint       not null,
    phone          varchar(64)    not null,
    balance        numeric(10, 2) not null,
    _password      varchar(20)    not null,
    "_payPassword" varchar(32)    not null
);

comment on table apijson_privacy is '用户隐私信息表。
对安全要求高，不想泄漏真实名称。对外名称为 Privacy';

comment on column apijson_privacy.id is '唯一标识';

comment on column apijson_privacy.certified is '已认证';

comment on column apijson_privacy.phone is '手机号，仅支持 11 位数的。不支持 +86 这种国家地区开头的。如果要支持就改为 VARCHAR(14)';

comment on column apijson_privacy.balance is '余额';

comment on column apijson_privacy._password is '登录密码';

comment on column apijson_privacy."_payPassword" is '支付密码';

alter table apijson_privacy
    owner to postgres;

create index "phone_UNIQUE"
    on apijson_privacy (phone);

INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (38710, 1, '13000038710', 33376.00, 'apijson', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (70793, 0, '13000070793', 56000.00, 'apijson', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82001, 1, '13000082001', 98826.90, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82002, 1, '13000082002', 6817.23, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82003, 1, '13000082003', 2000.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82004, 0, '13000082004', 2000.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82005, 0, '13000082005', 1923.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82006, 0, '13000082006', 2000.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82009, 0, '13000082009', 2000.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82012, 0, '13000082012', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82020, 0, '12345678900', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82021, 0, '12345678901', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82022, 0, '12345678902', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82023, 0, '12345678903', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82024, 0, '12345678904', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82025, 0, '12345678905', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82026, 0, '12345678906', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82027, 0, '12345678907', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82028, 0, '12345678908', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82029, 0, '12345678909', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82030, 0, '12345678910', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82031, 0, '12345678911', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82032, 0, '12345678912', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82033, 0, '12345678913', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82034, 0, '12345678914', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82035, 0, '12345678915', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82036, 0, '12345678916', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82037, 0, '12345678917', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82038, 0, '12345678918', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82039, 0, '12345678919', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82040, 0, '13000082019', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82041, 0, '13000082015', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82042, 0, '13000082016', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82043, 0, '13000082017', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82044, 0, '13000082018', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82045, 0, '13000082020', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82046, 0, '13000082010', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82047, 0, '13000082021', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82048, 0, '13000038711', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82049, 0, '13000038712', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82050, 0, '13000038713', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82051, 0, '13000038714', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82052, 0, '13000038715', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82053, 0, '13000038720', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82054, 0, '13000038721', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82055, 0, '13000082030', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82056, 0, '13000082040', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82057, 0, '13000038730', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82058, 0, '13000038750', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82059, 0, '13000082033', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (82060, 0, '13000082050', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (90814, 1, '13000090814', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (93793, 1, '13000093793', 3000.00, 'apijson', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (93794, 0, '99999999999', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1490109742863, 0, '13000082100', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1490109845208, 0, '13000082101', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1490420651686, 0, '13000038716', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1490427139175, 0, '13000038717', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1490427577823, 0, '13000082102', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1490584952968, 0, '13000038790', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1490973670928, 0, '13000082051', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1492936169722, 0, '13000093794', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1493480142628, 0, '13000038888', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1493747512860, 0, '13000038777', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1493747777770, 0, '13000038778', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1493748594003, 0, '13000038779', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1493748615711, 0, '13000038780', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1493749090643, 0, '13000038781', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1493836043151, 0, '13000038999', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1493883110132, 0, '13000039999', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1493890214167, 0, '13000031000', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1493890303473, 0, '13000031001', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1493890303474, 0, '13000088888', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1497792972314, 0, '13000082111', 0.00, '654321', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1499057230629, 0, '13000082011', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1500825221910, 0, '13000099999', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1502639062900, 0, '13000082222', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1502639424119, 0, '13000082333', 0.00, '12345678', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1507220582167, 0, '13000011111', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1508072071492, 0, '13000071492', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1508072105320, 0, '13000082008', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1508072160401, 0, '13000082007', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1508072202871, 0, '13000082031', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1510495628760, 0, '13000082000', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1511407581570, 0, '17610725819', 0.00, '123123', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1511761906715, 0, '13708222312', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1511965911349, 0, '13000083333', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1512387063078, 0, '15858585858', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1512531601485, 0, '18210847727', 0.00, '5816136', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1514623064133, 0, '13000038725', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1514625918255, 0, '13000038726', 255.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1514626163032, 0, '13000038727', 4951.37, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1514858422969, 0, '13000082041', 164.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1515565976140, 0, '15009257563', 0.00, 'qazwsx', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1518218350585, 0, '18663689263', 0.00, 'cherish751220', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1519778917280, 0, '15000536915', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1520242280259, 0, '18917212395', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1521274648008, 0, '18989491914', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1521371722416, 0, '13000088889', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1521374327542, 0, '13000056789', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1523626157302, 0, '15603313259', 0.00, '15603313259', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1523935772553, 0, '15603313258', 0.00, '15603313258', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1524042900591, 0, '15222297100', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1524298730523, 0, '17854217949', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1524878698882, 0, '13917451840', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1525677515673, 0, '13390935538', 10000.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1527495857924, 0, '13142033345', 15.00, 'qweasd', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1527498229991, 0, '13142033342', 0.00, 'qweasd', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1527821445610, 0, '13142033346', 0.00, 'qweasd', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1528250827953, 0, '15122820115', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1528254173621, 0, '15225556855', 200.00, 'lmt970208', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1528255497767, 0, '15822798927', 0.00, '111111', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1528264711016, 0, '15620878773', 0.00, '111111', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1528339692804, 0, '15122541683', 0.00, '568599', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1528344980598, 0, '15188899797', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1528356470041, 0, '15620878772', 0.00, '111111', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1531969715979, 0, '13800138000', 10000.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1532188114543, 0, '13977757845', 20360.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1532439021068, 0, '18779607703', 0.00, '15879684798qq', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1533835176109, 0, '13977757846', 1700.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1534926301956, 0, '17602120205', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1538504264944, 0, '13000087654', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1538504500574, 0, '13000087655', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1538987952996, 0, '18662327672', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1541583762603, 0, '18689846285', 0.00, 'jyt123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1544276209348, 0, '13000087656', 1050.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1544503822963, 0, '13000082968', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1545707526805, 0, '13533039558', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1545895694424, 0, '13533039550', 357.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1547177436600, 0, '18980210241', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1548068043688, 0, '17181595855', 0.00, '0812563993gg', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1553095415917, 0, '13185236871', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1553527700480, 0, '13189758117', 0.00, '3802489', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1559129626356, 0, '13000000000', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1560409157504, 0, '18030546471', 0.00, '123456789', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1561539257158, 0, '15870873323', 0.00, '123qwe', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1563605318975, 0, '13590330481', 0.00, '123456', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1566064943195, 0, '13111182001', 0.00, '6733', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1566065271307, 0, '13111182002', 0.00, '6733', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1566065319823, 0, '13111182003', 0.00, '6733', '123456');
INSERT INTO sys.apijson_privacy (id, certified, phone, balance, _password, "_payPassword") VALUES (1566065621308, 0, '13111182008', 0.00, '6733', '123456');