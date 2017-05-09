package valderfields.rjb_1.Activity;

import android.os.Looper;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import valderfields.rjb_1.Bean.NetUtil;
import valderfields.rjb_1.Bean.User;
import valderfields.rjb_1.Bean.jxJSON;
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
    private int position = 0;
    private boolean first = true;

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
        listener.addObserver(this);
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
            final List<Image> images = (List<Image>)arg;
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(first){
                        listener.UpdateViewData(0);
                        first = false;
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }
        else if(o instanceof ImageClickListener){
            if(arg.equals("skip")){
                imageData.Remove(position);
                this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
            else{
                submitTag(position,arg.toString());
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.position = position;
        if((ImageData.imageList.size()- position)<=3){
            imageData.getData();
        }
        //更新弹出框数据
        listener.UpdateViewData(this.position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void submitTag(int position,String tag){
        Image image = ImageData.imageList.get(position);
        final RequestBody body = new FormBody.Builder()
                .add("tags",tag)
                .add("id",image.Id)
                .add("uid",User.getUID())
                .add("name",image.Name)
                .build();
        new Thread(){
            @Override
            public void run() {
                Request request = NetUtil.getRequest(NetUtil.getSubmitTagUrl(), body);
                NetUtil.getOkHttpClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("submit", "Failure");
                        Log.i("submit", e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.code()!=200){
                            Looper.prepare();
                            Toast.makeText(ImageActivity.this,"提交错误",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }.start();
    }
}
