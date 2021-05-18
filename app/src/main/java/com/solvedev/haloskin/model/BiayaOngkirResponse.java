package com.solvedev.haloskin.model;

import java.util.List;

public class BiayaOngkirResponse {
    private String service, description;
    private List<BiayaOngkir> cost;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<BiayaOngkir> getCost() {
        return cost;
    }

    public void setCost(List<BiayaOngkir> cost) {
        this.cost = cost;
    }
}
