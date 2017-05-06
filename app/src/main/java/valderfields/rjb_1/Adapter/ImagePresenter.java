package valderfields.rjb_1.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import valderfields.rjb_1.Bean.Image;
import valderfields.rjb_1.Bean.NetUtil;
import valderfields.rjb_1.Bean.jxJSON;

/**
 * Image界面逻辑,监控滑动，加载数据。
 * Created by 11650 on 2017/5/6.
 */

public class ImagePresenter extends Observable implements ViewPager.OnPageChangeListener {

    public List<Image> images = new ArrayList<>();
    private Context context;

    public ImagePresenter(Context context,ViewPager pager){
        this.context = context;
        pager.addOnPageChangeListener(this);
    }


    /**
     * 启动事务，第一次加载图片。
     */
    public void start() {
        getData();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if(position==images.size()-1){
            //getData();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    public void getData(){
        final RequestBody body = new FormBody.Builder().build();
        new Thread() {
            public void run() {
                Request request = NetUtil.getRequest(NetUtil.getRequestImageUrl(), body);
                NetUtil.getOkHttpClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("Register", "Failure");
                        Log.i("Register", e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.code()!=200){
                            Toast.makeText(context,"请求错误",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            images = jxJSON.jxImageReturn(response.body().string());
                            getBitmap(images);
                        }
                    }
                });
            }
        }.start();
    }

    /**
     * 从URL获取图片
     * @param images 图片列表
     * @throws IOException IO异常
     */
    public void getBitmap(List<Image> images) throws IOException {
        for(int i=0;i<images.size();i++){
            URL url = new URL(images.get(i).Url);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(500);
            conn.setRequestMethod("GET");
            if(conn.getResponseCode() == 200){
                InputStream inputStream = conn.getInputStream();
                images.get(i).bitmap = BitmapFactory.decodeStream(inputStream);
            }
        }
        setChanged();
        notifyObservers(images);
    }
}
