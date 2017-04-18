package valderfields.rjb_1;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;

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
        ViewHolder viewHolder = null;
        View convertView = null;
        if(mViewCache.size() == 0){
            /*实例化
            convertView = this.mLayoutInflater.inflate(R.layout.viewadapter_item_layout , null ,false);
            TextView textView = (TextView)convertView.findViewById(R.id.view_pager_item_textview);
            viewHolder = new ViewHolder();
            viewHolder.textView = textView;
            */
            convertView.setTag(viewHolder);
        }else {
            convertView = mViewCache.removeFirst();
            viewHolder = (ViewHolder)convertView.getTag();
        }
        /*填数据
        viewHolder.textView.setText(datas.get(position).title);
        viewHolder.textView.setTextColor(Color.YELLOW);
        viewHolder.textView.setBackgroundColor(Color.GRAY);
        */
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

    }
}
