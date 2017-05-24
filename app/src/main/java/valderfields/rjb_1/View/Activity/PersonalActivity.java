package valderfields.rjb_1.View.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import valderfields.rjb_1.Model.User;
import valderfields.rjb_1.R;

public class PersonalActivity extends AppCompatActivity {

    private Boolean isModify = false;

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
                Log.d("onPrepareOptionsMenu","点击修改");
                isModify = true;
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
        Log.d("onPrepareOptionsMenu","调用");
        if(isModify)
        {
            Log.d("onPrepareOptionsMenu","true");
            menu.findItem(R.id.modify).setVisible(false);
            menu.findItem(R.id.complete).setVisible(true);
        }
        else
        {
            Log.d("onPrepareOptionsMenu","false");
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
        TextView tvPhone = (TextView)findViewById(R.id.i_phone);
        tvPhone.setText(User.getPhone());
    }

    public void OnClickPersonal(View view){

    }

    public void submitMessage(){

    }
}
