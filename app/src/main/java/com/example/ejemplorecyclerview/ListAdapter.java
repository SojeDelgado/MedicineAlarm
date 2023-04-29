package com.example.ejemplorecyclerview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//Clase que nos servirá de puente para el list_element.xml (Para que se muestren los datos en el menú principal)
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<MedicamentoElement> mData;
    private LayoutInflater mInflater;
    private Context context;

    //Con esto podremos clickear el RecyclerView
    final ListAdapter.OnItemClickListener listener;

    final ListAdapter.OnItemLongClickListener longClickListener;

    public interface OnItemClickListener{
        void onItemClick(MedicamentoElement item);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(MedicamentoElement item);
    }

    public ListAdapter(List<MedicamentoElement> itemList, Context context, ListAdapter.OnItemClickListener listener, ListAdapter.OnItemLongClickListener longClickListener){
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
        View view = mInflater.from(parent.getContext()).inflate(R.layout.list_element,parent,false);
        return new ListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, final int position){
        holder.cv.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_trancition));
        //holder.bindData(mData.get(position));

        final MedicamentoElement item = mData.get(position);
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

    public void setItems(List<MedicamentoElement> items){ mData = items; }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iconImage;
        TextView nombre, medicamento, hora;
        CardView cv;
        Switch onOff;

        ViewHolder(View itemView){
            super(itemView);
            iconImage = itemView.findViewById(R.id.iconImageView);
            nombre = itemView.findViewById(R.id.nombreTextView);
            medicamento = itemView.findViewById(R.id.medicamentoTextView);
            hora = itemView.findViewById(R.id.horaTextView);
            cv = itemView.findViewById(R.id.cv);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longClickListener.onItemLongClick(mData.get(getAdapterPosition()));
                    return true;
                }
            });

        }

        void bindData(final MedicamentoElement item){
            iconImage.setColorFilter(Color.parseColor(item.getColor()), PorterDuff.Mode.SRC_IN);
            nombre.setText(item.getNombre());
            medicamento.setText(item.getMedicamento());

            int hora2 = item.getHora();
            int minutos = hora2%100;
            hora2 = hora2 / 100;
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hora2);
            calendar.set(Calendar.MINUTE, minutos);
            calendar.set(Calendar.SECOND, 0);
            Date date = calendar.getTime();
            DateFormat dateFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
            String horaFormateada = dateFormat.format(date);

            hora.setText(horaFormateada);
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
