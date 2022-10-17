package com.example.currentplacedetailsonmap.camera;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.currentplacedetailsonmap.MapsActivity;
import com.example.currentplacedetailsonmap.R;
import com.example.currentplacedetailsonmap.camera.ar.ImageDetectionFilter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.apache.commons.math3.ml.neuralnet.twod.util.LocationFinder;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

public final class CameraActivity extends AppCompatActivity implements CvCameraViewListener2, OnMapReadyCallback {

    // A tag for log output.
    private static final String TAG = CameraActivity.class.getSimpleName();


    // The filters.
    private Filter[] mImageDetectionFilters;

    // The indices of the active filters.
    private int mImageDetectionFilterIndex;

    // The camera view.
    private CameraBridgeViewBase mCameraView;

    //get location
    private GoogleMap map;

    // The entry point to the Places API.
    private PlacesClient placesClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;
    private LocationRequest locationRequest;

    // Keys for storing activity state.
    // [START maps_current_place_state_keys]
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    // [END maps_current_place_state_keys]

    private boolean requestingLocationUpdates;
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("OpenCV", "OpenCV loaded successfully");
                    Log.d("OpenCV", "creating mat");
                    Mat imageMat = new Mat();
                    Log.d("OpenCV", "mat created");

                    Log.d("info", "Starting main activaty of camera cativity");
//                    mCameraView = new JavaCameraView(this, 0);
//                    mCameraView.setCvCameraViewListener(this);
//                    setContentView(mCameraView);
//                    mCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };
    private LocationCallback locationCallback;
    private String key_word;
    String shop_name;
    String LastShop;
    LatLng shop_location;
    int shop_rating;
    String shop_business_status;
    String shop_address;
    String shop_snippet;
    private String []author_name=new String[3];
    private int [] author_rating=new int[3];
    private String [] author_time_description=new String[3];
    private String [] author_text=new String[3];

    private int Check_scene=0;//0 is false
    private float NowSpeed=0;
    TextView SpeedView;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mCameraView = new JavaCameraView(this, 0);
        mCameraView.setCvCameraViewListener(this);
        setContentView(mCameraView);
        mCameraView.enableView();

