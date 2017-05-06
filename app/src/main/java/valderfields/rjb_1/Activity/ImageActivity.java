package valderfields.rjb_1.Activity;

import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import valderfields.rjb_1.Adapter.ImageClickListener;
import valderfields.rjb_1.Adapter.ImagePresenter;
import valderfields.rjb_1.Bean.Image;
import valderfields.rjb_1.Adapter.PageTransformer;
import valderfields.rjb_1.R;
import valderfields.rjb_1.Adapter.ViewPagerAdapter;
import valderfields.rjb_1.SlidingMenu;

public class ImageActivity extends AppCompatActivity implements Observer{

    private List<Image> images = new ArrayList<>();
    private ImagePresenter presenter;
    private ImageClickListener listener;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        getSupportActionBar().hide();
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        final SlidingMenu menu = (SlidingMenu)findViewById(R.id.menu);
        presenter = new ImagePresenter(this,viewPager);
        presenter.start();
        presenter.addObserver(this);
        //initData();
        adapter = new ViewPagerAdapter(this);
        viewPager.setPageTransformer(true,new PageTransformer());
        viewPager.setAdapter(adapter);
        listener = ImageClickListener.getInstance(this);
        listener.SetSlidingMenu(menu);

    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(Observable o, Object arg) {
        if(o instanceof ImagePresenter){
            final List<Image> images = (List<Image>)arg;
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.Update(images);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }
}
