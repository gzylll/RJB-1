package valderfields.rjb_1.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import valderfields.rjb_1.R;
import valderfields.rjb_1.SlidingMenu;

/**
 * Created by 11650 on 2017/5/4.
 */

public class ImageClickListener implements View.OnClickListener {

    private static ImageClickListener listener;
    //点击之后的弹出框和状态
    private static PopupWindow top;
    private static PopupWindow bottom;
    private boolean isShow = false;
    private static LayoutInflater mLayoutInflater;
    //slidingmenu
    private static SlidingMenu slinding;
    //top组件
    private static ImageButton toPerson;
    //bottom组件

    //弹出框等的初始化
    private static void init(Context context){
        listener = new ImageClickListener();
        mLayoutInflater = LayoutInflater.from(context);
        View popTopView = mLayoutInflater.inflate(R.layout.image_popwindow_top, null, false);
        View popBottomView = mLayoutInflater.inflate(R.layout.image_popwindow_bottom, null, false);
        top = new PopupWindow(popTopView, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        toPerson = (ImageButton) popTopView.findViewById(R.id.toPerson);
        toPerson.setOnClickListener(listener);
        TextView tv = (TextView)popTopView.findViewById(R.id.image_num);
        tv.setText("hahaha");
        bottom = new PopupWindow(popBottomView, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
    }

    //获取listener
    public static ImageClickListener getInstance(Context context){
        if(listener==null){
            init(context);
        }
        return listener;
    }

    //设置SlidingMenu
    public static void SetSlidingMenu(SlidingMenu menu){
        slinding = menu;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.view_pager_item_ImageView:
                if(isShow)
                {
                    top.dismiss();
                    bottom.dismiss();
                    isShow = false;
                }
                else
                {
                    //设置menu位置在底部
                    bottom.showAtLocation(v, Gravity.BOTTOM,0,0);
                    bottom.setFocusable(false);
                    bottom.setOutsideTouchable(true);
                    bottom.setAnimationStyle(R.style.popwindow_bottom_anim);
                    bottom.update();

                    //设置title位置在顶部
                    top.showAtLocation(v, Gravity.TOP,0,0);
                    top.setFocusable(false);
                    top.setOutsideTouchable(true);
                    top.setAnimationStyle(R.style.popwindow_top_anim);
                    top.update();
                    isShow = true;
                }
                break;
            case R.id.toPerson:
                isShow = false;
                top.dismiss();
                bottom.dismiss();
                slinding.toggle();
                break;
        }

    }
}
