# Android_Router#
##通过一个url地址访问Android页面##
##一，目标##
###1.嵌套跳转，解决配置文件重复配置
例： 跳购买需要登录绑卡，绑卡需要登录，购买前置条件只需要配置绑卡。
考虑方案：应用初始化，初始化所有前置条件，通过优先级控制执行顺序，flag标记是否执行业务代码。###
###2.跳转采用链式调用。###
###3.页面路径，页面所需参数，前置条件 采用注解的形式，前置条件热更放在配置文件中。
配置文件中仅配置需要修改的业务，其余页面配置使用注解，配置在类中。###
###4.支持传递自定义Serializable对象###
###5.增加跳转结果的回调###

##二，配置文件格式##
<pre><code>
    {
        "/deal/buy-detail": [//路由url
        {
            "condition": {//一对多的时候，路由的精确匹配条件，可以为空
                "type": "spirit",
                "rateType": "unfix"
            },
            "result": {//路由匹配结果
                "activity": "com.rxhui.pay.business.deal.buy.BuyCurrentActivity",//跳转目标类
                "params": {//跳转参数
                    "arriveType": "spirit"
                },
                "group":"deal"//路由所属组
                "interceptors": [//路由需要的拦截器，这里只描述当前除组拦截器之外的拦截器
                {
                    "interceptorClazz": "com.rxhui.pay.application.gotopage.LoginInterceptor",//具体拦截器
                    "params": {},//拦截器需要的参数
                    "options": {}//拦截器需要的配置
                },
                {
                    "interceptorClazz": "com.rxhui.pay.application.gotopage.BindCardInterceptor",
                    "params": {},
                    "options": {}
                },
                {
                    "interceptorClazz": "com.rxhui.pay.application.gotopage.buy.ProductRiskInterceptor",
                    "params": {},
                    "options": {}
                }
                ],
                "rContext": {//当前路由的上下文管理类，可以不写，默认系统的RouteContext
                    "clazz": "com.jinhui365.router.route.RouteContext",
                    "options": {//上下文管理类配置项，默认是从传递参数里面去掉配置里面包含的参数，
                        "detailVO": ""
                    }
                }
            }
        },
        {
            "result": {
                "activity": "com.rxhui.pay.business.deal.buy.BuyCoolCurrentActivity",
                "params": {
                    "arriveType": "opencash"
                },
                "interceptors": [
                {
                    "interceptorClazz": "com.rxhui.pay.application.gotopage.LoginInterceptor",
                    "params": {},
                    "options": {}
                },
                {
                    "interceptorClazz": "com.rxhui.pay.application.gotopage.BindCardInterceptor",
                    "params": {},
                    "options": {}
                },
                {
                    "interceptorClazz": "com.rxhui.pay.application.gotopage.buy.InvestorWebViewInterceptor",
                    "params": {
                        "state": "-1"
                    },
                    "options": {
                        "url1": "/user/qualified",
                        "url2": "/user/unqualified"
                    }
                },
                {
                    "interceptorClazz": "com.rxhui.pay.application.gotopage.buy.ProductRiskInterceptor",
                    "params": {},
                    "options": {}
                }
                ],
                "rContext": {
                    "clazz": "com.jinhui365.router.route.RouteContext",
                    "options": {
                        "detailVO": ""
                    }
                }
            }
        }
        ],
    }
</code></pre>

##三，API使用##

###1，初始化,(注：最好是在Application里面进行初始化)：###
    <pre><code>Router.getInstance().initialize(configJsonString);//configJsonString指配置文件转换的json，可以为空，例如：Router.initialize(null)<pre><code>

###2，调用路由API###


####(1),最简单的调用,例如，登录：
    Router.getInstance().build("/login").go(context);
    //如果项目内使用配置文件形式，需要在配置文件配置：
    "/login":[
    {
        "result":{
           "activity": "...user.LoginActivity"
        }
    }]
    //如果使用注解：
    @Route(path = "/login")
    public class LoginActivity{

    }


####(2),配置文件的配置，如上第二步，不再具体描述。


