package com.example.ejemplorecyclerview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//Clase que nos servirá de puente para el list_element.xml (Para que se muestren los datos en el menú principal)
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<ListElement> mData;
    private LayoutInflater mInflater;
    private Context context;

    //Con esto podremos clickear el RecyclerView
    final ListAdapter.OnItemClickListener listener;

    final ListAdapter.OnItemLongClickListener longClickListener;

    public interface OnItemClickListener{
        void onItemClick(ListElement item);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(ListElement item);
    }

    public ListAdapter(List<ListElement> itemList, Context context, ListAdapter.OnItemClickListener listener, ListAdapter.OnItemLongClickListener longClickListener){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.listener = listener;
        this.longClickListener = longClickListener;
    }
    @Override
    public int getItemCount(){ return mData.size(); }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.list_element,null);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position){
        //holder.bindData(mData.get(position));

        final ListElement item = mData.get(position);
        holder.bindData(item);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(item);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                longClickListener.onItemLongClick(item);
                return true;
            }
        });
    }

    public void setItems(List<ListElement> items){ mData = items; }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iconImage;
        TextView nombre, medicamento, hora;
        Switch onOff;

        ViewHolder(View itemView){
            super(itemView);
            iconImage = itemView.findViewById(R.id.iconImageView);
            nombre = itemView.findViewById(R.id.nombreTextView);
            medicamento = itemView.findViewById(R.id.medicamentoTextView);
            hora = itemView.findViewById(R.id.horaTextView);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longClickListener.onItemLongClick(mData.get(getAdapterPosition()));
                    return true;
                }
            });

        }

        void bindData(final ListElement item){
            iconImage.setColorFilter(Color.parseColor(item.getColor()), PorterDuff.Mode.SRC_IN);
            nombre.setText(item.getNombre());
            medicamento.setText(item.getMedicamento());
            hora.setText(item.getHora());
            if (onOff != null) {
                onOff.setChecked(item.isOnOff());
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
