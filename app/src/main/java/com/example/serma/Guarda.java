package com.example.serma;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.Calendar;
public class Guarda {
    double ingreso = 0.0;
    double egresos = 0.0;
    double ingresoLocal = 0.0;
    double egresoLocal = 0.0;
    double iva = 0.0;
    DecimalFormat df = new DecimalFormat("####0.00");
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public Guarda(){


    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getIngreso() {
        return ingreso;
    }
    public void seteaIngresos(final TextView Ingreso, final TextView IVA){
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
                                            ingreso = 0.0;

                                        } else {


                                            ingreso +=  document.getDouble("total");
                                            iva += document.getDouble("IVA");

                                        }
                                    }
                                    IVA.setText(df.format(iva));
                                    Ingreso.setText(String.valueOf(df.format(ingreso)));
                                    Log.i("Ingresó el ingreso xddd", ingreso + "");

                                } else {
                                    Log.i("feo", "Error getting documents: ", task.getException());
                                }
                            }

                        });
                    }
                });
    }
    public void seteaEgresos(final TextView Egresos) {
        db.collection("Historial").whereEqualTo("tipo", "Egreso").whereEqualTo("MES", Calendar.getInstance().get(Calendar.MONTH) + 1).whereEqualTo("AÑO", Calendar.getInstance().get(Calendar.YEAR))
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
                                            egresos = 0.0;

                                        } else {
                                            egresos += document.getDouble("total");

                                        }
                                    }
                                    Log.i("DEBERÍA ESTAR GUARDADOO", egresos + "SIIIIIUUUUUU");
                                    Egresos.setText(df.format(egresos));


                                } else {
                                    Log.i("feo", "Error getting documents: ", task.getException());
                                }
                            }

                        });
                    }
                });

    }

        public void seteaCapitalandEfectivo(final TextView Capital, final TextView Efectivo){

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
                                                ingresoLocal = 0.0;

                                            } else {


                                                ingresoLocal +=  document.getDouble("total");


                                            }
                                        }


                                    } else {
                                        Log.i("feo", "Error getting documents: ", task.getException());
                                    }
                                }

                            });
                        }
                    });
            db.collection("Historial").whereEqualTo("tipo", "Egreso").whereEqualTo("MES", Calendar.getInstance().get(Calendar.MONTH) + 1).whereEqualTo("AÑO", Calendar.getInstance().get(Calendar.YEAR))
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
                                                egresoLocal = 0.0;

                                            } else {
                                                egresoLocal += document.getDouble("total");

                                            }
                                        }
                                        double Capita = ingresoLocal - egresoLocal;
                                        Capital.setText(df.format(Capita));
                                        Efectivo.setText(df.format(Capita - egresoLocal));

                                    } else {
                                        Log.i("feo", "Error getting documents: ", task.getException());
                                    }

                                }

                            });
                        }
                    });


        }


    public void setIngreso(double ingreso) {


        this.ingreso = ingreso;
    }

    public boolean borraCuentaPorCobrar(String id){
        try {

            db.collection("Deudor").document(id)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            App.showToast("Si se pudoooo");

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(App.getAppContext(), "No se logró eliminar, pero tranquilo, no es tu culpa",
                                    Toast.LENGTH_LONG).show();

                        }
                    });
            return true;
        }
        catch (Exception e){
            return false;
        }

    }
    public void getCapital(){

    }

}