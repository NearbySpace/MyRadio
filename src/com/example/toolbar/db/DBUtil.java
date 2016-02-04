package com.example.toolbar.db;

import com.example.toolbar.bean.DownloadEntry;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class DBUtil {
    private static DBUtil mInstance;
    private SQLHelper mSQLHelp;
    private SQLiteDatabase mSQLiteDatabase;
    private final String TAG="DBUtil";
    private DBUtil(Context context) {
        mSQLHelp = new SQLHelper(context);
        mSQLiteDatabase = mSQLHelp.getWritableDatabase();
    }

    public static DBUtil getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DBUtil(context);
        }
        return mInstance;
    }

    public void close() {
        mSQLHelp.close();
        mSQLHelp = null;
        mSQLiteDatabase.close();
        mSQLiteDatabase = null;
        mInstance = null;
    }

    public void insertData(String tableName,ContentValues values) {
        long i=mSQLiteDatabase.insert(tableName, null, values);
        Log.i(TAG, "i值------->"+i);
    }

    public void updateData(String tableName,ContentValues values, String whereClause,
            String[] whereArgs) {
        mSQLiteDatabase.update(tableName, values, whereClause,
                whereArgs);
    }

    public void deleteData(String tableName,String whereClause, String[] whereArgs) {
        mSQLiteDatabase
                .delete(tableName, whereClause, whereArgs);
    }

    public Cursor selectData(String tableName,String[] columns, String selection,
            String[] selectionArgs, String groupBy, String having,
            String orderBy) {
        Cursor cursor = mSQLiteDatabase.query(tableName, columns, selection,
                selectionArgs, groupBy, having, orderBy);
        return cursor;
    }
    
    public boolean updateDownloadEntry(DownloadEntry downloadEntry) {
		String whereClause = SQLHelper.DOWNLOAD_URL + " = ? ";
		String[] whereArgs = { downloadEntry.getUrl()};
		ContentValues c = new ContentValues();
		c.put(SQLHelper.DOWNLOAD_STATE, downloadEntry.getState());
		c.put(SQLHelper.DOWNLOAD_NAME, downloadEntry.getTitle());
		c.put(SQLHelper.DOWNLOAD_PROGRESS, downloadEntry.getFileProgress());
		// c.put(DOWNLOAD_TASK_LOCAL_PATH, downloadEntry.localPath);
		return mSQLiteDatabase.update(SQLHelper.TABLE_DOWNLOADING, c, whereClause, whereArgs) > 0;
	}

    public boolean deleteDownloadEntry(String url) {
		int rowCount = mSQLiteDatabase.delete(SQLHelper.TABLE_DOWNLOADING, SQLHelper.DOWNLOAD_URL
				+ " = ? " ,
				new String[] { url });
		Log.i(TAG, "deleteDownloadEntry rowCount: " + rowCount);
		return rowCount > 0;
	}
}
