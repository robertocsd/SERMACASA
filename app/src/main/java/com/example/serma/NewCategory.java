package com.example.serma;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.Empty;

import java.util.HashMap;
import java.util.Map;

public class NewCategory extends DialogFragment {

    private NewCategoryViewModel mViewModel;
    private View view;



    //INSTANCIA DE FIREBASE.

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Inventario = db.collection("categoria");
    Map<String, Object> Categoria = new HashMap<>();

    public static NewCategory newInstance() {
        return new NewCategory();
    }
    EditText categoria;






    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

       return inflater.inflate(R.layout.new_category_fragment, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(NewCategoryViewModel.class);
        // TODO: Use the ViewModel
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.new_category_fragment,null);
        categoria = view.findViewById(R.id.EdiTextCategory);

        builder.setTitle("Introduce la nueva categoria");

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {


            public void onClick(DialogInterface dialog, int id) {
                try {
                    if (categoria.getText().toString().trim().equals("")) {
                        throw new Exception("Perdón, pero no puede existir una categoría vacía :(");
                    } else {
                        DocumentReference docRef = db.collection("categoria").document(categoria.getText().toString());
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d("FEO", "DocumentSnapshot data: " + document.getData());
                                    } else {
                                        Categoria.put("Nombre", categoria.getText().toString());
                                        Inventario.document(categoria.getText().toString()).set(Categoria);
                                    }
                                } else {
                                    Log.d("FEO", "get failed with ", task.getException());

                                }
                            }
                        });
                    }
                }catch (Exception e){
                    Toast.makeText(getActivity(), "Ocurrió algo pero no te preocupes, no es tu culpa, a menos que hayas puesto una categoría repetida",
                            Toast.LENGTH_LONG).show();
                }

                // FIRE ZE MISSILES!
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

}
