package com.example.serma;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Objeto.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Objeto#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Objeto extends Fragment implements AdapterView.OnItemClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Cliente = db.collection("categoria");
    Map<String, Object> cate = new HashMap<>();
    List<String> Objetos = new ArrayList<String>();
    ArrayAdapter<String> clienteAdapter;
    TextView Titulo;
    ProgressBar cat;
    ListView ArrayTO;
    Button newObject;
    private String categoria;
    private OnFragmentInteractionListener mListener;

    public Objeto() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Objeto.
     */
    // TODO: Rename and change types and number of parameters
    public static Objeto newInstance(String param1, String param2) {
        Objeto fragment = new Objeto();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoria = getArguments().get("Categoria").toString();

        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_objeto, container, false);
        Titulo = layout.findViewById(R.id.textViewCategoria);
        ArrayTO = layout.findViewById(R.id.ListViewObjetos);
        newObject = layout.findViewById(R.id.buttonNewObject);
        Objetos.clear();
        db.collection("Objeto")
                .whereEqualTo("categoria",categoria).get().
        addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<QuerySnapshot> task) {


                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Objetos.add(document.getString("Nombre"));

                                ArrayAdapter<String> tasko = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,Objetos);
                                Log.d("eshta", Objetos.toString());
                                tasko.notifyDataSetChanged();
                                ArrayTO.setAdapter(tasko);


                            }
                        } else {
                            Log.d("feo", "Error getting documents: ", task.getException());
                        } }
                });



        ArrayTO.setOnItemClickListener(this);

        newObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle cate = new Bundle();
                cate.putString("cate",categoria);

                DialogFragment dlg = new ObjectDescription();
                dlg.setArguments(cate);
                dlg.show(getFragmentManager().beginTransaction(), "login");
            }
        });

        Titulo.setText(categoria);
        db.collection("categoria").whereEqualTo("Nombre",categoria);
        //TODO HACER VALIDACIONES, NO PUEDE HABER UN NOMBRE DE CATEGORIA REPETIDO.

        db.collection("objetos");
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
    public void OpenFragment(Fragment nuevo){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.your_placeholder,nuevo);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String Categoria = Objetos.get(position);

        Bundle bundle = new Bundle();
        bundle.putString("id",Categoria);
        Fragment newOb = new infoObjeto();
        newOb.setArguments(bundle);
        OpenFragment(newOb);

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

    public static class MuestraDatos {
    }
}
