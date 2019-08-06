package com.ciatec.sucahersa_apptv02.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ciatec.sucahersa_apptv02.R;
import com.ciatec.sucahersa_apptv02.cliente.VolleySingleton;
import com.ciatec.sucahersa_apptv02.modelo.Producto;
import com.ciatec.sucahersa_apptv02.modelo.Promocion;
import com.ciatec.sucahersa_apptv02.tools.Constantes;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, YouTubePlayer.PlaybackEventListener {

    String claveAPIYoutube = "AIzaSyBOogQ7p8FGSTfqqyf54j3itv4LuPqr0L0";
    YouTubePlayerView youTubePlayerView;

    // Etiqueta de depuracion
    private static final String TAG = MainActivity.class.getSimpleName();

    // Adaptador del recycler view
    private ProductoAdaptador adapter;

    // Instancia global del recycler view
    private RecyclerView lista;

    // instancia global del administrador
    private RecyclerView.LayoutManager lManager;

    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //youTubePlayerView=(YouTubePlayerView)findViewById(R.id.youtube_view);
        //youTubePlayerView.initialize(claveAPIYoutube, this);


        lista = (RecyclerView) findViewById(R.id.rcv_productos);
        lista.setHasFixedSize(true);
        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        lista.setLayoutManager(lManager);

        // Cargar datos en el adaptador de Productos
        cargarAdaptadorProducto();

        //Cargar datos Noticias
        peticionNoticias();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean fueRestaurado) {
        if (!fueRestaurado)
        {
            //youTubePlayer.cueVideo("kPa9YoPZALs&list=PLty-EzYotmfSRMC1jNdbP6yygILz2wHAG");
            youTubePlayer.loadPlaylist("PLty-EzYotmfSRMC1jNdbP6yygILz2wHAG");
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if(youTubeInitializationResult.isUserRecoverableError()){
            youTubeInitializationResult.getErrorDialog(this, 1).show();
        }else {
            String error = "Error al inicializar Youtube " + youTubeInitializationResult.toString();
            Toast.makeText(this, error,Toast.LENGTH_LONG).show();
        }
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        if(resultCode == 1){
            getYoutubePLayerProvider().initialize(claveAPIYoutube, this);
        }
    }

    protected YouTubePlayer.Provider getYoutubePLayerProvider(){
        return youTubePlayerView;
    }

    @Override
    public void onPlaying() {

    }

    @Override
    public void onPaused() {

    }

    @Override
    public void onStopped() {

    }

    @Override
    public void onBuffering(boolean b) {

    }

    @Override
    public void onSeekTo(int i) {

    }

    /**
     * Carga el adaptador con los productos obtenidos
     * en la respuesta
     */
    public void cargarAdaptadorProducto() {
        Log.d(TAG, "OBtener_productos: " + Constantes.OBTENER_PRODUCTOS);
        // Petición GET
        VolleySingleton.getInstance(this).addToRequestQueue(
                        new JsonArrayRequest(Request.Method.GET,
                                Constantes.OBTENER_PRODUCTOS,
                                null,
                                new Response.Listener<JSONArray>() {

                                    @Override
                                    public void onResponse(JSONArray response) {
                                        // Procesar la respuesta Json
                                        procesarRespuestaProducto(response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(TAG, "Error Volley: " + error.toString());
                                    }
                                }

                        )
                );
    }

    private void procesarRespuestaProducto(JSONArray response) {
            try {
                Producto[] productos = gson.fromJson(response.toString(), Producto[].class);
                // Inicializar adaptador
                adapter = new ProductoAdaptador(Arrays.asList(productos), this);
                // Setear adaptador a la lista
                lista.setAdapter(adapter);

            } catch (Exception e) {
                Log.d(TAG, "ERROR:  ..............." + e.getMessage());
            }
    }

    public void peticionNoticias() {
        Log.d(TAG, "OBtener_productos: " + Constantes.OBTENER_NOTICIAS);
        // Petición GET
        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonArrayRequest(Request.Method.GET,
                        Constantes.OBTENER_NOTICIAS,
                        null,
                        new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                // Procesar la respuesta Json
                                procesarRespuestaNoticias(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error Volley: " + error.toString());
                            }
                        }
                )
        );
    }

    private void procesarRespuestaNoticias(JSONArray response) {
        try {
            Promocion[] noticias = gson.fromJson(response.toString(), Promocion[].class);
            // Inicializar adaptador
            // adapter = new ProductoAdaptador(Arrays.asList(productos), this);
            // Setear adaptador a la lista
            //lista.setAdapter(adapter);

        } catch (Exception e) {
            Log.d(TAG, "ERROR:  ..............." + e.getMessage());
        }
    }



}
