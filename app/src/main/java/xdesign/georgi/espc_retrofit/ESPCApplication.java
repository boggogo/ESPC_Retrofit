package xdesign.georgi.espc_retrofit;

import android.app.Application;

import java.util.ArrayList;

import xdesign.georgi.espc_retrofit.Backend.Property;
import xdesign.georgi.espc_retrofit.Database.EspcItemDataSource;

/**
 * Created by georgi on 20/06/16.
 */
public class ESPCApplication extends Application {
    private EspcItemDataSource mPropertyItemDataSource;
    private ArrayList<Property> preLoadedProperties = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();

//        mPropertyItemDataSource = new EspcItemDataSource(this);
//        mPropertyItemDataSource.open();
//
//        Property p1 = new Property();
//        p1.setId(0);
//        p1.setPrice("200.00");
//        p1.setUserID(8);
//        p1.setAddress("37 Mock Str. Aberdeen, AB26 UT");
//
//        Property p2 = new Property();
//        p2.setId(0);
//        p2.setPrice("350.00");
//        p2.setUserID(8);
//        p2.setAddress("11 St. Mock Str. Edinburgh, EH4");
//
//        preLoadedProperties.add(p1);
//        preLoadedProperties.add(p2);
//
//        for(Property p: preLoadedProperties){
//            mPropertyItemDataSource.createPropertyItem(p);
//        }
//
//
//        mPropertyItemDataSource.close();
    }
}
