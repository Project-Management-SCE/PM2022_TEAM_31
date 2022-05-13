package com.subzzz.getoverhere;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.common.util.concurrent.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private CameraPosition cameraPosition;

    private AutocompleteSupportFragment autocompleteSupportFragment;

    //location provider
    private FusedLocationProviderClient fusedLocationProviderClient;

    private ActivityResultLauncher<String[]> requestLocationPermission;

    private GeofencingClient geofencingClient;
    private PlacesClient placesClient;


    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private LatLng currentLocation;
    private LatLng destinationLocation;
    private CircleOptions currentLocationCircle;
    private Circle mapCircle;


    public static final long UPDATE_INTERVAL = 2000;
    public static final long FASTEST_INTERVAL = 3000;
    private static final int DEFAULT_ZOOM = 17;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private Marker originMarker;
    private Marker destMarker;
    private Marker currentLocationMarker;

    public MapsFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            currentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }

        Places.initialize(requireContext(),getString(R.string.google_maps_key));
        placesClient = Places.createClient(requireContext());
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerRequestLocationPermissions();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        geofencingClient = LocationServices.getGeofencingClient(requireActivity()); // add geofencing
        
        initAutocompleteSupportFragment();
        initLocationCallback();

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void initAutocompleteSupportFragment() {
        autocompleteSupportFragment = (AutocompleteSupportFragment) getChildFragmentManager()
                .findFragmentById(R.id.autocomplete_fragment_search);

        assert autocompleteSupportFragment != null;
        autocompleteSupportFragment.setTypeFilter(TypeFilter.ADDRESS);

        autocompleteSupportFragment.setLocationBias(RectangularBounds.newInstance(
                new LatLng(31, 35),
                new LatLng(31, 35)));

        autocompleteSupportFragment.setCountry("IL");

        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.NAME
                ,Place.Field.LAT_LNG));

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onError(@NonNull Status status) {
                Log.i("Place Tag","An Error occured: " + status );
            }

            @Override
            public void onPlaceSelected(@NonNull Place place) {
                destinationLocation =new LatLng(Objects.requireNonNull(place.getLatLng()).latitude, place.getLatLng().longitude);
                Log.i("Place Tag","Place: " + place.getName() + ", "+ place.getId() +"\n"
                        + place.getLatLng());
                destinationMarker();
            }
        });
    }

    private void registerRequestLocationPermissions() {
        requestLocationPermission = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    for (Map.Entry<String, Boolean> entry : result.entrySet()) {
                        String key = entry.getKey();
                        Boolean value = entry.getValue();
                        if (!value) {
                            Toast.makeText(requireActivity(), "App location permission is mandatory:\n" + key, Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    getMyLastLocation();
                });
    }

    @SuppressLint("MissingPermission")
    private void getMyLastLocation() {
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
            if (location != null) {
                currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
                updateLocationMarker();
            }
        });
        startLocationUpdates();
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        locationRequest = LocationRequest.create()
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(100);

        if(!isLocationPermissionGranted()){
            Toast.makeText(requireActivity(), "You need to enable permission to display location!", Toast.LENGTH_SHORT).show();
        }


        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void initLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                currentLocation = new LatLng(location.getLatitude(),location.getLongitude());
                updateLocationMarker();
            }

            @Override
            public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };
    }

    private boolean isLocationPermissionGranted() {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.mMap = googleMap;
        if (isLocationPermissionGranted()) {
            getMyLastLocation();
        } else {
            requestLocationPermission.launch(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION});
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void destinationMarker(){
        if(destMarker != null){
            destMarker.remove();
        }
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(destinationLocation,DEFAULT_ZOOM);
        mMap.animateCamera(cameraUpdate);
        MarkerOptions newMarkerOption = new MarkerOptions()
                .position(destinationLocation);
        destMarker = mMap.addMarker(newMarkerOption);
    }

    private void updateLocationMarker(){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,DEFAULT_ZOOM));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker())
                .position(currentLocation));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, currentLocation);
        }
        super.onSaveInstanceState(outState);
    }



}