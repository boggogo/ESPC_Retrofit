package xdesign.georgi.espc_retrofit.UI;

import android.content.Context;
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xdesign.georgi.espc_retrofit.Adapters.PropertyAdapter;
import xdesign.georgi.espc_retrofit.Backend.ESPCService;
import xdesign.georgi.espc_retrofit.R;
import xdesign.georgi.espc_retrofit.UI.Dialogs.AddNewPropertyDialogFragment;
import xdesign.georgi.espc_retrofit.Utils.Constants;
import xdesign.georgi.espc_retrofit.Utils.DividerItemDecoration;
import xdesign.georgi.espc_retrofit.Backend.Property;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Callback<List<Property>>, SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    private static ESPCService espcService;
    private static ArrayList<Property> mProperties = new ArrayList<>();

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
//    UI References
    private RecyclerView mRecyclerView;
    private static PropertyAdapter mAdapter;
    private FloatingActionButton addNewPropertyFAB;
    private static SwipeRefreshLayout mRefreshLayout;

    //    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.e(TAG,"onCreate - MainActivity");


        mRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        mRefreshLayout.setOnRefreshListener(this);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

//         Check if the user is logged in
        if(!mPreferences.getBoolean(Constants.IS_USER_LOGGED_IN,false)){
            Log.d(TAG,"User is NOT logged in");
            // no logged user => transfer the user to the log in page
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else {
            // user is logged in
            Log.d(TAG,"User is logged in");
        }


        espcService = ESPCService.retrofit.create(ESPCService.class);

        refetchDataFromBackend();

        setUpFabButton();


        mAdapter = new PropertyAdapter(this,mProperties);


        mRecyclerView = (RecyclerView) findViewById(R.id.listView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, null));
        mRecyclerView.setAdapter(mAdapter);




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

    private void refetchDataFromBackend() {
        Call<List<Property>> call = espcService.getAllProperties();
        call.enqueue(this);
    }

    @Override
    public void onClick(View v) {
        // Add new property
        AddNewPropertyDialogFragment addNewFragment = AddNewPropertyDialogFragment.newInstance("Add Property","Create another property");
        addNewFragment.show(getFragmentManager(),getString(R.string.add_new_property_dialog_tag));
    }

    @Override
    public void onResponse(Call<List<Property>> call, Response<List<Property>> response) {
        Log.e(TAG, "onResponse");
        mProperties.addAll(response.body());
        mAdapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false);
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
            mEditor.putBoolean(Constants.IS_USER_LOGGED_IN,false).apply();

            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        mProperties.clear();
        refetchDataFromBackend();
    }

    public static void onPositiveAddNewProperty(String address, String price, final Context context) {
        Log.d(TAG,"onPositiveAddNewProperty: address: " + address + " Price: "+price);
        // set up the new property...
        Property newProperty = new Property();
        newProperty.setAddress(address);
        newProperty.setPrice(price);
        // start refreshing...
        mRefreshLayout.setRefreshing(true);
        // add the new property locally first...
        mProperties.add(newProperty);
        mAdapter.notifyDataSetChanged();
        // save the new property to the backend...
        espcService.addNewProperty(newProperty).enqueue(new Callback<Property>() {
            @Override
            public void onResponse(Call<Property> call, Response<Property> response) {
                mRefreshLayout.setRefreshing(false);
                Log.e(TAG, "onResponse: Success: " + response.isSuccessful());
                Toast.makeText(context,"Successfully added new property", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Property> call, Throwable t) {
                mRefreshLayout.setRefreshing(false);
                Log.e(TAG, "onFailure: error: " + t.toString());
            }
        });

    }


//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Log.d(TAG,"Selected Item: " + id);
//    }
}
