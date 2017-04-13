package valderfields.rjb_1;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText username;
    private EditText password;
    private EditText password2;
    private TextView confirmMessage1,confirmMessage2,confirmMessage3;
    private Button register;
    private Boolean canClick1=false,canClick2=false,canClick3=false;
    private TextWatcher nameWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length()>20){
                confirmMessage1.setText("用户名长度不超过20");
                confirmMessage1.setTextColor(Color.RED);
                confirmMessage1.setVisibility(View.VISIBLE);
                canClick1 = false;
            }else{
                confirmMessage1.setVisibility(View.GONE);
                canClick1 = true;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(canClick1&&canClick3&&canClick2)
                register.setVisibility(View.VISIBLE);
            else{
                register.setVisibility(View.GONE);
            }
        }
    };
    private TextWatcher pwdWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length()>20){
                confirmMessage2.setText("密码长度不超过20");
                confirmMessage2.setTextColor(Color.RED);
                confirmMessage2.setVisibility(View.VISIBLE);
                canClick2 = false;
            }else{
                confirmMessage2.setVisibility(View.GONE);
                canClick2 = true;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(canClick1&&canClick3&&canClick2)
                register.setVisibility(View.VISIBLE);
            else{
                register.setVisibility(View.GONE);
            }
        }
    };
    private TextWatcher pwd2Watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(password.getText().toString().equals(s.toString())) {
                confirmMessage3.setVisibility(View.GONE);
                canClick3=true;
            }else{
                confirmMessage3.setText("密码不一致");
                confirmMessage3.setTextColor(Color.RED);
                confirmMessage3.setVisibility(View.VISIBLE);
                canClick3 = false;
            }

            if(canClick1&&canClick3&&canClick2)
                register.setVisibility(View.VISIBLE);
            else{
                register.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
}

    private void initView(){
        username = (EditText)findViewById(R.id.username_register);
        username.addTextChangedListener(nameWatcher);
        password = (EditText)findViewById(R.id.password_register);
        password.addTextChangedListener(pwdWatcher);
        password2 = (EditText)findViewById(R.id.password_reedit);
        password2.addTextChangedListener(pwd2Watcher);
        confirmMessage1 = (TextView)findViewById(R.id.confirmMessage1);
        confirmMessage2 = (TextView)findViewById(R.id.confirmMessage2);
        confirmMessage3 = (TextView)findViewById(R.id.confirmMessage3);
        register = (Button)findViewById(R.id.ZC);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String name = username.getText().toString();
        String pwd = password2.getText().toString();
        Toast.makeText(this,"name:"+name+"pwd:"+pwd,Toast.LENGTH_SHORT).show();
    }
}
