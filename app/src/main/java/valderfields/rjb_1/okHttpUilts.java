package valderfields.rjb_1;

import java.net.CookieManager;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by 11650 on 2017/4/13.
 */

public class okHttpUilts {

    private static OkHttpClient PersonalOkHttpCilent = new OkHttpClient.Builder().build();

    /**
     * 登录的url
     */
    final private static String loginUrl = "http://123.206.214.198:8080/ImageSortServer/LoginServlet";

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
}
