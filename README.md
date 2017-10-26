# Android_Router

### 通过一个url地址访问Android页面</p>
## 一，目标
1.支持嵌套跳转，解决配置文件重复配置   例： 跳购买需要登录绑卡，绑卡需要登录，购买前置条件只需要配置绑卡。考虑方案：多层嵌套之间实现父子关系关联。</p>
2.跳转采用链式调用。</p>
3.跳转路由采用分组管理</p>
4.支持传递自定义Serializable对象</p>
5.增加跳转结果的回调</p>
6.增加打开目标页的方式，1，startActivity 2，startActivityForResult</p>
7.动画配置

## 二，配置文件格式

### 1.配置文件格式事例：
<pre><code>
    {
        "deal": {//路由
            "interceptors": [//路由对应拦截器数组
              "com.rxhui.pay.application.gotopage.LoginInterceptor",
              "com.rxhui.pay.application.gotopage.BindCardInterceptor"
            ],
            "subRoutes": {//子路由对象
              "buy": {//路由
                "interceptors": [
                  "com.rxhui.pay.application.gotopage.buy.ProductRiskInterceptor"
                ],
                "taskClass":"com.rxhui.pay.application.gotopage.ActivityTask",//路由目标执行类
                "subRoutes": {
                  "detail?type=spirit&rateType=unfix": {
                    "taskOptions": {//目标执行类配置信息
                      "clazz": "com.rxhui.pay.business.deal.buy.BuyCurrentActivity",
                      "arriveType": "spirit",
                      "index:": "1"
                    }
                  },
                  "detail?type=opencashNew": {
                    "taskOptions": {
                      "clazz": "com.rxhui.pay.business.deal.buy.BuyCoolCurrentActivity",
                      "arriveType": "opencashNew"
                    }
                  },
                  "detail": {
                    "interceptors": [
                      "com.rxhui.pay.application.gotopage.buy.InvestorWebViewInterceptor"
                    ],
                    "taskOptions": "com.rxhui.pay.business.deal.buy.BuyFixedActivity"
                  }
                }
              },
              "withdraw": "com.rxhui.pay.business.deal.withdraw.WithdrawCurrentActivity"
            }
          },
          "login": "com.rxhui.pay.business.auth.UserLoginActivity",
    }
</code></pre>
### 2.配置文件key字段解释表
#### 1>.Router：
|:属性名:|:是否可选:|:类型:|:默认属性:|
|:subRoutes:|:是:|:object:|:空:|
|:interceptors:|:是:|:Array:|:class:|
|:taskClass:|:是:|:String:|:class:|
|:taskOptions:|:否:|:Object:|:class:|
#### 2>.interceptors：



## 三，API使用

### 1，初始化( 注：最好是在Application里面进行初始化)：
    Router.getInstance().initialize(configJsonString,iErrorHandler,iMatcherTarget);
    注：</p>
        configJsonString指默认配置文件读出的json，不可以为空。
        iErrorHandler指默认错误处理，可以为空，为空时默认错只打log，不做UI处理。
        iMatcherTarget指默认使用跳转目标匹配类，可以为空，为空的时候，匹配库默认的activity跳转，也可以自己实现匹配跳转类，实现接口IMatcherTarget，后期库内会丰富多种跳转匹配
        例如：
        Router.getInstance().initialize(configJsonString,null，null);
        Router.getInstance().initialize(configJsonString,null，new ActivityMatcher());
        Router.getInstance().initialize(configJsonString,new IErrorHandler(){
            @Override
            public void onClientError(Context context, RouteResponse response){

            }

            @Override
            public void onServerError(Context context, RouteResponse response){

            }
        }，new ActivityMatcher());

### 2，调用路由API
##### 注：所有API的调用，除了.build(),和.go()是必选之外，其他API的调用在两者之间，根据需要调用

##### 1>.路由直接跳转，不需要任何配置API，登录如下：
        Router.getInstance().build("/login")//路由地址
                            .go(context);

##### 2>.跳转需要添加参数:
        Router.getInstance().build("/deal/buy-detail")
                            .with(params)//动态添加的参数，添加参数支持多种格式，with(key,value),with(map),with(jsonString)
                            .go(context);

##### 3>.跳转需要跳过注解或者配置文件中配置的某些拦截器：
        //跳过所有拦截器，例1:
        Router.getInstance().build("/deal/buy-detail")
                                    .with(params)
                                    .skipInterceptors()
                                    .go(context);
        //跳过指定拦截器，例2:
        Router.getInstance().build("/deal/buy-detail")
                            .with(params)
                            .skipInterceptors(LoginInterceptor.class)
                            .go(context);
        //跳过多个指定拦截器，例3:
        Router.getInstance().build("/deal/buy-detail")
                            .with(params)
                            .skipInterceptors(LoginInterceptor.class,BindCardInterceptor.class)
                            .go(context);

##### 4>.跳转需要动态添加一些拦截器：
        //添加一个拦截器,例1：
        Router.getInstance().build("/deal/buy-detail")
                            .with(params)
                            .addInterceptors(LoginInterceptor.class,params,options,index)//params:在拦截器中传递给拦截操作的参数，可以是map，可以使json；options 拦截器需要的配置参数，可以是map，可以使json；index 插入拦截器的位置。后面三个参数都可以为空，默认是放在配置拦截器的最后
                            .go(context);
        //添加多个拦截器,例2：
        Router.getInstance().build("/deal/buy-detail")
                            .with(params)
                            .addInterceptors(LoginInterceptor.class,params,options,3);
                            .addInterceptors(BindCardInterceptor.class,params,options)
                            .go(context);

