package xuwei.com.eventbusxtest.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import xuwei.com.eventbusxtest.R;

/**
 * Created by Xu Wei on 2017/6/28.
 */

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
