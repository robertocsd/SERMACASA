package com.example.serma;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.serma.models.Categoria;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ObjectDescription.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ObjectDescription#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ObjectDescription extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public CollectionReference Inventario = db.collection("Objeto");
    Map<String, Object> Object = new HashMap<>();
    EditText nombre, precioPublico, precioCompra, stockActual, stockIdeal, editID;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private View view;

    String nombre2;
    String precioPublico2;
    String precioCompra2;
    Double precioCompra3;
    String StockAc2;
    int StockAc3;
    String stockID;
    int STOCKID3;
    String tipo;
    String nombreUpdate;
    Switch switchIVA;
    private OnFragmentInteractionListener mListener;

    public ObjectDescription() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment ObjectDescription.
     */
    // TODO: Rename and change types and number of parameters
    public static ObjectDescription newInstance(String param1) {
        ObjectDescription fragment = new ObjectDescription();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tipo = getArguments().get("tipo").toString();
            if(tipo.equals("update")){
                nombreUpdate = getArguments().get("nombre").toString();
            }



        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_object_description, container, false);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }

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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_object_description, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Introduce el nuevo equipo");
        nombre = view.findViewById(R.id.editTextObjectName);
        precioPublico = view.findViewById(R.id.editTextPrecioPublico);
        precioCompra = view.findViewById(R.id.editTextPrecioCompra);
        editID = view.findViewById(R.id.editTextIDObject);
        stockActual = view.findViewById(R.id.editTextStockInicial);
        stockIdeal = view.findViewById(R.id.editTextStockIdeal);
        nombre2 = nombre.getText().toString();
        if(tipo.equals("update")){
            nombre.setVisibility(View.GONE);
            builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    double iVA;
                    try {




                        if(!editID.getText().toString().trim().equals("")){
                            db.collection("Objeto").document(nombreUpdate).update("ID",editID.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            App.showToast("F xd");
                                        }
                                    });
                        }

                        precioPublico2 = precioPublico.getText().toString();
                        if(!precioPublico2.trim().equals("")){
                            db.collection("Objeto").document(nombreUpdate).update("PrecioAlPublico",Double.parseDouble(precioPublico2)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            App.showToast("F xd");
                                        }
                                    });
                        }

                        precioCompra2 = precioCompra.getText().toString();
                        precioCompra3 = Double.parseDouble(precioCompra2);
                        if(!precioCompra2.trim().equals("")){
                            db.collection("Objeto").document(nombreUpdate).update("PrecioDeCompra",precioCompra3).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            App.showToast("F xd");
                                        }
                                    });
                        }
                        StockAc2 = stockActual.getText().toString();
                        StockAc3 = Integer.parseInt(StockAc2);
                        if(!StockAc2.trim().equals("")){
                            db.collection("Objeto").document(nombreUpdate).update("Stockactual",StockAc3).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            App.showToast("F xd");
                                        }
                                    });
                        }
                        stockID = stockIdeal.getText().toString();
                        STOCKID3 = Integer.parseInt(stockID);
                        if(!stockID.trim().equals("")){
                            db.collection("Objeto").document(nombreUpdate).update("Stockideal",STOCKID3).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            App.showToast("F xd");
                                        }
                                    });
                        }


                    } catch (Exception e) {
                        Toast.makeText(getActivity(),  e.getMessage(),
                                Toast.LENGTH_LONG).show();

                    }
                    App.showToast("Producto actualizado con éxito");
                }

            })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

        }
         if(tipo.equals("create")){
            builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    try {

                        nombre2 = nombre.getText().toString();

                        if(nombre2.trim().equals("")){
                            throw new Exception("El nombre no puede estar vacío");
                        }
                        if(editID.getText().toString().trim().equals("")){
                            throw new Exception("El codigo del producto no puede estar vacío");
                        }

                        precioPublico2 = precioPublico.getText().toString();
                        if(precioPublico2.trim().equals("")){
                            throw new Exception("El precio al publico no puede estar vacío");
                        }

                        precioCompra2 = precioCompra.getText().toString();
                        precioCompra3 = Double.parseDouble(precioCompra2);
                        if(precioCompra2.trim().equals("")){
                            throw new Exception("El precio de compra no puede estar vacio");
                        }
                        StockAc2 = stockActual.getText().toString();
                        StockAc3 = Integer.parseInt(StockAc2);
                        if(StockAc2.trim().equals("")){
                            throw new Exception("El stock actual no puede estar vacío");
                        }
                        stockID = stockIdeal.getText().toString();
                        STOCKID3 = Integer.parseInt(stockID);
                        if(stockID.trim().equals("")){
                            throw new Exception("El stock ideal no puede estar vacío");
                        }

                        Object.put("Nombre", nombre2);
                        Object.put("PrecioAlPublico", Double.parseDouble(precioPublico.getText().toString()));
                        Object.put("PrecioDeCompra", precioCompra3);
                        Object.put("Stockactual", StockAc3);
                        Object.put("Stockideal", STOCKID3);
                        Object.put("ID", editID.getText().toString());
                        App.showToast(  "Guardando...");
                        Guarda guar = new Guarda();
                        guar.guardarNuevoObjeto(nombre2,Object);


                    } catch (Exception e) {
                        Toast.makeText(getActivity(),  e.getMessage(),
                                Toast.LENGTH_LONG).show();

                    }
                }

            })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });

        }
        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
