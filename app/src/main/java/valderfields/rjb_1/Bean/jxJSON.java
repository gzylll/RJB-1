package valderfields.rjb_1.Bean;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析JSON数据
 * Created by 11650 on 2017/4/15.
 */

public class jxJSON {

    /*
    public static User jxLoginData(String data){
        User user;
        return user;
    }
    */
    public static void Ceshi(String data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject jsonObject1 = jsonObject.getJSONObject("user");
            Log.i("password",jsonObject1.getString("password"));
            Log.i("phone",jsonObject1.getString("phone"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static List<Image> jxImageReturn(String data){
        List<Image> images = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray imageArray = jsonObject.getJSONArray("images");
            for (int i=0;i<imageArray.length();i++) {
                JSONObject o = (JSONObject) imageArray.get(i);
                Image image = new Image();
                image.Url=o.getString("url");
                if(o.has("tags")){
                    image.Tags=o.getString("tags").split(",");
                }
                image.Name = o.getString("name");
                image.Id = o.getString("id");
                images.add(image);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return images;
    }
}
