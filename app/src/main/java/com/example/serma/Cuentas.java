package com.example.serma;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Cuentas extends AppCompatActivity implements nuevoDeudor.OnFragmentInteractionListener,muestraDeudores.OnFragmentInteractionListener,BlankFragment.OnFragmentInteractionListener {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Cliente = db.collection("categoria");


    ArrayList todacate = new ArrayList();
    double totalIngresos = 0.0;
    double totalEgresos = 0.0;
    double totalIVA = 0.0;
    Objeto objeto = new Objeto();
    double ingresos = 0.0;

    double egresos = 0.0;
    double iva = 0.0;
    double capital = 0.0;
    double deudas = 0.0;
    double efectivo = 0.0;

    Date time = Calendar.getInstance().getTime();
    DecimalFormat df = new DecimalFormat("####0.00");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuentas);

        Fragment frag = new BlankFragment();
        OpenFragment(frag);





        //SACA EL EGRESO DIRECTAMENTE DEL HISTORIAL DE LAS TRANSACCIONES.



/*
        db.collection("Historial").whereEqualTo("tipo","Egreso").whereEqualTo("MES",Calendar.getInstance().get(Calendar.MONTH) + 1).whereEqualTo("AÑO",Calendar.getInstance().get(Calendar.YEAR))
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

                                                                        iva = document.getDouble("IVA") + iva;
                                                                        Ingresos.setText(df.format(ingresos));
                                                                    }
                                                                }
                                                                Log.i("EGRESOOOOOOOOOSSSSS",egresos + "---" +ingresos + "---" + iva + "EGRESOOOOOS");
                                                                //TODO HACER CUENTAS POR COBRAR AQUI DEBE IR SU CALCULO.
                                                                db.collection("Deudor")
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
                                                                                                   deudas =  document.getDouble("Cantidad") + deudas;
                                                                                                }
                                                                                            }
                                                                                            Log.i("EGRESOOOOOOOOOSSSSS",deudas + "DEUDAAAAAS");
                                                                                            //TODO HACER CUENTAS POR COBRAR AQUI DEBE IR SU CALCULO.
                                                                                            Log.i("EGRESOOOOOOOOOSSSSS",deudas + "DEUDAAAAAS 222222");
                                                                                            capital = (ingresos+deudas)-egresos;
                                                                                            IVA.setText(df.format(iva));
                                                                                            efectivo = capital-egresos-deudas;
                                                                                            Efectivo.setText(df.format(efectivo));
                                                                                            Capital.setText(String.valueOf(df.format(capital)));
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
                                } else {
                                    Log.i("feo", "Error getting documents: ", task.getException());
                                }
                            }

                        });



                    }
                });*/




/*
        final ArrayList ed2 = new ArrayList<>();
        final Button[] btn = new Button[objeto.size()];
        for (int i = 0; i < objeto.size(); i++) {
            final int j = i;
            ed[i] = new EditText(getBaseContext());
            ed[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT));
            ed[i].setHint("Ingresa la cantidad para " + objeto.get(i));
            ed[i].setInputType(InputType.TYPE_CLASS_NUMBER);
            linear.addView(ed[i]);
            btn[i] = new Button(getContext());
            btn[i].setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            btn[i].setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),"Presionaste el boton de " + objeto.get(j),
                            Toast.LENGTH_SHORT).show();
                    objeto.remove(j);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("equipos",objeto);
                    DialogFragment dlg = new GetTransaction();
                    dlg.setArguments(bundle);
                    dlg.show(getFragmentManager().beginTransaction(), "login");
                    getDialog().dismiss();


                }



            });

            btn[i].setText("Borrar " + objeto.get(i).toString());
            linear.addView(btn[i]);
        }
*/








    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    public void OpenFragment(Fragment nuevo){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.your_placeholder,nuevo);
        transaction.addToBackStack(null);
        transaction.commit();

    }


}
