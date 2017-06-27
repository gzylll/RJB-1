package valderfields.rjb_1.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Observable;
import java.util.Observer;

import cn.smssdk.SMSSDK;
import valderfields.rjb_1.model.EncodeUtil;
import valderfields.rjb_1.model.User;
import valderfields.rjb_1.presenter.LoginPresenter;
import valderfields.rjb_1.R;
import valderfields.rjb_1.view.customView.Rotate3DAnimation;

/**
 * 登录和注册操作
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener,Observer{

    String TAG = "LoginActivity";
    //login
    private Button bLogin;
    private TextView register;
    private EditText username_Login;
    private EditText password_Login;
    private CheckBox remPWD,autoLogin;
    private View loginView;
    //register
    private View PhoneView;
    private EditText mEditTextPhoneNumber;
    private EditText mEditTextCode;
    private Button mButtonGetCode;
    private Button mButtonSubmitYZM;
    private View OtherView;

    private TextView cancel;
    private EditText username_Register;
    private EditText password_Register;
    private EditText password2_Register;
    private Button mButtonRegister;
    private View registerView;

    private LoginPresenter presenter;
    private View container;
    //animation
    private float centerX;
    private float centerY;
    private float Z = 0.0f;

    private String strPhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        initLoginView();
        initRegisterView();
        presenter = new LoginPresenter(this);
        presenter.addObserver(this);
        presenter.initSDK();
        container = findViewById(R.id.container);
    }

    private void initLoginView(){
        loginView = findViewById(R.id.user_login);
        bLogin = (Button)findViewById(R.id.dl);
        bLogin.setOnClickListener(this);
        register = (TextView)findViewById(R.id.zc);
        register.setOnClickListener(this);
        username_Login = (EditText)findViewById(R.id.username);
        password_Login = (EditText)findViewById(R.id.password);
        remPWD = (CheckBox)findViewById(R.id.rempwn);
        if(User.getRemember()){
            remPWD.setChecked(true);
            username_Login.setText(User.getUsername());
            password_Login.setText(User.getPassword());
        }
        autoLogin = (CheckBox)findViewById(R.id.autologin);
        if(User.getAuto())
            autoLogin.setChecked(true);
    }

    private void initRegisterView(){
        PhoneView = findViewById(R.id.registerPhone);
        OtherView = findViewById(R.id.registerMessage);
        registerView = findViewById(R.id.user_register);

        cancel = (TextView)findViewById(R.id.backToLogin);
        cancel.setOnClickListener(this);
        mEditTextPhoneNumber = (EditText)findViewById(R.id.number);
        mEditTextCode = (EditText)findViewById(R.id.yzm);
        username_Register = (EditText) findViewById(R.id.username_register);
        password_Register = (EditText) findViewById(R.id.password_register);
        password2_Register = (EditText) findViewById(R.id.password_reedit);
        mButtonRegister = (Button) findViewById(R.id.ZC);
        mButtonGetCode = (Button)findViewById(R.id.getyzm);
        mButtonGetCode.setOnClickListener(this);
        mButtonSubmitYZM = (Button)findViewById(R.id.submitYZM);
        mButtonSubmitYZM.setOnClickListener(this);
        mButtonRegister.setOnClickListener(this);
    }

    private void Login(){
        String un = username_Login.getText().toString().trim();
        String pw = password_Login.getText().toString().trim();
        pw=(pw.length()==40)?pw:EncodeUtil.shaEncode(pw);
        if(!un.equals("")&&!pw.equals("")){
            if(remPWD.isChecked()){
                User.setUsername(un);
                User.setPassword(pw);
                User.setRemember(true);
            }
            else{
                User.clear();
                User.setUsername(un);
            }
            if(autoLogin.isChecked()){
                User.setAuto(true);
            }
            //登录
            Log.e("Login","un:"+un+",pw:"+pw);
            presenter.Login(un,pw);
        }
        else{
            Toast.makeText(this,"用户名和密码不能为空",Toast.LENGTH_SHORT).show();
        }
    }

    private void Register(){
        String name = username_Register.getText().toString();
        String pwd1 = password_Register.getText().toString();
        String pwd2 = password2_Register.getText().toString();
        if(name.equals("")||pwd1.equals("")||pwd2.equals("")){
            Toast.makeText(this,"输入不能为空",Toast.LENGTH_SHORT).show();
        }
        else if(!pwd1.equals(pwd2)){
            Toast.makeText(this,"密码不一致",Toast.LENGTH_SHORT).show();
        }
        else if(pwd1.length()>20) {
            Toast.makeText(this,"密码不超过20位",Toast.LENGTH_SHORT).show();
        }
        else{
            presenter.Register(name,pwd1,strPhoneNumber);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dl:
                Login();
                break;
            case R.id.zc:
                Rotate3DTo(1);
                break;
            case R.id.ZC:
                Register();
                break;
            case R.id.backToLogin:
                Rotate3DTo(2);
                break;
            case R.id.getyzm:
                strPhoneNumber = mEditTextPhoneNumber.getText().toString();
                if ("".equals(strPhoneNumber) || strPhoneNumber.length() != 11) {
                    Toast.makeText(LoginActivity.this, "电话号码输入有误", Toast.LENGTH_SHORT).show();
                    return;
                }
                SMSSDK.getVerificationCode("86", strPhoneNumber);
                mButtonGetCode.setClickable(false);
                //开启线程去更新button的text
                presenter.updateButton();
                break;
            case R.id.submitYZM:
                String strCode = mEditTextCode.getText().toString();
                if (null != strCode && strCode.length() == 4) {
                    Log.d(TAG, mEditTextCode.getText().toString());
                    SMSSDK.submitVerificationCode("86", strPhoneNumber, mEditTextCode.getText().toString());
                } else {
                    Toast.makeText(LoginActivity.this, "密码长度不正确", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof LoginPresenter){
            Log.e("Re",arg.toString());
            if(arg.equals("Login Success"))
            {
                Intent intent = new Intent(this,ImageActivity.class);
                startActivity(intent);
                finish();
            }
            if(arg.equals("Register Success"))
            {
                Log.e("Re","re");
                Rotate3DTo(2);
            }
        }
    }

    /**
     * 调用旋转动画
     * @param i 表示旋转方向的参数
     *          1：转到注册
     *          2：转到登录
     */
    public void Rotate3DTo(int i){
        centerX = container.getWidth()/2;
        centerY = container.getHeight()/2;
        final Rotate3DAnimation r3a;
        if(i==1){
            r3a = new Rotate3DAnimation(0,90,centerX,centerY,Z);
        } else {
          r3a = new Rotate3DAnimation(0,-90,centerX,centerY,Z);
        }
        r3a.setAnimationListener(new MyAnimationListener(i));
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        container.startAnimation(r3a);
                    }
                }
        );
    }

    private class MyAnimationListener implements Animation.AnimationListener{

        private int direction;

        public MyAnimationListener(int i){
            direction = i;
        }

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            container.post(
                    new Thread(new Runnable() {
                @Override
                public void run() {
                    Rotate3DAnimation r3a;
                    if (direction==1) {
                        loginView.setVisibility(View.GONE);
                        registerView.setVisibility(View.VISIBLE);
                        registerView.requestFocus();
                        r3a = new Rotate3DAnimation(-90, 0, centerX, centerY, Z);
                    } else {
                        registerView.setVisibility(View.GONE);
                        loginView.setVisibility(View.VISIBLE);
                        loginView.requestFocus();
                        r3a = new Rotate3DAnimation(90, 0, centerX, centerY, Z);
                    }
                    container.startAnimation(r3a);
                }
            }));
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private void updateRegisterView(){
        PhoneView.setVisibility(View.GONE);
        OtherView.setVisibility(View.VISIBLE);
        PhoneView.setAnimation(AnimationUtils.makeOutAnimation(this, false));
        // 向右边移入
        OtherView.setAnimation(AnimationUtils.makeInAnimation(this, false));

    }

    public Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        //回调  当返回的结果是complete
                        if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            //获取验证码
                            Toast.makeText(LoginActivity.this, "发送验证码成功", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "get verification code successful.");
                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            //提交验证码
                            Log.d(TAG, "submit code successful");
                            Toast.makeText(LoginActivity.this, "提交验证码成功", Toast.LENGTH_SHORT).show();
                            updateRegisterView();
                        } else {
                            Log.d(TAG, data.toString());
                        }
                    } else {
                        //进行操作出错，通过下面的信息区分析错误原因
                        try {
                            Throwable throwable = (Throwable) data;
                            throwable.printStackTrace();
                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");//错误描述
                            int status = object.optInt("status");//错误代码
                            //错误代码：
                            // http://wiki.mob.com/android-api-%E9%94%99%E8%AF%AF%E7%A0%81%E5%8F%82%E8%80%83/
                            Log.e(TAG, "status: " + status + ", detail: " + des);
                            if (status > 0 && !TextUtils.isEmpty(des)) {
                                Toast.makeText(LoginActivity.this, des, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 0x01:
                    mButtonGetCode.setText("重新发送(" + msg.arg1 + ")");
                    break;
                case 0x02:
                    mButtonGetCode.setText("获取验证码");
                    mButtonGetCode.setClickable(true);
                    break;
            }
        }
    };
}
