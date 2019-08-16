package com.ciatec.sucahersa_apptv02.modelo;

public class ProductoPrecio {


    private String id;
    private String idsch;
    private String nombre;
    private String estrella;
    private String imagen;
    private String precio;

    public ProductoPrecio
            (String id,
             String idsch,
             String nombre,
             String estrella,
             String imagen,
             String precio)
    {
        this.id = id;
        this.idsch = idsch;
        this.nombre = nombre;
        this.estrella = estrella;
        this.imagen = imagen;
        this.precio = precio;
    }

    public String getIdProducto(){return id;}

    public String getIdSCH(){return idsch;}

    public String getNombre(){return nombre;}

    public String getEstrella(){return estrella;}

    public String getImagen(){return imagen;}

    public String getPrecio(){return precio;}
}
