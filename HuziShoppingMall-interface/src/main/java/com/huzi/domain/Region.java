package com.huzi.domain;

import java.io.Serializable;

public class Region implements Serializable {

    private int regionId;
    private String regionType;
    private String regionName;


    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public String getRegionType() {
        return regionType;
    }

    public void setRegionType(String regionType) {
        this.regionType = regionType;
    }

    public String getRegionIntroduction() {
        return regionName;
    }


    public void setRegionIntroduction(String regionIntroduction) {
        this.regionName = regionIntroduction;
    }


}
