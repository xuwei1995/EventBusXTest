# EventBusXTest
***站在巨人的肩膀，只为看的更远***
## 先上gif事例图
![](https://github.com/xuwei1995/EventBusXTest/blob/master/app/src/main/java/xuwei/com/eventbusxtest/gif/GIF.gif?raw=true) 
## 这里给出eventbus的官方文档 http://greenrobot.org/eventbus/documentation/
### EventBus 3 简介
EventBus是一种为了优化Android组件之间事件传递的解耦工具，</br>通过发布/订阅事件总线来实现事件在不同组件之间的事件传递。</br>
在EventBus 3之前，greenrobot团队因为考虑性能原因所以比较抵触使用注解框架。</br>目前的EventBus3开始使用注解来申明订阅事件的处理方法。</br>虽然目前Android 6 和ART都有了，但是对于Java反射造成的性能影响还是没能很好的解决。
在EventBus3中，</br>greenrobot团队通过利用在编译时检索所有注解代码，然后生成一个包含所有在运行时要花很大代价才能获取的数据的类，</br>通过这种新的注解处理方式来提升性能，让EventBus3比其他的eventbus会更加快。

### EventBus 3的使用

引入EventBus库文件

在这里以gradle引用EventBus3库为例进行说明
```java 
compile 'org.greenrobot:eventbus:3.0.0'
```
首先给我们要写自己的事件（就是定义事件，然后我们可以去发送这个定义的事件或者接受这个定义的事件）
</br>废话 不多说我来贴代码</br>
```java  
  

 public interface BaseEvents
{
     void  setObject(Object obj);
    Object getObject();</br>
    //事件定义</br>
    enum CommonEvent implements BaseEvents</br> {
        LOGIN, //登录
        LOGOUT, //登出
        BACK;
        private Object obj;
        @Override</br>
        public void setObject(Object obj) {
            this.obj = obj;</br>
        }
        @Override
        public Object getObject() {
            return obj;
        }
    }
    // ... 其他事件定义

}
```
好了我这里写了一个CommonEvent 枚举 有login logout　back 分别代表不同是事情类型 
setObject和getObject方法可以传递object（我用eventbus的目的就是传递各种类型的对象，这里我们定义万能型）
好了 我们定义好事件了 等着接来来的发送和接受事件吧。
## Eventbus的注册和反注册 
在使用eventbus的发送接受之前我们必须要在活动中去注册
官方推荐我们在baseActivity中注册反注册 下面给出版主的代码：</br>
```java  
  
public class BaseActivity extends AppCompatActivity
   {
    @Override
  
    protected void onCreate(Bundle savedInstanceState)</br> {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        registEventBus();
    }

    @Override
    public void onStop() {
        unRegistEventBus();
        super.onStop();
    }

    protected void registEventBus() {
        //子类如果需要注册eventbus，则重写此方法
        EventBus.getDefault().register(this);
    }

    protected void unRegistEventBus() {
        //子类如果需要注销eventbus，则重写此方法
        EventBus.getDefault().unregister(this);
    }
}
  
```
</br>
注册方法后，需要使用注解@Subscribe声明处理事件的方法，以下代码摘自官方介绍</br>
这里 说的声明处理事件的方法其实就是接受事件了 
下面给出版主的代码：</br>

```java  

@Subscribe  // This method will be called when a MessageEvent is posted
public void onMessageEvent(MessageEvent event){    
      Toast.makeText(getActivity(), event.message, Toast.LENGTH_SHORT).show();
}
// This method will be called when a SomeOtherEvent is posted
@Subscribe
public void handleSomethingElse(SomeOtherEvent event){  
    doSomethingWith(event);
}
  ```

看完官方的介绍我来贴出我的
```java
 //处理事件
    @Subscribe(threadMode = ThreadMode.MAIN)//这里的MAIN是指的UI线程 
    public void onMoonEvent(  BaseEvents.CommonEvent baseEvents) {
        User user= (User) baseEvents.getObject();
        text.setText(user.getUserName()+"\n"+user.getPassWord());
    }
```
ThreadMode枚举包含四个值： 
### - PostThread 
### - MainThread 
### - BackgroundThread 
### - Async
 A.PostThread(默认模式)

调用线程：事件发布线程 
　　当订阅者所关注的变化发生时，EventBus将会在事件所发布的线程中调用订阅者对应的方法。 
　　因为订阅者在发布事件的线程中被调用，所以这种线程模式可以完全避免线程切换所带来的开销。适用于短时间能够完成并且不限定在主线程的事件处理情景。任何事件处理情景使用这种模式必须保证能够快速地返回结果，否者可能将引起线程阻塞。当发布事件的线程是主线程时，还可能会出现ANR。

B.MainThread

调用线程：Android主线程（UI线程） 
　　无论发布事件的是哪个线程，订阅者都将在android主线程（也即UI线程）中被调用。 
　　任何事件处理场景使用这种模式必须保证快速地返回结果，从而避免阻塞主线程。

C.BackgroundThread

调用线程：事件发布线程或者后台线程 
　　当发布事件的线程不是主线程时，事件处理方法将会立即在该线程中被调用；如果发布时间的线程是主线程，EventBus会使用一个单例的后台线程调用事件处理函数，该后台线程将会按照时间顺序处理并交付所有的事件。 
　　任何事件处理场景使用这种模式必须保证快速地返回结果，从而避免阻塞线程。

D.Async

调用线程：异步线程 
事件处理方法将会在异步的线程中被调用。该异步线程既不是主线程，也不是事件发布线程。 
在该模式下，发布事件不必等待事件处理方法执行完毕。在事件处理方法执行诸如网络请求等需要花费一定时间的任务时，推荐使用该模式。在使用过程中为了限制并发线程的数量，尽量避免在同一时间段内触发过多需要长时间执行的异步处理方法。

EventBus使用线程池来有效地重用异步线程。
 
## 发送事件

在发送事件时，可以利用我们定义的一个BaseEvent进行值的传递，因为定义的是一个Object对象，只需要保证类型转换正确的前提下就可以随意传值了。示例如下：

BaseEvents. CommonEvent event = BaseEvents.CommonEvent.LOGIN;
event.setObject("Send Event"); //传入一个String对象
eventBus.post(event); //发布事件
如此就简单的实现了一个EventBus事件的发布。
## StickyEvent
sticky事件就是指在EventBus内部被缓存的那些事件。EventBus为每个类（class）类型保存了最近一次被发送的事件——sticky。后续被发送过来的相同类型的sticky事件会自动替换之前缓存的事件。当一个监听者向EventBus进行注册时，它可能会去请求这些缓存事件。这时，所有已缓存的事件就会立即自动发送给这个监听者，就象这些事件又重新刚被发送了一次一样。这就意味着，一个监听者可以收到在它注册之前就已经被发送到EventBus中的事件（甚至是在这个监听者的实例被创建出来前，这一点是不是很奇妙）。这一强大功能将有助于我们解决某些固有的问题，如android上跨Activity和Fragment生命周期传递数据这种复杂问题，异步调用等等。（废话可以不看）
 
有时候，我们会面对这样一个问题，那就是我们要把一个Event发送到一个还没有初始化的Activity/Fragment，即尚未订阅事件。那么如果只是简单的post一个事件，那么是无法收到的，这时候，需要使用 StickyEvent，实例说明：
```java  
BaseEvents. CommonEvent event = BaseEvents.CommonEvent.LOGIN;
event.setObject("Send StickyEvent"); 
EventBus.getDefault().postSticky(event);
```
这 时间你发送的这个事情已经被缓存了，如果在一个你没有打开的fragment中你接受了这个方法 当你进入这个fragment就会执行这个方法了
## 注意事项
工作线程和UI线程之间的事件传递
在使用EventBus3时，由于event 在任何地方都可以发布一个事件，那么在不同线程之间传递事件，比如在工作线程传递一个事件更新UI线程中的一个控件，则需要注意threadMode的切换。

取消线程仅限于 threadMode处于POSTING模式才可以
否则容易导致错误:
org.greenrobot.eventbus.EventBusException: This method may only be called from inside event handling methods on the posting thread

eventbus 还有一些功能这里没有讲到比如混淆和事件处理优先级及事件拦截 我会接着更新
如果这个demo帮助到你了，请你给版主一个star谢谢，版主还是一个android菜鸟 希望和大家共同学习下面是我的QQ二维码 欢迎大家骚扰和我一起学习 ！！！
![](https://github.com/xuwei1995/EventBusXTest/blob/master/app/src/main/java/xuwei/com/eventbusxtest/gif/erweimaqq.png?raw=true)
