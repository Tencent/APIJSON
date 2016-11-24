package zuo.biao.apijson.server;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;
import zuo.biao.apijson.JSON;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static zuo.biao.apijson.StringUtil.UTF_8;

/**
 * @author Lemon
 */
@RestController
@RequestMapping("")
public class UserController {

    @RequestMapping("get/{request}")
    public String get(@PathVariable String request) {
        try {
            request = URLDecoder.decode(request, UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("request = " + request);
        JSONObject object = new ServerGet2().get(request);
        System.out.println("return " + JSON.toJSONString(object));
        return JSON.toJSONString(object);
    }

//    @RequestMapping(value="/request",produces="application/json")
//    public String get2(@RequestBody JSONObject request){
//        return JSON.toJSONString(request);
//    }
//    @RequestMapping(method = RequestMethod.GET)
//    public String create(@RequestBody @Valid JSONObject request) {
//        return JSON.toJSONString(request);
//    }

}
