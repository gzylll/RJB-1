package valderfields.rjb_1.Model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 *
 * Created by 11650 on 2017/4/15.
 */

public class User{

    private static String _UID;
    private static String _username;
    private static String _phone;
    private static String _email;
    private static String _password;
    private static Boolean _isRemember;
    private static Boolean _isAuto;

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    public static void init(Context context){
        preferences = context.getSharedPreferences("userData",Context.MODE_PRIVATE);
        editor = preferences.edit();
        _UID = preferences.getString("uid","");
        _username = preferences.getString("username","");
        _password = preferences.getString("password","");
        _isRemember = preferences.getBoolean("isR",false);
        _isAuto = preferences.getBoolean("isA",false);
    }


    public static String getUID() {
        return _UID;
    }

    public static void setUID(String UID) {
        _UID = UID;
        editor.putString("uid",UID);
        editor.commit();
    }

    public static String getUsername() {
        return _username;
    }

    public static void setUsername(String username) {
        _username = username;
        editor.putString("username",username);
        editor.commit();
    }

    public static String getPhone() {
        return _phone;
    }

    public static void setPhone(String phone) {
        _phone = phone;
    }

    public static String getEmail() {
        return _email;
    }

    public static void setEmail(String email) {
        _email = email;
    }

    public static String getPassword() {
        return _password;
    }

    public static void setPassword(String password) {
        _password = password;
        editor.putString("password",password);
        editor.commit();
    }

    public static Boolean getRemember() {
        return _isRemember;
    }

    public static void setRemember(Boolean remember) {
        _isRemember = remember;
        editor.putBoolean("isR",remember);
        editor.commit();
    }

    public static Boolean getAuto() {
        return _isAuto;
    }

    public static void setAuto(Boolean auto) {
        _isAuto = auto;
        editor.putBoolean("isA",auto);
        editor.commit();
    }

    public static void clear(){
        editor.clear();
        editor.commit();
    }
}
