package com.example.serma;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

 class Guarda {
    double ingreso = 0.0;
    double egresos = 0.0;
    double ingresoLocal = 0.0;
    double egresoLocal = 0.0;
    double deudaLocal = 0.0;
    double iva = 0.0;
    double ingre;
    double egre;
    double Deuda;
    HashMap<String, Object> latestData = new HashMap<>();
    DecimalFormat df = new DecimalFormat("####0.00");
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Date time = Calendar.getInstance().getTime();


    public Guarda(){


        db.collection("Historial")
                .whereEqualTo("MES", Calendar.getInstance().get(Calendar.MONTH) + 1).whereEqualTo("AÑO",Calendar.getInstance().get(Calendar.YEAR))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {

                        iva = 0.0;
                        egresoLocal = 0.0;
                        ingresoLocal = 0.0;
                        deudaLocal = 0.0;
                        if (e != null) {
                            Log.i("lA ESCUCHA FALLÓ","La escucha ha fallado xd");
                            return;
                        }
                        for (QueryDocumentSnapshot doc : value) {

                            if(doc.getString("tipo").equals("Egreso")){
                                if(doc.getDouble("total") != null){
                                    egresoLocal += doc.getDouble("total");
                                }
                                else{
                                    egresoLocal = 0.0;
                                }
                            }
                            if(doc.getString("tipo").equals("Venta") && doc.getDouble("IVA") != null){
                                iva += doc.getDouble("IVA");
                            }
                            if(doc.getString("tipo").equals("Venta")){
                                if(doc.getDouble("total") == null){
                                    ingresoLocal = 0.0;
                                }
                                else{
                                    ingresoLocal += doc.getDouble("total");
                                }


                            }

                            if(doc.getString("tipo").equals("Deuda")){
                                if(doc.getDouble("total") != null){
                                    deudaLocal += doc.getDouble("total");
                                }
                                else{
                                    deudaLocal = 0.0;
                                }

                            }


                        }

                        latestData.put("Egreso",egresoLocal);
                        latestData.put("Ingreso",ingresoLocal);
                        latestData.put("Deuda",deudaLocal);
                        latestData.put("IVA",iva);
                        latestData.put("AÑO",Calendar.getInstance().get(Calendar.YEAR));
                        latestData.put("MES",Calendar.getInstance().get(Calendar.MONTH) + 1);
                        latestData.put("DIA",Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                        latestData.put("HORA",Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
                        latestData.put("MINUTO",Calendar.getInstance().get(Calendar.MINUTE));
                        latestData.put("SEGUNDO",Calendar.getInstance().get(Calendar.SECOND));

                        db.collection("LatestData").document(time.toString()).set(latestData);


                    }
                });


    }

    public double getIva() {
        return iva;
    }
    public void updatelastDocumentWithDeudor(){


    }

    public void setIva(double iva) {
        this.iva = iva;
    }


    public double getIngreso() {
        return ingreso;
    }
    public void seteaIngresos(final TextView Ingreso, final TextView IVA){





        db.collection("LatestData").orderBy("AÑO",Query.Direction.DESCENDING).orderBy("MES",Query.Direction.DESCENDING).orderBy("DIA", Query.Direction.DESCENDING).orderBy("HORA", Query.Direction.DESCENDING).orderBy("MINUTO", Query.Direction.DESCENDING).orderBy("SEGUNDO", Query.Direction.DESCENDING).limit(1)
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

                                            ingreso = document.getDouble("Ingreso");
                                            iva = document.getDouble("IVA");

                                        }
                                    }
                                    IVA.setText(df.format(iva));
                                    Ingreso.setText(String.valueOf(df.format(ingreso)));
                                    Ingreso.setTextColor(ContextCompat.getColor(App.getAppContext(),R.color.verdeIngreso));
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
        db.collection("LatestData").orderBy("AÑO",Query.Direction.DESCENDING).orderBy("MES",Query.Direction.DESCENDING).orderBy("DIA", Query.Direction.DESCENDING).orderBy("HORA", Query.Direction.DESCENDING).orderBy("MINUTO", Query.Direction.DESCENDING).orderBy("SEGUNDO", Query.Direction.DESCENDING).limit(1)

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
                                            egresos = document.getDouble("Egreso");

                                        }
                                    }
                                    Log.i("DEBERÍA ESTAR GUARDADOO", egresos + "SIIIIIUUUUUU");
                                    Egresos.setText(df.format(egresos));
                                    Egresos.setTextColor(ContextCompat.getColor(App.getAppContext(),R.color.colorAccent));



                                } else {
                                    Log.i("feo", "Error getting documents: ", task.getException());
                                }
                            }

                        });
                    }
                });
    }

        public void seteaCapitalandEfectivo(final TextView Capital, final TextView Efectivo){
        egresoLocal = 0.0;
        ingresoLocal = 0.0;
        deudaLocal = 0.0;


            db.collection("LatestData").orderBy("AÑO",Query.Direction.DESCENDING).orderBy("MES",Query.Direction.DESCENDING).orderBy("DIA", Query.Direction.DESCENDING).orderBy("HORA", Query.Direction.DESCENDING).orderBy("MINUTO", Query.Direction.DESCENDING).orderBy("SEGUNDO", Query.Direction.DESCENDING).limit(1)

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
                                                egresoLocal = document.getDouble("Egreso");
                                                ingresoLocal = document.getDouble("Ingreso");
                                                deudaLocal = document.getDouble("Deuda");


                                            }
                                           Log.i("PRUEABSUQBSABS", document.getId());
                                        }
                                        double Capita = ingresoLocal - egresoLocal + deudaLocal;
                                        double Efecti = ingresoLocal - egresoLocal - deudaLocal;
                                        Log.i("PRUEBAAAAA", Capita + "");
                                        Capital.setText(df.format(Capita));
                                        Efectivo.setText(df.format(Efecti));


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

    public void saldarCuenta(String document){
        db.collection("Historial").document(document).update("objeto","Cuenta cobrada").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                App.showToast("¡Cuenta saldada exitosamente!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
            App.showToast("F xd");
                    }
                });
        db.collection("Historial").document(document).update("tipo","Venta").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                App.showToast("¡Cuenta saldada exitosamente!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        App.showToast("F xd");
                    }
                });

        db.collection("Deudor").document(document)
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

    }
    public void guardarCuentaPorCobrar(final Map<String, Object> deudor,TextView nombreDeudor ){
        db.collection("Deudor").document(nombreDeudor.getText().toString()).set(deudor);
        db.collection("Historial").document(nombreDeudor.getText().toString()).set(deudor);

 }
 public void getTotalDeuda(final TextView deuda){
     db.collection("LatestData").orderBy("AÑO",Query.Direction.DESCENDING).orderBy("MES",Query.Direction.DESCENDING).orderBy("DIA", Query.Direction.DESCENDING).orderBy("HORA", Query.Direction.DESCENDING).orderBy("MINUTO", Query.Direction.DESCENDING).orderBy("SEGUNDO", Query.Direction.DESCENDING).limit(1)

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
                                         if(document.getDouble("Deuda") == null){
                                             deuda.setText("No hay cuentas por cobrar");
                                         }
                                         else {
                                             try {
                                                 if (document.getDouble("Deuda") == null) {
                                                    throw new Exception();
                                                 }
                                                 deuda.setText("Total: " + document.getDouble("Deuda").toString());
                                             }
                                             catch (Exception e){
                                                 App.showToast("Lo siento, hubo un error :(");
                                             }
                                         }




                                     }
                                     Log.i("PRUEABSUQBSABS", document.getId());
                                 }



                             } else {
                                 Log.i("feo", "Error getting documents: ", task.getException());
                             }



                         }

                     });
                 }
             });


 }
 public void guardarNuevoObjeto(final String nombre2, final Map Object){
     db.collection("Objeto").document(nombre2)
             .set(Object)
             .addOnSuccessListener(new OnSuccessListener<Void>() {
                 @Override
                 public void onSuccess(Void aVoid) {

                    App.showToast("¡Cambios concretados!");
                 }
             })
             .addOnFailureListener(new OnFailureListener() {
                 @Override
                 public void onFailure(@NonNull Exception e) {
                     App.showToast("Algo ocurrió, probablemente el equipo ya exista :(");
                 }
             });
 }

    //todo: preguntar sobre las respuestas.

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
            db.collection("Historial").document(id)
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