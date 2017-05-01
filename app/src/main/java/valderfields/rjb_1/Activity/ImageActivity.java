package valderfields.rjb_1.Activity;

import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import valderfields.rjb_1.Bean.Image;
import valderfields.rjb_1.Adapter.PageTransformer;
import valderfields.rjb_1.R;
import valderfields.rjb_1.Adapter.ViewPagerAdapter;

public class ImageActivity extends AppCompatActivity {

    private List<Image> images = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        getSupportActionBar().hide();
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        initData();
        ViewPagerAdapter adapter = new ViewPagerAdapter(images,this);
        viewPager.setPageTransformer(true,new PageTransformer());
        viewPager.setAdapter(adapter);
    }

    private void initData(){
        for(int i=0;i<4;i++){
            Image image = new Image();
            image.bitmap = getRes("p"+String.valueOf(i+1));
            images.add(image);
        }
    }

    public Bitmap getRes(String name) {
        ApplicationInfo appInfo = getApplicationInfo();
        int resID = getResources().getIdentifier(name, "mipmap", appInfo.packageName);
        return BitmapFactory.decodeResource(getResources(), resID);
    }
}