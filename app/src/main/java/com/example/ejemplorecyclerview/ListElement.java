package com.example.ejemplorecyclerview;

import java.io.Serializable;

public class ListElement implements Serializable {
    public String color;
    public String nombre;
    public String medicamento;
    public String hora;
    public boolean onOff;

    public ListElement(String color, String nombre, String medicamento, String hora) {
        this.color = color;
        this.nombre = nombre;
        this.medicamento = medicamento;
        this.hora = hora;
        onOff = false;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public boolean isOnOff() {
        return onOff;
    }

    public void setOnOff(boolean onOff) {
        this.onOff = onOff;
    }
}
