//
//  ViewController.swift
//  testswift
//
//  Created by TommyLemon on 17/11/28.
//  Copyright © 2017年 https://github.com/TommyLemon/APIJSON . All rights reserved.
//

import UIKit

class ViewController: UIViewController {

    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        
        
        test()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    
    
    /**
     * 通过POST请求测试APIJSON
     */
    func test() {
        
        let url = "http://apijson.cn:8080/get";
        //要发送的请求数据
        let json = [
            //返回数据太长 "[]": [
            "User": [ //如果对象value是空的，请用[:]表示value，否则会被Swift解析为空数组[]，而不是空对象{}
                "sex": 1
            ]
            //]
        ]

        let req = toJSONString(json);

        print("start http request...\n\nURL = " + url + "\nRequest = \n" + req)
        
        //生成UI <<<<<<<<<<<<<<<<<<<<<<
        
        
        let requestLabel = UILabel(frame:CGRect(x:20, y:10, width:400, height:130))
        requestLabel.text = "Request:\n" + req;
        requestLabel.numberOfLines = 6
        self.view.addSubview(requestLabel)
        
        let responseLable = UILabel(frame:CGRect(x:20, y:130, width:400, height:600))
        responseLable.text = "request..."
        responseLable.numberOfLines = 100
        self.view.addSubview(responseLable)
        
        //生成UI >>>>>>>>>>>>>>>>>>>>>
        
    
        

        
        //请求URL
        
        let request:NSMutableURLRequest = NSMutableURLRequest(URL: NSURL(string: url)!)
        
        request.HTTPMethod = "POST"
        //设置发送的数据格式为JSON
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        do {
            request.HTTPBody = try NSJSONSerialization.dataWithJSONObject(json, options: NSJSONWritingOptions.PrettyPrinted)
        } catch {
            print("Something went wrong!")
        }
        
        //默认session配置
        let config = NSURLSessionConfiguration.defaultSessionConfiguration()
        let session = NSURLSession(configuration: config)
        
        //发起请求
        let dataTask = session.dataTaskWithRequest(request) { (data, response, error) in
           
            print("\n\nreceived result!\n\n")

            print(data)
            print(response)
            print(error)
            
            //数据类型转换
            let jsonData:NSDictionary = try! NSJSONSerialization.JSONObjectWithData(data!, options: .MutableContainers) as! NSDictionary
            
            print(jsonData)
            
            let res:String = self.toJSONString(jsonData);
            print("Response = \n" + res)
            
            
            //显示返回结果
            dispatch_async(dispatch_get_main_queue(), {
                
                responseLable.text = "Response:\n" + res
                print("set text end\n\n")
            });
            
        }
        
        //请求开始
        dataTask.resume()
    }
    
    func toJSONString(jsonData: NSDictionary!) -> String {
        let data : NSData! = try? NSJSONSerialization.dataWithJSONObject(jsonData, options: [NSJSONWritingOptions.PrettyPrinted]) as NSData!
        let str = String(data: data, encoding: NSUTF8StringEncoding)
        return str!
    }
    

}

