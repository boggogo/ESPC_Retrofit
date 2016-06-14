package xdesign.georgi.espc_retrofit.UI;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import xdesign.georgi.espc_retrofit.Backend.Property;
import xdesign.georgi.espc_retrofit.R;
import xdesign.georgi.espc_retrofit.Utils.Constants;

public class PropertyDetailsActivity extends AppCompatActivity {
    private static final String TAG = PropertyDetailsActivity.class.getSimpleName();

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

        Bundle data = getIntent().getExtras();
        Property property = (Property) data.getSerializable(Constants.KEY_PROPERTY_OBJECT);
        getSupportActionBar().setTitle(property.getAddress());
        Log.d(TAG, property.toString());
    }

}
