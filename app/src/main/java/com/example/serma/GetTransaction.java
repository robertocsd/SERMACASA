package com.example.serma;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GetTransaction.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GetTransaction#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GetTransaction extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    DecimalFormat df = new DecimalFormat("####0.00");



    ArrayList spinnerDataList;
    ArrayList clienteDataList;


    EditText CantidadMáxima;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    Date date = new Date();
    Map<String, Object> transaccion = new HashMap<>();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Spinner object;
    Spinner spinnerTipoTransaccion;
    ArrayList spinnerListTransaccion;
    ArrayAdapter spinnerAdapterListTransaccion;
    Switch switchIVA;

    String nombreObjecto;

    private OnFragmentInteractionListener mListener;

    public GetTransaction() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GetTransaction.
     */
    // TODO: Rename and change types and number of parameters
    public static GetTransaction newInstance(String param1, String param2) {
        GetTransaction fragment = new GetTransaction();
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
            nombreObjecto = getArguments().get("nombre").toString();
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view;
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_get_transaction,null);
        Toast.makeText(getActivity(), nombreObjecto,
                Toast.LENGTH_SHORT).show();
        spinnerDataList = new ArrayList();
        clienteDataList = new ArrayList();
        spinnerListTransaccion = new ArrayList();
        spinnerTipoTransaccion = view.findViewById(R.id.spinnerTipoTransaccion);
        spinnerListTransaccion.add("Compra");
        spinnerListTransaccion.add("Venta");
        spinnerAdapterListTransaccion = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,spinnerListTransaccion);
        spinnerTipoTransaccion.setAdapter(spinnerAdapterListTransaccion);
        final EditText total42 = view.findViewById(R.id.editTextTotal);

        builder.setTitle("Nueva transaccion");

        CantidadMáxima = view.findViewById(R.id.editTextCantidad);

      /*  db.collection("categoria")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<QuerySnapshot> task) {


                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                spinnerDataList.add(document.getString("Nombre"));

                                adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerDataList);

                                adapter.notifyDataSetChanged();
                                spinnerCategoria.setAdapter(adapter);

                            }
                        } else {
                            Log.d("feo", "Error getting documents: ", task.getException());
                        }
                    }
                });*/

      switchIVA = view.findViewById(R.id.switchIVA);
      final EditText Cliente = view.findViewById(R.id.editTextCliente);
      final EditText Descripcion = view.findViewById(R.id.editTextDescripcion);

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            double total;

            public void onClick(DialogInterface dialog, int id) {
                try {


                    if (CantidadMáxima.getText().toString().trim().equals("")) {
                        throw new Exception("Alguno de los campos estaba vacío, así que no añadí la transacción.");
                    }
                    ;


                    if (spinnerTipoTransaccion.getSelectedItem().toString() == "Compra") {

                        transaccion.put("objeto", nombreObjecto);
                        transaccion.put("cliente", Cliente.getText().toString());
                        transaccion.put("cantidad", Double.parseDouble(CantidadMáxima.getText().toString()));
                        transaccion.put("tipo", spinnerTipoTransaccion.getSelectedItem().toString());
                        transaccion.put("descripcion", Descripcion.getText().toString());


                        db.collection("Objeto").whereEqualTo("Nombre", nombreObjecto)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.i("DOCUMENT", document.getDouble("PrecioAlPublico") * Double.parseDouble(CantidadMáxima.getText().toString()) + dateFormat.format(date) + "");
                                                if(total42.getText().toString().isEmpty()) {
                                                    transaccion.put("total", document.getDouble("PrecioAlPublico") * Double.parseDouble(CantidadMáxima.getText().toString()));

                                                }
                                                else{
                                                    transaccion.put("total", Double.parseDouble(total42.getText().toString()));
                                                }
                                                transaccion.put("MES", Calendar.getInstance().get(Calendar.MONTH) + 1);
                                                transaccion.put("AÑO", Calendar.getInstance().get(Calendar.YEAR));
                                                transaccion.put("DIA", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

                                                double efe = document.getDouble("Stockactual");
                                                if (switchIVA.isChecked() == true) {
                                                    transaccion.put("IVA", document.getDouble("PrecioAlPublico") * 0.13);
                                                }
                                                else{
                                                    transaccion.put("IVA", 0.0);
                                                }

                                                Log.i("efe", "DocumentSnapshot successfully updated!" + (efe + Double.parseDouble(CantidadMáxima.getText().toString())));

                                                DocumentReference washingtonRef = db.collection("Objeto").document(nombreObjecto);
                                                washingtonRef
                                                        .update("Stockactual", efe + Double.parseDouble(CantidadMáxima.getText().toString()))
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.i("efe", "DocumentSnapshot successfully updated!");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w("efe", "Error updating document", e);
                                                            }
                                                        });
                                                db.collection("Historial").document().set(transaccion);
                                            }
                                        } else {
                                            Log.d("feo", "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                        Toast.makeText(getActivity(), "La transaccion se guardó",
                                Toast.LENGTH_SHORT).show();

                    } else {

                        transaccion.put("objeto", nombreObjecto);
                        transaccion.put("cliente", Cliente.getText().toString());
                        transaccion.put("cantidad", Double.parseDouble(CantidadMáxima.getText().toString()));
                        transaccion.put("tipo", spinnerTipoTransaccion.getSelectedItem().toString());
                        transaccion.put("descripcion", Descripcion.getText().toString());
                        db.collection("Objeto").whereEqualTo("Nombre", nombreObjecto)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.i("DOCUMENT", document.getDouble("PrecioAlPublico") * Double.parseDouble(CantidadMáxima.getText().toString()) + dateFormat.format(date) + "");

                                                if(total42.getText().toString().isEmpty()) {
                                                    transaccion.put("total", document.getDouble("PrecioAlPublico") * Double.parseDouble(CantidadMáxima.getText().toString()));

                                                }
                                                else{
                                                    transaccion.put("total", total42.getText().toString());
                                                }

                                                transaccion.put("MES", Calendar.getInstance().get(Calendar.MONTH) + 1);
                                                transaccion.put("AÑO", Calendar.getInstance().get(Calendar.YEAR));
                                                transaccion.put("DIA", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

                                                double efe = document.getDouble("Stockactual");
                                                if (switchIVA.isChecked() == true) {
                                                    transaccion.put("IVA", document.getDouble("PrecioAlPublico") * 0.13);
                                                }

                                                Log.i("efe", "DocumentSnapshot successfully updated!" + (efe - Double.parseDouble(CantidadMáxima.getText().toString())));

                                                DocumentReference washingtonRef = db.collection("Objeto").document(nombreObjecto);
                                                washingtonRef
                                                        .update("Stockactual", efe + Double.parseDouble(CantidadMáxima.getText().toString()))
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                Log.i("efe", "DocumentSnapshot successfully updated!");
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w("efe", "Error updating document", e);
                                                            }
                                                        });
                                                db.collection("Historial").document().set(transaccion);
                                            }
                                        } else {
                                            Log.d("feo", "Error getting documents: ", task.getException());
                                        }
                                    }
                                });
                        Toast.makeText(getActivity(), "La transaccion se guardó",
                                Toast.LENGTH_SHORT).show();
                    }


                    // Create the AlertDialog object and return it

                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Se canceló la transaccion",
                        Toast.LENGTH_SHORT).show();


            }
        });
        builder.setView(view);
        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_get_transaction, container, false);
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
