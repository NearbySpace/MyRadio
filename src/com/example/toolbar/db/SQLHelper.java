
package com.example.toolbar.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "database.db";// 数据库名称
    public static final int VERSION = 1;

    public static final String TABLE_CHANNEL = "ChannelItem";// 数据表
    public static final String TABLE_DOWNLOADING = "downloading_table";
    public static final String TABLE_DOWNLOADED ="downloaded_table";
    
    //ChannelItem表的内容
    public static final String ID = "id";//
    public static final String NAME = "name";
    public static final String ORDERID = "orderId";
    public static final String SELECTED = "selected";
    
    //downloading_table表的内容
    public static final String DOWNLOAD_ID="program_id";//节目id
    public static final String DOWNLOAD_NAME="name";
    public static final String DOWNLOAD_URL="url";
    public static final String DOWNLOAD_SD_PATH="storage_path";
    public static final String DOWNLOAD_STATE="state";
    public static final String DOWNLOAD_PROGRESS="file_progress";
    public static final String DOWNLOAD_LENGTH="download_length";//已下载长度
    
    //downloaded_table表的内容
    public static final String DOWNLOADED_THUMB="thumb";//节目封面图
    public static final String DOWNLOADED_NAME="name";//下载内容的名称
    public static final String DOWNLOADED_SIZE="size"; //大小
    public static final String DOWNLOADED_AUTHOR="author";//作者
    public static final String DOWNLOADED_SD_PATH="storage_path";//存储路径
    
    
    private final Context context;

    public SQLHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO 创建数据库后，对数据库的操作
        String sql = "create table if not exists " + TABLE_CHANNEL +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ID + " INTEGER , " +
                NAME + " TEXT , " +
                ORDERID + " INTEGER , " +
                SELECTED + " TEXT)";
        db.execSQL(sql);
        
        //保存节目下载列表的数据
        String sql1 = "create table if not exists " + TABLE_DOWNLOADING +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DOWNLOAD_ID + " INTEGER , " +
                DOWNLOAD_NAME + " TEXT , " +
                DOWNLOAD_URL + " TEXT , " +
                DOWNLOAD_SD_PATH + " TEXT,"+
                DOWNLOAD_STATE +" TEXT,"+
                DOWNLOAD_LENGTH+" TEXT, "+
                DOWNLOAD_PROGRESS +" TEXT)";
        db.execSQL(sql1);
        
        //保存已下载的节目数据
        String sql2 = "create table if not exists " + TABLE_DOWNLOADED +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DOWNLOAD_ID + " TEXT , " +
                DOWNLOADED_NAME + " TEXT , " +
                DOWNLOADED_SD_PATH + " TEXT , "+
                DOWNLOADED_THUMB +" TEXT, "+
                DOWNLOADED_AUTHOR +" TEXT)";
        db.execSQL(sql2);
        
        //下载中断时，用于保存下载断点信息
//        db.execSQL("CREATE TABLE IF NOT EXISTS filedownlog ("
//        		+ "id integer primary key autoincrement, "
//        		+"programId varchar(20), "
//        		+ "downurl varchar(100), "
//        		+ "downlength INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO 更改数据库版本的操作
        onCreate(db);
    }

}
