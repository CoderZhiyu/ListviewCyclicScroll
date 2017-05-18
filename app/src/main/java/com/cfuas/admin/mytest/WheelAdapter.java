package com.cfuas.admin.mytest;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 包名：com.ykbjson.demo.customview.listview
 * 描述：类似WheelView的ListView的adapter
 * 创建者：yankebin
 * 日期：2016/6/1
 */
public abstract class WheelAdapter<T> extends BaseAdapter implements WheelListView.OnSelectCallback {

    /**
     * 数据源
     */
    private List<T> mData;
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * item布局索引
     */
    private int layoutId;


    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public List<T> getmData() {
        return mData;
    }

    public void setmData(List<T> data) {
        this.mData = data;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }


    /**
     * @param context 上下文
     * @param data    数据源
     * @param id      item的布局资源文件
     */
    public WheelAdapter(Context context, List<T> data, int id) {
        this.mContext = context;
        this.mData = data;
        this.layoutId = id;
    }

    /**
     * 数据源改变，刷新界面
           * @param data
     */
    public void refersh(List<T> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mData != null) {
//            return mData.size();
            return Integer.MAX_VALUE;
        }
        return 0;
    }

    @Override
    public T getItem(int position) {
        if (mData != null) {
            return mData.get(position  % mData.size());
//            return mData.get(position);
        }
        return null;
    }
    @Override
    public long getItemId(int position) {
        return position % Integer.MAX_VALUE;
//        return position % mData.size();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SimpleAdapterHolder holder = SimpleAdapterHolder.get(convertView, parent, layoutId,
                position);
        covertView(holder, position, mData, getItem(position));
        return holder.getmConvertView();
    }

    /**
     * 子类可重写此方法实现不同的滚动效果
     * @param wheelListView
     * @param selectPosition
     */
    @Override
    public void onHandleIdle(WheelListView wheelListView, int selectPosition) {
        notifyDataSetChanged();
        wheelListView.smoothScrollToPositionFromTop(selectPosition, 0, 400);
    }

    public abstract void covertView(SimpleAdapterHolder holder, int position, List<T> dataSource, T data);
}