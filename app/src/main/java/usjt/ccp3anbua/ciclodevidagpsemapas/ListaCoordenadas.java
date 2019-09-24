package usjt.ccp3anbua.ciclodevidagpsemapas;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListaCoordenadas extends AppCompatActivity {

    private ListView coordenadasListView;
    ArrayList listaCoordenadas = new ArrayList();

    private Float coordenada1;
    private Float coordenada2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_coordenadas);

        coordenadasListView = findViewById(R.id.coordenadasListView);


        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();

        listaCoordenadas = (ArrayList) bundle.getSerializable("lista");


        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaCoordenadas);

        coordenadasListView.setAdapter(adapter);



        coordenadasListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String [] coordenadas = listaCoordenadas.get(position).toString().split(":");

                Uri gmmIntentUri = Uri.parse(String.format("geo:%s,%s", coordenadas[0], coordenadas[1]));
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);

            }

        });


    }
}
