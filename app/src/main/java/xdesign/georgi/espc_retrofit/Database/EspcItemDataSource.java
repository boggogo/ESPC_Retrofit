package xdesign.georgi.espc_retrofit.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import xdesign.georgi.espc_retrofit.Backend.Feature;
import xdesign.georgi.espc_retrofit.Backend.Property;
import xdesign.georgi.espc_retrofit.Backend.Room;
import xdesign.georgi.espc_retrofit.Backend.Sync;
import xdesign.georgi.espc_retrofit.Backend.UserPropertyRating;
import xdesign.georgi.espc_retrofit.Backend.UserRoomRating;
import xdesign.georgi.espc_retrofit.Backend.User_ESPC;

public class EspcItemDataSource {

    private SQLiteDatabase mDatabase;
    private Context mContext;
    private EspcSQLiteHelper mDbHelper;

    private String[] allUserRoomRatingCollumns = {
            EspcSQLiteHelper.USERROOMRATING_COLUMN_ID,
            EspcSQLiteHelper.USERROOMRATING_COLUMN_ROOMID,
            EspcSQLiteHelper.USERROOMRATING_COLUMN_USERID,
            EspcSQLiteHelper.USERROOMRATING_COLUMN_RATING,
    };
    private String[] allUserPropertyRatingCollumns = {
            EspcSQLiteHelper.USERPROPERTYRATING_COLUMN_ID,
            EspcSQLiteHelper.USERPROPERTYRATING_COLUMN_OVERALLRATING,
            EspcSQLiteHelper.USERPROPERTYRATING_COLUMN_USERID,
            EspcSQLiteHelper.USERPROPERTYRATING_COLUMN_UUID,
            EspcSQLiteHelper.USERPROPERTYRATING_COLUMN_PROPERTYID,
    };
    private String[] allUser_ESPCCollumns = {
            EspcSQLiteHelper.USER_ESPC_COLUMN_ID,
            EspcSQLiteHelper.USER_ESPC_COLUMN_PASSWORD,
            EspcSQLiteHelper.USER_ESPC_COLUMN_NAME,
    };
    private String[] allSyncCollumns = {
            EspcSQLiteHelper.SYNC_COLUMN_ID,
            EspcSQLiteHelper.SYNC_COLUMN_ACTION,
            EspcSQLiteHelper.SYNC_COLUMN_TABLE,
            EspcSQLiteHelper.SYNC_COLUMN_UUID,
            EspcSQLiteHelper.SYNC_COLUMN_TIMECHANGED,
    };
    private String[] allRoomCollumns = {
            EspcSQLiteHelper.ROOM_COLUMN_ID,
            EspcSQLiteHelper.ROOM_COLUMN_NAME,
            EspcSQLiteHelper.ROOM_COLUMN_PROPERTYID,
    };
    private String[] allPropertyCollumns = {
            EspcSQLiteHelper.PROPERTY_COLUMN_ID,
            EspcSQLiteHelper.PROPERTY_COLUMN_PRICE,
            EspcSQLiteHelper.PROPERTY_COLUMN_USERID,
            EspcSQLiteHelper.PROPERTY_COLUMN_UUID,
            EspcSQLiteHelper.PROPERTY_COLUMN_LASTUPDATED,
            EspcSQLiteHelper.PROPERTY_COLUMN_ADDRESS,
    };
    private String[] allFeatureCollumns = {
            EspcSQLiteHelper.FEATURE_COLUMN_ID,
            EspcSQLiteHelper.FEATURE_COLUMN_ROOMID,
            EspcSQLiteHelper.FEATURE_COLUMN_NAME,
    };

