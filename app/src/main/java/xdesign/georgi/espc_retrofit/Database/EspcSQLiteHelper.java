package xdesign.georgi.espc_retrofit.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by georgi on 17/06/16.
 */
public class EspcSQLiteHelper extends SQLiteOpenHelper {

    //==============================Property==========================
    //name of the table
    public static final String PROPERTY_TABLE_NAME = "Property";
    //name of the column id
    public static final String PROPERTY_COLUMN_ID = "id";
    //name of the column description
    public static final String PROPERTY_COLUMN_PRICE = "price";
    //name of the column title
    public static final String PROPERTY_COLUMN_ADDRESS = "address";
    //name of the column publication date
    public static final String PROPERTY_COLUMN_USER_ID = "userID";
    // name of the column uuid
    public static final String PROPERTY_COLUMN_UUID = "uuid";
    //file name of the database
    public static final String PROPERTY_COLUMN_LAST_UPDATED = "lastUpdated";


    //==============================UserPropertyRating===================
    //name of the table
    public static final String USER_PROPERTY_RATING_TABLE_NAME = "UserPropertyRating";
    //name of the column id
    public static final String USER_PROPERTY_RATING_COLUMN_ID = "id";
    // name of the column uuid
    public static final String USER_PROPERTY_RATING_COLUMN_UUID = "uuid";
    //name of the column publication date
    public static final String USER_PROPERTY_RATING_COLUMN_USER_ID = "userID";
    //name of the column description
    public static final String USER_PROPERTY_RATING_COLUMN_PROPERTY_ID = "propertyID";
    //name of the column title
    public static final String USER_PROPERTY_RATING_COLUMN_OVERALL_RATING = "overallRating";


    private static final String DATABASE_NAME = "espc.db";
    //version number of the database
    private static final int DATABASE_VERSION = 1;

    // Property table creation sql statement
    private static final String PROPERTY_CREATE = "create table "
            + PROPERTY_TABLE_NAME + "( " + PROPERTY_COLUMN_ID + " integer primary key autoincrement, "
            + PROPERTY_COLUMN_UUID + " text, "
            + PROPERTY_COLUMN_PRICE + " text, "
            + PROPERTY_COLUMN_ADDRESS + " text, "
            + PROPERTY_COLUMN_USER_ID + " integer, "
            + PROPERTY_COLUMN_LAST_UPDATED + " text " + ");";

    // Property table creation sql statement
    private static final String USER_PROPERTY_RATING_CREATE = "create table "
            + USER_PROPERTY_RATING_TABLE_NAME + "( " + USER_PROPERTY_RATING_COLUMN_ID + " integer primary key autoincrement, "
            + USER_PROPERTY_RATING_COLUMN_UUID + " text, "
            + USER_PROPERTY_RATING_COLUMN_USER_ID + " integer, "
            + USER_PROPERTY_RATING_COLUMN_PROPERTY_ID + " integer, "
            + USER_PROPERTY_RATING_COLUMN_OVERALL_RATING + " integer " + ");";



    public EspcSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        // create Property table...
        database.execSQL(PROPERTY_CREATE);
        // create UserPropertyRating table...
        database.execSQL(USER_PROPERTY_RATING_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(EspcSQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + PROPERTY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USER_PROPERTY_RATING_TABLE_NAME);
        onCreate(db);
    }
}
