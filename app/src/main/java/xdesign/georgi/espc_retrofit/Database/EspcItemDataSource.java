package xdesign.georgi.espc_retrofit.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import xdesign.georgi.espc_retrofit.Backend.Property;
import xdesign.georgi.espc_retrofit.Backend.UserPropertyRating;

/**
 * EspcItemDataSource class that encapsulates the database object
 */
public class EspcItemDataSource {
    //The context of the class
    private Context mContext;
    //string tag used for debugging purposes
    private static final String TAG = EspcItemDataSource.class.getSimpleName();
    // SQLiteDatabase object
    private SQLiteDatabase mDatabase;
    // RssFeedSQLiteHelper object
    private EspcSQLiteHelper mDbHelper;
    // string array of all Property table columns
    private String[] allPropertyColumns = {
            EspcSQLiteHelper.PROPERTY_COLUMN_ID,
            EspcSQLiteHelper.PROPERTY_COLUMN_UUID,
            EspcSQLiteHelper.PROPERTY_COLUMN_PRICE,
            EspcSQLiteHelper.PROPERTY_COLUMN_ADDRESS,
            EspcSQLiteHelper.PROPERTY_COLUMN_USER_ID,
            EspcSQLiteHelper.PROPERTY_COLUMN_LAST_UPDATED
    };

    private String[] allUserPropertyRatingColumns = {
            EspcSQLiteHelper.USER_PROPERTY_RATING_COLUMN_ID,
            EspcSQLiteHelper.USER_PROPERTY_RATING_COLUMN_UUID,
            EspcSQLiteHelper.USER_PROPERTY_RATING_COLUMN_USER_ID,
            EspcSQLiteHelper.USER_PROPERTY_RATING_COLUMN_PROPERTY_ID,
            EspcSQLiteHelper.USER_PROPERTY_RATING_COLUMN_OVERALL_RATING
    };

    private static EspcItemDataSource mInstance = null;

