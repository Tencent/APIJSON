package apijson.demo.server.model;

import zuo.biao.apijson.MethodAccess;

import static zuo.biao.apijson.RequestRole.*;

/**
 * @author hjt
 * @date 2018/8/28
 * @description
 */
@MethodAccess(
        POST = {UNKNOWN, ADMIN},
        DELETE = {UNKNOWN,ADMIN,LOGIN,CONTACT,CIRCLE,OWNER}
)
public class Contract {

}
