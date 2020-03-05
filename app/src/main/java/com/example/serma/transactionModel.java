package com.example.serma;

import java.util.List;

public class transactionModel {
    Double AÑO, DIA, IVA, MES;
    String descripcion, cliente;
    String tipo,fecha;
    Double total;
    String objetos;
    String cantidad;


    public transactionModel(Double AÑO, Double DIA, Double IVA, Double MES, String descripcion, String tipo, Double total,String cliente,String objetos,String Cantidad) {
        this.AÑO = AÑO;
        this.DIA = DIA;
        this.cliente = cliente;
        this.IVA = IVA;
        this.MES = MES;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.total = total;
        this.objetos = objetos;
        this.cantidad = Cantidad;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getObjetos() {
        return objetos;
    }

    public void setObjetos(String objetos) {
        this.objetos = objetos;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public int getMES() {
        return MES.intValue();
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
    public String getFecha(){
        fecha = getDIA()+"/"+getMES()+"/"+getAÑO();
        return fecha;
    }
    public void setFecha(String fecha){
        this.fecha = fecha;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getAÑO() {
        return AÑO.intValue();
    }

    public void setAÑO(Double AÑO) {
        this.AÑO = AÑO;
    }

    public int getDIA() {
        return DIA.intValue();
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


