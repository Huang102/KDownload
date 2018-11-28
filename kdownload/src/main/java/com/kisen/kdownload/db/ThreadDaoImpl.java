package com.kisen.kdownload.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kisen.kdownload.entity.ThreadInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangwy on 2017/12/26.
 * email: kisenhuang@163.com.
 */

public class ThreadDaoImpl implements ThreadDao {

    private DBHelper mDBHelper;

    public ThreadDaoImpl(Context context) {
        mDBHelper = new DBHelper(context);
    }

    @Override
    public void insertThread(ThreadInfo info) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("thread_id", info.getId());
            values.put("url", info.getUrl());
            values.put("file_name", info.getFileName());
            values.put("totalSize", info.getTotalSize());
            values.put("downloadTime", info.getDownloadTime());
            values.put("finished", info.getFinished());
            db.insert(DBHelper.THREAD_TABLE, null, values);
            db.close();
        }
    }

    @Override
    public void deleteThread(String url, int threadId) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        if (db.isOpen()) {
            db.delete(DBHelper.THREAD_TABLE, "url = ? and thread_id = ?", new String[]{url, threadId + ""});
            db.close();
        }
    }

    @Override
    public void updateThread(String url, int threadId, int finished, long downloadTime) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        String sql = "update " + DBHelper.THREAD_TABLE + " set finished = ?, downloadTime = ? where url = ? and thread_id = ?";
        db.execSQL(sql, new Object[]{finished, downloadTime, url, threadId});
        db.close();
    }

    @Override
    public List<ThreadInfo> queryThread(String url) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        List<ThreadInfo> list = new ArrayList<>();
        Cursor cursor = db.query(DBHelper.THREAD_TABLE, null, "url = ?", new String[]{url}, null, null, null);
        while (cursor.moveToNext()) {
            ThreadInfo info = new ThreadInfo();
            info.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
            info.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            info.setFileName(cursor.getString(cursor.getColumnIndex("file_name")));
            info.setTotalSize(cursor.getInt(cursor.getColumnIndex("totalSize")));
            info.setDownloadTime(cursor.getInt(cursor.getColumnIndex("downloadTime")));
            info.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
            list.add(info);
        }
        cursor.close();
        db.close();
        return list;
    }

    @Override
    public boolean isExists(String url, int threadId) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.THREAD_TABLE, null, "url = ? and thread_id = ?", new String[]{url, threadId + ""},
                null, null, null);
        boolean exists = cursor.moveToNext();
        cursor.close();
        db.close();
        return exists;
    }
}
