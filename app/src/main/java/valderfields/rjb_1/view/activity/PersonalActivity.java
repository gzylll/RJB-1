package valderfields.rjb_1.view.activity;

import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;
import valderfields.rjb_1.model.EncodeUtil;
import valderfields.rjb_1.model.NetUtil;
import valderfields.rjb_1.model.User;
import valderfields.rjb_1.R;

public class PersonalActivity extends AppCompatActivity{

    private Boolean isModify = false;
    private String Email="",Password="",Hobby="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        getSupportActionBar().setTitle("个人信息");
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.image_action_button, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.modify:
                isModify = true;
                showdialog();
                invalidateOptionsMenu();
                return true;
            case R.id.complete:
                submitMessage();
                isModify = false;
                invalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(isModify)
        {
            menu.findItem(R.id.modify).setVisible(false);
            menu.findItem(R.id.complete).setVisible(true);
        }
        else
        {
            menu.findItem(R.id.modify).setVisible(true);
            menu.findItem(R.id.complete).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void initView(){
        TextView UID = (TextView)findViewById(R.id.uid);
        UID.setText(User.getUID());
        TextView tvName = (TextView)findViewById(R.id.i_name);
        tvName.setText(User.getUsername());
        TextView tvEmail = (TextView)findViewById(R.id.i_email);
        tvEmail.setText(User.getEmail());
        TextView tvPhone = (TextView)findViewById(R.id.i_phone);
        tvPhone.setText(User.getPhone());
        TextView tvHobby = (TextView)findViewById(R.id.i_hobby);
        tvHobby.setText(User.getHobbies());
    }

    public void submitMessage(){
        Toast.makeText(this,"Email:"+Email+",Password:"+Password+",Hobby"+Hobby+".",Toast.LENGTH_SHORT).show();
        if(Email.equals("")&&Password.equals("")&&Hobby.equals(""))
            return;
        else{
            if(Email.equals(""))
                Email=User.getEmail();
            if(Hobby.equals(""))
                Hobby=User.getHobbies();
            if(!Password.equals(""))
                Password=EncodeUtil.shaEncode(Password);
            NetUtil.getOkHttpClient().newCall(
                    NetUtil.getRequestWithSession(
                            NetUtil.getGetUpdateUserInfoUrl(),
                            new FormBody.Builder()
                                    .add("email",Email)
                                    .add("password", Password)
                                    .add("hobbies",Hobby)
                                    .build()
                    )
            ).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    showMessage("修改失败");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.code()==200){
                        String s = response.body().string();
                        if(s.equals("success")){
                            User.setEmail(Email);
                            User.setHobbies(Hobby);
                            showMessage("修改成功");
                        }else{
                            showMessage("修改失败");
                        }
                    }else{
                        showMessage("修改失败");
                    }
                }
            });
        }
    }

    public void showMessage(String s){
        Looper.prepare();
        Toast.makeText(PersonalActivity.this,s,Toast.LENGTH_SHORT).show();
        Looper.loop();
    }
    public void showdialog(){
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_editpw,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final EditText hb = (EditText)view.findViewById(R.id.chb);
        final EditText mm = (EditText)view.findViewById(R.id.cmm);
        final EditText em = (EditText)view.findViewById(R.id.cem);
        TextView confirm = (TextView)view.findViewById(R.id.editPassword_yes);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email = em.getText().toString();
                Password = mm.getText().toString();
                Hobby = hb.getText().toString();
                dialog.dismiss();

            }
        });
        TextView cancel = (TextView)view.findViewById(R.id.editPassword_no);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}
