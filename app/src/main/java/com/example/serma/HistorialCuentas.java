package com.example.serma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class HistorialCuentas extends AppCompatActivity  {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView fecha;
    SearchView svHistorial;
    ListView ArrayTO;
    SimpleAdapter adapters;
    HashMap<String, List> resultado = new HashMap<>();
    int contadoraClicks;

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
                                        List list = new ArrayList();
                                        list.add(document.get("DIA") + "/" + document.get("MES") + "/" + document.get("AÑO"));
                                        list.add(document.get("DIA") + "/" + document.get("MES") + "/" + document.get("AÑO"));
                                        list.add(document.get("DIA") + "/" + document.get("MES") + "/" + document.get("AÑO"));
                                        list.add(document.get("DIA") + "/" + document.get("MES") + "/" + document.get("AÑO"));


                                        resultado.put(document.get("descripcion").toString(),list);
                                        List<HashMap<String, String>> listItems = new ArrayList<>();
                                        adapters = new SimpleAdapter(HistorialCuentas.this,listItems,R.layout.list_item2,
                                                new String[]{"First line","Second line","tercera","cuarta","quinta"},new int[]{R.id.text1,R.id.text2,R.id.text3,R.id.text4,R.id.text5});
                                        Iterator it = resultado.entrySet().iterator();

                                        while(it.hasNext()){
                                            HashMap<String, String> resultsmap = new HashMap<>();
                                            Map.Entry pair = (Map.Entry) it.next();
                                            for(int i = 1;i<=list.size();i++) {

                                                resultsmap.put("First line", pair.getKey().toString());

                                                resultsmap.put("Second line", list.get(i).toString()); ;

                                                resultsmap.put("tercera", list.get(i).toString());

                                                resultsmap.put("cuarta", list.get(i).toString());

                                                resultsmap.put("quinta", list.get(i).toString());
                                            }



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

        ArrayTO.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                contadoraClicks++;
                HashMap<String, String> clickedItem = (HashMap<String, String>) adapters.getItem(position);
                final String value = clickedItem.get("First line");
                final String key = clickedItem.get("Second line");
                Log.i("PRUEBA DE OBTENCIÍON",position+ "");
                Handler handler =  new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(contadoraClicks == 1){



                            final DocumentReference docRef = db.collection("Historial").document(value);
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());



                                        } else {
                                            Log.d(TAG, "No such document");
                                        }
                                    } else {
                                        Log.d(TAG, "get failed with ", task.getException());
                                    }
                                }
                            });


                        }
                        else if(contadoraClicks == 2){
                            AlertDialog alertDialog = new AlertDialog.Builder(App.getAppContext()).create();
                            alertDialog.setTitle("¿Eliminar cuenta?");
                            alertDialog.setMessage("¿Deseas eliminar la cuenta por cobrar de " + value);
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(App.getAppContext(), "De acuerdo, no elimino nada",
                                                    Toast.LENGTH_SHORT).show();


                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Si", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Guarda guar = new Guarda();
                                    guar.borraCuentaPorCobrar(resultado.get(position).toString());
                                    dialog.dismiss();
                                }
                            });
                            ;
                            alertDialog.show();
                        }
                        contadoraClicks = 0;
                    }
                },500);



            }
        });
    }




}
