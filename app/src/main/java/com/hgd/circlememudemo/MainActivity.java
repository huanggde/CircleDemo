package com.hgd.circlememudemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    CircleMemuLayout circleMemuLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        circleMemuLayout = (CircleMemuLayout) findViewById(R.id.id_menulayout);
        CircleMemuLayout.Builder builder = new CircleMemuLayout.Builder(this)
                .addMenuItem("签到", R.drawable.app_poslogon, Main2Activity.class)
                .addMenuItem("查询", R.drawable.app_query, Main2Activity.class)
                .addMenuItem("退货", R.drawable.app_refund, Main2Activity.class)
                .addMenuItem("结算", R.drawable.app_settle, Main3Activity.class)
                .addMenuItem("设置", R.drawable.app_setting, Main3Activity.class)
                .addCentertem(R.drawable.centerlogo, Main2Activity.class);

        circleMemuLayout.setAdapter(builder.getcircleMenuAdapter());
    }
}
