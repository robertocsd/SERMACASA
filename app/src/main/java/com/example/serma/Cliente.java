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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

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
 * {@link Cliente.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Cliente#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Cliente extends Fragment implements AdapterView.OnItemClickListener  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Cliente = db.collection("Cliente");
    Map<String, Object> cate = new HashMap<>();
    List<String> todacate = new ArrayList<String>();
    ListView ArrayTO;
    ArrayAdapter<String> tasko;
    SimpleAdapter adapters;
    List<String> Arrays10 = new ArrayList<>();
    SearchView sv;
    List<HashMap<String, String>> listItems = new ArrayList<>();
    HashMap<String, String> resultado = new HashMap<>();
    private OnFragmentInteractionListener mListener;

    public Cliente() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Cliente.
     */
    // TODO: Rename and change types and number of parameters
    public static Cliente newInstance(String param1, String param2) {
        Cliente fragment = new Cliente();
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
         View view = inflater.inflate(R.layout.fragment_cliente, container, false);
        FloatingActionButton newClient = view.findViewById(R.id.buttonClientNew);
        sv = view.findViewById(R.id.searchViewCliente);

        ArrayTO = view.findViewById(R.id.ListViewDynamic);
        todacate.clear();

        newClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dlg = new NuevoCliente();
                dlg.show(getFragmentManager().beginTransaction(), "login");
            }
        });
        db.collection("Cliente")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        listItems.clear();

                                        resultado.put(document.getString("Nombre"),document.getId());
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

                                    }
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

                adapters.getFilter().filter(text);

                return true;
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
    public void OpenFragment(Fragment nuevo){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.your_placeholder,nuevo);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, String> clickedItem = (HashMap<String, String>) adapters.getItem(position);
        String value = clickedItem.get("First line");
        String key = clickedItem.get("Second line");
        Log.i("PRUEBA DE OBTENCIÍON",value +  " ----- " + key);


        Log.i("IDDDDD",key);
        Bundle bundle = new Bundle();
        bundle.putString("ID",key);
        Fragment newOb = new infoCliente();
        newOb.setArguments(bundle);
        OpenFragment(newOb);
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
