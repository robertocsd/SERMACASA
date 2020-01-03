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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

public class Cuentas extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Cliente = db.collection("categoria");

    ArrayList todacate = new ArrayList();
    double totalIngresos = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuentas);
        final TextView Ingresos = findViewById(R.id.textViewTotalIngresos);


        db.collection("Historial").whereEqualTo("tipo","Venta").whereEqualTo("MES",Calendar.getInstance().get(Calendar.MONTH) + 2).whereEqualTo("AÑO",Calendar.getInstance().get(Calendar.YEAR))
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
                                            Ingresos.setText("0.0");
                                        } else {

                                            totalIngresos = document.getDouble("total") + totalIngresos;
                                            Ingresos.setText(String.valueOf(totalIngresos));
                                        }
                                    }

                                } else {
                                    Log.i("feo", "Error getting documents: ", task.getException());
                                }
                            }

                        });


                    }
                });



    }




}
