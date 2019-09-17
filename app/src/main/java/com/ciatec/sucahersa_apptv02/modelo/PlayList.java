package com.ciatec.sucahersa_apptv02.modelo;

public class PlayList {

    private String id;
    private String titulo;
    private String liga;
    private String vigencia;

    public PlayList
            (String id,
             String titulo,
             String liga,
             String vigencia)
    {
        this.id = id;
        this.titulo = titulo;
        this.liga = liga;
        this.vigencia = vigencia;
    }

    public String getIdPlaylist(){return id;}

    public String getTitulo(){return titulo;}

    public String getLiga(){return liga;}

    public String getEstrella(){return vigencia;}

}
