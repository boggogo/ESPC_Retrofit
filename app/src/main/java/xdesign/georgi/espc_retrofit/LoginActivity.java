package xdesign.georgi.espc_retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = LoginActivity.class.getSimpleName();
    // UI references.
    private ProgressBar mProgressBar;
    private EditText mNameView;
    private EditText mPasswordView;
    private Button mLoginButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.



    }


    @Override
    public void onClick(View v) {
        mProgressBar.setVisibility(View.VISIBLE);


    }
}

