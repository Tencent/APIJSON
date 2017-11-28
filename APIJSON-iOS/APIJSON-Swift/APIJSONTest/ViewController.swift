//
//  ViewController.swift
//  testswift
//
//  Created by Tommy on 17/11/28.
//  Copyright © 2017年 APIJSON. All rights reserved.
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
    
        //生成UI <<<<<<<<<<<<<<<<<<<<<<
    
        let requestLabel = UILabel(frame:CGRect(x:20, y:10, width:400, height:130))
        requestLabel.text = "Request:\n{\n  \"User\": {\n    \"sex\": 1\n  }\n}"
        requestLabel.numberOfLines = 6
        self.view.addSubview(requestLabel)
        
        let responseLable = UILabel(frame:CGRect(x:20, y:130, width:400, height:600))
        responseLable.text = "request..."
        responseLable.numberOfLines = 100
        self.view.addSubview(responseLable)
        
        //生成UI >>>>>>>>>>>>>>>>>>>>>
        
        
        
        
        print("start http request...\n")
        
        //要发送的请求数据
        let json = [
            //返回数据太长 "[]": [
                "User": [
                    "sex": 1
                ]
            //]
        ]

        
        //请求URL
        let url:NSURL! = NSURL(string: "http://39.108.143.172:8080/get")
        
        let request:NSMutableURLRequest = NSMutableURLRequest(URL: url)
        
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
           
            print("received result!\n\n")

            print(data)
            print(response)
            print(error)
            
            //数据类型转换
            let jsonData:NSDictionary = try! NSJSONSerialization.JSONObjectWithData(data!, options: .MutableContainers) as! NSDictionary
            
            print(jsonData)
            
            let data : NSData! = try? NSJSONSerialization.dataWithJSONObject(jsonData, options: [NSJSONWritingOptions.PrettyPrinted]) as NSData!
            let str = String(data: data, encoding: NSUTF8StringEncoding)
            print("str = \n" + str!)
            
            
            //显示返回结果
            dispatch_async(dispatch_get_main_queue(), {
                
                responseLable.text = "Response:\n" + str!
                print("set text end\n\n")
            });
            
        }
        
        //请求开始
        dataTask.resume()
    }
    

}

