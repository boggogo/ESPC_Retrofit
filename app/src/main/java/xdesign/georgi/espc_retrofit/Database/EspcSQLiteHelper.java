package xdesign.georgi.espc_retrofit.Database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EspcSQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "espc.db";
    private static final int DATABASE_VERSION = 1;

    public static final String USERROOMRATING_TABLE_NAME = "UserRoomRating";
    public static final String USERROOMRATING_COLUMN_ID = "id";
    public static final String USERROOMRATING_COLUMN_ROOMID = "roomID";
    public static final String USERROOMRATING_COLUMN_USERID = "userID";
    public static final String USERROOMRATING_COLUMN_RATING = "rating";


    private static final String USERROOMRATING_CREATE = "create table "
            + USERROOMRATING_TABLE_NAME + "( " + USERROOMRATING_COLUMN_ID + " integer primary key autoincrement, "
            + USERROOMRATING_COLUMN_ROOMID + " integer, "
            + USERROOMRATING_COLUMN_USERID + " integer, "
            + USERROOMRATING_COLUMN_RATING + " integer); ";


    public static final String USERPROPERTYRATING_TABLE_NAME = "UserPropertyRating";
    public static final String USERPROPERTYRATING_COLUMN_ID = "id";
    public static final String USERPROPERTYRATING_COLUMN_OVERALLRATING = "overallRating";
    public static final String USERPROPERTYRATING_COLUMN_USERID = "userID";
    public static final String USERPROPERTYRATING_COLUMN_UUID = "uuid";
    public static final String USERPROPERTYRATING_COLUMN_PROPERTYID = "propertyID";


    private static final String USERPROPERTYRATING_CREATE = "create table "
            + USERPROPERTYRATING_TABLE_NAME + "( " + USERPROPERTYRATING_COLUMN_ID + " integer primary key autoincrement, "
            + USERPROPERTYRATING_COLUMN_OVERALLRATING + " integer, "
            + USERPROPERTYRATING_COLUMN_USERID + " integer, "
            + USERPROPERTYRATING_COLUMN_UUID + " text, "
            + USERPROPERTYRATING_COLUMN_PROPERTYID + " integer); ";


    public static final String USER_ESPC_TABLE_NAME = "User_ESPC";
    public static final String USER_ESPC_COLUMN_ID = "id";
    public static final String USER_ESPC_COLUMN_PASSWORD = "password";
    public static final String USER_ESPC_COLUMN_NAME = "name";


    private static final String USER_ESPC_CREATE = "create table "
            + USER_ESPC_TABLE_NAME + "( " + USER_ESPC_COLUMN_ID + " integer primary key autoincrement, "
            + USER_ESPC_COLUMN_PASSWORD + " integer, "
            + USER_ESPC_COLUMN_NAME + " text); ";


    public static final String SYNC_TABLE_NAME = "Sync";
    public static final String SYNC_COLUMN_ID = "id";
    public static final String SYNC_COLUMN_ACTION = "action";
    public static final String SYNC_COLUMN_TABLE = "table";
    public static final String SYNC_COLUMN_UUID = "uuid";
    public static final String SYNC_COLUMN_TIMECHANGED = "timeChanged";


    private static final String SYNC_CREATE = "create table "
            + SYNC_TABLE_NAME + "( " + SYNC_COLUMN_ID + " integer primary key autoincrement, "
            + SYNC_COLUMN_ACTION + " text, "
            + SYNC_COLUMN_TABLE + " text, "
            + SYNC_COLUMN_UUID + " text, "
            + SYNC_COLUMN_TIMECHANGED + " integer); ";


    public static final String ROOM_TABLE_NAME = "Room";
    public static final String ROOM_COLUMN_ID = "id";
    public static final String ROOM_COLUMN_NAME = "name";
    public static final String ROOM_COLUMN_PROPERTYID = "propertyID";


    private static final String ROOM_CREATE = "create table "
            + ROOM_TABLE_NAME + "( " + ROOM_COLUMN_ID + " integer primary key autoincrement, "
            + ROOM_COLUMN_NAME + " text, "
            + ROOM_COLUMN_PROPERTYID + " integer); ";


    public static final String PROPERTY_TABLE_NAME = "Property";
    public static final String PROPERTY_COLUMN_ID = "id";
    public static final String PROPERTY_COLUMN_PRICE = "price";
    public static final String PROPERTY_COLUMN_USERID = "userID";
    public static final String PROPERTY_COLUMN_UUID = "uuid";
    public static final String PROPERTY_COLUMN_LASTUPDATED = "lastUpdated";
    public static final String PROPERTY_COLUMN_ADDRESS = "address";


    private static final String PROPERTY_CREATE = "create table "
            + PROPERTY_TABLE_NAME + "( " + PROPERTY_COLUMN_ID + " integer primary key autoincrement, "
            + PROPERTY_COLUMN_PRICE + " integer, "
            + PROPERTY_COLUMN_USERID + " integer, "
            + PROPERTY_COLUMN_UUID + " text, "
            + PROPERTY_COLUMN_LASTUPDATED + " text, "
            + PROPERTY_COLUMN_ADDRESS + " text); ";


    public static final String FEATURE_TABLE_NAME = "Feature";
    public static final String FEATURE_COLUMN_ID = "id";
    public static final String FEATURE_COLUMN_ROOMID = "roomID";
    public static final String FEATURE_COLUMN_NAME = "name";


    private static final String FEATURE_CREATE = "create table "
            + FEATURE_TABLE_NAME + "( " + FEATURE_COLUMN_ID + " integer primary key autoincrement, "
            + FEATURE_COLUMN_ROOMID + " integer, "
            + FEATURE_COLUMN_NAME + " text); ";


    public EspcSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(USERROOMRATING_CREATE);
        database.execSQL(USERPROPERTYRATING_CREATE);
        database.execSQL(USER_ESPC_CREATE);
        database.execSQL(SYNC_CREATE);
        database.execSQL(ROOM_CREATE);
        database.execSQL(PROPERTY_CREATE);
        database.execSQL(FEATURE_CREATE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + USERROOMRATING_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USERPROPERTYRATING_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USER_ESPC_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SYNC_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ROOM_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PROPERTY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FEATURE_TABLE_NAME);
        onCreate(db);

    }


}