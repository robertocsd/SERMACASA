package com.example.serma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentTransaction;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ObjectDescription.OnFragmentInteractionListener,Objeto.OnFragmentInteractionListener,Inventario.OnFragmentInteractionListener,Cliente.OnFragmentInteractionListener,Money.OnFragmentInteractionListener,infoObjeto.OnFragmentInteractionListener,GetTransaction.OnFragmentInteractionListener,infoCliente.OnFragmentInteractionListener{


    private int currentApiVersion;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference Cliente = db.collection("Cliente");
    Map<String, Object> user = new HashMap<>();



    BottomNavigationView bottomBar;
    Button NC;
    Button buttonCompra;
    Button buttonVenta;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.

                Fragment fm3 = new Inventario();
                OpenFragment(fm3);

        bottomBar = findViewById(R.id.bottomNavigationView);

        progress = new ProgressDialog(this);
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.menu_entidad:

                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                Fragment fm = new Cliente();
                                OpenFragment(fm);

                            }
                        });

                        return true;
                    case R.id.menu_money:
                        Fragment fm = new Money();
                        OpenFragment(fm);
                        return true;
                    case  R.id.menu_inventario:
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                    Fragment fm3 = new Inventario();
                                OpenFragment(fm3);
                            }
                        });
                        return true;

                }
                return false;
            }
        });

        currentApiVersion = android.os.Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT)
        {

            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
                    {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility)
                        {
                            if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                            {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }





    }
    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);

        if(currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus)
        {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
    @Override
    public void onFragmentInteraction(Uri uri) {


    }

    public void OpenFragment(Fragment nuevo){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.your_placeholder,nuevo);
        transaction.addToBackStack(null);
        transaction.commit();

    }


}





