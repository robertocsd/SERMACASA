package com.example.serma.models;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Objeto {
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Cliente = db.collection("categoria");
    public static double ingreso;


    public Objeto() {

    }

     double getIngresos(){

        db.collection("Historial").whereEqualTo("tipo","Venta")
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
                                            ingreso =  document.getDouble("total") ;
                                        }
                                    }


                                } else {
                                    Log.i("feo", "Error getting documents: ", task.getException());
                                }
                            }

                        });
                    }
                });

        return ingreso;
    }
    public static double getEgresos(){
        return 0.0;
    }

    public static double getIva(){
        return 0.0;
    }



}