    public UserRoomRating createUserRoomRatingItem(UserRoomRating item) {
        UserRoomRating tempItem = item;
        ContentValues values = new ContentValues();
        values.put(EspcSQLiteHelper.USERROOMRATING_COLUMN_ID, item.getId());
        values.put(EspcSQLiteHelper.USERROOMRATING_COLUMN_ROOMID, item.getUserroomrating_column_roomid());
        values.put(EspcSQLiteHelper.USERROOMRATING_COLUMN_USERID, item.getUserroomrating_column_userid());
        values.put(EspcSQLiteHelper.USERROOMRATING_COLUMN_RATING, item.getUserroomrating_column_rating());
        long insertId = mDatabase.insert(EspcSQLiteHelper.USERROOMRATING_TABLE_NAME, null, values);
        return tempItem;
    }

    public UserPropertyRating createUserPropertyRatingItem(UserPropertyRating item) {
        UserPropertyRating tempItem = item;
        ContentValues values = new ContentValues();
        values.put(EspcSQLiteHelper.USERPROPERTYRATING_COLUMN_ID, item.getId());
        values.put(EspcSQLiteHelper.USERPROPERTYRATING_COLUMN_OVERALLRATING, item.getUserpropertyrating_column_overallrating());
        values.put(EspcSQLiteHelper.USERPROPERTYRATING_COLUMN_USERID, item.getUserpropertyrating_column_userid());
        values.put(EspcSQLiteHelper.USERPROPERTYRATING_COLUMN_UUID, item.getUserpropertyrating_column_uuid());
        values.put(EspcSQLiteHelper.USERPROPERTYRATING_COLUMN_PROPERTYID, item.getUserpropertyrating_column_propertyid());
        long insertId = mDatabase.insert(EspcSQLiteHelper.USERPROPERTYRATING_TABLE_NAME, null, values);
        return tempItem;
    }

    public User_ESPC createUser_ESPCItem(User_ESPC item) {
        User_ESPC tempItem = item;
        ContentValues values = new ContentValues();
        values.put(EspcSQLiteHelper.USER_ESPC_COLUMN_ID, item.getId());
        values.put(EspcSQLiteHelper.USER_ESPC_COLUMN_PASSWORD, item.getUser_espc_column_password());
        values.put(EspcSQLiteHelper.USER_ESPC_COLUMN_NAME, item.getUser_espc_column_name());
        long insertId = mDatabase.insert(EspcSQLiteHelper.USER_ESPC_TABLE_NAME, null, values);
        return tempItem;
    }

    public Sync createSyncItem(Sync item) {
        Sync tempItem = item;
        ContentValues values = new ContentValues();
        values.put(EspcSQLiteHelper.SYNC_COLUMN_ID, item.getId());
        values.put(EspcSQLiteHelper.SYNC_COLUMN_ACTION, item.getSync_column_action());
        values.put(EspcSQLiteHelper.SYNC_COLUMN_TABLE, item.getSync_column_table());
        values.put(EspcSQLiteHelper.SYNC_COLUMN_UUID, item.getSync_column_uuid());
        values.put(EspcSQLiteHelper.SYNC_COLUMN_TIMECHANGED, item.getSync_column_timechanged());
        long insertId = mDatabase.insert(EspcSQLiteHelper.SYNC_TABLE_NAME, null, values);
        return tempItem;
    }

    public Room createRoomItem(Room item) {
        Room tempItem = item;
        ContentValues values = new ContentValues();
        values.put(EspcSQLiteHelper.ROOM_COLUMN_ID, item.getId());
        values.put(EspcSQLiteHelper.ROOM_COLUMN_NAME, item.getRoom_column_name());
        values.put(EspcSQLiteHelper.ROOM_COLUMN_PROPERTYID, item.getRoom_column_propertyid());
        long insertId = mDatabase.insert(EspcSQLiteHelper.ROOM_TABLE_NAME, null, values);
        return tempItem;
    }

