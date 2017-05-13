package valderfields.rjb_1.Presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Observable;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import valderfields.rjb_1.Model.EncodeUtil;
import valderfields.rjb_1.Model.NetUtil;
import valderfields.rjb_1.Model.jxJSON;
import valderfields.rjb_1.MyApplication;
import valderfields.rjb_1.View.Activity.ImageActivity;
/**
 * Created by 11650 on 2017/5/13.
 */

public class LoginPresenter extends Observable{

    private Context context;

    public LoginPresenter(Context context) {
        this.context = context;
    }

    public void Login(String un, String pw) {
        //登录
        final RequestBody requestBody;
        requestBody = new FormBody.Builder()
                .add("username", un)
                .add("password", pw)
                .build();
        new Thread() {
            public void run() {
                Request request = NetUtil.getRequest(NetUtil.getLoginUrl(), requestBody);
                NetUtil.getOkHttpClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("Login", "Failure");
                        Log.i("Login", e.getMessage());
                        ShowMessage("Login Request onFailure！");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.code()!=200){
                            ShowMessage("网络错误！");
                        }else{
                            String s = response.body().string();
                            Log.e("return",s);
                            if(jxJSON.jxLoginData(s)){
                                setChanged();
                                notifyObservers("Login Success");
                            }else{
                                ShowMessage(s);
                            }
                        }
                    }
                });
            }
        }.start();
    }

    public void Register(String un, String pw) {
        final RequestBody requestBody = new FormBody.Builder()
                .add("username", un)
                .add("password", EncodeUtil.shaEncode(pw))
                .build();
        //注册
        new Thread() {
            public void run() {
                Request request = NetUtil.getRequest(NetUtil.getRegisterUrl(), requestBody);
                NetUtil.getOkHttpClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.i("Register", "Failure");
                        Log.i("Register", e.getMessage());
                        ShowMessage("Register Request onFailure！");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.code()!=200){
                            ShowMessage("网络错误！");
                        }
                        else{
                            if(response.body().string().equals("success")){
                                setChanged();
                                notifyObservers("Register Success");
                                ShowMessage("注册成功！");
                            }
                            else{
                                ShowMessage("注册失败！");
                            }
                        }
                    }
                });
            }
        }.start();
    }

    public void ShowMessage(String m){
        Looper.prepare();
        Toast.makeText(context,m,Toast.LENGTH_SHORT).show();
        Looper.loop();
    }
}
