package valderfields.rjb_1.Adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import valderfields.rjb_1.Bean.Image;
import valderfields.rjb_1.R;
import valderfields.rjb_1.SlidingMenu;

/**
 * Image界面点击处理类
 * Created by 11650 on 2017/5/4.
 */

public class ImageClickListener implements View.OnClickListener {

    private static ImageClickListener listener;
    //点击之后的弹出框和状态
    private static PopupWindow top;
    private static View popTopView;
    private static PopupWindow bottom;
    private static View popBottomView;
    private boolean isShow = false;
    private static LayoutInflater mLayoutInflater;
    //slidingmenu
    private SlidingMenu slinding;
    //top组件
    private static ImageButton toPerson;
    //bottom组件


    //弹出框等的初始化
    private static void init(Context context){
        listener = new ImageClickListener();
        mLayoutInflater = LayoutInflater.from(context);
        popTopView = mLayoutInflater.inflate(R.layout.image_popwindow_top, null, false);
        popBottomView = mLayoutInflater.inflate(R.layout.image_popwindow_bottom, null, false);
        top = new PopupWindow(popTopView, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        toPerson = (ImageButton) popTopView.findViewById(R.id.toPerson);
        toPerson.setOnClickListener(listener);
        //此处写入组件获取
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
    public void SetSlidingMenu(SlidingMenu menu){
        slinding = menu;
    }

    /**
     * 更新弹出框的数据
     * @param image 当前选中的数据
     */
    public static void UpdateView(Image image){

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
