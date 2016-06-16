package xdesign.georgi.espc_retrofit.UI;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xdesign.georgi.espc_retrofit.Adapters.PropertyAdapter;
import xdesign.georgi.espc_retrofit.Adapters.RoomsAdapter;
import xdesign.georgi.espc_retrofit.Backend.ESPCService;
import xdesign.georgi.espc_retrofit.Backend.Property;
import xdesign.georgi.espc_retrofit.Backend.Room;
import xdesign.georgi.espc_retrofit.R;
import xdesign.georgi.espc_retrofit.Utils.Constants;
import xdesign.georgi.espc_retrofit.Utils.DividerItemDecoration;

public class PropertyDetailsActivity extends AppCompatActivity {
    private static final String TAG = PropertyDetailsActivity.class.getSimpleName();
    private static ESPCService espcService;
    private ArrayList<Room> mRooms = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private static RoomsAdapter mAdapter;
    private TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEmptyTextView = (TextView)findViewById(R.id.empty);
        espcService = ESPCService.retrofit.create(ESPCService.class);
        mAdapter = new RoomsAdapter(this, mRooms);

        mRecyclerView = (RecyclerView)findViewById(R.id.listView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, null));
        mRecyclerView.setAdapter(mAdapter);

        Bundle data = getIntent().getExtras();
        Property property = (Property) data.getSerializable(Constants.KEY_PROPERTY_OBJECT);
        getSupportActionBar().setTitle(property.getAddress());
        Log.d(TAG, property.toString());

        Call<List<Room>> propertyRooms = espcService.getAllRoomsAssociatedWithPropertyID(property.getId());

        propertyRooms.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                Log.d(TAG,"onResponse Property rooms: " + response.body().toString());
                mRooms.addAll(response.body());
                mAdapter.notifyDataSetChanged();

                if(mRooms.size() == 0){
                    mEmptyTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                Log.e(TAG,"Update Property failed" + t.toString());
            }
        });
    }

}
