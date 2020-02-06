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
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
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
    ArrayList objeto = new ArrayList();
    EditText totalTransaccion;
    String tipoTransaccion;
    String nombreObjecto;

    private OnFragmentInteractionListener mListener;

    public GetTransaction() {
        // Required empty public constructor
    }

    /**
     * Use this factory metho =d to create a new instance of
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
            objeto.clear();
            objeto = getArguments().getStringArrayList("equipos");
            tipoTransaccion = getArguments().getString("tipo");
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view;
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_get_transaction,null);
        Toast.makeText(getActivity(), nombreObjecto,
                Toast.LENGTH_SHORT).show();
        ScrollView scroll;
        spinnerDataList = new ArrayList();
        clienteDataList = new ArrayList();
        spinnerListTransaccion = new ArrayList();
        spinnerTipoTransaccion = view.findViewById(R.id.spinnerTipoTransaccion);
        totalTransaccion = view.findViewById(R.id.editTextTotalManual);


        if(tipoTransaccion.equals("Egreso")){
            spinnerListTransaccion.add("Egreso");
        }
        if(tipoTransaccion.equals("Venta")){
            spinnerListTransaccion.add("Venta");
        }




        spinnerAdapterListTransaccion = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,spinnerListTransaccion);
        spinnerTipoTransaccion.setAdapter(spinnerAdapterListTransaccion);
        final EditText total42 = view.findViewById(R.id.editTextTotalManual);

        builder.setTitle("Nueva transaccion");
        scroll =  view.findViewById(R.id.scrollView);
        final LinearLayout linear = view.findViewById(R.id.dynamic);
        linear.setOrientation(LinearLayout.VERTICAL);
        final EditText[] ed = new EditText[objeto.size()];
        final ArrayList ed2 = new ArrayList<>();
        final Button[] btn = new Button[objeto.size()];
        for (int i = 0; i < objeto.size(); i++) {
            final int j = i;
            ed[i] = new EditText(getContext());
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
                    bundle.putString("tipo",tipoTransaccion);
                    DialogFragment dlg = new GetTransaction();
                    dlg.setArguments(bundle);
                    dlg.show(getFragmentManager().beginTransaction(), "login");
                    getDialog().dismiss();


                }



            });

            btn[i].setText("Borrar " + objeto.get(i).toString());
            linear.addView(btn[i]);
        }




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
                if(objeto.isEmpty()){
                    try {
                        transaccion.put("descripcion", Descripcion.getText().toString());
                        transaccion.put("MES", Calendar.getInstance().get(Calendar.MONTH) + 1);
                        transaccion.put("AÑO", Calendar.getInstance().get(Calendar.YEAR));
                        transaccion.put("DIA", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                        transaccion.put("tipo", spinnerTipoTransaccion.getSelectedItem().toString());
                        if (totalTransaccion.getText().toString().isEmpty()) {
                            throw new Exception("El total no puede estar vacío para una transaccion sin equipos");
                        } else {
                            total = Double.parseDouble(totalTransaccion.getText().toString());
                            transaccion.put("total", total);
                        }
                        if (switchIVA.isChecked()) {
                            transaccion.put("IVA", total * 0.13);

                        }
                            if (Descripcion.getText().toString().trim().equals("")) {
                            throw new Exception("No puede estar vacía la descripción");
                        }
                        if (switchIVA.isChecked()) {
                            transaccion.put("IVA", total * 0.13);


                            AlertDialog alertDialog = new AlertDialog.Builder(builder.getContext()).create();
                            alertDialog.setTitle("¿Seguro");
                            alertDialog.setMessage("El total de " + spinnerTipoTransaccion.getSelectedItem().toString() + " es: " + total + " con un IVA de " + total * 0.13 + " es correcto?");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            db.collection("Historial").document().set(transaccion);
                                            Toast.makeText(builder.getContext(), "Transaccion almacenada!",
                                                    Toast.LENGTH_SHORT).show();



                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(builder.getContext(), "Se ha cancelado la transaccion!",
                                            Toast.LENGTH_SHORT).show();

                                    dialog.dismiss();
                                }
                            });
                            ;
                            alertDialog.show();


                        } else {
                            transaccion.put("IVA", 0.0);


                            AlertDialog alertDialog = new AlertDialog.Builder(builder.getContext()).create();
                            alertDialog.setTitle("¿Seguro");
                            alertDialog.setMessage("El total de " + spinnerTipoTransaccion.getSelectedItem().toString() + " es: " + total + ", sin iva. ¿Es correcto?");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(builder.getContext(), "Transaccion almacenada!",
                                                    Toast.LENGTH_SHORT).show();

                                            db.collection("Historial").document().set(transaccion);
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(builder.getContext(), "Se ha cancelado la transaccion!",
                                            Toast.LENGTH_SHORT).show();

                                    dialog.dismiss();
                                }
                            });
                            ;
                            alertDialog.show();

                        }



                    }
                    catch (Exception E){
                        Toast.makeText(getActivity(), E.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                }
                else {

                    try {
                        for (int i = 0; i < ed.length; i++) {
                            ed2.add(ed[i].getText().toString());
                            final int j = i;
                            if (ed[i].getText().toString().trim().equals("")) {
                                throw new Exception("Alguno de los campos estaba vacío, así que no añadí la transacción.");
                            }
                            if (spinnerTipoTransaccion.getSelectedItem().toString() == "Egreso") {
                                Log.i("AGREGADOS: ", ed[i].getText().toString() + i);
                                db.collection("Objeto").whereEqualTo("Nombre", objeto.get(i).toString())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        Log.i("DOCUMENT", document.getDouble("PrecioAlPublico") * Double.parseDouble(ed[j].getText().toString()) + dateFormat.format(date) + "");


                                                        double efe = document.getDouble("Stockactual");
                                                        Log.i("efe", "DocumentSnapshot successfully updated!" + (efe + Double.parseDouble(ed[j].getText().toString())));

                                                        DocumentReference washingtonRef = db.collection("Objeto").document(objeto.get(j).toString());
                                                        washingtonRef
                                                                .update("Stockactual", efe + Double.parseDouble(ed[j].getText().toString()))
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
                                                    }
                                                } else {
                                                    Log.d("feo", "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });

                            }
                            if (spinnerTipoTransaccion.getSelectedItem().toString() == "Venta") {
                                db.collection("Objeto").whereEqualTo("Nombre", objeto.get(j).toString())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        Log.i("DOCUMENT", document.getDouble("PrecioAlPublico") * Double.parseDouble(ed[j].getText().toString()) + dateFormat.format(date) + "");
/*
                                                    transaccion.put("MES", Calendar.getInstance().get(Calendar.MONTH) + 1);
                                                    transaccion.put("AÑO", Calendar.getInstance().get(Calendar.YEAR));
                                                    transaccion.put("DIA", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));


                                                    if (switchIVA.isChecked() == true) {
                                                        transaccion.put("IVA", document.getDouble("PrecioAlPublico") * 0.13);
                                                    }
                                                    */
                                                        double efe = document.getDouble("Stockactual");

                                                        Log.i("efe", "DocumentSnapshot successfully updated!" + (efe - Double.parseDouble(ed[j].getText().toString())));

                                                        DocumentReference washingtonRef = db.collection("Objeto").document(objeto.get(j).toString());
                                                        washingtonRef
                                                                .update("Stockactual", efe - Double.parseDouble(ed[j].getText().toString()))
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
                                                    }
                                                } else {
                                                    Log.d("feo", "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });

                            }
                            if (spinnerTipoTransaccion.getSelectedItem().toString() == "Ingreso de producto") {
                                db.collection("Objeto").whereEqualTo("Nombre", objeto.get(j).toString())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        Log.i("DOCUMENT", document.getDouble("PrecioAlPublico") * Double.parseDouble(ed[j].getText().toString()) + dateFormat.format(date) + "");
/*
                                                    transaccion.put("MES", Calendar.getInstance().get(Calendar.MONTH) + 1);
                                                    transaccion.put("AÑO", Calendar.getInstance().get(Calendar.YEAR));
                                                    transaccion.put("DIA", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));


                                                    if (switchIVA.isChecked() == true) {
                                                        transaccion.put("IVA", document.getDouble("PrecioAlPublico") * 0.13);
                                                    }
                                                    */
                                                        double efe = document.getDouble("Stockactual");

                                                        DocumentReference washingtonRef = db.collection("Objeto").document(objeto.get(j).toString());
                                                        washingtonRef
                                                                .update("Stockactual", efe + Double.parseDouble(ed[j].getText().toString()))
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
                                                    }
                                                } else {
                                                    Log.d("feo", "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });

                            }

                        }

                        transaccion.put("objeto", objeto);
                        transaccion.put("cliente", Cliente.getText().toString());
                        transaccion.put("cantidad", ed2);
                        transaccion.put("tipo", spinnerTipoTransaccion.getSelectedItem().toString());
                        if (Descripcion.getText().toString().trim().equals("")) {
                            throw new Exception("No puede estar vacía la descripción");
                        }

                        transaccion.put("descripcion", Descripcion.getText().toString());
                        transaccion.put("MES", Calendar.getInstance().get(Calendar.MONTH) + 1);
                        transaccion.put("AÑO", Calendar.getInstance().get(Calendar.YEAR));
                        transaccion.put("DIA", Calendar.getInstance().get(Calendar.DAY_OF_MONTH));


                        for (int i = 0; i < ed.length; i++) {
                            final int j = i;
                            db.collection("Objeto").whereEqualTo("Nombre", objeto.get(i).toString())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    total += (document.getDouble("PrecioAlPublico") * Double.parseDouble(ed[j].getText().toString()));
                                                }
                                            } else {
                                                Log.d("feo", "Error getting documents: ", task.getException());
                                            }
                                            if (j == (ed.length - 1)) {
                                                //TODO: GENERAL.
                                                Log.i("TOAAAL", total + "");
                                                if (totalTransaccion.getText().toString().isEmpty()) {
                                                    transaccion.put("total", total);
                                                } else {
                                                    total = Double.parseDouble(totalTransaccion.getText().toString());
                                                    transaccion.put("total", total);
                                                }
                                                if (switchIVA.isChecked()) {
                                                    transaccion.put("IVA", total * 0.13);


                                                    AlertDialog alertDialog = new AlertDialog.Builder(builder.getContext()).create();
                                                    alertDialog.setTitle("¿Seguro");
                                                    alertDialog.setMessage("El total de " + spinnerTipoTransaccion.getSelectedItem().toString() + " es: " + total + " con un IVA de " + total * 0.13 + " es correcto?");
                                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    Toast.makeText(builder.getContext(), "Transaccion almacenada!",
                                                                            Toast.LENGTH_SHORT).show();

                                                                    db.collection("Historial").document().set(transaccion);
                                                                    dialog.dismiss();
                                                                }
                                                            });
                                                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Toast.makeText(builder.getContext(), "Se ha cancelado la transaccion!",
                                                                    Toast.LENGTH_SHORT).show();

                                                            dialog.dismiss();
                                                        }
                                                    });
                                                    ;
                                                    alertDialog.show();


                                                } else {
                                                    transaccion.put("IVA", 0.0);


                                                    AlertDialog alertDialog = new AlertDialog.Builder(builder.getContext()).create();
                                                    alertDialog.setTitle("¿Seguro");
                                                    alertDialog.setMessage("El total de " + spinnerTipoTransaccion.getSelectedItem().toString() + " es: " + total + ", sin iva. ¿Es correcto?");
                                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    Toast.makeText(builder.getContext(), "Transaccion almacenada!",
                                                                            Toast.LENGTH_SHORT).show();

                                                                    db.collection("Historial").document().set(transaccion);
                                                                    dialog.dismiss();
                                                                }
                                                            });
                                                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Toast.makeText(builder.getContext(), "Se ha cancelado la transaccion!",
                                                                    Toast.LENGTH_SHORT).show();

                                                            dialog.dismiss();
                                                        }
                                                    });
                                                    ;
                                                    alertDialog.show();

                                                }
                                            }
                                        }
                                    });
                        }


                        // Create the AlertDialog object and return it

                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
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
