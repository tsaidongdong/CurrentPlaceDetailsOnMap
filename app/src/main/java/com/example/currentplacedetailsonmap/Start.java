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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.currentplacedetailsonmap.camera.CameraActivity;
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

public class Start extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    private Button btn_go_map;
    private Button btn_login;
    private Button btn_exit;
    private Button btn_to_driven;
    private TextView mOutputText;

    GoogleAccountCredential mCredential;
    ProgressDialog mProgress;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static Vector<String> tags=new Vector<String>();//store the tags
    private static final String BUTTON_TEXT = "Call YouTube Data API";

    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { YouTubeScopes.YOUTUBE_READONLY };

    private char key_char;
    Like_analyze like_analyze=new Like_analyze();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);

        //btn_go_map=(Button)findViewById(R.id.button_to_map);
        btn_login=(Button)findViewById(R.id.button_to_login);
        btn_exit=(Button)findViewById(R.id.button_to_exit);
        //btn_to_driven=(Button)findViewById(R.id.button_to_driven);
        //mOutputText=(TextView)findViewById(R.id.textView);
        //mOutputText.setVisibility(TextView.INVISIBLE);
        /*btn_go_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(Start.this, MapsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putChar("key_char", key_char);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });*/
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(Start.this, Login_Menu.class);
                Bundle bundle = new Bundle();
                //bundle.putChar("key_char", key_char);
                intent.putExtras(bundle);
                startActivity(intent);
                /*btn_login.setEnabled(false);
                btn_go_map.setEnabled(false);
                //getResultsFromApi();
                //mOutputText.setVisibility(TextView.VISIBLE);
                btn_login.setEnabled(true);
                btn_go_map.setEnabled(true);*/
            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.exit(0);
            }
        });
        /*btn_to_driven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(Start.this, CameraActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });*/


        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Calling YouTube Data API ...");

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        //取得login資料
        Bundle bundle=this.getIntent().getExtras();
        //System.out.println(bundle);
        if(bundle!=null) {
            /*System.out.println("in the bundle");
            tags1 = new String[bundle.getStringArray("tags1").length];
            tags1 = bundle.getStringArray("tags1");
            tags2 = new String[bundle.getStringArray("tags2").length];
            tags2 = bundle.getStringArray("tags2");
            tags3 = new String[bundle.getStringArray("tags3").length];
            tags3 = bundle.getStringArray("tags3");
            tags4 = new String[bundle.getStringArray("tags4").length];
            tags4 = bundle.getStringArray("tags4");
            tags5 = new String[bundle.getStringArray("tags5").length];
            tags5 = bundle.getStringArray("tags5");
            System.out.println("tags1:");
            for(int i=0;i<tags1.length;i++){System.out.println(tags1[i]);}
            System.out.println("tags2:");
            for(int i=0;i<tags2.length;i++){System.out.println(tags2[i]);}
            System.out.println("tags3:");
            for(int i=0;i<tags3.length;i++){System.out.println(tags3[i]);}
            System.out.println("tags4:");
            for(int i=0;i<tags4.length;i++){System.out.println(tags4[i]);}
            System.out.println("tags5:");
            for(int i=0;i<tags5.length;i++){System.out.println(tags5[i]);}*/
        }
    }

    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private void getResultsFromApi() {
        Log.d("debug", "getResultsFromApi:");
        if (! isGooglePlayServicesAvailable()) {
            Log.d("debug", "acquireGooglePlayServices:");
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            Log.d("debug", "chooseAccount:");
            chooseAccount();
        } else if (! isDeviceOnline()) {
            Log.d("debug", "No network connection available:");
            Toast.makeText(this, "No network connection available.", Toast.LENGTH_LONG).show();
        } else {
            Log.d("debug", "MakeRequestTask:");
            new MakeRequestTask(mCredential).execute();
        }
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            Log.d("debug", "EasyPermissions.hasPermissions");
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                Log.d("debug", "accountName="+accountName);
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                Log.d("debug", "startActivityForResult:");
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
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

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */
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
                    String accountName =
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

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     *     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d("debug", "onRequestPermissionsResult");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        Log.d("debug", "onPermissionsGranted");
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        Log.d("debug", "onPermissionsDenied");
        // Do nothing.
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        Log.d("debug", "isDeviceOnline");
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        Log.d("debug", "isGooglePlayServicesAvailable");
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
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


    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        Log.d("debug", "showGooglePlayServicesAvailabilityErrorDialog");
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                Start.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /**
     * An asynchronous task that handles the YouTube Data API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
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
         * @return List of Strings containing information about the channel.
         * @throws IOException
         */
        private List<String> getDataFromApi() throws IOException {
            Log.d("debug", "getting from api ");
            // Get a list of up to 10 files.
            List<String> channelInfo = new ArrayList<String>();
            Log.d("debug", "getting result ");
            /*ChannelListResponse result = mService.channels().list("snippet,contentDetails,statistics")
                    .setForUsername("GoogleDevelopers")
                    .execute();*/
            Log.d("debug", "got result ");
            /*List<Channel> channels = result.getItems();*/

            /*Log.d("debug", "channels "+ channels.get(0).getId());
            if (channels != null) {
                Channel channel = channels.get(0);
                channelInfo.add("This channel's ID is " + channel.getId() + ". " +
                        "Its title is '" + channel.getSnippet().getTitle() + ", " +
                        "and it has " + channel.getStatistics().getViewCount() + " views.");
            }*/

            YouTube.Videos.List request = mService.videos().list("snippet,contentDetails,statistics");
            VideoListResponse response = request.setMaxResults(40L).setMyRating("like").execute();

            /*for(int i=0;i<10;i++){
                System.out.println(response.getItems().get(i).getSnippet().getTags());
            }*/
            //System.out.println(response.getItems().get(6).getSnippet().getTags());\
            //System.out.println(response.getItems().size());
            tags.clear();
            for(int i=0;i<10;i++){
                if (response.getItems().get(i).getSnippet().getTags()!=null){
                    //Intercept(response.getItems().get(i).getSnippet().getTags());
                    //Log.d("debug", "response.getItems().get(i).getSnippet().getTags()!=null");
                    //System.out.println(response.getItems().get(i).getSnippet().getTags());
                }

            }
            for(int i=0;i<10;i++){
                if (response.getItems().get(i).getSnippet().getTags()!=null){
                    Log.d("debug", " Intercept(response.getItems().get(i).getSnippet().getTags());");
                    Capture(response.getItems().get(i).getSnippet().getTags());
                }
            }
            /*while (tags.get(i)!=null){
                System.out.println(tags.get(i));
                i++;
            }*/

            return channelInfo;
        }



        @Override
        protected void onPostExecute(List<String> output) {
            Log.d("debug", "post exe ");
            //key_char=like_analyze.input_tags(tags);
            //System.out.println("main_activity key_char:"+key_char);
            mOutputText.setText(Character.toString(key_char));
            mProgress.hide();
            if (output == null || output.size() == 0) {

                //mOutputText.setText("No results returned.");
            } else {

                output.add(0, "Data retrieved using the YouTube Data API:");
                //.setText(TextUtils.join("\n", output));
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
            tags.add(new String(tmp));
            //System.out.println(tmp);
        }


        /*for (int i = 0 ; i < tags.size() ; i++){
            Object obj = tags.get(i);
            System.out.println(obj);
        }*/
    }
}
