package com.example.serma;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class adapter_historial_cardview extends RecyclerView.Adapter<historia_transacciones_holder> {

    Context c;

    ArrayList<transactionModel> models;

    public adapter_historial_cardview(Context ci, ArrayList<transactionModel> models) {
        this.c = ci;
        this.models = models;
    }

    @NonNull
    @Override
    public historia_transacciones_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(App.getAppContext()).inflate(R.layout.cardview_historial_cuentas,null);

        return new historia_transacciones_holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull historia_transacciones_holder holder, int position) {
        holder.titulo.setText(models.get(position).getTitulo());

        holder.total.setText(models.get(position).getTotal().toString());
        holder.cliente.setText(models.get(position).getCliente());
        holder.tipo.setText(models.get(position).getTipo().toString());


    }
    public void updateList(List<transactionModel> model){
        models = new ArrayList<>();
        models.addAll(model);
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}