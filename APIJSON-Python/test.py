#!/usr/bin/env python
import urllib2
import json

print '\n{APIJSON @ Python}'

url = 'http://apijson.cn:8080/get'
print '\nURL: ' + url

data = {
    '[]': {
        'User': {
            'sex': 1
        },
        'count': 2
    }
}
print '\nRequest:\n' + json.dumps(data, indent=2)

headers = {'Content-Type': 'application/json'}
request = urllib2.Request(url=url, headers=headers, data=json.dumps(data))
response = urllib2.urlopen(request)

print '\nResponse:\n' + json.dumps(json.loads(response.read()), indent=2)