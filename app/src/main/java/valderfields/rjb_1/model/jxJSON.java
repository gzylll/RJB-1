package valderfields.rjb_1.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                Log.e("Login",data);
                JSONObject jsonObject = new JSONObject(data);
                JSONObject jsonObject1 = jsonObject.getJSONObject("user");
                User.setUID(jsonObject1.getString("uid"));
                User.setEmail(jsonObject1.getString("email"));
                User.setPhone(jsonObject1.getString("phone"));
                User.setUsername(jsonObject1.getString("username"));
                User.setScore(Integer.parseInt(jsonObject1.getString("score")));
                User.setHobbies(jsonObject1.getString("hobbies"));
                Log.e("Login",String.valueOf(jsonObject1.getString("score")));
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
                images.add(image);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return images;
    }

    public static List<Map<String,String>> jxHistory(String data){
        List<Map<String,String>> histories = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(data);
            for(int i=0;i<array.length();i++){
                JSONObject object = array.getJSONObject(i);
                Map<String,String> map = new HashMap<>();
                map.put("id",object.getString("id"));
                map.put("tags",object.getString("tags"));
                map.put("name",object.getString("name"));
                histories.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return histories;
    }

}
