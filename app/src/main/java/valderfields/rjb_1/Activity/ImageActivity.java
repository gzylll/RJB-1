package valderfields.rjb_1.Activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import valderfields.rjb_1.ImageClickListener;
import valderfields.rjb_1.ImageData;
import valderfields.rjb_1.Bean.Image;
import valderfields.rjb_1.PageTransformer;
import valderfields.rjb_1.R;
import valderfields.rjb_1.ViewPagerAdapter;
import valderfields.rjb_1.SlidingMenu;

public class ImageActivity extends AppCompatActivity implements Observer,ViewPager.OnPageChangeListener{

    private ViewPagerAdapter adapter;
    private ImageData imageData;
    private ImageClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        getSupportActionBar().hide();
        //初始化主界面元素
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        final SlidingMenu menu = (SlidingMenu)findViewById(R.id.menu);
        //初始化viewPager数据
        imageData = new ImageData();
        imageData.init(this);
        imageData.addObserver(this);
        //初始化viewPager
        listener = new ImageClickListener(this);
        adapter = new ViewPagerAdapter(this, listener);
        viewPager.setPageTransformer(true,new PageTransformer());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        listener.SetSlidingMenu(menu);
    }

    /**
     * 接收presenter传来的更新图片，并通知adapter更新
     * @param o Observable
     * @param arg 更新的图片
     */
    @Override
    @SuppressWarnings("unchecked")
    public void update(Observable o, Object arg) {
        if(o instanceof ImageData){
            Log.e("getData","update Start at:"+new Date().toString());
            final List<Image> images = (List<Image>)arg;
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                    Log.e("getData","update End at:"+new Date().toString());
                }
            });
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(ImageData.imageList.size()>1&&position==ImageData.imageList.size()-1){
            imageData.getData();
        }
        //更新弹出框数据
        listener.UpdateViewData(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
