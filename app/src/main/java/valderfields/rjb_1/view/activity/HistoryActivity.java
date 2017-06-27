package valderfields.rjb_1.view.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;
import valderfields.rjb_1.model.NetUtil;
import valderfields.rjb_1.model.User;
import valderfields.rjb_1.model.dbHelper;
import valderfields.rjb_1.model.jxJSON;
import valderfields.rjb_1.R;

public class HistoryActivity extends AppCompatActivity {

    private dbHelper db = new dbHelper(this,"history");
    private List<Map<String,String>> histories = new ArrayList<>();
    private ProgressDialog progressDialog;
    private ListView historyList;
    private SimpleAdapter adapter;
    private Bitmap chooseBitmap;
    private Map<String,String> chooseHistory;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1){
                case 0x11:
                    if(msg.obj.equals("wrong")){
                        //获取不到记录
                        progressDialog.dismiss();
                        Toast.makeText(HistoryActivity.this,"没有记录",Toast.LENGTH_SHORT).show();
                    }else {
                        //获取到了记录
                        histories.clear();
                        histories.addAll(jxJSON.jxHistory(msg.obj.toString()));
                        //获取接收信息
                        getIsReceived();
                    }
                    break;
                case 0x12:
                    //获取到了图片之后
                    progressDialog.dismiss();
                    showDialog();
                    break;
                case 0x13:
                    //获取到了接收信息的情况
                    progressDialog.dismiss();
                    addReceiveTag(msg.obj.toString());
                    showHistory();
                    break;
                case 0x14:
                    //获取不到接受信息的情况
                    progressDialog.dismiss();
                    showHistory();
                    break;
                case 0x10:
                    //一般的传信息情况
                    progressDialog.dismiss();
                    Toast.makeText(HistoryActivity.this,msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            progressDialog.setMessage("获取信息中");
            progressDialog.setCancelable(false);
            progressDialog.show();
            try {
                chooseHistory = histories.get(position);
                getBitmap(histories.get(position).get("name"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setTitle("我的历史");
        initView();
        QueryData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_history,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.Refresh){
            QueryData();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView(){
        progressDialog = new ProgressDialog(this);
        historyList = (ListView)findViewById(R.id.historyList);
        adapter = new SimpleAdapter(this,
                                    histories,
                                    R.layout.item_listview,
                                    new String[]{"id","name","tags","isrecieve","time"},
                                    new int[]{R.id.pictureUID,R.id.pictName,R.id.label,R.id.isAccept,R.id.time});
        historyList.setAdapter(adapter);
        historyList.setOnItemClickListener(listener);
    }

    /**
     * 查询历史信息
     */
    private void QueryData(){
        progressDialog.setMessage("查询数据中,请不要退出");
        progressDialog.setCancelable(false);
        progressDialog.show();
        histories.clear();
        histories.addAll(db.query());
//        本地查不到
        if(histories.size()==0){
            NetUtil.getOkHttpClient().newCall(
                    NetUtil.getRequest(NetUtil.getGetTagHistoryUrl(),
                    new FormBody.Builder()
                            .add("uid",User.getUID())
                            .build())
            ).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Message message = Message.obtain();
                    message.arg1=0x10;
                    message.obj="获取信息失败";
                    handler.sendMessage(message);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.code()==200){
                        String s = response.body().string();
                        Log.e("history",s);
                        Message message = Message.obtain();
                        message.arg1=0x11;
                        message.obj = s;
                        handler.sendMessage(message);
                    }
                }
            });
        } else {
            getIsReceived();
        }
    }

    /**
     * 刷新界面
     */
    private void showHistory(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 获取标签接收情况
     */
    private void getIsReceived(){
        Log.e("receive","getIsReceived");
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        NetUtil.getOkHttpClient().newCall(
                                NetUtil.getRequestWithSession(NetUtil.getGetReceivedUrl(),
                                        new FormBody.Builder().build())
                        ).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Message message = Message.obtain();
                                message.arg1=0x14;
                                message.obj="获取接收信息失败";
                                handler.sendMessage(message);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String s = response.body().string();
                                if(response.code()==200){
                                    Log.e("receive",s);
                                    Message message = Message.obtain();
                                    message.arg1=0x13;
                                    message.obj=s;
                                    handler.sendMessage(message);
                                }else{
                                    Log.e("receive",s);
                                    Message message = Message.obtain();
                                    message.arg1=0x14;
                                    message.obj="获取接收信息失败";
                                    handler.sendMessage(message);
                                }
                            }
                        });
                    }
                }
        ).start();

    }

    /**
     * 根据名字获取图片
     * @param name 图片名
     * @throws IOException IO
     */
    private void getBitmap(String name) throws IOException {
        final String s = NetUtil.getLocalHost()+"/images/"+name;
        final URL url = new URL(s);
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection conn = null;
                        try {
                            conn = (HttpURLConnection)url.openConnection();
                            conn.setConnectTimeout(500);
                            try {
                                conn.setRequestMethod("GET");
                            } catch (ProtocolException e) {
                                e.printStackTrace();
                            }
                            if(conn.getResponseCode() == 200){
                                InputStream inputStream = conn.getInputStream();
                                chooseBitmap = BitmapFactory.decodeStream(inputStream);
                                if(chooseBitmap==null){
                                    Log.e("getData",s+"图片获取为空");
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Message message = Message.obtain();
                        message.arg1=0x12;
                        handler.sendMessage(message);
                    }
                }
        ).start();
    }

    /**
     * 修改标签弹窗
     */
    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_record,null);
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        imageView.setImageBitmap(chooseBitmap);
        builder.setView(view);
        final Dialog dialog = builder.create();
        dialog.show();
        final EditText editText = (EditText)view.findViewById(R.id.label);
        editText.setText(chooseHistory.get("tags"));
        TextView yes = (TextView)view.findViewById(R.id.edit_yes);
        TextView no = (TextView)view.findViewById(R.id.edit_no);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText.getText().toString().equals(chooseHistory.get("tags"))){
                    dialog.dismiss();
                }else{
                    dialog.dismiss();
                    UpdateTags(editText.getText().toString());
                }
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 将接受信息整合到历史记录中
     * @param s 接受信息
     */
    private void addReceiveTag(String s){
        Log.e("receive-s",s);
        String s1 = s.substring(1,s.length()-1);
        Log.e("receive-s1",s1);
        if(s1.length()!=0){
            String[] cut = s1.split(",");
            for(int i=0;i<histories.size();i++){
                boolean isfind = false;
                Map<String,String> map = histories.get(i);
                for(int j=0;j<cut.length;j++){
                    if(map.get("id").equals(cut[j])){
                        isfind = true;
                        break;
                    }
                }
                if(isfind)
                    map.put("isrecieve","是");
                else
                    map.put("isrecieve","否");
            }
        }
    }

    private void UpdateTags(final String input){
        NetUtil.getOkHttpClient().newCall(
                NetUtil.getRequestWithSession(
                        NetUtil.getGetUpdateUrl(),
                        new FormBody.Builder()
                            .add("id",chooseHistory.get("id"))
                            .add("name",chooseHistory.get("name"))
                            .add("tags",input)
                            .build()
                )
        ).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = Message.obtain();
                message.arg1=0x10;
                message.obj="更新失败";
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code()==200){
                    String s = response.body().string();
                    if(s.equals("success")){
                        Message message = Message.obtain();
                        message.arg1=0x10;
                        message.obj="更新成功";
                        handler.sendMessage(message);
                        db.update(chooseHistory.get("id"),input);
                    }
                }else{
                    Message message = Message.obtain();
                    message.arg1=0x10;
                    message.obj="更新失败";
                    handler.sendMessage(message);
                }
            }
        });
    }
}