        //GET LOCATION
        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }
        // [START_EXCLUDE silent]
        // Construct a PlacesClient
        Places.initialize(getApplicationContext(), getString(R.string.Maps_api_key));
        placesClient = Places.createClient(this);

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Build the map.
        // [START maps_current_place_map_fragment]
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager();
        //mapFragment.getMapAsync(this);
        // [END maps_current_place_map_fragment]
        // [END_EXCLUDE]

        getLocationPermission();

        getDeviceLocation();
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    getDeviceLocation();
                }
            }
        };
        createLocationRequest();
        startLocationUpdates();

        //宣告speedview
        SpeedView= new TextView(this);
        SpeedView.setEnabled(false);
        SpeedView.setVisibility(View.INVISIBLE);


    }

    public void ShowAttraction() {
        String json_url = null;
        json_url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                +lastKnownLocation.getLatitude()
                +","
                +lastKnownLocation.getLongitude()
                +"&radius=1000&keyword="
                +key_word
                +"&key=AIzaSyCrkpGMPSpno8i20ukKDGgveveF4oGy00M";

        //json_url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+"24.095700279571645, 120.55596208462191"+"&radius=200&keyword=" +key_word+"&key=AIzaSyCrkpGMPSpno8i20ukKDGgveveF4oGy00M";
        AttractionUrl(json_url);
    }

    @Override
    public void onPause() {
        if (mCameraView != null) {
            mCameraView.disableView();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        //OpenCVLoader.initDebug();
        if (!OpenCVLoader.initDebug()) {
            Log.d("OpenCV", "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);
        } else {
            Log.d("OpenCV", "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        if (requestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        //在线程中执行时，必须指定Looper，且调用Looper.prepare
    }


    @Override
    public void onDestroy() {
        if (mCameraView != null) {
            mCameraView.disableView();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.activity_camera, menu);
        return true;
    }



    @Override
    public void onCameraViewStarted(int width, int height) {
        Filter hi_life = null;
        try {
            hi_life = new ImageDetectionFilter(CameraActivity.this, R.drawable.hi_life2);

        } catch (IOException e) {
            e.printStackTrace();
        }


        Filter family_mart = null;
        try {
            family_mart = new ImageDetectionFilter(CameraActivity.this, R.drawable.family_mart);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Filter seveneleven = null;
        try {
            seveneleven = new ImageDetectionFilter(CameraActivity.this, R.drawable.seven_eleven);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Filter ok_mart = null;
        try {
            ok_mart = new ImageDetectionFilter(CameraActivity.this, R.drawable.okmart);

        } catch (IOException e) {
            e.printStackTrace();
        }
        mImageDetectionFilters = new Filter[]{
                hi_life,
                family_mart,
                seveneleven,
                ok_mart
        };
        Check_scene=0;
    }

    @Override
    public void onCameraViewStopped() {

    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        final Mat rgba = inputFrame.rgba();
        System.out.println("in the cameraframe");
        if (mImageDetectionFilters != null) {
            mImageDetectionFilterIndex++;
            if (mImageDetectionFilterIndex == mImageDetectionFilters.length) {
                mImageDetectionFilterIndex = 0;

            }
            if(mImageDetectionFilterIndex==0){
                key_word="萊爾富";
            }
            else if (mImageDetectionFilterIndex==1){
                key_word="全家";
            }
            else if(mImageDetectionFilterIndex==2){
                key_word="7-11";
            }
            else {
                key_word="OKMart";
            }
            Check_scene=mImageDetectionFilters[mImageDetectionFilterIndex].apply(rgba, rgba);
            //System.out.println("check_scene"+Check_scene);
            if(Check_scene==1){
                ShowAttraction();
            }
        }

        return rgba;
    }

    protected void createLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        requestingLocationUpdates=true;
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }
    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    // [START maps_current_place_update_location_ui]
    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    // [END maps_current_place_update_location_ui]

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {

                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map=map;
        this.map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = infoWindow.findViewById(R.id.title);
                title.setText(marker.getTitle());

                TextView snippet = infoWindow.findViewById(R.id.snippet);
                snippet.setText(marker.getSnippet());

                return infoWindow;
            }
        });
        // [END map_current_place_set_info_window_adapter]

        // Prompt the user for permission.
        getLocationPermission();
        // [END_EXCLUDE]

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void AttractionUrl(String input) {//show the attraction
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, input, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(JSONObject response) {
                //System.out.println("get_AttractionJSON");
                try {
                    for (int i = 0; i < 1; i++) {
                        shop_name = response.getJSONArray("results").getJSONObject(i).getString("name");
                        //System.out.println("name:"+shop_name);
                        shop_location = new LatLng(response.getJSONArray("results").getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lat")
                                , response.getJSONArray("results").getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
                        //System.out.println("location:"+shop_location);
                        shop_rating = response.getJSONArray("results").getJSONObject(i).getInt("rating");
                        //System.out.println("rating:"+shop_rating);
                        shop_business_status = response.getJSONArray("results").getJSONObject(i).getString("business_status");
                        //System.out.println("business_status:"+shop_business_status);
                        shop_address = response.getJSONArray("results").getJSONObject(i).getString("vicinity");
                        //System.out.println("address:"+shop_address);
                        shop_snippet = "Address:" + shop_address + "\n"
                                + "Rating:" + shop_rating + "\n"
                                + "Business_status:" + shop_business_status;
                        String id=response.getJSONArray("results").getJSONObject(i).getString("place_id");
                        String id_url="https://maps.googleapis.com/maps/api/place/details/json?place_id="
                                    +id
                                    +"&key=AIzaSyCrkpGMPSpno8i20ukKDGgveveF4oGy00M&fields=reviews";
                        if(LastShop!=shop_name) {
                            getAttractionReview(id_url);
                            draw_the_shop_information();
                        }
                        LastShop=shop_name;
                    }
                } catch (JSONException e) {
                    //System.out.println("e.printStackTrace();");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //ln("onErrorResponse:");
                // TODO: Handle error

            }

        });

        // Access the RequestQueue through your singleton class.
        //MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

    }



    public void getAttractionReview(String input){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, input, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(JSONObject response) {
                //System.out.println("get_AttractionJSON");
                try {
                    if(response.length()>=3){
                        for(int i=0;i<3;i++){
                            //System.out.println("in the getAttractionReview");
                            author_name[i]=response.getJSONObject("result").getJSONArray("reviews").getJSONObject(i).getString("author_name");
                            author_rating[i]=response.getJSONObject("result").getJSONArray("reviews").getJSONObject(i).getInt("rating");
                            author_time_description[i]=response.getJSONObject("result").getJSONArray("reviews").getJSONObject(i).getString("relative_time_description");
                            author_text[i]=response.getJSONObject("result").getJSONArray("reviews").getJSONObject(i).getString("text");
                            System.out.println("author_name:"+author_name[i]);
                            System.out.println("author_rating:"+author_rating[i]);
                            System.out.println("author_time_description:"+author_time_description[i]);
                            System.out.println("author_text:"+author_text[i]);
                        }
                    }
                    else{

                    }

                } catch (JSONException  e) {
                    //System.out.println("e.printStackTrace();");
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //System.out.println("onErrorResponse:");
                // TODO: Handle error
            }
        });
        // Access the RequestQueue through your singleton class.
        //MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }

    private void draw_the_shop_information() {
        String review=null;
        for(int i=0;i<author_name.length;i++){
            review=review+"\n"
                    +author_name[i]+"(評分:"+author_rating[i]+")"+"     "+author_time_description[i]+"\n"
                    +"=>"+author_text;
        }
        String message="地址:"+shop_address+"\n"
                +"狀態:"+shop_business_status+"\n"
                +"評分:"+shop_rating+"\n"
                +"評論:\n"
                +review;
        AlertDialog.Builder builder=new AlertDialog.Builder(CameraActivity.this);
        builder.setTitle("店名:"+shop_name+"\n");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.setNegativeButton("", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp=dialog.getWindow().getAttributes();
        lp.gravity=Gravity.RIGHT;

        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.cancel();
            }
        }, 5000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.chanage_mode){
            Intent intent=new Intent();
            intent.setClass(CameraActivity.this, MapsActivity.class);
            Bundle bundle = new Bundle();
            intent.putExtras(bundle);
            startActivity(intent);
        }
        return true;
    }
   /* @Override
    public void onLocationChanged(@NonNull Location location) {
        NowSpeed=  location.getSpeed();
        System.out.println("NowSpeed"+NowSpeed);
        SpeedView.setTextColor(Color.WHITE);
        SpeedView.setTextSize(50);
        SpeedView.setEnabled(false);
        SpeedView.setVisibility(View.INVISIBLE);
        SpeedView.setText((int) NowSpeed);


    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }*/
}

