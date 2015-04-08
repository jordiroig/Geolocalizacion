package geolocalizacion.android.jordiroig.com.geolocalizacion;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
    private GoogleMap map;                      //nuestro mapa
    private boolean firstattempt = true;        //variable para controlar la primera vez que obtenemos nuestra posición
    private LatLng position;                    //variable para guardar la posición inicial

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);    //instanciamos el fragment que contiene el mapa
        mapFragment.getMapAsync(this);          //establecemos un objeto callback que se activará cuando el mapa esté listo (onMapReady)
        map = mapFragment.getMap();             //instanciamos el mapa

        final ImageButton loc_btn = (ImageButton) findViewById(R.id.button);
        final ImageButton loc_btn2 = (ImageButton) findViewById(R.id.button2);
        loc_btn.setOnClickListener(this);       //listeners para los botones
        loc_btn2.setOnClickListener(this);
        loc_btn.setEnabled(false);              //desactivamos los botones hasta tener la posición
        loc_btn2.setEnabled(false);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);    //clase para acceder a los recursos de localización
        LocationListener locationListener = new LocationListener() {    //listener de localización

            @Override
            public void onLocationChanged(Location location) {          //listener cuando la posición ha cambiado
                if(firstattempt && map != null)                         //solo entramos la primera vez que cambia la posición
                {
                    firstattempt = false;
                    Double lat = location.getLatitude();
                    Double lng = location.getLongitude();
                    Random randomGenerator = new Random();
                    double random1 = randomGenerator.nextInt(40) - 20; //Valor aleatorio entre -20 i +20
                    double random2 = randomGenerator.nextInt(40) - 20; //Valor aleatorio entre -20 i +20
                    random1 = random1 / 1000;           //valor +/- 0.02
                    random2 = random2 / 1000;           //valor +/- 0.02
                    map.addMarker(new MarkerOptions()   //Añadimos un marcador en una posición aleatoria cercana a la localización
                            .position(new LatLng(lat + random1, lng + random2))
                            .title("Trabajo"));
                    Toast.makeText(getApplicationContext(), "Localización inicial", Toast.LENGTH_SHORT).show();
                    position = new LatLng(lat, lng);    //Guardamos la posición
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 13)); //movemos la camara a la posición (con zoom 13)
                    loc_btn.setEnabled(true);       //Activamos los botones
                    loc_btn2.setEnabled(true);
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // no usado
            }

            @Override
            public void onProviderEnabled(String provider) {
                // no usado
            }

            @Override
            public void onProviderDisabled(String provider) {
                // no usado
            }
        };

        Criteria criteria = new Criteria(); //Establecemos unos criterios para la localización
        criteria.setAccuracy(Criteria.ACCURACY_FINE);   //nivel de precisión
        criteria.setAltitudeRequired(false);            //no es necesaria la altitud
        criteria.setBearingRequired(false);             //no es necesaria la orientación
        criteria.setSpeedRequired(false);               //no es necesaria la velocidad
        String bestProvider = locationManager.getBestProvider(criteria, false);         //Obtenemos el mejor provider segur los criterios
        locationManager.requestLocationUpdates(bestProvider, 0, 0, locationListener);   //Escuchamos los cambios de posición
    }

    @Override
    public void onMapReady(GoogleMap map)       //método lanzado cuando el mapa está listo
    {
        this.map = map;
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);             //mapa tipo terreno
        map.getUiSettings().setMyLocationButtonEnabled(false);  //desactivamos el boton de localizarnos
        map.getUiSettings().setZoomControlsEnabled(true);       //activamos los controles de zoom
        map.setTrafficEnabled(true);                            //activamos el tráfico
        map.setMyLocationEnabled(true);                         //activamos la posición en el mapa (bola azul)
    }

    @Override
    public void onClick(View v)             //clic en la vista
    {
        int targetId = v.getId();
        if (targetId == R.id.button)        //es el boton de localizarnos
        {
            Toast.makeText(getApplicationContext(), "Localizando posición", Toast.LENGTH_SHORT).show();     //mensaje por pantalla
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 13));                             //movemos la cámara a la posición (zoom 13)
        }
        else if(targetId == R.id.button2)   //es el boton de mostrar las coordenadas
        {
            Toast.makeText(getApplicationContext(), "Mi localización es: " + position.latitude + "," + position.longitude, Toast.LENGTH_SHORT).show();  //mostramos las coordenadas
        }
    }

}