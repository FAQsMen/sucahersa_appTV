package com.ciatec.sucahersa_apptv02.tools;

/**
 * Clase que contiene las direcciones usados en "sucahersa_appTV" para
 * realizar las peticiones para los servicios web entre actividades
 * y fragmentos
 */

public class Constantes {

    /**
     * Puerto que utilizas para la conexión.
     * estará en  blanco si no ha configurado esta carácteristica.
     */
    //private static final String PUERTO_HOST = ":63343";
    private static final String PUERTO_HOST = "";
    private static final String PUERTO_HOST_sucahersa = ":8818";

    /**
     * Dirección IP de genymotion o AVD
     */
    private static final String IP = "sucahersa.ciatec.info";
    private static final String IP_sucahersa = "200.77.145.150";
    /**
     * URLs del Web Service
     */

    public static final String OBTENER_PRODUCTOS = "https://" + IP + PUERTO_HOST + "/api/values/productos";
    public static final String OBTENER_NOTICIAS = "https://" + IP + PUERTO_HOST + "/api/values/noticias/pantallas";
    public static final String OBTENER_VIDEOS = "https://" + IP + PUERTO_HOST + "/api/values/videos  ";
    public static final String OBTENER_PRECIOS = "http://" + IP + PUERTO_HOST_sucahersa + "/rest/CtqSrvc/Precios";
    public static final String OBTENER_PRECIOS_BY_ID = "http://" + IP + PUERTO_HOST_sucahersa + "/rest/CtqSrvc/Precios";

    /**
     * Clave para el valor extra que representa al identificador de un producto
     */
    public static final String EXTRA_ID = "IDEXTRA";
}
