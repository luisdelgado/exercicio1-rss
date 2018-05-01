package br.ufpe.cin.if1001.rss.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import static br.ufpe.cin.if1001.rss.db.SQLiteRSSHelper.ITEM_LINK;

public class RssProvider extends ContentProvider {

    private SQLiteRSSHelper db;
    Context c;

    public RssProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        SQLiteDatabase db = this.db.getWritableDatabase();
        return db.delete(getTableName(uri), selection, selectionArgs);
    }

    @Override
    public String getType(Uri uri) {

        // at the given URI.
        String type = "";
        SQLiteDatabase db = this.db.getWritableDatabase();
        String search = "SELECT * FROM " + getTableName(uri) + " WHERE " +
                ITEM_LINK + " = ?";
        Cursor cursor = db.rawQuery(search, new String[] {String.valueOf(uri)});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                type = cursor.getString(5);
            }
            cursor.close();
        }
        return type;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = this.db.getWritableDatabase();
        if (db.insert(getTableName(uri), null, values) == -1) uri = null;
        return uri;
    }

    @Override
    public boolean onCreate() {
        c = this.getContext();
        if (db == null) {
            db = new SQLiteRSSHelper(this.getContext());
        }
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = this.db.getWritableDatabase();
        return db.query(getTableName(uri), projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = this.db.getWritableDatabase();
        return db.update(getTableName(uri), values, selection, selectionArgs);
    }

    public static String getTableName(Uri uri){
        String value = uri.getPath();
        value = value.replace("/", "");//we need to remove '/'
        return value;
    }
}