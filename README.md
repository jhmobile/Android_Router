# Android_Router

### 通过一个url地址访问Android页面</p>
## 一，实现的内容
1.支持嵌套跳转，解决配置文件重复配置   例： 跳购买需要登录绑卡，绑卡需要登录，购买前置条件只需要配置绑卡。考虑方案：多层嵌套之间实现父子关系关联。</p>
2.跳转采用链式调用。</p>
3.跳转路由采用分组管理</p>
4.支持传递自定义Serializable对象</p>
5.增加跳转结果的回调</p>
6.增加打开目标页的方式，1，startActivity 2，startActivityForResult</p>
7.动画配置

## 二，配置文件格式及使用介绍

### 1.初级配置文件格式事例：
<pre><code>
    {
        "/deal": {//路由
            "interceptors": [//路由对应拦截器数组
              "(文件路径名).LoginInterceptor",
              "(文件路径名).BindCardInterceptor"
            ],
            "subRoutes": {//子路由对象
              "/buy": {//路由
                "interceptors": [
                  "(文件路径名).ProductRiskInterceptor"
                ],
                "taskClass":"(文件路径名).ActivityTask",//路由目标执行类
                "subRoutes": {
                  "/detail?type=spirit&rateType=unfix": {
                    "taskOptions": {//目标执行类配置信息
                      "clazz": "(文件路径名).BuyCurrentActivity",
                      "arriveType": "spirit",
                      "index:": "1"
                    }
                  },
                  "/detail?type=opencashNew": {
                    "taskOptions": {
                      "clazz": "(文件路径名).BuyCoolCurrentActivity",
                      "arriveType": "opencashNew"
                    }
                  },
                  "/detail": {
                    "interceptors": [
                      "(文件路径名).buy.InvestorWebViewInterceptor"
                    ],
                    "taskOptions": "(文件路径名).BuyFixedActivity"
                  }
                }
              },
              "/withdraw": "(文件路径名).WithdrawCurrentActivity"
            }
          },
          "/login": "(文件路径名).UserLoginActivity",
    }
</code></pre>
### 2.配置文件key字段解释表
#### 1>.Router：

|属性名|是否可选|类型|默认属性|说明|
|:-----------:|:-----------:|:-----------:|:-----------:|:-----------:|
|subRoutes|是|object|无|路由的集合，是一个map对象，每一个key代表当前层的一个路由|
|interceptors|是|Array|class|拦截器数组，里面的内容为拦截器类的集合|
|taskClass|是|String|ActivityTaskClass|路由任务处理类名  默认是库内的Activity跳转任务执行类|
|taskOptions|否|Object|class|路由任务处理类的配置项，默认是处理的目标类的类名|


#### 2>.taskOptions：

|属性名|是否可选|类型|默认属性|说明|
|:-----------:|:-----------:|:-----------:|:-----------:|:-----------:|
|class|否|String|无|目标类类名，该字段是指当前路由任务处理的目标类，例如跳转activity的目标activity类|
|options|是|Object|无|该字段只有目标类（class）是activity跳转的时候配置|
|.....|是|String|无|路由任务处理类的任意处理参数|


### 3.高级配置文件使用

##### 如果只是一个拦截器类名，不能满足业务需求，可以丰富拦截器的字段，如下

#### 1>.高级配置文件格式事例：
      {
              "/deal": {//路由
                  "interceptors": [//路由对应拦截器数组
                  {
                        "class":"(文件路径名).LoginInterceptor",
                        "options":{}
                  },
                   {
                        "class":"(文件路径名).BindCardInterceptor",
                        "options":{}
                   }
                  ],
                  "subRoutes": {//子路由对象
                    "/buy": {//路由
                      "interceptors": [
                       {
                             "class":"(文件路径名).buy.ProductRiskInterceptor",
                              "options":{}
                       }
                      ],
                      "taskClass":"(文件路径名).ActivityTask",//路由目标执行类
                      "subRoutes": {
                        "/detail?type=spirit&rateType=unfix": {
                          "taskOptions": {//目标执行类配置信息
                            "clazz": "(文件路径名).BuyCurrentActivity",
                            "arriveType": "spirit",
                            "index:": "1"
                          }
                        }
                        "/detail": {
                          "interceptors": [
                          {
                                "class":"(文件路径名).InvestorWebViewInterceptor",
                                 "options":{
                                    "state":"-1",
                                    "url1":"a/b/v",
                                    "url2":"a/b/c"
                                 }
                           }
                          ],
                          "taskOptions": "(文件路径名).BuyFixedActivity"
                        }
                      }
                    },
                    "/withdraw": "(文件路径名).WithdrawCurrentActivity"
                  }
                },
                "/login": "(文件路径名).UserLoginActivity",
          }

