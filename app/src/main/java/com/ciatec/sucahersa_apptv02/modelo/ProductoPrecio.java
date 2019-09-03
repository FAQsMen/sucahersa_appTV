package com.ciatec.sucahersa_apptv02.modelo;

public class ProductoPrecio {


    private String articulo;
    private String nombre;
    private double menudeo;
    private double mayoreo;
    private int solomayoreo;

    public ProductoPrecio
            (String articulo,
             String nombre,
             double menudeo,
             double mayoreo,
             int solomayoreo)
    {
        this.articulo = articulo;
        this.nombre = nombre;
        this.menudeo = menudeo;
        this.mayoreo = mayoreo;
        this.solomayoreo = solomayoreo;
    }

    public String getArticulo(){return articulo;}

    public String getNombre(){return nombre;}

    public double getMenudeo(){return menudeo;}

    public double getMayoreo(){return mayoreo;}

    public int getSoloMayoreo(){return solomayoreo;}
}
