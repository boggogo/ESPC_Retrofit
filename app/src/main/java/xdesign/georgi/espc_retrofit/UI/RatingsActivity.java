package xdesign.georgi.espc_retrofit.UI;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xdesign.georgi.espc_retrofit.Adapters.UserPropertyRatingsAdapter;
import xdesign.georgi.espc_retrofit.Backend.ESPCService;
import xdesign.georgi.espc_retrofit.Backend.UserPropertyRating;
import xdesign.georgi.espc_retrofit.Backend.User_ESPC;
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();
        espcService = ESPCService.retrofit.create(ESPCService.class);
        userId = mPreferences.getInt(Constants.USER_ID_KEY,userId);

        // retrieve and store the selected property's id (needed for adding more ratings for THIS property)
        propertyId = getIntent().getExtras().getInt(Constants.KEY_PROPERTY_ID);
        Log.d(TAG, "Selected property id: " + propertyId);

        mAdapter = new UserPropertyRatingsAdapter(RatingsActivity.this, mPropertyRatings , mUsers);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mRefreshLayout.setOnRefreshListener(this);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, null));
        mRecyclerView.setAdapter(mAdapter);

        getDataFromBackend();



        setUpFabButton();



    }

    private void getDataFromBackend() {

        Call<List<UserPropertyRating>> userRatingsCall = espcService.getAllPropRatingsAssociatedWithPropId(propertyId);
        userRatingsCall.enqueue(this);
    }

    private Callback<List<User_ESPC>> getPropertyRatingCallback = new Callback<List<User_ESPC>>() {
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
    public void onResponse(Call<List<UserPropertyRating>> call, Response<List<UserPropertyRating>> response){
        Log.d(TAG,"onResponse");
        if(response.isSuccessful()) {
            Log.d(TAG,"onResponse user ratings list size: " + response.body().size());
           // for (UserPropertyRating ur : response.body()) {
                mPropertyRatings.clear();
                mPropertyRatings.addAll(response.body());
                //Log.d(TAG, "Rating: " + ur.toString());
           // }
        }
        espcService.getAllUsers().enqueue(getPropertyRatingCallback);

//        mRefreshLayout.setRefreshing(false);
//        mAdapter.notifyDataSetChanged();
    }

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
        getDataFromBackend();
    }

    public void onPositiveAddPropertyRating(int selectedValue) {
        final UserPropertyRating newUserPropertyRating = new UserPropertyRating();
        newUserPropertyRating.setOverallRating(selectedValue);
        newUserPropertyRating.setUserID(userId);
        newUserPropertyRating.setPropertyID(propertyId);

        Call<UserPropertyRating> call = espcService.addNewRating(newUserPropertyRating);
        call.enqueue(new Callback<UserPropertyRating>() {
            @Override
            public void onResponse(Call<UserPropertyRating> call, Response<UserPropertyRating> response) {
                Log.d(TAG,"onResponse add new :" + response.isSuccessful());
                // Show the user the status of the post request...
                if(response.isSuccessful()){
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
}