#### 2>.高级路由需要丰富字段表interceptors：

|属性名|是否可选|类型|默认属性|说明|
|:-----------:|:-----------:|:-----------:|:-----------:|:-----------:|
|class|否|String|无|拦截器的类名|
|options|是|Object|无|拦截器需要的配置参数|

## 三，API介绍及使用实例

### 1，IRouteCallBack
#### IRouteCallBack接口的作用是设置整个路由跳转过程中的各种状态的回调处理，包含：onSuccess(RouteResponse response) 和 onFail(RouteResponse response)两个回调方法，它的使用，可以是在一次路由跳转过程中进行设置，也可以直接初始化的时候进行设置，默认不设置就是不做任何处理；使用方式如下：
##### 1>.初始化时整体设置，使用于整个项目：
        RouteManager.getInstance().initialize(configJsonString,new IRouteCallBack(){
            @Override
            public void onSuccess(RouteResponse response){
                //成功的处理
            }

            @Override
            public void onFail(RouteResponse response){
                //失败的处理
            }
        },new ActivityTask());

##### 2>.单个路由运行时设置，只使用于当前路由：
       RouteManager.getInstance().build("/deal/buy-detail")
             .withParams("type","shb")
             .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP)
             .callback(callback)//实现IRouteCallBack接口，进行成功，错误设置
             .go(context);
       RouteManager.getInstance().build("/deal/buy-detail")
             .withParams("type","shb")
             .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP)
             .go(context,callBack);// callback即为IRouteCallBack接口的实现

### 2，IRouteTask
#### IRouteTask 接口的作用是重新构建一个路由任务执行类，实现类的运用可以在配置配置文件中添加，也可在代码中设置，默认不设置是实现库的activity跳转任务执行类，使用方式如下：
##### 1>.在配置文件中添加，即通过taskClass字段配置，根据业务需要配置指定的地方，没有配置的地方，默认依然是使用库中的activity跳转任务执行类
##### 2>.在代码中配置，即通过初始化的时候配置进去，使用于整个项目。
       RouteManager.getInstance().initialize(configJsonString,routeCallBack,new ViewTask());
       // ViewTask 类即为IRouteTask接口的具体实现类

### 3，mergeConfig(String jsonString)
#### String jsonString:配置文件转化成的json String 数据
#### mergeConfig方法属于RouteManager类，用于合并更新部分配置文件，如果项目没有动态更新配置文件的功能，该方法可以忽略，不做了解。使用方式如下：
      //在获取的更新的配置文件内容之后，直接调用：
      RouteManager.getInstance().mergeConfig(jsonConfig)

### 4，build(String path)
#### String path:路由地址，项目内可以使相对地址，项目外必须是scheme:host+url完整地址
#### build方法属于RouteManager类，是一次路由的入口，用于构建一次路由对饮的Uri，它需要和go()方法一起使用下面在够方法里面列举他对应的使用实例

### 5，go(Context context),go(Context context, IRouteCallBack callback)
#### Context context 当前上下文
#### IRouteCallBack callback：跳转结果回调接口实现类
#### go方法属于RouteContextBuilder类，是一次路由跳转的最终构建，它需要和build方法结合使用，使用方式如下：
     RouteManager.getInstance().build("/login")
            .go(context);

### 6，callback(IRouteCallBack callback)
#### IRouteCallBack callback：跳转结果回调接口实现类
#### callback方法属于RouteContextBuilder类，用于在跳转过程中设置跳转状态的回调，具体事例看IRouteCallBack接口的介绍实例

### 7，requestCode(int requestCode)
#### int requestCode:当前页面接收forResult的code值
#### requestCode方法属于RouteContextBuilder类，通过添加不同的requestCode值来实现源页面对目标页状态的接收，使用方式如下：
      RouteManager.getInstance().build("/deal/buy-detail")
              .requestCode(100)//传入的值不为-1时，默认activity任务实现类是实现startActivityForResult启动页面，可以在源页面的onActivityResult里面进行处理
              .go(context);

### 8，withParams(String key, Object value), withParams(Map<String, Object> map),withParams(String json)
#### withParams方法属于RouteContextBuilder类，用于动态向路由的目标页传递参数，
    RouteManager.getInstance().build("/deal/buy-detail")
           .withParams(params)//动态添加的参数，添加参数支持多种格式，withParams(key,value),withParams(map),withParams(jsonString)
           .go(context);

