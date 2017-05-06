package valderfields.rjb_1.Adapter;

import android.content.Context;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import valderfields.rjb_1.Bean.Image;
import valderfields.rjb_1.R;

/**
 * 自定义ViewPagerAdapter
 * Created by 11650 on 2017/4/18.
 */

public class ViewPagerAdapter extends PagerAdapter{

    //显示的数据
    private List<Image> dataList = new ArrayList<>();
    //显示的View,复用
    private LinkedList<View> mViewCache = null;
    private Context context;
    private LayoutInflater mLayoutInflater = null;

    public ViewPagerAdapter(Context context){
        super();
        this.context=context;
        dataList.add(new Image());
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
            ProgressBar loading = (ProgressBar)convertView.findViewById(R.id.view_pager_item_Loading);
            viewHolder = new ViewHolder();
            viewHolder.mImage = imageView;
            viewHolder.loading = loading;
            convertView.setTag(viewHolder);
        }else {
            convertView = mViewCache.removeFirst();
            viewHolder = (ViewHolder)convertView.getTag();
        }
        //填数据
        Log.i("ceshi",String.valueOf(dataList.size()));
        if(dataList.get(position).bitmap!=null){
            Log.i("shuju",String.valueOf(position));
            viewHolder.loading.setVisibility(View.GONE);
            viewHolder.mImage.setVisibility(View.VISIBLE);
            viewHolder.mImage.setImageBitmap(dataList.get(position).bitmap);
        }
        else{
            viewHolder.loading.setVisibility(View.VISIBLE);
            viewHolder.mImage.setVisibility(View.GONE);
        }
        container.addView(convertView);
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

    public void Update(List<Image> images) {
        dataList = images;
    }

    private final class ViewHolder{
        ImageView mImage;
        ProgressBar loading;
    }
}
