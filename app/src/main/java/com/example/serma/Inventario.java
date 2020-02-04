package com.example.serma;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Inventario.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Inventario#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Inventario extends Fragment implements AdapterView.OnItemClickListener {

    ListView ArrayTO;
    SearchView sv;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Cliente = db.collection("categoria");
    EditText cantidad;
    Button botonTerminar;
    List<String> clientes1 = new ArrayList<String>();
    ToggleButton estado;
    ArrayList equiposAgregados = new ArrayList();
    List<HashMap<String, String>> listItems = new ArrayList<>();
    SimpleAdapter adapters;
    TextView inventarioText;


    public String tipo;

    HashMap<String, String> resultado = new HashMap<>();
    private OnFragmentInteractionListener mListener;

    //Instancia de la coleccion en Firebase.


    public Inventario() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Inventario.
     */
    // TODO: Rename and change types and number of parameters
    public static Inventario newInstance(String param1, String param2) {
        Inventario fragment = new Inventario();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override

    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        // Button newCategory = getView().findViewById(R.id.buttonClass);
        if (getArguments() != null) {
            tipo = getArguments().get("tipo").toString();


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_inventario, container, false);
        equiposAgregados.clear();


        ArrayTO = layout.findViewById(R.id.ListViewDynamic);
        listItems.clear();
        sv = layout.findViewById(R.id.searchViewInventario);
        FloatingActionButton newC = layout.findViewById(R.id.b1);
        inventarioText = layout.findViewById(R.id.textViewInventario);

        botonTerminar = layout.findViewById(R.id.buttonTerminar);
        botonTerminar.setVisibility(View.GONE);

        if(tipo.equals("Inventario")){
            inventarioText.setText("Inventario");
            botonTerminar.setVisibility(View.VISIBLE);

        }
        if(tipo.equals("Venta")){
            inventarioText.setText("Venta");
            botonTerminar.setVisibility(View.VISIBLE);
        }
        if(tipo.equals("Egreso")){
            inventarioText.setText("Egreso");
            botonTerminar.setVisibility(View.VISIBLE);
        }

        resultado.clear();
        /*
        estado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        botonTerminar.setVisibility(View.VISIBLE);inventarioText.setText("Transaccion");
                    }
                    else{
                        inventarioText.setText("Inventario");botonTerminar.setVisibility(View.GONE);
                        equiposAgregados.clear();
                    }
            }
        });
        */

        db.collection("Objeto")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        resultado.put(document.getString("Nombre"), document.get("ID").toString());
                                    }
                                    adapters = new SimpleAdapter(getContext(),listItems,R.layout.list_item,
                                            new String[]{"First line","Second line"},new int[]{R.id.text1,R.id.text2});
                                    Iterator it = resultado.entrySet().iterator();
                                    while(it.hasNext()){
                                        HashMap<String, String> resultsmap = new HashMap<>();
                                        Map.Entry pair = (Map.Entry)it.next();
                                        resultsmap.put("First line",pair.getKey().toString());
                                        resultsmap.put("Second line",pair.getValue().toString());
                                        listItems.add(resultsmap);
                                    }
                                    ArrayTO.setAdapter(adapters);
                                } else {
                                    Log.d("feo", "Error getting documents: ", task.getException());
                                }
                            }
                        });
                    }
                });

        ArrayTO.setOnItemClickListener(this);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String text) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {

                if(adapters == null){
                    Toast.makeText(getActivity(), "Aún no se cargan los datos",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    adapters.getFilter().filter(text);
                }


                return true;
            }
        });

        newC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFragment dlg = new ObjectDescription();
                        dlg.show(getFragmentManager().beginTransaction(), "login");
                    }
                });
        botonTerminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("equipos",equiposAgregados);
                bundle.putString("tipo",tipo);
                DialogFragment dlg = new GetTransaction();
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
        listItems.clear();
        transaction.commit();

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        HashMap<String, String> clickedItem = (HashMap<String, String>) adapters.getItem(position);
        String value = clickedItem.get("First line");
        String key = clickedItem.get("Second line");
        Log.i("PRUEBA DE OBTENCIÍON",value +  " ----- " + key);


        if(tipo.equals("Egreso") || tipo.equals("Venta")){
            equiposAgregados.add(value);
            Toast.makeText(getActivity(), "Se ha agregado " + value + " a la transaccion",
                    Toast.LENGTH_LONG).show();

        }
        else {
            Bundle bundle = new Bundle();
            bundle.putString("Nombre", value);
            bundle.putString("Código", key);


            Fragment newOb = new infoObjeto();
            newOb.setArguments(bundle);
            OpenFragment(newOb);
        }

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
