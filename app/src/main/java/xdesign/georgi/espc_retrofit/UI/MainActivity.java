package xdesign.georgi.espc_retrofit.UI;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xdesign.georgi.espc_retrofit.Adapters.PropertyAdapter;
import xdesign.georgi.espc_retrofit.Backend.ESPCService;
import xdesign.georgi.espc_retrofit.Backend.UserPropertyRating;
import xdesign.georgi.espc_retrofit.Database.EspcItemDataSource;
import xdesign.georgi.espc_retrofit.EspcJobSheculerService;
import xdesign.georgi.espc_retrofit.R;
import xdesign.georgi.espc_retrofit.UI.Dialogs.AddNewPropertyDialog;
import xdesign.georgi.espc_retrofit.Utils.Constants;
import xdesign.georgi.espc_retrofit.Utils.DividerItemDecoration;
import xdesign.georgi.espc_retrofit.Backend.Property;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        Callback<List<Property>>,
        SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static ESPCService espcService;
    public static ArrayList<Property> mProperties = new ArrayList<>();
    public static ArrayList<UserPropertyRating> mPropertyUserRatings = new ArrayList<>();
    private static int userId = -1;
    public static PropertyAdapter mAdapter;
    public static boolean isUpdatePending = false;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    //    UI References
    private RecyclerView mRecyclerView;
    private TextView mEmptyTextView;
    private FloatingActionButton addNewPropertyFAB;
    private static SwipeRefreshLayout mRefreshLayout;

    private static EspcItemDataSource mPropertyItemDataSource;

    private JobScheduler mJobScheduler;

    //    private TextView textView;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.e(TAG, "onCreate - MainActivity");

        mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);

        JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(getPackageName(), EspcJobSheculerService.class.getName()));

        // 10 seconds intervals
        builder.setPeriodic(15 * 1000);

        JobInfo ji = builder.build();
        mJobScheduler.schedule(ji);

        mEmptyTextView = (TextView) findViewById(R.id.empty);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRefreshLayout.setOnRefreshListener(this);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();
        setUpFabButton();
        mPropertyItemDataSource = new EspcItemDataSource(this);
        mPropertyItemDataSource.open();

//         Check if the user is logged in
        if (!mPreferences.getBoolean(Constants.IS_USER_LOGGED_IN, false)) {
            Log.d(TAG, "User is NOT logged in");
            // no logged user => transfer the user to the log in page
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            // user is logged in
            userId = mPreferences.getInt(Constants.USER_ID_KEY, -1);
            Log.d(TAG, "User with id: " + userId + " is logged in");
        }


        espcService = ESPCService.retrofit.create(ESPCService.class);

//        refetchPropertiesFromBackend();


        mAdapter = new PropertyAdapter(this, mProperties, mPropertyUserRatings);

        getDataFromTheLocalDB();

        mRecyclerView = (RecyclerView) findViewById(R.id.listView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, null));
        mRecyclerView.setAdapter(mAdapter);


    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "List size: " + mProperties.size());

        mAdapter.notifyDataSetChanged();
