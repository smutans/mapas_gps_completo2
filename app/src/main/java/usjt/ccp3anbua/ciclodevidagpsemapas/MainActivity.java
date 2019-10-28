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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;

    private double latitude, longitude;


    private TextView locationTextView;
    private EditText locationEditText;

    private String termo;

    private RecyclerView localizacaoRecyclerView;
    private LocalizacaoAdapter adapter;
    private List<Localizacao> localizacoes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        RecyclerView = findViewById(R.id.localizacaoRecyclerView);
        localizacoes = new ArrayList<>();

        adapter = new LocalizacaoAdapter(this, localizacoes);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        localizacaoRecyclerView.setAdapter(adapter);
        localizacaoRecyclerView.setLayoutManager(llm);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                Intent intent = new Intent(MainActivity.this, ListaCoordenadas.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("lista",novaLista);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });



        // Classe do Android que permite obter localizações GPS/GSM....
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // locationListener é uma interface, por isso não pode ser imlpementada apenas com new, precisa
        // ter (){} e implementar todos os métodos pois é uma classe concreta, precisa sobrescrever os métodos.
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                // Grava cada localização em um ArrayList de Objetos do tipo Localização
                listaLocalizacao.add(new Localizacao(location.getLatitude(), location.getLongitude()));


                Toast.makeText(MainActivity.this, "Qtd: " + listaLocalizacao.size(),Toast.LENGTH_SHORT).show();

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

    class LocalizacaoViewHolder extends RecyclerView.ViewHolder{

        public TextView latitudeTextView;
        public TextView longitudeTextView;

        public LocalizacaoViewHolder (View raiz){
            super (raiz);

            this.latitudeTextView =
                    raiz.findViewById(R.id.latitudeTextView);
            this.longitudeTextView =
                    raiz.findViewById(R.id.longitudeTextView);

        }
    }

    class LocalizacaoAdapter extends RecyclerView.Adapter <LocalizacaoViewHolder>{

        private Context context;
        private List<Localizacao> localizacoes;

        public LocalizacaoAdapter(Context context, List<Localizacao> localizcoes) {
            this.context = context;
            this.localizacoes = localizacoes;
        }

        @NonNull
        @Override
        public LocalizacaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater =
                    LayoutInflater.from(context);
            View raiz = inflater.inflate(R.layout.content_main,parent,false);


            return new LocalizacaoViewHolder(raiz);
        }

        @Override
        public void onBindViewHolder(@NonNull LocalizacaoViewHolder holder, int position) {

            Localizacao w = localizacoes.get(position);

            holder.latitudeTextView.setText(
                    context.getString(latitude)
            );
            holder.longitudeTextView.setText(
                    context.getString(longitude)
            );
        }

        @Override
        public int getItemCount() {
            return localizacoes.size();
        }
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
