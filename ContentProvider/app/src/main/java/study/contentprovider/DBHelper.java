package study.contentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by simen on 25.07.2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String db = "example.db";

    public DBHelper(Context context) {
        super(context, db, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ItemEntry.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static class ItemEntry implements BaseColumns {

        public static final String TABLE_NAME = "item";

        public static final String _ID = BaseColumns._ID;
        public static final String TEXT = "text";

        public static final String CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME
                + " ( " + _ID + " integer primary key autoincrement, "
                + TEXT + " text);";
    }

}
