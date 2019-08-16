package com.ciatec.sucahersa_apptv02.modelo;

public class ProductoPrecio {

    package com.ciatec.sucahersa_apptv02.modelo;

    public class Promocion {

        private String id;
        private String titulo;
        private String contenido;
        private String imagen;
        private String vigencia;

        public Promocion
                (String id,
                 String titulo,
                 String contenido,
                 String imagen,
                 String vigencia)
        {
            this.id = id;
            this.titulo = titulo;
            this.contenido = contenido;
            this.imagen = imagen;
            this.vigencia = vigencia;
        }

        public String getId(){return id;}

        public String getTitulo(){return titulo;}

        public String getContenido(){return contenido;}

        public String getImagen(){return imagen;}

        public String getVigencia(){return vigencia;}

    }
}
