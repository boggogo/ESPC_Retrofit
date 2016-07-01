package xdesign.georgi.espc_retrofit;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xdesign.georgi.espc_retrofit.Backend.ESPCService;
import xdesign.georgi.espc_retrofit.Backend.Property;
import xdesign.georgi.espc_retrofit.Backend.Sync;
import xdesign.georgi.espc_retrofit.Backend.UserPropertyRating;
import xdesign.georgi.espc_retrofit.Database.EspcItemDataSource;
import xdesign.georgi.espc_retrofit.UI.MainActivity;
import xdesign.georgi.espc_retrofit.Utils.Constants;

/**
 * Created by georgi on 20/06/16.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class EspcJobSheculerService extends JobService implements Callback<List<Sync>> {
    private static final String TAG = EspcJobSheculerService.class.getSimpleName();
    private EspcItemDataSource mPropertyItemDataSource;
    private static ESPCService espcService;
    private Call<List<Sync>> getAllSyncsCall;
    private LinkedList<Sync> syncQueue;
    private final String ACTION_CREATE = "create";
    private final String ACTION_DELETE = "delete";
    private final String ACTION_UPDATE = "update";
    private final String TABLE_PROPERTY = "Property";
    private final String TABLE_USER_PROPERTY_RATING = "UserPropertyRating";
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private Call<List<UserPropertyRating>> getUsPropRtByUUIDCall;

    @Override
    public void onCreate() {
        super.onCreate();
        espcService = ESPCService.retrofit.create(ESPCService.class);
        syncQueue = new LinkedList<Sync>();

        mPropertyItemDataSource = EspcItemDataSource.getInstance(getApplicationContext());
        mPropertyItemDataSource.open();

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        if (params.getJobId() == 1) {
            mJobHandler.sendMessage(Message.obtain(mJobHandler, 1, params));
            return true;
        }


        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mJobHandler.removeMessages(1);
        mPropertyItemDataSource.close();
        return false;
    }

    private Handler mJobHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            Toast.makeText(getApplicationContext(),
                    "JobService task running", Toast.LENGTH_SHORT)
                    .show();
            // 1466598611995L
            Long lastSyncTime = 0L;
            lastSyncTime = mPreferences.getLong(Constants.LAST_SYNC_TIME_KEY, lastSyncTime);
            Log.d(TAG, "Last  sync time is: " + lastSyncTime);

            Log.e(TAG, "Job Service task is running...");
            getAllSyncsCall = espcService.getAllSyncsAfterThisTimeStamp(lastSyncTime);
            // get all of the sync records that equal or are bigger than the local lastSyncTime...
            getAllSyncsCall.enqueue(EspcJobSheculerService.this);

            jobFinished((JobParameters) msg.obj, false);
            return true;
        }

    });


    @Override
    public void onResponse(Call<List<Sync>> call, Response<List<Sync>> response) {
        Log.d(TAG, "onResponse success:" + response.isSuccessful());
        if (response.isSuccessful()) {
            for (Sync c : response.body()) {
                syncQueue.add(c);
                Log.d(TAG, c.toString());
            }
        }
        Iterator<Sync> syncIterator = syncQueue.iterator();
        Log.d(TAG, "Iterating over linked list=============================== Size: " + response.body().size());

        while (syncIterator.hasNext()) {

            Sync c = syncIterator.next();
            // get the uuid of the current Sync item
            String uuid = c.getUuid();
            // Check which table that the change occur in
            switch (c.getTable()) {
                case TABLE_PROPERTY:
                    Log.d(TAG, "Table: Property");
// check what action to do in the local database...
                    switch (c.getAction()) {
                        case ACTION_CREATE:
                            Log.d(TAG, "action - create");
                            // create a new record in the local database but first we have to cherry pick the record from the remote api
                            // get the record from Property table with this uuid...
                            createOrUpdateLocally(uuid, ACTION_CREATE, TABLE_PROPERTY);
                            Call<List<Property>> getPropByUUIDCall;
                            break;

                        case ACTION_DELETE:
                            Log.d(TAG, "action - delete");
                            // loop through the local db and find the property with the uuid that needs to be deleted
                            deleteRecordLocally(uuid, TABLE_PROPERTY);
                            break;

                        case ACTION_UPDATE:
                            Log.d(TAG, "action - update");
                            // get the record from Property table with this uuid...
                            createOrUpdateLocally(uuid, ACTION_UPDATE, TABLE_PROPERTY);
                            break;
                    }
                    break;

                case TABLE_USER_PROPERTY_RATING:
                    Log.d(TAG, "Table: TABLE_USER_PROPERTY_RATING");
                    switch (c.getAction()) {
                        case ACTION_CREATE:
                            createOrUpdateLocally(uuid, ACTION_CREATE, TABLE_USER_PROPERTY_RATING);
                            break;

                        case ACTION_UPDATE:
                            Log.d(TAG, "ACTION_UPDATE");
                            createOrUpdateLocally(uuid, ACTION_UPDATE, TABLE_USER_PROPERTY_RATING);
                            break;

                        case ACTION_DELETE:
                            Log.d(TAG, "ACTION_DELETE");
                            deleteRecordLocally(uuid, TABLE_USER_PROPERTY_RATING);
                            break;

                    }

            }

            mEditor.putLong(Constants.LAST_SYNC_TIME_KEY, c.getTimeChanged()).apply();
        }
    }

    private void deleteRecordLocally(String uuid, String table) {
        switch (table) {
            case TABLE_PROPERTY:
                ArrayList<Property> localProperties = mPropertyItemDataSource.getAllPropertyItems();
                for (Property p : localProperties) {
                    if (p.getUuid().equals(uuid)) {
                        // delete it
                        Log.d(TAG, "Deleting property: " + p.toString());
                        mPropertyItemDataSource.deletePropertyItem(p);
                        // refresh the screen with the latest data from the local db...
                        MainActivity.getDataFromTheLocalDB();
                    }
                }
                break;
            case TABLE_USER_PROPERTY_RATING:
                ArrayList<UserPropertyRating> userPropertyRatings = mPropertyItemDataSource.getAllUserPropertyRatingItems();
                for (UserPropertyRating ur : userPropertyRatings) {
                    if (ur.getUuid().equals(uuid)) {
                        mPropertyItemDataSource.deleteUserPropertyRatingItem(ur);
                        Log.d(TAG, "Deleting UserPropertyRating: " + ur.toString());
                    }
                }
                break;
        }
    }

    private void createOrUpdateLocally(String uuid, final String action, String table) {
        switch (table) {
            case TABLE_PROPERTY:
                Call<List<Property>> getPropByUUIDCall = espcService.getPropertyByUUID(uuid);
                getPropByUUIDCall.enqueue(new Callback<List<Property>>() {
                    @Override
                    public void onResponse(Call<List<Property>> call, Response<List<Property>> response) {
                        Log.d(TAG, "onResponse success:" + response.isSuccessful() + " getting property by uuid");
                        if (response.isSuccessful()) {
                            for (Property p : response.body()) {
                                Log.d(TAG, p.toString());
                                if (action.equals(ACTION_CREATE)) {
                                    // create new property locally as well.
                                    mPropertyItemDataSource.createPropertyItem(p);
                                }
                                if (action.equals(ACTION_UPDATE)) {
                                    // update local property
                                    mPropertyItemDataSource.updatePropertyItem(p);
                                }
                                // refresh the screen with the latest data from the local db...
                                MainActivity.getDataFromTheLocalDB();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Property>> call, Throwable t) {
                        Log.e(TAG, "onFailure error getting property by uuid" + t.toString());
                    }
                });
                break;
            case TABLE_USER_PROPERTY_RATING:
                getUsPropRtByUUIDCall = espcService.getUserPropertyRatingByUUID(uuid);
                getUsPropRtByUUIDCall.enqueue(new Callback<List<UserPropertyRating>>() {
                    @Override
                    public void onResponse(Call<List<UserPropertyRating>> call, Response<List<UserPropertyRating>> response) {
                        Log.d(TAG, "onResponse success:" + response.isSuccessful() + " getting user property rating by uuid");
                        if (response.isSuccessful()) {
                            for (UserPropertyRating upr : response.body()) {
                                if (action.equals(ACTION_CREATE)) {
                                    mPropertyItemDataSource.createUserPropertyRatingItem(upr);
                                }
                                if (action.equals(ACTION_UPDATE)) {
                                    mPropertyItemDataSource.updateUserPropertyRatingItem(upr);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<UserPropertyRating>> call, Throwable t) {
                        Log.e(TAG, "onFailure getting user property rating by uuid");
                    }
                });
                break;
        }
    }
    @Override
    public void onFailure(Call<List<Sync>> call, Throwable t) {
        Log.e(TAG, "onFailure error" + t.toString());
    }
}
