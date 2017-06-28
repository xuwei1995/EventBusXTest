package xuwei.com.eventbusxtest.event;

/**
 * Created by Xu Wei on 2017/6/28.
 */

public interface BaseEvents{
 void    setObject(Object obj);
    Object getObject();
    //事件定义
    enum CommonEvent implements BaseEvents {
        LOGIN, //登录
        LOGOUT, //登出
        BACK;
        private Object obj;
        @Override
        public void setObject(Object obj) {
            this.obj = obj;
        }
        @Override
        public Object getObject() {
            return obj;
        }
    }
    // ... 其他事件定义

}
