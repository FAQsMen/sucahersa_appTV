package com.ciatec.sucahersa_apptv02.ui;

import android.support.v7.widget.RecyclerView;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ciatec.sucahersa_apptv02.R;
import com.ciatec.sucahersa_apptv02.modelo.Producto;
import com.ciatec.sucahersa_apptv02.modelo.ProductoMerge;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adaptador del recycler view
 */

public class ProductoEstrellaAdaptador
        extends RecyclerView.Adapter<ProductoEstrellaAdaptador.ProductoEstrellaViewHolder>
        implements ItemClickListener
{
    /**
     * Lista de objetos {@link com.ciatec.sucahersa_apptv02.modelo.ProductoMerge} que representan la fuente de datos
     * de inflado
     */
    private List<ProductoMerge> items;

    /*
    Contexto donde actua el recycler view
    */
    private Context context;

    public ProductoEstrellaAdaptador(List<ProductoMerge> items, Context context) {
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public ProductoEstrellaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_productos, parent, false);
        return new ProductoEstrellaViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoEstrellaViewHolder viewHolder, int position) {
        //Nombre del producto
        viewHolder.txv_nombre.setText(items.get(position).getNombre());
        // Imagen del producto
        //String path = "https://www.ciatec.mx/images/home/d94802583f41eac743e99c5c0c78caac.png";
        String path = items.get(position).getImagen();
        Picasso.get().load(path)
                .error(R.mipmap.ic_isotipo)
                .into(viewHolder.imv_producto);
        //Precio del producto
        viewHolder.txv_precio.setText("$ " +
                String.valueOf(
                        items.get(position).getMayoreo()
                ) + " / $"+
                String.valueOf(items.get(position).getMenudeo()));
        if(items.get(position).getSoloMayoreo() == 1){
            viewHolder.txv_soloMayoreo.setText("SoloMayoreo");
        }else{
            viewHolder.txv_soloMayoreo.setText("Mayoreo/Menudeo");
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Sobrescritura del método de la interfaz {@link ItemClickListener}
     *
     * @param view     item actual
     * @param position posición del item actual
     */
    @Override
    public void onItemClick(View view, int position) {


    }

    public static class ProductoEstrellaViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        // Campos respectivos de un item
        public TextView txv_nombre;
        public TextView txv_precio;
        public TextView txv_soloMayoreo;
        public ImageView imv_producto;
        public ItemClickListener listener;

        public ProductoEstrellaViewHolder(View v, ItemClickListener listener) {
            super(v);
            txv_nombre = (TextView) v.findViewById(R.id.item_NombreProducto);
            txv_precio = (TextView) v.findViewById(R.id.item_Precio);
            txv_soloMayoreo = (TextView) v.findViewById(R.id.item_SoloMayoreo);
            imv_producto = (ImageView) v.findViewById(R.id.img_Producto);
            this.listener = listener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v, getAdapterPosition());
        }
    }
}

interface ItemClickListener {
    void onItemClick(View view, int position);
}


