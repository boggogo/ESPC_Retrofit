package xdesign.georgi.espc_retrofit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import xdesign.georgi.espc_retrofit.Utils.Constants;
import xdesign.georgi.espc_retrofit.Utils.User_ESPC;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Callback<List<User_ESPC>> {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private ESPCService espcService;
    private ArrayList<User_ESPC> mUsers = new ArrayList<>();
    private Call<List<User_ESPC>> call;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;

    // UI references.
    private ProgressBar mProgressBar;
    private EditText mNameView;
    private EditText mPasswordView;
    private Button mLoginButton;
    private CoordinatorLayout mCoordinatorLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        mCoordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinatorLayout);

        espcService = ESPCService.retrofit.create(ESPCService.class);

        mNameView = (EditText) findViewById(R.id.name);
        mPasswordView = (EditText) findViewById(R.id.password);


        mLoginButton = (Button)findViewById(R.id.loginBtn);
        mLoginButton.setOnClickListener(this);




    }


    @Override
    public void onClick(View v) {
//        mProgressBar.setVisibility(View.VISIBLE);
        call = espcService.getUserByNameAndPass(mNameView.getText().toString().trim(),mPasswordView.getText().toString().trim());
        call.enqueue(this);

    }

    @Override
    public void onResponse(Call<List<User_ESPC>> call, Response<List<User_ESPC>> response) {
        Log.e("MainActivity", "onResponse");

        for(User_ESPC u: response.body()){
            Log.d(TAG,u.getName());
        }
        // Check if the list is with size 0, if it is user with the provided name and password does not exists in the backend
        if(response.body().size() != 0){
            // user exists
            // save the user state as logged in
            mEditor.putBoolean(Constants.IS_USER_LOGGED_IN,true).apply();

            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else{
            Snackbar snackbar = Snackbar
                    .make(mCoordinatorLayout, "Wrong user credentials!", Snackbar.LENGTH_LONG)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            DO SOMETHING
                        }
                    });
            snackbar.show();
        }
    }

    @Override
    public void onFailure(Call<List<User_ESPC>> call, Throwable t) {
        Log.e("MainActivity", "onFailure - " + t.toString());
    }
}