### 9，addFlags(int flags)
#### addFlags方法属于RouteContextBuilder类，用于设置不同的页面启动标识，例如，activity的启动设置，使用方式如下：
      RouteManager.getInstance().build("/deal/buy-detail")
                .withParams(params)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP)//清空原页面之上的所以页面，构建目标页
                .go(context);

### 10，anim(@AnimRes int enterAnim, @AnimRes int exitAnim)
#### int enterAnim:进入目标页动画id
#### int exitAnim:退出目标页动画id
#### anim方法属于RouteContextBuilder类，用于指定目标页进入和退出的动画，使用方式如下：
    RouteManager.getInstance().build("/deal/buy-detail")
                .anim(enterAnim,exitAnim)
                .go(context);

### 11，skipInterceptors()
#### skipInterceptors方法属于RouteContextBuilder类，用于表达动态跳过所有拦截器，如果跳转过程中使用该方法，则后面无论添加或者跳过指定拦截器都无效。使用方法如下：
     RouteManager.getInstance().build("/deal/buy-detail")
                 .skipInterceptors()//跳过所有拦截器
                 .go(context);

### 12，skipInterceptors(Class<AbsInterceptor>... interceptors)
#### Class<AbsInterceptor>... interceptors:拦截器的class文件
#### skipInterceptors方法属于RouteContextBuilder类，用于动态指定跳过的拦截器，可以链式一个一个添加，也可以通过不定参数指定多个，使用方式如下：
##### 1>.链式添加跳过多个拦截器
        RouteManager.getInstance().build("/deal/buy-detail")
                   .withParams(params)
                   .skipInterceptors(LoginInterceptor.class)
                   .skipInterceptors(BindCardInterceptor.class)
                   .go(context);

##### 2>.通过不定参数指定多个
       RouteManager.getInstance().build("/deal/buy-detail")
                   .withParams(params)
                   .skipInterceptors(LoginInterceptor.class,BindCardInterceptor.class)
                   .go(context);

### 13，addInterceptor(Class<AbsInterceptor> clazz, Object options, int index)
#### Class<AbsInterceptor> clazz :拦截器的class文件
#### Object options:当前拦截器需要的参数，可以是map，可以使jsonString
#### int index：拦截器添加的位置，默认-1，添加在集合的最后
#### addInterceptor方法属于RouteContextBuilder类，用于动态添加拦截器，使用方法如下：
       RouteManager.getInstance().build("/deal/buy-detail")
                   .withParams(params)
                   .addInterceptors(LoginInterceptor.class,options,index)//options 拦截器需要的配置参数，可以是map，可以使json；index 插入拦截器的位置。后面两个参数都可以为空，默认是放在配置拦截器集合的最后
                   .go(context);
       //如果需要添加多个拦截器，直接链式调用.addInterceptors()方法就可以啦

### 14，interceptorVerifyResult(InterceptorState state, Object options)
#### InterceptorState state: 验证状态，有VERIFIED(验证通过)，UNVERIFIED(验证未通过)，PENDING(验证中)
#### Object options: 验证传给当前拦截器的参数，有需要填，没有可以直接为空；参数类型为json或者map
#### interceptorVerifyResult方法属于RouteManager类，用于拦截器验证操作完成时候的回调，通知库验证操作完成，例如完成登录操作，使用方法如下：
     public class LoginActivity{
          @Override
          public void onCreate(Bundle savedInstanceState){
                 super.onCreate(savedInstanceState);
                 login();
          }

          private void login(){
               ......
               //接口调用登录成功
               RouteManager.getInstance().interceptorVerifyResult(VERIFIED)

               //接口调用登录失败:
               RouteManager.getInstance().interceptorVerifyResult(UNVERIFIED)
          }
     }

### 15，interceptorInterrupts()
#### interceptorInterrupts方法属于RouteManager类，用于进入拦截器验证操作页面，但是没有做任何验证操作，就直接关闭了该页面，例如进入登录页，点击左上角关闭按钮或者Android物理返回键，实现方式如下：
      public class LoginActivity{
           @Override
           public void onCreate(Bundle savedInstanceState){
                super.onCreate(savedInstanceState);
           }

           //关闭按钮和物理返回键点击实现方法
           private void close(){
               RouteManager.getInstance().interceptorInterrupts();
           }
      }

