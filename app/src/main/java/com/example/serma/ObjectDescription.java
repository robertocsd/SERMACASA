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
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Inventario = db.collection("Objeto");
    Map<String, Object> Object = new HashMap<>();
    EditText nombre, precioPublico, precioCompra, stockActual, stockIdeal, editID;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private View view;
    private String cat;
    String nombre2;
    String precioPublico2;
    String precioCompra2;
    Double precioCompra3;
    String StockAc2;
    int StockAc3;
    String stockID;
    int STOCKID3;
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

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                double iVA;
                try {

                    nombre2 = nombre.getText().toString();
                    switchIVA = view.findViewById(R.id.switchCalcularIVA);

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
                    if(switchIVA.isChecked()){
                        iVA = 0.00;
                    }
                    else {
                        iVA = Double.parseDouble(precioPublico.getText().toString()) * 0.13;
                    }


                    Object.put("Nombre", nombre2);
                    Object.put("PrecioAlPublico", Double.parseDouble(precioPublico.getText().toString()));
                    Object.put("PrecioDeCompra", precioCompra3);
                    Object.put("Stockactual", StockAc3);
                    Object.put("Stockideal", STOCKID3);
                    Object.put("ID", editID.getText().toString());
                    Object.put("IVA",iVA);
                    db.collection("Objeto").whereEqualTo("Nombre",nombre2)
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
                                                        Inventario.document(nombre2).set(Object);
                                                        Toast.makeText(App.getAppContext(), nombre2 + " ha sido guardado",
                                                                Toast.LENGTH_SHORT).show();

                                                    } else {
                                                        Toast.makeText(App.getAppContext(), "Lo siento, el equipo ya existe",
                                                                Toast.LENGTH_SHORT).show();


                                                    }
                                                }


                                            } else {
                                                Log.i("feo", "Error getting documents: ", task.getException());
                                            }

                                        }

                                    });
                                }
                            });



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
        // Create the AlertDialog object and return it


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
