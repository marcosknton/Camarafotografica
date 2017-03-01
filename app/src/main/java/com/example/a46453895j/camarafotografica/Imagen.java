package com.example.a46453895j.camarafotografica;

import java.io.Serializable;

/**
 * Created by 46453895j on 17/02/17.
 */

public class Imagen implements Serializable {

    private String rutaimagen;

    public Imagen(){}


    public Imagen(String rutaimagen) {

        this.rutaimagen = rutaimagen;
    }

    public String getRutaimagen() {
        return rutaimagen;
    }

    public void setRutaimagen(String rutaimagen) {
        this.rutaimagen = rutaimagen;
    }
}
