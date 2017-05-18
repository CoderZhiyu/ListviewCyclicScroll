package com.cfuas.admin.mytest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * 包名：com.ykbjson.demo.customview.listview
 * 描述：类似WheelView的ListView
 * 创建者：yankebin
 * 日期：2016/6/1
 */
public class WheelListView extends ListView {
    private static final int MAX_Y_OVERSCROLL_DISTANCE = 200;
    private final Object SCROLL_LOCK = new Object();

    private int topY;
    private int middleY;
    private int bottomY;
    private int selectPosition;
    private boolean fromTouch;
    private WheelAdapter wheelAdapter;
    private OnSelectCallback callback;
    private int mMaxYOverScrollDistance;

    public interface OnSelectCallback {
        void onHandleScroll(int selectPosition);

        void onHandleIdle(WheelListView wheelListView, int selectPosition);
    }

    public WheelListView(Context context) {
        this(context, null);
    }

    public WheelListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WheelListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        initBounceListView();
        setClipChildren(false);
        setClipToPadding(false);
    }

    /**
     * 阻尼效果实现
     */
//    private void initBounceListView(){
//        //get the density of the screen and do some maths with it on the max overscroll distance
//        //variable so that you get similar behaviors no matter what the screen size
//        final DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
//        final float density = metrics.density;
//        mMaxYOverScrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
//    }
//
//    @Override
//    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent){
//        //This is where the magic happens, we have replaced the incoming maxOverScrollY with our own custom variable mMaxYOverScrollDistance;
//        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxYOverScrollDistance, isTouchEvent);
//    }
    public void setAdapter(WheelAdapter adapter) {
        super.setAdapter(adapter);
        wheelAdapter = adapter;
        setCallback(adapter);
    }

    private void setCallback(OnSelectCallback callback) {
        this.callback = callback;
    }

    /**
     * 初始化
     *
     * @param selectView
     */
    protected void setUp(View selectView, View rootView) {
        if (null == selectView || null == rootView) {
            return;
        }
        setPadding(0, selectView.getTop() - 30, 0, rootView.getBottom() - selectView.getBottom() + 30);

        int location1[] = new int[2];
        selectView.getLocationOnScreen(location1);

        topY = location1[1];
        middleY = topY + selectView.getMeasuredHeight() / 2;
        bottomY = topY + selectView.getMeasuredHeight();

        setUpScroll();
    }

    /**
     * 设置滚动监听
     */
    public void setUpScroll() {
        setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (null == callback) {
                    return;
                }
                if (scrollState == SCROLL_STATE_IDLE) {
                    if (!fromTouch) {
                        return;
                    }
                    fromTouch = false;
//                    SLog.d("SCROLL_STATE_IDLE");
                    //adapter实现了callback接口
                    callback.onHandleIdle(WheelListView.this,selectPosition);
//                    wheelAdapter.notifyDataSetChanged();
//                    smoothScrollToPositionFromTop(selectPosition, 0, 400);
//                    requestLayout();
                } else if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    fromTouch = true;
                }
//                Log.d("MainActivity"," onScrollStateChanged fromTouch:"+fromTouch+"  selectPosition:"+selectPosition);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (null == callback) {
                    return;
                }
                if (!fromTouch) {
                    return;
                }
                synchronized (SCROLL_LOCK) {
                    handleScroll(firstVisibleItem);
                }
//                Log.d("MainActivity"," onScroll firstVisibleItem:"+firstVisibleItem+"  visibleItemCount:"+visibleItemCount);
                /** 到顶部添加数据关键 **/
//                if (firstVisibleItem <= 2) {
//                    setSelection(12 + 2);
//                } else if (firstVisibleItem + visibleItemCount > wheelAdapter.getCount() - 2) {//到底部添加数据
//                    setSelection(firstVisibleItem - 12);
//                }
            }
        });
    }

    /**
     * 处理滚动
     *
     * @param firstVisibleItem
     */
    private void handleScroll(int firstVisibleItem) {
        //取出与中线距离最近的item
        int tempD = -1;
        //本次滑动计算出的position
        int tempP = -1;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            int location2[] = new int[2];
            child.getLocationOnScreen(location2);
            int childBottom = location2[1] + child.getMeasuredHeight();
            int childTop = location2[1];
//            SLog.d("bottomY : " + bottomY + " topY : " + topY + " middleY : " + middleY + " childBottom : " + childBottom + " childTop : " + childTop);
            if (childBottom < topY || childTop > bottomY) {
                continue;
            }
            int childMiddleY = childBottom - child.getMeasuredHeight() / 2;
            int position = firstVisibleItem + i;//当前item真正的position
            int distance = Math.abs(middleY - childMiddleY);
            if (tempD == -1) {
                tempD = distance;
                tempP = position;
            } else if (tempD > distance) {
                tempD = distance;
                tempP = position;
            }
            //这种方式也可以实现，但是总感觉有问题
//            if (childTop >= topY && childBottom <= bottomY) {
//                selectPosition = position;
//            } else if (childTop < topY) {
//                if (childBottom < middleY) {
//                    selectPosition = position + 1;
//                } else {
//                    selectPosition = position;
//                }
//            } else {
//                if (childTop < middleY) {
//                    selectPosition = position - 1;
//                } else {
//                    selectPosition = position;
//                }
//            }
//            SLog.d("position: " + position);
//            break;
        }

        if (tempP < 0) {
            tempP = 0;
        } else if (tempP > wheelAdapter.getCount() - 1) {
            tempP = wheelAdapter.getCount() - 1;
        }
        //防止多次notify同一个position
        if (selectPosition == tempP) {
            return;
        }
        selectPosition = tempP;
        callback.onHandleScroll(selectPosition);
//        Log.d("MainActivity"," handleScroll firstVisibleItem:"+selectPosition);
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }
}
