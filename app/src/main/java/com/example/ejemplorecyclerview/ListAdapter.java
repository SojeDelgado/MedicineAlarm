package com.example.ejemplorecyclerview;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
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

    private SharedPreferences mSharedPreferences;

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
        this.mSharedPreferences = context.getSharedPreferences("myprefs", Context.MODE_PRIVATE);
    }

    @Override
    public int getItemCount(){ return mData.size(); }

    public void updateList(List<MedicamentoElement> newList){
        mData.clear(); //Limpiamos la lista actual
        mData.addAll(newList); //Añadimos los nuevos elementos
        notifyDataSetChanged(); //Notificamos al adaptador que los datos han cambiado
    }

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

        /*if (holder.onOff != null) {
            holder.onOff.setChecked(item.isOnOff());
        }*/
        if (holder.onOff != null) {
            boolean switchState = mSharedPreferences.getBoolean("switch_state_" + item.getId(), false);
            holder.onOff.setChecked(switchState);
        }
        //holder.onOff.setChecked(item.isOnOff()); // Establece el estado del Switch
        Switch switchview = holder.onOff;
        if(switchview != null) {
            switchview.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.setOnOff(isChecked); // Actualiza el estado del switch en el objeto MedicamentoElement
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putBoolean("switch_state_" + item.getId(), isChecked);
                    editor.apply();
                }
            });
        }
    }

    public void setItems(List<MedicamentoElement> items){ mData = items; }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iconImage;
        TextView nombre, medicamento, hora, cantidad;
        CardView cv;
        Switch onOff;
        int id;

        ViewHolder(View itemView){
            super(itemView);
            iconImage = itemView.findViewById(R.id.iconImageView);
            nombre = itemView.findViewById(R.id.nombreTextView);
            cantidad = itemView.findViewById(R.id.cantidadTextView);
            medicamento = itemView.findViewById(R.id.medicamentoTextView);
            hora = itemView.findViewById(R.id.horaTextView);
            cv = itemView.findViewById(R.id.cv);
            onOff = itemView.findViewById(R.id.onOffSwitch);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longClickListener.onItemLongClick(mData.get(getAdapterPosition()));
                    return true;
                }
            });

        }

        void bindData(final MedicamentoElement item){
            iconImage.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
            nombre.setText(item.getNombre());
            cantidad.setText(String.valueOf(item.getCantidad()) + " gr");
            item.setCantidad(0);
            medicamento.setText(item.getMedicamento());

            Date hora = item.getHora();
            DateFormat dateFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
            String horaFormateada = dateFormat.format(hora);

            this.hora.setText(horaFormateada);
            if (onOff != null) {
                onOff.setChecked(item.isOnOff());
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item);
                }
            });
            id = item.getId();
        }
    }
}
