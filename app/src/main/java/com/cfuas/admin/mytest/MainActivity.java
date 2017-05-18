package com.cfuas.admin.mytest;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.view.ViewHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/***
 *
 * Created by huangzhiyu on 2017/5/18.
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TravelListAdapter adapter;
    private ArrayList<AssistantManager> assistantManagers = new ArrayList<>();
    private WheelView mWheel;

    protected BaseHandler baseHandler = new BaseHandler(this);
    private Timer autoUpdate;
    private WheelListView wheelListView;

    protected class BaseHandler extends Handler {
        private WeakReference<MainActivity> baseActivity;

        public BaseHandler(MainActivity baseActivity) {
            this.baseActivity = new WeakReference<>(baseActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            adapter = new TravelListAdapter(MainActivity.this, assistantManagers, R.layout.item_assistant_avatar);
            mWheel.setAdapter(adapter);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWheel = (WheelView) findViewById(R.id.wheel);
        wheelListView = mWheel.getWheelListView();
        wheelListView.setOnItemClickListener(this);
        initData();
        autoUpdate();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this,"click position:"+position,Toast.LENGTH_SHORT).show();
    }
    /**
     * 自动滚动
     */
    int index = 0;
    private void autoUpdate() {
        autoUpdate = new Timer();
        autoUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        mSelectPosition += 1;
                        if (mSelectPosition >= wheelListView.getCount()) {
                            mSelectPosition = 0;
                        }
                          wheelListView.smoothScrollToPosition(mSelectPosition);
                          wheelListView.setSelection(mSelectPosition);
//                        listView.setSelection(Integer.MAX_VALUE/2+1);
                        Log.d(TAG," autoUpdate index:"+mSelectPosition);
                    }
                });
            }
        }, 0, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            autoUpdate.cancel();
        }
    }

    private void initData() {
        for (int i = 0; i < 15; i++) {
            AssistantManager manager = new AssistantManager();
            manager.setName("CF-12345678 -->" + i);
            manager.setNickName("00:0C:04:11:03:05-->" + i);
            assistantManagers.add(manager);
        }
        baseHandler.sendEmptyMessageDelayed(1, 200);
    }

    private int mSelectPosition;
    private class TravelListAdapter extends WheelAdapter<AssistantManager> {

        /**
         * @param context 上下文
         * @param data    数据源
         * @param id      item的布局资源文件
         */
        public TravelListAdapter(Context context, List data, int id) {
            super(context, data, id);
        }

        @Override
        public void onHandleScroll(int selectPosition) {
//            Log.d(TAG," onHandleScroll selectPosition:"+selectPosition);
            mSelectPosition = selectPosition;
            notifyDataSetChanged();
        }

        @Override
        public void covertView(SimpleAdapterHolder holder, int position, List<AssistantManager> dataSource, AssistantManager manager) {
            LinearLayout content = holder.getView(R.id.layout_content);
            content.setBackgroundColor(Color.BLACK);
            float  scale = 0.4f;
            if (mSelectPosition == position - 1 || mSelectPosition == position + 1) {
                scale = 0.7f;
                content.setBackgroundColor(Color.RED);
            } else if (mSelectPosition == position) {
                scale = 1f;
                content.setBackgroundColor(Color.BLUE);
            }
            TextView tvName = holder.getView(R.id.tv_name);
            TextView tv = holder.getView(R.id.tv);

//            tvName.setText(dataSource.get(position).getNickName());
            tvName.setText(dataSource.get(position % dataSource.size()).getNickName());
//            tv.setText(dataSource.get(position).getName());
            tv.setText(dataSource.get(position % dataSource.size()).getName());
            ViewHelper.setScaleX(holder.getmConvertView().findViewById(R.id.layout_content), scale);
            ViewHelper.setScaleY(holder.getmConvertView().findViewById(R.id.layout_content), scale);
        }
    }
}
