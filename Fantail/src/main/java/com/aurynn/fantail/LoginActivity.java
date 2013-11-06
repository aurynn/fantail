package com.aurynn.fantail;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.alwaysallthetime.adnlib.AppDotNetClient;
import com.alwaysallthetime.adnlib.data.Token;
import com.alwaysallthetime.adnlib.response.LoginResponseHandler;
import com.aurynn.fantail.model.Settings;
import com.aurynn.fantail.sql.SettingsDAO;


public class LoginActivity extends Activity {

    /**
     * The default email to populate the email field with.
     */
//    public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
//    private UserLoginTask mAuthTask = null;

    // Values for email and password at the time of the login attempt.
    private String mUser;
    private String mPassword;
    private final String SCOPE = "basic,stream,write_post";

    // UI references.
    private EditText mUserView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private View mLoginStatusView;
    private TextView mLoginStatusMessageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // First, see if we're already logged in.

        Settings s = getSettings();
        if (s.getClientId() != null) {
            // We're good. Intent over to MainActivity, closing ourselves.
            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
            // Could set some things up here to pass it forwards.
            startActivity(mainActivity);
            finish();
            return; // leave now.
        }
        // otherwise, we need to try to do a login.

        setContentView(R.layout.activity_login);

        // Set up the login form.
//        mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
//        mEmailView = (EditText) findViewById(R.id.email);
//        mEmailView.setText(mEmail);

        mUserView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mLoginStatusView = findViewById(R.id.login_status);
        mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    private synchronized Settings getSettings() {

        SettingsDAO dao = new SettingsDAO(this);
        dao.open();
        Settings settings = dao.getSettings();
        dao.close();
        return settings;
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    public void attemptLogin() {
        String clientID = "" ;
        String pwGrantSecret = "";
        AppDotNetClient client = new AppDotNetClient(clientID, pwGrantSecret);
        final LoginActivity closure = this;

        mUser = mUserView.getText().toString();
        mPassword = mPasswordView.getText().toString();
        showProgress(true); // Switch!

        client.authenticateWithPassword(mUser, mPassword, SCOPE, new LoginResponseHandler() {
                @Override
                public void onSuccess(String accessToken, Token token) {
                    // The access token has already been set on the client; you don't need to call setToken() here.
                    // This also happens on the main thread.

                    Log.d("onSuccess", "got token?");
                    Log.d("onSuccess", accessToken);
                    SettingsDAO dao = new SettingsDAO(closure);
                    dao.open();
//                    Settings settings = dao.getSettings();
                    Settings settings = new Settings();
                    settings.setClientID( accessToken );
                    dao.save( settings );
                    dao.close();
                    // the settings object should now have an id!
                    Log.d("Settings ID", settings.getColumnId().toString());
                    // Let's redirect to the main thing now.
                    Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                    // Could set some things up here to pass it forwards.
                    startActivity(mainActivity);
                    finish();
                }
//                @Override
//                public void onError() {
//                    Log.d("error!", "error");
//                }
            });

//        if (mAuthTask != null) {
//            return;
//        }
//
//
//
//        // Reset errors.
//        mUserView.setError(null);
//        mPasswordView.setError(null);
//
//        // Store values at the time of the login attempt.
//        mUser = mUserView.getText().toString();
//        mPassword = mPasswordView.getText().toString();
//
//        boolean cancel = false;
//        View focusView = null;
//
//        // Check for a valid password.
//        if (TextUtils.isEmpty(mPassword)) {
//            mPasswordView.setError(getString(R.string.error_field_required));
//            focusView = mPasswordView;
//            cancel = true;
//        } else if (mPassword.length() < 4) {
//            mPasswordView.setError(getString(R.string.error_invalid_password));
//            focusView = mPasswordView;
//            cancel = true;
//        }
//
//        // Check for a valid email address.
//        if (TextUtils.isEmpty(mUser)) {
//            mUserView.setError(getString(R.string.error_field_required));
//            focusView = mUserView;
//            cancel = true;
//        } else if (!mUser.contains("@")) {
//            mUserView.setError(getString(R.string.error_invalid_email));
//            focusView = mUserView;
//            cancel = true;
//        }
//
//        if (cancel) {
//            // There was an error; don't attempt login and focus the first
//            // form field with an error.
//            focusView.requestFocus();
//        } else {
//            // Show a progress spinner, and kick off a background task to
//            // perform the user login attempt.
//            mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
//            showProgress(true);
//            mAuthTask = new UserLoginTask();
//            mAuthTask.execute((Void) null);
//        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginStatusView.setVisibility(View.VISIBLE);
            mLoginStatusView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 1 : 0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
                        }
                    });

            mLoginFormView.setVisibility(View.VISIBLE);
            mLoginFormView.animate()
                    .setDuration(shortAnimTime)
                    .alpha(show ? 0 : 1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
//    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
//        @Override
//        protected Boolean doInBackground(Void... params) {
//
//            try {
//                // Simulate network access.
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                return false;
//            }
//
////            for (String credential : DUMMY_CREDENTIALS) {
////                String[] pieces = credential.split(":");
////                if (pieces[0].equals(mUser)) {
////                    // Account exists, return true if the password matches.
////                    return pieces[1].equals(mPassword);
////                }
////            }
//
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success) {
//            mAuthTask = null;
//            showProgress(false);
//
//            if (success) {
//                finish();
//            } else {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
//        }
//    }
}
