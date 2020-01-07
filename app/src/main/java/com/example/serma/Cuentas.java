package com.example.serma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

public class Cuentas extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Cliente = db.collection("categoria");

    ArrayList todacate = new ArrayList();
    double totalIngresos = 0.0;
    double totalEgresos = 0.0;
    double totalIVA = 0.0;

    double ingresos = 0.0;
    double egresos = 0.0;
    public static double iva = 0.0;
    double capital = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuentas);
        final TextView Ingresos = findViewById(R.id.textViewTotalIngresos);
        final TextView Egresos = findViewById(R.id.textViewTotalEgresos);
        final TextView IVA = findViewById(R.id.textViewTotalIva);
        final TextView Capital = findViewById(R.id.textViewTotalCapital);


        db.collection("Historial").whereEqualTo("tipo","Compra").whereEqualTo("MES",Calendar.getInstance().get(Calendar.MONTH) + 1).whereEqualTo("AÑO",Calendar.getInstance().get(Calendar.YEAR))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (!document.exists()) {
                                            Egresos.setText(String.valueOf(egresos));
                                        } else {
                                            // TODO: SI HAY ERROR AQUÍ, ES PORQUE EN GETTRASANCTION SE GUARDA COMO STRING Y NO COMO DOUBLE. VERIFICAR ESO
                                            egresos = document.getDouble("total") + egresos + document.getDouble("IVA");


                                        }

                                    }


                                    db.collection("Historial").whereEqualTo("tipo","Venta").whereEqualTo("MES",Calendar.getInstance().get(Calendar.MONTH) + 1).whereEqualTo("AÑO",Calendar.getInstance().get(Calendar.YEAR))
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                                                    new Handler().post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            if (task.isSuccessful()) {
                                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                                    if (!document.exists()) {
                                                                        Ingresos.setText(String.valueOf(ingresos));
                                                                    } else {

                                                                        ingresos = document.getDouble("total") + ingresos;
                                                                        iva = document.getDouble("IVA") + iva;

                                                                        Ingresos.setText(String.valueOf(ingresos));

                                                                    }
                                                                }
                                                                Log.i("EGRESOOOOOOOOOSSSSS",egresos + "---" +ingresos + "---" + iva + "EGRESOOOOOS");
                                                                //TODO HACER CUENTAS POR COBRAR AQUI DEBE IR SU CALCULO.

                                                                Egresos.setText(String.valueOf(egresos + iva));
                                                                capital = ingresos - (egresos + iva);
                                                                Capital.setText(String.valueOf(capital));
                                                            } else {
                                                                Log.i("feo", "Error getting documents: ", task.getException());
                                                            }
                                                        }

                                                    });


                                                }
                                            });


                                } else {
                                    Log.i("feo", "Error getting documents: ", task.getException());
                                }
                            }

                        });



                    }
                });


        db.collection("Historial").whereEqualTo("Compra","IVA").whereEqualTo("MES",Calendar.getInstance().get(Calendar.MONTH) + 1).whereEqualTo("AÑO",Calendar.getInstance().get(Calendar.YEAR))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (!document.exists()) {

                                        } else {
                                            // TODO: SI HAY ERROR AQUÍ, ES PORQUE EN GETTRASANCTION SE GUARDA COMO STRING Y NO COMO DOUBLE. VERIFICAR ESO
                                            iva = document.getDouble("IVA") + iva;
                                            IVA.setText(String.valueOf(iva));

                                        }
                                    }
                                } else {
                                    Log.i("feo", "Error getting documents: ", task.getException());
                                }
                            }

                        });


                    }
                });
        db.collection("Historial").whereEqualTo("MES",Calendar.getInstance().get(Calendar.MONTH) + 1).whereEqualTo("AÑO",Calendar.getInstance().get(Calendar.YEAR))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {

                                        Log.i("CAPITAL",egresos + "E--" + "I" + ingresos);

                                    }

                                } else {
                                    Log.i("feo", "Error getting documents: ", task.getException());
                                }
                            }

                        });


                    }
                });









    }
    public static double getIVA(FirebaseFirestore db){

        db.collection("Historial").whereEqualTo("MES",Calendar.getInstance().get(Calendar.MONTH) + 1).whereEqualTo("AÑO",Calendar.getInstance().get(Calendar.YEAR))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (!document.exists()) {

                                        } else {
                                            // TODO: SI HAY ERROR AQUÍ, ES PORQUE EN GETTRASANCTION SE GUARDA COMO STRING Y NO COMO DOUBLE. VERIFICAR ESO
                                            iva = document.getDouble("IVA") + iva;

                                        }
                                    }
                                } else {
                                    Log.i("feo", "Error getting documents: ", task.getException());
                                }
                            }

                        });


                    }
                });
            Log.i("IVA",iva + "--");
        return iva;

    }




}
