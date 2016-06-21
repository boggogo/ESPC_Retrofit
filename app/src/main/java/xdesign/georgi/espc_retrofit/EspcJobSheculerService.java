package xdesign.georgi.espc_retrofit;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xdesign.georgi.espc_retrofit.Backend.ESPCService;
import xdesign.georgi.espc_retrofit.Backend.Property;
import xdesign.georgi.espc_retrofit.Database.EspcItemDataSource;
import xdesign.georgi.espc_retrofit.UI.MainActivity;

/**
 * Created by georgi on 20/06/16.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class EspcJobSheculerService extends JobService implements Callback<List<Property>> {
    private static final String TAG = EspcJobSheculerService.class.getSimpleName();
    private EspcItemDataSource mPropertyItemDataSource;
    private static ESPCService espcService;
    private Call<List<Property>> call;

    @Override
    public void onCreate() {
        super.onCreate();
        espcService = ESPCService.retrofit.create(ESPCService.class);
        call = espcService.getAllProperties();

        mPropertyItemDataSource = new EspcItemDataSource(getApplicationContext());
        mPropertyItemDataSource.open();
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        mJobHandler.sendMessage(Message.obtain(mJobHandler, 1, params));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mJobHandler.removeMessages(1);
        mPropertyItemDataSource.close();
        return false;
    }

    private Handler mJobHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            Toast.makeText(getApplicationContext(),
                    "JobService task running", Toast.LENGTH_SHORT)
                    .show();
            Log.e(TAG, "Job Service task is running...");
            call.enqueue(EspcJobSheculerService.this);

            jobFinished((JobParameters) msg.obj, false);
            return true;
        }

    });

    @Override
    public void onResponse(Call<List<Property>> call, Response<List<Property>> response) {
        Log.d(TAG, "onResponse get all properties. Success: " + response.isSuccessful());
        if (response.isSuccessful()) {
            for (Property p : response.body()) {
                // Store the property in the database...
                Log.d(TAG, "Property with address: " + p.getAddress());


                if (mPropertyItemDataSource.ifExistsLocally(p)) {
                    // entry exists locally. Check if needs updating...
                    Log.d(TAG, "// entry exists locally. Check if needs updating...");

                    Property localProperty = mPropertyItemDataSource.getPropertyItemById(p.getId());

                    Log.d(TAG, localProperty.toString());

                    long remoteTimeStamp = Long.parseLong(p.getLastUpdated());
                    long localTimeStamp = Long.parseLong(localProperty.getLastUpdated());

                    Date remoteDate = new Date(remoteTimeStamp);
                    Date localDate = new Date(localTimeStamp);


                    // check if local property lastUpdated equalst remote property last updated
                    if(localDate.compareTo(remoteDate) == 0){
                        // two dates are equal
                        Log.d(TAG, "Two dates are equal. NO NEED to update");
                    }else{
                        Log.d(TAG, "Two dates are NOT equal. Update performed!");
                        mPropertyItemDataSource.updatePropertyItem(p);
                    }

                } else {
                    // entry does not exists locally. Create it...
                    Log.d(TAG, "entry does not exists locally. Create it...");
                    mPropertyItemDataSource.createPropertyItem(p);
                }


            }
// TODO WORK ON THAT
            mPropertyItemDataSource.retainAllLocalFromRemote(response.body());

            MainActivity.getDataFromTheLocalDB();
        }
    }

    @Override
    public void onFailure(Call<List<Property>> call, Throwable t) {
        Log.e(TAG, "onFailure error: " + t.toString());
    }
}
