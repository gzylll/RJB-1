package valderfields.rjb_1.Presenter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Observable;

import valderfields.rjb_1.Model.Image;
import valderfields.rjb_1.R;
import valderfields.rjb_1.View.CustomView.FlowLayout;
import valderfields.rjb_1.View.CustomView.SlidingMenu;

/**
 * Image界面点击处理类
 * Created by 11650 on 2017/5/4.
 */

public class ImagePresenter extends Observable implements View.OnClickListener{
    //上下文
    public Context context;
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
    private FlowLayout tagsArea;
    private Button skip;
    private Button submit;

    public ImagePresenter(Context context){
        Log.e("ImagePresenter","Create");
        this.context = context;
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
        tagsArea = (FlowLayout)popBottomView.findViewById(R.id.TagArea);
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
        Image cImage = ImageDataPresenter.imageList.get(position);
        imageName.setText(cImage.Name);
        if(cImage.Tags==null){
            noneTags.setVisibility(View.VISIBLE);
            tagsArea.setVisibility(View.GONE);
        }
        else{
            for(int i=0;i<cImage.Tags.length;i++){
                final Button b = new Button(context);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                ViewGroup.MarginLayoutParams mlp = new ViewGroup.MarginLayoutParams(lp);
                mlp.setMargins(15,10,15,10);
                b.setLayoutParams(mlp);
                b.setText(cImage.Tags[i]);
                //b.setBackground(context.getResources().getDrawable(R.drawable.tag_button));
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inputTag.setText(b.getText());
                    }
                });
                tagsArea.addView(b);
            }
        }
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
                top.dismiss();
                bottom.dismiss();
                setChanged();
                notifyObservers("skip");
                break;
            case R.id.submit:
                String tag = inputTag.getText().toString();
                if(!tag.equals("")){
                    inputTag.setText("");
                    setChanged();
                    notifyObservers(tag);
                }
                else{
                    Toast.makeText(context,"标签不能为空",Toast.LENGTH_SHORT).show();
                }
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
            bottom.setOutsideTouchable(true);
            bottom.setFocusable(true);
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
