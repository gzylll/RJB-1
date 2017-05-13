package valderfields.rjb_1.View.Activity;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import okhttp3.RequestBody;
import valderfields.rjb_1.Model.EncodeUtil;
import valderfields.rjb_1.Model.User;
import valderfields.rjb_1.Presenter.LoginPresenter;
import valderfields.rjb_1.R;
import valderfields.rjb_1.View.CustomView.Rotate3DAnimation;

/**
 * 登录和注册操作
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener,Observer{

    //login
    private Button bLogin;
    private TextView register;
    private EditText username_Login;
    private EditText password_Login;
    private CheckBox remPWD,autoLogin;
    private View loginView;
    //register
    private TextView cancel;
    private EditText username_Register;
    private EditText password_Register;
    private EditText password2_Register;
    private Button bRegister;
    private View registerView;

    private LoginPresenter presenter;
    private View container;
    //animation
    private float centerX;
    private float centerY;
    private float Z = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        initLoginView();
        initRegisterView();
        presenter = new LoginPresenter(this);
        presenter.addObserver(this);
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
        registerView = findViewById(R.id.user_register);
        cancel = (TextView)findViewById(R.id.backToLogin);
        cancel.setOnClickListener(this);
        username_Register = (EditText) findViewById(R.id.username_register);
        password_Register = (EditText) findViewById(R.id.password_register);
        password2_Register = (EditText) findViewById(R.id.password_reedit);
        bRegister = (Button) findViewById(R.id.ZC);
        bRegister.setOnClickListener(this);
    }

    private void Login(){
        String un = username_Login.getText().toString().trim();
        String pw = password_Login.getText().toString().trim();
        if(!un.equals("")&&!pw.equals("")){
            if(remPWD.isChecked()){
                User.setUsername(un);
                User.setPassword(EncodeUtil.shaEncode(pw));
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
            final RequestBody requestBody;
            if(remPWD.isChecked()){
                presenter.Login(un,pw);
            }
            else{
               presenter.Login(un,EncodeUtil.shaEncode(pw));
            }
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
            return;
        }
        if(pwd1.equals(pwd2)){
            presenter.Register(name,pwd1);
        } else {
            Toast.makeText(this,"密码不一致",Toast.LENGTH_SHORT).show();
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
}
