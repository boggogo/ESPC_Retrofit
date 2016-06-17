package xdesign.georgi.espc_retrofit.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
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

public class PropertyDetailsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener{
    private static final String TAG = PropertyDetailsActivity.class.getSimpleName();
    private static ESPCService espcService;
    private ArrayList<Room> mRooms = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private static RoomsAdapter mAdapter;
    private TextView mEmptyTextView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);


        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mEmptyTextView = (TextView)findViewById(R.id.empty);
        espcService = ESPCService.retrofit.create(ESPCService.class);
        mAdapter = new RoomsAdapter(this, mRooms);

        mRecyclerView = (RecyclerView)findViewById(R.id.listView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, null));
        mRecyclerView.setAdapter(mAdapter);

        getAllPropertyRooms();
    }

    private void getAllPropertyRooms() {
        Bundle data = getIntent().getExtras();
        Property property = (Property) data.getSerializable(Constants.KEY_PROPERTY_OBJECT);
        getSupportActionBar().setTitle(property.getAddress());
        Log.d(TAG, property.toString());

        Call<List<Room>> propertyRooms = espcService.getAllRoomsAssociatedWithPropertyID(property.getId());
        propertyRooms.enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(Call<List<Room>> call, Response<List<Room>> response) {
                Log.d(TAG,"onResponse Property rooms: " + response.body().toString());
                if(response.isSuccessful()){
                    mRooms.clear();
                    mRooms.addAll(response.body());
                    mAdapter.notifyDataSetChanged();

                    if(mRooms.size() == 0){
                        mEmptyTextView.setVisibility(View.VISIBLE);
                    }
                }

                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Room>> call, Throwable t) {
                Log.e(TAG,"Update Property failed " + t.toString());
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void onPositiveDeleteRoomById(final Room roomToDelete) {
        Log.d(TAG,"roomID to delete: " + roomToDelete.getId());
        Call<HashMap<String, Integer>> deleteRoomCall = espcService.deleteRoomById(roomToDelete.getId());
        deleteRoomCall.enqueue(new Callback<HashMap<String, Integer>>() {
            @Override
            public void onResponse(Call<HashMap<String, Integer>> call, Response<HashMap<String, Integer>> response) {

                if(response.isSuccessful() | response.body().size() == 1) {
                    Log.d(TAG, "onResponse delete a room: " + response.body().toString());
                    mRooms.remove(roomToDelete);
                    mAdapter.notifyDataSetChanged();
                }

                mSwipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<HashMap<String, Integer>> call, Throwable t) {
                Log.e(TAG,"Delete Room failed " + t.toString());
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        getAllPropertyRooms();
    }

    @Override
    public void onClick(View v) {
        // Add new Room dialog here
        Room newRoom = new Room();
        newRoom.setPropertyID(111);
        newRoom.setName("Property from Android");

        espcService.addNewRoom(newRoom).enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {

                if(response.isSuccessful()){
                    Log.d(TAG, "onResponse add a new room: " + response.body().toString());
                }

               getAllPropertyRooms();
            }

            @Override
            public void onFailure(Call<Room> call, Throwable t) {
                Log.e(TAG,"Add a new Room" + t.toString());
            }
        });

    }
}
