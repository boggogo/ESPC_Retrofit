package xdesign.georgi.espc_retrofit.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by georgi on 17/06/16.
 */
public class EspcSQLiteHelper extends SQLiteOpenHelper {
    //name of the table
    public static final String TABLE_NAME = "Property";
    //name of the column id
    public static final String COLUMN_ID = "id";
    //name of the column description
    public static final String COLUMN_PRICE = "price";
    //name of the column title
    public static final String COLUMN_ADDRESS = "address";
    //name of the column publication date
    public static final String COLUMN_USER_ID = "userID";
    //file name of the database
    public static final String COLUMN_LAST_UPDATED = "lastUpdated";
    private static final String DATABASE_NAME = "espc.db";
    //version number of the database
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "( " + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_PRICE + " text, "
            + COLUMN_ADDRESS + " text, "
            + COLUMN_USER_ID + " integer, "
            + COLUMN_LAST_UPDATED + " text " + ");";




    public EspcSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(EspcSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
