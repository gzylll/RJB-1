package valderfields.rjb_1.Presenter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import valderfields.rjb_1.Model.Image;
import valderfields.rjb_1.Model.NetUtil;
import valderfields.rjb_1.Model.User;
import valderfields.rjb_1.Model.jxJSON;

/**
 * Created by 11650 on 2017/5/8.
 */

public class ImageDataPresenter extends Observable{

    public static List<Image> imageList = new ArrayList<>();
    private Context context;

    public void init(Context context){
        this.context = context;
        Image image = new Image();
        imageList.add(image);
        getData();
    }

    public void update(List<Image> images){
        imageList.remove(imageList.size()-1);
        for (Image image: images) {
            imageList.add(image);
        }
        Image image = new Image();
        imageList.add(image);
    }

    public void Remove(int position){
        imageList.remove(position);
        setChanged();
        notifyObservers();
    }

    public void getData(){
        Log.e("getData","getData Start at:"+new Date().toString());
        final RequestBody body = new FormBody.Builder()
                .add("uid", User.getUID())
                .build();
        new Thread() {
            public void run() {
                Request request = NetUtil.getRequest(NetUtil.getRequestImageUrl(), body);
                NetUtil.getOkHttpClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("getData", "Failure");
                        Log.i("getData", e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.code()!=200){
                            Looper.prepare();
                            Toast.makeText(context,"请求错误",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Log.e("getData","getData End at:"+new Date().toString());
                            List<Image> images = jxJSON.jxImageReturn(response.body().string());
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
    private void getBitmap(List<Image> images) throws IOException {
        Log.e("getData","getBitmap Start at:"+new Date().toString());
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
        Log.e("getData","getBitmap End at:"+new Date().toString());
        update(images);
        setChanged();
        notifyObservers();
    }

}
