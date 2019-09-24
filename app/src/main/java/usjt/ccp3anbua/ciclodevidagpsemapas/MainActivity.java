package usjt.ccp3anbua.ciclodevidagpsemapas;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;

    private double latitude, longitude;
    private double latitudeAtual, longitudeAtual;

    private TextView locationTextView;
    private EditText locationEditText;

    private String termo;

    public ArrayList<String> novaLista = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        locationTextView = findViewById(R.id.locationTextView);
//        locationEditText = findViewById(R.id.locationEditText);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(MainActivity.this, ListaCoordenadas.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("lista",novaLista);
                intent.putExtras(bundle);
                startActivity(intent);


//                termo = locationEditText.getText().toString();
//
//                if (termo.isEmpty()){
//                    Toast.makeText(MainActivity.this, getString(R.string.buscaVazia), Toast.LENGTH_SHORT).show();
//                } else {
//                    Uri gmmIntentUri = Uri.parse(String.format("geo:%f,%f?q=%s", latitudeAtual, longitudeAtual, termo));
//                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//                    mapIntent.setPackage("com.google.android.apps.maps");
//                    startActivity(mapIntent);
//                }


            }
        });


        // Classe do Android que permite obter localizações GPS/GSM....
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // locationListener é uma interface, por isso não pode ser imlpementada apenas com new, precisa
        // ter (){} e implementar todos os métodos pois é uma classe concreta, precisa sobrescrever os métodos.
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                latitudeAtual = latitude;
                longitudeAtual = longitude;

                if (novaLista.size() >= 50){
                    novaLista.remove(0);
                }


                novaLista.add(String.format("%f:%f", latitudeAtual, longitudeAtual));
//
                //locationTextView.setText(String.format("Lat: %f, Long: %f", latitude, longitude));

                Toast.makeText(MainActivity.this, latitudeAtual+","+longitudeAtual,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle
                    extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };

    }


    private static final int REQUEST_PERMISSION_CODE_GPS = 1001;

    @Override
    protected void onStart() {
        super.onStart();
        //a permissão já foi dada?
        //somente ativa
        //a localização é obtida via hardware, intervalo de segundos e 0 metros entre atualizações
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 5, locationListener);
        } else {
            //permissão ainda não foi nada, solicita ao usuário
            //quando o usuário responder, o método onRequestPermissionsResult vai ser chamado
            //o número 1001 pode ser qq número inteiro, quando aparecer a caixinha de diálogo, o programa continua
            //executando. E esse número fica associado à permissão garantida, se for de GPS pex.
            ActivityCompat.requestPermissions(this,
                    new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_CODE_GPS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION_CODE_GPS){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // Permissão concedida, ativando o GPS
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
                }
            } else {
                //usuário negou, não ativamos
                Toast.makeText(this, getString(R.string.no_gps_no_app), Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }
}
