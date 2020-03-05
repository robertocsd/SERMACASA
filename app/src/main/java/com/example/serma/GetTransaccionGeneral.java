package com.example.serma;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GetTransaccionGeneral.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GetTransaccionGeneral#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GetTransaccionGeneral extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    DecimalFormat df = new DecimalFormat("####0.00");

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    Date date = new Date();


    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Button inventario;
    Button cuentas;
    Button clientes;
    FloatingActionButton Venta;
    Button newActivity;
    Button newCuenta;
    FloatingActionButton Egreso;



    private OnFragmentInteractionListener mListener;

    public GetTransaccionGeneral() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GetTransaccionGeneral.
     */
    // TODO: Rename and change types and number of parameters
    public static GetTransaccionGeneral newInstance(String param1, String param2) {
        GetTransaccionGeneral fragment = new GetTransaccionGeneral();
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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_menu, container, false);
        inventario = view.findViewById(R.id.buttoninventario);
        cuentas = view.findViewById(R.id.buttonMovimientos);
        clientes = view.findViewById(R.id.buttonClientes);
        Venta = view.findViewById(R.id.floatingActionButtonVenta);
        Egreso = view.findViewById(R.id.floatingActionButtonEgreso);

        inventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("tipo","Inventario");
                Fragment dlg = new Inventario();
                dlg.setArguments(bundle);
                OpenFragment(dlg);

            }
        });
        cuentas.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Bundle bundle = new Bundle();
                Fragment dlg = new Money();
                dlg.setArguments(bundle);
                OpenFragment(dlg);

            }
        });

        clientes.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Bundle bundle = new Bundle();
                Fragment dlg = new Cliente();
                OpenFragment(dlg);

            }
        });
        Venta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Bundle bundle = new Bundle();
                bundle.putString("tipo","Venta");
                Fragment dlg = new Inventario();
                dlg.setArguments(bundle);
                OpenFragment(dlg);

            }
        });
        Egreso.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Bundle bundle = new Bundle();
                bundle.putString("tipo","Egreso");
                Fragment dlg = new Inventario();
                dlg.setArguments(bundle);
                OpenFragment(dlg);

            }
        });
        newActivity = view.findViewById(R.id.buttonMovimientos);
        newCuenta = view.findViewById(R.id.buttonCuentas);


        newActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getContext(), HistorialCuentas.class);
                //Optional parameters
                getContext().startActivity(myIntent);

            }
        });
        newCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getContext(), Cuentas.class);
                //Optional parameters
                getContext().startActivity(myIntent);

            }
        });



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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public void OpenFragment(Fragment nuevo){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.your_placeholder,nuevo);
        transaction.commit();
        transaction.addToBackStack(null);

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
