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
    private static final String PUERTO_HOST = ":63343";
    /**
     * Dirección IP de genymotion o AVD
     */
    private static final String IP = "10.0.3.2";
    /**
     * URLs del Web Service
     */

    public static final String GET = "http://" + IP + PUERTO_HOST + "/I%20Wish/obtener_metas.php";
    public static final String GET_BY_ID = "http://" + IP + PUERTO_HOST + "/I%20Wish/obtener_meta_por_id.php";
    public static final String UPDATE = "http://" + IP + PUERTO_HOST + "/I%20Wish/actualizar_meta.php";
    public static final String DELETE = "http://" + IP + PUERTO_HOST + "/I%20Wish/borrar_meta.php";
    public static final String INSERT = "http://" + IP + PUERTO_HOST + "/I%20Wish/insertar_meta.php";

    /**
     * Clave para el valor extra que representa al identificador de una meta
     */
    public static final String EXTRA_ID = "IDEXTRA";
}
