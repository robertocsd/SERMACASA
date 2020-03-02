package com.example.serma;

import java.util.List;

public class transactionModel {
    Double AÑO, DIA, IVA, MES;
    String descripcion, cliente;
    String tipo;
    Double total;

    public transactionModel(Double AÑO, Double DIA, Double IVA, Double MES, String descripcion, String tipo, Double total,String cliente) {
        this.AÑO = AÑO;
        this.DIA = DIA;
        this.cliente = cliente;
        this.IVA = IVA;
        this.MES = MES;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.total = total;

    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public Double getMES() {
        return MES;
    }

    public void setMES(Double MES) {
        this.MES = MES;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getTitulo() {
        return descripcion;
    }

    public void setTitulo(String titulo) {
        this.descripcion = titulo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getAÑO() {
        return AÑO;
    }

    public void setAÑO(Double AÑO) {
        this.AÑO = AÑO;
    }

    public Double getDIA() {
        return DIA;
    }

    public void setDIA(Double DIA) {
        this.DIA = DIA;
    }

    public Double getIVA() {
        return IVA;
    }

    public void setIVA(Double IVA) {
        this.IVA = IVA;
    }

}


