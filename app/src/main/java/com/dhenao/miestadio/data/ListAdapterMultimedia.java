package com.dhenao.miestadio.data;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dhenao.miestadio.R;


import java.util.List;

/**
 * Adaptador para multimedia usadas en la sección "multimedia"
 */
public class ListAdapterMultimedia extends RecyclerView.Adapter<ListAdapterMultimedia.ViewHolder> {

    private final List<DatosMultimedia> items;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView nombre;
        public TextView descripcion;
        public ImageView imagen;

        public ViewHolder(View v) {
            super(v);

            nombre = (TextView) v.findViewById(R.id.encabezado);
            descripcion = (TextView) v.findViewById(R.id.destalle);
            imagen = (ImageView) v.findViewById(R.id.imagenminiatura);
        }
    }


    public ListAdapterMultimedia(List<DatosMultimedia> items) {
        this.items = items;
    }

    public void clear(){
        items.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<DatosMultimedia> lista){
        items.addAll(lista);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.para_lista_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        DatosMultimedia item = items.get(i);

        Glide.with(viewHolder.itemView.getContext())
                .load(item.getMultimedia1IdDrawable())
                .centerCrop()
                .into(viewHolder.imagen);
        viewHolder.nombre.setText(item.getMultimedia1nombre());
        viewHolder.descripcion.setText(item.getMultimedia());
    }



}