### 16，goBack(Context context)
#### goBack方法属于RouteManager类，通过路由跳转的时候，用于回退到它的父页面或者当前路由的根页面；参数用于返回需要，如果不需要就不用传。使用方式如下：
      RouteManager.getInstance().goBack(this)

### 17，getCurrentUrl()
#### getCurrentUrl方法属于RouteManager类，通过路由跳转的页面，在当前页，可以通过该方法获取当前页面的url

### 18，AbsInterceptor
#### AbsInterceptor类是所有实现拦截器的基类。


## 四，库的使用流程及实例

### 1，在assets中配置项目需要的配置文件。

### 2，初始化( 注：最好是在Application里面进行初始化)。
    RouteManager.getInstance().initialize(configJsonString,callBack,iTask);

### 3，调用路由API。
##### 注：所有API的调用，除了.build(),和.go()是必选之外，其他API的调用在两者之间，根据需要调用

##### 1>.路由直接跳转，不需要任何配置API，登录如下：
        RouteManager.getInstance().build("/login")//路由地址
                            .go(context);

##### 2>.跳转需要添加参数:
       RouteManager.getInstance().build("/deal/buy-detail")
                            .withParams(params)
                            .go(context);

##### 3>.跳转需要跳过注解或者配置文件中配置的某些拦截器：
        //跳过所有拦截器，例1:
         RouteManager.getInstance().build("/deal/buy-detail")
                      .withParams(params)
                      .skipInterceptors()
                      .go(context);
        //跳过指定拦截器，例2:
         RouteManager.getInstance().build("/deal/buy-detail")
                      .withParams(params)
                      .skipInterceptors(LoginInterceptor.class)
                      .go(context);

##### 4>.路由API的组合调用实例：
        //底部弹出登录页，例1：
        Router.getInstance().build("/login")
                            .anim(R.anim.push_down_in,R.anim.push_up_out)
                            .go(context);
        //跳转购买页，需要携带一个类型参数，指定打开方式是清空目标页之上所有页面，需要购买结果回调给目标页，例2：
        Router.getInstance().build("/deal/buy-detail")
                            .with("type","shb")
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            .requestCode(100)
                            .go(context);


### 4，拦截器,任务执行类具体实例

##### 1>.拦截器的具体实现，例如登录：
      public class LoginInterceptor extends InterceptorImpl{
             @Override
             public void verify(InterceptorCallBack callBack){
                    //是否登录判断验证,在这里，其他拦截器也可以通过异步获取数据验证，再回调
                    if(User.getInstance.isLogin()){
                         callBack.stateCallBack(VERIFIED)
                    }else{
                       callBack.stateCallBack(UNVERIFIED)
                    }
             }

             @Override
             public void onFailBefore(){
                  //首次检查失败，在该方法进行登录验证，调用登录路由
                  RouteManager.getInstance().build("/login").go(context);
             }
      }
      //上面两个是必须实现的方法，还有其他打断，interrupt(),验证通过等的方法也可以重写，

##### 2>.任务执行类的实现，例如，activity跳转的任务执行类
        public class ActivityTask implements IRouteTask{
            private static final String TAG = "ActivityTask";

            private Context context;
            private RouteContext routeContext;
            private RouteRequest routeRequest;

            @Override
            public void execute(RouteContext routeContext) {
                this.context = routeContext.getContext();
                this.routeContext = routeContext;
                this.routeRequest = routeContext.getRouteRequest();
                if (context instanceof Activity) {
                    Intent intent = assembleFlagsIntent();
                    if (intent == null) {
                        return;
                    }
                    if (routeRequest.getRequestCode() > 0) {
                        ActivityCompat.startActivityForResult((Activity) context, intent, routeRequest.getRequestCode(), null);
                    } else {
                        ActivityCompat.startActivity((Activity) context, intent, null);
                    }
                    assembleAnim();
                }
            }
        }
###### 注：各种API的组合，及具体效果，大家可以自己进行验证和体验。

## 五，路由库相关状态表

### 1，拦截器验证状态表

|属性名|类型|说明|
|:-------:|:------:|:-------:|
|VERIFIED|enum|验证通过|
|UNVERIFIED|enum|验证未通过|
|PENDING|enum|验证中|

### 2，Route回调结果RouteResponse对象code状态表
|属性名|类型|说明|
|:-------:|:------:|:-------:|
|200|int|成功|
|201|int|数据初始化失败|
|202|int|url找不到|
|203|int|目标类找不到到|
