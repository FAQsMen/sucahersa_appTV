package com.ciatec.sucahersa_apptv02.modelo;

public class ProductoMerge {

    //Propiedades Productos con Precio (SUCAHERSA)
    private String articulo;
    private String nombre_sch;
    private double menudeo;
    private double mayoreo;
    private int solomayoreo;

    //Propiedades Productos Estrella
    private String id;
    private String idsch;
    private String nombre;
    private String estrella;
    private String imagen;

    public ProductoMerge
            (String articulo,
             String nombre_sch,
             double menudeo,
             double mayoreo,
             int solomayoreo,
             String id,
             String idsch,
             String nombre,
             String estrella,
             String imagen
             )
    {
        this.articulo = articulo;
        this.nombre_sch = nombre_sch;
        this.menudeo = menudeo;
        this.mayoreo = mayoreo;
        this.solomayoreo = solomayoreo;

        this.id = id;
        this.idsch = idsch;
        this.nombre = nombre;
        this.estrella = estrella;
        this.imagen = imagen;
    }
    //Metodos Productos con Precio (SUCAHERSA)
    public String getArticulo(){return articulo;}

    public String getNombre(){return nombre;}

    public double getMenudeo(){return menudeo;}

    public double getMayoreo(){return mayoreo;}

    public int getSoloMayoreo(){return solomayoreo;}

    //Metodos Productos Estrella
    public String getIdProducto(){return id;}

    public String getIdSCH(){return idsch;}

    public String getNombreSCH(){return nombre_sch;}

    public String getEstrella(){return estrella;}

    public String getImagen(){return imagen;}

}
