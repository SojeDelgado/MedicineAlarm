package com.example.ejemplorecyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MedicamentosAdapter extends ArrayAdapter<MedicamentoElement> {
    public MedicamentosAdapter(Context context, ArrayList<MedicamentoElement> medicamentos) {
        super(context, R.layout.medicamento_item, medicamentos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obtener el medicamento actual
        MedicamentoElement medicamento = getItem(position);

        // Reutilizar la vista si es posible
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.medicamento_item, parent, false);
        }

        // Asignar los valores del medicamento a la vista
        TextView nombreTextView = convertView.findViewById(R.id.nombreTextView);
        TextView horaTextView = convertView.findViewById(R.id.horaTextView);

        nombreTextView.setText(medicamento.getNombre());
        horaTextView.setText((CharSequence) medicamento.getHora());

        // Devolver la vista actualizada
        return convertView;
    }
}
