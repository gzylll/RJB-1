package valderfields.rjb_1.View.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import valderfields.rjb_1.Model.NetUtil;
import valderfields.rjb_1.Model.User;
import valderfields.rjb_1.Presenter.ImagePresenter;
import valderfields.rjb_1.Presenter.ImageDataPresenter;
import valderfields.rjb_1.Model.Image;
import valderfields.rjb_1.View.CustomView.PageTransformer;
import valderfields.rjb_1.R;
import valderfields.rjb_1.View.CustomView.SlidingMenu;
import valderfields.rjb_1.View.CustomView.ViewPagerAdapter;

public class ImageActivity extends AppCompatActivity implements
                Observer,ViewPager.OnPageChangeListener,View.OnClickListener,View.OnTouchListener{

    private ViewPagerAdapter adapter;
    private ImageDataPresenter imageDataPresenter;
    private ImagePresenter presenter;
    private int position = 0;
    private boolean first = true;

    private TextView name;
    private View myInfo;
    private View Quit;
    private View myRecord;

   private Handler handler = new Handler(){
       @Override
       public void handleMessage(Message msg) {
            Bundle b = msg.getData();
            if(b.getBoolean("onResponse",false)){
                imageDataPresenter.Remove(position);
                adapter.notifyDataSetChanged();
                presenter.closePopwindow();
            }
            else {
                Toast.makeText(ImageActivity.this,"提交错误",Toast.LENGTH_SHORT).show();
            }
       }
   };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        getSupportActionBar().hide();
        final SlidingMenu menu = (SlidingMenu)findViewById(R.id.menu);
        presenter = new ImagePresenter(this);
        presenter.SetSlidingMenu(menu);
        presenter.addObserver(this);
        initView();
        initViewPager();
    }

    private void initView(){
        name = (TextView)findViewById(R.id.myName);
        name.setText(User.getUsername());
        myInfo = findViewById(R.id.myInfo);
        myInfo.setOnClickListener(this);
        myInfo.setOnTouchListener(this);
        Quit = findViewById(R.id.Quit);
        Quit.setOnClickListener(this);
        myRecord = findViewById(R.id.myRecord);
        myRecord.setOnClickListener(this);
    }

    private void initViewPager(){
        //初始化viewPager
        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        //初始化viewPager数据
        imageDataPresenter = new ImageDataPresenter();
        imageDataPresenter.init(this);
        imageDataPresenter.addObserver(this);
        adapter = new ViewPagerAdapter(this, presenter);
        viewPager.setPageTransformer(true,new PageTransformer());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

    }
    /**
     * 接收presenter传来的更新图片，并通知adapter更新
     * @param o Observable
     * @param arg 更新的图片
     */
    @Override
    @SuppressWarnings("unchecked")
    public void update(Observable o, Object arg) {
        if(o instanceof ImageDataPresenter){
            final List<Image> images = (List<Image>)arg;
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(first){
                        presenter.UpdateViewData(0);
                        first = false;
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }
        else if(o instanceof ImagePresenter){
            if(arg.equals("skip")){
                imageDataPresenter.Remove(position);
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
        if((ImageDataPresenter.imageList.size()- position)<=3){
            imageDataPresenter.getData();
        }
        //更新弹出框数据
        presenter.UpdateViewData(this.position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void submitTag(final int position, String tag){
        Image image = ImageDataPresenter.imageList.get(position);
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
                        Bundle bundle = new Bundle();
                        if(response.code()!=200){
                            bundle.putBoolean("onResponse",false);
                        }
                        else{
                            bundle.putBoolean("onResponse",true);
                        }
                        Message message = handler.obtainMessage();
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }
                });
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        Log.e("onTouchEvent","Click");
        Intent intent;
        switch(v.getId()){
            case R.id.myInfo:
                Toast.makeText(this,"Click myInfo",Toast.LENGTH_SHORT).show();
                intent = new Intent(this,PersonalActivity.class);
                startActivity(intent);
                break;
            case R.id.Quit:
                intent = new Intent(this,LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.myRecord:
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.e("onTouchEvent", "Touch");
        Intent intent;
        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                Log.e("action down here","ontouch");
                return false;
            case MotionEvent.ACTION_UP:
                switch (v.getId()) {
                    case R.id.myInfo:
                        Toast.makeText(this, "Click myInfo", Toast.LENGTH_SHORT).show();
                        intent = new Intent(this, PersonalActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.Quit:
                        intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.myRecord:
                        break;
                }
                return true;
            default:
                return false;
        }
    }

}
