package com.example.jmemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MemoDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 4;
    private static final String DATABASE_NAME = "memo_db";
    private static final String TABLE_MEMOS = "memos";
    private static final String TABLE_FOLDERS = "folders";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_FOLDER_ID = "folder_id";

    private static final String COLUMN_FOLDER_NAME = "name";
    private static final String COLUMN_ORDER = "item_order";

    public MemoDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MEMOS_TABLE = "CREATE TABLE " + TABLE_MEMOS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_CONTENT + " TEXT,"
                + COLUMN_FOLDER_ID + " INTEGER,"
                + COLUMN_ORDER + " INTEGER" + ")";
        db.execSQL(CREATE_MEMOS_TABLE);

        String CREATE_FOLDERS_TABLE = "CREATE TABLE " + TABLE_FOLDERS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_FOLDER_NAME + " TEXT,"
                + COLUMN_ORDER + " INTEGER" + ")";
        db.execSQL(CREATE_FOLDERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 기존 테이블 삭제
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOLDERS);
        // 새로운 테이블 생성
        onCreate(db);
    }

    public void addFolder(Folder folder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOLDER_NAME, folder.getName());
        values.put(COLUMN_ORDER, folder.getOrder());
        db.insert(TABLE_FOLDERS, null, values);
        db.close();
    }

    public List<Folder> getAllFolders() {
        List<Folder> folderList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_FOLDERS + " ORDER BY " + COLUMN_ORDER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Folder folder = new Folder(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOLDER_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER))
                );
                folderList.add(folder);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return folderList;
    }

    public void deleteFolder(Folder folder) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FOLDERS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(folder.getId())});
        db.close();
    }

    public void updateFolderOrder(Folder folder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER, folder.getOrder());
        db.update(TABLE_FOLDERS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(folder.getId())});
        db.close();
    }

    public void updateMemoOrder(Memo memo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER, memo.getOrder());
        db.update(TABLE_MEMOS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(memo.getId())});
        db.close();
    }

    public void addMemo(Memo memo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, memo.getTitle());
        values.put(COLUMN_CONTENT, memo.getContent());
        values.put(COLUMN_FOLDER_ID, memo.getFolderId());
        values.put(COLUMN_ORDER, memo.getOrder());
        db.insert(TABLE_MEMOS, null, values);
        db.close();
    }

    public Memo getMemo(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MEMOS, new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_CONTENT, COLUMN_FOLDER_ID, COLUMN_ORDER},
                COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Memo memo = new Memo(
                Objects.requireNonNull(cursor).getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FOLDER_ID)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER))
        );
        cursor.close();
        return memo;
    }

    public List<Memo> getAllMemos() {
        List<Memo> memoList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_MEMOS + " ORDER BY " + COLUMN_ORDER;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Memo memo = new Memo(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FOLDER_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER))
                );
                memoList.add(memo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return memoList;
    }

    public List<Memo> getMemosByFolder(int folderId) {
        List<Memo> memoList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_MEMOS + " WHERE " + COLUMN_FOLDER_ID + " = " + folderId;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Memo memo = new Memo(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FOLDER_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER))
                );
                memoList.add(memo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return memoList;
    }

    public void updateMemo(Memo memo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, memo.getTitle());
        values.put(COLUMN_CONTENT, memo.getContent());
        values.put(COLUMN_FOLDER_ID, memo.getFolderId());
        db.update(TABLE_MEMOS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(memo.getId())});
    }

    public void deleteMemo(Memo memo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEMOS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(memo.getId())});
        db.close();
    }

    public int getMemoCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT COUNT(*) FROM " + TABLE_MEMOS;
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
}
