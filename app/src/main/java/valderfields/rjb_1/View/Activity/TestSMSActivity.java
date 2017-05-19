package valderfields.rjb_1.View.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import valderfields.rjb_1.R;

public class TestSMSActivity extends AppCompatActivity {

    String TAG = "TestSMSActivity";

    EditText mEditTextPhoneNumber;
    EditText mEditTextCode;
    Button mButtonGetCode;
    Button mButtonTiJiao;

    String strPhoneNumber;

    EventHandler eh=new EventHandler(){
        @Override
        public void afterEvent(int event, int result, Object data) {
            Message message = myHandler.obtainMessage(0x00);
            message.arg1 = event;
            message.arg2 = result;
            message.obj = data;
            myHandler.sendMessage(message);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_sms);
        initSDK();
        mButtonGetCode = (Button)findViewById(R.id.zhuce);
        mButtonTiJiao = (Button)findViewById(R.id.tijiao);
        mEditTextPhoneNumber = (EditText)findViewById(R.id.number);
        mEditTextCode = (EditText)findViewById(R.id.input);
        mButtonGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strPhoneNumber = mEditTextPhoneNumber.getText().toString();
                if ("".equals(strPhoneNumber) || strPhoneNumber.length() != 11) {
                    Toast.makeText(TestSMSActivity.this, "电话号码输入有误", Toast.LENGTH_SHORT).show();
                    return;
                }
                SMSSDK.getVerificationCode("86", strPhoneNumber);
                mButtonGetCode.setClickable(false);
                //开启线程去更新button的text
                new Thread() {
                    @Override
                    public void run() {
                        int totalTime = 60;
                        for (int i = 0; i < totalTime; i++) {

                            Message message = myHandler.obtainMessage(0x01);
                            message.arg1 = totalTime - i;
                            myHandler.sendMessage(message);
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        myHandler.sendEmptyMessage(0x02);
                    }
                }.start();
            }
        });

        mButtonTiJiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strCode = mEditTextCode.getText().toString();
                if (null != strCode && strCode.length() == 4) {
                    Log.d(TAG, mEditTextCode.getText().toString());
                    SMSSDK.submitVerificationCode("86", strPhoneNumber, mEditTextCode.getText().toString());
                } else {
                    Toast.makeText(TestSMSActivity.this, "密码长度不正确", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }

    private void initSDK(){
        SMSSDK.initSDK(this, "1df79ee15fafb", "e18c7e5956e6dea1afa0ed8045e969ae");
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x00:
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    if (result == SMSSDK.RESULT_COMPLETE) { //回调  当返回的结果是complete
                        if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) { //获取验证码
                            Toast.makeText(TestSMSActivity.this, "发送验证码成功", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "get verification code successful.");
                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) { //提交验证码
                            Log.d(TAG, "submit code successful");
                            Toast.makeText(TestSMSActivity.this, "提交验证码成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, data.toString());
                        }
                    } else { //进行操作出错，通过下面的信息区分析错误原因
                        try {
                            Throwable throwable = (Throwable) data;
                            throwable.printStackTrace();
                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");//错误描述
                            int status = object.optInt("status");//错误代码
                            //错误代码：  http://wiki.mob.com/android-api-%E9%94%99%E8%AF%AF%E7%A0%81%E5%8F%82%E8%80%83/
                            Log.e(TAG, "status: " + status + ", detail: " + des);
                            if (status > 0 && !TextUtils.isEmpty(des)) {
                                Toast.makeText(TestSMSActivity.this, des, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 0x01:
                    mButtonGetCode.setText("重新发送(" + msg.arg1 + ")");
                    break;
                case 0x02:
                    mButtonGetCode.setText("获取验证码");
                    mButtonGetCode.setClickable(true);
                    break;
            }
        }
    };
}