####(3),注解的使用

#####1>.只需要一个地址的路由注解：
        @Route(path = "/login")
        public class LoginActivity{

        }

#####2>.需要拦截器的路由注解：
        @Route(path = "/deal/buy-detail",interceptors = {"loginInterceptor","bindCardInterceptor"})
        public class FixBuyDetailActivity{

        }

        //登录拦截器注解实现
        @Interceptor(name = "loginInterceptor")
        public class LoginInterceptor extends InterceptorImpl{
            @Override
            public void checkoutState(InterceptorStateCallBack stateCallBack){
                //登录状态检查，通过stateCallBack回调给库拦截器检查状态
            }

            @Override
            public void onFailBefore(){
                //首次检查失败，在该方法进行登录验证，调用登录路由
                Router.getInstance().build("/login").go(context);
            }
        }
        //绑卡拦截器，同上，不在放具体代码

#####3>.如果实现分组管理，2>中的路由可以写成如下方式：
         @Route(path = "/deal/buy-detail",group = DealGroup.class)
         public class FixBuyDetailActivity{

         }
        //组的构建
         public class DealGroup extends RouteGroup{
            @Override
            public String[] getInterceptor(){
                String[] array = new String[]{"loginInterceptor","bindCardInterceptor"}
                return array;
            }
         }

#####4>.如果实现分组管理，路由中还有其他拦截器，注解实现如下：
        @Route(path = "/deal/buy-detail",group = DealGroup.class,interceptors = {"webViewInterceptor"})
        public class FixBuyDetailActivity{

        }

        //如果拦截器中像配置文件一样需要配置参数，拦截器注解如下
        @Interceptor(name = "webViewInterceptor"，params = "{\"state\":\"-1\"}",options = "{\"url1\":\"/a/b\",\"url2\":\"/a/c\"}")
        public class WebViewInterceptor extends InterceptorImpl{
               @Override
               public void checkoutState(InterceptorStateCallBack stateCallBack){
                    //异步获取状态，并回调给库
               }

               @Override
               public void onFailBefore(){
                   //检查失败，进入页面，进行验证
               }
        }

#####5>.如果路由是同一个路由，对应多个页面，注解使用如下，通过conditions匹配区分目标页：
        @Route(path = "/deal/buy-detail",group = DealGroup.class,interceptors = {"webViewInterceptor"})
        public class FixBuyDetailActivity{

        }

        @Route(path = "/deal/buy-detail",conditions = "{\"type\":\"shb\"}",group = DealGroup.class)
        public class CoolBuyDetailActivity{

        }

#####6>.如果多个路由，对应同一个页面：
        @Route(path = {"/webView","/deal/baseWebView"})
        public class BaseWebViewActivity{

        }

#####7>.如果路由跳转，需要一些固定参数，可以有如下两种方式实现：
        方式一：
        @Route(path = "/deal/buy-detail",group = DealGroup.class,interceptors = {"webViewInterceptor"},params = "{\"state\":\"1\"}")
        public class FixBuyDetailActivity{

        }
        方式二：
        @Route(path = "/deal/buy-detail?state=1",conditions = "{\"type\":\"shb\"}",group = DealGroup.class)
        public class CoolBuyDetailActivity{

        }

#####8>.如果库的上下文管理类不能满足当前应用的跳转，我们可以实现子类，重写RouteContext，注解实现如下：
         @Route(path = "/deal/buy-detail?state=1",conditions = "{\"type\":\"shb\"}",group = DealGroup.class,,injectContext = "buyRouteContext")
         public class CoolBuyDetailActivity{

         }

         //RouteContext子类注解实现如下：
         @InjectContext(name = "buyRouteContext")
         public class BuyRouteContext extends RouteContext{
            //可以重写动画实现，getIntent等方法
         }
         //如果注解需要一些配置项参数，可以如下实现：
         @InjectContext(name = "buyRouteContext",options = "{\"url1\":\"/a/b\",\"url2\":\"/a/c\"}")
         public class BuyRouteContext extends RouteContext{
              //可以重写动画实现，getIntent等方法
         }


