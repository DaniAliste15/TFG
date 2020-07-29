package com.example.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MapaAdminActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FirebaseAuth mAutho;
    DatabaseReference Database;

    private ArrayList<Marker> tmpRealTimeMarkers = new ArrayList<>();
    private ArrayList<Marker> realTimeMarkers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_admin);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mAutho = FirebaseAuth.getInstance();
        Database = FirebaseDatabase.getInstance().getReference();
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
        mMap.getUiSettings().setZoomControlsEnabled(true); //zoom + -
        String id = "QRx6hwIDLXRAWbZggUiCu2aMIzh2";
        //String id = mAutho.getCurrentUser().getUid(); //obtener el id del usuario

        Database.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(Marker marker : realTimeMarkers) {
                    marker.remove();
                }

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TraerLatLong mp = snapshot.getValue(TraerLatLong.class);
                    Double latitud = mp.getLatitud();
                    Double longitud = mp.getLongitud();

                    LatLng incendio = new LatLng(latitud,longitud);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(incendio).title(""+latitud+" / "+longitud);
                    if(latitud != 0.0 && longitud != 0.0) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(incendio, 14));
                    }
                    tmpRealTimeMarkers.add(mMap.addMarker(markerOptions));

                }

                realTimeMarkers.clear();
                realTimeMarkers.addAll(tmpRealTimeMarkers);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
    }

    /*public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true); //zoom + -

        Database.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(Marker marker : realTimeMarkers) {
                    marker.remove();
                }

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TraerLatLong mp = snapshot.getValue(TraerLatLong.class);
                    Double latitud = mp.getLatitud();
                    Double longitud = mp.getLongitud();

                    LatLng incendio = new LatLng(latitud,longitud);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(incendio).title(""+latitud+" / "+longitud);
                    if(latitud != 0.0 && longitud != 0.0) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(incendio, 14));
                    }
                    tmpRealTimeMarkers.add(mMap.addMarker(markerOptions));

                }

                realTimeMarkers.clear();
                realTimeMarkers.addAll(tmpRealTimeMarkers);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
}
*/
    @Override public boolean onCreateOptionsMenu(Menu mimenu) {

          /*OTRA FORMA DE HACERLO
          MenuInflater inflater = getMenuInflater();
          inflater.inflate(R.menu.menu_en_activity,mimenu);

        return super.onCreateOptionsMenu(mimenu);*/

        getMenuInflater().inflate(R.menu.menu_mapa_admin,mimenu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem opcion_menu) {
        int id = opcion_menu.getItemId();

        if(id == R.id.salir){
            final AlertDialog.Builder alerta = new AlertDialog.Builder(MapaAdminActivity.this);
            alerta.setMessage("¿Desea cerrar sesion?")
                    .setCancelable(false)
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAutho.signOut();
                            startActivity(new Intent(MapaAdminActivity.this,LoginActivity.class));
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog titulo = alerta.create();
            titulo.setTitle("Atención");
            titulo.show();

            return true;
        }
        return super.onOptionsItemSelected(opcion_menu);
    }
}
