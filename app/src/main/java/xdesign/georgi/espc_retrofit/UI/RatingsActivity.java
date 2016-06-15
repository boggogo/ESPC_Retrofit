package xdesign.georgi.espc_retrofit.UI;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xdesign.georgi.espc_retrofit.Backend.ESPCService;
import xdesign.georgi.espc_retrofit.Backend.UserPropertyRating;
import xdesign.georgi.espc_retrofit.R;
import xdesign.georgi.espc_retrofit.Utils.Constants;

public class RatingsActivity extends AppCompatActivity implements Callback<List<UserPropertyRating>>, View.OnClickListener {
    private static final String TAG = RatingsActivity.class.getSimpleName();
    private static ESPCService espcService;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private int userId = -1;
    private int propertyId = -1;


    // UI references
    private FloatingActionButton addNewPropertyFAB;


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

        Call<List<UserPropertyRating>> userRatingsCall = espcService.getAllPropRatingsAssociatedWithUserId(userId);
        userRatingsCall.enqueue(this);



        setUpFabButton();



    }

    @Override
    public void onResponse(Call<List<UserPropertyRating>> call, Response<List<UserPropertyRating>> response){
        Log.d(TAG,"onResponse");
        if(response.isSuccessful()) {
            Log.d(TAG,"onResponse user ratings list size: " + response.body().size());
            for (UserPropertyRating ur : response.body()) {

                Log.d(TAG, "Rating: " + ur.toString());
            }
        }
    }

    @Override
    public void onFailure(Call<List<UserPropertyRating>> call, Throwable t) {
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
        // Add new rating for the selected property
        UserPropertyRating userPropertyRating = new UserPropertyRating();
        userPropertyRating.setUserID(userId);
        userPropertyRating.setPropertyID(propertyId);
        userPropertyRating.setOverallRating(100);

        Call<UserPropertyRating> call = espcService.addNewRating(userPropertyRating);
        call.enqueue(new Callback<UserPropertyRating>() {
            @Override
            public void onResponse(Call<UserPropertyRating> call, Response<UserPropertyRating> response) {
                Log.d(TAG,"onResponse add new :" + response.isSuccessful());
                // Show the user the status of the post request...
                if(response.isSuccessful()){
                    showToast("Success!");
                }else{
                    showToast("Failed!");
                }
            }

            @Override
            public void onFailure(Call<UserPropertyRating> call, Throwable t) {
                Log.e(TAG, "Add new user property ratings error: " + t.toString());
            }
        });
    }


    private void showToast(String toastMessage) {
        Toast.makeText(RatingsActivity.this, toastMessage, Toast.LENGTH_LONG).show();
    }
}
