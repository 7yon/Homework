package study.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by simen on 25.07.2017.
 */

public class MyContentProvider extends ContentProvider {

    // // Uri
    // authority
    public static final String AUTHORITY = "study.contentprovider.MyContentProvider";

    // path
    static final String ITEM_PATH = DBHelper.ItemEntry.TABLE_NAME;

    // Общий Uri
    public static final Uri ITEM_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + ITEM_PATH);

    // Типы данных
    // набор строк
    static final String ITEM_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + ITEM_PATH;

    // одна строка
    static final String ITEM_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + ITEM_PATH;

    //// UriMatcher
    // общий Uri
    static final int URI_ITEMS = 1;

    // Uri с указанным ID
    static final int URI_ITEM_ID = 2;

    // описание и создание UriMatcher
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, ITEM_PATH, URI_ITEMS);
        uriMatcher.addURI(AUTHORITY, ITEM_PATH + "/#", URI_ITEM_ID);
    }

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private String LOG_TAG = "MyContentProvider";

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());

        //TODO return?
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

            switch (uriMatcher.match(uri)) {
                case URI_ITEMS: // общий Uri
                    Log.d(LOG_TAG, "URI_ITEMS");
                    if (TextUtils.isEmpty(sortOrder)) {
                        sortOrder = DBHelper.ItemEntry._ID + " DESC";
                    }
                    break;
                case URI_ITEM_ID: // Uri с ID
                    String id = uri.getLastPathSegment();
                    Log.d(LOG_TAG, "URI_ITEM_ID, " + id);
                    // добавляем ID к условию выборки
                    if (TextUtils.isEmpty(selection)) {
                        selection = DBHelper.ItemEntry._ID + " = " + id;
                    } else {
                        selection = selection + " AND " + DBHelper.ItemEntry._ID + " = " + id;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Wrong URI: " + uri);
            }
            db = dbHelper.getWritableDatabase();
            Cursor cursor = db.query(DBHelper.ItemEntry.TABLE_NAME, projection, selection,
                    selectionArgs, null, null, sortOrder);
            // просим ContentResolver уведомлять этот курсор
            // об изменениях данных в ITEM_CONTENT_URI
            cursor.setNotificationUri(getContext().getContentResolver(), ITEM_CONTENT_URI);

            return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());

        switch (uriMatcher.match(uri)) {
            case URI_ITEMS:
                return ITEM_CONTENT_TYPE;
            case URI_ITEM_ID:
                return ITEM_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.d(LOG_TAG, "insert, " + uri.toString());

        if (uriMatcher.match(uri) != URI_ITEMS)
            throw new IllegalArgumentException("Wrong URI: " + uri);

        db = dbHelper.getWritableDatabase();
        long rowID = db.insert(DBHelper.ItemEntry.TABLE_NAME, null, values);
        Uri resultUri = ContentUris.withAppendedId(ITEM_CONTENT_URI, rowID);

        // уведомляем ContentResolver, что данные по адресу resultUri изменились
        getContext().getContentResolver().notifyChange(resultUri, null);

        return resultUri;
    }



    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(LOG_TAG, "delete, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_ITEMS:
                Log.d(LOG_TAG, "URI_CONTACTS");
                break;
            case URI_ITEM_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = DBHelper.ItemEntry._ID + " = " + id;
                } else {
                    selection = selection + " AND " + DBHelper.ItemEntry._ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        db = dbHelper.getWritableDatabase();
        int lineCount = db.delete(DBHelper.ItemEntry.TABLE_NAME, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);

        return lineCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(LOG_TAG, "update, " + uri.toString());
        switch (uriMatcher.match(uri)) {
            case URI_ITEMS:
                Log.d(LOG_TAG, "URI_CONTACTS");

                break;
            case URI_ITEM_ID:
                String id = uri.getLastPathSegment();
                Log.d(LOG_TAG, "URI_CONTACTS_ID, " + id);
                if (TextUtils.isEmpty(selection)) {
                    selection = DBHelper.ItemEntry._ID + " = " + id;
                } else {
                    selection = selection + " AND " + DBHelper.ItemEntry._ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        db = dbHelper.getWritableDatabase();
        int cnt = db.update(DBHelper.ItemEntry.TABLE_NAME, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }
}
