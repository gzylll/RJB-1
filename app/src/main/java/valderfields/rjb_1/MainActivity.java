package valderfields.rjb_1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button login;
    private Button register;
    private EditText username;
    private EditText password;
    private CheckBox remPWD,autoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    }

    private void Login(){
        String un = username.getText().toString();
        String pw = password.getText().toString();
        if(!un.equals("")&&!pw.equals("")){
            //登录
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
                break;
        }
    }
}
