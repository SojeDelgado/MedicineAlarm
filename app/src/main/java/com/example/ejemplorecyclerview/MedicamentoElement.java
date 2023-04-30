package com.example.ejemplorecyclerview;

import java.io.Serializable;
import java.util.Date;

public class MedicamentoElement implements Serializable {
    public int id;
    public String color;
    public String nombre;
    public String medicamento;
    public Date hora;
    public boolean onOff;

    public MedicamentoElement(int id, String color, String nombre, String medicamento, Date hora, Boolean onOff) {
        this.id = id;
        this.color = color;
        this.nombre = nombre;
        this.medicamento = medicamento;
        this.hora = hora;
        this.onOff = onOff;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public boolean isOnOff() {
        return onOff;
    }

    public void setOnOff(boolean onOff) {
        this.onOff = onOff;
    }
}
