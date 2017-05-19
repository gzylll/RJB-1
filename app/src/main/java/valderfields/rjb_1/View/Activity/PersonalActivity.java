package valderfields.rjb_1.View.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import valderfields.rjb_1.R;

public class PersonalActivity extends AppCompatActivity {

    private Boolean isModify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
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
                return true;
            case R.id.complete:
                submitMessage();
                isModify = false;
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

    public void submitMessage(){

    }
}
