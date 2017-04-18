package valderfields.rjb_1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 登录操作
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private Button login;
    private Button register;
    private EditText username;
    private EditText password;
    private CheckBox remPWD,autoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    private void initView(){
        login = (Button)findViewById(R.id.dl);
        login.setOnClickListener(this);
        register = (Button)findViewById(R.id.zc);
        register.setOnClickListener(this);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        remPWD = (CheckBox)findViewById(R.id.rempwn);
        autoLogin = (CheckBox)findViewById(R.id.autologin);
        getSupportActionBar().setTitle("登录");
    }

    private void Login(){
        String un = username.getText().toString().trim();
        String pw = password.getText().toString().trim();
        if(!un.equals("")&&!pw.equals("")){
            //登录
            final RequestBody requestBody = new FormBody.Builder()
                    .add("username",un)
                    .add("password",EncodeUtil.shaEncode(pw))
                    .build();

            new Thread(){
                public void run(){
                    Request request = NetUtil.getRequest(NetUtil.getLoginUrl(),requestBody);
                    NetUtil.getOkHttpClient().newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.i("Login","Failure");
                            Log.i("Login",e.getMessage());
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            //Log.i("Login",response.body().string());
                            jxJSON.Ceshi(response.body().string());
                        }
                    });
                }
            }.start();
        }
        else{
            Toast.makeText(this,"用户名和密码不能为空",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dl:
                Login();
                break;
            case R.id.zc:
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
