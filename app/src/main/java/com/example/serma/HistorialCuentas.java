package com.example.serma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HistorialCuentas extends AppCompatActivity implements SearchView.OnQueryTextListener {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView fecha;
    SearchView svHistorial;
    ListView ArrayTO;
    RecyclerView.Adapter adapters;
    RecyclerView recyclerHistorial;
    RecyclerView.LayoutManager layoutManager;
    String cantidad;
    ArrayList<transactionModel> models = new ArrayList<>();
    HashMap<String, List> resultado = new HashMap<>();
    adapter_historial_cardview mo = new adapter_historial_cardview(App.getAppContext(),models);
    private int currentApiVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_cuentas);
       // ArrayTO = findViewById(R.id.ResultadosHistorial);
        fecha = findViewById(R.id.textViewFECHA);
        svHistorial = findViewById(R.id.searchViewHistorial);
        recyclerHistorial = findViewById(R.id.mostarHistorial);

        String fechas = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH )+1) + "/" + Calendar.getInstance().get(Calendar.YEAR);
        fecha.setText(fechas);

        recyclerHistorial.setHasFixedSize(true);



        currentApiVersion = android.os.Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT)
        {

            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
                    {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility)
                        {
                            if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                            {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }




        getList();
        svHistorial.setOnQueryTextListener(this);
        /*

        svHistorial.setOnQueryTextListener(new SearchView.OnQueryTextListener() {





            @Override
            public boolean onQueryTextSubmit(String text) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {

                if(adapters == null){
                    Toast.makeText(getBaseContext(), "Aún no se cargan los datos",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    //antiguo buscador en el adaptador
                 //   adapters.getFilter().filter(text);
                }


                return true;
            }
        });*/






}
public void getList(){

    db.collection("Historial")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String objetos = document.get("objeto").toString();

                                    if(objetos.trim().equals("")){
                                        objetos = "Sin objetos";
                                    }
                                    if(document.get("cantidad") == null){
                                        cantidad = "Sin cantidad";
                                    }
                                    else{
                                        cantidad = document.get("cantidad").toString();
                                    }



                                    transactionModel m = new transactionModel(document.getDouble("AÑO"),document.getDouble("DIA"),document.getDouble("IVA"),document.getDouble("MES"),document.getString("descripcion"),document.getString("tipo"),document.getDouble("total"),document.getString("cliente"),objetos,cantidad);

                                    models.add(m);


                                    Log.i("NUEVO",m.toString());







                                }


                                recyclerHistorial.setAdapter(mo);
                                recyclerHistorial.setLayoutManager(new LinearLayoutManager(App.getAppContext()));
                            } else {
                                Log.d("feo", "Error getting documents: ", task.getException());
                            }


                        }
                    });


                }
            });

}

//TODO: STRING NULL IN SEARCH ok boomer
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        List<transactionModel> newList = new ArrayList<>();
        Log.i("LO QUE CONTIENE",newText);
        for(transactionModel model: models){
            if(model.getTitulo() == null){
                model.setTitulo(" ");
            }
            if(model.getTipo() == null){
                model.setTipo(" ");
            }
            if(model.getCliente() == null){
                model.setCliente(" ");
            }
            if(model.getTotal() == null){
                model.setTotal(0.0);
            }
            if(model.getFecha() == null){
                model.setFecha("Fecha inválida");
            }

            if(model.getTitulo().toLowerCase().contains(newText.toLowerCase()) ||model.getTipo().toLowerCase().contains((newText.toLowerCase())) || model.getCliente().toLowerCase().contains(newText.toLowerCase()) || model.getTotal().toString().toLowerCase().contains(newText.toLowerCase()) || model.getFecha().toLowerCase().contains(newText.toLowerCase())){
                newList.add(model);
                Log.i("BUSQUEDA",newList.toString());


            }


        }
        mo.updateList(newList);

        return true;
    }
}

