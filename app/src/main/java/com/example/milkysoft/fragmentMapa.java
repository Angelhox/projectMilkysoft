package com.example.milkysoft;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.milkysoft.databinding.FragmentMapaBinding;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DatabaseReference;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class fragmentMapa extends DialogFragment implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener
        , GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        LocationListener
{
    Button btnGuardarUbicacion;
    private FragmentMapaBinding binding;
    private View root;
    EditText txtlatitud, txtlongitud, txtdireccion;
    GoogleMap mMap;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    Location location;
    Marker markerLocation;
    DatabaseReference mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =FragmentMapaBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        // Inflate the layout for this fragment
        mDatabase= FirebaseDatabase.getInstance().getReference();


        btnGuardarUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Geocoder geocoder=new Geocoder(getContext(), Locale.getDefault());
                List<Address> direccion= null;
                try {
                    direccion = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Map<String,Object> latlang=new HashMap<>();
                latlang.put("latitud",location.getLatitude());
                latlang.put("longitud",location.getLongitude());
                latlang.put("direccion",direccion.get(0).getAddressLine(0));

                mDatabase.child("Ubicación").push().setValue(latlang);
            }
        });


        return root;
    }

    private void initFuset() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        buildLocationRequest();
        bulidLocationCallback();
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());

    }
    private void iniciarControles(){
        btnGuardarUbicacion=root.findViewById(R.id.btnGuardarUbicacion);
        txtlatitud = root.findViewById(R.id.txtlatitud);
        txtlongitud = root.findViewById(R.id.txtlongitud);
        txtdireccion = root.findViewById(R.id.txtdireccion);

    }

    private void bulidLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                location = locationResult.getLocations().get(locationResult.getLocations().size() - 1);
                Log.e("Location",""+location.getLatitude()+" "+location.getLongitude());


                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            if (markerLocation != null) {
                                markerLocation.remove();
                            }
                            markerLocation = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker())
                                    .position(new LatLng(location.getLatitude(), location.getLongitude())).title("Mi ubicación actual"));
                            txtlatitud.setText("Latitud: "+location.getLatitude());
                            txtlongitud.setText("Longitud: "+location.getLongitude());
                            Geocoder geocoder=new Geocoder(getContext(), Locale.getDefault());
                            try {
                                List<Address> direccion=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                                txtdireccion.setText(direccion.get(0).getAddressLine(0));



                            } catch (IOException e) {
                                e.printStackTrace();
                            }





                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),15.0f));
                        }

                    }
                });
            }
        };
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10);
    }

    private void checkPermission(String accessFineLocation, String accessCoarseLocation) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), accessCoarseLocation) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(), accessFineLocation) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{accessCoarseLocation, accessFineLocation}, 3);
                return;
            }
        }
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult) {

        if (grantResult.length > 0 && grantResult[0] == PackageManager.PERMISSION_DENIED) {
            initFuset();

        } else {
            Toast.makeText(getContext(), "NO OK", Toast.LENGTH_SHORT).show();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResult);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        mMap = googleMap;
        buildLocationRequest();
        bulidLocationCallback();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
      /*  this.mMap.setOnMapClickListener(this);
        this.mMap.setOnMapLongClickListener(this);

        LatLng ecuador = new LatLng(-0.2298500, -78.5249500);
        mMap.addMarker(new MarkerOptions().position(ecuador).title("Ecuador"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ecuador));*/

    }

    @Override
    public void onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        super.onStop();
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        txtlatitud.setText("Latitud: "+location.getLatitude());
        txtlongitud.setText("Longitud: "+location.getLongitude());


        mMap.clear();
        LatLng ecuador = new LatLng(latLng.latitude, latLng.longitude);
        mMap.addMarker(new MarkerOptions().position(ecuador).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ecuador));

    }

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        txtlatitud.setText("Latitud: "+location.getLatitude());
        txtlongitud.setText("Longitud: "+location.getLongitude());



        mMap.clear();
        LatLng ecuador = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.addMarker(new MarkerOptions().position(ecuador).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ecuador));



    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }


}