package valderfields.rjb_1.model;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * 加密类
 * Created by 11650 on 2017/4/18.
 */

public class EncodeUtil {

    public static String shaEncode(String inStr){
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA");
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        byte[] byteArray = new byte[0];
        try {
            byteArray = inStr.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] shaBytes = sha.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();
        for (byte shaByte : shaBytes) {
            int val = ((int) shaByte) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }

}
