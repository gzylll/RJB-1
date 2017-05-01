package valderfields.rjb_1.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;

import java.util.LinkedList;
import java.util.List;

import valderfields.rjb_1.Bean.Image;
import valderfields.rjb_1.R;

/**
 * 自定义ViewPagerAdapter
 * Created by 11650 on 2017/4/18.
 */

public class ViewPagerAdapter extends PagerAdapter {

    //显示的数据
    private List<Image> dataList = null;
    //显示的View,复用
    private LinkedList<View> mViewCache = null;
    private Context context;
    private LayoutInflater mLayoutInflater = null;
    //点击之后的弹出窗口
    private PopupWindow top;
    private PopupWindow bottom;
    private boolean isShow = false;
    //布局点击事件
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("click","Click");
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
        }
    };


    public ViewPagerAdapter(List<Image> list,Context context){
        super();
        this.dataList=list;
        this.context=context;
        this.mLayoutInflater=LayoutInflater.from(context);
        this.mViewCache=new LinkedList<>();
        View popTopView = mLayoutInflater.inflate(R.layout.image_popwindow_top, null, false);
        View popBottomView = mLayoutInflater.inflate(R.layout.image_popwindow_bottom, null, false);
        top = new PopupWindow(popTopView, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        bottom = new PopupWindow(popBottomView, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    /**
     * 加载界面的时候调用
     * @param container ViewGroup
     * @param position 位置
     * @return View
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ViewHolder viewHolder;
        View convertView;
        if(mViewCache.size() == 0){
            //实例化一个写入缓存
            convertView = this.mLayoutInflater.inflate(R.layout.items_viewpager , null ,false);
            ImageView imageView= (ImageView)convertView.findViewById(R.id.view_pager_item_ImageView);
            imageView.setOnClickListener(onClickListener);
            viewHolder = new ViewHolder();
            viewHolder.mImage = imageView;
            convertView.setTag(viewHolder);
        }else {
            convertView = mViewCache.removeFirst();
            viewHolder = (ViewHolder)convertView.getTag();
        }
        //填数据
        viewHolder.mImage.setImageBitmap(dataList.get(position).bitmap);
        container.addView(convertView ,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT );
        return convertView;
    }

    /**
     * 在销毁view时回收，实现复用
     * @param container ViewGroup
     * @param position 位置
     * @param object viewObject
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View contentView = (View) object;
        container.removeView(contentView);
        this.mViewCache.add(contentView);
    }

    /**
     * 重写函数，在adapter的notifyDataSetChanged的时候重绘界面
     * @param object itemObject
     * @return POSITION_NONE
     */
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    public final class ViewHolder{
        ImageView mImage;
    }
}