    public Property createPropertyItem(Property item) {
        Property tempItem = item;
        ContentValues values = new ContentValues();
        values.put(EspcSQLiteHelper.PROPERTY_COLUMN_ID, item.getId());
        values.put(EspcSQLiteHelper.PROPERTY_COLUMN_PRICE, item.getProperty_column_price());
        values.put(EspcSQLiteHelper.PROPERTY_COLUMN_USERID, item.getProperty_column_userid());
        values.put(EspcSQLiteHelper.PROPERTY_COLUMN_UUID, item.getProperty_column_uuid());
        values.put(EspcSQLiteHelper.PROPERTY_COLUMN_LASTUPDATED, item.getProperty_column_lastupdated());
        values.put(EspcSQLiteHelper.PROPERTY_COLUMN_ADDRESS, item.getProperty_column_address());
        long insertId = mDatabase.insert(EspcSQLiteHelper.PROPERTY_TABLE_NAME, null, values);
        return tempItem;
    }

    public Feature createFeatureItem(Feature item) {
        Feature tempItem = item;
        ContentValues values = new ContentValues();
        values.put(EspcSQLiteHelper.FEATURE_COLUMN_ID, item.getId());
        values.put(EspcSQLiteHelper.FEATURE_COLUMN_ROOMID, item.getFeature_column_roomid());
        values.put(EspcSQLiteHelper.FEATURE_COLUMN_NAME, item.getFeature_column_name());
        long insertId = mDatabase.insert(EspcSQLiteHelper.FEATURE_TABLE_NAME, null, values);
        return tempItem;
    }

    public void deleteUserRoomRatingItem(UserRoomRating item) {
        long id = item.getId();
        mDatabase.delete(EspcSQLiteHelper.USERROOMRATING_TABLE_NAME, EspcSQLiteHelper.USERROOMRATING_COLUMN_ID
                + " = " + id, null);
    }

    public void deleteUserPropertyRatingItem(UserPropertyRating item) {
        long id = item.getId();
        mDatabase.delete(EspcSQLiteHelper.USERPROPERTYRATING_TABLE_NAME, EspcSQLiteHelper.USERPROPERTYRATING_COLUMN_ID
                + " = " + id, null);
    }

    public void deleteUser_ESPCItem(User_ESPC item) {
        long id = item.getId();
        mDatabase.delete(EspcSQLiteHelper.USER_ESPC_TABLE_NAME, EspcSQLiteHelper.USER_ESPC_COLUMN_ID
                + " = " + id, null);
    }

    public void deleteSyncItem(Sync item) {
        long id = item.getId();
        mDatabase.delete(EspcSQLiteHelper.SYNC_TABLE_NAME, EspcSQLiteHelper.SYNC_COLUMN_ID
                + " = " + id, null);
    }

    public void deleteRoomItem(Room item) {
        long id = item.getId();
        mDatabase.delete(EspcSQLiteHelper.ROOM_TABLE_NAME, EspcSQLiteHelper.ROOM_COLUMN_ID
                + " = " + id, null);
    }

    public void deletePropertyItem(Property item) {
        long id = item.getId();
        mDatabase.delete(EspcSQLiteHelper.PROPERTY_TABLE_NAME, EspcSQLiteHelper.PROPERTY_COLUMN_ID
                + " = " + id, null);
    }

    public void deleteFeatureItem(Feature item) {
        long id = item.getId();
        mDatabase.delete(EspcSQLiteHelper.FEATURE_TABLE_NAME, EspcSQLiteHelper.FEATURE_COLUMN_ID
                + " = " + id, null);
    }

    public void deleteAllUserRoomRating() {
        mDatabase.delete(EspcSQLiteHelper.USERROOMRATING_TABLE_NAME, null, null);
    }

    public void deleteAllUserPropertyRating() {
        mDatabase.delete(EspcSQLiteHelper.USERPROPERTYRATING_TABLE_NAME, null, null);
    }

    public void deleteAllUser_ESPC() {
        mDatabase.delete(EspcSQLiteHelper.USER_ESPC_TABLE_NAME, null, null);
    }

    public void deleteAllSync() {
        mDatabase.delete(EspcSQLiteHelper.SYNC_TABLE_NAME, null, null);
    }

    public void deleteAllRoom() {
        mDatabase.delete(EspcSQLiteHelper.ROOM_TABLE_NAME, null, null);
    }

