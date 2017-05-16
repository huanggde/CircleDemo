# CircleDemo
环型界面Demo,1采用适配器模式。2.界面旋转优化
布局添加控件
 <com.hgd.circlememudemo.CircleMemuLayout xmlns:android="http://schemas.android.com/apk/res/android"
        android:id="@+id/id_menulayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:padding="100dp"
        android:background="@drawable/circle_bg2">


    </com.hgd.circlememudemo.CircleMemuLayout>
    不可添加子布局
    调用范例
       circleMemuLayout = (CircleMemuLayout) findViewById(R.id.id_menulayout);
        CircleMemuLayout.Builder builder = new CircleMemuLayout.Builder(this)
                .addMenuItem("签到", R.drawable.app_poslogon, Main2Activity.class)
                .addMenuItem("查询", R.drawable.app_query, Main2Activity.class)
                .addMenuItem("退货", R.drawable.app_refund, Main2Activity.class)
                .addMenuItem("结算", R.drawable.app_settle, Main3Activity.class)
                .addMenuItem("设置", R.drawable.app_setting, Main3Activity.class)
                .addCentertem(R.drawable.centerlogo, Main2Activity.class);

        circleMemuLayout.setAdapter(builder.getcircleMenuAdapter());
