package com.example.serma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
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

public class HistorialCuentas extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView fecha;
    SearchView svHistorial;
    ListView ArrayTO;
    SimpleAdapter adapters;
    HashMap<String, String> resultado = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_cuentas);
        ArrayTO = findViewById(R.id.ResultadosHistorial);
        fecha = findViewById(R.id.textViewFECHA);
        svHistorial = findViewById(R.id.searchViewHistorial);

        String fechas = Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "/" + (Calendar.getInstance().get(Calendar.MONTH )+1) + "/" + Calendar.getInstance().get(Calendar.YEAR);
        fecha.setText(fechas);




        db.collection("Historial").orderBy("DIA", Query.Direction.DESCENDING).orderBy("MES", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        resultado.put(document.getString("descripcion"), document.get("DIA") + "/" + document.get("MES") + "/" + document.get("AÑO"));

                                        List<HashMap<String, String>> listItems = new ArrayList<>();
                                        adapters = new SimpleAdapter(HistorialCuentas.this,listItems,R.layout.list_item,
                                                new String[]{"First line","Second line"},new int[]{R.id.text1,R.id.text2});
                                        Iterator it = resultado.entrySet().iterator();
                                        while(it.hasNext()){
                                            HashMap<String, String> resultsmap = new HashMap<>();
                                            Map.Entry pair = (Map.Entry)it.next();
                                            resultsmap.put("First line",pair.getKey().toString());
                                            resultsmap.put("Second line",pair.getValue().toString());
                                            listItems.add(resultsmap);
                                        }

                                        ArrayTO.setAdapter(adapters);


                                    }
                                } else {
                                    Log.d("feo", "Error getting documents: ", task.getException());
                                }
                            }
                        });


                    }
                });
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
                    adapters.getFilter().filter(text);
                }


                return true;
            }
        });

        //ArrayTO.setOnItemClickListener(HistorialCuentas.this);
    }

}
