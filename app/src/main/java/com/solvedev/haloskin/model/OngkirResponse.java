package com.solvedev.haloskin.model;

import java.util.List;

public class OngkirResponse {
    private List<Ongkir> results;
    private OngkirResponse rajaongkir;

    public List<Ongkir> getResults() {
        return results;
    }

    public void setResults(List<Ongkir> results) {
        this.results = results;
    }

    public OngkirResponse getRajaongkir() {
        return rajaongkir;
    }

    public void setRajaongkir(OngkirResponse rajaongkir) {
        this.rajaongkir = rajaongkir;
    }
}
