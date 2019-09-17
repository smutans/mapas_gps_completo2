package usjt.ccp3anbua.ciclodevidagpsemapas;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ListaCoordenadas extends AppCompatActivity {

    private ListView coordenadasListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_coordenadas);

        coordenadasListView = findViewById(R.id.coordenadasListView);

        Intent intent = new Intent();
        Bundle bundle = intent.getExtras();

        ArrayList listaCoordenadas = (ArrayList) bundle.getSerializable("lista");

        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaCoordenadas);

        coordenadasListView.setAdapter(adapter);


    }
}
