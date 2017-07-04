package xuwei.com.eventbusxtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import xuwei.com.eventbusxtest.base.BaseActivity;
import xuwei.com.eventbusxtest.bean.User;
import xuwei.com.eventbusxtest.event.BaseEvents;

public class SecondActivity extends BaseActivity {
    private static final String TAG = "SecondActivity";
    private TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        text= (TextView) findViewById(R.id.text);
    }
    @Subscribe(sticky = true)
    public void onEvent(BaseEvents. CommonEvent event) {

        // UI updates must run on MainThread
        if(event==BaseEvents.CommonEvent.LOGIN){
            User user= (User) event.getObject();
            text.setText(user.getUserName()+"\n"+user.getPassWord());
             Log.d(TAG,"Content is : "+user.getUserName()+"\n"+user.getPassWord());

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BaseEvents. CommonEvent event = BaseEvents.CommonEvent.BACK;
        event.setObject("我是SecondActibity返回的");
        EventBus.getDefault().postSticky(event); //这里发送的是粘性事件 在secondActivity可以接受 此方法可以代替onActivityResult
    }
}