    public void deleteAllProperty() {
        mDatabase.delete(EspcSQLiteHelper.PROPERTY_TABLE_NAME, null, null);
    }

    public void deleteAllFeature() {
        mDatabase.delete(EspcSQLiteHelper.FEATURE_TABLE_NAME, null, null);
    }

    public void updateUserRoomRatingItem(UserRoomRating item) {
        long id = item.getId();
        ContentValues values = new ContentValues();
        values.put(EspcSQLiteHelper.USERROOMRATING_COLUMN_ID, item.getId());
        values.put(EspcSQLiteHelper.USERROOMRATING_COLUMN_ROOMID, item.getUserroomrating_column_roomid());
        values.put(EspcSQLiteHelper.USERROOMRATING_COLUMN_USERID, item.getUserroomrating_column_userid());
        values.put(EspcSQLiteHelper.USERROOMRATING_COLUMN_RATING, item.getUserroomrating_column_rating());
        mDatabase.update(
                EspcSQLiteHelper.USERROOMRATING_TABLE_NAME,
                values,
                "id=?",
                new String[]{"" + id});
    }

    public void updateUserPropertyRatingItem(UserPropertyRating item) {
        long id = item.getId();
        ContentValues values = new ContentValues();
        values.put(EspcSQLiteHelper.USERPROPERTYRATING_COLUMN_ID, item.getId());
        values.put(EspcSQLiteHelper.USERPROPERTYRATING_COLUMN_OVERALLRATING, item.getUserpropertyrating_column_overallrating());
        values.put(EspcSQLiteHelper.USERPROPERTYRATING_COLUMN_USERID, item.getUserpropertyrating_column_userid());
        values.put(EspcSQLiteHelper.USERPROPERTYRATING_COLUMN_UUID, item.getUserpropertyrating_column_uuid());
        values.put(EspcSQLiteHelper.USERPROPERTYRATING_COLUMN_PROPERTYID, item.getUserpropertyrating_column_propertyid());
        mDatabase.update(
                EspcSQLiteHelper.USERPROPERTYRATING_TABLE_NAME,
                values,
                "id=?",
                new String[]{"" + id});
    }

    public void updateUser_ESPCItem(User_ESPC item) {
        long id = item.getId();
        ContentValues values = new ContentValues();
        values.put(EspcSQLiteHelper.USER_ESPC_COLUMN_ID, item.getId());
        values.put(EspcSQLiteHelper.USER_ESPC_COLUMN_PASSWORD, item.getUser_espc_column_password());
        values.put(EspcSQLiteHelper.USER_ESPC_COLUMN_NAME, item.getUser_espc_column_name());
        mDatabase.update(
                EspcSQLiteHelper.USER_ESPC_TABLE_NAME,
                values,
                "id=?",
                new String[]{"" + id});
    }

    public void updateSyncItem(Sync item) {
        long id = item.getId();
        ContentValues values = new ContentValues();
        values.put(EspcSQLiteHelper.SYNC_COLUMN_ID, item.getId());
        values.put(EspcSQLiteHelper.SYNC_COLUMN_ACTION, item.getSync_column_action());
        values.put(EspcSQLiteHelper.SYNC_COLUMN_TABLE, item.getSync_column_table());
        values.put(EspcSQLiteHelper.SYNC_COLUMN_UUID, item.getSync_column_uuid());
        values.put(EspcSQLiteHelper.SYNC_COLUMN_TIMECHANGED, item.getSync_column_timechanged());
        mDatabase.update(
                EspcSQLiteHelper.SYNC_TABLE_NAME,
                values,
                "id=?",
                new String[]{"" + id});
    }

    public void updateRoomItem(Room item) {
        long id = item.getId();
        ContentValues values = new ContentValues();
        values.put(EspcSQLiteHelper.ROOM_COLUMN_ID, item.getId());
        values.put(EspcSQLiteHelper.ROOM_COLUMN_NAME, item.getRoom_column_name());
        values.put(EspcSQLiteHelper.ROOM_COLUMN_PROPERTYID, item.getRoom_column_propertyid());
        mDatabase.update(
                EspcSQLiteHelper.ROOM_TABLE_NAME,
                values,
                "id=?",
                new String[]{"" + id});
    }

