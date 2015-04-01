package geolocalizacion.android.jordiroig.com.geolocalizacion;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Random;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener
{
    private GoogleMap map;
    private boolean firstattempt = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        map = mapFragment.getMap();
        map.setOnMyLocationChangeListener(myLocationChangeListener);

        ImageButton loc_btn = (ImageButton) findViewById(R.id.button);
        ImageButton loc_btn2 = (ImageButton) findViewById(R.id.button2);
        loc_btn.setOnClickListener(this);
        loc_btn2.setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap map)
    {
        this.map = map;
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setTrafficEnabled(true);
        map.setMyLocationEnabled(true);
    }

    @Override
    public void onClick(View v)
    {
        int targetId = v.getId();
        if (targetId == R.id.button)
        {
            Toast.makeText(getApplicationContext(), "Localizando posición", Toast.LENGTH_SHORT).show();
            LatLng position = new LatLng(map.getMyLocation().getLatitude(),map.getMyLocation().getLongitude());
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 13));
        }
        else if(targetId == R.id.button2)
        {
            Toast.makeText(getApplicationContext(), "Mi localización es: " + map.getMyLocation().getLatitude()+ "," + map.getMyLocation().getLongitude(), Toast.LENGTH_SHORT).show();
        }
    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            if(firstattempt && map != null)
            {
                firstattempt = false;
                Double lat = location.getLatitude();
                Double lng = location.getLongitude();
                Random randomGenerator = new Random();
                double random1 = randomGenerator.nextInt(40) - 20; //Valor aleatorio entre -15 i +15
                double random2 = randomGenerator.nextInt(40) - 20; //Valor aleatorio entre -15 i +15
                random1 = random1 / 1000;
                random2 = random2 / 1000;
                map.addMarker(new MarkerOptions()
                        .position(new LatLng(lat + random1, lng + random2))
                        .title("Trabajo"));
                Toast.makeText(getApplicationContext(), "Localización inicial", Toast.LENGTH_SHORT).show();
                LatLng position = new LatLng(lat, lng);
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 13));
            }
        }
    };
}