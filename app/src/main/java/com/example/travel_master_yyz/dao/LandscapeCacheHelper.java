package com.example.travel_master_yyz.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class LandscapeCacheHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "landscape.db";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "landscape";

    public LandscapeCacheHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                "id TEXT," +
                "uid TEXT," +
                "image TEXT," +
                "description TEXT," +
                "praise INTEGER," +
                "name TEXT," +
                "timestamp LONG," +
                "PRIMARY KEY (id, uid))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 你可以处理升级逻辑
    }

    public void insertLandscape(LandscapeCacheItem item) {
        SQLiteDatabase db = this.getWritableDatabase();


        // 1. 查询该用户的缓存记录数
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE uid = ?", new String[]{item.uid});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();

        // 2. 如果达到8条或更多，删除最旧的一条
        if (count >= 8) {
            db.execSQL("DELETE FROM " + TABLE_NAME +
                    " WHERE rowid IN (SELECT rowid FROM " + TABLE_NAME +
                    " WHERE uid = ? ORDER BY timestamp ASC LIMIT 1)", new String[]{item.uid});
            Log.d("CacheDebug", "达到8条，删除最旧记录");
        }

        ContentValues values = new ContentValues();
        values.put("id", item.id);
        values.put("uid", item.uid);
        values.put("image", item.image);
        values.put("description", item.description);
        values.put("praise", item.praise);
        values.put("name", item.name);
        values.put("timestamp", item.timestamp);

        long rowId = db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        Log.d("CacheDebug", "插入缓存 rowId = " + rowId);
    }

    public List<LandscapeCacheItem> getAllLandscapeItems(String uid) {
        List<LandscapeCacheItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE uid = ? ORDER BY timestamp DESC", new String[]{uid});

        while (cursor.moveToNext()) {
            LandscapeCacheItem item = new LandscapeCacheItem(
                    cursor.getString(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("image")),
                    cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("praise")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getLong(cursor.getColumnIndexOrThrow("timestamp"))
            );
            item.uid = cursor.getString(cursor.getColumnIndexOrThrow("uid")); // 可选，补 uid
            list.add(item);
        }

        cursor.close();
        Log.d("CacheDebug", "获取记录数 = " + list.size());
        return list;
    }
}
