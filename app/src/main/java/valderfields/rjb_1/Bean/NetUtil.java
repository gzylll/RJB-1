package valderfields.rjb_1.Bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 网络访问工具类
 * Created by 11650 on 2017/4/13.
 */

public class NetUtil {

    private static OkHttpClient PersonalOkHttpCilent = new OkHttpClient.Builder().build();
    /**
     * localHost
     */
    final private static String localHost = "http://123.206.214.198:8080/ImageSortServer";
    /**
     * 登录的url
     */
    final private static String loginUrl = localHost+"/LoginServlet";

    /**
     * 注册的url
     */
    final private static String registerUrl = localHost+"/RegisterServlet";
    /**
     * 请求图片的url
     */
    final private static String requestImageUrl = localHost+"/ImageDistributeServlet";
    /**
     * 获得带有cookie管理的对象
     * @return 对象
     */
    public static OkHttpClient getOkHttpClient() {
        return PersonalOkHttpCilent;
    }

    public static String getLoginUrl(){
        return loginUrl;
    }

    public static String getRegisterUrl() {
        return registerUrl;
    }

    public static String getRequestImageUrl() {
        return requestImageUrl;
    }

    /**
     * 获得请求函数
     * @param url  请求url
     * @param requestBody  请求的requestBody
     * @return 指定url的POST请求
     */
    public static Request getRequest(String url, RequestBody requestBody)
    {
        return new Request.Builder().url(url).post(requestBody).build();
    }

    /**
     * 从URL获取图片
     * @param path URL
     * @return 图片
     * @throws IOException IO异常
     */
    public static Bitmap getBitmap(String path) throws IOException {
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == 200){
            InputStream inputStream = conn.getInputStream();
            return BitmapFactory.decodeStream(inputStream);
        }
        return null;
    }
}