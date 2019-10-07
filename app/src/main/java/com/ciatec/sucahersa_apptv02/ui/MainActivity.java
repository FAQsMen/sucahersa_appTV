package com.ciatec.sucahersa_apptv02.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import com.squareup.picasso.Picasso;

import com.ciatec.sucahersa_apptv02.R;
import com.ciatec.sucahersa_apptv02.cliente.VolleySingleton;
import com.ciatec.sucahersa_apptv02.modelo.PlayList;
import com.ciatec.sucahersa_apptv02.modelo.Producto;
import com.ciatec.sucahersa_apptv02.modelo.ProductoMerge;
import com.ciatec.sucahersa_apptv02.modelo.ProductoPrecio;
import com.ciatec.sucahersa_apptv02.modelo.Promocion;
import com.ciatec.sucahersa_apptv02.tools.Conectividad;
import com.ciatec.sucahersa_apptv02.tools.Constantes;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class MainActivity extends YouTubeBaseActivity
                          implements YouTubePlayer.OnInitializedListener,
                                     YouTubePlayer.PlaybackEventListener {

    //region VARIABLES GLOBALES

    String claveAPIYoutube = "AIzaSyBOogQ7p8FGSTfqqyf54j3itv4LuPqr0L0";

    YouTubePlayerView youTubePlayerView;

    TextView txv_titulo;
    TextView txv_contenido;
    ImageView imv_noticia;

    // Etiqueta de depuracion
    private static final String TAG = MainActivity.class.getSimpleName();

    // Adaptador del recyclerview
    private ProductoAdaptador adapter;
    private ProductoEstrellaAdaptador adapterEstrella;

    // Instancia global del recyclerview
    private RecyclerView listaProductos;
    private RecyclerView listaProductosEstrella;

    // instancia global del administrador
    private RecyclerView.LayoutManager lManager;
    private RecyclerView.LayoutManager lManagerEstrella;

    int c = 0;

    List<Promocion> list_Promociones;
    List<Producto> list_Productos;
    List<ProductoMerge> list_ProductosEstrella;
    List<ProductoPrecio> list_ProductosPrecios;
    List<ProductoMerge> list_ProductosSinEstrella;
    List<PlayList> list_PlayList;

    //endregion

    //region CICLO DE VIDA DE LA APP

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listaProductos = (RecyclerView) findViewById(R.id.rcv_productos);
        listaProductos.setHasFixedSize(true);
        listaProductosEstrella = (RecyclerView) findViewById(R.id.rcv_productosEstrella);
        listaProductosEstrella.setHasFixedSize(true);

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(this);
        listaProductos.setLayoutManager(lManager);
        lManagerEstrella = new LinearLayoutManager(this);
        listaProductosEstrella.setLayoutManager(lManagerEstrella);

        Conectividad conectividad = new Conectividad();
        boolean ConexionExistente = conectividad.ProbarConexionInternet(MainActivity.this);

        if(ConexionExistente) {

            ejecutarThreadPeticiones();

        }else{
            Toast.makeText(MainActivity.this,
                    "No existe conexión a Internet",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    //endregion

    //region FUNCIONES YOUTUBE

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean fueRestaurado) {

        if(list_PlayList != null && list_PlayList.size()>0) {

            if(list_PlayList.get(c).getLiga().contains("=")) {

                String[] list_parts = list_PlayList.get(c).getLiga().split("=");
                if (list_parts.length > 0) {
                    String keylist = list_parts[1];

                    if (!fueRestaurado) {
                        //youTubePlayer.cueVideo("kPa9YoPZALs&list=PLty-EzYotmfSRMC1jNdbP6yygILz2wHAG");

                        youTubePlayer.loadPlaylist(keylist);
                        //PlaylistEventListener
                        youTubePlayer.setPlaylistEventListener(new YouTubePlayer.PlaylistEventListener() {
                            @Override
                            public void onPrevious() {
                            }

                            @Override
                            public void onNext() {
                            }

                            @Override
                            public void onPlaylistEnded() {
                                c = c + 1;
                                if (c < list_PlayList.size()) {
                                    String[] list_parts = list_PlayList.get(c).getLiga().split("=");

                                    if (list_parts.length > 1) {
                                        String keylist = list_parts[1];
                                        youTubePlayer.loadPlaylist(keylist);
                                    } else {
                                        c = c - 1;
                                        String[] list_parts02 = list_PlayList.get(c).getLiga().split("=");
                                        String keylist = list_parts02[1];
                                        youTubePlayer.loadPlaylist(keylist);
                                    }
                                }else {
                                    c = c - 1;
                                    String[] list_parts02 = list_PlayList.get(c).getLiga().split("=");
                                    String keylist = list_parts02[1];
                                    youTubePlayer.loadPlaylist(keylist);
                                }
                            }
                        });
                    }

                }
            }
            else{
                Toast.makeText(MainActivity.this,"Link de video no valido",Toast.LENGTH_SHORT).show();
            }
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

    //region YouTubePlayer.PlaybackEventListener

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

    //endregion

    //endregion

    //region PLAYLIST

    public void ObtenerPlayList(){
        Log.d(TAG, "Obtener_playList: " + Constantes.OBTENER_VIDEOS);
        // Petición GET
        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonArrayRequest(Request.Method.GET,
                        Constantes.OBTENER_VIDEOS,
                        null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                // Procesar la respuesta Json
                                procesarRespuestaPlayList(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error Volley: " + error.toString());
                                Toast.makeText(MainActivity.this,
                                        "No se pudieron obtener los videos para reproducir",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                )
        );
    }

    private void procesarRespuestaPlayList(JSONArray response){
        if(response != null){
            try
            {
                list_PlayList = new ArrayList<>();

                for(int i = 0; i<response.length(); i++){
                    String id = response.getJSONObject(i).getString("id");
                    String titulo = response.getJSONObject(i).getString("titulo");
                    String liga = response.getJSONObject(i).getString("liga");
                    String vigencia = response.getJSONObject(i).getString("vigencia");

                    //inicializamos la lista donde almacenaremos los objetos Promocion
                    list_PlayList.add(new PlayList(id, titulo, liga, vigencia));
                }
            }
            catch (Exception ex){
                Toast.makeText(MainActivity.this,
                        "Ocurrio algo inesperado con los links de las PLayList: " + ex ,
                        Toast.LENGTH_SHORT).show();
            }
            finally {

            }
        }else{
            Toast.makeText(MainActivity.this,
                    "No se pudieron obtener los links de las PLayList",
                    Toast.LENGTH_SHORT).show();
            Log.v(TAG, "No se pudieron obtener los links de las PLayList");
        }
    }

    //endregion

    //region PRODUCTOS

    public void peticionProductosPrecio() {
        Log.d(TAG, "Obtener_productos: " + Constantes.OBTENER_PRECIOS);
        // Petición GET
        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonObjectRequest(Request.Method.GET,
                        Constantes.OBTENER_PRECIOS,
                        null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta Json
                                procesarRespuestaProductosPrecios(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error Volley: " + error.toString());
                                Toast.makeText(MainActivity.this,
                                        "No se pudieron obtener los precios de los productos",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                )
        );
    }

    public void peticionProductos() {
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
                                procesarRespuestaProductos(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Error Volley: " + error.toString());
                                Toast.makeText(MainActivity.this,
                                        "No se pudieron obtener los productos estrella",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                )
        );
    }

    private void procesarRespuestaProductosPrecios(JSONObject response){
        if(response != null){
            try
            {
                JSONObject jresponse = response.optJSONObject("response");
                JSONObject jttprecios = jresponse.optJSONObject("ttPrecios");
                JSONArray jttprecios2 = jttprecios.optJSONArray("ttPrecios");

                list_ProductosPrecios = new ArrayList<>();

                for(int i = 0; i<jttprecios2.length(); i++){
                    String _articulo = jttprecios2.getJSONObject(i).getString("Articulo");
                    String _nombre = jttprecios2.getJSONObject(i).getString("Nombre");
                    double _menudeo = jttprecios2.getJSONObject(i).getDouble("Menudeo");
                    double _mayoreo = jttprecios2.getJSONObject(i).getDouble("Mayoreo");
                    int _soloMayoreo = jttprecios2.getJSONObject(i).getInt("SoloMayoreo");

                    //inicializamos la lista donde almacenaremos los objetos Promocion
                    list_ProductosPrecios.add(new ProductoPrecio(_articulo, _nombre, _menudeo,_mayoreo,_soloMayoreo));
                }
            }
            catch (Exception ex){
                Toast.makeText(MainActivity.this,
                        "Ocurrio algo inesperado con los precios de los productos: " + ex ,
                        Toast.LENGTH_SHORT).show();
            }
            finally {
                //combinarListas();
            }
        }else{
            Toast.makeText(MainActivity.this,
                    "No se pudieron obtener los precios de los productos",
                    Toast.LENGTH_SHORT).show();
            Log.v(TAG, "No se pudieron obtener los precios de los productos");
        }
    }

    private void procesarRespuestaProductos(JSONArray response){
        if(response != null){
            try
            {
                list_Productos = new ArrayList<>();
                for(int i = 0; i<response.length(); i++){
                    String id = response.getJSONObject(i).getString("id");
                    String id_sch = response.getJSONObject(i).getString("idsch");
                    String nombre = response.getJSONObject(i).getString("nombre");
                    String imagen = response.getJSONObject(i).getString("imagen");
                    String estrella = response.getJSONObject(i).getString("estrella");

                    //inicializamos la lista donde almacenaremos los objetos Productos
                    list_Productos.add(new Producto(id, id_sch, nombre, estrella, imagen));
                }
            }
            catch (Exception ex){
                Toast.makeText(MainActivity.this,
                        "Ocurrio algo inesperado al obtener los productos: " + ex ,
                        Toast.LENGTH_SHORT).show();
            }
            finally {

                //combinarListas();
                //Realizar petición a ws de Sucahaersa para Precios de los Productos
                /*
                if(list_ProductosPrecios != null){
                    combinarListas();
                }

                 */
            }
        }
        else{
            Toast.makeText(MainActivity.this,
                    "No se pudieron obtener los productos estrella",
                    Toast.LENGTH_SHORT).show();
            Log.v(TAG, "No se pudieron obtener los productos estrella");
        }
    }

    private void combinarListas() {
        if (list_ProductosPrecios != null) {
            if (list_Productos != null) {
                String productoEstrella, productoPrecio;
                list_ProductosEstrella = new ArrayList<>();
                list_ProductosSinEstrella = new ArrayList<>();
                Log.d(TAG, ".................  Combinar Listas ..........................");
                for (int i = 0; i < list_Productos.size(); i++) {
                    for (int j = 0; j < list_ProductosPrecios.size(); j++) {
                        productoEstrella = list_Productos.get(i).getIdSCH();
                        productoPrecio = list_ProductosPrecios.get(j).getArticulo();
                        if (productoPrecio.equals(productoEstrella)) {
                            if (list_Productos.get(i).getEstrella().equals("1")){
                                list_ProductosEstrella.add(new ProductoMerge(
                                        list_ProductosPrecios.get(j).getArticulo(),
                                        list_ProductosPrecios.get(j).getNombre(),
                                        list_ProductosPrecios.get(j).getMenudeo(),
                                        list_ProductosPrecios.get(j).getMayoreo(),
                                        list_ProductosPrecios.get(j).getSoloMayoreo(),
                                        list_Productos.get(i).getIdProducto(),
                                        list_Productos.get(i).getIdSCH(),
                                        list_Productos.get(i).getNombre(),
                                        list_Productos.get(i).getEstrella(),
                                        list_Productos.get(i).getImagen()
                                ));
                            }
                            else {
                                list_ProductosSinEstrella.add(new ProductoMerge(
                                        list_ProductosPrecios.get(j).getArticulo(),
                                        list_ProductosPrecios.get(j).getNombre(),
                                        list_ProductosPrecios.get(j).getMenudeo(),
                                        list_ProductosPrecios.get(j).getMayoreo(),
                                        list_ProductosPrecios.get(j).getSoloMayoreo(),
                                        list_Productos.get(i).getIdProducto(),
                                        list_Productos.get(i).getIdSCH(),
                                        list_Productos.get(i).getNombre(),
                                        list_Productos.get(i).getEstrella(),
                                        list_Productos.get(i).getImagen()
                                ));
                            }
                        }
                        // Productos sin coincidencias
                        else{

                        }
                    }
                }
                //Salida del for
                if(list_ProductosEstrella.size() > 0){
                    //Producto Estrella
                    adapterEstrella = new ProductoEstrellaAdaptador(list_ProductosEstrella,MainActivity.this);
                    listaProductosEstrella.setAdapter(adapterEstrella);
                }

                if (list_ProductosSinEstrella.size() > 0){
                    //Productos sin Estrella
                    adapterEstrella = new ProductoEstrellaAdaptador(list_ProductosSinEstrella,MainActivity.this);
                    listaProductos.setAdapter(adapterEstrella);
                }


            }
            // Lista vacia de productos
            else {
                Toast.makeText(MainActivity.this,
                        "No se pudieron obtener los productos estrella",
                        Toast.LENGTH_SHORT).show();
            }
        }
        // Lista vacia de precios de productos
        else if(list_Productos.size() > 0 && list_Productos != null){
            adapter = new ProductoAdaptador(list_Productos, MainActivity.this);
            listaProductos.setAdapter(adapter);
        }
        AsignarListasProductosEstrella();
    }

    public void AsignarListasProductosEstrella(){
        if(list_ProductosEstrella != null ){
            ejecutarRotacionProductos();
        }
    }

    public void RotarListas(){
        if(list_ProductosEstrella.size() > 0){
            //Producto Estrella
            int ultimoElemento = (list_ProductosEstrella.size())-1;
            ProductoMerge primerElemento = list_ProductosEstrella.get(0);
            for(int i=0; i<list_ProductosEstrella.size(); i++){
                if (i == ultimoElemento){
                    list_ProductosEstrella.set(i,primerElemento);
                }else{
                    list_ProductosEstrella.set(i,list_ProductosEstrella.get(i+1));
                }
            }
        }

        if (list_ProductosSinEstrella.size() > 0){
            //Productos sin Estrella
            int ultimoElemento = (list_ProductosSinEstrella.size())-1;
            ProductoMerge primerElemento = list_ProductosSinEstrella.get(0);
            for(int i=0; i<list_ProductosSinEstrella.size(); i++){
                if (i == ultimoElemento){
                    list_ProductosSinEstrella.set(i,primerElemento);
                }else{
                    list_ProductosSinEstrella.set(i,list_ProductosSinEstrella.get(i+1));
                }
            }
        }
    }

    public void RefrescarListas(){

        if(list_ProductosEstrella.size() > 0){
            //Producto Estrella
            int ultimoElemento = (list_ProductosEstrella.size())-1;
            for(int i=0; i<list_ProductosEstrella.size(); i++){
                if (i == ultimoElemento){
                    list_ProductosEstrella.set(i,list_ProductosEstrella.get(0));
                }else{
                    list_ProductosEstrella.set(i,list_ProductosEstrella.get(i+1));
                }
            }
            adapterEstrella = new ProductoEstrellaAdaptador(list_ProductosEstrella,MainActivity.this);
            listaProductosEstrella.setAdapter(adapterEstrella);
        }

        if (list_ProductosSinEstrella.size() > 0){
            //Productos sin Estrella
            int ultimoElemento = (list_ProductosSinEstrella.size())-1;
            for(int i=0; i<list_ProductosSinEstrella.size(); i++){
                if (i == (list_ProductosSinEstrella.size()-1)){
                    list_ProductosSinEstrella.set(i,list_ProductosSinEstrella.get(0));
                }else{
                    list_ProductosSinEstrella.set(i,list_ProductosSinEstrella.get(i+1));
                }
            }
            adapterEstrella = new ProductoEstrellaAdaptador(list_ProductosSinEstrella,MainActivity.this);
            listaProductos.setAdapter(adapterEstrella);
        }

    }

    //endregion

    //region NOTICIAS/PROMOCIONES

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
                                //AsignarElementosViews();
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
                list_Promociones = new ArrayList<>();
                Log.d(TAG, "Entrando a for de response.................. " + Constantes.OBTENER_NOTICIAS);
                for(int i = 0; i<response.length(); i++){
                    String id = response.getJSONObject(i).getString("id");
                    String titulo = response.getJSONObject(i).getString("titulo");
                    String contenido = response.getJSONObject(i).getString("contenido");
                    String imagen = response.getJSONObject(i).getString("imagen");
                    String vigencia = response.getJSONObject(i).getString("vigencia");

                    //inicializamos la lista donde almacenaremos los objetos Promocion
                    list_Promociones.add(new Promocion(id, titulo, contenido, imagen, vigencia));
                }
            } catch (Exception e) {
                Log.d(TAG, "ERROR:  ..............." + e.getMessage());
            }finally {

            }
        }
    }

    private void AsignarElementosViews(){
        int i= 0;
        ejecutarCambioNoticia(i);
    }

    //endregion

    //region EJECUTAR HILOS

    public void ejecutarThreadPeticiones(){
        ThreadPeticiones threadPeticiones = new ThreadPeticiones();
        threadPeticiones.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void ejecutarRotacionProductos(){
        PausaRotarProductos pausaRotarProductos = new PausaRotarProductos();
        pausaRotarProductos.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void ejecutarCambioProductos(int i){
        TimerProductos timerProductos = new TimerProductos(i);
        timerProductos.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        //timerProductos.execute();
        //timerProductos.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public void ejecutarCambioNoticia(int f){
        if(f < list_Promociones.size()) {
            TimerPromociones timerPromociones = new TimerPromociones(f);
            timerPromociones.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }else {
            AsignarElementosViews();
        }
    }

    //endregion

    //region HILOS

    public class TimerPromociones extends AsyncTask<Void, Integer, Boolean> {

        int i;

        public TimerPromociones (int f){
            i=f;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            //Log.v(TAG, "Ejecutando doInBackground de AsyncTask..... TimerPromociones" );

            boolean timmerterminado = false;

            try {
                Thread.sleep(Constantes.milisegundosNOTICIAS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            timmerterminado = true;


            return timmerterminado;
        }

        @Override
        protected void onPostExecute(Boolean finalizo) {
            if(finalizo) {
                Promocion promocion = list_Promociones.get(i);
                txv_titulo.setText(promocion.getTitulo());
                txv_contenido.setText(promocion.getContenido());
                Picasso.get().load(promocion.getImagen())
                        .error(R.mipmap.ic_isotipo)
                        .into(imv_noticia);

                ejecutarCambioNoticia(i+1);
            }else
                ejecutarCambioNoticia(i);
            //Log.v(TAG, "Ejecutando onPostExecute de AsyncTask..... CambioNoticia");

        }
    }

    public class TimerProductos extends AsyncTask<Void, Integer, Boolean> {
        int i;

        public TimerProductos (int j){
            i = j;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Log.v(TAG, "Ejecutando doInBackground de AsyncTask..... Refrescando Listas" );
            try {
                Thread.sleep(Constantes.milisegundosPRODUCTOS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {

            RefrescarListas();
            Log.v(TAG, "Ejecutando onPostExecute de AsyncTask..... RefrescarLista" );

            ejecutarCambioProductos(i);

        }
    }

    public class PausaRotarProductos extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {

            Log.v(TAG, "Ejecutando doInBackground de AsyncTask..... PausaRotarProductos" );
            try {
                Thread.sleep(Constantes.milisegundosPRODUCTOS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            RotarListas();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            //RefrescarListas();
            Log.v(TAG, "Ejecutando onPostExecute de AsyncTask..... PausaRotarProductos" );
            //ejecutarCambioProductos(i);
            adapterEstrella = new ProductoEstrellaAdaptador(list_ProductosEstrella,MainActivity.this);
            listaProductosEstrella.setAdapter(adapterEstrella);
            adapterEstrella = new ProductoEstrellaAdaptador(list_ProductosSinEstrella,MainActivity.this);
            listaProductos.setAdapter(adapterEstrella);
            ejecutarRotacionProductos();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    public class ThreadPeticiones extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            Log.v(TAG, "Ejecutando doInBackground de AsyncTask..... Peticiones" );

            boolean _peticionescompletas = false;
            boolean _plylist = false;
            boolean _noticias = false;
            boolean _productos = false;
            boolean _precios = false;

            // Realizar petición a ws para obtener las listas de reproducción
            ObtenerPlayList();
            //Realizar petición a ws para obtener las noticias y/o promociones
            peticionNoticias();
            // Realizar petición a ws para obtener los Productos con estrella
            peticionProductos();
            // Realizar petición a ws para obtener los precios de los productos
            peticionProductosPrecio();

            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            do {
                if(list_PlayList != null && list_PlayList.size()>0){_plylist = true; _peticionescompletas = true;}else {_peticionescompletas = false;}
                if(list_Promociones != null && list_Promociones.size()>0){_noticias = true; _peticionescompletas = true;} else {_peticionescompletas = false;}
                if(list_Productos != null && list_Productos.size()>0){_productos = true; _peticionescompletas = true;} else {_peticionescompletas = false;}
                if(list_ProductosPrecios != null && list_ProductosPrecios.size()>0){_precios = true; _peticionescompletas = true;} else {_peticionescompletas = false;}
                Log.v(TAG, "Ejecutando do While de AsyncTask..... Peticiones" );

            }while (_peticionescompletas == false );

            return _peticionescompletas;
        }

        @Override
        protected void onPostExecute(Boolean finalizado) {
            if (finalizado) {
                Log.v(TAG, "Ejecutando onPostExecute de AsyncTask..... Peticiones");

                youTubePlayerView=(YouTubePlayerView)findViewById(R.id.youtube_view);
                youTubePlayerView.initialize(claveAPIYoutube, MainActivity.this);

                // Obtener productos estrella y productos sin estrella
                combinarListas();

                //Asignar elementos a views
                AsignarElementosViews();
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Toast.makeText(MainActivity.this, "Proceso de peticiones cancelado", Toast.LENGTH_SHORT).show();
        }
    }

    //endregion
}