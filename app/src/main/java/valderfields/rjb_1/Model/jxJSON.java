package valderfields.rjb_1.Model;

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

    public static boolean jxLoginData(String data){
        if(data.equals("wrong"))
            return false;
        else{
            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONObject jsonObject1 = jsonObject.getJSONObject("user");
                User.setUID(jsonObject1.getString("uid"));
                User.setEmail(jsonObject1.getString("email"));
                User.setPhone(jsonObject1.getString("phone"));
                User.setUsername(jsonObject1.getString("username"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return true;
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
                image.Name = o.getString("name");
                image.Id = o.getString("id");
                if(o.has("tags")){
                    image.Tags=o.getString("tags").split(",");
                }
                else{
                    image.Tags=new String[]{"yi","啦啦","测试","test","lalalalalalalala","lalalalalalalala"
                    ,"lalalalalalalala"};
                }
                images.add(image);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return images;
    }
}
