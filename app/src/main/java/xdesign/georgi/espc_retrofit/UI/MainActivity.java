package xdesign.georgi.espc_retrofit.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xdesign.georgi.espc_retrofit.Adapters.PropertyAdapter;
import xdesign.georgi.espc_retrofit.Backend.ESPCService;
import xdesign.georgi.espc_retrofit.R;
import xdesign.georgi.espc_retrofit.Utils.Constants;
import xdesign.georgi.espc_retrofit.Utils.DividerItemDecoration;
import xdesign.georgi.espc_retrofit.Backend.Property;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Callback<List<Property>>{
    private static final String TAG = MainActivity.class.getSimpleName();
    private ESPCService espcService;
    private ArrayList<Property> mProperties = new ArrayList<>();

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    private RecyclerView mRecyclerView;
    private PropertyAdapter mAdapter;
    private FloatingActionButton AddNewPropertyFAB;

    //    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

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

        Call<List<Property>> call = espcService.getAllProperties();

        call.enqueue(this);

        // set up AddNewPropertyFAB button
        AddNewPropertyFAB = (FloatingActionButton) findViewById(R.id.fab);
        if (AddNewPropertyFAB != null)
            AddNewPropertyFAB.setOnClickListener(this);

        mAdapter = new PropertyAdapter(mProperties);


        mRecyclerView = (RecyclerView) findViewById(R.id.listView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, null));
        mRecyclerView.setAdapter(mAdapter);




    }

    @Override
    public void onClick(View v) {
        // Add new property
    }

    @Override
    public void onResponse(Call<List<Property>> call, Response<List<Property>> response) {
        Log.e("MainActivity", "onResponse");
        for (Property p : response.body()) {
            mProperties.add(p);
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFailure(Call<List<Property>> call, Throwable t) {
        Log.e("MainActivity", "onFailure");
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


//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Log.d(TAG,"Selected Item: " + id);
//    }
}
