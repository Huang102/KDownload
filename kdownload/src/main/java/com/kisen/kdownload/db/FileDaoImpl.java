package com.kisen.kdownload.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kisen.kdownload.entity.FileInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangwy on 2017/12/28.
 * email: kisenhuang@163.com.
 */

public class FileDaoImpl implements FileDao {

    private DBHelper mDBHelper;

    public FileDaoImpl(Context context) {
        mDBHelper = new DBHelper(context);
    }

    @Override
    public void insertFile(FileInfo info) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        if (db.isOpen()) {
            ContentValues values = new ContentValues();
            values.put("file_id", info.getId());
            values.put("url", info.getUrl());
            values.put("root_path", info.getRootPath());
            values.put("file_name", info.getFileName());
            values.put("length", info.getLength());
            values.put("finished", info.getFinished());
            db.insert(DBHelper.FILE_TABLE, null, values);
            db.close();
        }
    }

    @Override
    public void deleteFile(String url, int fileId) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        if (db.isOpen()) {
            db.delete(DBHelper.FILE_TABLE, "url = ? and file_id = ?", new String[]{url, fileId + ""});
            db.close();
        }
    }

    @Override
    public void updateFile(String url, int fileId, int finished) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        String sql = "update " + DBHelper.FILE_TABLE + " set finished = ? where url = ? and file_id = ?";
        db.execSQL(sql, new Object[]{finished, url, fileId});
        db.close();
    }

    @Override
    public List<FileInfo> queryFile(String url) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        List<FileInfo> list = new ArrayList<>();
        Cursor cursor = db.query(DBHelper.FILE_TABLE, null, "url = ?", new String[]{url}, null, null, null);
        while (cursor.moveToNext()) {
            FileInfo info = new FileInfo();
            info.setId(cursor.getInt(cursor.getColumnIndex("file_id")));
            info.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            info.setRootPath(cursor.getString(cursor.getColumnIndex("root_path")));
            info.setFileName(cursor.getString(cursor.getColumnIndex("file_name")));
            info.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
            info.setLength(cursor.getInt(cursor.getColumnIndex("length")));
            list.add(info);
        }
        cursor.close();
        db.close();
        return list;
    }

    @Override
    public List<FileInfo> queryAllFile() {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        List<FileInfo> list = new ArrayList<>();
        Cursor cursor = db.query(DBHelper.FILE_TABLE, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            FileInfo info = new FileInfo();
            info.setId(cursor.getInt(cursor.getColumnIndex("file_id")));
            info.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            info.setRootPath(cursor.getString(cursor.getColumnIndex("root_path")));
            info.setFileName(cursor.getString(cursor.getColumnIndex("file_name")));
            info.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
            info.setLength(cursor.getInt(cursor.getColumnIndex("length")));
            list.add(info);
        }
        cursor.close();
        db.close();
        return list;
    }

    @Override
    public boolean isExists(String url) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor cursor = db.query(DBHelper.FILE_TABLE, null, "url = ?", new String[]{url},
                null, null, null);
        boolean exists = cursor.moveToNext();
        cursor.close();
        db.close();
        return exists;
    }
}
