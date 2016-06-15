package xdesign.georgi.espc_retrofit.UI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xdesign.georgi.espc_retrofit.Backend.ESPCService;
import xdesign.georgi.espc_retrofit.Backend.UserPropertyRating;
import xdesign.georgi.espc_retrofit.R;
import xdesign.georgi.espc_retrofit.Utils.Constants;

public class RatingsActivity extends AppCompatActivity implements Callback<List<UserPropertyRating>> {
    private static final String TAG = RatingsActivity.class.getSimpleName();
    private static ESPCService espcService;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private int userId = -1;
    private int propertyId = -1;


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

    }
}
