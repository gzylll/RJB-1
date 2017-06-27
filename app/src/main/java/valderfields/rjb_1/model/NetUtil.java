package valderfields.rjb_1.model;

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
    final private static String localHost = "http://114.115.142.214:8080/ImageSortServer";
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
     * 提交标签的Url
     */
    final private static String submitTagUrl = localHost+"/ImageReceiveServlet";
    /**
     * 获得标签历史的Url
     */
    final private static String getTagHistoryUrl = localHost+"/QueryRecords";
    /**
     * 获得接收的数组的Url
     */
    final private static String getReceivedUrl = localHost+"/QueryReceiveTags";
    /**
     * 获得接收的数组的Url
     */
    final private static String getUpdateUrl = localHost+"/UpdateTag";
    /**
     * 获得积分的Url
     */
    final private static String getScoreUrl = localHost+"/UpdateScore";

    final private static String getUpdateUserInfoUrl = localHost+"/UpdateUser";
    /**
     * 获得带有cookie管理的对象
     * @return 对象
     */
    public static OkHttpClient getOkHttpClient() {
        return PersonalOkHttpCilent;
    }

    public static String getLocalHost() {
        return localHost;
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

    public static String getSubmitTagUrl() {
        return submitTagUrl;
    }

    public static String getGetTagHistoryUrl(){
        return getTagHistoryUrl;
    }

    public static String getGetReceivedUrl() {
        return getReceivedUrl;
    }

    public static String getGetUpdateUrl() {
        return getUpdateUrl;
    }

    public static String getGetScoreUrl() {
        return getScoreUrl;
    }

    public static String getGetUpdateUserInfoUrl() {
        return getUpdateUserInfoUrl;
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

    public static Request getRequestWithSession(String url, RequestBody requestBody)
    {
        return new Request.Builder()
                          .addHeader("Cookie",User.getSession().split(";")[0])
                          .url(url).post(requestBody)
                          .build();
    }

}
