package valderfields.rjb_1;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 11650 on 2017/4/15.
 */

public class jxJSON {

    /*
    public static User jxLoginData(String data){
        User user;
        return user;
    }
    */
    static void Ceshi(String data){
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject jsonObject1 = jsonObject.getJSONObject("user");
            Log.i("password",jsonObject1.getString("password"));
            Log.i("phone",jsonObject1.getString("phone"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
