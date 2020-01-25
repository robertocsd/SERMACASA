package com.example.serma;

import com.google.firebase.firestore.FirebaseFirestore;

public class Guarda {
    double ingreso;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public Guarda(){

    }

    public double getIngreso() {

        return ingreso;
    }

    public void setIngreso(double ingreso) {


        this.ingreso = ingreso;
    }
    public void getCapital(){

    }

}
