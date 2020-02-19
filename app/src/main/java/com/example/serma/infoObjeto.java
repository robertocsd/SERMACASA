package com.example.serma;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link infoObjeto.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link infoObjeto#newInstance} factory method to
 * create an instance of this fragment.
 */
public class infoObjeto extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView Nombre,PrecioP,PrecioC,StockA,StockI,Categoria,iva;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Cliente = db.collection("categoria");
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String nombre;
//TODO: TRANSACCION NO SE BORRA Y SE QUEDA AHÍ MIMSO.
    private OnFragmentInteractionListener mListener;

    public infoObjeto() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment infoObjeto.
     */
    // TODO: Rename and change types and number of parameters
    public static infoObjeto newInstance(String param1, String param2) {
        infoObjeto fragment = new infoObjeto();
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
            nombre = getArguments().get("Nombre").toString();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_info_objeto, container, false);
        Nombre = layout.findViewById(R.id.textViewNombre);
        PrecioP = layout.findViewById(R.id.info1);
        PrecioC = layout.findViewById(R.id.info2);
        StockA = layout.findViewById(R.id.info3);
        StockI = layout.findViewById(R.id.info4);
        Categoria = layout.findViewById(R.id.info5);
        iva = layout.findViewById(R.id.info6);
        Button transaccion = layout.findViewById(R.id.buttonVenta);
        Nombre.setText(nombre);
        try {
            if (Nombre.getText().toString().trim().equals("")) {
                throw new Exception("Ocurrio algo, puede que el objeto ya exista");
            }

            final DocumentReference docRef = db.collection("Objeto").document(nombre);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            PrecioP.setText("Precio al público: " + document.getDouble("PrecioAlPublico").toString());
                            PrecioC.setText("Precio al que se compró: " + document.getDouble("PrecioDeCompra").toString());
                            StockA.setText("Stock actual: " + document.getDouble("Stockactual").toString());
                            StockI.setText("Stock ideal: " + document.getDouble("Stockideal").toString());
                            Categoria.setText("Categoria: " + document.getString("ID"));
                            if(document.getDouble("IVA") == null || document.getDouble("IVA") == 0.00 ){
                                iva.setText("IVA: No se le añadió IVA");
                            }
                            else{
                                iva.setText("IVA: "+ document.getDouble("IVA").toString());
                            }

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }catch (Exception e){

        }
        transaccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dlg = new ObjectDescription();
                Bundle bundle = new Bundle();
                bundle.putString("nombre",nombre);
                bundle.putString("tipo","update");
                dlg.setArguments(bundle);
                dlg.show(getFragmentManager().beginTransaction(), "login");
                }
            });


        return layout;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public void OpenFragment(Fragment nuevo){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.your_placeholder,nuevo);
        transaction.addToBackStack(null);
        transaction.commit();

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
