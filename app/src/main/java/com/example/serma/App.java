package com.example.serma;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class App extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        App.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return App.context;
    }
    public static void showToast(String data) {
        Toast.makeText(App.getAppContext(), data,
                Toast.LENGTH_SHORT).show();
    }
}