package com.example.serma;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class historia_transacciones_holder extends RecyclerView.ViewHolder {
    public TextView titulo, total, objetos,tipo,cliente,cantidad,fecha;

    public historia_transacciones_holder(@NonNull View itemView) {
        super(itemView);
        this.titulo = itemView.findViewById(R.id.titulo);
        this.cliente = itemView.findViewById(R.id.cliente);
        this.total = itemView.findViewById(R.id.total);
        this.tipo = itemView.findViewById(R.id.tipo);
        this.objetos = itemView.findViewById(R.id.objetos);
        this.cantidad = itemView.findViewById(R.id.Cantidad);
        this.fecha = itemView.findViewById(R.id.fecha);
    }
}
