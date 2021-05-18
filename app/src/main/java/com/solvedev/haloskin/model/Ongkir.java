package com.solvedev.haloskin.model;

import java.util.List;

public class Ongkir {
    private String province, province_id, city_name, city_id, subdistrict_name, subdistrict_id, type;
    private String code, name;
    private List<BiayaOngkirResponse> costs;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getSubdistrict_name() {
        return subdistrict_name;
    }

    public void setSubdistrict_name(String subdistrict_name) {
        this.subdistrict_name = subdistrict_name;
    }

    public String getSubdistrict_id() {
        return subdistrict_id;
    }

    public void setSubdistrict_id(String subdistrict_id) {
        this.subdistrict_id = subdistrict_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<BiayaOngkirResponse> getCosts() {
        return costs;
    }

    public void setCosts(List<BiayaOngkirResponse> costs) {
        this.costs = costs;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