####(4),路由API的调用详解，所有API的调用，除了.build(),和.go()是必选之外，其他API的调用在两者之间，根据需要调用

#####1>.路由直接跳转，如下：
        Router.getInstance().build("/login")//路由地址
                            .go(context);

#####2>.跳转需要添加参数:
        Router.getInstance().build("/deal/buy-detail")
                            .with(params)//动态添加的参数，添加参数支持多种格式，with(key,value),with(map),with(jsonString)
                            .go(context);

#####3>.跳转需要跳过注解或者配置文件中配置的某些拦截器：
        需要传递参数,跳过所有拦截器，例1:
        Router.getInstance().build("/deal/buy-detail")
                                    .with(params)
                                    .skipInterceptors()
                                    .go(context);
        需要传递参数,跳过指定拦截器，例2:
                Router.getInstance().build("/deal/buy-detail")
                                    .with(params)
                                    .skipInterceptors("loginInterceptor")
                                    .go(context);
        需要传递参数,跳过多个指定拦截器，例3:
                Router.getInstance().build("/deal/buy-detail")
                                    .with(params)
                                    .skipInterceptors("loginInterceptor","BindCardInterceptor")
                                    .go(context);
        不需要传递参数,跳过多个指定拦截器，例3:
                Router.getInstance().build("/deal/buy-detail")
                                    .skipInterceptors("loginInterceptor","BindCardInterceptor")
                                    .go(context);

#####4>.跳转需要动态添加一些拦截器：
        添加一个拦截器,例1：
        Router.getInstance().build("/deal/buy-detail")
                            .with(params)
                            .addInterceptors("loginInterceptor")
                            .go(context);
        添加多个拦截器,例2：
        Router.getInstance().build("/deal/buy-detail")
                            .with(params)
                            .addInterceptors("loginInterceptor","BindCardInterceptor");
                            .go(context);

######注：关于添加和跳过拦截器API，需要注意.skipInterceptors()级别最高，如果一个跳转中有改API的调用，则后面无论添加多
少个拦截器，都是无效的；如果调用的是.skipInterceptors("loginInterceptor")含参数的API，则只会移除指定的拦截器，如果
后面有.addInterceptors("loginInterceptor")的调用，依然会执行。

#####5>.跳转需要在当前页的ActivityForResult里面接收数据:
        例1：
        Router.getInstance().build("/deal/buy-detail")
                            .with(params)
                            .addInterceptors("loginInterceptor","BindCardInterceptor");
                            .requestCode(100)
                            .go(context);
        例2：
        Router.getInstance().build("/deal/buy-detail")
                            .with(params)
                            .requestCode(100)
                            .go(context);

        build 和 go之间的所有API都是独立使用的，互不影响。

#####6>.如果需要指定打开和关闭目标页的动画：
        Router.getInstance().build("/deal/buy-detail")
                            .with(params)
                            .anim(enterAnim,exitAnim)
                            .go(context);

#####7>.如果需要制定打开目标页的打开方式：
        清除目标页上面的所有页面,例:
        Router.getInstance().build("/deal/buy-detail")
                            .with(params)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            .go(context);

#####8>.路由需要对跳转构成进行回调结果查看，处理：
        Router.getInstance().build("/deal/buy-detail")
                            .with(params)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            .go(context, new RouteCallback() {
                                  @Override
                                  public void callback(RouteResult state, Uri uri, String message) {
                                        //对跳转回调结果进行查看和处理
                                  }
                            });

#####9>.路由API的全景描述实例：
        底部弹出登录页，例1：
        Router.getInstance().build("/login")
                            .anim(R.anim.push_down_in,R.anim.push_up_out)
                            .go(context);
        跳转购买页，需要携带一个类型参数，指定打开方式是清空目标页之上所有页面，需要购买结果回调给目标页，例2：
        Router.getInstance().build("/deal/buy-detail")
                            .with("type","shb")
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP)
                            .requestCode(100)
                            .go(context);

######注：其他组合不在这里举例一一说明，大家可以自己进行验证和体验。