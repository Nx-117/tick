
###### Tick定时推送

# 添加/删除定时任务接口

### 请求地址
##### Post请求 Json格式传输
##### http://localhost:7890/tick 

### 接口参数

| 参数名称  | 必填 |参数定义 | 类型  | 示例  | 备注  |
| ------------ | ------------ |------------ | ------------ | ------------ |
|  notifyUrl |  是 | 回调地址| string  | http://localhost:8082/tick | 申请本接口服务必须开通 tick get请求接口
|  isDel |  否 | 是否删除操作 | int| 1是 0否  传空时为0 |
|  setList |  是 |数据集合| list  | 详情见下表  |

### setList 参数

| 参数名称  | 必填  | | 类型  | 示例  | 备注  |
| ------------ | ------------ |------------ | ------------ | ------------ |
|  key |  是 | key值  |string| adfsfsfsdfsdf1231 |
|  timeOut | 是 | 定时时间  |Lang| 100 |  单位秒


### 请求示例

    {
    	"notifyUrl":"http://localhost:8082/tick",
    	"isDel":0
    	"setList":[
    		{"key":"k1",
    			"timeOut":10
    		},
    			{"key":"k2",
    			"timeOut":20
    		},	{"key":"k3",
    			"timeOut":15
    		}
    		]
    	
    }

### 响应参数
| 参数名称  | 必填 |参数定义 | 类型  | 示例  | 备注  |
| ------------ | ------------ |------------ | ------------ | ------------ |
|  message |  是 | 响应信息| string  | success | 
|  code | 是 | 状态码 | int| 200 | 200为成功   其余状态码均为失败
|  data |  是 |数据集合| list  |   | 用于展示数据是否添加成功

### 响应示例

true为添加成功，flase为添加失败

    {
        "message": "success",
        "data": {
            "k1": true,
            "k2": true,
            "k3": true
        },
        "code": 200
    }


# 推送信息
#### 订阅服务需开通tick get请求接口，用于接受信息
##### 示例 
http://localhost:8082/test?key=12312
