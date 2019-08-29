package com.ciatec.sucahersa_apptv02.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

public class MainActivity extends YouTubeBaseActivity
                          implements YouTubePlayer.OnInitializedListener,
                                     YouTubePlayer.PlaybackEventListener {

    String claveAPIYoutube = "AIzaSyBOogQ7p8FGSTfqqyf54j3itv4LuPqr0L0";
    YouTubePlayerView youTubePlayerView;

    TextView txv_titulo;
    TextView txv_contenido;
    ImageView imv_noticia;

    // Etiqueta de depuracion
    private static final String TAG = MainActivity.class.getSimpleName();

    // Adaptador del recycler view
    private ProductoAdaptador adapter;

    // Instancia global del recycler view
    private RecyclerView listaProductos;
    private RecyclerView listaProductosEstrella;

    // instancia global del administrador
    private RecyclerView.LayoutManager lManager;

    private Gson gson = new Gson();

    List<Promocion> lista_promociones;
    List<Producto> lista_procutoEstrella;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        youTubePlayerView=(YouTubePlayerView)findViewById(R.id.youtube_view);
        //youTubePlayerView.initialize(claveAPIYoutube, this);

        listaProductos = (RecyclerView) findViewById(R.id.rcv_productos);
        listaProductos.setHasFixedSize(true);
        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        listaProductos.setLayoutManager(lManager);

        // Cargar datos en el adaptador de Productos
        cargarAdaptadorProducto();

        //Cargar datos Noticias
        peticionNoticias();

        Toast.makeText(MainActivity.this, "-------------- onCreate ----------------", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(MainActivity.this, "-------------- onStart ----------------", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {

        super.onResume();
        Toast.makeText(MainActivity.this, "-------------- onResume ----------------", Toast.LENGTH_LONG).show();
    }

    //region REGION


    //endregion

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean fueRestaurado) {
        if (!fueRestaurado)
        {
            //youTubePlayer.cueVideo("kPa9YoPZALs&list=PLty-EzYotmfSRMC1jNdbP6yygILz2wHAG");

            youTubePlayer.loadPlaylist(Constantes.playlist);
            Toast.makeText(MainActivity.this, "-------------- onInitializationSuccess ----------------", Toast.LENGTH_LONG).show();
            //PlaylistEventListener
            youTubePlayer.setPlaylistEventListener(new YouTubePlayer.PlaylistEventListener() {
                @Override
                public void onPrevious() {
                    Toast.makeText(MainActivity.this, "-------------- onPrevious ----------------", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNext() {
                    Toast.makeText(MainActivity.this, "-------------- onNext ----------------", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onPlaylistEnded() {
                    Toast.makeText(MainActivity.this, "-------------- onPlaylistEnded ----------------", Toast.LENGTH_LONG).show();
                    youTubePlayer.loadPlaylist(Constantes.playlist);

                }
            });



        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(MainActivity.this, "-------------- onInitializationFailure ----------------", Toast.LENGTH_LONG).show();
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
        Log.v(TAG, "----------------------- onPlaying -------------------------------");
        Toast.makeText(MainActivity.this, "onPlaying", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPaused() {
        Log.v(TAG, "----------------------- onPaused -------------------------------");
        Toast.makeText(MainActivity.this, "onPaused", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStopped() {
        Log.v(TAG, "----------------------- onStopped -------------------------------");
        Toast.makeText(MainActivity.this, "onStopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBuffering(boolean b) {
        Log.v(TAG, "----------------------- onBuffering -------------------------------");
        Toast.makeText(MainActivity.this, "onBuffering", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSeekTo(int i) {
        Log.v(TAG, "----------------------- onSeekTo -------------------------------");
        Toast.makeText(MainActivity.this, "onSeekTo", Toast.LENGTH_SHORT).show();
    }

    /**
     * Carga el adaptador con los productos obtenidos
     * en la respuesta
     */
    public void cargarAdaptadorProducto() {
        Log.d(TAG, "Obtener_productos: " + Constantes.OBTENER_PRODUCTOS);
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
        if (response != null) {
            try {
                Log.d(TAG, "Entrando a for de response.................. " + Constantes.OBTENER_PRODUCTOS);

                for(int i = 0; i<response.length(); i++){
                    String estrella = response.getJSONObject(i).getString("estrella");
                    if(estrella == "1"){//inicializamos la lista donde almacenaremos los objetos Promocion
                        String id = response.getJSONObject(i).getString("id");
                        String id_sch = response.getJSONObject(i).getString("idsch");
                        String nombre = response.getJSONObject(i).getString("nombre");
                        String imagen = response.getJSONObject(i).getString("imagen");

                        //inicializamos la lista donde almacenaremos los objetos Promocion
                        lista_procutoEstrella.add(new Producto(id, id_sch, nombre, estrella, imagen));
                    }
                }
                if(lista_procutoEstrella != null){
                    //Producto Estrella
                    adapter = new ProductoAdaptador(lista_procutoEstrella,MainActivity.this);
                    listaProductosEstrella.setAdapter(adapter);
                }


                Producto[] productos = gson.fromJson(response.toString(), Producto[].class);
                // Inicializar adaptador Productos Estrella
                adapter = new ProductoAdaptador(Arrays.asList(productos), this);
                // Setear adaptador a la lista
                listaProductos.setAdapter(adapter);


            }
            catch(Exception e){
            Log.d(TAG, "ERROR:  ..............." + e.getMessage());
            }
        }
    }

    public void peticionNoticias() {
        Log.d(TAG, "Obtener_productos: " + Constantes.OBTENER_NOTICIAS);

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
                                //Asignar elementos a views
                                AsignarElementosViews();
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

    public void procesarRespuestaNoticias(JSONArray response) {
        Log.d(TAG, "Procesar Respuesta Noticias.................................. ");
        txv_titulo = findViewById(R.id.item_Titulo);
        txv_contenido = findViewById(R.id.item_Contenido);
        imv_noticia = findViewById(R.id.img_Noticia);

        if (response != null){
            try {
                lista_promociones = new ArrayList<>();
                Log.d(TAG, "Entrando a for de response.................. " + Constantes.OBTENER_NOTICIAS);
                for(int i = 0; i<response.length(); i++){
                    String id = response.getJSONObject(i).getString("id");
                    String titulo = response.getJSONObject(i).getString("titulo");
                    String contenido = response.getJSONObject(i).getString("contenido");
                    String imagen = response.getJSONObject(i).getString("imagen");
                    String vigencia = response.getJSONObject(i).getString("vigencia");

                    //inicializamos la lista donde almacenaremos los objetos Promocion
                    lista_promociones.add(new Promocion(id, titulo, contenido, imagen, vigencia));
                }
            } catch (Exception e) {
                Log.d(TAG, "ERROR:  ..............." + e.getMessage());
            }
        }
    }

    private void AsignarElementosViews(){

        if(lista_promociones != null){

            for(int i = 0; i < lista_promociones.size(); i++){

                /*
                try {
                    Log.d(TAG, "TIMER..............................." );
                    Thread.sleep(3000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                */

                ejecutarCambioNoticia(i);
            }
        }
    }

    public void hiloNoticias() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void ejecutarCambioNoticia(int f){
        TimerPromociones timerPromociones = new TimerPromociones(f);
        timerPromociones.execute();
    }

    public class TimerPromociones extends AsyncTask<Void, Integer, Boolean> {

        int i;

        public TimerPromociones (int f){
            i=f;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            for(int i=1; i<Constantes.segundosNOTICIAS; i++){
                hiloNoticias();
                //Log.v(TAG, "Ejecutando doInBackground de AsyncTask..... Cambio de Noticia" + e);
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            ejecutarCambioNoticia(i);

            Promocion promocion = lista_promociones.get(i);
            txv_titulo.setText(promocion.getTitulo());
            txv_contenido.setText(promocion.getContenido());
            Picasso.get().load(promocion.getImagen())
                    .error(R.mipmap.ic_isotipo)
                    .into(imv_noticia);

            //Toast.makeText(MainActivity.this, "d", Toast.LENGTH_SHORT).show();
            Log.v(TAG, "Ejecutando onPostExecute de AsyncTask..... ");

        }
    }

}
