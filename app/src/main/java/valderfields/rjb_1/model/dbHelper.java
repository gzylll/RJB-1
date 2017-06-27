package valderfields.rjb_1.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 本地历史数据库
 */

public class dbHelper {

    private String dbName;
    private String tableName = "History";
    private int dbVersion = 1;
    private SQLiteDatabase db;
    private dbOpenHelper helper;

    /**
     * 内部类实现打开数据库
     */
    private class dbOpenHelper extends SQLiteOpenHelper {

        //建表语句
        private String createTable = "create table "+ tableName +
                "( id text primary key ," +             //ID
                "imageName text not null,"                        //图片名
                +"tags text not null,"                            //标签
                +"time timestamp not null)";

        public dbOpenHelper(Context context){
            super(context,dbName,null,dbVersion);
        }

        /**
         * 程序初次使用数据库的时候调用
         * @param db 数据库对象
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(createTable);
        }

        /**
         * 程序升级数据库时调用，这里只是简单的删除旧版数据库重新建立新版数据库
         * @param db 数据库对象
         * @param oldVersion 旧的版本
         * @param newVersion 新的版本
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS"+tableName);
            onCreate(db);
        }
    }

    /**
     * 构造函数，私有成员赋值，获得可写数据库
     * @param context Context对象
     * @param dbName 传入数据库名称
     */
    public dbHelper(Context context, String dbName){
        this.dbName = dbName;
        helper = new dbOpenHelper(context);
    }


    /**
     * 向数据库插入数据
     */
    public void instert(History h) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id",h.getId());
        values.put("imageName",h.getName());
        values.put("tags",h.getTags());
        values.put("time",h.getTime());
        db.insert(tableName,null,values);
        db.close();
    }

    /**
     * 查询数据库
     */
    public List<Map<String,String>> query(){
        db = helper.getReadableDatabase();
        List<Map<String,String>> histories = new ArrayList<>();
        String sql = "select * from "+tableName;
        Cursor cursor = db.rawQuery(sql,null);
        while(cursor.moveToNext()){
            History h = new History();
            h.setId(cursor.getString(0));
            h.setName(cursor.getString(1));
            h.setTags(cursor.getString(2));
            h.setTime(cursor.getString(3));
            histories.add(h.toMap());
        }
        cursor.close();
        db.close();
        return histories;
    }

    public void update(String id,String tag){
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tags",tag);
        String[] args = {id};
        db.update(dbName,contentValues,"id=?",args);
        db.close();
    }
}
