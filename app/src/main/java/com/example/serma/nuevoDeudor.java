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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link nuevoDeudor.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link nuevoDeudor#newInstance} factory method to
 * create an instance of this fragment.
 */
public class nuevoDeudor extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Map<String, Object> deudor = new HashMap<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public nuevoDeudor() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment nuevoDeudor.
     */
    // TODO: Rename and change types and number of parameters
    public static nuevoDeudor newInstance(String param1, String param2) {
        nuevoDeudor fragment = new nuevoDeudor();
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

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_nuevo_deudor, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final EditText nombreDeudor = view.findViewById(R.id.editTextNombreDeudor);
        final EditText cantidadDeudor = view.findViewById(R.id.editTextCantidadDeudor);

        builder.setTitle("Nuevo cuenta por cobrar");

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int id) {
                try {
                    deudor.put("Nombre",nombreDeudor.getText().toString());
                    deudor.put("Cantidad",Double.parseDouble(cantidadDeudor.getText().toString()));


                    db.collection("Deudor").document().set(deudor);
                    //TODO: VERIFICAR QUE NO SE PUEDAN AGREGAR MÁS DEUDORES CON EL MISMO NOMBRE
                    Toast.makeText(getContext(), nombreDeudor.getText().toString() + " ha sido guardado", Toast.LENGTH_SHORT).show();



                }catch (Exception e){
                    Toast.makeText(getActivity(), "Ocurrió algo pero no te preocupes, no es tu culpa, a menos que hayas dejado vacío algo.",
                            Toast.LENGTH_LONG).show();
                    Log.i("Exception",e.getMessage());
                }


            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        // Create the AlertDialog object and return it


        builder.setView(view);

        return builder.create();




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nuevo_deudor, container, false);
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
