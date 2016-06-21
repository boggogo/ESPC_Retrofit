package xdesign.georgi.espc_retrofit.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import xdesign.georgi.espc_retrofit.Backend.Property;

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
    // string array of all database columns
    private String[] allColumns = {
            EspcSQLiteHelper.COLUMN_ID,
            EspcSQLiteHelper.COLUMN_PRICE,
            EspcSQLiteHelper.COLUMN_ADDRESS,
            EspcSQLiteHelper.COLUMN_USER_ID,
            EspcSQLiteHelper.COLUMN_LAST_UPDATED
    };

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

        values.put(EspcSQLiteHelper.COLUMN_ID, item.getId());
        values.put(EspcSQLiteHelper.COLUMN_ADDRESS, item.getAddress());
        values.put(EspcSQLiteHelper.COLUMN_PRICE, item.getPrice());
        values.put(EspcSQLiteHelper.COLUMN_USER_ID, item.getUserID());
        values.put(EspcSQLiteHelper.COLUMN_LAST_UPDATED, item.getLastUpdated());

        if (!ifExistsLocally(item)) {
            // insert the values and store the id returned by the operation
            long insertId = mDatabase.insert(EspcSQLiteHelper.TABLE_NAME, null, values);
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

    /**
     * Method to edit a task in the database
     *
     * @param item object that contains the new data
     */
    public void updatePropertyItem(Property item) {
        long id = item.getId();
        ContentValues values = new ContentValues();
        values.put(EspcSQLiteHelper.COLUMN_ID, item.getId());
        values.put(EspcSQLiteHelper.COLUMN_ADDRESS, item.getAddress());
        values.put(EspcSQLiteHelper.COLUMN_PRICE, item.getPrice());
        values.put(EspcSQLiteHelper.COLUMN_USER_ID, item.getUserID());
        values.put(EspcSQLiteHelper.COLUMN_LAST_UPDATED, item.getLastUpdated());
        mDatabase.update(
                EspcSQLiteHelper.TABLE_NAME,
                values,
                "id=?",
                new String[]{"" + id}
        );
    }

    public Property getPropertyItemById(int id) {
        Cursor cursor = mDatabase.query(EspcSQLiteHelper.TABLE_NAME, allColumns, EspcSQLiteHelper.COLUMN_ID + " =? ",
                new String[]{id + ""}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        return generateObjectFromCursor(cursor);
    }

    /***
     * Method to check if an entry already exists in the db
     *
     * @param property
     * @return
     */
    public boolean ifExistsLocally(Property property) {
        ArrayList<Property> properties = getAllPropertyItems();
        for (Property p : properties) {
            if (p.getId() == property.getId()) {
                return true;
            }
        }
        return false;
    }

    public void retainAllLocalFromRemote(List<Property> remoteProperties) {
        ArrayList<Property> localProperties = getAllPropertyItems();


        Log.d(TAG, "Local db size: " + localProperties.size());
        Log.d(TAG, "Remote db size: " + remoteProperties.size());

        ArrayList<Property> differences = new ArrayList<>(localProperties);

        for (int i = 0; i < localProperties.size(); i++) {


            if (remoteProperties.contains(localProperties.get(i))) {
                Log.d(TAG, "remote contains local property with id: " + localProperties.get(i).getId());
            } else {
                Log.d(TAG, "remote DO NOT contains local property with id: " + localProperties.get(i).getId() + " so DELETE IT");
                // delete it
                deletePropertyItem(localProperties.get(i));
            }


        }

//        Log.d(TAG, "differences list size: " + differences.size());

//        Log.d(TAG, "differences list : " + differences.toString());


    }

    /**
     * Method that deletes an entry in the database
     *
     * @param item the task to be deleted
     */
    public void deletePropertyItem(Property item) {
        long id = item.getId();
        mDatabase.delete(EspcSQLiteHelper.TABLE_NAME, EspcSQLiteHelper.COLUMN_ID
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
        Cursor cursor = mDatabase.query(EspcSQLiteHelper.TABLE_NAME, allColumns, null,
                null, null, null, null);
        //iterate through the cursor
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                rssItems.add(generateObjectFromCursor(cursor));
                cursor.moveToNext();
            }
            //close the cursor object
            cursor.close();
        }
        //return the list of rssItems
        return rssItems;
    }


    public ArrayList<Property> read(String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        //temporary list of RssItems
        ArrayList<Property> items = new ArrayList<Property>();
        //cursor object to store the result of the query
        Cursor cursor = mDatabase.query(EspcSQLiteHelper.TABLE_NAME, allColumns, selection, selectionArgs, groupBy, having, orderBy);
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
        mDatabase.delete(EspcSQLiteHelper.TABLE_NAME, null, null);
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
        //set up the description
        item.setPrice(cursor.getString(1));
        //set up the title using
        item.setAddress(cursor.getString(2));
        //set up the publication date
        item.setUserID(cursor.getInt(3));
        // set up the last updated date
        item.setLastUpdated(cursor.getString(4));
        //return the item
        return item;
    }

    /**
     * Helper method that creates a RssItem object from Cursor object
     *
     * @param cursor reference to the cursor
     * @return the RssItem object
     */
    public Property generateObjectFromCursor(Cursor cursor) {
        //check if the cursor is null
        if (cursor == null) {
            //... return
            return null;
        }
        //temporary RssItem object
        Property propertyItem = new Property();
        //set up the object using the cursor object...
        propertyItem.setId(cursor.getInt(cursor.getColumnIndex(EspcSQLiteHelper.COLUMN_ID)));
        propertyItem.setUserID(cursor.getInt(cursor.getColumnIndex(EspcSQLiteHelper.COLUMN_USER_ID)));
        propertyItem.setAddress(cursor.getString(cursor.getColumnIndex(EspcSQLiteHelper.COLUMN_ADDRESS)));
        propertyItem.setPrice(cursor.getString(cursor.getColumnIndex(EspcSQLiteHelper.COLUMN_PRICE)));
        propertyItem.setLastUpdated(cursor.getString(cursor.getColumnIndex(EspcSQLiteHelper.COLUMN_LAST_UPDATED)));
        //return the object
        return propertyItem;
    }
}