//        if(mPropertyItemDataSource != null){
//            mPropertyItemDataSource.open();
//        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPropertyItemDataSource.close();
        mJobScheduler.cancelAll();
    }

    public static void getDataFromTheLocalDB() {
        mProperties.clear();
        mProperties.addAll(mPropertyItemDataSource.getAllPropertyItems());
        mAdapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false);
    }


    private void refetchPropertiesFromBackend() {
        mProperties.clear();
        mPropertyUserRatings.clear();
        Call<List<Property>> call = espcService.getAllProperties();
        call.enqueue(this);
    }

    private void refetchThisUserPropertiesFromBackend() {
        mProperties.clear();
        mPropertyUserRatings.clear();
        espcService.getAllPropertiesAssociatedWithUserId(userId).enqueue(this);
    }

    @Override
    public void onClick(View v) {
        // Add new property
        AddNewPropertyDialog addNewFragment = AddNewPropertyDialog.newInstance("Add Property", "Create another property");
        addNewFragment.show(getFragmentManager(), getString(R.string.add_new_property_dialog_tag));
    }

    @Override
    public void onResponse(Call<List<Property>> call, Response<List<Property>> response) {
        Log.e(TAG, "onResponse get all properties. Success: " + response.isSuccessful());
        if (response.isSuccessful()) {
            for (Property p : response.body()) {
                // Store the property in the database...
                // TODO DO SYNC LOGIC HERE IN A worker thread
                mPropertyItemDataSource.createPropertyItem(p);
            }
            // read from the database and add the list to the adapter's list...
            mProperties.addAll(mPropertyItemDataSource.getAllPropertyItems());
            // notify the adapter for the change...
            mAdapter.notifyDataSetChanged();
        }
        // Get all of the userPropertyRatings now after that we got the lit of properties
        espcService.getAllUserPropertyRatings().enqueue(new Callback<List<UserPropertyRating>>() {
            @Override
            public void onResponse(Call<List<UserPropertyRating>> call, Response<List<UserPropertyRating>> response) {
                Log.e(TAG, "onResponse get all properties ratings. Success: " + response.isSuccessful());

                mPropertyUserRatings.addAll(response.body());
                mAdapter.notifyDataSetChanged();
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<UserPropertyRating>> call, Throwable t) {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onFailure(Call<List<Property>> call, Throwable t) {
        Log.e(TAG, "onFailure");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_log_out) {
            mEditor.putBoolean(Constants.IS_USER_LOGGED_IN, false).apply();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_show_my_properties) {
            refetchThisUserPropertiesFromBackend();
        }

        if (id == R.id.action_reset_db) {
            mPropertyItemDataSource.deleteAll();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
//        refetchPropertiesFromBackend();
        getDataFromTheLocalDB();
    }

    public void onPositiveAddNewProperty(String address, String price, final Context context) {
        Log.d(TAG, "onPositiveAddNewProperty: address: " + address + " Price: " + price);
        // set up the new property...
        final Property newProperty = new Property();
        newProperty.setAddress(address);
        newProperty.setPrice(price);
        newProperty.setLastUpdated(getTimeStamp());
        newProperty.setUserID(userId);
        // start refreshing...
        mRefreshLayout.setRefreshing(true);

//        mAdapter.notifyDataSetChanged();
//        // save the new property to the backend...
//        espcService.addNewProperty(newProperty).enqueue(new Callback<Property>() {
//            @Override
//            public void onResponse(Call<Property> call, Response<Property> response) {
//
//                if (response.isSuccessful() || response.body() != null) {
//                    // add the new property locally first...
        mProperties.add(newProperty);
        mPropertyItemDataSource.createPropertyItem(newProperty);
        mRefreshLayout.setRefreshing(false);
        isUpdatePending = true;
        // refetch data from the backend (to get the new ids and delete with work at this point)
//                    refetchPropertiesFromBackend();
//
//                    mRefreshLayout.setRefreshing(false);
//                    Log.e(TAG, "onResponse: Success: " + response.isSuccessful());
//                    Toast.makeText(context, "Successfully added new property", Toast.LENGTH_SHORT).show();
//
//
//                } else {
//                    showErrorToast(getString(R.string.error_add_new_property_toast_message));
//                }
//
        mAdapter.notifyDataSetChanged();
//                mRefreshLayout.setRefreshing(false);
//
////                if(mProperties.size() == 0){
////                    mEmptyTextView.setVisibility(View.VISIBLE);
////                }
//            }
//
//
//            @Override
//            public void onFailure(Call<Property> call, Throwable t) {
//                mRefreshLayout.setRefreshing(false);
//                Log.e(TAG, "onFailure: error: " + t.toString());
//                showErrorToast(getString(R.string.error_add_new_property_toast_message));
//            }
//        });

    }

    private String getTimeStamp() {
        Date now = new Date();
        Long nowTime = now.getTime();
        return nowTime.toString();
    }

    public void onPositiveDeletePropertyById(final Property property) {
        // Check if the user can delete this property - e.g. the user addded it
        if (property.getUserID() == userId) {
            Call<HashMap<String, Integer>> call = espcService.deletePropertyById(property.getId());
            call.enqueue(new Callback<HashMap<String, Integer>>() {
                @Override
                public void onResponse(Call<HashMap<String, Integer>> call, Response<HashMap<String, Integer>> response) {
                    Log.e(TAG, "onResponse: Delete property Success: " + response.isSuccessful());
                    // Check if the delete was successful
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Delete property HashMap size: " + response.body().size());
                        // Upon successful deletion the body will be a HashMap(String, Integer) with size 1 - count:value
                        int count = 0;
                        HashMap<String, Integer> hm = response.body();
                        Iterator iterator = hm.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry pair = (Map.Entry) iterator.next();
                            count = (int) pair.getValue();
                            Log.d(TAG, "Iterator: " + pair.toString());
                            Log.d(TAG, "count:" + pair.getValue());
                        }
                        if (count == 1) {
                            // success = > delete the local item from the list
                            mProperties.remove(property);
                            mPropertyItemDataSource.deletePropertyItem(property);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            // deletion failed
                            showErrorToast(getString(R.string.toast_error_message_delete_property));
                        }

                    } else {
                        showErrorToast(getString(R.string.toast_error_message_delete_property));
                    }
                }

                @Override
                public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                    Log.e(TAG, "onFailure: Delete property " + t.toString());
                }
            });
        } else {
            // cant delete this property
            showErrorToast(getString(R.string.error_show_toast_cant_delete_prop));
        }
    }

    private void showErrorToast(String errorMessage) {
        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
    }

    private void setUpFabButton() {
        // set up addNewPropertyFAB button
        addNewPropertyFAB = (FloatingActionButton) findViewById(R.id.fab);
        // Change the color of the fab icon to white...
        Drawable fabDrawable = addNewPropertyFAB.getDrawable();
        DrawableCompat.setTint(fabDrawable, Color.WHITE);
        // set up the onClickListener...
        if (addNewPropertyFAB != null)
            addNewPropertyFAB.setOnClickListener(this);
    }

    public void onPositiveUpdatePropertyDetails(final int propertyToBeUpdatedIndex, String newPropAddress, String newPropPrice) {

        Log.d(TAG, "Property to be updated id: " + mProperties.get(propertyToBeUpdatedIndex).toString());
        final Property oldProp = mProperties.get(propertyToBeUpdatedIndex);


        final Property newProp = new Property();
        newProp.setAddress(newPropAddress);
        newProp.setPrice(newPropPrice);
        newProp.setLastUpdated(getTimeStamp());
        newProp.setId(oldProp.getId());
        newProp.setUserID(userId);

        if (mProperties.get(propertyToBeUpdatedIndex).getUserID() == userId) {
            // get the property id that will be updated in the backend

            mProperties.set(propertyToBeUpdatedIndex, newProp);
            mPropertyItemDataSource.updatePropertyItem(newProp);
            mAdapter.notifyDataSetChanged();

        } else {
            showErrorToast(getString(R.string.error_show_toast_cant_update_prop));
        }
    }


}
