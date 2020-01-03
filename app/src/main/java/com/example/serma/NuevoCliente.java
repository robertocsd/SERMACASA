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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.Guard;
import java.util.HashMap;
import java.util.Map;

public class NuevoCliente extends DialogFragment {

    private NuevoClienteViewModel mViewModel;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Cliente = db.collection("Cliente");
    Map<String, Object> user = new HashMap<>();
    EditText Nombre, Correo,Responsable,Tel,Servicio;



    public static NuevoCliente newInstance() {
        return new NuevoCliente();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.nuevo_cliente_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(NuevoClienteViewModel.class);
        // TODO: Use the ViewModel
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
         super.onCreateDialog(savedInstanceState);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.nuevo_cliente_fragment, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Nombre = view.findViewById(R.id.editTextNombre);
        Correo = view.findViewById(R.id.editTextEmail);
        Responsable = view.findViewById(R.id.editTextResponsable);
        Tel = view.findViewById(R.id.editTextTel);
        Servicio = view.findViewById(R.id.editTextService);
        builder.setTitle("Introduce al nuevo cliente");

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (Nombre.getText().toString().trim().equals("") || Correo.getText().toString().trim().equals("") || Responsable.getText().toString().trim().equals("")||Tel.getText().toString().trim().equals("")||Servicio.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "Alguno de los campos estaba vacío, así que no lo añadí.",
                            Toast.LENGTH_LONG).show();

                } else {

                    user.put("Nombre", Nombre.getText().toString());
                    user.put("Correo", Correo.getText().toString());
                    user.put("Responsable", Responsable.getText().toString());
                    user.put("Tel", Tel.getText().toString());
                    user.put("Servicio", Servicio.getText().toString());
                    Cliente.document().set(user);

                    // FIRE ZE MISSILES!
                }
            }
        })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it


        builder.setView(view);
        return builder.create();
    }
}
