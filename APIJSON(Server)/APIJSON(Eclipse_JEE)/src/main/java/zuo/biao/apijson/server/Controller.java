/*Copyright ©2016 TommyLemon(https://github.com/TommyLemon/APIJSON)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package zuo.biao.apijson.server;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;

import zuo.biao.apijson.JSON;

/**request receiver and controller
 * @author Lemon
 */
@RestController
@RequestMapping("")
public class Controller {

    @RequestMapping("get/{request}")
    public String get(@PathVariable String request) {
        System.out.println("request = " + request);
        JSONObject object = new RequestParser().parse(request);
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