    public void updatePropertyItem(Property item) {
        long id = item.getId();
        ContentValues values = new ContentValues();
        values.put(EspcSQLiteHelper.PROPERTY_COLUMN_ID, item.getId());
        values.put(EspcSQLiteHelper.PROPERTY_COLUMN_PRICE, item.getProperty_column_price());
        values.put(EspcSQLiteHelper.PROPERTY_COLUMN_USERID, item.getProperty_column_userid());
        values.put(EspcSQLiteHelper.PROPERTY_COLUMN_UUID, item.getProperty_column_uuid());
        values.put(EspcSQLiteHelper.PROPERTY_COLUMN_LASTUPDATED, item.getProperty_column_lastupdated());
        values.put(EspcSQLiteHelper.PROPERTY_COLUMN_ADDRESS, item.getProperty_column_address());
        mDatabase.update(
                EspcSQLiteHelper.PROPERTY_TABLE_NAME,
                values,
                "id=?",
                new String[]{"" + id});
    }

    public void updateFeatureItem(Feature item) {
        long id = item.getId();
        ContentValues values = new ContentValues();
        values.put(EspcSQLiteHelper.FEATURE_COLUMN_ID, item.getId());
        values.put(EspcSQLiteHelper.FEATURE_COLUMN_ROOMID, item.getFeature_column_roomid());
        values.put(EspcSQLiteHelper.FEATURE_COLUMN_NAME, item.getFeature_column_name());
        mDatabase.update(
                EspcSQLiteHelper.FEATURE_TABLE_NAME,
                values,
                "id=?",
                new String[]{"" + id});
    }

    public UserRoomRating getUserRoomRatingItemById(int id) {
        String q = " SELECT * FROM " + EspcSQLiteHelper.USERROOMRATING_TABLE_NAME + "WHERE id = " + id;
        Cursor cursor = mDatabase.rawQuery(q, null);
        if (cursor != null)
            cursor.moveToFirst();

        return generateUserRoomRatingObjectFromCursor(cursor);
    }

    public UserRoomRating generateUserRoomRatingObjectFromCursor(Cursor cursor) {
        if (cursor == null)
            return null;
        UserRoomRating userroomratingItem = new UserRoomRating();
        userroomratingItem.setId(cursor.getInt(cursor.getColumnIndex(EspcSQLiteHelper.USERROOMRATING_COLUMN_ID)));
        userroomratingItem.setUSERROOMRATING_COLUMN_ROOMID(cursor.getInt(cursor.getColumnIndex(EspcSQLiteHelper.USERROOMRATING_COLUMN_ROOMID)));
        userroomratingItem.setUSERROOMRATING_COLUMN_USERID(cursor.getInt(cursor.getColumnIndex(EspcSQLiteHelper.USERROOMRATING_COLUMN_USERID)));
        userroomratingItem.setUSERROOMRATING_COLUMN_RATING(cursor.getInt(cursor.getColumnIndex(EspcSQLiteHelper.USERROOMRATING_COLUMN_RATING)));
        return userroomratingItem;
    }

    public UserPropertyRating getUserPropertyRatingItemById(int id) {
        String q = " SELECT * FROM " + EspcSQLiteHelper.USERPROPERTYRATING_TABLE_NAME + "WHERE id = " + id;
        Cursor cursor = mDatabase.rawQuery(q, null);
        if (cursor != null)
            cursor.moveToFirst();

        return generateUserPropertyRatingObjectFromCursor(cursor);
    }