    public static EspcItemDataSource getInstance(Context ctx) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new EspcItemDataSource(ctx.getApplicationContext());
        }
        return mInstance;
    }

    /**
     * Constructor of the class
     *
     * @param context normally an Activity object is passed as context
     */
    public EspcItemDataSource(Context context) {
        //instanciate class fields...
        mDbHelper = new EspcSQLiteHelper(context);
        mContext = context;
    }

    // open the database for writing...
    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    /**
     * Method that closes the database for future manipulations
     */
    public void close() {
        mDbHelper.close();
    }

    /**
     * Method that creates and adds a new entry in the database
     *
     * @param item object of type RssItem to be added in the database
     * @return return the newly added object for further calculations
     */
    public Property createPropertyItem(Property item) {
        //temporary item of type RssItem
        Property tempItem = item;
        // values to be added in the db
        ContentValues values = new ContentValues();
        //check of the item to be added in the db is paced correctly by the saxParser

        values.put(EspcSQLiteHelper.PROPERTY_COLUMN_ID, item.getId());
        values.put(EspcSQLiteHelper.PROPERTY_COLUMN_ADDRESS, item.getAddress());
        values.put(EspcSQLiteHelper.PROPERTY_COLUMN_PRICE, item.getPrice());
        values.put(EspcSQLiteHelper.PROPERTY_COLUMN_UUID, item.getUuid());
        values.put(EspcSQLiteHelper.PROPERTY_COLUMN_USER_ID, item.getUserID());
        values.put(EspcSQLiteHelper.PROPERTY_COLUMN_LAST_UPDATED, item.getLastUpdated());

        if (!ifExistsLocally(item)) {
            // insert the values and store the id returned by the operation
            long insertId = mDatabase.insert(EspcSQLiteHelper.PROPERTY_TABLE_NAME, null, values);
            //log the inserting item for debugging purposes
            Log.d("MainActivity", "ADDING Property to db");
            Log.d(TAG, "=============================================");
            Log.d(TAG, "id: " + tempItem.getId());
            Log.d(TAG, "address: " + tempItem.getAddress());
            Log.d(TAG, "price: " + tempItem.getPrice());
            Log.d(TAG, "userID: " + tempItem.getUserID());
            Log.d(TAG, "lastUpdated: " + tempItem.getLastUpdated());
            Log.d(TAG, "=============================================");
        } else {
            Log.d(TAG, "Property: " + item.getAddress() + " Already Exists in the DB");
        }
        return tempItem;
    }


    public UserPropertyRating createUserPropertyRatingItem(UserPropertyRating upr) {
        UserPropertyRating userPropertyRating = upr;
        ContentValues values = new ContentValues();

        values.put(EspcSQLiteHelper.USER_PROPERTY_RATING_COLUMN_ID, upr.getId());
        values.put(EspcSQLiteHelper.USER_PROPERTY_RATING_COLUMN_UUID, upr.getUuid());
        values.put(EspcSQLiteHelper.USER_PROPERTY_RATING_COLUMN_USER_ID, upr.getUserID());
        values.put(EspcSQLiteHelper.USER_PROPERTY_RATING_COLUMN_PROPERTY_ID, upr.getPropertyID());
        values.put(EspcSQLiteHelper.USER_PROPERTY_RATING_COLUMN_OVERALL_RATING, upr.getOverallRating());

        if (!ifExistsLocally(upr)) {
            // insert the values and store the id returned by the operation
            long insertId = mDatabase.insert(EspcSQLiteHelper.USER_PROPERTY_RATING_TABLE_NAME, null, values);
            // log the inserting item for debugging purposes
            Log.d("MainActivity", "ADDING UserPropertyRating to db");
            Log.d(TAG, "=============================================");
            Log.d(TAG, "id: " + upr.getId());
            Log.d(TAG, "uuid: " + upr.getUuid());
            Log.d(TAG, "userID: " + upr.getUserID());
            Log.d(TAG, "propertyID: " + upr.getPropertyID());
            Log.d(TAG, "overallRating: " + upr.getOverallRating());
            Log.d(TAG, "=============================================");

        } else {
            Log.d(TAG, "UserPropertyRating Already Exists in the DB");
        }

        return userPropertyRating;
    }


    /**
     * Method to edit a task in the database
     *
     * @param item object that contains the new data
     */
    public void updatePropertyItem(Property item) {
        Log.d(TAG, "Updating property with id: " + item.getId());
        long id = item.getId();
        ContentValues values = new ContentValues();
        values.put(EspcSQLiteHelper.PROPERTY_COLUMN_ID, item.getId());
        values.put(EspcSQLiteHelper.PROPERTY_COLUMN_UUID, item.getUuid());
        values.put(EspcSQLiteHelper.PROPERTY_COLUMN_ADDRESS, item.getAddress());
        values.put(EspcSQLiteHelper.PROPERTY_COLUMN_PRICE, item.getPrice());
        values.put(EspcSQLiteHelper.PROPERTY_COLUMN_USER_ID, item.getUserID());
        values.put(EspcSQLiteHelper.PROPERTY_COLUMN_LAST_UPDATED, item.getLastUpdated());
        mDatabase.update(
                EspcSQLiteHelper.PROPERTY_TABLE_NAME,
                values,
                "id=?",
                new String[]{"" + id}
        );
    }

    public void updateUserPropertyRatingItem(UserPropertyRating upr) {
        Log.d(TAG, "Updating UPR with id: " + upr.getId());
        long id = upr.getId();
        ContentValues values = new ContentValues();
        values.put(EspcSQLiteHelper.USER_PROPERTY_RATING_COLUMN_ID, upr.getId());
        values.put(EspcSQLiteHelper.USER_PROPERTY_RATING_COLUMN_UUID, upr.getUuid());
        values.put(EspcSQLiteHelper.USER_PROPERTY_RATING_COLUMN_USER_ID, upr.getUserID());
        values.put(EspcSQLiteHelper.USER_PROPERTY_RATING_COLUMN_PROPERTY_ID, upr.getPropertyID());
        values.put(EspcSQLiteHelper.USER_PROPERTY_RATING_COLUMN_OVERALL_RATING, upr.getOverallRating());

        mDatabase.update(
                EspcSQLiteHelper.USER_PROPERTY_RATING_TABLE_NAME,
                values,
                "id=?",
                new String[]{"" + id}
        );
    }

    public Property getPropertyItemById(int id) {
//        Cursor cursor = mDatabase.query(EspcSQLiteHelper.PROPERTY_TABLE_NAME, allPropertyColumns, EspcSQLiteHelper.PROPERTY_COLUMN_ID + " =? ",
//                new String[]{id + ""}, null, null, null, null);
        String q = "SELECT * FROM "+EspcSQLiteHelper.PROPERTY_TABLE_NAME+" WHERE _id = " + id  ;
        Cursor cursor = mDatabase.rawQuery(q, null);
        if (cursor != null)
            cursor.moveToFirst();

        return generatePropertyObjectFromCursor(cursor);
    }

    /***
     * Method to check if an entry already exists in the db
     *
     * @param remoteProperty
     * @return
     */
    public boolean ifExistsLocally(Property remoteProperty) {
        ArrayList<Property> properties = getAllPropertyItems();
        for (Property p : properties) {
            if (p.getId() == remoteProperty.getId()) {
                return true;
            }
        }
        return false;
    }

    public boolean ifExistsLocally(UserPropertyRating upr) {
        ArrayList<UserPropertyRating> userPropertyRatings = getAllUserPropertyRatingItems();
        for (UserPropertyRating ur : userPropertyRatings) {
            if (ur.getId() == upr.getId()) {
                return true;
            }
        }
        return false;
    }


    /**
     * Method that deletes a record by id in the Property table
     *
     * @param item the task to be deleted
     */
    public void deletePropertyItem(Property item) {
        long id = item.getId();
        mDatabase.delete(EspcSQLiteHelper.PROPERTY_TABLE_NAME, EspcSQLiteHelper.PROPERTY_COLUMN_ID
                + " = " + id, null);
    }

    /***
     * Method that deletes a record by id in the UserPropertyRating table
     *
     * @param userPropertyRating
     */
    public void deleteUserPropertyRatingItem(UserPropertyRating userPropertyRating) {
        long id = userPropertyRating.getId();
        mDatabase.delete(EspcSQLiteHelper.USER_PROPERTY_RATING_TABLE_NAME, EspcSQLiteHelper.USER_PROPERTY_RATING_COLUMN_ID
                + " = " + id, null);
    }

    /**
     * Method that retrieve all database entries
     *
     * @return list of all db entries
     */
    public ArrayList<Property> getAllPropertyItems() {
        Log.d(TAG, "Getting all properties from the database...");
        //temporary list of RssItems
        ArrayList<Property> rssItems = new ArrayList<>();
        //cursor object to store the result of the query
        Cursor cursor = mDatabase.query(EspcSQLiteHelper.PROPERTY_TABLE_NAME, allPropertyColumns, null,
                null, null, null, null);
        //iterate through the cursor
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                rssItems.add(generatePropertyObjectFromCursor(cursor));
                cursor.moveToNext();
            }
            //close the cursor object
            cursor.close();
        }
        //return the list of rssItems
        return rssItems;
    }

    public ArrayList<UserPropertyRating> getAllUserPropertyRatingItems() {
        Log.d(TAG, "Getting all user property ratings from the database...");
        //temporary list of RssItems
        ArrayList<UserPropertyRating> userPropertyRatingsItems = new ArrayList<>();
        //cursor object to store the result of the query
        Cursor cursor = mDatabase.query(EspcSQLiteHelper.USER_PROPERTY_RATING_TABLE_NAME, allUserPropertyRatingColumns, null,
                null, null, null, null);
        //iterate through the cursor
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                userPropertyRatingsItems.add(generateUPRObjectFromCursor(cursor));
                cursor.moveToNext();
            }
            //close the cursor object
            cursor.close();
        }
        //return the list of rssItems
        return userPropertyRatingsItems;
    }

    public ArrayList<UserPropertyRating> getAllPropertyRatingsAssociatedWithPropertyId(int propertyId){
        ArrayList<UserPropertyRating> userPropertyRatingsWithId = new ArrayList<>();

        for(UserPropertyRating ups: getAllUserPropertyRatingItems()){
            if(ups.getPropertyID() == propertyId){
                userPropertyRatingsWithId.add(ups);
            }
        }

        return userPropertyRatingsWithId;
    }


    public ArrayList<Property> read(String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        //temporary list of RssItems
        ArrayList<Property> items = new ArrayList<Property>();
        //cursor object to store the result of the query
        Cursor cursor = mDatabase.query(EspcSQLiteHelper.PROPERTY_TABLE_NAME, allPropertyColumns, selection, selectionArgs, groupBy, having, orderBy);
        //iterate through the cursor
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            //temp object of type RssItem to hold the current cursor object
            Property item = cursorToPropertyItem(cursor);
            //add it to the list of objects
            items.add(item);
            //move the cursor to the next object
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        //return the list of items
        return items;

    }

    /**
     * Method that deletes/clears the database
     */
    public void deleteAll() {
        mDatabase.delete(EspcSQLiteHelper.PROPERTY_TABLE_NAME, null, null);
        Log.d(TAG, "Deleting all Property entries...");
    }

    /**
     * Helper method that creates a RssItem object from a cursor object
     *
     * @param cursor reference to the cursor
     * @return converted RssItem
     */
    private Property cursorToPropertyItem(Cursor cursor) {
        //temporary RssItem object..
        Property item = new Property();
        //set up the id using the cursor
        item.setId(cursor.getInt(0));
        //set up the uuid using the cursor
        item.setUuid(cursor.getString(1));
        //set up the description
        item.setPrice(cursor.getString(2));
        //set up the title using
        item.setAddress(cursor.getString(3));
        //set up the publication date
        item.setUserID(cursor.getInt(4));
        // set up the last updated date
        item.setLastUpdated(cursor.getString(5));
        //return the item
        return item;
    }

    /**
     * Helper method that creates a RssItem object from Cursor object
     *
     * @param cursor reference to the cursor
     * @return the RssItem object
     */
    public Property generatePropertyObjectFromCursor(Cursor cursor) {
        //check if the cursor is null
        if (cursor == null) {
            //... return
            return null;
        }
        //temporary RssItem object
        Property propertyItem = new Property();
        //set up the object using the cursor object...
        propertyItem.setId(cursor.getInt(cursor.getColumnIndex(EspcSQLiteHelper.PROPERTY_COLUMN_ID)));
        propertyItem.setUuid(cursor.getString(cursor.getColumnIndex(EspcSQLiteHelper.PROPERTY_COLUMN_UUID)));
        propertyItem.setUserID(cursor.getInt(cursor.getColumnIndex(EspcSQLiteHelper.PROPERTY_COLUMN_USER_ID)));
        propertyItem.setAddress(cursor.getString(cursor.getColumnIndex(EspcSQLiteHelper.PROPERTY_COLUMN_ADDRESS)));
        propertyItem.setPrice(cursor.getString(cursor.getColumnIndex(EspcSQLiteHelper.PROPERTY_COLUMN_PRICE)));
        propertyItem.setLastUpdated(cursor.getString(cursor.getColumnIndex(EspcSQLiteHelper.PROPERTY_COLUMN_LAST_UPDATED)));
        //return the object
        return propertyItem;
    }

    public UserPropertyRating generateUPRObjectFromCursor(Cursor cursor) {
        //check if the cursor is null
        if (cursor == null) {
            //... return
            return null;
        }
        //temporary UserPropertyRating object
        UserPropertyRating upr = new UserPropertyRating();
        //set up the object using the cursor object...
        upr.setId(cursor.getInt(cursor.getColumnIndex(EspcSQLiteHelper.USER_PROPERTY_RATING_COLUMN_ID)));
        upr.setUuid(cursor.getString(cursor.getColumnIndex(EspcSQLiteHelper.USER_PROPERTY_RATING_COLUMN_UUID)));
        upr.setUserID(cursor.getInt(cursor.getColumnIndex(EspcSQLiteHelper.USER_PROPERTY_RATING_COLUMN_USER_ID)));
        upr.setPropertyID(cursor.getInt(cursor.getColumnIndex(EspcSQLiteHelper.USER_PROPERTY_RATING_COLUMN_PROPERTY_ID)));
        upr.setOverallRating(cursor.getInt(cursor.getColumnIndex(EspcSQLiteHelper.USER_PROPERTY_RATING_COLUMN_OVERALL_RATING)));
        //return the object
        return upr;
    }

}