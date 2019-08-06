package com.ciatec.sucahersa_apptv02.modelo;

/**
 * Clase que representa la entidad Producto
 */

public class Producto {

    private String id;
    private String idsch;
    private String nombre;
    private String estrella;
    private String imagen;

    public Producto
            (String id,
            String idsch,
            String nombre,
            String estrella,
            String imagen)
    {
        this.id = id;
        this.idsch = idsch;
        this.nombre = nombre;
        this.estrella = estrella;
        this.imagen = imagen;
    }

    public String getIdProducto(){return id;}

    public String getIdSCH(){return idsch;}

    public String getNombre(){return nombre;}

    public String getEstrella(){return estrella;}

    public String getImagen(){return imagen;}

}