###### 注：关于添加和跳过拦截器API，需要注意.skipInterceptors()级别最高，如果一个跳转中有该API的调用，则后面无论添加多少个拦截器，都是无效的；如果调用的是.skipInterceptors(LoginInterceptor.class)含参数的API，则只会移除指定的拦截器，如果后面有.addInterceptors(LoginInterceptor.class)的调用，依然会执行。

##### 5>.跳转需要在当前页的ActivityForResult里面接收数据:
        //例1：
        Router.getInstance().build("/deal/buy-detail")
                            .with(params)
                            .addInterceptors(LoginInterceptor.class,params,options,3);
                            .addInterceptors(BindCardInterceptor.class,params,options)
                            .requestCode(100)
                            .go(context);
        //例2：
        Router.getInstance().build("/deal/buy-detail")
                            .with(params)
                            .requestCode(100)
                            .go(context);

        //build 和 go之间的所有API都是独立使用的，互不影响。

##### 6>.如果需要指定打开和关闭目标页的动画：
        Router.getInstance().build("/deal/buy-detail")
                            .with(params)
                            .anim(enterAnim,exitAnim)
                            .go(context);

##### 7>.如果需要制定打开目标页的打开方式：
        //清除目标页上面的所有页面,例:
        Router.getInstance().build("/deal/buy-detail")
                            .with(params)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            .go(context);

##### 8>.路由需要对跳转构成进行回调结果查看，处理：
        Router.getInstance().build("/deal/buy-detail")
                            .with(params)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            .go(context, new RouteCallback() {
                                  @Override
                                  public void callback(RouteResult state, Uri uri, String message) {
                                        //对跳转回调结果进行查看和处理
                                  }
                            });

##### 9>.路由API的组合调用实例：
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

##### 10>.路由其他API调用：

        I, 路由移交控制权，例如登录

        在拦截器里面,使用路由跳转登录页：
        public class LoginInterceptor extends InterceptorImpl{
                  @Override
                  public void checkoutState(InterceptorStateCallBack stateCallBack){
                       if(User.getInstance.isLogin()){
                            stateCallBack.tateCallBack(SUCCESS)
                       }else{
                            stateCallBack.tateCallBack(FAIL)
                       }
                  }

                  @Override
                  public void onFailBefore(){
                       //首次检查失败，在该方法进行登录验证，调用登录路由
                       Router.getInstance().build("/login").go(context);
                  }
         }
        在登录页面，登录成功，回调Router.getInstance().interceptorForSkipResult(InterceptorStateEnum state)
        public class LoginActivity{
                @Override
                public void onCreate(Bundle savedInstanceState){
                      super.onCreate(savedInstanceState);
                      login();
                }

                private void login(){
                    ......
                    //接口调用登录成功
                    Router.getInstance().interceptorForSkipResult(SUCCESS)
                }
                //点击返回按钮和手机返回键调用方法
                private void close(){
                    打断条件下：
                    Router.getInstance().interceptorForSkipResult(DEFAULT)
                    或者
                    Router.getInstance().interceptorForSkipResult()/可以不传，默认是DEFAULT
                }
         }

        II, 设置指定路由错误处理：

        Router.getInstance().build("/deal/buy-detail")
                            .with("type","shb")
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            .errorHandler(iErrorHandler)//实现IErrorHandler接口，进行错误设置
                            .requestCode(100)
                            .go(context);

        III, 如果通过路由进入某一个页面，可以通过API,getCurrentUri获取当前面的路由url，例如：
        通过路由从详情页进入爽活宝购买页，在购买页获取进入爽活宝购买页的url：

        String url = Router.getInstance().getCurrentUri();//url的格式例如：jhjrapp://deal/buy/detail?type=shb&code=123

        IV, 通过路由进入某页面，中间逻辑通过原生跳转处理，在最终页面，可以通过goBack回到路由进入页面的上一个页面，如果上一页为空，就回到进入页面，例如：
        从详情页进入购买页，中间需要拦截器，通过路由进入绑卡页面，在绑卡页面通过通过原生进入密码设置页，结果页，最后在结果页，调用：

        Router.getInstance().goBack(context);回到详情页，继续进行进入购买页的拦截器判断

        V, 如果需要通过非默认配置文件添加或者更改路由，只需要调用如下API，把更改的部分合并到默认路由配置中：

        Router.getInstance().mergeConfig(configJsonString);

### 3，拦截器,匹配类的具体实现

##### 1>.拦截器的具体实现，例如登录：
      public class LoginInterceptor extends InterceptorImpl{
             @Override
             public void checkoutState(InterceptorStateCallBack stateCallBack){
                    //是否登录判断验证,在这里，其他拦截器也可以通过异步获取数据验证，再回调
                    if(User.getInstance.isLogin()){
                         stateCallBack.tateCallBack(SUCCESS)
                    }else{
                        stateCallBack.tateCallBack(FAIL)
                    }
             }

             @Override
             public void onFailBefore(){
                  //首次检查失败，在该方法进行登录验证，调用登录路由
                  Router.getInstance().build("/login").go(context);
             }
      }
      //上面两个是必须实现的方法，还有其他打断，onBreak(),验证成功等的方法也可以重写，

##### 2>.跳转匹配类的实现，例如，匹配目标是activity跳转类
        public class ActivityMatcher implements IMatcherTarget {
            private static final String TAG = "ActivityMatcher";

            private Context context;
            private RouteContext routeContext;
            private RouteRequest routeRequest;

            @Override
            public void matcher(RouteContext routeContext) {
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
##### 3>.默认错误接口实现
        public class IErrorHandlerImpl implements IErrorHandler{
            @Override
            public void onClientError(Context context, RouteResponse response){

            }

            @Override
            public void onServerError(Context context, RouteResponse response){

            }
        }

###### 注：各种API的组合，及具体效果，大家可以自己进行验证和体验。