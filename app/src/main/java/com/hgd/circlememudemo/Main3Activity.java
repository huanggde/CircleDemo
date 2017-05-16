package com.hgd.circlememudemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Main3Activity extends Activity {
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        tv = (TextView) findViewById(R.id.tv3);

    }
}
