package valderfields.rjb_1;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import valderfields.rjb_1.Bean.Image;

/**
 * Image界面点击处理类
 * Created by 11650 on 2017/5/4.
 */

public class ImageClickListener implements View.OnClickListener {

    //点击之后的弹出框和状态
    private PopupWindow top;
    private View popTopView;
    private PopupWindow bottom;
    private View popBottomView;
    private boolean isShow = false;
    private LayoutInflater mLayoutInflater;
    //slidingmenu
    private SlidingMenu slinding;
    //top组件
    private ImageButton toPerson;
    private TextView imageName;
    //bottom组件
    private EditText inputTag;
    private TextView noneTags;
    private LinearLayout tagsArea;
    private Button skip;
    private Button submit;

    public ImageClickListener(Context context){
        mLayoutInflater = LayoutInflater.from(context);
        popTopView = mLayoutInflater.inflate(R.layout.image_popwindow_top, null, false);
        popBottomView = mLayoutInflater.inflate(R.layout.image_popwindow_bottom, null, false);
        //TOP
        top = new PopupWindow(popTopView, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        toPerson = (ImageButton) popTopView.findViewById(R.id.toPerson);
        toPerson.setOnClickListener(this);
        imageName = (TextView)popTopView.findViewById(R.id.image_name);
        //BOTTOM
        bottom = new PopupWindow(popBottomView, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        inputTag = (EditText)popBottomView.findViewById(R.id.tags);
        noneTags = (TextView)popBottomView.findViewById(R.id.nonetags);
        tagsArea = (LinearLayout)popBottomView.findViewById(R.id.TagArea);
        skip = (Button)popBottomView.findViewById(R.id.skip);
        skip.setOnClickListener(this);
        submit = (Button)popBottomView.findViewById(R.id.submit);
        submit.setOnClickListener(this);
    }


    //设置SlidingMenu
    public void SetSlidingMenu(SlidingMenu menu){
        slinding = menu;
    }

    /**
     * 更新弹出框的数据
     * @param position 当前选中的数据项
     */
    public void UpdateViewData(int position){
        Image cImage = ImageData.imageList.get(position);
        imageName.setText(cImage.Name);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.view_pager_item_ImageView:
                setPopWindow(v);
                break;
            case R.id.toPerson:
                isShow = false;
                top.dismiss();
                bottom.dismiss();
                slinding.toggle();
                break;
            case R.id.skip:
                break;
            case R.id.submit:
                break;
        }

    }

    /**
     * 设置弹出框位置和状态
     */
    private void setPopWindow(View v){
        if(isShow)
        {
            top.dismiss();
            bottom.dismiss();
            isShow = false;
        }
        else
        {
            //设置menu位置在底部
            bottom.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            bottom.showAtLocation(v, Gravity.BOTTOM,0,0);
            bottom.setFocusable(true);
            bottom.setOutsideTouchable(true);
            bottom.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
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
    }
}
