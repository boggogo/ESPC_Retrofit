package xdesign.georgi.espc_retrofit.UI;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import xdesign.georgi.espc_retrofit.R;

public class SplashScreenActivity extends AppCompatActivity {private final int SPLASH_DISPLAY_LENGTH = 500;

    /**
     * Method that is called when the activity is created
     *
     * @param savedInstanceState bundle object
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

/* New Handler to start the UrlActivity
         * and close this Splash-Screen after the above amount of seconds.*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the UrlActivity. */
                Intent mainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                SplashScreenActivity.this.startActivity(mainIntent);
                // Finish this activity to delete it from the activity hierarchy
                SplashScreenActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
