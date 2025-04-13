package com.example.travel_master_yyz.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SearchHistoryHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "search_history.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "search_history";

    public SearchHistoryHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                "uid TEXT," +
                "content TEXT," +
                "timestamp LONG," +
                "PRIMARY KEY(uid, content))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 暂时不处理版本升级
    }

    public void insertSearch(String uid, String content) {
        SQLiteDatabase db = getWritableDatabase();

        // 1. 查询当前 uid 的记录数
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE uid = ?", new String[]{uid});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();

        // 2. 如果记录数 >= 5，删除最旧的那条
        if (count >= 5) {
            db.execSQL("DELETE FROM " + TABLE_NAME +
                    " WHERE rowid IN (SELECT rowid FROM " + TABLE_NAME +
                    " WHERE uid = ? ORDER BY timestamp ASC LIMIT 1)", new String[]{uid});
            Log.d("SearchCache", "达到5条，删除最旧搜索记录");
        }

        // 3. 插入新搜索记录（或更新时间戳）
        ContentValues values = new ContentValues();
        values.put("uid", uid);
        values.put("content", content);
        values.put("timestamp", System.currentTimeMillis());

        long rowId = db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        Log.d("SearchCache", "插入搜索记录 rowId = " + rowId);
    }

    public List<String> getSearchHistory(String uid) {
        List<String> history = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT content FROM " + TABLE_NAME +
                " WHERE uid = ? ORDER BY timestamp DESC", new String[]{uid});

        while (cursor.moveToNext()) {
            history.add(cursor.getString(cursor.getColumnIndexOrThrow("content")));
        }

        cursor.close();
        return history;
    }

    public void clearHistory(String uid) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, "uid = ?", new String[]{uid});
        Log.d("SearchCache", "已清空用户 " + uid + " 的搜索记录");
    }
}
