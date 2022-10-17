package com.example.currentplacedetailsonmap;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class Login_Menu extends AppCompatActivity {

    private Button btn_account1;
    private Button btn_account2;
    private Button btn_account3;
    private Button btn_account4;
    private Button btn_account5;
    private Button btn_next;
    private Button self_button;
    private Button btn_exit;

    private static Vector<String> tags1 = new Vector<String>();//store the tags account1
    private static Vector<String> tags2 = new Vector<String>();//store the tags account2
    private static Vector<String> tags3 = new Vector<String>();//store the tags account3
    private static Vector<String> tags4 = new Vector<String>();//store the tags account4
    private static Vector<String> tags5 = new Vector<String>();//store the tags account5
    private static Vector<String> tags6 = new Vector<String>();//store the tags account5
    private GoogleAccountCredential mCredential;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_AUTHORIZATION = 1001;
    ProgressDialog mProgress;
    private static final String[] SCOPES = { YouTubeScopes.YOUTUBE_READONLY };
    String accountName=null;
    int accountNumber=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_menu);

        btn_account1 = (Button) findViewById(R.id.account1);
        btn_account2 = (Button) findViewById(R.id.account2);
        btn_account3 = (Button) findViewById(R.id.account3);
        btn_account4 = (Button) findViewById(R.id.account4);
        btn_account5 = (Button) findViewById(R.id.account5);
        self_button=(Button)findViewById(R.id.self);
        btn_next=(Button)findViewById(R.id.go_button);
        btn_exit=(Button)findViewById(R.id.button_to_exit2);

        btn_account1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountNumber=1;
                tags1.clear();
                getResultsFromApi();
            }
        });
        btn_account2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tags2.clear();
                accountNumber=2;
                getResultsFromApi();
            }
        });
        btn_account3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tags3.clear();
                accountNumber=3;
                getResultsFromApi();
            }
        });
        btn_account4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tags4.clear();
                accountNumber=4;
                getResultsFromApi();
            }
        });
        btn_account5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tags5.clear();
                accountNumber=5;
                getResultsFromApi();
            }
        });

        self_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tags6.clear();
                accountNumber=6;
                getResultsFromApi();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(Login_Menu.this, Menu.class);
                Bundle bundle = new Bundle();

                int NowtagsSize=tags1.size();
                String[] TmpStrings=tags1.toArray(new String[NowtagsSize]);
                bundle.putStringArray("tags1", TmpStrings);

                NowtagsSize=tags2.size();
                TmpStrings=tags2.toArray(new String[NowtagsSize]);
                bundle.putStringArray("tags2", TmpStrings);

                NowtagsSize=tags3.size();
                TmpStrings=tags3.toArray(new String[NowtagsSize]);
                bundle.putStringArray("tags3", TmpStrings);

                NowtagsSize=tags4.size();
                TmpStrings=tags4.toArray(new String[NowtagsSize]);
                bundle.putStringArray("tags4", TmpStrings);

                NowtagsSize=tags5.size();
                TmpStrings=tags5.toArray(new String[NowtagsSize]);
                bundle.putStringArray("tags5", TmpStrings);

                NowtagsSize=tags6.size();
                TmpStrings=tags6.toArray(new String[NowtagsSize]);
                bundle.putStringArray("tags6", TmpStrings);

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });


        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling YouTube Data API ...");

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
    }

    private void getResultsFromApi() {
        Log.d("debug", "getResultsFromApi:");
        if (!isGooglePlayServicesAvailable()) {
            Log.d("debug", "c");
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null || accountName==null) {
            Log.d("debug", "chooseAccount:");
            chooseAccount();
        } else if (!isDeviceOnline()) {
            Log.d("debug", "No network connection available:");
            Toast.makeText(this, "No network connection available.", Toast.LENGTH_LONG).show();
        } else {
            Log.d("debug", "MakeRequestTask:");
            new MakeRequestTask(mCredential).execute();
        }
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("debug", "onActivityResult: ");
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                Log.d("debug", "REQUEST_GOOGLE_PLAY_SERVICES: ");
                if (resultCode != RESULT_OK) {
                    Toast.makeText(this, "This app requires Google Play Services. Please install " + "Google Play Services on your device and relaunch this app.", Toast.LENGTH_LONG).show();
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                Log.d("debug", "REQUEST_ACCOUNT_PICKER: ");
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                Log.d("debug", "REQUEST_AUTHORIZATION: ");
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    private boolean isDeviceOnline() {
        Log.d("debug", "isDeviceOnline");
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        Log.d("debug", "in the choose account");

        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            Log.d("debug", "EasyPermissions.hasPermissions");
            accountName= getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            Log.d("debug", "startActivityForResult:");
            // Start a dialog from which the user can choose an account

            Log.d("debug", "startActivityForResult:");
            // Start a dialog from which the user can choose an account
            startActivityForResult(
                    mCredential.newChooseAccountIntent(),
                    REQUEST_ACCOUNT_PICKER);
            /*if (accountName != null) {
                Log.d("debug", "accountName=" + accountName);

                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                Log.d("debug", "startActivityForResult:");
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }*/
        } else {
            Log.d("debug", "EasyPermissions.requestPermissions");
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    private void acquireGooglePlayServices() {
        Log.d("debug", "acquireGooglePlayServices");
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        Log.d("debug", "showGooglePlayServicesAvailabilityErrorDialog");
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                Login_Menu.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    private boolean isGooglePlayServicesAvailable() {
        Log.d("debug", "isGooglePlayServicesAvailable");
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    private class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.youtube.YouTube mService = null;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.youtube.YouTube.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("YouTube Data API Android Quickstart")
                    .build();

            Log.d("debug", "MakeRequestTask successful ");
        }

        @Override
        protected void onPreExecute() {
            Log.d("debug", "pre execute ");
            mProgress.show();
        }

        /**
         * Background task to call YouTube Data API.
         *
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<String> doInBackground(Void... params) {
            Log.d("debug", "do inback ");
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch information about the "GoogleDevelopers" YouTube channel.
         *
         * @return List of Strings containing information about the channel.
         * @throws IOException
         */
        private List<String> getDataFromApi() throws IOException {
            Log.d("debug", "getting from api ");
            // Get a list of up to 10 files.
            List<String> channelInfo = new ArrayList<String>();
            Log.d("debug", "getting result ");
            Log.d("debug", "got result ");

            YouTube.Videos.List request = mService.videos().list("snippet,contentDetails,statistics");
            VideoListResponse response = request.setMaxResults(40L).setMyRating("like").execute();

            //System.out.println(response.getItems().size());
            //tags.clear();
            for (int i = 0; i < 10; i++) {
                if (response.getItems().get(i).getSnippet().getTags() != null) {
                    //Intercept(response.getItems().get(i).getSnippet().getTags());
                    //Log.d("debug", "response.getItems().get(i).getSnippet().getTags()!=null");
                    //System.out.println(response.getItems().get(i).getSnippet().getTags());
                }

            }
            for (int i = 0; i < 10; i++) {
                if (response.getItems().get(i).getSnippet().getTags() != null) {
                    Log.d("debug", " Intercept(response.getItems().get(i).getSnippet().getTags());");
                    Capture(response.getItems().get(i).getSnippet().getTags());
                }
            }
            return channelInfo;


        }

        @Override
        protected void onPostExecute(List<String> output) {
            Log.d("debug", "post exe ");
            mProgress.hide();
            if (output == null || output.size() == 0) {

                //mOutputText.setText("No results returned.");
            } else {

                output.add(0, "Data retrieved using the YouTube Data API:");
            }
        }

        @Override
        protected void onCancelled() {
            Log.d("debug", "cancelled ");
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    Log.d("debug", "GooglePlayServicesAvailabilityIOException ");
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    Log.d("debug", "UserRecoverableAuthIOException ");
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(), REQUEST_AUTHORIZATION);
                } else {
                    Log.d("debug", "error ");
                    Log.d("debug", "Trace");
                    mLastError.printStackTrace();
                    Log.d("debug", "Cause");
                    Log.d("debug", String.valueOf(mLastError.getCause()));
                    //Toast.makeText(Menu.this, "The following error occurred:\n", )
                    //mOutputText.setText("The following error occurred:\n"+ mLastError.getMessage());
                }
            } else {
                Log.d("debug", "cancel ");
                //mOutputText.setText("Request cancelled.");
            }
        }
    }

    public void Capture(List<String> input){
        Log.d("debug", "tags.add");
        String tmp;



        for(int i = 0;i<input.size();i++){
            tmp=input.get(i).toString();
            if(accountNumber==1){
                tags1.add(new String(tmp));
                System.out.println("tags1.get(i)"+tags1.get(i));
            }
            if(accountNumber==2){
                tags2.add(new String(tmp));
                System.out.println("tags2.get(i)"+tags2.get(i));
            }
            if (accountNumber==3){
                tags3.add(new String(tmp));
                System.out.println("tags3.get(i)"+tags3.get(i));
            }
            if(accountNumber==4){
                tags4.add(new String(tmp));
                System.out.println("tags4.get(i)"+tags4.get(i));
            }
            if(accountNumber==5){
                tags5.add(new String(tmp));
                System.out.println("tags5.get(i)"+tags5.get(i));
            }
            if(accountNumber==6){
                tags6.add(new String(tmp));
                System.out.println("tags6.get(i)"+tags6.get(i));
            }
        }

        accountName=null;
    }
}
