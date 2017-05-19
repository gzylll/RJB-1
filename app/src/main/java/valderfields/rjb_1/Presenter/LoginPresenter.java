package valderfields.rjb_1.Presenter;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Observable;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
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
import valderfields.rjb_1.View.Activity.LoginActivity;

/**
 * Created by 11650 on 2017/5/13.
 */

public class LoginPresenter extends Observable{

    private LoginActivity activity;
    private Handler myHandler;

    public LoginPresenter(LoginActivity activity) {
        this.activity = activity;
        this.myHandler = activity.myHandler;
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

    public void Register(String un, String pw,String phone) {
        final RequestBody requestBody = new FormBody.Builder()
                .add("username", un)
                .add("password", EncodeUtil.shaEncode(pw))
                .add("phone",phone)
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
        Toast.makeText(activity,m,Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    public void initSDK(){
        SMSSDK.initSDK(activity, "1df79ee15fafb", "e18c7e5956e6dea1afa0ed8045e969ae");
        EventHandler eh=new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message message = myHandler.obtainMessage(0x00);
                message.arg1 = event;
                message.arg2 = result;
                message.obj = data;
                myHandler.sendMessage(message);
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    public void updateButton(){
        new Thread() {
            @Override
            public void run() {
                int totalTime = 60;
                for (int i = 0; i < totalTime; i++) {

                    Message message = myHandler.obtainMessage(0x01);
                    message.arg1 = totalTime - i;
                    myHandler.sendMessage(message);
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                myHandler.sendEmptyMessage(0x02);
            }
        }.start();
    }
}
