package xdesign.georgi.espc_retrofit.UI;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xdesign.georgi.espc_retrofit.Adapters.UserPropertyRatingsAdapter;
import xdesign.georgi.espc_retrofit.Backend.ESPCService;
import xdesign.georgi.espc_retrofit.Backend.Sync;
import xdesign.georgi.espc_retrofit.Backend.UserPropertyRating;
import xdesign.georgi.espc_retrofit.Backend.User_ESPC;
import xdesign.georgi.espc_retrofit.Database.EspcItemDataSource;
import xdesign.georgi.espc_retrofit.R;
import xdesign.georgi.espc_retrofit.UI.Dialogs.AddNewPropertyRatingDialog;
import xdesign.georgi.espc_retrofit.Utils.Constants;
import xdesign.georgi.espc_retrofit.Utils.DividerItemDecoration;

public class RatingsActivity extends AppCompatActivity implements Callback<List<UserPropertyRating>>, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG = RatingsActivity.class.getSimpleName();
    private static ESPCService espcService;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private int userId = -1;
    private int propertyId = -1;
    private ArrayList<UserPropertyRating> mPropertyRatings = new ArrayList<>();
    private ArrayList<User_ESPC> mUsers = new ArrayList<>();
    private UserPropertyRatingsAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private TextView mEmptyTextView;
    private EspcItemDataSource mEpcItemDataSource;



    // UI references
    private FloatingActionButton addNewPropertyFAB;
    private SwipeRefreshLayout mRefreshLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEpcItemDataSource = EspcItemDataSource.getInstance(getApplicationContext());
        mEpcItemDataSource.open();

        mEmptyTextView = (TextView)findViewById(R.id.empty);
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();
        espcService = ESPCService.retrofit.create(ESPCService.class);
        userId = mPreferences.getInt(Constants.USER_ID_KEY,userId);

        Bundle data = getIntent().getExtras();

        // retrieve and store the selected property's id (needed for adding more ratings for THIS property)
        propertyId = data.getInt(Constants.KEY_PROPERTY_ID);
        Log.d(TAG, "Selected property id: " + propertyId);
        getSupportActionBar().setTitle(data.getString(Constants.KEY_PROPERTY_NAME));

        mAdapter = new UserPropertyRatingsAdapter(RatingsActivity.this, mPropertyRatings , mUsers);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, null));
        mRecyclerView.setAdapter(mAdapter);

//        getDataFromBackend();
        getDataFromLocalDB();


        setUpFabButton();



    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"OnResume called");
