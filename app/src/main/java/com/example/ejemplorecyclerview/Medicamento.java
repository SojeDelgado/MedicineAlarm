package com.example.ejemplorecyclerview;

import java.util.Date;

public class Medicamento {
    private String nombre;
    private Date horaToma;

    public Medicamento(String nombre, Date horaToma) {
        this.nombre = nombre;
        this.horaToma = horaToma;
    }

    public String getNombre() {
        return nombre;
    }

    public Date getHora() {
        return horaToma;
    }
}
