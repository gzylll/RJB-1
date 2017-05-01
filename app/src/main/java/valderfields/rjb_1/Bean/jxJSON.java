package valderfields.rjb_1.Bean;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    /*
    public static Image[] jxImageReturn(String data){
        Image[] images;
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray imageArray = jsonObject.getJSONArray("images");
            images = new Image[imageArray.length()];
            for (int i=0;i<imageArray.length();i++) {
                JSONObject o = (JSONObject) imageArray.get(i);
                Image image = new Image();
                image.Url=o.getString("url");
                image.tags=o.getString("tags").split(",");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return images;
    }
    */
}
