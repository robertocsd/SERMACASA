package com.example.serma;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link muestraDeudores.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link muestraDeudores#newInstance} factory method to
 * create an instance of this fragment.
 */
public class muestraDeudores extends Fragment implements AdapterView.OnItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // TODO: Rename and change types of parameters
    ListView ArrayTO;
    private String mParam1;
    private String mParam2;
    ScrollView scrollDeudores;
    int contadoraClicks = 0;
    HashMap<String,String> resultado = new HashMap<>();
    ArrayList idList = new ArrayList();
    private OnFragmentInteractionListener mListener;
    SimpleAdapter adapters;
    public muestraDeudores() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment muestraDeudores.
     */
    // TODO: Rename and change types and number of parameters
    public static muestraDeudores newInstance(String param1, String param2) {
        muestraDeudores fragment = new muestraDeudores();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_muestra_deudores, container, false);
        final TextView totalDeudor = view.findViewById(R.id.textViewTOTALDEUDA);
        Guarda guar = new Guarda();
        guar.getTotalDeuda(totalDeudor);
        idList.clear();
        ArrayTO = view.findViewById(R.id.listViewDeudores);
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
                                        resultado.put(document.getString("Nombre"), document.getDouble("total").toString());
                                        idList.add(document.getId());

                                        List<HashMap<String, String>> listItems = new ArrayList<>();
                                        adapters = new SimpleAdapter(App.getAppContext(),listItems,R.layout.list_item,
                                                new String[]{"First line","Second line"},new int[]{R.id.text1,R.id.text2});
                                        Iterator it = resultado.entrySet().iterator();
                                        while(it.hasNext()){
                                            HashMap<String, String> resultsmap = new HashMap<>();
                                            Map.Entry pair = (Map.Entry)it.next();
                                            resultsmap.put("First line",pair.getKey().toString());
                                            resultsmap.put("Second line",pair.getValue().toString());
                                            resultsmap.put("third line",pair.getValue().toString());
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




        ArrayTO.setOnItemClickListener(this);

        // Inflate the layout for this fragment
        return view;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        contadoraClicks++;
        HashMap<String, String> clickedItem = (HashMap<String, String>) adapters.getItem(position);
        final String value = clickedItem.get("First line");
        final String key = clickedItem.get("Second line");
        Log.i("PRUEBA DE OBTENCIÍON",idList.get(position).toString());
        Handler handler =  new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(contadoraClicks == 1){

                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
                    alertDialog.setTitle("¿Saldar cuenta?");
                    alertDialog.setMessage("¿Deseas saldar la cuenta por cobrar de " + value);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getContext(), "De acuerdo, no saldaré nada",
                                            Toast.LENGTH_SHORT).show();

                                   // db.collection("Historial").document().set(transaccion);
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Guarda guarda = new Guarda();
                            guarda.saldarCuenta(idList.get(position).toString());
                            idList.remove(adapters.getItem(position));

                            Toast.makeText(getContext(), "Se ha saldado la cuenta por cobrar",
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            Intent myIntent = new Intent(getContext(), Cuentas.class);

                            //Optional parameters
                            getContext().startActivity(myIntent);

                        }
                    });
                    ;
                    alertDialog.show();
                }
                else if(contadoraClicks == 2){
                    AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
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
                            guar.borraCuentaPorCobrar(idList.get(position).toString());
                            dialog.dismiss();

                            Intent myIntent = new Intent(getContext(), Cuentas.class);
                            //Optional parameters
                            getContext().startActivity(myIntent);
                        }
                    });
                    ;
                    alertDialog.show();
                }
                contadoraClicks = 0;
            }
        },500);






    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void OpenFragment(Fragment nuevo){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.your_placeholder,nuevo);
        transaction.addToBackStack(null);
        transaction.commit();

    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