    public UserPropertyRating generateUserPropertyRatingObjectFromCursor(Cursor cursor) {
        if (cursor == null)
            return null;
        UserPropertyRating userpropertyratingItem = new UserPropertyRating();
        userpropertyratingItem.setId(cursor.getInt(cursor.getColumnIndex(EspcSQLiteHelper.USERPROPERTYRATING_COLUMN_ID)));
        userpropertyratingItem.setUSERPROPERTYRATING_COLUMN_OVERALLRATING(cursor.getInt(cursor.getColumnIndex(EspcSQLiteHelper.USERPROPERTYRATING_COLUMN_OVERALLRATING)));
        userpropertyratingItem.setUSERPROPERTYRATING_COLUMN_USERID(cursor.getInt(cursor.getColumnIndex(EspcSQLiteHelper.USERPROPERTYRATING_COLUMN_USERID)));
        userpropertyratingItem.setUSERPROPERTYRATING_COLUMN_UUID(cursor.getString(cursor.getColumnIndex(EspcSQLiteHelper.USERPROPERTYRATING_COLUMN_UUID)));
        userpropertyratingItem.setUSERPROPERTYRATING_COLUMN_PROPERTYID(cursor.getInt(cursor.getColumnIndex(EspcSQLiteHelper.USERPROPERTYRATING_COLUMN_PROPERTYID)));
        return userpropertyratingItem;
    }

    public User_ESPC getUser_ESPCItemById(int id) {
        String q = " SELECT * FROM " + EspcSQLiteHelper.USER_ESPC_TABLE_NAME + "WHERE id = " + id;
        Cursor cursor = mDatabase.rawQuery(q, null);
        if (cursor != null)
            cursor.moveToFirst();

        return generateUser_ESPCObjectFromCursor(cursor);
    }

    public User_ESPC generateUser_ESPCObjectFromCursor(Cursor cursor) {
        if (cursor == null)
            return null;
        User_ESPC user_espcItem = new User_ESPC();
        user_espcItem.setId(cursor.getInt(cursor.getColumnIndex(EspcSQLiteHelper.USER_ESPC_COLUMN_ID)));
        user_espcItem.setUSER_ESPC_COLUMN_PASSWORD(cursor.getInt(cursor.getColumnIndex(EspcSQLiteHelper.USER_ESPC_COLUMN_PASSWORD)));
        user_espcItem.setUSER_ESPC_COLUMN_NAME(cursor.getString(cursor.getColumnIndex(EspcSQLiteHelper.USER_ESPC_COLUMN_NAME)));
        return user_espcItem;
    }

    public Sync getSyncItemById(int id) {
        String q = " SELECT * FROM " + EspcSQLiteHelper.SYNC_TABLE_NAME + "WHERE id = " + id;
        Cursor cursor = mDatabase.rawQuery(q, null);
        if (cursor != null)
            cursor.moveToFirst();

        return generateSyncObjectFromCursor(cursor);
    }

    public Sync generateSyncObjectFromCursor(Cursor cursor) {
        if (cursor == null)
            return null;
        Sync syncItem = new Sync();
        syncItem.setId(cursor.getInt(cursor.getColumnIndex(EspcSQLiteHelper.SYNC_COLUMN_ID)));
        syncItem.setSYNC_COLUMN_ACTION(cursor.getString(cursor.getColumnIndex(EspcSQLiteHelper.SYNC_COLUMN_ACTION)));
        syncItem.setSYNC_COLUMN_TABLE(cursor.getString(cursor.getColumnIndex(EspcSQLiteHelper.SYNC_COLUMN_TABLE)));
        syncItem.setSYNC_COLUMN_UUID(cursor.getString(cursor.getColumnIndex(EspcSQLiteHelper.SYNC_COLUMN_UUID)));
        syncItem.setSYNC_COLUMN_TIMECHANGED(cursor.getInt(cursor.getColumnIndex(EspcSQLiteHelper.SYNC_COLUMN_TIMECHANGED)));
        return syncItem;
    }

