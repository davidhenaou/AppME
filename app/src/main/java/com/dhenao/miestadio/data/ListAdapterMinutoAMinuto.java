package com.dhenao.miestadio.data;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dhenao.miestadio.R;

import java.util.List;

/**
 * Adaptador para multimedia usadas en la sección "multimedia"
 */
public class ListAdapterMinutoAMinuto extends RecyclerView.Adapter<ListAdapterMinutoAMinuto.ViewHolder> {

    private final List<DatosMinutoAMinuto> items;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public TextView txtminutoi;
        public TextView txtminutod;
        public TextView txttituloIzquierdo;
        public TextView txttituloDerecho;
        public TextView txttituloCentral;
        public ImageView imgIzquierda;
        public ImageView imgDerecha;
        public LinearLayout contenedorPpal;

        public ViewHolder(View v) { super(v);
            txtminutoi = (TextView) v.findViewById(R.id.txtminutoi);
            txtminutod = (TextView) v.findViewById(R.id.txtminutod);
            txttituloIzquierdo = (TextView) v.findViewById(R.id.mintituloizquierdo);
            txttituloDerecho = (TextView) v.findViewById(R.id.mintituloderecho);
            txttituloCentral = (TextView) v.findViewById(R.id.mintitulocentral);
            imgIzquierda = (ImageView) v.findViewById(R.id.imgIzquierda);
            imgDerecha = (ImageView) v.findViewById(R.id.imgDerecha);
            contenedorPpal = (LinearLayout) v.findViewById(R.id.linearRecuadro);
        }
    }


    public ListAdapterMinutoAMinuto(List<DatosMinutoAMinuto> items) {
        this.items = items;
    }

    public void clear(){
        items.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<DatosMinutoAMinuto> lista){
        items.addAll(lista);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_minutoaminuto, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        DatosMinutoAMinuto item = items.get(i);
        viewHolder.txtminutoi.setText(item.getMinutoi());
        viewHolder.txtminutod.setText(item.getMinutod());
        String accionahacer = item.getAccion().trim();

        viewHolder.txttituloIzquierdo.setText("");
        viewHolder.txttituloCentral.setText("");
        viewHolder.txttituloDerecho.setText("");

        viewHolder.imgIzquierda.setVisibility(View.INVISIBLE);
        viewHolder.imgDerecha.setVisibility(View.INVISIBLE);

        viewHolder.contenedorPpal.setBackgroundColor(Color.parseColor("#FFFFFFFF"));




        if (item.getMinutoi()=="" && item.getMinutod()==""){
            viewHolder.contenedorPpal.setBackgroundColor(Color.parseColor("#ecdf2c"));
            viewHolder.txttituloCentral.setText(item.getTitulo());
        }else{
            if (item.getMinutoi()=="") {
                viewHolder.txttituloDerecho.setText(item.getTitulo());
                viewHolder.imgDerecha.setVisibility(View.VISIBLE);
            }else{
                viewHolder.txttituloIzquierdo.setText(item.getTitulo());
                viewHolder.imgIzquierda.setVisibility(View.VISIBLE);
            }
        }



        if (accionahacer.equals("gol1") || accionahacer.equals("gol2")) {
            viewHolder.imgIzquierda.setImageResource(R.drawable.icogol);
            viewHolder.imgDerecha.setImageResource(R.drawable.icogol);
        }
        if (accionahacer.equals("fal1") || accionahacer.equals("fal2")) {
            viewHolder.imgIzquierda.setImageResource(R.drawable.icofalta);
            viewHolder.imgDerecha.setImageResource(R.drawable.icofalta);
        }
        if (accionahacer.equals("cor1") || accionahacer.equals("cor2")) {
            viewHolder.imgIzquierda.setImageResource(R.drawable.icocorner);
            viewHolder.imgDerecha.setImageResource(R.drawable.icocorner);
        }
        if (accionahacer.equals("troj1") || accionahacer.equals("troj2") || accionahacer.equals("taz1") || accionahacer.equals("taz2") || accionahacer.equals("tam1") || accionahacer.equals("tam2")) {
            viewHolder.imgIzquierda.setImageResource(R.drawable.icotarjeta);
            viewHolder.imgDerecha.setImageResource(R.drawable.icotarjeta);
        }
        if (accionahacer.equals("cam1") || accionahacer.equals("cam2")) {
            viewHolder.imgIzquierda.setImageResource(R.drawable.icocambio);
            viewHolder.imgDerecha.setImageResource(R.drawable.icocambio);
        }
        if (accionahacer.equals("pos1") || accionahacer.equals("pos2")) {
            viewHolder.imgIzquierda.setImageResource(R.drawable.icofueral);
            viewHolder.imgDerecha.setImageResource(R.drawable.icofueral);
        }


    }

}