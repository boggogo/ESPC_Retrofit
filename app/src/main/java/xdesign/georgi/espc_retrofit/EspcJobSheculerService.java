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
        Log.d(TAG, "Iterating over linked list===============================");

        while (syncIterator.hasNext()) {

            Sync c = syncIterator.next();
            // get the uuid of the current Sync item
            String uuid = c.getUuid();
            // check if there is a DELETE action after CREATE action for this uuid...
            if (doesContainDeleteAfterCreateAction(uuid, syncQueue.iterator())) {
                Log.e(TAG, "No need to create this Action. There is a delete action statement with this uuid in the queue");
            } else {
                Log.e(TAG, "Need to create this Action. There is a NO delete action statement with this uuid in the queue");
                // Check which table that the change occur in
                if (c.getTable().equals(TABLE_PROPERTY)) {
                    Log.d(TAG, "Table: Property");
// check what action to do in the local database...
                    switch (c.getAction()) {
                        case ACTION_CREATE:
                            Log.d(TAG, "action - create");
                            // create a new record in the local database but first we have to cherry pick the record from the remote api
                            // get the record from Property table with this uuid...
                            getOrUpdate(uuid, ACTION_CREATE);
                            Call<List<Property>> getPropByUUIDCall;
                            break;

                        case ACTION_DELETE:
                            Log.d(TAG, "action - delete");
                            // loop through the local db and find the property with the uuid that needs to be deleted

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

                        case ACTION_UPDATE:
                            Log.d(TAG, "action - update");
                            // get the record from Property table with this uuid...
                            getOrUpdate(uuid, ACTION_UPDATE);
                            break;

                    }
                    if (c.getTable().equals(TABLE_USER_PROPERTY_RATING)) {
                        Log.d(TAG, "Table: TABLE_USER_PROPERTY_RATING");


                        switch (c.getAction()) {
                            case ACTION_CREATE:
                                Log.d(TAG, "ACTION_CREATE");
                                getUsPropRtByUUIDCall = espcService.getUserPropertyRatingByUUID(c.getUuid());
                                getUsPropRtByUUIDCall.enqueue(new Callback<List<UserPropertyRating>>() {
                                    @Override
                                    public void onResponse(Call<List<UserPropertyRating>> call, Response<List<UserPropertyRating>> response) {
                                        Log.d(TAG, "onResponse success:" + response.isSuccessful() + " getting user property rating by uuid");
                                        if (response.isSuccessful()) {
                                            for (UserPropertyRating upr : response.body()) {
                                                mPropertyItemDataSource.createUserPropertyRatingItem(upr);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<UserPropertyRating>> call, Throwable t) {
                                        Log.e(TAG, "onFailure getting user property rating by uuid");
                                    }
                                });
                                break;

                            case ACTION_UPDATE:
                                Log.d(TAG, "ACTION_UPDATE");
                                getUsPropRtByUUIDCall = espcService.getUserPropertyRatingByUUID(c.getUuid());
                                getUsPropRtByUUIDCall.enqueue(new Callback<List<UserPropertyRating>>() {
                                    @Override
                                    public void onResponse(Call<List<UserPropertyRating>> call, Response<List<UserPropertyRating>> response) {
                                        Log.d(TAG, "onResponse success:" + response.isSuccessful() + " getting user property rating by uuid");
                                        if (response.isSuccessful()) {
                                            for (UserPropertyRating upr : response.body()) {
                                                mPropertyItemDataSource.updateUserPropertyRatingItem(upr);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<List<UserPropertyRating>> call, Throwable t) {
                                        Log.e(TAG, "onFailure getting user property rating by uuid");
                                    }
                                });
                                break;

                            case ACTION_DELETE:
                                Log.d(TAG, "ACTION_DELETE");
                                ArrayList<UserPropertyRating> userPropertyRatings = mPropertyItemDataSource.getAllUserPropertyRatingItems();
                                for (UserPropertyRating ur : userPropertyRatings) {
                                    if (ur.getUuid().equals(c.getUuid())) {
                                        mPropertyItemDataSource.deleteUserPropertyRatingItem(ur);
                                        Log.d(TAG, "Deleting UserPropertyRating: " + ur.toString());
                                    }
                                }
                                break;

                        }



                    }
//        Log.d(TAG,syncQueue.peek().toString());


                }
            }

            mEditor.putLong(Constants.LAST_SYNC_TIME_KEY, c.getTimeChanged()).apply();
        }
    }

    private void getOrUpdate(String uuid, final String action) {
        Call<List<Property>> getPropByUUIDCall = espcService.getPropertyByUUID(uuid);
        getPropByUUIDCall.enqueue(new Callback<List<Property>>() {
            @Override
            public void onResponse(Call<List<Property>> call, Response<List<Property>> response) {
                Log.d(TAG, "onResponse success:" + response.isSuccessful() + " getting property by uuid");
                if (response.isSuccessful()) {
                    for (Property p : response.body()) {
                        Log.d(TAG, p.toString());
                        if(action.equals(ACTION_CREATE)) {
                            // create new property locally as well.
                            mPropertyItemDataSource.createPropertyItem(p);
                        }

                        if(action.equals(ACTION_UPDATE)){
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
    }


    @Override
    public void onFailure(Call<List<Sync>> call, Throwable t) {
        Log.e(TAG, "onFailure error" + t.toString());
    }


    private boolean doesContainDeleteAfterCreateAction(String currentSyncUuid, Iterator<Sync> iterator) {
        int count = 0;
        boolean hasDeleteAfterCreate;
        while (iterator.hasNext()) {
            Sync c = iterator.next();
            if (c.getAction().equals(ACTION_CREATE) && currentSyncUuid.equals(c.getUuid())) {
                count++;
            }

            if (c.getAction().equals(ACTION_DELETE) && currentSyncUuid.equals(c.getUuid())) {
                count *= -1;
            }
        }

        if (count == -1) {
            hasDeleteAfterCreate = true;
        } else {
            hasDeleteAfterCreate = false;
        }

        return hasDeleteAfterCreate;
    }
}