    public Room getRoomItemById(int id) {
        String q = " SELECT * FROM " + EspcSQLiteHelper.ROOM_TABLE_NAME + "WHERE id = " + id;
        Cursor cursor = mDatabase.rawQuery(q, null);
        if (cursor != null)
            cursor.moveToFirst();

        return generateRoomObjectFromCursor(cursor);
    }

    public Room generateRoomObjectFromCursor(Cursor cursor) {
        if (cursor == null)
            return null;
        Room roomItem = new Room();
        roomItem.setId(cursor.getInt(cursor.getColumnIndex(EspcSQLiteHelper.ROOM_COLUMN_ID)));
        roomItem.setROOM_COLUMN_NAME(cursor.getString(cursor.getColumnIndex(EspcSQLiteHelper.ROOM_COLUMN_NAME)));
        roomItem.setROOM_COLUMN_PROPERTYID(cursor.getInt(cursor.getColumnIndex(EspcSQLiteHelper.ROOM_COLUMN_PROPERTYID)));
        return roomItem;
    }

    public Property getPropertyItemById(int id) {
        String q = " SELECT * FROM " + EspcSQLiteHelper.PROPERTY_TABLE_NAME + "WHERE id = " + id;
        Cursor cursor = mDatabase.rawQuery(q, null);
        if (cursor != null)
            cursor.moveToFirst();

        return generatePropertyObjectFromCursor(cursor);
    }

    public Property generatePropertyObjectFromCursor(Cursor cursor) {
        if (cursor == null)
            return null;
        Property propertyItem = new Property();
        propertyItem.setId(cursor.getInt(cursor.getColumnIndex(EspcSQLiteHelper.PROPERTY_COLUMN_ID)));
        propertyItem.setPROPERTY_COLUMN_PRICE(cursor.getInt(cursor.getColumnIndex(EspcSQLiteHelper.PROPERTY_COLUMN_PRICE)));
        propertyItem.setPROPERTY_COLUMN_USERID(cursor.getInt(cursor.getColumnIndex(EspcSQLiteHelper.PROPERTY_COLUMN_USERID)));
        propertyItem.setPROPERTY_COLUMN_UUID(cursor.getString(cursor.getColumnIndex(EspcSQLiteHelper.PROPERTY_COLUMN_UUID)));
        propertyItem.setPROPERTY_COLUMN_LASTUPDATED(cursor.getString(cursor.getColumnIndex(EspcSQLiteHelper.PROPERTY_COLUMN_LASTUPDATED)));
        propertyItem.setPROPERTY_COLUMN_ADDRESS(cursor.getString(cursor.getColumnIndex(EspcSQLiteHelper.PROPERTY_COLUMN_ADDRESS)));
        return propertyItem;
    }

    public Feature getFeatureItemById(int id) {
        String q = " SELECT * FROM " + EspcSQLiteHelper.FEATURE_TABLE_NAME + "WHERE id = " + id;
        Cursor cursor = mDatabase.rawQuery(q, null);
        if (cursor != null)
            cursor.moveToFirst();

        return generateFeatureObjectFromCursor(cursor);
    }

    public Feature generateFeatureObjectFromCursor(Cursor cursor) {
        if (cursor == null)
            return null;
        Feature featureItem = new Feature();
        featureItem.setId(cursor.getInt(cursor.getColumnIndex(EspcSQLiteHelper.FEATURE_COLUMN_ID)));
        featureItem.setFEATURE_COLUMN_ROOMID(cursor.getInt(cursor.getColumnIndex(EspcSQLiteHelper.FEATURE_COLUMN_ROOMID)));
        featureItem.setFEATURE_COLUMN_NAME(cursor.getString(cursor.getColumnIndex(EspcSQLiteHelper.FEATURE_COLUMN_NAME)));
        return featureItem;
    }

    private static EspcItemDataSource mInstance = null;

    public static EspcItemDataSource getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new EspcItemDataSource(ctx.getApplicationContext());
        }
        return mInstance;
    }

    public EspcItemDataSource(Context context) {
        mDbHelper = new EspcSQLiteHelper(context);
        mContext = context;
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }
}