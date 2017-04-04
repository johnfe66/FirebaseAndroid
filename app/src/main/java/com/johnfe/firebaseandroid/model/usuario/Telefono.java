package com.johnfe.firebaseandroid.model.usuario;

/**
 * Created by Johnfe Vargas on 2017-04-04.
 */

public class Telefono {

    private int idTelefono;
    private String TipoTelefono;
    private String numero;

    public int getIdTelefono() {
        return idTelefono;
    }

    public void setIdTelefono(int idTelefono) {
        this.idTelefono = idTelefono;
    }

    public String getTipoTelefono() {
        return TipoTelefono;
    }

    public void setTipoTelefono(String tipoTelefono) {
        TipoTelefono = tipoTelefono;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}
