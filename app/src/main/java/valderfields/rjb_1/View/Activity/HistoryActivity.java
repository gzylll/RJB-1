package valderfields.rjb_1.View.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import valderfields.rjb_1.R;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setTitle("我的历史");
    }
}
