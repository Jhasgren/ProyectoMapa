package com.example.proyectomapa;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapaActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Enlace enlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng chihuahua = new LatLng(28.6682636, -106.1499306);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(chihuahua, 12.0f));
        buscarUbicacion();
    }

    public void buscarUbicacion() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 0, 0, new android.location.LocationListener() {
                    boolean ubicado = false;

                    @Override
                    public void onLocationChanged(Location location) {
                        if (!ubicado) {
                            Toast.makeText(MapaActivity.this, "ubicado", Toast.LENGTH_SHORT).show();
                            ubicado = true;
                            final LatLng ubicacion = new LatLng(location.getLatitude(),
                                    location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(ubicacion).title("Ubicacion"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(ubicacion));
                            enlace = new Enlace("https://maps.googleapis.com/maps/api/place/nearbysearch/xml?location=" +
                                    ubicacion.latitude + "," + ubicacion.longitude +
                                    "&radius=5000&types=food&key=AIzaSyCEZvyJVUQZoaDs4keQDTMg8AR3YZoqCgk") {
                                @Override
                                public void onRespuesta(final ArrayList<Ubicaciones> ubicaciones) {
                                    super.onRespuesta(ubicaciones);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (ubicaciones != null) {
                                                for (Ubicaciones u : ubicaciones) {
                                                    Log.e("ubicacion", u.getNombre());
                                                    mMap.addMarker(new MarkerOptions().position(u.getPos()).title(u.getNombre()));
                                                }
                                            } else {
                                                Toast.makeText(MapaActivity.this, "No se encontraron lugares", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            };
                        }
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });
    }
}
