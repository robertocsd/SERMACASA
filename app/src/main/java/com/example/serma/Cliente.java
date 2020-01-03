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
    List<String> Arrays10 = new ArrayList<>();
    ArrayAdapter<String> clienteAdapter;
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
         Button newClient;
        newClient = view.findViewById(R.id.buttonClientNew);
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

                                        todacate.add(document.getString("Nombre"));

                                        ArrayAdapter<String> tasko = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, todacate);
                                        Log.d("eshta", todacate.toString());
                                        Arrays10.add(document.getId());
                                        tasko.notifyDataSetChanged();
                                        ArrayTO.setAdapter(tasko);


                                    }
                                } else {
                                    Log.d("feo", "Error getting documents: ", task.getException());
                                }
                            }
                        });


                    }
                });
        ArrayTO.setOnItemClickListener(this);


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
        String Categoria = Arrays10.get(position);
        Log.i("IDDDDD",Categoria);
        Bundle bundle = new Bundle();
        bundle.putString("ID",Categoria);
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
