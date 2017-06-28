package xuwei.com.eventbusxtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import xuwei.com.eventbusxtest.base.BaseActivity;
import xuwei.com.eventbusxtest.bean.User;
import xuwei.com.eventbusxtest.event.BaseEvents;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private TextView text;
    private Button button,intentsec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text= (TextView) findViewById(R.id.text);
        button= (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        intentsec= (Button) findViewById(R.id.intentSec);
        intentsec.setOnClickListener(this);
    }
    //处理事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(  BaseEvents.CommonEvent baseEvents) {
        User user= (User) baseEvents.getObject();
        text.setText(user.getUserName()+"\n"+user.getPassWord());
    }
    @Subscribe(sticky = true)
    public void onEvent(BaseEvents. CommonEvent event) {
        // UI updates must run on MainThread
        if(event==BaseEvents.CommonEvent.BACK){

             text.setText(event.getObject().toString());
            Log.d(TAG,"Content is : "+event.getObject().toString());

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.button :
                BaseEvents. CommonEvent event = BaseEvents.CommonEvent.LOGIN;
                User user=new User();
                user.setUserName("xuwei");
                user.setPassWord("111");
                event.setObject(user);
                EventBus.getDefault().postSticky(event); //这里发送的是粘性事件 在secondActivity可以接受 此方法可以代替onActivityResult
                break;
            case  R.id.intentSec:
                startActivity(new Intent(MainActivity.this,SecondActivity.class));
                break;
        }
    }

}
