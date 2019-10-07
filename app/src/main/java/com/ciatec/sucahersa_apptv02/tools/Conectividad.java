package com.ciatec.sucahersa_apptv02.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/*
* Obtiene la información para determinar si existe algun tipo de conexión a internet
*/

public class Conectividad {

    public boolean ProbarConexionInternet(Context context){
        boolean conexionestablecida;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("Conectividad", "Online");
            Log.d("Conectividad", " Estado actual: " + networkInfo.getState());

            conexionestablecida = true;

            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                // Estas conectado a un Wi-Fi
                Log.d("MIAPP", " Nombre red Wi-Fi: " + networkInfo.getExtraInfo());
            }

        } else {
            Log.d("MIAPP", "Estás offline");
            conexionestablecida = false;
        }
        return conexionestablecida;
    }
}
