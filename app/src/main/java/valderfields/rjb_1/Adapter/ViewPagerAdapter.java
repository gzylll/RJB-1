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

    public ViewPagerAdapter(List<Image> list,Context context){
        super();
        this.dataList=list;
        this.context=context;
        this.mLayoutInflater=LayoutInflater.from(context);
        this.mViewCache=new LinkedList<>();
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
            //将监听给ImageCLickListener
            imageView.setOnClickListener(ImageClickListener.getInstance(context));
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