//        getDataFromBackend();
        getDataFromLocalDB();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mEpcItemDataSource != null)
        mEpcItemDataSource.close();
    }

    private void getDataFromBackend() {

        Call<List<UserPropertyRating>> userRatingsCall = espcService.getAllPropRatingsAssociatedWithPropId(propertyId);
        userRatingsCall.enqueue(this);

    }

    private void getDataFromLocalDB(){
        mPropertyRatings.clear();
        mPropertyRatings.addAll(mEpcItemDataSource.getAllPropertyRatingsAssociatedWithPropertyId(propertyId));

        if(mPropertyRatings.size() == 0){
            mEmptyTextView.setVisibility(View.VISIBLE);
        }
        espcService.getAllUsers().enqueue(getPropertyRatingForTheSignedUserCallback);
    }



    @Override
    public void onResponse(Call<List<UserPropertyRating>> call, Response<List<UserPropertyRating>> response){
        Log.d(TAG,"onResponse");
        if(response.isSuccessful()) {
            Log.d(TAG,"onResponse user ratings list size: " + response.body().size());
           // for (UserPropertyRating ur : response.body()) {
                mPropertyRatings.clear();
                mPropertyRatings.addAll(response.body());
            Log.d(TAG,response.body().toString());
                //Log.d(TAG, "Rating: " + ur.toString());
           // }
        }
        Log.d(TAG,"List size: " + mPropertyRatings.size());
        if(mPropertyRatings.size() == 0){
            mEmptyTextView.setVisibility(View.VISIBLE);
        }
        espcService.getAllUsers().enqueue(getPropertyRatingForTheSignedUserCallback);

    }

    private Callback<List<User_ESPC>> getPropertyRatingForTheSignedUserCallback = new Callback<List<User_ESPC>>() {
        @Override
        public void onResponse(Call<List<User_ESPC>> call, Response<List<User_ESPC>> response) {
            Log.d(TAG,"onResponse getting all users...");

            if(response.isSuccessful()) {
                mUsers.clear();
                mUsers.addAll(response.body());
            }
            mRefreshLayout.setRefreshing(false);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(Call<List<User_ESPC>> call, Throwable t) {
            mRefreshLayout.setRefreshing(false);
            Log.e(TAG,"onFailure getting all users");
        }
    };

    @Override
    public void onFailure(Call<List<UserPropertyRating>> call, Throwable t) {
        mRefreshLayout.setRefreshing(false);
        Log.e(TAG, "Get all user property ratings error: " + t.toString());
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

    @Override
    public void onClick(View v) {

        AddNewPropertyRatingDialog dialog = AddNewPropertyRatingDialog.newInstance("Add New Rating","Select a rating from 1 to 5");
        dialog.show(getFragmentManager(),"add_new_prop_rating_dialog_tag");

    }


    private void showToast(String toastMessage) {
        Toast.makeText(RatingsActivity.this, toastMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRefresh() {
//        getDataFromBackend();
        getDataFromLocalDB();
    }

    public void onPositiveAddPropertyRating(int selectedValue) {
        final UserPropertyRating newUserPropertyRating = new UserPropertyRating();
        newUserPropertyRating.setUSERPROPERTYRATING_COLUMN_OVERALLRATING(selectedValue);
        newUserPropertyRating.setUSERPROPERTYRATING_COLUMN_USERID(userId);
        newUserPropertyRating.setUSERPROPERTYRATING_COLUMN_PROPERTYID(propertyId);

        Call<UserPropertyRating> call = espcService.addNewUserPropertyRating(newUserPropertyRating);
        call.enqueue(new Callback<UserPropertyRating>() {
            @Override
            public void onResponse(Call<UserPropertyRating> call, Response<UserPropertyRating> response) {
                Log.d(TAG,"onResponse add new :" + response.isSuccessful());
                // Show the user the status of the post request...
                if(response.isSuccessful() | response.body() != null){
                    mPropertyRatings.add(newUserPropertyRating);
                    showToast("Success!");
                }else{
                    showToast("Failed!" + response.errorBody());
                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<UserPropertyRating> call, Throwable t) {
                Log.e(TAG, "Add new user property ratings error: " + t.toString());
            }
        });
    }

    public void onPositiveUpdatePropRating(int selectedValue, final int propertyToBeUpdatedIndex) {
        Log.d(TAG,"Selected Value: " + selectedValue + " " + "id of property rating to be updated: " + mPropertyRatings.get(propertyToBeUpdatedIndex).getId());
        UserPropertyRating oldRating = mPropertyRatings.get(propertyToBeUpdatedIndex);

        final String uuid = UUID.randomUUID().toString();
        final long unixTimestamp = System.currentTimeMillis();

        // Set up new poroperty rating
        final UserPropertyRating newRating = new UserPropertyRating();
        newRating.setUSERPROPERTYRATING_COLUMN_PROPERTYID(oldRating.getUserpropertyrating_column_propertyid());
        newRating.setUSERPROPERTYRATING_COLUMN_UUID(uuid);
        newRating.setUSERPROPERTYRATING_COLUMN_USERID(oldRating.getUserpropertyrating_column_userid());
        newRating.setUSERPROPERTYRATING_COLUMN_OVERALLRATING(selectedValue);
        newRating.setId(oldRating.getId());

        Call<UserPropertyRating> userPropertyRatingCall = espcService.updateUserPropertyRatingbyId(mPropertyRatings.get(propertyToBeUpdatedIndex).getId(), newRating);
        userPropertyRatingCall.enqueue(new Callback<UserPropertyRating>() {
            @Override
            public void onResponse(Call<UserPropertyRating> call, Response<UserPropertyRating> response) {
                Log.d(TAG,"onResponse add new :" + response.isSuccessful());
                // Show the user the status of the post request...
                if(response.isSuccessful() | response.body() != null){
                    addSyncToRemote(propertyToBeUpdatedIndex, uuid, unixTimestamp, newRating);

                }else{
                    showToast("Failed!" + response.errorBody());
                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<UserPropertyRating> call, Throwable t) {
                Log.e(TAG, "Update new user property ratings error: " + t.toString());
            }
        });



    }

    private void addSyncToRemote(final int propertyToBeUpdatedIndex, String uuid, long unixTimestamp, final UserPropertyRating newRating) {
        Sync sync = new Sync();
        sync.setSYNC_COLUMN_UUID(uuid);
        sync.setSYNC_COLUMN_ACTION("update");
        sync.setSYNC_COLUMN_TABLE("UserPropertyRating");
        sync.setSYNC_COLUMN_TIMECHANGED(unixTimestamp);

        espcService.addNewSync(sync).enqueue(new Callback<Sync>() {
            @Override
            public void onResponse(Call<Sync> call, Response<Sync> response) {
                Log.d(TAG,"onResponse add new :" + response.isSuccessful());
                if(response.isSuccessful() && response.body() != null){
                    mPropertyRatings.set(propertyToBeUpdatedIndex,newRating);
                    mEpcItemDataSource.updateUserPropertyRatingItem(newRating);
                    showToast("Success!");
                }
            }

            @Override
            public void onFailure(Call<Sync> call, Throwable t) {
                Log.e(TAG, "onFailure error: " + t.toString());
            }
        });
    }

    public void onPositiveDeletePropertyRatingById(UserPropertyRating propertyRatingToDelete) {
        final UserPropertyRating ratingToDelete = propertyRatingToDelete;

        espcService.deleteUserPropertyRatingById(ratingToDelete.getId()).enqueue(new Callback<HashMap<String, Integer>>() {
            @Override
            public void onResponse(Call<HashMap<String, Integer>> call, Response<HashMap<String, Integer>> response) {
                if(response.isSuccessful()){
                    if(response.body().size() == 1){
                        // returns HashMap with size 1 upon successful deletion
                        mPropertyRatings.remove(ratingToDelete);
                        mAdapter.notifyDataSetChanged();
                        showToast("Success!");
                    }else{
                        return;
                    }
                }else{
                    showToast("Failed!" + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                showToast("Failed!" + t.toString());
            }
        });
    }
}
