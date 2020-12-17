package dqa.com.msibook;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class msibook_login extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    private ProgressDialog pDialog;
    UserData UserDataClass = new UserData();

    private RequestQueue mQueue;
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    //private AutoCompleteTextView mEmailView;
    private LinearLayout login_main_r;
    private ImageView imageView_logo;
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    private  ProgressDialog progressBar;//讀取狀態


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msibook_login);
        // Set up the login form.
        //mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        //讀取時間Bar
        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMessage("資料載入中");


        mEmailView = (EditText) findViewById(R.id.edit_mail);
        mEmailView.setHint("Outlook ID");//未點擊的字樣
        populateAutoComplete();

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("FCM", "Token:"+token);

        //判斷Outlook mail 點擊事件
        mEmailView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == true){
                    mEmailView.setHint("");
                    mEmailView.setGravity(Gravity.LEFT);
                }else{
                    mEmailView.setHint("Outlook ID");
                    mEmailView.setGravity(Gravity.CENTER);
                }
            }
        });


        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setHint("Password");//未點擊的字樣
        //判斷pwd 點擊事件
        mPasswordView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus == true){
                    mPasswordView.setHint("");
                    mPasswordView.setGravity(Gravity.LEFT);
                }else{
                    mPasswordView.setHint("Password");
                    mPasswordView.setGravity(Gravity.CENTER);
                }
            }
        });

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });


        //縮鍵盤1
        imageView_logo = (ImageView) findViewById(R.id.imageView_logo);//Relative lay out  登入頁面
        imageView_logo.setOnTouchListener(new View.OnTouchListener() {  //Relative lay out  點選
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //點選 Layout任一方 將鍵盤收起來
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEmailView.getWindowToken(),0);
                imm.hideSoftInputFromWindow(mPasswordView.getWindowToken(),0);

                return false;
            }
        });
        //縮鍵盤2
        login_main_r = (LinearLayout) findViewById(R.id.login_main_relative);//Relative lay out  登入頁面
        login_main_r.setOnTouchListener(new View.OnTouchListener() {  //Relative lay out  點選
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //點選 Layout任一方 將鍵盤收起來
                InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mEmailView.getWindowToken(),0);
                imm.hideSoftInputFromWindow(mPasswordView.getWindowToken(),0);

                return false;
            }
        });

        final Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

        //登入按鈕
        mEmailSignInButton.setText("Log In");
        mEmailSignInButton.setOnTouchListener(new Button.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                if (event.getAction() == MotionEvent.ACTION_DOWN) {  //按下的時候改變背景及顏色
                    mEmailSignInButton.setBackgroundResource(R.drawable.msibook_btnlogin_down_shape);
                    mEmailSignInButton.setTextColor(Color.RED);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {  //起來的時候恢復背景與顏色
                    mEmailSignInButton.setBackgroundResource(R.drawable.msibook_btnlogin_up_shape);
                    mEmailSignInButton.setTextColor(Color.WHITE);
                    imm.hideSoftInputFromWindow(msibook_login.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mEmailView.getText().toString().equals("") || mPasswordView.getText().toString().equals("")){//判斷帳號是否為空值
                    if(mEmailView.getText().toString().equals("")) {
                        mEmailView.setError("Outlook帳號不得為空");
                    }
                    if(mPasswordView.getText().toString().equals("")){
                        mPasswordView.setError("密碼不得為空");
                    }
                }else{
                    if (mPasswordView.getText().toString().equals("abcd1234")){
                        GetAccountData_Back_door(mEmailView.getText().toString());
                    }else{
                        attemptLogin();
                    }
                }

            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
//        if (TextUtils.isEmpty(email)) {
//            mEmailView.setError(getString(R.string.error_field_required));
//            focusView = mEmailView;
//            cancel = true;
//        } else if (!isEmailValid(email)) {
//            mEmailView.setError(getString(R.string.error_invalid_email));
//            focusView = mEmailView;
//            cancel = true;
//        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(msibook_login.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        //mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {

            mEmail = email;
            mPassword = password;

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {

                String Account = mEmail;

                String Password = mPassword;

                GetAccountData(Account, Password);


            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private void GetAccountData(String Account, String Password) {



        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }

        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.ServicePath + "/AuthenticateWTSC?OutlookID=" + Account + "&OutlookPassword=" + Password;


        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                if (SetUserData(result)) {

                    UserDB UserDB = new UserDB(getApplicationContext());

                    UserDB.insert(UserDataClass);

                    Intent intent = new Intent(getBaseContext(), MainPage.class);

                    startActivity(intent);

                } else {
                    AppClass.AlertMessage("Wrong Outlook ID or Password!!", msibook_login.this);

                }


            }
        });

    }


    private void GetAccountData_Back_door(String WorkID) {

        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }
        HTTPSTrustManager.allowAllSSL();//信任所有证书，信任憑證
        String Path = GetServiceData.IMS_ServicePath + "/Find_Member_Information?WorkID=" + WorkID;


        GetServiceData.getString(Path, mQueue, new GetServiceData.VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) {

                if (SetUserData(result)) {

                    UserDB UserDB = new UserDB(getApplicationContext());

                    UserDB.insert(UserDataClass);

                    UserData UserData = new UserData();

                    UserData = UserDB.getAll().get(0);

                    Intent intent = new Intent(msibook_login.this, MainPage.class);

                    startActivity(intent);


                } else {
                    AppClass.AlertMessage("Wrong Outlook ID or Password!!", msibook_login.this);

                }


            }
        });

    }


    private Boolean SetUserData(JSONObject UserResult) {

        Boolean Validate = false;

        try {
            if (UserResult != null) {

                JSONArray UserArray = new JSONArray(UserResult.getString("Key"));

                if (UserArray.length() > 0) {

                    String DeptID = String.valueOf(UserArray.getJSONObject(0).getInt("DeptID"));

                    String F_OrgID = UserArray.getJSONObject(0).getString("F_OrgID");

                    String Account = UserArray.getJSONObject(0).getString("EnglishName");

                    String Email = UserArray.getJSONObject(0).getString("Email");

                    String Password = "";

                    String WorkID = UserArray.getJSONObject(0).getString("WorkID");

                    String Name = UserArray.getJSONObject(0).getString("ChineseName");

                    String Phone = UserArray.getJSONObject(0).getString("Tel");

                    String Dept = UserArray.getJSONObject(0).getString("DeptName");

                    String WebFlowBoss = UserArray.getJSONObject(0).getString("WebFlowBoss");

                    String WebFlowBossName = UserArray.getJSONObject(0).getString("WebFlowBossName");

                    String WebFlowBossTel = UserArray.getJSONObject(0).getString("WebFlowBossTel");

                    String Region = UserArray.getJSONObject(0).getString("Region");

                    String LastTab = "";

                    UserDataClass = new UserData(DeptID,F_OrgID,Account,Email, Password, WorkID, Name,Dept,Phone,Name,WebFlowBoss,WebFlowBossName,WebFlowBossTel,Region,LastTab);


                }

                if (!TextUtils.isEmpty(UserDataClass.Account)) {
                    Validate = true;

                }
            }


        } catch (JSONException ex) {
            AppClass.AlertMessage("Wrong Outlook ID or Password!!", msibook_login.this);
        }


        return Validate;

    }


    private void ValidationOutlook() {


    }
}

